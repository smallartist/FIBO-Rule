package com.fibo.ddp.common.utils.websocket.config;


import com.fibo.ddp.common.utils.websocket.handler.MyWebSocketHandler;
import com.fibo.ddp.common.utils.websocket.interceptor.H5HandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final int MAX_MESSAGE_SIZE = 1024 * 1024;
    private static final long MAX_IDLE = 60 * 60 * 1000;
    /**
     * 注入拦截器
     */
    @Resource
    private H5HandshakeInterceptor h5HandshakeInterceptor;
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(MAX_MESSAGE_SIZE);
        container.setMaxBinaryMessageBufferSize(MAX_MESSAGE_SIZE);
        container.setMaxSessionIdleTimeout(MAX_IDLE);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        
        webSocketHandlerRegistry
                //添加myHandler消息处理对象，和websocket访问地址
                .addHandler(myHandler(), "/ws")
                //设置允许跨域访问
                .setAllowedOrigins("*")
                //添加拦截器可实现用户链接前进行权限校验等操作
                .addInterceptors(h5HandshakeInterceptor);
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyWebSocketHandler();
    }
}
