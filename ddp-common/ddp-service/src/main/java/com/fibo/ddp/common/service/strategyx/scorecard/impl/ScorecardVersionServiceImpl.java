package com.fibo.ddp.common.service.strategyx.scorecard.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.scorecard.ScorecardVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetail;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDetailCondition;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardDimension;
import com.fibo.ddp.common.model.strategyx.scorecard.ScorecardVersion;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardDetailVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardDimensionVo;
import com.fibo.ddp.common.model.strategyx.scorecard.vo.ScorecardVersionVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDetailConditionService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDetailService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardDimensionService;
import com.fibo.ddp.common.service.strategyx.scorecard.ScorecardVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.enginex.EngineOperator;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.strategyx.CustomValueUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScorecardVersionServiceImpl extends ServiceImpl<ScorecardVersionMapper, ScorecardVersion> implements ScorecardVersionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ScorecardVersionMapper versionMapper;

    @Autowired
    private ScorecardDimensionService scorecardDimensionService;  // 维度

    @Autowired
    private ScorecardDetailService scorecardDetailService;  // 明细

    @Autowired
    private ScorecardDetailConditionService scorecardDetailConditionService;  // Condition

    @Autowired
    private FieldService fieldService;  // 指标(字段)

    @Autowired
    private StrategyOutputService outputService;//自定义输出
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public ScorecardVersionVo queryById(Long id) {
//        ScorecardVersion scorecardVersion = versionMapper.selectById(id);
//        ScorecardVersionVo result = new ScorecardVersionVo();
//        if (scorecardVersion==null){
//            return result;
//        }
//        BeanUtils.copyProperties(scorecardVersion,result);
//
//        List<ScorecardDimensionVo> scorecardDimensionVos = new ArrayList<>();
//        LambdaQueryWrapper<ScorecardDimension> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ScorecardDimension::getVersionId, scorecardVersion.getId());
//        List<ScorecardDimension> dimensionList = scorecardDimensionService.list(queryWrapper);
//        if (dimensionList != null && !dimensionList.isEmpty()) {
//            for (ScorecardDimension scorecardDimension : dimensionList) {
//                scorecardDimensionVos.add(assemblyScorecardDimensionVo(scorecardDimension));
//            }
//        }
//        result.setScorecardDimension(scorecardDimensionVos);
//        //输出字段
//        List<StrategyOutput> strategyOutputList = outputService.queryByTactics(new StrategyOutput(Long.valueOf(id.toString()), StrategyType.SCORECARD));
//        result.setStrategyOutputList(strategyOutputList);
//        return result;
//    }

    @Override
    public ScorecardVersionVo queryById(Long id) {
        ScorecardVersion scorecardVersion = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_SCORECARD_VERSION, id);
            scorecardVersion = redisManager.getByPrimaryKey(key, ScorecardVersion.class);
        } else {
            scorecardVersion = versionMapper.selectById(id);
        }

        ScorecardVersionVo result = new ScorecardVersionVo();
        BeanUtils.copyProperties(scorecardVersion, result);

        List<ScorecardDimensionVo> scorecardDimensionVos = new ArrayList<>();
        List<ScorecardDimension> dimensionList = scorecardDimensionService.getDimensionListByVersionId(scorecardVersion.getId());
        if (dimensionList != null && !dimensionList.isEmpty()) {
            for (ScorecardDimension scorecardDimension : dimensionList) {
                scorecardDimensionVos.add(assemblyScorecardDimensionVo(scorecardDimension));
            }
        }
        result.setScorecardDimension(scorecardDimensionVos);
        //输出字段
        List<StrategyOutput> strategyOutputList = outputService.queryByTactics(new StrategyOutput(Long.valueOf(id.toString()), StrategyType.SCORECARD));
        result.setStrategyOutputList(strategyOutputList);
        return result;
    }

    @Override
    public List<ScorecardVersionVo> queryVersionListByScorecardId(Long scorecardId) {
        LambdaQueryWrapper<ScorecardVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScorecardVersion::getScorecardId,scorecardId);
        queryWrapper.eq(ScorecardVersion::getStatus,1);
        queryWrapper.orderByDesc(ScorecardVersion::getUpdateTime);
        List<ScorecardVersion> scorecardVersions = versionMapper.selectList(queryWrapper);
        List<ScorecardVersionVo> scorecardVersionVoList = new ArrayList<>();
        for (ScorecardVersion scorecardVersion : scorecardVersions) {
            ScorecardVersionVo versionVo = new ScorecardVersionVo();
            BeanUtils.copyProperties(scorecardVersion,versionVo);
            scorecardVersionVoList.add(versionVo);
        }
        return scorecardVersionVoList;
    }


    @Override
    @Transactional
    public int addVersionList(List<ScorecardVersionVo> versionList) {
        int result = 0;
        for (ScorecardVersionVo versionVo : versionList) {
            boolean b = addVersion(versionVo);
            if (b){
                result++;
            }
        }
        return result;
    }

    @Override
    @Transactional
    public boolean addVersion(ScorecardVersionVo versionVo) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        versionVo.setOrganId(loginSysUser.getOrganId());
        versionVo.setCreateUserId(loginSysUser.getUserId());
        versionVo.setUpdateUserId(loginSysUser.getUserId());
        versionVo.setCreateTime(null);
        versionVo.setUpdateTime(null);
        if (versionVo.getVersionCode()==null){
            versionVo.setVersionCode("V:0");
        }
        if (versionVo.getDescription()==null){
            versionVo.setDescription("初始版本");
        }
        int insert = versionMapper.insert(versionVo);
        if (insert>0){
            this.addVersionDetail(versionVo);
            this.saveSnapshot(versionVo.getId());
            return true;
        }else {
            logger.error("新增评分卡版本失败{}",versionVo);
        }
        return false;
    }
    @Transactional
    public boolean addVersionDetail(ScorecardVersionVo version){
        List<ScorecardDimensionVo> DimensionVos  = version.getScorecardDimension();
        for (ScorecardDimensionVo dimensionVo : DimensionVos) {
            ScorecardDimension dimension = new ScorecardDimension();
            BeanUtils.copyProperties(dimensionVo, dimension);
            dimension.setVersionId(version.getId());
            // 数据库 insertOne(dimension)  之后dimension就有id了 (把id传给子表)
            boolean saveDimension = scorecardDimensionService.save(dimension);
            if (!saveDimension) {
                throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
            }
            List<ScorecardDetailVo> detailVos = dimensionVo.getChildren();  // 明细Array

            // 递归 detailVos
            recursionOfAdd(detailVos, dimension.getId(), 0);
        }
        //添加输出字段
        List<StrategyOutput> strategyOutputList = version.getStrategyOutputList();
        if (strategyOutputList !=null&& strategyOutputList.size()>0){
            outputService.insertTacticsOutput(version.getId(), strategyOutputList);
        }
        return true;
    }
    @Override
    @Transactional
    public boolean copyVersion(ScorecardVersionVo version) {
        ScorecardVersionVo versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(ScorecardVersionVo version) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        versionMapper.updateById(version);
        this.cleanScorecardVersion(version);
        boolean b = this.addVersionDetail(version);
        this.saveSnapshot(version.getId());
        return b;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam statusParam) {
//        int result = versionMapper.updateStatus(ids, status);
        LambdaQueryWrapper<ScorecardVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(ScorecardVersion::getId,statusParam.getIds());
        updateWrapper.eq(ScorecardVersion::getScorecardId,statusParam.getStrategyId());
        ScorecardVersion scorecardVersion = new ScorecardVersion();
        scorecardVersion.setStatus(statusParam.getStatus());
        boolean update = this.update(scorecardVersion, updateWrapper);
        return update;
    }

//    /**
//     * 组装评分卡维度信息
//     * @param scorecardDimension
//     * @return
//     */
//    private ScorecardDimensionVo assemblyScorecardDimensionVo(ScorecardDimension scorecardDimension) {
//        ScorecardDimensionVo scorecardDimensionVo = new ScorecardDimensionVo();
//        BeanUtils.copyProperties(scorecardDimension, scorecardDimensionVo);
//        List<ScorecardDetailVo> children = new ArrayList<>();
//        LambdaQueryWrapper<ScorecardDetail> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ScorecardDetail::getDimensionId, scorecardDimension.getId());
//        List<ScorecardDetail> scorecardDetails = scorecardDetailService.list(queryWrapper);
//
//        List<ScorecardDetail> scorecardDetailsGroup = scorecardDetails.stream().filter(item -> item.getParentId().intValue() == 0).collect(Collectors.toList());
//        for (ScorecardDetail scorecardDetail : scorecardDetailsGroup) {
//            ScorecardDetailVo scorecardDetailVo = assemblyScorecardDetailVo(scorecardDetail, scorecardDetails);
//            children.add(scorecardDetailVo);
//        }
//        scorecardDimensionVo.setChildren(children);
//        return scorecardDimensionVo;
//    }

    /**
     * 组装评分卡维度信息
     *
     * @param scorecardDimension
     * @return
     */
    private ScorecardDimensionVo assemblyScorecardDimensionVo(ScorecardDimension scorecardDimension) {
        ScorecardDimensionVo scorecardDimensionVo = new ScorecardDimensionVo();
        BeanUtils.copyProperties(scorecardDimension, scorecardDimensionVo);
        List<ScorecardDetailVo> children = new ArrayList<>();

        List<ScorecardDetail> scorecardDetails = scorecardDetailService.getDetailListByDimensionId(scorecardDimension.getId());
        List<ScorecardDetail> scorecardDetailsGroup = scorecardDetails.stream()
                .filter(item -> item.getParentId().intValue() == 0).collect(Collectors.toList());
        for (ScorecardDetail scorecardDetail : scorecardDetailsGroup) {
            ScorecardDetailVo scorecardDetailVo = assemblyScorecardDetailVo(scorecardDetail, scorecardDetails);
            children.add(scorecardDetailVo);
        }
        scorecardDimensionVo.setChildren(children);
        return scorecardDimensionVo;
    }

    /**
     * 递归组装评分卡明细信息
     *
     * @param scorecardDetail
     * @param scorecardDetails
     * @return
     */
    private ScorecardDetailVo assemblyScorecardDetailVo(ScorecardDetail scorecardDetail, List<ScorecardDetail> scorecardDetails) {
        ScorecardDetailVo scorecardDetailVo = new ScorecardDetailVo();
        BeanUtils.copyProperties(scorecardDetail, scorecardDetailVo);
        scorecardDetailVo.setCondition(assemblyCondition(scorecardDetail.getId()));
        List<ScorecardDetailVo> children = new ArrayList<>();
        List<ScorecardDetail> detailList = scorecardDetails.stream().filter(item -> item.getParentId().equals(scorecardDetail.getId())).collect(Collectors.toList());
        for (ScorecardDetail cardDetail : detailList) {
            ScorecardDetailVo cardDetailVo = this.assemblyScorecardDetailVo(cardDetail, scorecardDetails);
            children.add(cardDetailVo);
        }
        scorecardDetailVo.setChildren(children);

        Long userId = SessionManager.getLoginAccount().getUserId();
        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", scorecardDetail.getFieldId());
        param.put("userId", userId);
        param.put("engineId", null);
        Field field = fieldService.findByFieldId(param);
        if (field != null) {
            scorecardDetailVo.setFieldName(field.getFieldCn());
        }

        return scorecardDetailVo;
    }

//    /**
//     * 组装区间信息
//     *
//     * @param detailId
//     * @return
//     */
//    private String assemblyCondition(Integer detailId) {
//        String[] conditionArr = new String[]{"(", ")"};
//
//        LambdaQueryWrapper<ScorecardDetailCondition> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ScorecardDetailCondition::getDetailId, detailId);
//        List<ScorecardDetailCondition> scorecardDetailConditions = scorecardDetailConditionService.list(queryWrapper);
//        if (scorecardDetailConditions != null && !scorecardDetailConditions.isEmpty()) {
//            for (ScorecardDetailCondition detailCondition : scorecardDetailConditions) {
//                String operator = detailCondition.getOperator();
//                if (EngineOperator.OPERATOR_GREATER_RELATION.equals(operator)
//                        || EngineOperator.OPERATOR_GREATER_EQUALS_RELATION.equals(operator)) {
//                    conditionArr[0] = convertOperatorToBrackets(operator) + detailCondition.getFieldValue();
//                } else if (EngineOperator.OPERATOR_LESS_RELATION.equals(operator)
//                        || EngineOperator.OPERATOR_LESS_EQUALS_RELATION.equals(operator)) {
//                    conditionArr[1] = detailCondition.getFieldValue() + convertOperatorToBrackets(operator);
//                }
//            }
//        }
//        String condition = StringUtils.join(conditionArr, CommonConst.SYMBOL_COMMA);
//        return condition;
//    }

    /**
     * 组装区间信息
     *
     * @param detailId
     * @return
     */
    private String assemblyCondition(Integer detailId) {
        String[] conditionArr = new String[]{"(", ")"};
        List<ScorecardDetailCondition> scorecardDetailConditions = scorecardDetailConditionService.getConditionListByDetailId(detailId);
        if (scorecardDetailConditions != null && !scorecardDetailConditions.isEmpty()) {
            for (ScorecardDetailCondition detailCondition : scorecardDetailConditions) {
                String operator = detailCondition.getOperator();
                if (EngineOperator.OPERATOR_GREATER_RELATION.equals(operator)
                        || EngineOperator.OPERATOR_GREATER_EQUALS_RELATION.equals(operator)) {
                    conditionArr[0] = convertOperatorToBrackets(operator) + detailCondition.getFieldValue();
                } else if (EngineOperator.OPERATOR_LESS_RELATION.equals(operator)
                        || EngineOperator.OPERATOR_LESS_EQUALS_RELATION.equals(operator)) {
                    conditionArr[1] = detailCondition.getFieldValue() + convertOperatorToBrackets(operator);
                }
            }
        }
        String condition = StringUtils.join(conditionArr, CommonConst.SYMBOL_COMMA);
        return condition;
    }

    /**
     * 运算符转换为括号
     *
     * @param operator
     * @return
     */
    private String convertOperatorToBrackets(String operator) {
        String brackets = "";
        switch (operator) {
            case EngineOperator.OPERATOR_GREATER_RELATION:
                brackets = EngineOperator.OPERATOR_LEFT_PARENTHESES;
                break;
            case EngineOperator.OPERATOR_GREATER_EQUALS_RELATION:
                brackets = EngineOperator.OPERATOR_LEFT_BRACKET;
                break;
            case EngineOperator.OPERATOR_LESS_RELATION:
                brackets = EngineOperator.OPERATOR_RIGHT_PARENTHESES;
                break;
            case EngineOperator.OPERATOR_LESS_EQUALS_RELATION:
                brackets = EngineOperator.OPERATOR_RIGHT_BRACKET;
                break;
            default:
                break;
        }
        return brackets;
    }

    /**
     * 清除评分卡版本信息
     * @param versionVo
     */
    private void cleanScorecardVersion(ScorecardVersionVo versionVo) {
        // 删除评分卡主表
        // removeById(scorecardVo.getUserId());

        // 删除维度信息
        LambdaQueryWrapper<ScorecardDimension> dimensionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dimensionLambdaQueryWrapper.eq(ScorecardDimension::getVersionId, versionVo.getId());
        List<ScorecardDimension> scorecardDimensions = scorecardDimensionService.list(dimensionLambdaQueryWrapper);
        if (scorecardDimensions == null || scorecardDimensions.isEmpty()) {
            return;
        }
        List<Integer> dimensionIds = scorecardDimensions.stream().map(item -> item.getId()).collect(Collectors.toList());
        scorecardDimensionService.removeByIds(dimensionIds);
        //删除输出字段
        outputService.deleteByTactics(new StrategyOutput(versionVo.getId(), StrategyType.SCORECARD));
        // 删除维度明细信息
        LambdaQueryWrapper<ScorecardDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        detailLambdaQueryWrapper.in(ScorecardDetail::getDimensionId, dimensionIds);
        List<ScorecardDetail> scorecardDetails = scorecardDetailService.list(detailLambdaQueryWrapper);
        if (scorecardDetails == null || scorecardDetails.isEmpty()) {
            return;
        }
        List<Integer> detailIds = scorecardDetails.stream().map(item -> item.getId()).collect(Collectors.toList());
        scorecardDetailService.removeByIds(detailIds);

        // 删除维度明细条件信息
        LambdaQueryWrapper<ScorecardDetailCondition> conditionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        conditionLambdaQueryWrapper.in(ScorecardDetailCondition::getDetailId, detailIds);
        scorecardDetailConditionService.remove(conditionLambdaQueryWrapper);
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        Set<String> fieldEns = new HashSet<>();
        LambdaQueryWrapper<ScorecardDimension> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScorecardDimension::getVersionId, versionId);
        queryWrapper.select(ScorecardDimension::getId);
        List<ScorecardDimension> dimensionList = scorecardDimensionService.list(queryWrapper);
        Set<Integer> dimensionIds = new HashSet<>();
        if (dimensionList!=null&&dimensionList.size()>0){
            for (ScorecardDimension scorecardDimension : dimensionList) {
                dimensionIds.add(scorecardDimension.getId());
            }
        }
        if (dimensionIds.size()>0){
            LambdaQueryWrapper<ScorecardDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.in(ScorecardDetail::getDimensionId,dimensionIds);
            List<ScorecardDetail> list = scorecardDetailService.list(detailWrapper);
            if (list!=null&&list.size()>0){
                for (ScorecardDetail detail : list) {
                    fieldEns.add(fieldService.getFieldEnById(detail.getFieldId().longValue()));
                    String custom = detail.getCustom();
                    fieldEns.addAll(CustomValueUtils.getFieldEnSet(custom));
                }
            }
        }
        return new ArrayList<>(fieldEns);
    }

    private void recursionOfAdd(List<ScorecardDetailVo> detailVos, Integer dimensionId, Integer parentId) {

        if (detailVos == null || detailVos.size() < 1) {
            return;
        }

        for (ScorecardDetailVo detailVo : detailVos) {
            ScorecardDetail detail = new ScorecardDetail();
            BeanUtils.copyProperties(detailVo, detail);
            detail.setDimensionId(dimensionId);  // 设置 明细表 的 维度id
            detail.setParentId(parentId);

            String conditionStr = detailVo.getCondition().replace(" ", "");  // 去掉空格
            if (!conditionStr.matches("^((\\[|\\()(\\d(\\d)*(\\.(\\d)+)?)|\\(),((\\d(\\d)*(\\.(\\d)+)?(\\]|\\)))|\\))$")){
                throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(),"区间不正确:"+conditionStr);
            }
            String[] split = conditionStr.split(",");
            if (split[0].length()>1&&split[1].length()>1){
                detail.setLogical("&&");
            }else if (split[0].length()<=1&&split[1].length()<=1){
                logger.error("区间有误，conditionStr:{}", conditionStr);
            }else {
                detail.setLogical(null);
            }

            // 叶子节点和非叶子节点 情况处理
            if (detailVo.getChildren() == null || detailVo.getChildren().size() < 1) {
                // 叶子节点
                detail.setType(1);
            } else {
                // 非叶子节点
                detail.setType(0);
                detail.setScore(null);
            }

            // 数据库 insertOne(detail)  之后detail就有id了 (把id传给子表)
            boolean saveDetail = scorecardDetailService.save(detail);
            if (!saveDetail) {
                throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
            }

            this.addCondition(conditionStr, detail.getId());  // 处理并添加 Condition

            this.recursionOfAdd(detailVo.getChildren(), dimensionId, detail.getId());  // 递归 树
        }

    }

    /**
     * 处理并添加 Condition
     *
     * @param conditionStr Condition字符串
     * @param detailId     明细id
     */
    private void addCondition(String conditionStr, Integer detailId) {

        List<ScorecardDetailCondition> conditionList = new ArrayList<>();  // Condition 数组
        if (!conditionStr.matches("^((\\[|\\()(\\d(\\d)*(\\.(\\d)+)?)|\\(),((\\d(\\d)*(\\.(\\d)+)?(\\]|\\)))|\\))$")){
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(),"区间不正确:"+conditionStr);
        }
        //修正评分卡逻辑
        String[] strings = conditionStr.split(",");
        String left = strings[0];
        String right = strings[1];
        //处理左半边
        if (StringUtils.isNotBlank(left)){
            ScorecardDetailCondition condition = new ScorecardDetailCondition();
            condition.setDetailId(detailId);
            if (left.startsWith("(")){
                condition.setOperator(">");
            }else if (left.startsWith("[")){
                condition.setOperator(">=");
            }
            if (left.length()>1){
                condition.setFieldValue(left.substring(1));
                conditionList.add(condition);
            }


        }
        //处理右半边
        if (StringUtils.isNotBlank(right)){
            ScorecardDetailCondition condition = new ScorecardDetailCondition();
            condition.setDetailId(detailId);
            if (right.endsWith(")")){
                condition.setOperator("<");
            }else if (right.endsWith("]")){
                condition.setOperator("<=");
            }
            if (right.length()>1){
                condition.setFieldValue(right.substring(0,right.length()-1));
                conditionList.add(condition);
            }
        }

        if (conditionList.size()>0){
            boolean saveCondition = scorecardDetailConditionService.saveBatch(conditionList);
            if (!saveCondition) {
                throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
            }
        }

    }

    private boolean saveSnapshot(Long versionId){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LambdaUpdateWrapper<ScorecardVersion> wrapper = new  LambdaUpdateWrapper<>();
                ScorecardVersion versionVo = queryById(versionId);
                versionVo.setSnapshot(null);
                wrapper.eq(ScorecardVersion::getId,versionId).set(ScorecardVersion::getSnapshot, JSON.toJSONString(versionVo));
                versionMapper.update(null,wrapper);
            }
        });
        return true;
    }
}
