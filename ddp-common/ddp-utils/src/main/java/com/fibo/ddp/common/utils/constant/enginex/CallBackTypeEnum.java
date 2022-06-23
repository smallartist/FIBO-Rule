package com.fibo.ddp.common.utils.constant.enginex;

public enum CallBackTypeEnum {

    SYNC(1,"同步"),
    ASYNC(2,"异步");

    private int code;
    private String message;

    CallBackTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
