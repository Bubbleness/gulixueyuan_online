package com.dhu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.dhu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    @Override
    public boolean send(Map<String, Object> param, String phone) {

        if(StringUtils.isEmpty(phone))
            return false;

        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI5tNR5WBUQj51zWMW6yZF", "raYw28nXXWxgJPD3ENW2gNoiPRAXsR");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关参数
        //固定内容不能改
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送的相关参数
        request.putQueryParameter("PhoneNumbers", phone);
        //申请的阿里云短信服务的签名的名称
        request.putQueryParameter("SignName", "我的谷粒在线教育网站");
        //申请的阿里云短信服务的模板code
        request.putQueryParameter("TemplateCode", "SMS_218164636");
        //转为json格式
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        //最终发送
        try {

            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();

        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
