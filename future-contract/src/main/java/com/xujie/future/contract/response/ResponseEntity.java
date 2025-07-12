package com.xujie.future.contract.response;


import com.xujie.future.contract.enums.ErrorCode;
import com.xujie.future.contract.enums.HttpStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回结果集
 *
 * @author Xujie
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     *
     * @mock 200
     */
    private Integer code;

    /**
     * 状态信息
     */
    private Boolean status;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    private static <T> ResponseEntity<T> response(Integer code, Boolean status, String message, T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(code);
        responseEntity.setStatus(status);
        responseEntity.setMessage(message);
        responseEntity.setData(data);
        return responseEntity;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param status  状态
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    private static <T> ResponseEntity<T> response(Integer code, Boolean status, String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.setCode(code);
        responseEntity.setStatus(status);
        responseEntity.setMessage(message);
        return responseEntity;
    }

    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success() {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（枚举参数）
     *
     * @param httpResponseEnum 枚举参数
     * @param <T>              泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), true, httpResponseEnum.getMessage());
    }

    /**
     * 成功返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success(Integer code, String message) {
        return response(code, true, message);
    }

    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success(String message, T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success(Integer code, String message, T data) {
        return response(code, true, message, data);
    }

    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> success(T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> message(String message) {
        return response(HttpStatusEnum.SUCCESS.getCode(), true, message, null);
    }

    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail() {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     *
     * @param httpResponseEnum 枚举
     * @param <T>              泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(HttpStatusEnum httpResponseEnum) {
        return response(httpResponseEnum.getCode(), false, httpResponseEnum.getMessage());
    }

    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(Integer code, String message) {
        return response(code, false, message);
    }

    /**
     * 失败返回（返回信息+数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(String message, T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, data);
    }

    /**
     * 失败返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回消息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(Integer code, String message, T data) {
        return response(code, false, message, data);
    }

    /**
     * 失败返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(T data) {
        return response(HttpStatusEnum.ERROR.getCode(), false, HttpStatusEnum.ERROR.getMessage(), data);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(String message) {
        return response(HttpStatusEnum.ERROR.getCode(), false, message, null);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param errorCode 错误码
     * @param message   返回信息
     * @return {@link ResponseEntity <T>}
     */
    public static <T> ResponseEntity<T> fail(ErrorCode errorCode, String message) {

        return response(errorCode.getCode(), false, message);
    }

    public Boolean isSuccess() {
        return this.status;
    }
}