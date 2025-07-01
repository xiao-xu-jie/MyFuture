package com.xujie.future.contract.exception;


import com.xujie.future.contract.enums.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author Xujie
 */
@Getter
public class BusinessException extends RuntimeException {

    // 获取错误码
    // 错误码
    private final int code;

    // 获取错误码信息
    // 错误码信息
    private final String description;

    // 复用其他的构造函数，根据传递的参数不同做区分
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(description);
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCode.FAIL.getCode();
        this.description = ErrorCode.FAIL.getDescription();
    }


}
