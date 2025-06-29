package com.xujie.future.contract.enums;

import lombok.Getter;

/**
 * 错误码
 *
 * @author Xujie
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "OK", ""),
    FAIL(1, "Fail", "失败"),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    FORBIDDEN(40301, "禁止操作", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");


    /*** AI模块错误码 ****/


    // 只能读取，不能修改
    // 错误码
    private final int code;

    // 错误码信息
    private final String message;

    // 错误码描述（详细）
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
