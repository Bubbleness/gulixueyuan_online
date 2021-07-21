package com.dhu.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhu.commonutils.JwtUtils;
import com.dhu.commonutils.MD5;
import com.dhu.educenter.entity.UcenterMember;
import com.dhu.educenter.entity.vo.RegisterVo;
import com.dhu.educenter.mapper.UcenterMemberMapper;
import com.dhu.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhu.servicebase.exceptionhandler.GuliException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Yushui
 * @since 2021-06-15
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
    * @Description: 登录
    * @Param:
    * @return:
    * @Author: Yushui
    * @Date: 2021/6/15
    */
    @Override
    public String login(UcenterMember member) {

        //获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"用户名或者密码不能为空，登录失败！");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);

        if(mobileMember == null)
            throw new GuliException(20001,"此用户名不存在，登录失败！！！ ");

        //将输入的密码进行加密，在于数据库进行比较
        //使用 MD5 进行加密 ： MD5只能加密不能解密
        if(!MD5.encrypt(password).equals(mobileMember.getPassword()))
            throw new GuliException(20001,"密码错误，登录失败！！！ ");

        //判断用户是否被禁用
        if(mobileMember.getIsDisabled())
            throw new GuliException(20001,"用户被禁用，登录失败！！！ ");

        //登录成功
        //生成token
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }



    @Override
    public void register(RegisterVo registerVo) {

        //获取注册的数据
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
            || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败！");
        }

        //判断验证码
        //获取redis中的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode))
            throw new GuliException(20001,"注册失败！");

        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0)
            throw new GuliException(20001,"注册失败！");

        //注册成功
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://edu76.oss-cn-shanghai.aliyuncs.com/2021/05/17/fac036ad52ee4265ac6381dcb727836bfile.png");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }
}
