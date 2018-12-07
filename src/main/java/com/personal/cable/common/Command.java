package com.personal.cable.common;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/21 10:36
 * @description: 命令码
 */
public enum Command {

    UNKNOWN((short) 0, "未知"),
    LOGIN_REQUEST((short) 1, "登录请求"),
    LOGIN_RESPONSE((short) 2, "登录响应"),
    LOGOUT_REQUEST((short) 3, "登出请求"),
    LOGOUT_RRESPONSE((short) 4, "登出响应"),
    AUTH_REQUEST((short) 5, "认证请求"),
    AUTH_RRESPONSE((short) 6, "认证响应"),
    SHAKEHANDS_REQUEST((short) 7, "心跳请求"),
    SHAKEHANDS_RRESPONSE((short) 8, "心跳响应"),
    PUSH_REQUEST((short) 9, "广播请求"),
    PUSH_RRESPONSE((short) 10, "广播响应"),
    GLOBAL_RRESPONSE((short) 9999, "全局响应"),
    ;
    private short type;
    private String desc;

    Command(short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
