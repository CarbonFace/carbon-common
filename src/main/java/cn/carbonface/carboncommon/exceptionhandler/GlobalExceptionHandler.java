package cn.carbonface.carboncommon.exceptionhandler;

import cn.carbonface.carboncommon.dto.ApiResult;
import cn.carbonface.carboncommon.dto.RetCode;
import cn.carbonface.carboncommon.exception.CarbonException;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classname: GlobalExceptionHandler
 * Description: Global Exception Handler for those exceptions not throw
 * @author CarbonFace <553127022@qq.com>
 * Date" 2021/3/18 17:09
 * @version V1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CarbonException.class)
    public ApiResult<?> exceptionHandler(CarbonException e) {
        logException(e);
        if (e.getRetCode() != null) {
            return ApiResult.error(e.getRetCode());
        } else {
            return ApiResult.error(e.getMessage());
        }
    }
    @ExceptionHandler(value = DecodeException.class)
    public ApiResult<?> decoderExceptionHandler(DecodeException e){
        return ApiResult.error(e.status(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<?> exceptionHandler(Exception e){
        logException(e);
        return ApiResult.error(RetCode.INTERNAL_ERROR);
    }

    private void logException(Exception e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        List<StackTraceElement> carbonStacks = Arrays.stream(stackTrace).filter(stackTraceElement ->
                stackTraceElement.getClassName().startsWith("cn.carbonface")
        ).collect(Collectors.toList());
        String message = e.getMessage();
        StringBuilder exceptionString = new StringBuilder();
        exceptionString.append(message);
        for (StackTraceElement carbonStack : carbonStacks) {
            String className = carbonStack.getClassName();
            String methodName = carbonStack.getMethodName();
            int lineNumber = carbonStack.getLineNumber();
            exceptionString.append("Class:").append(className).append("method:").append(methodName).append(" ").append(lineNumber).append("\n");
        }
        log.error(exceptionString.toString());
        //e.printStackTrace();
    }
}
