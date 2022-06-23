package com.fibo.ddp.common.service.strategyx.scorecard.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.scorecard.Scorecard;
import com.fibo.ddp.common.model.strategyx.scorecard.request.ListParam;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评分卡(Scorecard) Service实现类
 */
@Service("ScorecardServiceV3")
public class ScorecardServiceImpl extends ServiceImpl<ScorecardMapper, Scorecard> implements ScorecardService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScorecardVersionService scorecardVersionService;//评分卡版本
    @Autowired
    private ScorecardMapper scorecardMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private StrategyOutputService outputService;
    @Autowired
    private ScorecardVersionService versionService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    /**
     * 新增
     *
     * @param scorecardVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class, isolation = Isolation.REPEATABLE_READ)
    public Scorecard addScorecard(ScorecardVo scorecardVo) {

        Scorecard scorecard = new Scorecard();
        BeanUtils.copyProperties(scorecardVo, scorecard);
        this.checkUniqueness(scorecard);  // 校验评分卡 name versionCode 唯一性

        SysUser user = SessionManager.getLoginAccount();
        scorecard.setUserId(user.getUserId());
        scorecard.setAuthor(user.getUserId());
        scorecard.setOrganId(user.getOrganId());

        scorecard.setType(1);  // 1表示组织
        scorecard.setStatus(1);

        boolean saveScorecard = this.save(scorecard);

        if (!saveScorecard) {
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }
        List<ScorecardVersionVo> versionList = scorecardVo.getVersionList();
        if (versionList!=null&&versionList.size()==1){
            ScorecardVersionVo versionVo = versionList.get(0);
            versionVo.setScorecardId(scorecard.getId());
            scorecardVersionService.addVersion(versionVo);
        }
        ScorecardVo scorecardInfo = this.getScorecardInfo(scorecard.getId());
        return scorecardInfo;
    }

    /**
     * 获取评分卡详细信息
     * @param id
     * @return
     */
    @Override
    public ScorecardVo getScorecardInfo(Long id) {
        ScorecardVo scorecardVo = new ScorecardVo();
        Scorecard scorecard = this.getById(id);
        if (scorecard == null) {
            throw new ApiException(ErrorCodeEnum.DATA_IS_NOT_EXIST.getCode(), ErrorCodeEnum.DATA_IS_NOT_EXIST.getMessage());
        }
        BeanUtils.copyProperties(scorecard, scorecardVo);
        scorecardVo.setVersionList(scorecardVersionService.queryVersionListByScorecardId(id.longValue()));
        return scorecardVo;
    }

    // 获取列表(分页信息)
    @Override
    public PageInfo<Scorecard> getScorecardList(ListParam listParam) {

        LambdaQueryWrapper<Scorecard> scorecardQueryWrapper = new LambdaQueryWrapper<>();
        if(listParam.getParentId()!=0){
            scorecardQueryWrapper.eq(Scorecard::getParentId, listParam.getParentId());
        }

        scorecardQueryWrapper.in(Scorecard::getStatus, listParam.getStatus());
        if (listParam.getSearch() && !"".equals(listParam.getValue())) {
            scorecardQueryWrapper.like(Scorecard::getName, listParam.getValue());
        }
        scorecardQueryWrapper.orderByDesc(Scorecard::getUpdated);

        if (listParam.getPageNum()!=0&&listParam.getPageSize()!=0){
            PageHelper.startPage(listParam.getPageNum(), listParam.getPageSize());
        }
        List<Scorecard> scorecardList = this.list(scorecardQueryWrapper);
        PageInfo<Scorecard> pageInfo = new PageInfo<>(scorecardList);

        ArrayList<Scorecard> voList = new ArrayList<>();
        for (Scorecard scorecard : scorecardList) {
            String nickName = sysUserMapper.findNickNameById(scorecard.getAuthor());
            scorecard.setAuthorName(nickName);
            voList.add(scorecard);
        }
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public List<Scorecard> getScorecardList(Map<String, Object> paramMap) {
        return scorecardMapper.getScorecardList(paramMap);
    }

    /**
     * 修改评分卡
     *
     * @param scorecardVo
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class, isolation = Isolation.REPEATABLE_READ)
    public Scorecard updateScorecard(ScorecardVo scorecardVo) {
        if (scorecardVo.getId()==null){
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        scorecardMapper.updateById(scorecardVo);
        List<ScorecardVersionVo> versionList = scorecardVo.getVersionList();
        if (versionList!=null&&versionList.size()==1){
            ScorecardVersionVo versionVo = versionList.get(0);
            scorecardVersionService.updateVersion(versionVo);
        }

        ScorecardVo scorecardInfo = this.getScorecardInfo(scorecardVo.getId());
        return scorecardInfo;
    }

    /**
     * 修改评分卡 状态 (批量)
     *
     * @param ids    id集合
     * @param status 状态
     */
    @Override
    public void updateScorecardStatus(List<Long> ids, Integer status) {

        LambdaUpdateWrapper<Scorecard> scorecardUpdateWrapper = new LambdaUpdateWrapper<>();
        scorecardUpdateWrapper.in(Scorecard::getId, ids);
        scorecardUpdateWrapper.set(Scorecard::getStatus, status);

        boolean update = this.update(scorecardUpdateWrapper);
        if (!update) {
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 校验评分卡 name versionCode 唯一性
     *
     * @param scorecard Scorecard实体
     */
    private void checkUniqueness(Scorecard scorecard) {

        // 后期可以改为sql语句形式
        // SELECT COUNT(`id`) FROM `t_scorecard` WHERE `name` = #{name};
        LambdaQueryWrapper<Scorecard> lqwName = new LambdaQueryWrapper<>();
        lqwName.eq(Scorecard::getName, scorecard.getName());
        if (scorecard.getId() != null) {
            lqwName.ne(Scorecard::getId, scorecard.getId());
        }
        int countName = this.count(lqwName);
        if (countName > 0) {
            throw new ApiException(ErrorCodeEnum.SCORECARD_NAME_REPEAT.getCode(), ErrorCodeEnum.SCORECARD_NAME_REPEAT.getMessage());
        }

        // 后期可以改为sql语句形式
        // SELECT COUNT(`id`) FROM `t_scorecard` WHERE `versionCode` = #{versionCode} AND `id` != #{id};
        LambdaQueryWrapper<Scorecard> lqwCode = new LambdaQueryWrapper<>();
        lqwCode.eq(Scorecard::getCode, scorecard.getCode());
        if (scorecard.getId() != null) {
            lqwCode.ne(Scorecard::getId, scorecard.getId());
        }
        int countCode = this.count(lqwCode);
        if (countCode > 0) {
            throw new ApiException(ErrorCodeEnum.SCORECARD_CODE_REPEAT.getCode(), ErrorCodeEnum.SCORECARD_CODE_REPEAT.getMessage());
        }
    }

    @Override
    public List<JSONObject> setOutput(Long scorecardId, Map<String, Object> map) {
        return outputService.setOutput(new StrategyOutput(scorecardId, StrategyType.SCORECARD),map);
    }

    @Override
    public ScorecardVo queryExecuteScorecard(Long versionId) {
        ScorecardVersionVo version = versionService.queryById(versionId);
        Long scorecardId = version.getScorecardId();

        Scorecard scorecard = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_SCORECARD, scorecardId);
            scorecard = redisManager.getByPrimaryKey(key, Scorecard.class);
        } else {
            scorecard = this.getById(scorecardId);
        }
        ScorecardVo scorecardVo = new ScorecardVo();
        BeanUtils.copyProperties(scorecard, scorecardVo);
        scorecardVo.setExecuteVersion(version);
        return scorecardVo;
    }
}
