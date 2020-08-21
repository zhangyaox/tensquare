package com.zhangyaoxing.test;

import com.zhangyaoxing.rabbit.ApplicationRabbit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRabbit.class)
public class ProductTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void send(){
        rabbitTemplate.convertAndSend("itcast","直接测试2");
    }

    @Test
    public void sendtow(){
        rabbitTemplate.convertAndSend("chuanzhi","","分裂模式测试");
    }

    @Test//主题模式
    public void sendthree(){
        rabbitTemplate.convertAndSend("topic84","good.log","主题模式测试");
    }
}
