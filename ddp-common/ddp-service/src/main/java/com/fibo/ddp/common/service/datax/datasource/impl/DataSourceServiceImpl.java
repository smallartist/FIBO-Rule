package com.fibo.ddp.common.service.datax.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.datax.datasource.DataSourceMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.datax.datasource.DataSource;
import com.fibo.ddp.common.model.datax.datasource.request.DataSourceListParam;
import com.fibo.ddp.common.model.datax.datasource.vo.DataSourceVo;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datasource.DataSourceService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.utils.constant.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {

    @Resource
    public DataSourceMapper dataSourceMapper;

    @Resource
    public SysUserMapper sysUserMapper;

    @Autowired
    private RedisManager redisManager;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public Integer saveDataSource(DataSourceVo dataSourceVo) {
        SysUser sysUser = SessionManager.getLoginAccount();
        Long organId = sysUser.getOrganId();
        DataSource dataSource = new DataSource();
        BeanUtils.copyProperties(dataSourceVo, dataSource);
        dataSource.setCreator(sysUser.getUserId());
        dataSource.setModifier(sysUser.getUserId());
        dataSource.setOrganId(organId);
        return dataSourceMapper.insert(dataSource);
    }

    @Override
    public Integer updateDataSource(DataSourceVo dataSourceVo) {
        SysUser sysUser = SessionManager.getLoginAccount();
        DataSource dataSource = new DataSource();
        BeanUtils.copyProperties(dataSourceVo, dataSource);
        dataSource.setModifier(sysUser.getUserId());
        return dataSourceMapper.updateById(dataSource);
    }

    @Override
    public DataSourceVo getDataSourceById(Integer id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        DataSourceVo dataSourceVo = new DataSourceVo();
        BeanUtils.copyProperties(dataSource, dataSourceVo);
        SysUser creator = sysUserMapper.findUserById(dataSource.getCreator());
        SysUser modifier = sysUserMapper.findUserById(dataSource.getModifier());
        dataSourceVo.setCreatorName(creator.getAccount());
        dataSourceVo.setModifierName(modifier.getAccount());
        return dataSourceVo;
    }

    @Override
    public Map<String, Object> getDataSourceList(DataSourceListParam param) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getStatus, 1);
        if(param.getTypeList()!=null&&!param.getTypeList().isEmpty()){
            queryWrapper.in(DataSource::getType, param.getTypeList());
        }
        queryWrapper.orderByDesc(DataSource::getUpdateTime);
        if (param.getPageNo()>0 && param.getPageSize()>0){
            PageHelper.startPage(param.getPageNo(), param.getPageSize());
        }
        List<DataSource> dataSourceList = dataSourceMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo<>(dataSourceList);
        pageInfo.setList(null);
        result.put("pager", pageInfo);

        List<DataSourceVo> dataSourceVoList = new ArrayList<>();
        for (DataSource dataSource : dataSourceList) {
            DataSourceVo dataSourceVo = new DataSourceVo();
            BeanUtils.copyProperties(dataSource, dataSourceVo);
            SysUser creator = sysUserMapper.findUserById(dataSource.getCreator());
            SysUser modifier = sysUserMapper.findUserById(dataSource.getModifier());
            dataSourceVo.setCreatorName(creator.getAccount());
            dataSourceVo.setModifierName(modifier.getAccount());
            dataSourceVoList.add(dataSourceVo);
        }
        result.put("data", dataSourceVoList);

        return result;
    }

    @Override
    public Integer deleteDataSourceById(Integer id) {
        DataSource dataSource = new DataSource();
        dataSource.setId(id);
        dataSource.setStatus(0);
        return dataSourceMapper.updateById(dataSource);
    }

    @Override
    public DataSource getDataSourceByIdRunner(Integer id) {
        DataSource dataSource = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_FIELD_DATA_SOURCE, id);
            dataSource = redisManager.getByPrimaryKey(key, DataSource.class);
        } else {
            dataSource = this.getById(id);
        }
        return dataSource;
    }
}
