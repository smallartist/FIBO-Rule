package com.fibo.ddp.common.service.strategyx.guiderule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleVersion;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVersionVo;

import java.util.List;

public interface RuleVersionService extends IService<RuleVersion> {

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<RuleVersionVo> versionList);
    boolean addVersion(RuleVersionVo version);

    boolean copyVersion(RuleVersionVo version);

    boolean updateVersion(RuleVersionVo version);

    boolean updateStatus(StatusParam statusParam);

    // runner
    RuleVersionVo queryById(Long id);

    List<RuleVersionVo> queryVersionListByRuleId(Long ruleId);
}
