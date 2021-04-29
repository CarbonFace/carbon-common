package cn.carbonface.carboncommon.dto;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Optional;

/**
 * Classname: ApiResult
 * Description: common ApiResult class for http request handle and lay response
 * Date 2021/3/12 13:16
 * @author CarbonFace <553127022@qq.com>
 * @version V1.0
 */
@Slf4j
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -8356120970616292445L;
    private int retCode;
    private String msg;
    private T data;

    public ApiResult(){

    }

    public ApiResult(T data, String msg, int retCode) {
        this.retCode = retCode;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(T data,RetCode retCode){
        this.data = data;
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public ApiResult(RetCode retCode){
        this.retCode = retCode.getCode();
        this.msg = retCode.getMessage();
    }

    public static <T> ApiResult<T> ok(T data, String msg){
        return new ApiResult<>(data,msg,RetCode.SUCCESS.getCode());
    }
    public static <T> ApiResult<T> ok(T data){
        return new ApiResult<>(data,RetCode.SUCCESS.getMessage(),RetCode.SUCCESS.getCode());
    }

    public static <T> ApiResult<T> okMsg(String msg){
        return new ApiResult<>(null,msg,RetCode.SUCCESS.getCode());
    }

    public static <T> ApiResult<T> error(RetCode retCode) {
        return new ApiResult<>(retCode);
    }

    public static <T> ApiResult<T> error(String msg){
        return new ApiResult<>(null,msg,RetCode.COMMON_FAIL.getCode());
    }

    public static <T> ApiResult<T> error(int retCode, String msg){
        return new ApiResult<>(null,msg,retCode);
    }

    public static void response(HttpServletResponse response,Class<?> data,RetCode retCode){
        response(response,new ApiResult<>(data,retCode));
    }

    public static void response(HttpServletResponse response,ApiResult<?> apiResult){
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            writer = response.getWriter();
            writer.println(JSON.toJSONString(apiResult));
            writer.flush();
        } catch (Exception e) {
            log.error("Response输出Json异常：" + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success(){
        return retCode ==200;
    };
}
