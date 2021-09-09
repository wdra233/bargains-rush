package com.eric.projects.bargainrush.result;

import lombok.Getter;

@Getter
public class CodeMsg {
    private int code;
    private String msg;
    private CodeMsg(int code, String msg ){
        this.code = code;
        this.msg = msg;
    }



    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "error code");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "Params validation error: %s");


    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "SESSION_INVALIDATE");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "LOGIN_PASSWORD_CANNOT_BE_EMPTY");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "MOBILE_CANNOT_BE_EMPTY");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "Mobile number not exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "Password error");

    public static CodeMsg BARGAIN_COMPLETE = new CodeMsg(500500, "Bargain item complete");
    public static CodeMsg BARGAIN_FAIL = new CodeMsg(500502, "BargainRush Failed");
    public static CodeMsg DUPLICATE_BARGAIN = new CodeMsg(500501, "Duplicate bargain order not allowed");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "Order not exists");

    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "illegal request");


    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }
}
