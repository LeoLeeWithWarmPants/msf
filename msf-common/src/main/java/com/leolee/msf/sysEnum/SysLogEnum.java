package com.leolee.msf.sysEnum;

/**
 * 功能描述: <br> 系统日志类型枚举
 * 〈〉
 * @Author: LeoLee
 * @Date: 2020/8/17 16:11
 */
public enum SysLogEnum {

    EXCEPTION("异常日志"),
    BUSSINESS("业务日志"),
    SUCCESS("成功"),
    FAIL("失败");

    String message;

    private SysLogEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

