package com.rhenium.meethere.aop;

import com.rhenium.meethere.dto.CustomerRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

/**
 * @author HavenTong
 * @date 2019/12/14 11:29 上午
 * 通过aop进行全局拦截，必须要保证HTTP头部中的TOKEN字段中携带的id
 * 与传入的CustomerRequest中的customerId相匹配才可以进入相应的controller
 */
@Aspect
@Component
@Slf4j
public class UserLoginVerificationAspect {
    @Pointcut("@annotation(com.rhenium.meethere.annotation.UserLoginRequired)")
    public void verifyUserLoginPoint(){}

    @Before("verifyUserLoginPoint()")
    public void verifyUserLogin(JoinPoint joinPoint){
        String token = JwtUtil.getToken();
        Claims claims = JwtUtil.parseJwt(token);
        Integer decodedCustomerId = Integer.parseInt(claims.getId());
        Object[] arguments = joinPoint.getArgs();
        for (Object argument: arguments){
            if (argument instanceof CustomerRequest){
                Integer actualCustomerId = ((CustomerRequest) argument).getCustomerId();
                if (!decodedCustomerId.equals(actualCustomerId)){
                    throw new MyException(ResultEnum.TOKEN_NOT_MATCH);
                }
                break;
            }
        }
    }
}
