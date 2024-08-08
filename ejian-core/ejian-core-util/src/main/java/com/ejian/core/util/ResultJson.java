package com.ejian.core.util;

import com.ejian.core.common.enums.ResultEnum;
import com.ejian.core.common.enums.ResultJsonCodeEnum;

import java.io.Serializable;

/**
 * 返回给前端的json格式封装 异常错误码 默认400 业务处理失败错误码 默认 600 更多错误码会定义错误码enums
 */
public class ResultJson implements Serializable {

    private int code;

    private String msg;

    private Object data;

    public static ResultJson success() {
        return success(null);
    }

    public static ResultJson success(Object data) {
        return success(ResultJsonCodeEnum.SUCCESS, data);
    }

    public static ResultJson success(ResultJsonCodeEnum resultJsonCodeEnum, Object data) {
        return result(resultJsonCodeEnum, data);
    }

    public static ResultJson fail() {
        return result(ResultJsonCodeEnum.FAIL, null);
    }

    public static ResultJson tokenExpired() {
        return result(ResultJsonCodeEnum.LOGIN_TOKEN_EXPIRED, null);
    }

    public static ResultJson batchFailed(int count) {
        ResultJson result = new ResultJson();
        result.setCode(ResultJsonCodeEnum.BATCH_OPERATION_FAILED.getCode());
        result.setMsg(ResultJsonCodeEnum.BATCH_OPERATION_FAILED.getMsg().formatted(count));
        result.setData(null);
        return result;
    }

    /**
     * 业务处理失败返回
     *
     * @param resultEnum 失败原因
     * @return 失败
     */
    public static ResultJson fail(ResultEnum resultEnum) {
        return result(resultEnum, null);
    }

    public static ResultJson fail(ResultJsonCodeEnum resultJsonCodeEnum, Object data) {
        return result(resultJsonCodeEnum, data);
    }

    /**
     * 产生异常的时候返回
     *
     * @param resultJsonCodeEnum 异常值
     * @return 异常
     */
    public static ResultJson error(ResultJsonCodeEnum resultJsonCodeEnum) {
        return error(resultJsonCodeEnum, null);
    }

    public static ResultJson error(ResultJsonCodeEnum resultJsonCodeEnum, Object data) {
        return result(resultJsonCodeEnum, data);
    }

    public static ResultJson result(ResultEnum resultEnum, Object data) {
        ResultJson result = new ResultJson();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        result.setData(data);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{code:" + code + ", msg='" + msg + "'}";
    }
}
