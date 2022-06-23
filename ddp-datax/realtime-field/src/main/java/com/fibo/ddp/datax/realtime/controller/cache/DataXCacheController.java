package com.fibo.ddp.datax.realtime.controller.cache;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.service.datax.cache.DataXCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/v3/datax/cache")
public class DataXCacheController {
    //长轮询间隔时间
    private static long REQUEST_INTERVAL_SECONDS = 10;
    @Autowired
    private DataXCacheService dataXCacheService;

    @PostMapping("/setRequestInterval/{interval}")
    public ResponseEntityDto setRequestInterval(@PathVariable("interval") long interval) {
        if (interval > 0) {
            REQUEST_INTERVAL_SECONDS = interval;
        }
        return ResponseEntityBuilder.buildNormalResponse();
    }

    @PostMapping("/queryForChange")
    public ResponseEntityDto queryForChange(@RequestBody Map<String, String> param) {
        Map result = new HashMap();
        Map map = dataXCacheService.queryForChange(param,REQUEST_INTERVAL_SECONDS);
        if (map != null) {
            result = map;
        }
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
    @PostMapping("/push")
    public ResponseEntityDto push(@RequestBody Map<String, Object> param) {
         dataXCacheService.pushRedisSub(null,"");
        return ResponseEntityBuilder.buildNormalResponse();
    }
}
