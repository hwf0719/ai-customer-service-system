package com.example.aics.interceptor;

import com.example.aics.config.JwtProperties;
import com.example.aics.context.BaseContext;
import com.example.aics.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws  Exception{
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String token=request.getHeader(jwtProperties.getTokenName());
        log.info("tokenName: {}", jwtProperties.getTokenName());
        log.info("token: {}", token);

        // 去掉 "Bearer " 前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try{
            Long userId=jwtUtil.parseToken(token);
            log.info("userId: {}", userId);
            BaseContext.setCurrentId(userId);
            return true;
        }catch (Exception e){
            log.error("token解析失败", e);
            response.setStatus(401);
            return false;
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除ThreadLocal
        BaseContext.removeCurrentId();
    }
}
