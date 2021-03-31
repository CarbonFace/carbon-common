package cn.carbonface.carboncommon.exceptionhandler;

import cn.carbonface.carboncommon.dto.ApiResult;
import cn.carbonface.carboncommon.exception.CarbonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CarbonException.class)
    public ApiResult exceptionHandler(CarbonException e) {
        if (e.getRetCode() != null) {
            return ApiResult.error(e.getRetCode());
        } else {
            return ApiResult.error(e.getMessage());
        }
    }
}
