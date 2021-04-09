package cn.carbonface.carboncommon.exceptionhandler;

import cn.carbonface.carboncommon.dto.ApiResult;
import cn.carbonface.carboncommon.exception.CarbonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Classname GlobalExceptionHandler
 * @Description Global Exception Handler for those exceptions not throw
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/18 17:09
 * @Version V1.0
 */
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
