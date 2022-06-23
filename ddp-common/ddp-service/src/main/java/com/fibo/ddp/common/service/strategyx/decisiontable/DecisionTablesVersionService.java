package com.fibo.ddp.common.service.strategyx.decisiontable;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesVersion;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;

import java.io.Serializable;
import java.util.List;

/**
 * (DecisionTablesVersion)表服务接口
 */
public interface DecisionTablesVersionService extends IService<DecisionTablesVersion> {

    List<DecisionTablesVersionVo> queryVersionListByDecisionTablesId(Serializable decisionTablesId);

    List<String> queryFieldEnByVersionId(Long versionId);
    int addVersionList(List<DecisionTablesVersionVo> versionList);

    boolean addVersion(DecisionTablesVersionVo version);
    boolean copyVersion(DecisionTablesVersionVo version);

    boolean updateVersion(DecisionTablesVersionVo version);

    boolean updateStatus(StatusParam statusParam);

    // runner
    DecisionTablesVersionVo queryById(Long id);
}
