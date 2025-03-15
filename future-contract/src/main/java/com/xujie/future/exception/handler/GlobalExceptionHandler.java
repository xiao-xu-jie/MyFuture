package com.xujie.future.exception.handler;

import com.xujie.future.enums.ErrorCode;
import com.xujie.future.exception.BusinessException;
import com.xujie.future.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Xujie
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 处理自定义异常
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> businessException(BusinessException e) {
        log.error("businessException:{}", e.getMessage(), e);
        return ResponseResult.fail(e.getCode(), e.getDescription());
    }

    // 处理系统异常
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<String> runtimeException(BusinessException e) {
        log.error("runtimeException:{}", e.getMessage(), e);
        return ResponseResult.fail(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
