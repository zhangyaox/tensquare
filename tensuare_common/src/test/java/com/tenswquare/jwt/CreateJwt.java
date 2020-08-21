package com.tenswquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwt {//用来生成令牌
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666")//载荷
                .setSubject("我")//载荷
                .setIssuedAt(new Date())//载荷
                .signWith(SignatureAlgorithm.HS256,"itcast")
                .setExpiration(new Date(new Date().getTime()+60000))
                .claim("role","admin");//头部
        System.out.println(jwtBuilder.compact());
    }
}
