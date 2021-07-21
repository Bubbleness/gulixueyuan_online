package com.dhu.msmservice.controller;

import com.dhu.commonutils.R;
import com.dhu.msmservice.service.MsmService;
import com.dhu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@EnableDiscoveryClient
public class MsmController {

    @Autowired
    private MsmService msmService;

    //SpringBoot封装的Redis
    //通过设置Redis的过期时间来完成验证码在一定时间内有效的功能
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /** 
    * @Description: 根据传递的电话号码进行发送短信验证码
    * @Param:  
    * @return:  
    * @Author: Yushui
    * @Date: 2021/6/15 
    */
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone){

        //1. 从redis中取code验证码
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code))
            return R.ok();

        //生成随机值，传递给阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        //调用service发送短信的方法
        boolean isSend = msmService.send(param,phone);
        if(isSend){

            //发送成功就把验证码放到redis里面去
            //同时设置redis的有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }
        else
            return R.error().message("短信发送失败！");
    }
}
