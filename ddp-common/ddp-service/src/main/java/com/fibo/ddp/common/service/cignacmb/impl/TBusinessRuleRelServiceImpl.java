package com.fibo.ddp.common.service.cignacmb.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.cignacmb.TBusinessRuleRelMapper;
import com.fibo.ddp.common.model.cignacmb.TBusinessRuleRel;
import com.fibo.ddp.common.model.cignacmb.req.BusinessRuleRelReq;
import com.fibo.ddp.common.model.cignacmb.vo.BusinessRuleRelVO;
import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.service.cignacmb.ITBusinessRuleRelService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.utils.util.StringUtil;
import com.fibo.ddp.common.utils.constant.RuleInfoConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 业务类型与规则关联表 服务实现类
 * </p>
 *
 * @author oldRose
 * @since 2021-11-10
 */
@Service("iTBusinessRuleRelService")
public class TBusinessRuleRelServiceImpl extends ServiceImpl<TBusinessRuleRelMapper, TBusinessRuleRel> implements ITBusinessRuleRelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseEntityDto<?> addBusinessRuleRel(BusinessRuleRelReq param) {
        try {
            //参数校验
            checkParam(param);
            TBusinessRuleRel businessRuleRel =  new TBusinessRuleRel();
            //组件规则信息
            buildModel(param,businessRuleRel);
            //插入
            this.save(businessRuleRel);
        } catch (Exception e) {
            logger.info("新增业务类型和规则关联 记录出错");
            return ResponseEntityBuilder.buildErrorResponse("999999999",e.getMessage());
        }
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 组件实体
     * @param param
     */
    private void buildModel(BusinessRuleRelReq param,TBusinessRuleRel businessRuleRel) {
        BeanUtils.copyProperties(param,businessRuleRel);
        //组件规则信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RuleInfoConstants.BLACK_LIST,param.getBlacklist());
        jsonObject.put(RuleInfoConstants.THRESHOLD,param.getThreshold());
        jsonObject.put(RuleInfoConstants.MUTEX,param.getMutex());
        jsonObject.put(RuleInfoConstants.MERGE,param.getMerge());
        jsonObject.put(RuleInfoConstants.INTERCEPT,param.getIntercept());
        businessRuleRel.setRuleInfo(JSON.toJSONString(jsonObject));
        businessRuleRel.setStatus(1);
        businessRuleRel.setCreator(Long.valueOf(1));
        businessRuleRel.setCreateTime(new Date());
        businessRuleRel.setUpdateTime(new Date());
        businessRuleRel.setOrganId(SessionManager.getLoginAccount().getOrganId());
    }

    /**
     * 检查参数
     * @param param
     */
    private void checkParam(BusinessRuleRelReq param) throws Exception {
        if(param == null){
            throw new Exception("参数不能为空", new Throwable());
        }
        if(StringUtil.isBlank(param.getBusinessName())){
            throw new Exception("业务类型名称不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getBusinessChildCode())){
            throw new Exception("业务类型编码不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getBusinessChildName())){
            throw new Exception("业务子类型名称不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getBusinessChildCode())){
            throw new Exception("业务子类型编码不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getSendType())){
            throw new Exception("发送方式不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getIsUnsubscribe())){
            throw new Exception("是否取消订阅 不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getEventType())){
            throw new Exception("事件类型 不能为空",new Throwable());
        }
        if(StringUtil.isBlank(param.getIsManualIntervention())){
            throw new Exception("是否人工干预 不能为空",new Throwable());
        }
    }

    @Override
    public ResponseEntityDto<?> editBusinessRuleRel(BusinessRuleRelReq param) {
        //参数校验
        if(param.getId()==null){
            return ResponseEntityBuilder.buildErrorResponse("999999","id必须传，不能为空");
        }
        try {
            checkParam(param);
            //组件规则信息
            TBusinessRuleRel businessRuleRel =  new TBusinessRuleRel();
            buildModel(param,businessRuleRel);
            this.saveOrUpdate(businessRuleRel);
        } catch (Exception e) {
            logger.info("编辑业务类型与规则关联表 出错");
            return ResponseEntityBuilder.buildErrorResponse("999999999",e.getMessage());
        }
        return ResponseEntityBuilder.buildNormalResponse();
    }

    @Override
    public ResponseEntityDto<?> detailBusinessRuleRel(BusinessRuleRelReq param) {
        //参数校验
        if(param.getId()==null){
            return ResponseEntityBuilder.buildErrorResponse("999999","id必须传，不能为空");
        }
        TBusinessRuleRel businessRuleRel  = this.getById(param.getId());
        //获取出ruleInfo,循环拿出规则，方便展示
        String ruleInfo = businessRuleRel.getRuleInfo();
        JSONObject jsonObject = JSON.parseObject(ruleInfo);
        JSONArray blacklist = JSONArray.parseArray(jsonObject.get("blacklist")+"");
        JSONArray threshold = JSONArray.parseArray(jsonObject.get("threshold")+"");
        JSONArray mutex = JSONArray.parseArray(jsonObject.get("mutex")+"");
        JSONArray merge = JSONArray.parseArray(jsonObject.get("merge")+"");
        JSONArray intercept = JSONArray.parseArray(jsonObject.get("intercept")+"");
        //提取到对应的id 查出规则
        BusinessRuleRelVO businessRuleRelVO =  new BusinessRuleRelVO();
        BeanUtils.copyProperties(businessRuleRel,businessRuleRelVO);
        businessRuleRelVO.setBlackList(blacklist);
        businessRuleRelVO.setThresholdList(threshold);
        businessRuleRelVO.setMutexList(mutex);
        businessRuleRelVO.setMergeList(merge);
        businessRuleRelVO.setInterceptList(intercept);
        return ResponseEntityBuilder.buildNormalResponse(businessRuleRel);
    }

    @Override
    public ResponseEntityDto<?> deleteBusinessRuleRel(BusinessRuleRelReq param) {
        //参数校验
        if(param.getId()==null){
            return ResponseEntityBuilder.buildErrorResponse("999999","id必须传，不能为空");
        }
        //删除
        QueryWrapper<TBusinessRuleRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",param.getId());
        TBusinessRuleRel businessRuleRel = new TBusinessRuleRel();
        businessRuleRel.setStatus(0);
        this.update(businessRuleRel,queryWrapper);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    @Override
    public ResponseEntityDto<?> pageList(BusinessRuleRelReq param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        QueryWrapper<TBusinessRuleRel> queryWrapper = new QueryWrapper<>();
        buildQueryWrapper(queryWrapper, param);
        List<TBusinessRuleRel> businessRuleRelList = this.list(queryWrapper);
        PageInfo<TBusinessRuleRel> pageInfo = new PageInfo<>(businessRuleRelList);
        pageInfo.setList(businessRuleRelList);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    @Override
    public ResponseEntityDto<?> queryByBusinessType(BusinessRuleRelReq param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        //根据业务类型查询对应规则(对外接口)
        QueryWrapper<TBusinessRuleRel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_code",param.getBusinessCode());
        queryWrapper.eq("business_child_code",param.getBusinessChildCode());
        List<TBusinessRuleRel> businessRuleRelList = this.list(queryWrapper);
        PageInfo<TBusinessRuleRel> pageInfo = new PageInfo<>(businessRuleRelList);
        pageInfo.setList(businessRuleRelList);
        return ResponseEntityBuilder.buildNormalResponse(pageInfo);
    }

    /**
     * 组建查询条件
     *
     * @param queryWrapper
     * @param param
     */
    private void buildQueryWrapper(QueryWrapper<TBusinessRuleRel> queryWrapper, BusinessRuleRelReq param) {
        queryWrapper.eq("status",1);
        //更新时间倒序查询
        queryWrapper.orderByDesc("update_time");
    }
}
