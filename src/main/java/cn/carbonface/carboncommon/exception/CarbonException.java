package cn.carbonface.carboncommon.exception;

import cn.carbonface.carboncommon.dto.RetCode;

public class CarbonException extends Exception{
    private RetCode retCode;


    public CarbonException(RetCode retCode){
        super(retCode.getMessage());
        this.retCode = retCode;
    }

    public RetCode getRetCode() {
        return retCode;
    }
    public void setRetCode(RetCode retCode) {
        this.retCode = retCode;
    }
    private static final long serialVersionUID = -3691728958918582706L;

    public CarbonException() {
    }
    public CarbonException(String message) {
        super(message);
    }

    public CarbonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarbonException(Throwable cause) {
        super(cause);
    }

    public CarbonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
