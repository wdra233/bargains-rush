package com.eric.projects.bargainrush.exception;

import com.eric.projects.bargainrush.result.CodeMsg;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cmg;

    public GlobalException(CodeMsg cmg) {
        super(cmg.toString());
        this.cmg = cmg;
    }
}
