package com.fibo.ddp.enginex.runner.ksession;

import com.fibo.ddp.common.service.redis.RedisManager;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * kSession工厂类
 */
@Component
public class KSessionFactory extends BaseKeyedPooledObjectFactory<String, StatefulKnowledgeSession> {

    @Autowired
    private RedisManager redisManager;

    @Override
    public StatefulKnowledgeSession create(String key) throws Exception {
        StatefulKnowledgeSession kSession = null;
        try {
            String ruleString = redisManager.get(key);
            if(ruleString == null){
                throw new Exception("create kSession fail, key is "+ key + ", ruleString is null！");
            }

            long start = System.currentTimeMillis();
            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kb.add(ResourceFactory.newByteArrayResource(ruleString.getBytes("utf-8")), ResourceType.DRL);
            KnowledgeBuilderErrors errors = kb.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
            kBase.addKnowledgePackages(kb.getKnowledgePackages());
            kSession = kBase.newStatefulKnowledgeSession();
            long end = System.currentTimeMillis();
            System.out.println("------------------drools kSession创建耗时：" + (end - start) + " ----------------------");
        } catch (Exception e) {
            throw e;
        }

        return kSession;
    }

    @Override
    public PooledObject<StatefulKnowledgeSession> wrap(StatefulKnowledgeSession kSession) {
        return new DefaultPooledObject<StatefulKnowledgeSession>(kSession);
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}
