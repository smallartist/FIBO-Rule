package com.fibo.ddp.common.utils.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.fibo.ddp.common.utils.websocket.constant.SendToWebConst;
import com.fibo.ddp.common.utils.websocket.manager.MyWebSocketSessionManager;
import com.fibo.ddp.common.utils.websocket.model.vo.SendToWebModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        log.info("获取到"+session.getAttributes().get("uid")+"消息 >> " + message.getPayload());
       if(message.getPayload().equals("ping")){
            session.sendMessage(new TextMessage("pong"));
            //存储处理生存节点的信息
//           MessageHandler.handlerReceivePing(session);
        }else {
            MessageHandler.handlerReceiveMessage(session,message.getPayload());

        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        super.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws
            Exception {
        log.info("获取到拦截器中用户ID : " + session.getAttributes().get("uid"));
        String uid = session.getAttributes().get("uid").toString();
        //TODO: 重复链接拒绝掉
        WebSocketSession old = MyWebSocketSessionManager.get(uid);
        MyWebSocketSessionManager.addOnlineCount();
        if (old!=null&&old.isOpen()){
            session.sendMessage(new TextMessage(JSON.toJSONString( new SendToWebModel(SendToWebConst.CLIENT_ID_REPEAT,"连接失败,客户端:"+uid+"已存在一个链接，如果确需连接，请断开此客户端其他链接后再重试"))));
            session.close();
            return;
        }
         //TODO: 提示再其他地方上线，本机被顶替
        MyWebSocketSessionManager.add(uid,session);
        log.info(uid + "加入webSocket！当前人数为" + MyWebSocketSessionManager.getOnlineNum());
        session.sendMessage(new TextMessage(JSON.toJSONString( new SendToWebModel(SendToWebConst.connected,"欢迎连接到ws服务! 当前人数为：" + MyWebSocketSessionManager.getOnlineNum()))));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        String uid = session.getAttributes().get("uid").toString();
        log.warn(uid+"》》断开连接!");
        MyWebSocketSessionManager.remove(uid);
        MyWebSocketSessionManager.remove(uid);
        MyWebSocketSessionManager.subOnlineCount();
    }


}
