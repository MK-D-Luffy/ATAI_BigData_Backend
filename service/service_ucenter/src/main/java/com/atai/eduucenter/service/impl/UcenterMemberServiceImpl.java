package com.atai.eduucenter.service.impl;

import com.atai.commonutils.result.ResultCodeEnum;
import com.atai.commonutils.util.*;
import com.atai.eduucenter.entity.UcenterMember;
import com.atai.eduucenter.entity.vo.ChangeMobileOrEmailVo;
import com.atai.eduucenter.entity.vo.ChangePwdVo;
import com.atai.eduucenter.entity.vo.LoginVo;
import com.atai.eduucenter.entity.vo.RegisterVo;
import com.atai.eduucenter.mapper.UcenterMemberMapper;
import com.atai.eduucenter.service.UcenterMemberService;
import com.atai.servicebase.exceptionhandler.MSException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 会员表 服务实现类
 *
 * @author ZengJinming
 * @since 2020-04-09
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @param mobileOrEmail 手机号或邮箱
     * @param validateStr   用于统计校验次数的字符串
     * @param code          验证码
     */
    public void validateCode(String mobileOrEmail, String validateStr, String code) {
        //增加验证码使用次数的校验
        int validateCount = 1;
        String countStr = redisTemplate.opsForValue().get(validateStr + "ValidateCount");
        if (countStr != null) {
            validateCount = Integer.parseInt(countStr);
            if (validateCount >= 5) {
                redisTemplate.delete(mobileOrEmail);
                redisTemplate.delete(validateStr + "ValidateCount");
                throw new MSException(20001, "验证码错误次数已经超过最大次数，已重置，请重新获取验证码！");
            }
        }

        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobileOrEmail);
        //判断验证码
        if (!code.equals(redisCode)) {
            validateCount++;
            redisTemplate.opsForValue().set(validateStr + "ValidateCount", validateCount + "", 5, TimeUnit.MINUTES);
            throw new MSException(20001, "验证🐎有误！注册失败！");
        } else {
            //校验成功后,删除验证码
            redisTemplate.delete(mobileOrEmail);
        }
    }

    //登录的方法
    @Override
    public String login(LoginVo loginVo) {
        String mobileOrEmail = loginVo.getMobileOrEmail();
        String password = loginVo.getPassword();

        try {
            password = AESUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            throw new MSException(20001, "密码出现错误");
        }

        //校验：参数是否合法
        if (StringUtils.isEmpty(mobileOrEmail) || StringUtils.isEmpty(password)) {
            throw new MSException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验手机号或邮箱是否存在
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        if (FormUtils.isMobile(mobileOrEmail)) {
            queryWrapper.eq("mobile", mobileOrEmail);
        } else if (FormUtils.isEmail(mobileOrEmail)) {
            queryWrapper.eq("email", mobileOrEmail);
        } else {
            throw new MSException(ResultCodeEnum.PARAM_ERROR);
        }

        //获取用户信息
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        if (member == null) {
            throw new MSException(ResultCodeEnum.LOGIN_MOBILE_EMAIL_ERROR);
        }


        int loginFailedCount = 0;
        String timeCounter = redisTemplate.opsForValue().get(mobileOrEmail + "timeCounter");
        if (timeCounter != null) {
            throw new MSException(20001, "账号冻结中,无法登录");
        }

        String countStr = redisTemplate.opsForValue().get(mobileOrEmail + "LoginFailedCount");
        if (countStr != null) {
            loginFailedCount = Integer.parseInt(countStr);
            if (loginFailedCount == 5) {
                loginFailedCount++;
                redisTemplate.opsForValue().set(mobileOrEmail + "LoginFailedCount", loginFailedCount + "");
                redisTemplate.opsForValue().set(mobileOrEmail + "timeCounter", "value", 2, TimeUnit.MINUTES);
                throw new MSException(20001, "您的账号已被冻结,请于两分钟后重试");
            } else if (loginFailedCount == 8) {
                loginFailedCount++;
                redisTemplate.opsForValue().set(mobileOrEmail + "LoginFailedCount", loginFailedCount + "");
                redisTemplate.opsForValue().set(mobileOrEmail + "timeCounter", "value", 10, TimeUnit.MINUTES);
                throw new MSException(20001, "您的账号已被冻结,请于十分钟后重试");
            } else if (loginFailedCount == 12) {
                loginFailedCount++;
                redisTemplate.opsForValue().set(mobileOrEmail + "LoginFailedCount", loginFailedCount + "");
                redisTemplate.opsForValue().set(mobileOrEmail + "timeCounter", "value", 1, TimeUnit.HOURS);
                throw new MSException(20001, "您的账号已被冻结,请于一小时后重试");
            } else if (loginFailedCount == 16) {
//                loginFailedCount++;
//                redisTemplate.opsForValue().set(mobileOrEmail + "LoginFailedCount", loginFailedCount + "");
                redisTemplate.delete(mobileOrEmail + "LoginFailedCount");
                redisTemplate.opsForValue().set(mobileOrEmail + "timeCounter", "value", 1, TimeUnit.DAYS);
                throw new MSException(20001, "您的账号已被冻结,请于一天后重试");
            }
        }

        //校验密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            loginFailedCount++;
            redisTemplate.opsForValue().set(mobileOrEmail + "LoginFailedCount", loginFailedCount + "");
            throw new MSException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //校验用户是否被禁用
        if (member.getIsDisabled()) {
            throw new MSException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        //登录:生成token
        JwtInfo info = new JwtInfo();
        info.setId(member.getId());
        info.setNickname(member.getNickname());
        info.setAvatar(member.getAvatar());
        //删除登录失败信息
        redisTemplate.delete(mobileOrEmail + "LoginFailedCount");

        String jwtToken = JwtUtils.getJwtToken(info, 604800);

        return jwtToken;
    }

    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据 校验参数
        String nickname = registerVo.getNickname(); //昵称
        String mobile = registerVo.getMobile(); //手机号
        String email = registerVo.getEmail(); //邮箱
        String code = registerVo.getCode(); //验证码
        String password = registerVo.getPassword(); //密码
        String codeType = registerVo.getCodeType();

        //非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new MSException(20001, "注册失败！");
        }

        String mobileOrEmail;
        if ("1".equals(codeType)) {
            mobileOrEmail = mobile;
        } else {
            mobileOrEmail = email;
        }

        //校验验证码
        validateCode(mobileOrEmail, mobileOrEmail + "Register", code);

        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setEmail(email);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//密码需要进行MD5加密
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://atai-bigdata.oss-cn-chengdu.aliyuncs.com/2021/08/24/e1a697a90c4d43ed9f1ba6b7dc31d5281.png");
        baseMapper.insert(member);
    }

    //根据openid判断是否有相同微信数据
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return baseMapper.selectOne(wrapper);
    }

    //查询某天注册人数
//    @Override
//    public Integer countRegisterDay(String day) {
//        return baseMapper.countRegister(day);
//    }

    //判断昵称是否重复(返回true为重复)
    @Override
    public Boolean checkNickname(String nickname) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", nickname);
        return baseMapper.selectCount(wrapper) > 0;
    }

    //判断手机号是否重复(返回true为重复)
    @Override
    public Boolean checkPhone(String mobile) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        return baseMapper.selectCount(wrapper) > 0;
    }

    //判断邮箱是否重复(返回true为重复)
    @Override
    public Boolean checkEmail(String email) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return baseMapper.selectCount(wrapper) > 0;
    }

    //更改密码
    @Override
    public void changePwd(ChangePwdVo changePwdVo) {
        //获取注册的数据 校验参数
        String id = changePwdVo.getId(); //用户id
        String mobile = changePwdVo.getMobile(); //手机号
        String email = changePwdVo.getEmail(); //邮箱
        String code = changePwdVo.getCode(); //验证码
        String password = changePwdVo.getPassword(); //密码

        //密码解密
        try {
            password = AESUtil.desEncrypt(password).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String mobileOrEmail = "";
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        UcenterMember ucenterMember = baseMapper.selectById(id);

        //非空判断
        if (!StringUtils.isEmpty(code)) {
            if (!StringUtils.isEmpty(email)) {
                mobileOrEmail = email;
                if (!email.equals(ucenterMember.getEmail())) {
                    throw new MSException(20001, "验证邮箱非本人邮箱");
                }
                wrapper.eq("email", email);
            }
            if (!StringUtils.isEmpty(mobile)) {
                mobileOrEmail = mobile;
                if (!mobile.equals(ucenterMember.getMobile())) {
                    throw new MSException(20001, "验证手机号非本人手机号");
                }
                wrapper.eq("mobile", mobile);
            }
        }

        //校验验证码
        validateCode(mobileOrEmail, mobileOrEmail + "ChangePwd", code);

        UcenterMember member = baseMapper.selectOne(wrapper);
        member.setPassword(MD5.encrypt(password));//密码需要进行MD5加密
        baseMapper.updateById(member);
    }

    @Override
    public boolean changeMobileOrEmail(ChangeMobileOrEmailVo changeMobileOrEmailVo, String id) {
        //获取修改邮箱手机号的信息 校验参数
        String mobile = changeMobileOrEmailVo.getMobile(); //手机号
        String email = changeMobileOrEmailVo.getEmail(); //邮箱
        String code = changeMobileOrEmailVo.getCode(); //验证码

        String mobileOrEmail = "";

        //非空判断
        if (!StringUtils.isEmpty(code)) {
            if (!StringUtils.isEmpty(email)) {
                mobileOrEmail = email;
            }
            if (!StringUtils.isEmpty(mobile)) {
                mobileOrEmail = mobile;
            }
        }

        //校验验证码
        validateCode(mobileOrEmail, mobileOrEmail + "ChangeMobileOrEmail", code);

        UcenterMember ucenterMember = baseMapper.selectById(id);
        if (!StringUtils.isEmpty(email)) {
            ucenterMember.setEmail(email);
        }
        if (!StringUtils.isEmpty(mobile)) {
            ucenterMember.setMobile(mobile);
        }
        return baseMapper.updateById(ucenterMember) > 0;
    }

    @Override
    public boolean validateSecurity(ChangeMobileOrEmailVo changeMobileOrEmailVo, String id) {
        String mobile = changeMobileOrEmailVo.getMobile();
        String code = changeMobileOrEmailVo.getCode();

        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            throw new MSException(20001, "校验失败!!!");
        }

        UcenterMember ucenterMember = baseMapper.selectById(id);
        if (!mobile.equals(ucenterMember.getMobile())) {
            throw new MSException(20001, "手机号验证错误!!!");
        }

        //校验验证码
        validateCode(mobile, mobile + "ValidateSecurity", code);

        return true;
    }

    @Override
    public Map<String, Object> getUserListPage(Page<UcenterMember> userPage) {
        //把分页数据封装到userPage对象里去
        baseMapper.selectPage(userPage, null);

        List<UcenterMember> records = userPage.getRecords();
        long current = userPage.getCurrent();
        long pages = userPage.getPages();
        long size = userPage.getSize();
        long total = userPage.getTotal();
        boolean hasNext = userPage.hasNext();//下一页
        boolean hasPrevious = userPage.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }


}
