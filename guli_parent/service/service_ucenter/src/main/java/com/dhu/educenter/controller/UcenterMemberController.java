package com.dhu.educenter.controller;


import com.dhu.commonutils.JwtUtils;
import com.dhu.commonutils.R;
import com.dhu.educenter.entity.UcenterMember;
import com.dhu.educenter.entity.vo.RegisterVo;
import com.dhu.educenter.service.UcenterMemberService;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-06-15
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
    * @Description: 登录
    * @Param:
    * @return:
    * @Author: Yushui
    * @Date: 2021/6/15
    */
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member){

        //调用service方法实现登录
        //返回token值，使用jwt的方式生成
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }


    /** 
    * @Description: 注册
    * @Param:  
    * @return:  
    * @Author: Yushui
    * @Date: 2021/6/15 
    */
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }


    /** 
    * @Description: 根据token获取用户信息 
    * @Param:  
    * @return:  
    * @Author: Yushui
    * @Date: 2021/6/15 
    */
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){

        //调用jwt工具类方法
        //根据request对象获取头信息 返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }
}

