package com.jarvins.inceptor;

import com.jarvins.entity.response.ErrorEnum;
import com.jarvins.entity.response.ErrorResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final String NOT_INTERCEPT_URI = "/jarvins-backend/login/login";
    //cookie记录
    private static final String COOKIE_NAME = "_UID";

    StringRedisTemplate redisTemplate;

    public AuthInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        if (NOT_INTERCEPT_URI.equals(request.getRequestURI())){
//            return true;
//        }
//        Cookie[] cookies = request.getCookies();
//        Optional<Cookie> match;
//        if (cookies != null &&
//                (match = Arrays.stream(request.getCookies()).filter(e -> e.getName().equals(COOKIE_NAME)).findFirst()).isPresent() &&
//                redisTemplate.opsForValue().get(match.get().getValue()) != null) {
//            return true;
//        }
//        //登录验证未通过
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.write(ErrorResponse.error(ErrorEnum.LOGIN_ERROR).toString().getBytes());
//        return false;
        return true;
    }
}
