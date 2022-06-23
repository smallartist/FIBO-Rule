package com.fibo.ddp.common.service.strategyx.scorecard;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.model.strategyx.scorecard.request.ListParam;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 评分卡(Scorecard) Service接口
 */
public interface ScorecardService extends IService<Scorecard> {

    // 新增
    Scorecard addScorecard(ScorecardVo scorecardVo);

    // 获取单个信息
    ScorecardVo getScorecardInfo(Long id);

    // 获取列表(分页信息)
    PageInfo<Scorecard> getScorecardList(ListParam listParam);

    public List<Scorecard> getScorecardList(Map<String, Object> paramMap);

    // 修改
    Scorecard updateScorecard(ScorecardVo scorecardVo);

    // 修改状态（ids，status）
    void updateScorecardStatus(List<Long> ids, Integer status);

//    List<String> queryFieldEnByScorecardId(Integer scorecardId);

    // runner
    List<JSONObject> setOutput(Long scorecardId, Map<String,Object> map);
    ScorecardVo queryExecuteScorecard(Long versionId);
}
