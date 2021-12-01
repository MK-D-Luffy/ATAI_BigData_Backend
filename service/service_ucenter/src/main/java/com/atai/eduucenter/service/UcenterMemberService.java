package com.atai.eduucenter.service;

import com.atai.eduucenter.entity.UcenterMember;
import com.atai.eduucenter.entity.vo.ChangeMobileOrEmailVo;
import com.atai.eduucenter.entity.vo.ChangePwdVo;
import com.atai.eduucenter.entity.vo.LoginVo;
import com.atai.eduucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

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

    //判断昵称是否重复
    Boolean checkNickname(@PathVariable String nickname);

    //判断手机号是否重复
    Boolean checkPhone(String mobile);

    //判断邮箱是否重复
    Boolean checkEmail(String email);

    //更改密码
    void changePwd(ChangePwdVo changePwdVo);

    boolean changeMobileOrEmail(ChangeMobileOrEmailVo changeMobileOrEmailVo, String id);

    boolean validateSecurity(ChangeMobileOrEmailVo changeMobileOrEmailVo, String id);
}
