package com.ripple.friend.exception;

import com.ripple.friend.common.BaseResponse;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 花海
 * @date 2023/11/9
 * @description 全局异常处理器
 */

//@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody // 使用 @RestControllerAdvice 的话可以省略该注解
    @ExceptionHandler({BusinessException.class})
    public BaseResponse businessExceptionHandler(BusinessException businessEx) {
        log.error("自定义业务异常：状态码-{}, {}, 描述-{}", businessEx.getCode(), businessEx.getMessage(), businessEx.getDescription());
        return ResultUtils.error(ErrorCode.getErrorEnumByCode(businessEx.getCode()), businessEx.getDescription());
    }

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public BaseResponse runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
        log.error("运行时异常：{}", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
