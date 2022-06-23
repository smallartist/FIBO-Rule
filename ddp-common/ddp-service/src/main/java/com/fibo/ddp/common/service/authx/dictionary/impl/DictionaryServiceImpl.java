package com.fibo.ddp.common.service.authx.dictionary.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.dictionary.DictionaryMapper;
import com.fibo.ddp.common.model.authx.dictionary.Dictionary;
import com.fibo.ddp.common.model.authx.dictionary.DictionaryCacheManager;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;

import com.fibo.ddp.common.service.authx.dictionary.DictionaryService;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (Dictionary)表服务实现类
 *
 * @author jgp
 * @since 2021-12-15 15:08:05
 */
@Service("dictionaryService")
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {
    private static boolean useCache = false;
    private static final String useCacheKey = "useCache";
    private static final String useCacheOn = "on";
    @Resource
    private DictionaryMapper dictionaryMapper;


    @Override
    public Dictionary queryByKey(String dictKey) {
        if (StringUtils.isBlank(dictKey)) {
            return null;
        }
        if (useCache) {
            return DictionaryCacheManager.getByKey(dictKey);
        }
        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getDictKey, dictKey);
        return dictionaryMapper.selectOne(wrapper);
    }

    @Override
    public PageInfo<Dictionary> queryList(QueryListParam<Dictionary> param) {
        QueryListParam.checkIsPage(param);
        if (useCache) {
            return DictionaryCacheManager.getByPage(param.getPageSize(), param.getPageNum());
        }
        if (QueryListParam.checkIsPage(param)) {

            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        PageInfo<Dictionary> pageInfo = new PageInfo<>(this.list());
        return pageInfo;
    }

    @Override
    public boolean saveBatchDictionary(Collection<Dictionary> dictionaries) {
        checkDictionaries(dictionaries);
        boolean result = dictionaryMapper.insertOrUpdateBatch(dictionaries) > 0;
        if (useCache) {
            DictionaryCacheManager.addCache(dictionaries.stream().filter(item -> item.getId() != null).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public boolean update(Dictionary param) {
        checkDictionary(param);
        boolean result = this.updateById(param);
        if (useCache) {
            DictionaryCacheManager.updateOrAddCache(param);
        }
        if (useCacheKey.equals(param.getDictKey())){
            refreshCache();
        }
        return result;
    }

    @Override
    public synchronized Map<String, Dictionary> refreshCache() {
        useCache = false;
        Dictionary dictionary = this.queryByKey(useCacheKey);
        if (dictionary != null && useCacheOn.equals(dictionary.getDictValue())) {
            useCache = true;
        }
        DictionaryCacheManager.clearCache();
        if (useCache) {
            DictionaryCacheManager.addCache(this.list());
        }
        return DictionaryCacheManager.getAllCache();
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        boolean result = this.removeByIds(ids);
        if (useCache) {
            DictionaryCacheManager.deleteCacheByIds(ids);
        }
        return result;
    }

    private boolean checkDictionaries(Collection<Dictionary> dictionaries) {
        if (!CollectionUtils.isEmpty(dictionaries)) {
            dictionaries.forEach(item -> {
                checkDictionary(item);
            });
            return true;
        }
        return false;
    }

    private void checkDictionary(Dictionary dictionary) {
        if (dictionary == null || StringUtils.isBlank(dictionary.getDictKey())) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "新增字典变量的key不能为空");
        }
    }
}
