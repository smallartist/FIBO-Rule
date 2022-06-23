package com.fibo.ddp.common.service.strategyx.guiderule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.model.strategyx.guiderule.request.RuleListParamV2;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RuleService extends IService<RuleInfo> {

    /**
     * 通过ID查询单条数据
     * @param id 主键
     * @return 实例对象
     */
    RuleVo queryById(Long id, Integer difficulty);

    /**
     * 根据实体类条件查询
     * @param ruleListParam
     * @return
     */
    PageInfo queryByEntity(RuleListParamV2 ruleListParam);

    /**
     * 新增数据
     * @param rule 实例对象
     * @return 实例对象
     */
    RuleVo insertRuleInfo(RuleVo rule);

    /**
     * 修改数据
     * @param rule 实例对象
     * @return 实例对象
     */
    RuleVo updateRuleInfo(RuleVo rule);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateParent(List<Long> ids, Long parentId);

    // runner
    List<JSONObject> setComplexRuleOutput(Long versionId, Map<String,Object> temp, Map<String,Object> input, String outType);

    List<JSONObject> setBaseRuleOutput(Long ruleId,  Map<String,Object> input);

    List<RuleInfo> getRuleList(List<Long> ruleIds);
}
