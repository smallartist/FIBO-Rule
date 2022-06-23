package com.fibo.ddp.strategyx.scorecard.controller;

import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.model.common.requestParam.UpdateStatusParam;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.model.strategyx.scorecard.request.ListParam;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.fibo.ddp.common.service.monitor.logger.ArchivesLog;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.utils.constant.OpTypeConst;
import com.fibo.ddp.common.utils.util.strategyx.SectionUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评分卡(Scorecard) Controller
 */
@Controller("ScorecardControllerV3")
@RequestMapping("/v3/scorecard")
@ResponseBody
public class ScorecardController {

    @Autowired
    private ScorecardService scorecardService;
    @Resource
    private ScorecardVersionService versionService;

    /**
     * 查询指定版本下的内容
     * @param versionId
     * @return
     */
    @PostMapping("/getScorecardVersionInfo/{versionId}")
    public ResponseEntityDto getVersionInfo(@PathVariable Long versionId) {
        ScorecardVersionVo version =versionService.queryById(versionId);
        return ResponseEntityBuilder.buildNormalResponse(version);
    }

    /**
     * 查询评分卡基本信息
     * @param scorecardId
     * @return
     */
    @PostMapping("/getScorecardInfo/{scorecardId}")
    public ResponseEntityDto getScorecardInfo(@PathVariable Long scorecardId) {
        ScorecardVo scorecardVo = scorecardService.getScorecardInfo(scorecardId);
        return ResponseEntityBuilder.buildNormalResponse(scorecardVo);
    }

    // 新增
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.SAVE_SCORECARD)
    public ResponseEntityDto<Object> add(@RequestBody ScorecardVo scorecardVo) {
        Scorecard scorecard = scorecardService.addScorecard(scorecardVo);
        return ResponseEntityBuilder.buildNormalResponse(scorecard);
    }

    // 获取列表(分页信息)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntityDto<Object> list(@RequestBody ListParam listParam) {
        if (listParam.getValue() != null) {
            listParam.setValue(listParam.getValue().trim());  // 去除 前后空格
        }
        PageInfo<Scorecard> pageInfo = scorecardService.getScorecardList(listParam);

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("scorecardList", pageInfo.getList());
        pageInfo.setList(null);
        bodyMap.put("pageInfo", pageInfo);
        return ResponseEntityBuilder.buildNormalResponse(bodyMap);
    }

    // 修改
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SCORECARD)
    public ResponseEntityDto<Object> update(@RequestBody ScorecardVo scorecardVo) {
        Scorecard scorecard = scorecardService.updateScorecard(scorecardVo);
        return ResponseEntityBuilder.buildNormalResponse(scorecard);
    }

    // 修改状态（ids，status）
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ArchivesLog(operationType = OpTypeConst.UPDATE_SCORECARD_STATUS)
    public ResponseEntityDto<Object> updateStatus(@RequestBody UpdateStatusParam param) {
        List<Long> ids = new ArrayList<>();
        for (String s : param.getIds().split(",")) {
            ids.add(Long.valueOf(s));
        }
        Integer status = param.getStatus();
        scorecardService.updateScorecardStatus(ids, status);
        return ResponseEntityBuilder.buildNormalResponse(null);
    }

    /**
     * @api {POST} /v2/knowledge/scorecard/section 5.07. 区间校验
     * @apiGroup scorecard
     * @apiVersion 2.0.0
     * @apiSuccess {String} status 状态: 1成功, 0失败
     * @apiSuccess {String} data.result 1:区间有效、-1:区间不完整或有重叠
     * @apiParamExample {json} 请求示例：
     * ["(,1]","(1,6]","(6,15]","(15,20]","(20,)"]
     * @apiSuccessExample {json} 成功返回数据示例：
     * {"status":"1","error":"00000000","msg":null,"data":{"result":"1","msg":"区间有效!"}}
     */
    @RequestMapping(value = "/section", method = RequestMethod.POST)
    public ResponseEntityDto<Object> saveEngineNode(@RequestBody List<String> sections) {

        // @RequestParam("sections[]") List<String> sections
        Map<String, String> resultMap = new HashMap<String, String>();
        // 验证区间完整性
        if (SectionUtils.checkSectionValid(sections)) {
            if (SectionUtils.checkSectionCoincide(sections)) {
                resultMap.put("result", "-1");
                resultMap.put("msg", "区间有重叠,请核准!");
            } else {
                resultMap.put("result", "1");
                resultMap.put("msg", "区间有效!");
            }
        } else {
            resultMap.put("result", "-1");
            resultMap.put("msg", "区间不完整,请核准!");
        }
        return ResponseEntityBuilder.buildNormalResponse(resultMap);
    }

}
