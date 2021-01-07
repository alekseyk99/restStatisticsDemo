package com.alekseyk99.springboot.service;

import java.io.Serializable;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "Custom",
        category = "Core",
        elementType = "appender",
        printObject = false)
public class CustomAppender extends AbstractAppender {

    private CustomAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    @PluginFactory
    public static CustomAppender createAppender(
        @PluginAttribute("name") String name,
        @PluginElement("Layout") Layout<? extends Serializable> layout,
        @PluginElement("Filter") Filter filter) {
        
        System.out.println("[CustomAppender#createAppender] name=" + name);
        return new CustomAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        
        String handler = ThreadContext.get(CustomRequestInterceptor.HANDLER_KEY);
        System.out.println("[CustomAppender#append] " + event.getMessage().getFormattedMessage() + " (" + handler + ")");
    }

    @Override
    public void stop() {
        
        System.out.println("[CustomAppender#stop");
        super.stop();
    }

}
