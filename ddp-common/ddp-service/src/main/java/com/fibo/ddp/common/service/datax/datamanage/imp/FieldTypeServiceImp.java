package com.fibo.ddp.common.service.datax.datamanage.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.datax.datamanage.FieldTypeMapper;
import com.fibo.ddp.common.dao.datax.datamanage.FieldTypeUserMapper;
import com.fibo.ddp.common.model.datax.datamanage.FieldType;
import com.fibo.ddp.common.model.datax.datamanage.request.FieldTreeParam;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datamanage.FieldTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FieldTypeServiceImp extends ServiceImpl<FieldTypeMapper, FieldType> implements FieldTypeService {
    @Autowired
    private FieldTypeMapper fieldTypeMapper;
    @Autowired
    private FieldTypeUserMapper fieldTypeUserMapper;

    @Override
    public List<FieldType> getFieldTypeList(Map<String, Object> paramMap) {
        return fieldTypeMapper.getFieldTypeList(paramMap);
    }

    @Override
    public boolean createFieldType(FieldType fieldTypeVo,
                                   Map<String, Object> paramMap) {
        // 检查字段类型是否存在
        if (fieldTypeMapper.createFieldType(fieldTypeVo)) {
            paramMap.put("fieldTypeId", fieldTypeVo.getId());
            if (fieldTypeUserMapper.createFieldTypeUserRel(paramMap)) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    @Override
    public boolean updateFieldType(FieldTreeParam param) {
        param.setOrganId(SessionManager.getLoginAccount().getOrganId());
        param.setUserId(SessionManager.getLoginAccount().getUserId());
        fieldTypeMapper.updateFieldType(param);
        fieldTypeUserMapper.updateFieldTypeUserRel(param);
        return true;
    }

    @Override
    public List<FieldType> getTreeList(FieldTreeParam param) {
        param.setOrganId(SessionManager.getLoginAccount().getOrganId());
        param.setUserId(SessionManager.getLoginAccount().getUserId());
        List<FieldType> fieldTypes = fieldTypeMapper.selectFieldTypeList(param);
        List<FieldType> collect = fieldTypes.stream().filter(fieldType -> fieldType.getParentId() == 0).collect(Collectors.toList());
        for (FieldType fieldType : collect) {
            fieldType.setChildren(this.assembleTreeList(fieldTypes, fieldType));
        }
        return collect;
    }

    private FieldType[] assembleTreeList(List<FieldType> fieldTypes, FieldType root) {
        List<FieldType> children = new ArrayList();
        for (FieldType fieldType : fieldTypes) {
            if (fieldType.getParentId().equals(root.getId())) {
                fieldType.setChildren(this.assembleTreeList(fieldTypes, fieldType));
                children.add(fieldType);
            }
        }
        if (children.size() == 0) {
            return new FieldType[0];
        }
        return children.toArray(new FieldType[children.size()]);
    }
}
