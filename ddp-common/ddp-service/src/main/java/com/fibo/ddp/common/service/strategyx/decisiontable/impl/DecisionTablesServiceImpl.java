package com.fibo.ddp.common.service.strategyx.decisiontable.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontable.DecisionTablesMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTables;
import com.fibo.ddp.common.model.strategyx.decisiontable.request.DecisionTablesListParam;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVersionVo;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesVo;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesService;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.constant.StatusConst;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (DecisionTables)表服务实现类
 */
@Service("decisionTablesService")
public class DecisionTablesServiceImpl extends ServiceImpl<DecisionTablesMapper, DecisionTables> implements DecisionTablesService {
    @Resource
    private DecisionTablesMapper decisionTablesMapper;
    @Resource
    private DecisionTablesVersionService versionService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisManager redisManager;
    @Resource
    private StrategyOutputService outputService;
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public DecisionTablesVo queryById(Long id) {
        //查询决策表主表信息
        DecisionTables decisionTables = this.getById(id);
        if (decisionTables == null) {
            return null;
        }
        DecisionTablesVo decisionTablesVo = new DecisionTablesVo();
        BeanUtils.copyProperties(decisionTables, decisionTablesVo);
        decisionTablesVo.setDecisionTablesVersionList(versionService.queryVersionListByDecisionTablesId(id));
        return decisionTablesVo;
    }

    @Override
    public PageInfo queryByEntity(DecisionTablesListParam listParam) {
        DecisionTables query = listParam.getDecisionTables();
        Integer pageNum = listParam.getPageNum();
        Integer pageSize = listParam.getPageSize();
        if (query != null && query.getName() != null && !"".equals(query.getName())) {
            query.setName("%" + query.getName() + "%");
        }
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        LambdaUpdateWrapper<DecisionTables> wrapper = createWrapper(query);
        List<DecisionTables> decisionTables = decisionTablesMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(decisionTables);

        //TODO 循环查用户表，待优化
        for (DecisionTables decisionTable : decisionTables) {
            if (decisionTable != null && decisionTable.getCreator() != null) {
                decisionTable.setCreatorName(sysUserMapper.findNickNameById(decisionTable.getCreator()));
                decisionTable.setDecisionTablesVersionList(versionService.queryVersionListByDecisionTablesId(decisionTable.getId()));
            }
        }
        return pageInfo;
    }

    @Override
    @Transactional
    public DecisionTablesVo insertDecisionTables(DecisionTablesVo decisionTablesVo) {
        //初始化基本参数
        DecisionTablesVo vo = initParam(decisionTablesVo);
        //拷贝VO到Info对象
        DecisionTables decisionTables = new DecisionTables();
        BeanUtils.copyProperties(vo, decisionTables);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(decisionTables);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_SAVE_ERROR.getCode(), ErrorCodeEnum.DECISION_TABLES_SAVE_ERROR.getMessage());
        }
        Long decisionTablesId = decisionTables.getId();
        List<DecisionTablesVersionVo> versionList = decisionTablesVo.getDecisionTablesVersionList();
        if (versionList != null && versionList.size() > 0) {
            for (DecisionTablesVersionVo versionVo : versionList) {
                versionVo.setDecisionTablesId(decisionTablesId);
            }
            versionService.addVersionList(versionList);
        }
        return this.queryById(decisionTablesId);
    }


    @Override
    @Transactional
    public DecisionTablesVo updateDecisionTables(DecisionTablesVo vo) {
        if (vo.getId() == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), ErrorCodeEnum.PARAMS_EXCEPTION.getMessage());
        }
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setModifier(sysUser.getUserId());
        DecisionTables decisionTables = new DecisionTables();
        BeanUtils.copyProperties(vo, decisionTables);
        //修改主表
        boolean updateResult = this.updateById(decisionTables);
        if (!updateResult) {
            throw new ApiException(ErrorCodeEnum.SERVER_ERROR.getCode(), ErrorCodeEnum.SERVER_ERROR.getMessage());
        }
        Long decisionTablesId = decisionTables.getId();
        List<DecisionTablesVersionVo> versionList = vo.getDecisionTablesVersionList();
        if (versionList != null && versionList.size() > 0) {
            versionService.updateVersion(versionList.get(0));
        }
        return this.queryById(decisionTablesId);
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
        LambdaUpdateWrapper<DecisionTables> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DecisionTables::getId, ids);
        DecisionTables decisionTables = new DecisionTables();
        decisionTables.setStatus(status);
        return this.update(decisionTables, wrapper);
    }

    @Override
    public boolean updateParent(List<Long> ids, Long folderId) {
        LambdaUpdateWrapper<DecisionTables> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DecisionTables::getId, ids).set(DecisionTables::getParentId, folderId);
        DecisionTables decisionTables = new DecisionTables();
        decisionTables.setParentId(folderId);
        return this.update(decisionTables, wrapper);
    }

    @Override
    public List<String> queryFieldEnByDecisionTablesVersionId(Long decisionTablesVersionId) {
        return versionService.queryFieldEnByVersionId(decisionTablesVersionId);
    }

    private LambdaUpdateWrapper<DecisionTables> createWrapper(DecisionTables entity) {
        SysUser loginAccount = SessionManager.getLoginAccount();
        LambdaUpdateWrapper<DecisionTables> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DecisionTables::getOrganId,loginAccount.getOrganId());
        wrapper.orderByDesc(DecisionTables::getId);
        if (entity == null) {
            wrapper.ne(DecisionTables::getStatus, StatusConst.STATUS_DELETE);
            return wrapper;
        }
        if (StringUtils.isNotBlank(entity.getCode())){
            wrapper.eq(DecisionTables::getCode,entity.getCode());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            wrapper.like(DecisionTables::getName, entity.getName());
        }
        if (CollectionUtils.isNotEmpty(entity.getParentIds())) {
            wrapper.in(DecisionTables::getParentId, entity.getParentIds());
        } else if (entity.getParentId() != null) {
            wrapper.eq(DecisionTables::getParentId, entity.getParentId());
        }
        if (entity.getStatus() != null){
            wrapper.eq(DecisionTables::getStatus,entity.getStatus());
        }else {
            wrapper.ne(DecisionTables::getStatus, StatusConst.STATUS_DELETE);
        }
        return wrapper;
    }

    //唯一性检查
    private boolean checkUniqueness(DecisionTablesVo vo) {
        DecisionTables decisionTables = new DecisionTables();
        decisionTables.setName(vo.getName());
        DecisionTables info = this.getOne(new QueryWrapper<>(decisionTables));
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_NAME_REPEAT.getMessage());
        }
        decisionTables.setName(null);
        decisionTables.setCode(vo.getCode());
        info = this.getOne(new QueryWrapper<>(decisionTables));
        if (info != null) {
            throw new ApiException(ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getCode(), ErrorCodeEnum.DECISION_TABLES_CODE_REPEAT.getMessage());
        }
        return true;
    }

    //新插入数据的准备工作
    private DecisionTablesVo initParam(DecisionTablesVo vo) {
        this.checkUniqueness(vo);
        //加入用户信息
        SysUser sysUser = SessionManager.getLoginAccount();
        vo.setCreator(sysUser.getUserId());
        vo.setOrganId(sysUser.getOrganId());
        vo.setModifier(sysUser.getUserId());
        //加入状态信息
        vo.setStatus(StatusConst.STATUS_ENABLED);
        return vo;
    }

    @Override
    public DecisionTablesVo queryByVersionId(Long id) {
        //查询版本
        DecisionTablesVersionVo versionVo = versionService.queryById(id);

        //根据版本信息查询决策表主表信息
        DecisionTables decisionTables = null;
        Long decisionTablesId = versionVo.getDecisionTablesId();
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_DECISION_TABLES, decisionTablesId);
            decisionTables = redisManager.getByPrimaryKey(key, DecisionTables.class);
        } else {
            decisionTables = this.getById(decisionTablesId);
        }
        if (decisionTables == null) {
            return null;
        }

        DecisionTablesVo decisionTablesVo = new DecisionTablesVo();
        BeanUtils.copyProperties(decisionTables, decisionTablesVo);
        decisionTablesVo.setExecuteVersion(versionVo);
        return decisionTablesVo;
    }

    @Override
    public List<JSONObject> setOutput(Long decisionTablesVersionId, Map<String, Object> map) {
        return outputService.setOutput(new StrategyOutput(decisionTablesVersionId, StrategyType.DECISION_TABLES),map);
    }
}
