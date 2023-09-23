package ind.xyz.mywebsite.config;

import ind.xyz.mywebsite.util.login.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /*
     *addResourceHandler:访问映射路径
     *addResourceLocations:资源绝对路径
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("http://localhost:8080/xyz/image/**").addResourceLocations("file:D:/test/usr/mywebsite/static/resource/");
//    }


        @Autowired
        LoginInterceptor loginInterceptor;
        @Resource
        private StringRedisTemplate stringRedisTemplate;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
////                .addPathPatterns("/**")   //默认对所有请求进行拦截
//                .excludePathPatterns("/static/**");     //对login页面和静态资源不拦截
//    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        WebMvcConfigurer.super.addResourceHandlers(registry);
//    }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RefreshLoginInterceptor(stringRedisTemplate)).order(0);
            registry.addInterceptor(new LoginInterceptor(stringRedisTemplate)).addPathPatterns( // Check user
                    "/blog/save",
                    "/blog/delete",
                    "/blog/modify"
            ).order(1);
        }
}
