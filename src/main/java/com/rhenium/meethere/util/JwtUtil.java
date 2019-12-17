package com.rhenium.meethere.util;

import com.rhenium.meethere.domain.Customer;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.exception.MyException;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author HavenTong
 * @date 2019/12/14 11:10 上午
 * 创建和解析JWT的工具类
 */
public class JwtUtil {
    private static final String KEY = "meethere";
    private static final long ttl = 60 * 60 * 24 * 1000 * 7;

    /**
     * 通过customer生成JWT
     * @param customer Customer类型的参数，登录时将customerId编码到JWT中返回
     * @return 返回通过customerId生成的JWT
     */
    public static String createJwt(Customer customer){
        long current = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setId(customer.getCustomerId().toString())
                .setIssuedAt(new Date(current))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setExpiration(new Date(current + ttl));
        return builder.compact();
    }

    /**
     * 解析JWT，解析失败则抛出自定义异常，提示"TOKEN错误，无法解析"
     * @param jwt JWT字符串
     * @return 解析后的body
     */
    public static Claims parseJwt(String jwt){
        try {
            return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
        } catch (Exception ex){
            throw new MyException(ResultEnum.INVALID_TOKEN);
        }
    }

    public static String getToken(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        String token = httpServletRequest.getHeader("TOKEN");
        if (StringUtils.isEmpty(token)){
            throw new MyException(ResultEnum.TOKEN_NOT_EXIST);
        }
        return token;
    }
}
