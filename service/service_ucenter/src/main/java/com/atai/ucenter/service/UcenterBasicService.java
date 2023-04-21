package com.atai.ucenter.service;

import com.atai.ucenter.entity.UcenterBasic;
import com.atai.ucenter.entity.vo.ChangeMobileOrEmailVo;
import com.atai.ucenter.entity.vo.ChangePwdVo;
import com.atai.ucenter.entity.vo.LoginVo;
import com.atai.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author baiyunRain
 * @since 2023-04-17
 */
public interface UcenterBasicService extends IService<UcenterBasic> {
    //登录的方法
    String login(LoginVo loginVo);

    //注册的方法
    void register(RegisterVo registerVo);

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

    Map<String, Object> getUserListPage(Page<UcenterBasic> compPage);
}
