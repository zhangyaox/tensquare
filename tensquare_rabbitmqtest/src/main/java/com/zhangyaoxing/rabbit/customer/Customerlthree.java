package com.zhangyaoxing.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "kudingyu")//标注消费者  queues = "itcast"指定哪一个队列
public class Customerlthree {//消费者
    @RabbitHandler
    public void getMsg(String msg){
        System.out.println("kudingyu消费了消息是"+msg);
    }
}
