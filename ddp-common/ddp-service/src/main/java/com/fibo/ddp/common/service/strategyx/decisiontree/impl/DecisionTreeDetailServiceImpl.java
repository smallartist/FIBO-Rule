package com.fibo.ddp.common.service.strategyx.decisiontree.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontree.DecisionTreeDetailMapper;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTreeDetail;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeDetailConditionService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeDetailService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("decisionTreeDetailService")
public class DecisionTreeDetailServiceImpl extends ServiceImpl<DecisionTreeDetailMapper, DecisionTreeDetail> implements DecisionTreeDetailService {

    @Autowired
    private DecisionTreeDetailMapper detailMapper;
    @Autowired
    private DecisionTreeDetailConditionService conditionService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public List<DecisionTreeDetail> queryByVersionId(Long versionId) {
//        LambdaQueryWrapper<DecisionTreeDetail> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(DecisionTreeDetail::getDecisionTreeVersionId, versionId);
//        List<DecisionTreeDetail> list = detailMapper.selectList(wrapper);
//        if (list == null || list.isEmpty()) {
//            return null;
//        }
//        return this.assemble(list);
//    }

    @Override
    public List<DecisionTreeDetail> queryByVersionId(Long versionId) {
        List<DecisionTreeDetail> list;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TREE_DETAIL, versionId);
            list = redisManager.getByForeignKey(key, DecisionTreeDetail.class);
        } else {
            LambdaQueryWrapper<DecisionTreeDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DecisionTreeDetail::getDecisionTreeVersionId, versionId);
            list = detailMapper.selectList(wrapper);
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return this.assemble(list);
    }

    @Override
    @Transactional
    public boolean addDecisionTreeDetailList(Long versionId, List<DecisionTreeDetail> list) {
        if (versionId == null || list == null || list.isEmpty()) {
            return false;
        }
        Long parentId = 0L;

        List<DecisionTreeDetail> detailList = this.disassemble(list, versionId, true);

        List<DecisionTreeDetail> rootList = detailList.stream().filter(detail -> {
            return detail.getParentId() == parentId;
        }).collect(Collectors.toList());
        for (DecisionTreeDetail root : rootList) {
            boolean saveResult = this.save(detailList, root);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updateByVersionId(Long versionId, List<DecisionTreeDetail> list) {
        if (versionId == null || list == null || list.isEmpty()) {
            return false;
        }
        boolean delete = this.deleteByVersionId(versionId);
        if (delete && list != null && list.size() > 0) {
            this.addDecisionTreeDetailList(versionId, list);
        }
        return false;
    }

    @Transactional
    public boolean deleteByVersionId(Long decisionTreeVersionId) {
        if (decisionTreeVersionId == null) {
            return false;
        }
        DecisionTreeDetail detail = new DecisionTreeDetail();
        detail.setDecisionTreeVersionId(decisionTreeVersionId);
        QueryWrapper<DecisionTreeDetail> queryWrapper = new QueryWrapper<>(detail);
        List<DecisionTreeDetail> details = this.list(queryWrapper);
        List<Long> ids = new ArrayList<>();
        for (DecisionTreeDetail d : details) {
            ids.add(d.getId());
        }
        conditionService.removeByDecisionTreeDetailId(ids);
        boolean remove = this.remove(queryWrapper);
        if (!remove) {
            List<DecisionTreeDetail> list = this.list(queryWrapper);
            if (list != null && !list.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    //装配方法
    public List<DecisionTreeDetail> assemble(List<DecisionTreeDetail> list) {
        //转换为Vo
        for (DecisionTreeDetail detail : list) {
            detail.setConditionList(conditionService.queryByDecisionTreeDetailId(detail.getId()));
        }
        //获取根节点列表并且返回拼装好的规则树
        List<DecisionTreeDetail> rootList = list.stream().filter(detail -> {
            return detail.getParentId() != null && detail.getParentId() == 0L;
        }).collect(Collectors.toList());
        if (rootList != null && !rootList.isEmpty()) {
            List<DecisionTreeDetail> result = new ArrayList<>();
            for (DecisionTreeDetail root : rootList) {
                DecisionTreeDetail detailTree = coupling(list, root);
                result.add(detailTree);
            }
            return result;
        }
        return null;
    }


    // 拆解方法
    public List<DecisionTreeDetail> disassemble(List<DecisionTreeDetail> list, Long decisionTreeVersionId, boolean needTempId) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<DecisionTreeDetail> result = new ArrayList<>();
        if (needTempId) {
            for (DecisionTreeDetail detail : list) {
                detail.setParentId(0L);
                detail.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
                List<DecisionTreeDetail> voList = decoupling(detail, decisionTreeVersionId, needTempId);
                result.addAll(voList);
            }
        }
        return result;
    }

    //存储
    @Transactional
    public boolean save(List<DecisionTreeDetail> list, DecisionTreeDetail root) {
        String tempId = root.getInsertTempId();
        boolean insert = this.save(root);
        Long id = root.getId();
        if (!insert || id == null) {
            return false;
        }
        conditionService.addDecisionTreeDetailCondition(id, root.getConditionList());
        for (int i = 0; i < list.size(); i++) {
            DecisionTreeDetail info = list.get(i);
            if (tempId.equals(info.getTempParentId())) {
                info.setParentId(id);
                save(list, info);
            }
        }
        return true;
    }

    //耦合方法：将规则节点列表耦合规则树()
    private DecisionTreeDetail coupling(List<DecisionTreeDetail> list, DecisionTreeDetail root) {
        List<DecisionTreeDetail> children = new ArrayList<>();
        for (DecisionTreeDetail detail : list) {
            if (root.getId().equals(detail.getParentId())) {
                DecisionTreeDetail coup = coupling(list, detail);
                children.add(coup);
            }
        }
        root.setChildren(children);
        return root;
    }

    //解耦方法：将规则树解耦为节点列表
    private List<DecisionTreeDetail> decoupling(DecisionTreeDetail detail, Long decisionTreeVersionId, boolean needTempId) {
        List<DecisionTreeDetail> list = new ArrayList<>();
        List<DecisionTreeDetail> children = detail.getChildren();
        if (children != null && children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                DecisionTreeDetail child = children.get(i);
                if (needTempId) {
                    child.setInsertTempId(UUID.randomUUID().toString().replace("-", ""));
                    child.setTempParentId(detail.getInsertTempId());
                }
                List<DecisionTreeDetail> childList = decoupling(child, decisionTreeVersionId, needTempId);
                list.addAll(childList);
            }
        }
        detail.setDecisionTreeVersionId(decisionTreeVersionId);
        list.add(detail);
        return list;
    }

}
