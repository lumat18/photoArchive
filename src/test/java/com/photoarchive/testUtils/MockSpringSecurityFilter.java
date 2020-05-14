package com.photoarchive.testUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MockSpringSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        SecurityContextHolder.getContext()
                .setAuthentication((Authentication)httpServletRequest.getUserPrincipal());
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
