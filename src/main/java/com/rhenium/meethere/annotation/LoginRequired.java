package com.rhenium.meethere.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author HavenTong
 * @date 2019/12/14 11:22 上午
 * 如果controller中的操作需要登录才可以进行，则在该方法上加上注解
 */
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
