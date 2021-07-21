package com.atai.eduucenter.service;

import com.atai.eduucenter.entity.UcenterMember;
import com.atai.eduucenter.entity.vo.ChangeVo;
import com.atai.eduucenter.entity.vo.LoginVo;
import com.atai.eduucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员表 服务类
 * @author ZengJinming
 * @since 2020-04-09
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录的方法
    String login(LoginVo loginVo);

    //注册的方法
    void register(RegisterVo registerVo);

    //根据openid判断是否有相同微信数据
    UcenterMember getOpenIdMember(String openid);

    //查询某天注册人数
    Integer countRegisterDay(String day);

    //更改密码
    void changePasswd(ChangeVo changeVo);

    //根据邮箱或手机号获取验证码
    String getValidateCodeByEmailOrMobile(String emailOrMobile);
}
