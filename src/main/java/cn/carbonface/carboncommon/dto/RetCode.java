package cn.carbonface.carboncommon.dto;

/**
 * Classname: RetCode
 * Description: retCode enum
 * @author CarbonFace  <553127022@qq.com>
 * Date: 2021/3/17 17:06
 * @version V1.0
 */
public enum RetCode {
    /* 成功 */
    SUCCESS(200, "成功"),
    ACCESS_DENIED(401,"拒绝访问"),
    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_LOGIN(2000, "用户登录成功"),
    USER_LOGOUT(2001, "用户登出成功"),
    USER_LOGIN_FAIL(2002, "用户登录失败"),
    USER_NOT_LOGIN(2003, "用户未登录"),
    USER_LOGIN_EXPIRED(2004, "登陆已过期"),
    USER_CREDENTIALS_ERROR(2005, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2006, "密码过期"),
    USER_ACCOUNT_DISABLE(2007, "账号不可用"),
    USER_ACCOUNT_LOCKED(2008, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2009, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2010, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2011, "账号下线"),

    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限"),
    FEIGN_ONLY(3002, "内部接口，无法访问！"),

    FILE_NOT_FIND(3030,"无法找到文件"),
    INTERNAL_ERROR(5000,"服务器内部错误");


    private Integer code;
    private String message;

    RetCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return string
     */
    public static String getMessageByCode(Integer code) {
        for (RetCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
