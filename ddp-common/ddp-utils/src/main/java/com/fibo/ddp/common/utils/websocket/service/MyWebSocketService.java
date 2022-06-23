package com.fibo.ddp.common.utils.websocket.service;

import com.alibaba.fastjson.JSON;

import com.fibo.ddp.common.utils.websocket.manager.MyWebSocketSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;

public class MyWebSocketService {
    private static Logger logger = LoggerFactory.getLogger(MyWebSocketService.class);

    public static boolean sendMessageToSession(Object message,String sessionId){
        WebSocketSession webSocketSession = MyWebSocketSessionManager.get(sessionId);
        if (webSocketSession==null){
            return false;
        }
        boolean b = sendMessageToSession(message,webSocketSession );
        return b;
    }

    public static boolean sendMessageToSession(Object message, WebSocketSession session){
        boolean result = false;
        if (message==null||session==null){
            return false;
        }
        try {
            result = sendTextMessageToSession(JSON.toJSONString(message), session);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean sendTextMessageToSession(String message,WebSocketSession session) throws IOException {
        if (StringUtils.isNotBlank(message)&&session!=null){
            TextMessage textMessage = new TextMessage(message);
            session.sendMessage(textMessage);
            logger.info("向"+session.getAttributes().get("uid")+"发送消息:"+message);
            return true;
        }
        return false;
    };

    public static boolean sendPingToSession(WebSocketSession session) throws IOException {
        session.sendMessage(new PingMessage());
        return true;
    };

    public static boolean sendPongToSession(WebSocketSession session) throws IOException {
        session.sendMessage(new PongMessage());
        return false;
    }

    public static boolean sendBinaryMessageToSession(String message,WebSocketSession session) throws IOException {
        session.sendMessage(new BinaryMessage(message.getBytes()));
        return true;
    };

    public static boolean sendTextToWeb(String string){
        return MyWebSocketSessionManager.sendTextToWeb(string);
    }
    public static boolean sendObjectToWeb(Object o){
        return sendTextToWeb(JSON.toJSONString(o));
    }

    public static boolean sendHeartbeat(Map<String, Object> map){
        boolean b= MyWebSocketSessionManager.sendHeartbeat(map);
        return true;
    }
}
