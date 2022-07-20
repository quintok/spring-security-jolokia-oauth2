package com.example.demo;

import com.example.demo.config.WebMvcConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected  Class<?>[] getServletConfigClasses() {
        return new Class[] {WebMvcConfig.class };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }
}