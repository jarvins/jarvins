package com.jarvins.entity.response;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    //登录异常
    AUTH_FAIL_ERROR(310, "认证失败"),
    MAX_AUTH_ERROR(311, "登录失败超过5次,请10分钟之后尝试登陆"),
    COOKIE_FAIL_ERROR(312, "cookie失效"),

    //请求异常
    LOGIN_ERROR(410, "身份认证失败"),
    NET_ERROR(411, "网络连接异常"),
    SERVICE_ERROR(412, "服务器处理异常"),
    UKNOWN_ERROR(419, "未知异常"),

    //操作异常
    ADD_ERROR(510, "资源添加失败"),
    DElETE_ERROR(511, "资源删除失败"),
    UPDATE_ERROR(512, "资源更新失败");

    private int code;
    private String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
