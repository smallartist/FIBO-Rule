package com.fibo.ddp.manager.web.config;




import com.fibo.ddp.common.service.authx.dictionary.DictionaryService;
import com.fibo.ddp.common.service.datax.cache.DataXCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component("applicationInitConfig")
@Slf4j
public class ApplicationInitConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    DataXCacheService dataXCacheService;

    @Override
    //启动时将所有可连接的通道链接上
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initDictionary();
        initDataXCache();
    }

    private void initDictionary(){
        dictionaryService.refreshCache();
    }

    private void initDataXCache(){
        dataXCacheService.initRedisSub();
    }
}
