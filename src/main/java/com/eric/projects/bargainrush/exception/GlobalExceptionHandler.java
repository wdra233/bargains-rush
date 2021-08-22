package com.eric.projects.bargainrush.exception;

import com.eric.projects.bargainrush.result.CodeMsg;
import com.eric.projects.bargainrush.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof GlobalException) {
          GlobalException globalException = (GlobalException) e;
          return Result.error(globalException.getCmg());
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            final List<ObjectError> allErrors = bindException.getAllErrors();
            String msg = allErrors.get(0).getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
