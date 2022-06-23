package com.fibo.ddp.common.service.strategyx.decisiontable.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontable.DecisionTablesDetailMapper;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesDetail;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesDetailVo;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailConditionService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesDetailService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.strategyx.DecisionTablesDetailConst;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (DecisionTablesDetail)表服务实现类
 *
 * @author jgp
 * @since 2021-04-12 17:58:46
 */
@Service("decisionTablesDetailService")
public class DecisionTablesDetailServiceImpl extends ServiceImpl<DecisionTablesDetailMapper, DecisionTablesDetail> implements DecisionTablesDetailService {
    @Resource
    private DecisionTablesDetailMapper decisionTablesDetailMapper;
    @Resource
    private DecisionTablesDetailConditionService conditionService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;
    @Autowired
    private FieldService fieldService;

//    @Override
//    public List<DecisionTablesDetailVo> queryByDecisionTablesVersionId(Long decisionTablesVersionId, Integer dimensionality) {
//
//        DecisionTablesDetail query = new DecisionTablesDetail();
//        query.setVersionId(decisionTablesVersionId);
//        query.setDimensionality(dimensionality);
//        QueryWrapper<DecisionTablesDetail> queryWrapper = new QueryWrapper<>(query);
//        List<DecisionTablesDetail> list = decisionTablesDetailMapper.selectList(queryWrapper);
//
//        if (list == null || list.isEmpty()) {
//            return null;
//        }
//        List<DecisionTablesDetailVo> result = this.assemble(list);
//        return result;
//    }

    @Override
    public List<DecisionTablesDetailVo> queryByDecisionTablesVersionId(Long decisionTablesVersionId, Integer dimensionality) {
        List<DecisionTablesDetail> list = null;
        if (Constants.switchFlag.ON.equals(cacheSwitch)) {
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TABLES_DETAIL, decisionTablesVersionId);
            list = redisManager.getByForeignKey(key, DecisionTablesDetail.class);
            list = list.stream().filter(item -> item.getDimensionality().intValue() == dimensionality).collect(Collectors.toList());
        } else {
            DecisionTablesDetail query = new DecisionTablesDetail();
            query.setVersionId(decisionTablesVersionId);
            query.setDimensionality(dimensionality);
            QueryWrapper<DecisionTablesDetail> queryWrapper = new QueryWrapper<>(query);
            list = decisionTablesDetailMapper.selectList(queryWrapper);
        }

        if (list == null || list.isEmpty()) {
            return null;
        }
        List<DecisionTablesDetailVo> result = this.assemble(list);
        return result;
    }


    @Override
    @Transactional
    public boolean insertDecisionTablesDetail(Long decisionTablesVersionId, List<DecisionTablesDetailVo> list, Integer dimensionality) {
        if (decisionTablesVersionId == null || list == null || list.isEmpty()) {
            return false;
        }
        Long parentId = DecisionTablesDetailConst.ROOT_PARENT_ID;

        List<DecisionTablesDetail> detailList = this.disassemble(list, decisionTablesVersionId, true, dimensionality);

        List<DecisionTablesDetail> rootList = detailList.stream().filter(detail -> {
            return detail.getParentId() == parentId;
        }).collect(Collectors.toList());
        for (DecisionTablesDetail root : rootList) {
            boolean saveResult = this.save(detailList, root);
        }
        return true;
    }

    @Override
    @Transactional
    public List<DecisionTablesDetailVo> updateDecisionTablesDetail(Long decisionTablesId, List<DecisionTablesDetailVo> list, Integer dimensionality) {
        if (decisionTablesId == null || dimensionality == null) {
            return null;
        }
        boolean delete = this.deleteByDecisionTablesVersionId(decisionTablesId, dimensionality);
        if (delete && list != null && list.size() > 0) {
            this.insertDecisionTablesDetail(decisionTablesId, list, dimensionality);
        }
        return this.queryByDecisionTablesVersionId(decisionTablesId, dimensionality);
    }

    @Override
    @Transactional
    public boolean deleteByDecisionTablesVersionId(Long decisionTablesVersionId, Integer dimensionality) {
        if (decisionTablesVersionId == null || dimensionality == null) {
            return false;
        }
        DecisionTablesDetail detail = new DecisionTablesDetail();
        detail.setVersionId(decisionTablesVersionId);
        detail.setDimensionality(dimensionality);
        QueryWrapper<DecisionTablesDetail> queryWrapper = new QueryWrapper<>(detail);
        List<DecisionTablesDetail> details = this.list(queryWrapper);
        List<Long> ids = new ArrayList<>();
        for (DecisionTablesDetail d : details) {
            ids.add(d.getId());
        }
        conditionService.deleteByDecisionTablesDetailId(ids);
        boolean remove = this.remove(queryWrapper);
        if (!remove) {
            List<DecisionTablesDetail> list = this.list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    //装配方法，将规则条件List装配成一个规则树并返回
    public List<DecisionTablesDetailVo> assemble(List<DecisionTablesDetail> list) {
        //转换为Vo
        List<DecisionTablesDetailVo> detailVoList = transferToVoList(list);
        for (DecisionTablesDetailVo detailVo : detailVoList) {
            detailVo.setConditionList(conditionService.queryByDecisionTablesDetailId(detailVo.getId()));
        }
        //获取根节点列表并且返回拼装好的规则树
        List<DecisionTablesDetailVo> rootList = detailVoList.stream().filter(detail -> {
            return detail.getParentId() != null && detail.getParentId() == DecisionTablesDetailConst.ROOT_PARENT_ID;
        }).collect(Collectors.toList());
        if (rootList != null && !rootList.isEmpty()) {
            List<DecisionTablesDetailVo> result = new ArrayList<>();
            for (DecisionTablesDetailVo root : rootList) {
                DecisionTablesDetailVo detailTree = coupling(detailVoList, root);
                result.add(detailTree);
            }
            return result;
        }
        return null;
    }


    // 拆解方法，将规则条件Vo转换未规则条件list
    public List<DecisionTablesDetail> disassemble(List<DecisionTablesDetailVo> list, Long decisionTablesVersionId, boolean needTempId, Integer dimensionality) {
        if (list == null) {
            return null;
        }
        List<DecisionTablesDetail> result = new ArrayList<>();
        if (needTempId) {
            for (DecisionTablesDetailVo vo : list) {
                vo.setParentId(DecisionTablesDetailConst.ROOT_PARENT_ID);
                vo.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
                List<DecisionTablesDetailVo> voList = decoupling(vo, decisionTablesVersionId, needTempId);
                result.addAll(transferToInfoList(voList));
            }
        }
        for (DecisionTablesDetail detail : result) {
            detail.setDimensionality(dimensionality);
        }
        return result;
    }

    //存储
    @Transactional
    public boolean save(List<DecisionTablesDetail> list, DecisionTablesDetail root) {
        String tempId = root.getInsertTempId();
//        String content = fittingContent(root);
//        root.setContent(content);
        boolean insert = this.save(root);

        if (!insert) {
            return false;
        }
        Long id = root.getId();
        conditionService.insertDecisionTablesDetailCondition(id, root.getConditionList());
        for (int i = 0; i < list.size(); i++) {
            DecisionTablesDetail info = list.get(i);
            if (tempId.equals(info.getTempParentId())) {
                info.setParentId(id);
                save(list, info);
            }
        }
        return true;
    }

    //耦合方法：将规则节点列表耦合规则树()
    private DecisionTablesDetailVo coupling(List<DecisionTablesDetailVo> list, DecisionTablesDetailVo root) {
        List<DecisionTablesDetailVo> children = new ArrayList<>();
        for (DecisionTablesDetailVo rc : list) {
            if (root.getId().equals(rc.getParentId())) {
                DecisionTablesDetailVo rcVo = coupling(list, rc);
                children.add(rcVo);
            }
        }
        root.setChildren(children);
        return root;
    }

    //解耦方法：将规则树解耦为节点列表
    private List<DecisionTablesDetailVo> decoupling(DecisionTablesDetailVo vo, Long decisionTablesVersionId, boolean needTempId) {
        List<DecisionTablesDetailVo> list = new ArrayList<>();
        List<DecisionTablesDetailVo> children = vo.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                DecisionTablesDetailVo child = children.get(i);
                if (needTempId) {
                    child.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
                    child.setTempParentId(vo.getInsertTempId());
                }
                List<DecisionTablesDetailVo> childList = decoupling(child, decisionTablesVersionId, needTempId);
                list.addAll(childList);
            }
        }
        vo.setVersionId(decisionTablesVersionId);
        list.add(vo);
        return list;
    }

    //List<DecisionTablesDetail>转换为List<DecisionTablesDetailVo>
    private List<DecisionTablesDetailVo> transferToVoList(List<DecisionTablesDetail> list) {
        List<DecisionTablesDetailVo> voList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DecisionTablesDetailVo vo = new DecisionTablesDetailVo();
            BeanUtils.copyProperties(list.get(i), vo);
            vo.setContent(null);
            vo.setTempParentId(null);
            vo.setInsertTempId(null);
            voList.add(vo);
        }
        return voList;
    }

    //List<DecisionTablesDetailVo>转换为List<DecisionTablesDetail>
    private List<DecisionTablesDetail> transferToInfoList(List<DecisionTablesDetailVo> list) {
        List<DecisionTablesDetail> detailList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DecisionTablesDetail detail = new DecisionTablesDetail();
            BeanUtils.copyProperties(list.get(i), detail);
            detailList.add(detail);
        }
        return detailList;
    }

//    private String fittingContent(DecisionTablesDetail detail){
//        if (detail==null){
//            return null;
//        }
//        Long fieldId = detail.getFieldId();
//        List<DecisionTablesDetailCondition> conditionList = detail.getConditionList();
//        Integer valueType = detail.getValueType();
//        String logical = detail.getLogical();
//        if (fieldId==null||conditionList==null||conditionList.isEmpty()){
//            return null;
//        }
//        if (valueType==null){
//            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getVersionCode(),ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
//        }
//        String fieldName = fieldMapper.findFieldNameById(fieldId);
//        String condition = "(";
//        if (valueType==2||valueType==5){
//            for (int i = 0; i < conditionList.size(); i++) {
//                DecisionTablesDetailCondition cond = conditionList.get(i);
//                condition += "(this['"+fieldName+"']"+cond.getOperator();
//                if (cond.getVariableType()!=null&&cond.getVariableType()==2){
//                    condition+="this['"+cond.getFieldValue()+"']";
//                }else {
//                    condition+="\""+cond.getFieldValue()+"\")";
//                }
//                if (i<conditionList.size()-1){
//                    condition+=logical;
//                }
//            }
//        }else {
//            for (int i = 0; i < conditionList.size(); i++) {
//                DecisionTablesDetailCondition cond = conditionList.get(i);
//                condition += "(this['"+fieldName+"']"+cond.getOperator();
//                if (cond.getVariableType()!=null&&cond.getVariableType()==2){
//                    condition+="this['"+cond.getFieldValue()+"']";
//                }else {
//                    condition+=cond.getFieldValue()+")";
//                }
//                if (i<conditionList.size()-1){
//                    condition+=logical;
//                }
//            }
//        }
//        condition += ")";
//        String content = DecisionTablesRunnerConst.fitContent(condition);
//        return content;
//    }

    @Override
    public List<Long> queryFieldIdByDecisionTablesVersionId(Long decisionTablesVersionId) {
        List<Long> fieldIdList = null;
        Set<Long> set = new HashSet<Long>();
        List<DecisionTablesDetail> list = new ArrayList<>();
        if (Constants.switchFlag.ON.equals(cacheSwitch)) {
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TABLES_DETAIL, decisionTablesVersionId);
            list.addAll(redisManager.getByForeignKey(key, DecisionTablesDetail.class));
        } else {
            list.addAll(this.list(new QueryWrapper<DecisionTablesDetail>().lambda().eq(DecisionTablesDetail::getVersionId, decisionTablesVersionId)));
        }
        if (list != null) {
            set.addAll(list.stream().map(item -> item.getFieldId()).collect(Collectors.toSet()));
            List<String> fieldEns = conditionService.queryFieldEnByDetailIds(list.stream().map(item -> item.getId()).collect(Collectors.toList()));
            if (fieldEns != null && !fieldEns.isEmpty()) {
                List<Field> fieldList = fieldService.selectFieldListByEns(fieldEns);
                set.addAll(fieldList.stream().map(item -> item.getId()).collect(Collectors.toSet()));
            }
        }
        fieldIdList = new ArrayList<>(set);
        return fieldIdList;
    }
}
