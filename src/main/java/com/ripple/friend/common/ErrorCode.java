package com.ripple.friend.common;

/**
 * @author 花海
 * @date 2023/11/9
 * @description
 */

public enum ErrorCode {

    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001,"数据为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 自定义状态码描述
     */
    private String description;


    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 根据错误码获取对应枚举值
     * @param code
     * @return
     */
    public static ErrorCode getErrorEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        ErrorCode[] errorCodes = ErrorCode.values();
        for (ErrorCode errorCode : errorCodes) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}
