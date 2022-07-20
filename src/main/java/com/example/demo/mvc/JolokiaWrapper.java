package com.example.demo.mvc;

import org.jolokia.http.AgentServlet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

@RestController("/jolokia")
public class JolokiaWrapper implements InitializingBean,
        ApplicationContextAware, ServletContextAware {

    private final ServletWrappingController controller = new ServletWrappingController();

    public JolokiaWrapper() {
        this.controller.setServletClass(AgentServlet.class);
        this.controller.setServletName("jolokia");
    }

    @RequestMapping("/**")
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return this.controller.handleRequest(new PathStripper(request, "/jolokia"),
                response);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.controller.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.controller.setApplicationContext(applicationContext);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.controller.setServletContext(servletContext);
    }

    private static class PathStripper extends HttpServletRequestWrapper {

        private final String path;

        private final UrlPathHelper urlPathHelper;

        PathStripper(HttpServletRequest request, String path) {
            super(request);
            this.path = path;
            this.urlPathHelper = new UrlPathHelper();
        }

        @Override
        public String getPathInfo() {
            String value = this.urlPathHelper.decodeRequestString(
                    (HttpServletRequest) getRequest(), super.getRequestURI());
            if (value.contains(this.path)) {
                value = value.substring(value.indexOf(this.path) + this.path.length());
            }
            int index = value.indexOf("?");
            if (index > 0) {
                value = value.substring(0, index);
            }
            while (value.startsWith("/")) {
                value = value.substring(1);
            }
            return value;
        }
    }
}
