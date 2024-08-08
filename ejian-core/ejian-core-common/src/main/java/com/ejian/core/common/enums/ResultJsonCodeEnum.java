package com.ejian.core.common.enums;

/**
 * 这是一个通用的错误枚举类，后面每一个业务有自己的枚举类
 * 错误码定义：
 * 0-1000 系统统一错误
 * 1000-1100 用户模块
 * 1100-1200 角色模块
 * 1200-1300 权限模块
 * 1300-1400 菜单模块
 * 1400-1500 字典模块
 */
public enum ResultJsonCodeEnum implements ResultEnum{

    SUCCESS(200, "操作成功"),
    PARAM_ERROR(299,"参数错误"),
    UNAUTHORIZED(320, "未授权，请联系管理员"),
    LOGIN_TOKEN_EXPIRED(401, "Token过期，请重新登录"),
    BAD_REQUEST(400, "错误请求"),
    BATCH_OPERATION_FAILED(997, "%d条操作失败"),
    FAIL(999, "操作失败");

    private final int code;

    private final String msg;

    ResultJsonCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMsg(){
        return msg;
    }

}
