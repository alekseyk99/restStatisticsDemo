package com.alekseyk99.springboot.service;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CustomRequestInterceptor extends HandlerInterceptorAdapter {

    public static final String HANDLER_KEY = "handler";

    @Override
    public boolean preHandle(
      HttpServletRequest request, 
      HttpServletResponse response, 
      Object handler) {
        if (handler instanceof HandlerMethod) {
            
            String message = getShortLogMessage((HandlerMethod) handler);
            ThreadContext.put(HANDLER_KEY, message);
            System.out.println("[CustomRequestInterceptor#preHandle] " + message);
        }

        return true;
    }
 
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadContext.put(HANDLER_KEY, null);
        System.out.println("[CustomRequestInterceptor#afterCompletion]");
    }

    private String getShortLogMessage(HandlerMethod hm) {
        Method method = hm.getMethod();
        StringBuilder sb = new StringBuilder();
        sb.append(hm.getBeanType().getSimpleName()).append("#").append(method.getName()).append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            sb.append(parameterTypes[0].getSimpleName());
            for (int i = 1; i < parameterTypes.length; i++) {
                sb.append(",").append(parameterTypes[i].getSimpleName());
            }
        }
        sb.append(")");
        return  sb.toString();
    }

}
