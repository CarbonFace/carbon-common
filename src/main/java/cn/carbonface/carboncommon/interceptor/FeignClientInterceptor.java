package cn.carbonface.carboncommon.interceptor;

import cn.carbonface.carboncommon.constants.FeignConstant;
import cn.carbonface.carboncommon.tools.HttpUtil;
import com.netflix.appinfo.ApplicationInfoManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Classname AnnotationInterceptor
 * @Description Feign interceptor used for header loss solve
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/28 15:16:22
 * @Version V1.0
 */
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(FeignConstant.FEIGN_HEADER_NAME,FeignConstant.FEIGN_HEADER_VALUE);
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        Enumeration<String> headerNames = request.getHeaderNames();
//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                String name = headerNames.nextElement();
//                String values = request.getHeader(name);
//                requestTemplate.header(name, values);
//            }
//        }
//        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(HttpUtil.getRequest().getServletContext());
//        Enumeration<String> bodyNames = request.getParameterNames();
//        StringBuffer body =new StringBuffer();
//        if (bodyNames != null) {
//            while (bodyNames.hasMoreElements()) {
//                String name = bodyNames.nextElement();
//                String values = request.getParameter(name);
//                body.append(name).append("=").append(values).append("&");
//            }
//        }
//        if(body.length()!=0) {
//            body.deleteCharAt(body.length()-1);
//            requestTemplate.body(body.toString());
//            log.info("feign interceptor body:{}",body.toString());
//        }


    }
}
