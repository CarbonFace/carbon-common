package cn.carbonface.carboncommon.webconfig;

import cn.carbonface.carboncommon.interceptor.FeignOnlyInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class FeignWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FeignOnlyInterceptor());
    }
}
