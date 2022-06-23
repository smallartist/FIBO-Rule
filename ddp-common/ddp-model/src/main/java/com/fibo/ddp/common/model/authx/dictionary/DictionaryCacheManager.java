package com.fibo.ddp.common.model.authx.dictionary;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DictionaryCacheManager {

    private final static Map<String, Dictionary> CACHE_KEY_MAP = new TreeMap<>();
    private final static Map<Long, Dictionary> CACHE_ID_MAP = new TreeMap<>();

    public synchronized static void clearCache() {
        CACHE_KEY_MAP.clear();
        CACHE_ID_MAP.clear();
    }

    public synchronized static void addCache(Collection<Dictionary> collection) {
        Map<String, Dictionary> temp = new HashMap<>();
        Map<Long, Dictionary> idTemp = new HashMap<>();
        if (CollectionUtils.isNotEmpty(collection)) {
            for (Dictionary dictionary : collection) {
                if (dictionary.getId() != null && StringUtils.isNotBlank(dictionary.getDictKey())) {
                    temp.put(dictionary.getDictKey(), dictionary);
                    idTemp.put(dictionary.getId(), dictionary);
                }
            }
            CACHE_KEY_MAP.putAll(temp);
            CACHE_ID_MAP.putAll(idTemp);
        }
    }

    public synchronized static void deleteCacheByIds(Collection<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            for (Long key : ids) {
                Dictionary remove = CACHE_ID_MAP.remove(key);
                CACHE_KEY_MAP.remove(remove.getDictKey());
            }
        }
    }

    public synchronized static void updateOrAddCache(Dictionary dictionary) {
        Dictionary remove = CACHE_ID_MAP.put(dictionary.getId(), dictionary);
        if (remove != null) {
            CACHE_KEY_MAP.remove(remove.getDictKey());
        }
        CACHE_KEY_MAP.put(dictionary.getDictKey(), dictionary);
    }

    public synchronized static Map<String, Dictionary> getAllCache() {
        return CACHE_KEY_MAP;
    }

    public synchronized static PageInfo<Dictionary> getByPage(int pageSize, int pageNum) {
        PageInfo<Dictionary> pageInfo = new PageInfo<>();
        int size = CACHE_ID_MAP.values().size();
        int start = 0;
        int end = size;
        if (pageSize > 0 && pageNum > 0) {
            start = pageSize * (pageNum - 1);
            end = start + pageSize;
            if (end >= size) {
                end = size;
            }
        }
        List<Dictionary> dictionaries = new ArrayList<>(CACHE_ID_MAP.values()).subList(start, end);
        pageInfo.setList(dictionaries);
        pageInfo.setTotal(size);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setStartRow(start + 1);
        pageInfo.setEndRow(end + 1);
        return pageInfo;
    }

    public synchronized static Dictionary getByKey(String key) {
        return CACHE_KEY_MAP.get(key);
    }
}
