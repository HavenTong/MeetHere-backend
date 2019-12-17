package com.rhenium.meethere.aop;

import com.rhenium.meethere.dto.PublicRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author HavenTong
 * @date 2019/12/16 5:53 下午
 */
@Component
@Slf4j
@Aspect
public class PublicLoginVerificationAspect {
    @Pointcut("@annotation(com.rhenium.meethere.annotation.PublicLoginRequired)")
    public void verifyPublicLoginPoint(){}

    @Before("verifyPublicLoginPoint()")
    public void verifyPublicLogin(JoinPoint joinPoint){
        String token = JwtUtil.getToken();
        Claims claims = JwtUtil.parseJwt(token);
        Integer decodedId = Integer.parseInt(claims.getId());
        Object[] arguments = joinPoint.getArgs();
        for (Object argument : arguments){
            if (argument instanceof PublicRequest){
                Integer actualId = ((PublicRequest) argument).getUserId();
                if (!decodedId.equals(actualId)){
                    throw new MyException(ResultEnum.TOKEN_NOT_MATCH);
                }
                break;
            }
        }
    }
}
