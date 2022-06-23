package com.fibo.ddp.common.service.strategyx.guiderule.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.guiderule.RuleInfoMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.guiderule.RuleInfo;
import com.fibo.ddp.common.model.strategyx.guiderule.request.RuleListParamV2;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVersionVo;
import com.fibo.ddp.common.model.strategyx.guiderule.vo.RuleVo;
import com.fibo.ddp.common.model.strategyx.scriptrule.RuleScriptVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleService;
import com.fibo.ddp.common.service.strategyx.guiderule.RuleVersionService;
import com.fibo.ddp.common.service.strategyx.scriptrule.RuleScriptVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.strategyx.RuleConst;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 组织规则维护表(RuleInfo)表服务实现类
 */
@Service("ruleService2")
public class RuleServiceImpl extends ServiceImpl<RuleInfoMapper, RuleInfo> implements RuleService {
    @Resource
    private RuleInfoMapper ruleInfoMapper;

    @Autowired
    private RuleVersionService versionService;

    @Resource
    private StrategyOutputService outputService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private RuleScriptVersionService ruleScriptVersionService;

    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public RuleVo queryById(Long id, Integer difficulty) {
        //查询规则
        RuleInfo ruleInfo = ruleInfoMapper.selectById(id);
        if (ruleInfo == null) {
            return null;
        }
        RuleVo ruleVo = new RuleVo();
        BeanUtils.copyProperties(ruleInfo, ruleVo);
        List<StrategyOutput> strategyOutputList = new ArrayList<>();
        if (difficulty == null || (difficulty != 1 && difficulty != 2 && difficulty != 3)) {
            difficulty = ruleInfo.getDifficulty();
        }
        switch (difficulty) {
            case 2:
                //查询版本
                List<RuleVersionVo> ruleVersionList = versionService.queryVersionListByRuleId(id);
                ruleVo.setRuleVersionList(ruleVersionList);
                break;
            case 3:
                //脚本规则集
                List<RuleScriptVersion> ruleScriptVersionList = ruleScriptVersionService.queryVersionListByRuleId(id);
                ruleVo.setRuleScriptVersionList(ruleScriptVersionList);
                break;
        }
        return ruleVo;
    }

    @Override
    public PageInfo queryByEntity(RuleListParamV2 ruleListParam) {

        RuleInfo query = ruleListParam.getRuleInfo();
        Integer pageNum = ruleListParam.getPageNum();
        Integer pageSize = ruleListParam.getPageSize();
        if (query != null && query.getName() != null && !"".equals(query.getName())) {
            query.setName("%" + query.getName() + "%");
        }
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        query.setOrganId(SessionManager.getLoginAccount().getOrganId());
        List<RuleInfo> ruleInfoList = ruleInfoMapper.queryRuleList(query);
        PageInfo pageInfo = new PageInfo(ruleInfoList);

        //TODO 循环查用户表，待优化
        for (RuleInfo info : ruleInfoList) {
            if (info != null && info.getAuthor() != null) {
                info.setAuthorName(sysUserMapper.findNickNameById(info.getAuthor()));
            }
        }
        return pageInfo;
    }


    @Override
    @Transactional
    public RuleVo insertRuleInfo(RuleVo vo) {
        Integer difficulty = vo.getDifficulty();
        if (difficulty == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        //初始化基本参数
        RuleVo ruleVo = initParam(vo);
        //拷贝VO到Info对象
        RuleInfo ruleInfo = new RuleInfo();
        BeanUtils.copyProperties(ruleVo, ruleInfo);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(ruleInfo);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.RULE_SAVE_ERROR.getCode(), ErrorCodeEnum.RULE_SAVE_ERROR.getMessage());
        }
        Long ruleId = ruleInfo.getId();

        switch (difficulty) {
            case 2:
                //插入版本表数据
                List<RuleVersionVo> ruleVersionList = ruleVo.getRuleVersionList();
                if (ruleVersionList != null && ruleVersionList.size() > 0) {
                    for (RuleVersionVo ruleVersionVo : ruleVersionList) {
                        ruleVersionVo.setRuleId(ruleId);
                    }
                    versionService.addVersionList(ruleVersionList);
                }
                ruleVo.setRuleVersionList(versionService.queryVersionListByRuleId(vo.getId()));
                break;
            case 3:
                //脚本规则集插入版本表
                List<RuleScriptVersion> ruleScriptVersionList = ruleVo.getRuleScriptVersionList();
                if (ruleScriptVersionList!=null&&ruleScriptVersionList.size()>0){
                    for (RuleScriptVersion ruleScriptVersion : ruleScriptVersionList) {
                        ruleScriptVersion.setRuleId(ruleId);
                    }
                    ruleScriptVersionService.addVersionList(ruleScriptVersionList);
                }
                ruleVo.setRuleScriptVersionList(ruleScriptVersionService.queryVersionListByRuleId(ruleId));
//                ruleVo.setRuleVersionList(ruleScriptVersionService.queryVersionListByRuleId(ruleId));
                break;
        }
        return ruleVo;
    }

    /**
     * 修改数据
     *
     * @param vo 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public RuleVo updateRuleInfo(RuleVo vo) {
        if (vo.getId() == null || vo.getDifficulty() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Integer difficulty = vo.getDifficulty();
        RuleInfo ruleInfo = new RuleInfo();
        BeanUtils.copyProperties(vo, ruleInfo);
        boolean updateResult = this.updateById(ruleInfo);
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }
        Long ruleId = vo.getId();
        switch (difficulty) {
            case 2:
                List<RuleVersionVo> ruleVersionList = vo.getRuleVersionList();
                if (ruleVersionList != null && ruleVersionList.size() > 0) {
                    RuleVersionVo ruleVersionVo = ruleVersionList.get(0);
                    ruleVersionVo.setRuleId(vo.getId());
                    versionService.updateVersion(ruleVersionVo);
                }
                vo.setRuleVersionList(versionService.queryVersionListByRuleId(ruleId));
                break;
            case 3:
                //脚本规则集插入版本表
                List<RuleScriptVersion> ruleScriptVersionList = vo.getRuleScriptVersionList();
                if (ruleScriptVersionList!=null&&ruleScriptVersionList.size()>0){
                    RuleScriptVersion ruleScriptVersion = ruleScriptVersionList.get(0);
                    ruleScriptVersion.setRuleId(ruleId);
                    ruleScriptVersionService.updateVersion(ruleScriptVersion);
                }
                vo.setRuleScriptVersionList(ruleScriptVersionService.queryVersionListByRuleId(ruleId));
//                vo.setRuleVersionList(ruleScriptVersionService.queryVersionListByRuleId(ruleId));
                break;
        }
        return vo;
    }

    /**
     * 通过主键修改状态，支持批量
     *
     * @param ids    主键id集合
     * @param status 状态代号
     * @return
     */
    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<RuleInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(RuleInfo::getId, ids);
        RuleInfo info = new RuleInfo();
        info.setStatus(status);
        return this.update(info, wrapper);
    }

    @Override
    @Transactional
    public boolean updateParent(List<Long> ids, Long parentId) {
        LambdaUpdateWrapper<RuleInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(RuleInfo::getId, ids);
        RuleInfo info = new RuleInfo();
        info.setParentId(parentId);
        return this.update(info, wrapper);
    }

    //唯一性检查
    private boolean checkUniqueness(RuleVo vo) {
        LambdaQueryWrapper<RuleInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(RuleInfo::getName, vo.getName());
        queryWrapper.ne(RuleInfo::getStatus, -1);
        queryWrapper.eq(RuleInfo::getOrganId, SessionManager.getLoginAccount().getOrganId());
        queryWrapper.and(wrapper -> wrapper.eq(RuleInfo::getName, vo.getName()).or().eq(RuleInfo::getCode, vo.getCode()));
        RuleInfo info = this.getOne(queryWrapper);
        if (info != null) {
            if (info.getCode().equals(vo.getCode())) {
                throw new ApiException(ErrorCodeEnum.RULE_CODE_REPEAT.getCode(), ErrorCodeEnum.RULE_CODE_REPEAT.getMessage());
            } else if (info.getName().equals(vo.getName())) {
                throw new ApiException(ErrorCodeEnum.RULE_NAME_REPEAT.getCode(), ErrorCodeEnum.RULE_NAME_REPEAT.getMessage());
            }
        }
        return true;
    }

    //新插入数据的准备工作
    private RuleVo initParam(RuleVo vo) {
        this.checkUniqueness(vo);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setUserId(sysUser.getUserId());
        vo.setOrganId(sysUser.getOrganId());
        vo.setAuthor(sysUser.getUserId());
        //加入状态信息
        vo.setType(RuleConst.TYPE_ORGAN);
        vo.setStatus(RuleConst.STATUS_ENABLED);
        //加入规则类型
        if (vo == null || vo.getRuleAudit() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        switch (vo.getRuleAudit()) {
            case RuleConst.RULE_AUDIT_TERMINATION:
                vo.setRuleType(RuleConst.RULE_TYPE_TERMINATION);
                break;
            case RuleConst.RULE_AUDIT_SCORING:
                vo.setRuleType(RuleConst.RULE_TYPE_SCORING);
                break;
            default:
                vo.setRuleType(RuleConst.RULE_TYPE_TERMINATION);
        }

        return vo;
    }

    @Override
    public List<JSONObject> setComplexRuleOutput(Long versionId, Map<String,Object> temp, Map<String, Object> input, String outType) {
        List<JSONObject> jsonObjectList = outputService.setOutput(new StrategyOutput(versionId, StrategyType.COMPLEX_RULE,outType), temp);
        for (JSONObject jsonObject : jsonObjectList) {
            input.putAll(jsonObject);
        }
        return jsonObjectList;
    }

    @Override
    public List<JSONObject> setBaseRuleOutput(Long ruleId, Map<String, Object> input) {
        List<JSONObject> jsonObjectList = outputService.setOutput(new StrategyOutput(ruleId, StrategyType.BASE_RULE), input);
        return jsonObjectList;
    }

    @Override
    public List<RuleInfo> getRuleList(List<Long> ruleIds) {
        if(!CollectionUtil.isNotNullOrEmpty(ruleIds)){
            return null;
        }
        List<RuleInfo> ruleInfoList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            List<String> keys = RedisUtils.getPrimaryKey(TableEnum.T_RULE, ruleIds);
            ruleInfoList = redisManager.hgetAllBatchByPrimaryKeys(keys, RuleInfo.class);
        } else {
            ruleInfoList = ruleInfoMapper.getRuleList(ruleIds);
        }
        return ruleInfoList;
    }
}
