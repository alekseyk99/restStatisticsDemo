package com.alekseyk99.springboot.service;

import java.io.IOException;
import java.lang.reflect.Type;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import com.alekseyk99.springboot.controller.WebController;
import com.alekseyk99.springboot.dto.Transaction;

@ControllerAdvice
public class CustomRequestBodyAdvice implements RequestBodyAdvice {

    public static final String BODY_KEY = "body";

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        ThreadContext.put(BODY_KEY, body.toString());
        System.out.println("[CustomRequestBodyAdvice#afterBodyRead] body=" + body.toString());
        return body;
    }
    
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        System.out.println("[CustomRequestBodyAdvice#beforeBodyRead]");
        return inputMessage;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println("[CustomRequestBodyAdvice#handleEmptyBody]");
        return body;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, 
            Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getContainingClass() == WebController.class 
                && targetType.getTypeName() == Transaction.class.getTypeName();
    }


}