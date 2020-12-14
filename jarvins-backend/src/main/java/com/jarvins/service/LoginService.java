package com.jarvins.service;

import com.jarvins.entity.response.ErrorResponse;
import com.jarvins.entity.Response;
import com.jarvins.entity.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.jarvins.entity.response.ErrorEnum.*;

@Slf4j
@Service
public class LoginService {

    StringRedisTemplate redisTemplate;

    //secretKey
    private static final String SECRETKEY = "secretKey";
    //cookie domain
    private static final String DOMAIN = "jarvins.com";
    //cookie maxAge
    private static final int COOKIE_MAX_AGE = 60 * 60 * 2;
    //cookie记录
    private static final String COOKIE_NAME = "_UID";
    //redis登录信息缓存时间
    private static final int SUCCESS_CACHE_TIME = 60 * 60;
    //ip黑名单
    private static final String BLACK_IP_PREFIX = "black_ip_";
    //黑名单缓存时效
    private static final int FAIL_CACHE_TIME = 60 * 10;

    public LoginService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Response login(HttpServletRequest request, HttpServletResponse response, String secretKey) {
        String remoteHost = request.getRemoteHost();
        //先检查ip把暴力破解
        String key = redisTemplate.opsForValue().get(BLACK_IP_PREFIX + remoteHost);
        String password = redisTemplate.opsForValue().get(SECRETKEY);
        if (key == null && !secretKey.equals(password)) {
            //登录失败，记录ip防止大量密码测试暴力破解
            redisTemplate.opsForValue().set(BLACK_IP_PREFIX + remoteHost, "1", FAIL_CACHE_TIME, TimeUnit.SECONDS);
            return ErrorResponse.error(AUTH_FAIL_ERROR);
        } else if (!secretKey.equals(password)) {
            if (Integer.parseInt(key) == 5) {
                log.warn("LoginService: remote host:[{}],login fail 5 times.", remoteHost);
                return ErrorResponse.error(MAX_AUTH_ERROR);
            } else {
                //记录 +1
                redisTemplate.opsForValue().increment(BLACK_IP_PREFIX + remoteHost);
                return ErrorResponse.error(AUTH_FAIL_ERROR);
            }
        } else {
            //登录成功
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //设置cookie
            Cookie cookie = new Cookie(COOKIE_NAME, uuid);
            cookie.setHttpOnly(true);
            cookie.setDomain(DOMAIN);
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
            //redis登录有效期记录
            redisTemplate.opsForValue().set(uuid, remoteHost, SUCCESS_CACHE_TIME, TimeUnit.SECONDS);
            //登陆成功，清除黑名单
            redisTemplate.delete(BLACK_IP_PREFIX + remoteHost);
            log.info("LoginService.login: remote host:[{}],login success.", remoteHost);
            return SuccessResponse.success(true);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String remoteHost = request.getRemoteHost();
        if (request.getCookies() != null && request.getCookies().length > 0) {
            Optional<Cookie> match = Arrays.stream(request.getCookies())
                    .filter(e -> e.getName().equals(COOKIE_NAME))
                    .findFirst();
            if (match.isPresent()) {
                Cookie cookie = match.get();
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                //清除登录有效期记录
                redisTemplate.delete(cookie.getValue());
            }
        }
        log.info("LoginService.logout: remote host:[{}],logout.", remoteHost);
    }

    //用于路由的权限验证
    public boolean logged(HttpServletRequest request) {
        if (request.getCookies() != null && request.getCookies().length > 0) {
            Optional<Cookie> match = Arrays.stream(request.getCookies())
                    .filter(e -> e.getName().equals(COOKIE_NAME))
                    .findFirst();
            return match.filter(cookie -> redisTemplate.opsForValue().get(cookie.getValue()) != null).isPresent();
        }
        return false;
    }
}
