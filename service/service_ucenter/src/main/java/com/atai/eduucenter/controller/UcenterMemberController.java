package com.atai.eduucenter.controller;


import com.atai.commonutils.ordervo.UcenterMemberOrder;
import com.atai.commonutils.result.R;
import com.atai.commonutils.result.ResultCodeEnum;
import com.atai.commonutils.util.JwtInfo;
import com.atai.commonutils.util.JwtUtils;
import com.atai.eduucenter.entity.UcenterMember;
import com.atai.eduucenter.entity.vo.ChangeVo;
import com.atai.eduucenter.entity.vo.LoginVo;
import com.atai.eduucenter.entity.vo.RegisterVo;
import com.atai.eduucenter.service.UcenterMemberService;
import com.atai.servicebase.exceptionhandler.MSException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员表 前端控制器
 * @author ZengJinming
 * @since 2020-04-09
 */
@Api(description="登录和注册")
@RestController
@RequestMapping("/eduucenter/ucenter-member")
@Slf4j
//@CrossOrigin
public class UcenterMemberController {
    @Autowired
    UcenterMemberService ucenterMemberService;

    //登录
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = ucenterMemberService.login(loginVo);
        return R.success().data("token", token).message("登录成功");
    }

    //注册
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.success().message("注册成功");
    }

    //更改密码
    @ApiOperation(value = "更改密码")
    @PostMapping("change")
    public R changePassword(@RequestBody ChangeVo changeVo) {
        ucenterMemberService.changePasswd(changeVo);
        return R.success().message("修改密码成功");
    }

    //根据token获取用户信息
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("getMemberInfo")
    public R getLoginInfo(HttpServletRequest request){

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
    @ApiOperation(value = "根据用户id获取用户信息 课程评论用")
    @PostMapping("getUserInfoOrder/{id}")
//    public R getUserInfoOrder(@PathVariable String id) {
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
//        return R.success().data("memberInfo",ucenterMemberOrder);
    }

    //根据用户id获取用户信息
    @ApiOperation(value = "根据用户id获取用户信息 个人中心用")
    @PostMapping("getUserInfo/{id}")
    public R getUserInfo(@PathVariable String id) {
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
 //       return ucenterMemberOrder;
        return R.success().data("memberInfo",ucenterMemberOrder);
    }

    //用户信息修改功能
    @ApiOperation(value = "用户信息修改")
    @PostMapping("updateMember")
    public R updateMember(@RequestBody UcenterMember ucenterMember) {
        boolean flag = ucenterMemberService.updateById(ucenterMember);
        if(flag) {
            return R.success();
        } else {
            return R.error();
        }
    }

    //查询某天注册人数
    @ApiOperation (value = "查询某天注册人数")
    @GetMapping ("countRegister/{day}")
    public R countRegister(@PathVariable String day) {
        Integer count = ucenterMemberService.countRegisterDay(day);
        return R.success().data("countRegister", count);
    }

    //根据邮箱或手机号获取验证码
    @ApiOperation (value = "根据邮箱或手机号获取验证码")
    @GetMapping ("getValidateCode/{emailOrMobile}")
    public R getValidateCode(@PathVariable String emailOrMobile) {
        String code = ucenterMemberService.getValidateCodeByEmailOrMobile(emailOrMobile);
        return R.success().data("code", code);
    }
}
