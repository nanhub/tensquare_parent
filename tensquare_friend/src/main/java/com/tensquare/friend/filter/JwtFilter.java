package com.tensquare.friend.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String head = request.getHeader("Authorization");

        //解析
        if(head!=null && !"".equals(head)){
            if(head.startsWith("Bearer ")){
                //得到token
                String token = head.substring(7);

                //对token验证
                try {

                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles !=null && roles.equals("admin")){
                        request.setAttribute("claims_admin",claims);
                    }
                    if(roles !=null && roles.equals("user")){
                        request.setAttribute("claims_user",claims);
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌有误");
                }
            }
        }
        return true;
    }
}
