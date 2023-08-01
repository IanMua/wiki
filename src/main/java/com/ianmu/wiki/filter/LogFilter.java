package com.ianmu.wiki.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//@Component
public class LogFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("---------- LogFilter Start ----------");
        LOG.info("请求地址 : {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("远程地址 : {}", request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        LOG.info("---------- LogFilter 结束 耗时 : {} ms ----------", System.currentTimeMillis() - startTime);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("初始化");
    }

    @Override
    public void destroy() {

    }
}
