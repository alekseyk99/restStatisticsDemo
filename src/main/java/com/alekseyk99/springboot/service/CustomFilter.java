package com.alekseyk99.springboot.service;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class CustomFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        System.out.println("[CustomFilter#preFilter]");
        try {
            chain.doFilter(req, res);
        } finally {
            ThreadContext.put(CustomRequestBodyAdvice.BODY_KEY, null);
        }
        System.out.println("[CustomFilter#postFilter]");
    }

}
