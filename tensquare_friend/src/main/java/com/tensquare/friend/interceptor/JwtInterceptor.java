package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {//拦截器

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("刚刚经过了拦截器");
        //用拦截器对 令牌进行检验  不管结果怎么样都放行  如果检验出是管理员就把消息存入request
        String authorization = request.getHeader("Authorization");
        if (authorization!=null&&!"".equals(authorization)){//非空进入下一阶段
            if (authorization.startsWith("Bearer ")){//内容为Bearer+空格+token  startsWith("Bearer ")判断是不是以Bearer+空格开头
                //符合格式
                //前面都过了 表示非空 格式正确  先取到token
                String token = authorization.substring(7);//从第7个开始截取  就是Bearer 后面的

                try {//解析token  防止有人乱写token
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");//按用户名 得到用户  然后判断这个用户是不是空 是不是admin(管理员)
                    if (roles!=null&&roles.equals("admin")){//非空 而且是管理员 request存入数据
                        request.setAttribute("claims_admin",claims);//是管理员  存入令牌
                    }
                    if (roles!=null&&roles.equals("user")){//非空 而且是用户 request存入数据
                        request.setAttribute("claims_user",claims);//是用户  存入令牌
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确");//如果Claims claims = jwtUtil.parseJWT(token); 有异常表名token有错误
                }
            }
        }


        return true;
    }
}
