package com.fibo.ddp.common.service.strategyx.listlibrary.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbMapper;
import com.fibo.ddp.common.dao.strategyx.listlibrary.ListDbVersionMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.requestParam.StatusParam;
import com.fibo.ddp.common.model.strategyx.listlibrary.ListDbVersion;
import com.fibo.ddp.common.model.strategyx.strategyout.StrategyOutput;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.strategyx.listlibrary.ListDbVersionService;
import com.fibo.ddp.common.service.strategyx.strategyout.StrategyOutputService;
import com.fibo.ddp.common.utils.constant.strategyx.StrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("listDbVersionService")
public class ListDbVersionServiceImpl extends ServiceImpl<ListDbVersionMapper, ListDbVersion> implements ListDbVersionService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ListDbVersionMapper versionMapper;
    @Autowired
    private ListDbMapper listDbMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StrategyOutputService outputService;

    @Override
    public ListDbVersion queryById(Long id) {
        ListDbVersion version = this.getById(id);
        if (version == null) {
            return null;
        }
        //查询策略输出
        List<StrategyOutput> strategyOutputs = outputService.queryByTactics(new StrategyOutput(id, StrategyType.LIST_DB));
        version.setStrategyOutputList(strategyOutputs);
        return version;
    }
    @Override
    public List<ListDbVersion> queryVersionListByListDbId(Long listDbId) {
        LambdaQueryWrapper<ListDbVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ListDbVersion::getListDbId, listDbId);
        queryWrapper.ne(ListDbVersion::getStatus, -1);
        queryWrapper.orderByDesc(ListDbVersion::getId);
        List<ListDbVersion> versionList = versionMapper.selectList(queryWrapper);
        return versionList;
    }
    @Override
    @Transactional
    public int addVersionList(List<ListDbVersion> versionList) {
        int result = 0;
        for (ListDbVersion versionVo : versionList) {
            boolean b = addVersion(versionVo);
            if (b) {
                result++;
            }
        }
        return result;
    }

    @Transactional
    @Override
    public boolean addVersion(ListDbVersion version) {
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setOrganId(loginSysUser.getOrganId());
        version.setCreateUserId(loginSysUser.getUserId());
        version.setUpdateUserId(loginSysUser.getUserId());
        version.setCreateTime(null);
        version.setUpdateTime(null);
        version.setStatus(1);
        if (version.getVersionCode() == null) {
            version.setVersionCode("V:0");
        }
        if (version.getDescription() == null) {
            version.setDescription("初始版本");
        }
        int insert = versionMapper.insert(version);
        if (insert > 0) {
            boolean result = this.addVersionDetail(version);
            if (result) {
                saveSnapshot(version.getId());
            }
            return true;
        } else {
            logger.error("新增名单库版本失败{}", version);
        }
        return false;
    }
    @Transactional
    public boolean addVersionDetail(ListDbVersion version) {
        //添加输出字段
        List<StrategyOutput> strategyOutputList = version.getStrategyOutputList();
        if (strategyOutputList != null && strategyOutputList.size() > 0) {
            outputService.insertTacticsOutput(version.getId(), strategyOutputList);
        }
        //建表
        this.createListDbTable(version);
        return true;
    }
    @Override
    @Transactional
    public boolean copyVersion(ListDbVersion version) {
        ListDbVersion versionVo = this.queryById(version.getId());
        versionVo.setId(null);
        versionVo.setVersionCode(version.getVersionCode());
        versionVo.setDescription(version.getDescription());
        return this.addVersion(versionVo);
    }

    @Override
    @Transactional
    public boolean updateVersion(ListDbVersion version) {
        Long versionId = version.getId();
        if (versionId == null) {
            return false;
        }
        SysUser loginSysUser = SessionManager.getLoginAccount();
        version.setUpdateUserId(loginSysUser.getUserId());
        //修改版本主表
        versionMapper.updateById(version);
        //修改策略输出
        outputService.updateTacticsOutput(versionId, version.getStrategyOutputList(), StrategyType.LIST_DB);
        //存快照
        saveSnapshot(version.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusParam StatusParam) {
        LambdaQueryWrapper<ListDbVersion> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(ListDbVersion::getId, StatusParam.getIds());
        updateWrapper.eq(ListDbVersion::getListDbId, StatusParam.getStrategyId());
        ListDbVersion version = new ListDbVersion();
        version.setStatus(StatusParam.getStatus());
        boolean update = this.update(version, updateWrapper);
        return update;
    }

    private boolean saveSnapshot(Long versionId) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LambdaUpdateWrapper<ListDbVersion> wrapper = new LambdaUpdateWrapper<>();
                ListDbVersion versionVo = queryById(versionId);
                versionVo.setSnapshot(null);
                wrapper.eq(ListDbVersion::getId, versionId).set(ListDbVersion::getSnapshot, JSON.toJSONString(versionVo));
                versionMapper.update(null, wrapper);
            }
        });
        return true;
    }

    @Transactional
    public boolean createListDbTable(ListDbVersion version) {

        Long organId = SessionManager.getLoginAccount().getOrganId();
        Long id = version.getListDbId();
        Long versionId = version.getId();
        String tableName = "organ"+"_"+organId+"_list_db_"+id+"_"+versionId;
        String sqlStr = "CREATE TABLE "+  tableName + "(" +
                "  `userId` int(11) NOT NULL AUTO_INCREMENT comment 'userId'," ;
        //生成表字段
        String tableColumn = version.getTableColumn();
        String arrayTableColumn[] = tableColumn.split(",");
        int arrayTC[] = new int[arrayTableColumn.length];
        for(int i=0;i<arrayTableColumn.length;i++){
            arrayTC[i]=Integer.parseInt(arrayTableColumn[i]);
            sqlStr += "  `t" + i + "`" + " varchar(100) DEFAULT NULL comment '" + arrayTC[i]+ "'," ;
        }
        int start = arrayTableColumn.length;
        for(int j=start;j<20;j++){
            sqlStr += "  `t" + j + "`" + " varchar(100) DEFAULT NULL comment ''," ;
        }
        Map<String,Object> paramMap = new HashMap<>();
        sqlStr += "`user_id` int(11) NOT NULL COMMENT '创建人编号',"+
                "`nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',"+
                "`created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"+
                "PRIMARY KEY (`userId`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        paramMap.put("sqlStr", sqlStr);

        listDbMapper.createTable(paramMap);

        Map<String, Object> indexMap = new HashMap<String, Object>();
        //循环生成索引列
        String queryField = version.getQueryField();
        String arrayQueryField[] = queryField.split(",");
        int arrayQF[] = new int[arrayQueryField.length];

        int arrayTC2[] = new int[arrayTableColumn.length];
        String indexStr = "";
        for(int i=0;i<arrayQueryField.length;i++){
            //查询字段id
            arrayQF[i]=Integer.parseInt(arrayQueryField[i]);
            //循环维护字段id
            for(int j=0;j<arrayTableColumn.length;j++){
                arrayTC2[j]=Integer.parseInt(arrayTableColumn[j]);
                if(arrayQF[i]==arrayTC2[j]){
                    if(indexStr.equals("")){
                        indexStr = "  `t" + j + "`";
                    }else{
                        indexStr += "," + "  `t" + j + "`";
                    }
                }
            }
        }

        String indexSql = "ALTER TABLE "+ tableName +" ADD INDEX `idx_top4` ("+indexStr+") ;";
        indexMap.put("indexSql", indexSql);
        listDbMapper.createIndex(indexMap);
        return true;
    }
}
