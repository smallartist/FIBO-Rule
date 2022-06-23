package com.fibo.ddp.common.service.strategyx.scorecard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardVersion;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;

import java.util.List;

/**
 * 评分卡(Scorecard) Service接口
 */
public interface ScorecardVersionService extends IService<ScorecardVersion> {

    List<ScorecardVersionVo> queryVersionListByScorecardId(Long scorecardId);

    List<String> queryFieldEnByVersionId(Long versionId);

    int addVersionList(List<ScorecardVersionVo> versionList);
    boolean addVersion(ScorecardVersionVo version);

    boolean copyVersion(ScorecardVersionVo version);

    boolean updateVersion(ScorecardVersionVo version);

    boolean updateStatus(StatusParam statusParam);

    // runner
    ScorecardVersionVo queryById(Long id);
}
