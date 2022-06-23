package com.fibo.ddp.common.service.strategyx.decisiontree;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTree;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface DecisionTreeService extends IService<DecisionTree> {

    DecisionTreeVo queryById(Long id);

    PageInfo queryByEntity(QueryListParam<DecisionTreeVo> listParam);

    DecisionTreeVo insertDecisionTree(DecisionTreeVo decisionTreeVo);

    DecisionTreeVo updateDecisionTree(DecisionTreeVo decisionTreeVo);

    boolean updateStatus(List<Long> ids, Integer status);

    boolean updateFolder(List<Long> ids, Long folderId);

    List<String> queryFieldEnByVersionId(Long versionId);

    // runner
    DecisionTreeVo queryExecuteDecisionTree(Long id, Long versionId);

    List<JSONObject> setOutput(Long versionId, Map<String,Object> map);

    List<Long> getNodeFieldIds(Long versionId);
}
