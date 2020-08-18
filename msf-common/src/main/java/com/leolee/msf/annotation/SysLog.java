package com.leolee.msf.annotation;

import com.leolee.msf.sysEnum.SysLogEnum;import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {

    public String value() default "";

    public SysLogEnum type() default SysLogEnum.BUSSINESS;

}
