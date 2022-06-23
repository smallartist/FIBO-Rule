package com.fibo.ddp.common.service.strategyx.decisiontree;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetail;

import java.util.List;

public interface DecisionTreeDetailService extends IService<DecisionTreeDetail> {

    boolean addDecisionTreeDetailList(Long versionId, List<DecisionTreeDetail> list);

    boolean updateByVersionId(Long versionId, List<DecisionTreeDetail> list);

    // runner
    List<DecisionTreeDetail> queryByVersionId(Long versionId);
}
