package cn.carbonface.carboncommon.validate;

import cn.carbonface.carboncommon.dto.ApiResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Classname ValidateExceptionHandler
 * @Description ValidateExceptionHandler for BindException handle
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/19 12:02
 * @Version V1.0
 */
@RestControllerAdvice
public class ValidateExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ApiResult<?> handleBindException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            msg.append(fieldError.getDefaultMessage()).append(";");
        });
        return ApiResult.error(msg.toString());
    }

}
