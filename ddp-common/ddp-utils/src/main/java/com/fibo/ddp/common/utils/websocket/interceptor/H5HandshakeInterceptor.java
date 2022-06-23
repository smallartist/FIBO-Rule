package com.fibo.ddp.common.utils.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
public class H5HandshakeInterceptor implements HandshakeInterceptor {
    /**
     * 握手之前，若返回false，则不建立链接 *
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse
            response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        //将用户id放入socket处理器的会话(WebSocketSession)中
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
        //获取参数
        String userId = serverHttpRequest.getServletRequest().getParameter("userId");
        //不是以h5开头的连接过滤掉
        if (StringUtils.isBlank(userId) || !userId.startsWith("h5_")){
            log.info("【webSocket拒绝连接】uid:{}, 连接信息",userId, request);
            return false;
        }
        attributes.put("uid", userId);
        //可以在此处进行权限验证，当用户权限验证通过后，进行握手成功操作，验证失败返回false
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse
            response, WebSocketHandler wsHandler, Exception exception) {
        log.info("【webSocket连接完成】,uid:{}", ((ServletServerHttpRequest) request).getServletRequest().getParameter("userId"));
    }
}
