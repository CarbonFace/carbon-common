package cn.carbonface.carboncommon.exception;


import cn.carbonface.carboncommon.dto.RetCode;

public class ApiException extends CarbonException {
    private static final long serialVersionUID = -5485177960058845859L;


    public ApiException(RetCode retCode) {
        super(retCode);
    }
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
