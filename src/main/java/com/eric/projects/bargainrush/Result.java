package com.eric.projects.bargainrush;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T t) {
        return new Result<>(200, "success", t);
    }

    public static <T> Result<T> error(T t) {
        return new Result<>(404, "error code", t);
    }
}
