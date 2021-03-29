package cn.carbonface.carboncommon.interceptor;

import cn.carbonface.carboncommon.dto.ApiResult;
import cn.carbonface.carboncommon.dto.RetCode;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Classname AnnotationInterceptor
 * @Description TODO
 * @Author CarbonFace <553127022@qq.com>
 * @Date 2021/3/26 18:00
 * @Version V1.0
 */
public class AnnotationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return feignOnly(handler);
    }

    private boolean feignOnly(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        FeignOnly feignOnly = method.getAnnotation(FeignOnly.class);
        return feignOnly!=null;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ApiResult.response(response,new ApiResult(RetCode.FEIGN_ONLY)/*.token()*/);
    }
}
