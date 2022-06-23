package com.fibo.ddp.common.service.strategyx.decisiontree;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeVersion;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;

import java.util.List;

public interface DecisionTreeVersionService extends IService<DecisionTreeVersion> {

    List<String> queryFieldEnByVersionId(Long versionId);

    List<DecisionTreeVersionVo> queryVersionListByDecisionTreeId(Long decisionTreeId);

    int addVersionList(List<DecisionTreeVersionVo> versionList);

    boolean addVersion(DecisionTreeVersionVo version);

    boolean copyVersion(DecisionTreeVersionVo version);

    boolean updateVersion(DecisionTreeVersionVo version);

    boolean updateStatus(StatusParam statusParam);

    // runner
    DecisionTreeVersionVo queryById(Long id);

    List<String> queryFieldEnByVersionIdRunner(Long versionId);
}

