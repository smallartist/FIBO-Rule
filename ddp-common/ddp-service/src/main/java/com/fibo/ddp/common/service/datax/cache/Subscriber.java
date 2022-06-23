package com.fibo.ddp.common.service.datax.cache;

import com.fibo.ddp.common.model.datax.consts.DataXCacheConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

@Slf4j
@Component
public class Subscriber extends JedisPubSub {
    @Autowired
    private DataXCacheService dataXCacheService;

    public Subscriber() {
    }
    @Override
    public void onMessage(String channel, String message) {
        log.info(String.format("receive redis published message, channel %s, message %s", channel, message));
        if (DataXCacheConst.DATAX_PUBSUB_CACHE_REDIS_CHANNEL.equals(channel)){
            dataXCacheService.notifyCondion();
            dataXCacheService.sendChangeToWeb();
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        log.info(String.format("receive redis published message, channel %s, message %s", channel, message));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        log.info(String.format("unsubscribe redis channel, pattern %s, subscribedChannels %d",
                pattern, subscribedChannels));
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info(String.format("subscribe redis channel success, pattern %s, subscribedChannels %d",
                pattern, subscribedChannels));
    }
}