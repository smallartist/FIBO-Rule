package com.fibo.ddp.common.service.analyse.impl;

import com.fibo.ddp.common.model.analyse.vo.AnalyseData;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.service.analyse.*;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.utils.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class AnalyseServiceImpl implements AnalyseService {
    @Autowired
    private AnalyseDecisionResultService decisionResultService;
    @Autowired
    private AnalyseDecisionTablesService analyseDecisionTablesService;
    @Autowired
    private AnalyseEngineCallService engineCallService;
    @Autowired
    private AnalyseEngineNodeService engineNodeService;
    @Autowired
    private AnalyseRuleService analyseRuleService;
    @Autowired
    private AnalyseScorecardService analyseScorecardService;
//    @Autowired
//    private AnalyseEngineSummaryService analyseEngineSummaryService;
    @Override
    public AnalyseData getAnalyseData(AnalyseRequestParam param) {
        param = initParam(param);
        AnalyseData analyseData = new AnalyseData();
        analyseData.setDecisionResultList(decisionResultService.getAnalyseData(param));
        analyseData.setDecisionTablesList(analyseDecisionTablesService.getAnalyseData(param));
        analyseData.setCallCountList(engineCallService.getAnalyseData(param));
        analyseData.setEngineNodeList(engineNodeService.getAnalyseData(param));
        analyseData.setRuleList(analyseRuleService.getAnalyseData(param));
        analyseData.setScorecardList(analyseScorecardService.getAnalyseData(param));
//        analyseData.setEngineSummary(analyseEngineSummaryService.getAnalyseData(inputParam));
        return analyseData;
    }
    private AnalyseRequestParam initParam(AnalyseRequestParam param){
        if (param==null
                ||param.getEngineId()==null
                ||param.getVersionId()==null
                ||param.getStart()==null
                ||param.getEnd()==null){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        param.setUser(SessionManager.getLoginAccount());
        Calendar c =new GregorianCalendar();
        c.setTime(param.getEnd());
        c.add(Calendar.DATE,1);
        param.setEnd(c.getTime());
        return param;

    }
}
