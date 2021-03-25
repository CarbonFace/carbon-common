package cn.carbonface.carboncommon.tools;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Classname HttpUtil
 * @Description httpUtil for obtain http attributes in the project
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/19 10:02
 * @Version V1.0
 */

@Component
public class HttpUtil {


    /**
     * @description: getServletRequestAttributes
     * warn : this method can not use in the Asynchronous thread, or it will cause null point exception
     * if must , may it be like belong codes before opening up a new thread to set "Thread sharing"
     * ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
     * RequestContextHolder.setRequestAttributes(sra, true);
     * @return org.springframework.web.context.request.ServletRequestAttributes
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:47
     * @version: 1.0
     */
    public static ServletRequestAttributes getServletRequestAttributes(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return (ServletRequestAttributes) requestAttributes;// might be null
        }
        return null;
    }

    /**
     * @description: getRequest 
     * warn : when the requestAttributes is null request is null
     * @return javax.servlet.http.HttpServletRequest
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:31
     * @version: 1.0 
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        return servletRequestAttributes == null?null:servletRequestAttributes.getRequest();
    }

    /**
     * @description: getResponse
     * warn : when the requestAttributes is null request is null
     * @return javax.servlet.http.HttpServletResponse
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:37
     * @version: 1.0
     */
    public static HttpServletResponse getResponse(){
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        return servletRequestAttributes == null?null:servletRequestAttributes.getResponse();
    }

    /**
     * @description: getSession
     * get session with httpServletRequest input
     * @param request
     * @return javax.servlet.http.HttpSession
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:52
     * @version: 1.0
     */
    public static HttpSession getSession(HttpServletRequest request){
        return request == null?null:request.getSession();
    }

    /**
     * @description: getSession
     * get threadLocal request's session
     * @return javax.servlet.http.HttpSession
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:55
     * @version: 1.0
     */
    public static HttpSession getSession(){
        HttpServletRequest request = getRequest();
        return getSession(request);
    }

    /**
     * @description: getIpAddress
     * get the threadLocal request's real ip address
     * @return java.lang.String
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:56
     * @version: 1.0
     */
    public static String getIpAddress(){
        HttpServletRequest request = getRequest();
        return getIpAddress(request);
    }

    /**
     * @description: getIpAddress
     * get the real ip address in the request
     * @return java.lang.String
     * @author: CarbonFace  <553127022@qq.com>
     * @date: 2021/3/19 10:55
     * @version: 1.0
     */
    public static String getIpAddress(HttpServletRequest request){
        if (request == null){
            return "";
        }else{
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
   }

}
