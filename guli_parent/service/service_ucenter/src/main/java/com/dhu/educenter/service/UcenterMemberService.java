package com.dhu.educenter.service;

import com.dhu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Yushui
 * @since 2021-06-15
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);
}
