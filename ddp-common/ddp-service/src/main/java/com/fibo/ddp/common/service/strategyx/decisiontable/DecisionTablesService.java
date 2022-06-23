package com.fibo.ddp.common.service.strategyx.decisiontable;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTables;
import com.fibo.ddp.common.model.strategyx.decisiontable.request.DecisionTablesListParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * (DecisionTables)表服务接口
 */
public interface DecisionTablesService extends IService<DecisionTables> {

    DecisionTablesVo queryById(Long id);

    PageInfo queryByEntity(DecisionTablesListParam listParam);

    DecisionTablesVo insertDecisionTables(DecisionTablesVo decisionTablesVo);

    DecisionTablesVo updateDecisionTables(DecisionTablesVo decisionTablesVo);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateParent(List<Long> ids, Long folderId);

    List<String> queryFieldEnByDecisionTablesVersionId(Long decisionTablesId);

    // runner
    DecisionTablesVo queryByVersionId(Long id);

    List<JSONObject> setOutput(Long decisionTablesId, Map<String,Object> map);
}
