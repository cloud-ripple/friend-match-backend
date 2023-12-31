package com.ripple.friend.common;

/**
 * @author 花海
 * @date 2023/11/9
 * @description
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(200, data, "请求成功");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    /**
     * 失败
     *
     * @param errorCode   错误码
     * @param description 自定义描述
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String description) {
        errorCode.setDescription(description);
        return new BaseResponse(errorCode);
    }
}
