package com.tenswquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {//这个是客户方 解析令牌  对 itcast进行解析
//        try {
//            Claims itcast = Jwts.parser().setSigningKey("itcast")
//                    .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLmiJEiLCJpYXQiOjE1OTYxOTM2NzAsImV4cCI6MTU5NjE5MzczMH0.LMNtMslpysxeOUJPcOMXZnnn_G51EslfF0hN2_Jh0UY")
//                    .getBody();
//            System.out.println("用户id="+itcast.getId()+"--用户名="+itcast.getSubject()+"--登录时间="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(itcast.getIssuedAt())+"--过期时间是"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(itcast.getExpiration()));
//        }catch (Exception e){
//
//        }

        Claims itcast = Jwts.parser().setSigningKey("itcast")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLmiJEiLCJpYXQiOjE1OTYxOTQ5OTYsImV4cCI6MTU5NjE5NTA1Niwicm9sZSI6ImFkbWluIn0.f8Oy5vKjVM1GSQ_KDAKVlQhF6ofxernUjv56tjYnTA0")
                .getBody();
        System.out.println("用户id="+itcast.getId()+"--用户名="+itcast.getSubject()+"--登录时间="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(itcast.getIssuedAt())+"--过期时间是"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(itcast.getExpiration())+"用户角色"+itcast.get("role"));
        System.out.println(itcast);

    }
}
