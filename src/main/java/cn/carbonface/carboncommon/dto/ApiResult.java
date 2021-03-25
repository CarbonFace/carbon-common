package cn.carbonface.carboncommon.dto;

import java.io.Serializable;

public class ApiResult implements Serializable {

    private static final long serialVersionUID = -8356120970616292445L;
    private int retCode;
    private String msg;
    private String token;
    private Object data;

    public ApiResult(Object data, String token, String msg, int retCode) {
        this.retCode = retCode;
        this.msg = msg;
        this.token = token;
        this.data = data;
    }
    public ApiResult(Object data, String token, RetCode retCode){
        this.data = data;
        this.token = token;
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public ApiResult(Object data, String msg , int retCode) {
        this.retCode = retCode;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(String msg , RetCode retCode) {
        this.retCode = retCode.getCode();
        this.msg = msg;
    }

    public ApiResult(RetCode retCode){
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public ApiResult token(String token){
        this.token = token;
        return this;
    }
    public static ApiResult ok(Object data, String msg){
        ApiResult apiResult = new ApiResult(data,msg,RetCode.SUCCESS.getCode());
        return apiResult;
    }

    public static ApiResult ok(String msg){
        ApiResult apiResult = new ApiResult(null,msg,RetCode.SUCCESS.getCode());
        return apiResult;
    }

    public static ApiResult error(String msg){
        ApiResult apiResult = new ApiResult(null,msg,RetCode.COMMON_FAIL.getCode());
        return apiResult;
    }

    public static ApiResult error(int retCode, String msg){
        ApiResult apiResult = new ApiResult(null,msg,retCode);
        return apiResult;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
