package com.fibo.ddp.common.service.strategyx.decisiontree.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontree.DecisionTreeMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datamanage.Field;
import com.fibo.ddp.common.model.strategyx.decisiontree.DecisionTree;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontree.vo.DecisionTreeVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeService;
import com.fibo.ddp.common.service.strategyx.decisiontree.DecisionTreeVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("decisionTreeService")
public class DecisionTreeServiceImpl extends ServiceImpl<DecisionTreeMapper, DecisionTree> implements DecisionTreeService {

    @Autowired
    private DecisionTreeMapper decisionTreeMapper;
    @Autowired
    private DecisionTreeVersionService versionService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private StrategyOutputService outputService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private RedisManager redisManager;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public DecisionTreeVo queryById(Long id) {
        DecisionTree decisionTree = decisionTreeMapper.selectById(id);
        if (decisionTree == null) {
            return null;
        }
        DecisionTreeVo decisionTreeVo = new DecisionTreeVo();
        BeanUtils.copyProperties(decisionTree, decisionTreeVo);
        decisionTreeVo.setVersionList(versionService.queryVersionListByDecisionTreeId(id));
        return decisionTreeVo;
    }

    @Override
    public PageInfo queryByEntity(QueryListParam<DecisionTreeVo> listParam) {
        DecisionTreeVo query = listParam.getEntity();
        Integer pageNum = listParam.getPageNum();
        Integer pageSize = listParam.getPageSize();
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaQueryWrapper<DecisionTree> wrapper = createWrapper(query);
        List<DecisionTree> decisionTreeList = decisionTreeMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(decisionTreeList);

        //TODO 循环查用户表，待优化
        for (DecisionTree tree : decisionTreeList) {
            if (tree != null && tree.getCreateUserId() != null) {
                tree.setCreatorName(sysUserMapper.findNickNameById(tree.getCreateUserId()));
                tree.setVersionList(versionService.queryVersionListByDecisionTreeId(tree.getId()));
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public DecisionTreeVo insertDecisionTree(DecisionTreeVo decisionTreeVo) {
        //初始化基本参数
        DecisionTreeVo vo = initParam(decisionTreeVo);
        //拷贝VO到Info对象
        DecisionTree decisionTree = new DecisionTree();
        BeanUtils.copyProperties(vo, decisionTree);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(decisionTree);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getCode(), ErrorCodeEnum.DECISION_TREE_SAVE_ERROR.getMessage());
        }
        Long decisionTreeId = decisionTree.getId();
        List<DecisionTreeVersionVo> versionList = decisionTreeVo.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            for (DecisionTreeVersionVo versionVo : versionList) {
                versionVo.setDecisionTreeId(decisionTreeId);
            }
            versionService.addVersionList(versionList);
        }
        return this.queryById(decisionTreeId);
    }

    @Override
    @Transactional
    public DecisionTreeVo updateDecisionTree(DecisionTreeVo vo) {
        if (vo.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setUpdateUserId(sysUser.getUserId());
        DecisionTree decisionTree = new DecisionTree();
        BeanUtils.copyProperties(vo, decisionTree);
        //修改主表
        boolean updateResult = this.updateById(decisionTree);
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        Long decisionTreeId = decisionTree.getId();
        List<DecisionTreeVersionVo> versionList = vo.getVersionList();
        if (versionList != null && versionList.size() > 0) {
            versionService.updateVersion(versionList.get(0));
        }
        return this.queryById(decisionTreeId);
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        LambdaUpdateWrapper<DecisionTree> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DecisionTree::getId, ids);
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setStatus(status);
        int updateNum = decisionTreeMapper.update(decisionTree, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateFolder(List<Long> ids, Long folderId) {
        LambdaUpdateWrapper<DecisionTree> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DecisionTree::getId, ids);
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setFolderId(folderId);
        int updateNum = decisionTreeMapper.update(decisionTree, wrapper);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> queryFieldEnByVersionId(Long versionId) {
        return versionService.queryFieldEnByVersionId(versionId);
    }

    private LambdaQueryWrapper<DecisionTree> createWrapper(DecisionTree query) {
        LambdaQueryWrapper<DecisionTree> wrapper = new LambdaQueryWrapper<>();
        if (query!=null){
            if (query.getName() != null) {
                wrapper.like(DecisionTree::getName, query.getName());
            }
            if (query.getCode() != null) {
                wrapper.like(DecisionTree::getCode, query.getCode());
            }
            if (query.getFolderId()!=null){
                wrapper.eq(DecisionTree::getFolderId,query.getFolderId());
            }
        }
        wrapper.ne(DecisionTree::getStatus,-1);
        wrapper.eq(DecisionTree::getOrganId,SessionManager.getLoginAccount().getOrganId());
        wrapper.orderByDesc(DecisionTree::getUpdateTime, DecisionTree::getCreateTime, DecisionTree::getId);
        return wrapper;
    }

    //新插入数据的准备工作
    private DecisionTreeVo initParam(DecisionTreeVo vo) {
        this.checkUniqueness(vo);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setCreateUserId(sysUser.getUserId());
        vo.setOrganId(sysUser.getOrganId());
        vo.setUpdateUserId(sysUser.getUserId());
        //加入状态信息
        vo.setStatus(StatusConst.STATUS_ENABLED);
        return vo;
    }

    //唯一性检查
    private boolean checkUniqueness(DecisionTreeVo vo) {
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setName(vo.getName());
        DecisionTree info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getMessage());
        }
        decisionTree.setName(null);
        decisionTree.setCode(vo.getCode());
        info = this.getOne(new QueryWrapper<>(decisionTree), false);
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getMessage());
        }
        return true;
    }

    @Override
    public DecisionTreeVo queryExecuteDecisionTree(Long id, Long versionId) {
        if (versionId==null){
            return null;
        }
        DecisionTreeVersionVo versionVo = versionService.queryById(versionId);
        if (versionVo==null){
            return null;
        }
        if (id==null){
            id= versionVo.getDecisionTreeId();
        }else if (id!=versionVo.getDecisionTreeId()){
            return null;
        }
        DecisionTree decisionTree;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_DECISION_TREE, id);
            decisionTree = redisManager.getByPrimaryKey(key, DecisionTree.class);
        } else {
            decisionTree = decisionTreeMapper.selectById(id);
        }
        if (decisionTree == null) {
            return null;
        }
        DecisionTreeVo decisionTreeVo = new DecisionTreeVo();
        BeanUtils.copyProperties(decisionTree, decisionTreeVo);
        decisionTreeVo.setExecuteVersion(versionVo);
        return decisionTreeVo;
    }

    @Override
    public List<JSONObject> setOutput(Long versionId, Map<String, Object> map) {
        return outputService.setOutput(new StrategyOutput(versionId, StrategyType.DECISION_TREE),map);
    }

    @Override
    public List<Long> getNodeFieldIds(Long versionId) {
        List<String> list = versionService.queryFieldEnByVersionIdRunner(versionId);
        List<Field> fieldList = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            fieldList = fieldService.selectFieldListByEns(list);
        }else {
            fieldList = fieldService.selectFieldListByEns(list);
        }
        Iterator<Field> iterator = fieldList.iterator();
        List<Long> ids = new ArrayList<>();
        while (iterator.hasNext()){
            ids.add(iterator.next().getId());
        }
        return ids;
    }
}
