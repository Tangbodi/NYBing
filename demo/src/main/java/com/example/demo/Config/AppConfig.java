package com.example.demo.Config;

import com.example.demo.Util.HeaderInterceptorUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {

        registry.addInterceptor(new HeaderInterceptorUtil());
    }

}
