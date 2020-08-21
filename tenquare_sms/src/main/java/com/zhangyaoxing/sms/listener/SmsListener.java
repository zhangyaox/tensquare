package com.zhangyaoxing.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.zhangyaoxing.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")//监听的是 sms  git数据测试
public class SmsListener {//消息的消费  就是客户  其实可以全写  tensquare_user的  这个是练习为了看的具体就下这里

    @Autowired
    private SmsUtil smsUtil;//调用 阿里云的 短信验证的第三方接口

    @Value("${aliyun.sms.template_code}")//调用配置的 数据
    private String template_code;

    @Value("${aliyun.sms.sign_name}")//调用配置的 数据
    private String sign_name;

    @RabbitHandler//该方法是监听消息队列的消息
    public void executeSms(Map<String,String> map){//因为我之前传递的消息是 map类型的
        System.out.println("手机号"+map.get("mobile")+"验证码"+map.get("num"));
        try {
            smsUtil.sendSms(map.get("mobile"),template_code,sign_name,"{\"checkcode\":\""+map.get("num")+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
