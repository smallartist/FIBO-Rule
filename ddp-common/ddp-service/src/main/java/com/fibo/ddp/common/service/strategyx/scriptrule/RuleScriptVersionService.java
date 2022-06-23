package com.fibo.ddp.common.service.strategyx.scriptrule;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;

import java.util.List;

/**
 * (RuleScriptVersion)表服务接口
 */
public interface RuleScriptVersionService extends IService<RuleScriptVersion> {

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<RuleScriptVersion> versionList);

    boolean addVersion(RuleScriptVersion version);

    boolean copyVersion(RuleScriptVersion version);

    boolean updateVersion(RuleScriptVersion version);

    boolean updateStatus(StatusParam statusParam);

    // runner
    RuleScriptVersion queryById(Long id);

    List<RuleScriptVersion> queryVersionListByRuleId(Long ruleId);

    List<String> queryFieldEnByVersionIds(List<Long> versionId);
}
