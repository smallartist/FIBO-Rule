package com.fibo.ddp.common.service.strategyx.knowledge.impl;

import com.fibo.ddp.common.dao.strategyx.knowledge.RuleMapper;
import com.fibo.ddp.common.model.strategyx.knowledge.Rule;
import com.fibo.ddp.common.service.strategyx.knowledge.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ClassName:RuleServiceImpl <br/>
 * Description: 规则接口实现类. <br/>
 */
@Service
public class RuleServiceImpl implements RuleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RuleMapper ruleMapper;

    @Override
    public List<Rule> getRuleList(Map<String, Object> paramMap) {
        return ruleMapper.getRuleList(paramMap);
    }
}
