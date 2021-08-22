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


    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }
}
