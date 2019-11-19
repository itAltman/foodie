package me.atm.foodie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Altman
 * @date 2019/11/19
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:/Users/admin/Documents/test/")   // 映射本地静态资源。原本路径：/Users/admin/Documents/test/1911064TAGPGNZ9P/%E5%B7%A5%E5%85%B7-20191119191947.png -> 新路径：http://localhost:8088/1911064TAGPGNZ9P/%E5%B7%A5%E5%85%B7-20191119191947.png
                .addResourceLocations("classpath:/META-INF/resources/");     // 映射swagger2
    }
}
