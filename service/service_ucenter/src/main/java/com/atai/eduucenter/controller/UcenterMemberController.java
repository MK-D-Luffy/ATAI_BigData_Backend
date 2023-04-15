package com.atai.eduucenter.controller;


import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.result.R;
import com.atai.commonutils.result.ResultCodeEnum;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.eduucenter.entity.UcenterMember;
import com.atai.eduucenter.entity.vo.ChangeMobileOrEmailVo;
import com.atai.eduucenter.entity.vo.ChangePwdVo;
import com.atai.eduucenter.entity.vo.LoginVo;
import com.atai.eduucenter.entity.vo.RegisterVo;
import com.atai.eduucenter.service.UcenterMemberService;
import com.atai.servicebase.exceptionhandler.MSException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 会员表 前端控制器
 *
 * @author ZengJinming
 * @since 2020-04-09
 */
@Api ("登录和注册")
@RestController
@RequestMapping ("/eduucenter/ucenter-member")
//@CrossOrigin
public class UcenterMemberController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UcenterMemberController.class);
    @Autowired
    UcenterMemberService ucenterMemberService;

    //登录
    @ApiOperation (value = "用户登录")
    @PostMapping ("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token;
        try {
            token = ucenterMemberService.login(loginVo);
        } catch (MSException e) {
            return R.error().code(e.getCode()).message(e.getMessage());
        } catch (Exception e) {
            return R.error().message(e.getMessage());
        }
        return R.success().data("token", token).message("登录成功");
    }

    //注册
    @ApiOperation (value = "用户注册")
    @PostMapping ("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.success().message("注册成功");
    }

    //判断昵称是否重复
    @ApiOperation (value = "判断昵称是否重复(返回true为重复)")
    @GetMapping ("checkNickname/{nickname}")
    public R checkNickname(@PathVariable String nickname) {
        Boolean flag = ucenterMemberService.checkNickname(nickname);
        return R.success().data("flag", flag);
    }

    //判断手机号是否重复
    @ApiOperation (value = "判断手机号是否重复(返回true为重复)")
    @GetMapping ("checkPhone/{mobile}")
    public R checkPhone(@PathVariable String mobile) {
        Boolean flag = ucenterMemberService.checkPhone(mobile);
        return R.success().data("flag", flag);
    }

    //判断邮箱是否重复
    @ApiOperation (value = "判断邮箱是否重复(返回true为重复)")
    @GetMapping ("checkEmail/{email}")
    public R checkEmail(@PathVariable String email) {
        Boolean flag = ucenterMemberService.checkEmail(email);
        return R.success().data("flag", flag);
    }

    //更改密码
    @ApiOperation (value = "更改密码")
    @PostMapping ("changePwd")
    public R changePassword(@RequestBody ChangePwdVo changePwdVo) {
        ucenterMemberService.changePwd(changePwdVo);
        return R.success().message("修改密码成功");
    }

    //根据token获取用户信息
    @ApiOperation (value = "根据token获取登录信息")
    @GetMapping ("getMemberInfo")
    public R getLoginInfo(HttpServletRequest request) {

        try {
            //调用jwt工具类的方法。根据request对象获取头信息，返回用户id
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.success().data("userInfo", jwtInfo);
        } catch (Exception e) {
            log.error("解析用户信息失败：" + e.getMessage());
            throw new MSException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    //根据用户id获取用户信息
    @ApiOperation (value = "根据用户id获取用户信息 个人中心用")
    @PostMapping ("getUserInfo/{id}")
    public R getUserInfo(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        return R.success().data("memberInfo", member);
    }

    //为其他微服务模块提供”根据用户id获取用户信息“的功能
    @ApiOperation (value = "根据用户id获取用户信息 个人中心用")
    @PostMapping ("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //用户信息修改功能
    @ApiOperation (value = "用户信息修改")
    @PostMapping ("updateMember")
    public R updateMember(@RequestBody UcenterMember ucenterMember) {
        boolean flag = ucenterMemberService.updateById(ucenterMember);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //查询某天注册人数
//    @ApiOperation (value = "查询某天注册人数")
//    @GetMapping ("countRegister/{day}")
//    public R countRegister(@PathVariable String day) {
//        Integer count = ucenterMemberService.countRegisterDay(day);
//        return R.success().data("countRegister", count);
//    }

    //修改邮箱或手机号
    @ApiOperation (value = "修改邮箱或手机号")
    @PostMapping ("changeMobileOrEmail/{id}")
    public R changeMobileOrEmail(@RequestBody ChangeMobileOrEmailVo changeMobileOrEmailVo,
                                 @PathVariable String id) {
        boolean flag = ucenterMemberService.changeMobileOrEmail(changeMobileOrEmailVo, id);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //安全性校验
    @ApiOperation (value = "安全性校验")
    @PostMapping ("validateSecurity/{id}")
    public R validateSecurity(@RequestBody ChangeMobileOrEmailVo changeMobileOrEmailVo,
                              @PathVariable String id) {
        boolean flag = ucenterMemberService.validateSecurity(changeMobileOrEmailVo, id);
        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //分页查询用户的方法
    @ApiOperation (value = "条件查询带分页查询比赛")
    @PostMapping ("getUserListPage/{page}/{limit}")
    public R getUserListPage(@PathVariable long page, @PathVariable long limit) {

        Page<UcenterMember> compPage = new Page<>(page, limit);
        Map<String, Object> map = ucenterMemberService.getUserListPage(compPage);
        //返回分页所有数据
        return R.success().data(map);
    }

    //逻辑删除比赛的方法
    @ApiOperation (value = "逻辑删除比赛")
    @DeleteMapping ("/{id}")
    public R removeCompetition(@ApiParam (name = "id", value = "比赛ID", required = true)
                               @PathVariable String id) {
        // 将Mysql中的删除
        Boolean flag = ucenterMemberService.removeById(id);

        if (flag) {
            return R.success();
        } else {
            return R.error();
        }
    }
}
