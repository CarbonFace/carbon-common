package cn.carbonface.carboncommon.dto;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @Classname ApiResult
 * @Description common ApiResult class for http request handle and lay response
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/12 13:16
 * @Version V1.0
 */
@Slf4j
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


    public ApiResult(Object data,RetCode retCode){
        this.data = data;
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public ApiResult(RetCode retCode){
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public static ApiResult error(RetCode retCode) {
        return new ApiResult(retCode);
    }

    public ApiResult token(String token){
        this.token = token;
        return this;
    }

    public static void response(HttpServletResponse response,Object data,RetCode retCode){
        response(response,new ApiResult(data,retCode));
    }

    public static void response(HttpServletResponse response,ApiResult apiResult){
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            writer = response.getWriter();
            writer.println(JSON.toJSONString(apiResult));
            writer.flush();
        } catch (Exception e) {
            log.error("Response输出Json异常：" + e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
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
