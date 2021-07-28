package cn.edu.guet.popular_blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author pangjian
 * @ClassName WebConfigurer
 * @Description TODO
 * @date 2021/7/27 20:48
 */

@Configuration
public class WebConfigurer extends WebMvcConfigurationSupport {

    @Value("${fileRootPath}")
    private String uploadLocation;


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**","/myblog/**").addResourceLocations("classpath:/static/","file:"+uploadLocation);
        super.addResourceHandlers(registry);
    }
}

