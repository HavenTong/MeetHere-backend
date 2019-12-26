package com.rhenium.meethere.aop;

import com.rhenium.meethere.dto.AdminRequest;
import com.rhenium.meethere.dto.NewsRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * @author HavenTong
 * @date 2019/12/17 9:45 下午
 */
@Aspect
@Slf4j
@Component
public class AdminLoginVerificationAspect {
    @Pointcut("@annotation(com.rhenium.meethere.annotation.AdminLoginRequired)")
    public void verifyAdminLoginPoint(){}

    @Before("verifyAdminLoginPoint()")
    public void verifyAdminLogin(JoinPoint joinPoint) throws IllegalAccessException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        String token = httpServletRequest.getHeader("TOKEN");
        if (StringUtils.isEmpty(token)){
            throw new MyException(ResultEnum.TOKEN_NOT_EXIST);
        }
        Claims claims = JwtUtil.parseJwt(token);
        Integer decodedAdminId = Integer.parseInt(claims.getId());
        String method = httpServletRequest.getMethod();
        Object[] arguments = joinPoint.getArgs();
        Integer actualAdminId = -1;
        if ("GET".equals(method)){
            actualAdminId = (Integer) arguments[arguments.length - 1];
        } else if ("POST".equals(method)){
            Object argument = arguments[0];
            Field[] fields = argument.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                if ("adminId".equals(field.getName())){
                    log.info("string actualId: {}", field.get(argument));
                    actualAdminId = (Integer) field.get(argument);
                    log.info("actualAdminId: {}", actualAdminId);
                    break;
                }
            }
        }
        if (!decodedAdminId.equals(actualAdminId)){
            log.info("not equals: actual: {}", actualAdminId);
            log.info("not equals: decoded: {}", decodedAdminId);
            throw new MyException(ResultEnum.TOKEN_NOT_MATCH);
        }
    }

}
