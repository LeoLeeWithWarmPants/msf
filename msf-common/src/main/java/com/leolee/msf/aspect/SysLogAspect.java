package com.leolee.msf.aspect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leolee.msf.annotation.SysLog;
import com.leolee.msf.sysEnum.SysLogEnum;
import com.leolee.msf.utils.IPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 功能描述: <br> 对应SysLog注解的AOP
 * 〈〉
 * @Author: LeoLee
 * @Date: 2020/8/17 16:27
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private Gson gson;

    @Pointcut("@annotation(com.leolee.msf.annotation.SysLog)")
    public void SysLog() {

    }

    @Before("SysLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        sysLogHandler(joinPoint, null);
    }



    /**
     * 功能描述: <br> controller层接口的处理器，可做日志打印入库
     * 〈〉
     * @Param: [joinPoint, succeed]
     * @Return: void
     * @Author: LeoLee
     * @Date: 2020/8/17 16:30
     */
    private void sysLogHandler(JoinPoint joinPoint, String succeed) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //------------------可以拿到的数据-----------------------------
        //获取注解信息
        SysLog syslog = method.getAnnotation(SysLog.class);
        if(syslog != null){
            //注解上的描述
            syslog.value();
            syslog.type().getMessage();
            System.out.println(syslog.value());
            System.out.println(syslog.type().getMessage());
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        //获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        IPUtils.getIpAddr(request);//ip地址
        Map<String, String[]> getMap = request.getParameterMap();//请求参数
        request.getRequestURL().toString();//请求地址
        System.out.println(request.getRequestURL().toString());
        System.out.println(IPUtils.getIpAddr(request));
        //----------------------调用mq排队入库--------------------------------


    }


    /**
     * 功能描述: <br> 获取接口调用是否成功的结果
     * 〈〉
     * @Param: [proceed]
     * @Return: java.lang.String
     * @Author: LeoLee
     * @Date: 2020/8/17 17:01
     */
    private String getExecutionResult(Object proceed) {
        String succeed = "";
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        if (proceed != null) {
            String result = gson.toJson(proceed, mapType);
            Map<String, Object> jsonMap = gson.fromJson(result, mapType);
            if (jsonMap.get("code").toString().equals(SysLogEnum.SUCCESS.getMessage())) {
                return SysLogEnum.SUCCESS.getMessage();
            }

            String erMsg = "";
            if(jsonMap.get("desc") == null){
                erMsg = jsonMap.get("msg").toString();
            }else{
                erMsg = jsonMap.get("desc").toString();
            }
            succeed = SysLogEnum.FAIL.getMessage() + "[" + erMsg + "]";
        } else {
            return SysLogEnum.SUCCESS.getMessage();
        }
        return succeed;
    }

}
