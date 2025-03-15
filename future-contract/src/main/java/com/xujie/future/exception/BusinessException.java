package com.xujie.future.exception;


import com.xujie.future.enums.ErrorCode;

/**
 * 自定义异常类
 *
 * @author Xujie
 */
public class BusinessException extends RuntimeException {

    // 错误码
    private final int code;

    // 错误码信息
    private final String description;

    // 复用其他的构造函数，根据传递的参数不同做区分
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    // 获取错误码
    public int getCode() {
        return code;
    }

    // 获取错误码信息
    public String getDescription() {
        return description;
    }
}
