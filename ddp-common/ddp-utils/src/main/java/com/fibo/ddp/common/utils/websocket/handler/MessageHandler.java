package com.fibo.ddp.common.utils.websocket.handler;


import com.fibo.ddp.common.utils.websocket.manager.MyWebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
/**
 * webSocket接收到消息处理的类
 */
public class MessageHandler {
    //处理收到的websocket消息
    public synchronized static void handlerReceiveMessage(WebSocketSession session, String message) {
        try {
            String uid = session.getAttributes().get("uid").toString();
            if (uid == null) {
                return;
            }
            if (StringUtils.isNotBlank(message)) {
                session.sendMessage(new TextMessage(String.format("收到用户：【%s】发来的【%s】", session.getAttributes().get("uid"), message)));
            }
        } catch (Exception e) {
            log.error("信息处理异常,message：" + message);
            e.printStackTrace();
        }
    }

}
