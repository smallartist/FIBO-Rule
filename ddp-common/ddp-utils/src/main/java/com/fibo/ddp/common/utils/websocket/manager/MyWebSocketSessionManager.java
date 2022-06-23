package com.fibo.ddp.common.utils.websocket.manager;


import com.fibo.ddp.common.utils.websocket.service.MyWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyWebSocketSessionManager {
    /**
     * 保存连接 session 的地方
     */
    private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();
    /**
     * 添加 session
     *
     * @param key
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    public static AtomicInteger getOnlineNum() {
        return onlineNum;
    }
    /**
     * 删除 session,会返回删除的 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
                log.info("【WebSocket连接关闭】MyWebSocketSessionManager，session:{}",session);
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得 session
     *
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        if (SESSION_POOL!=null) {
            return SESSION_POOL.get(key);
        }
        return null;
    }

    /**
     * 添加链接人数
     */
    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
    }

    /**
     * 移除链接人数
     */
    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }


    public static boolean sendHeartbeat(Map<String, Object> map) {
        if (SESSION_POOL!=null&&!SESSION_POOL.isEmpty()){
            for (Map.Entry<String, WebSocketSession> entry : SESSION_POOL.entrySet()) {
                try {
                    boolean b = MyWebSocketService.sendMessageToSession(map, entry.getValue());
                    if (!b){
                        //发送失败
                        removeAndClose(entry.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return true;
    }

    public static boolean sendTextToWeb(String string){
        if (SESSION_POOL!=null&&!SESSION_POOL.isEmpty()){
            for (Map.Entry<String, WebSocketSession> entry : SESSION_POOL.entrySet()) {
                if (entry.getKey().startsWith("h5_")){
                    try {
                        boolean b = MyWebSocketService.sendTextMessageToSession(string, entry.getValue());
                        if (!b){
                            //发送失败
                            log.error("【缓存更新通知失败】，websocket向web端推送消息失败，uid：{}",entry.getKey());
                            removeAndClose(entry.getKey());
                        }
                    }catch (Exception e){
                        log.error("【缓存更新通知异常】，websocket向web端推送消息失败,uid：{}, 异常信息：{}",entry.getKey(),e);
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return true;
    }
}
