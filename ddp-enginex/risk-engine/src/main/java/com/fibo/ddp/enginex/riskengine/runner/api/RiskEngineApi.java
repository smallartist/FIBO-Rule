package com.fibo.ddp.enginex.riskengine.runner.api;

import com.fibo.ddp.common.service.common.runner.RunnerSessionManager;
import com.fibo.ddp.common.service.common.runner.SessionData;
import com.fibo.ddp.enginex.riskengine.runner.business.DecisionApiBizData;
import com.fibo.ddp.enginex.riskengine.runner.business.DecisionApiRequest;
import com.fibo.ddp.enginex.riskengine.runner.business.RiskEngineBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("runner")
public class RiskEngineApi {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RiskEngineBusiness riskEngineBusiness;

    @RequestMapping(value = "/decision", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String decision(@RequestBody DecisionApiRequest apiRequest) {
        long start = System.currentTimeMillis();
        DecisionApiBizData bizData = apiRequest.getBiz_data();
        Map<String, Object> map = new HashMap<>();
        map.put("pid", bizData.getBusinessId());
        map.put("uid", "");
        map.put("reqType", 1);
        map.put("engineId", bizData.getEngineId());
        map.put("organId", bizData.getOrganId());

        SessionData sessionData = new SessionData();
        sessionData.setOrganId(bizData.getOrganId());
        sessionData.setEngineId(bizData.getEngineId());
        sessionData.setReqType(1);
        RunnerSessionManager.setSession(sessionData);

        if(bizData.getFields() != null){
            map.put("fields", bizData.getFields());
        } else {
            map.put("fields", new HashMap<>());
        }
        String result = riskEngineBusiness.engineApi(map);
        long end = System.currentTimeMillis();
        logger.info("============ 接口调用耗时：{}ms ============", (end -start));
        return result;
    }

    @RequestMapping(value = "/batchExecute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public List batchExecute(@RequestBody List<DecisionApiRequest> requestList){
        List<String> list = new ArrayList<>();
        for (DecisionApiRequest apiRequest : requestList) {
            String decision = this.decision(apiRequest);
            list.add(decision);
        }
        return list;
    }
}
