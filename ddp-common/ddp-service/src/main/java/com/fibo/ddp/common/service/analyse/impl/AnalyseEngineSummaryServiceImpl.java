package com.fibo.ddp.common.service.analyse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.analyse.AnalyseEngineSummaryMapper;
import com.fibo.ddp.common.model.analyse.AnalyseEngineSummary;
import com.fibo.ddp.common.model.analyse.vo.AnalyseEngineSummaryVo;
import com.fibo.ddp.common.model.analyse.vo.AnalyseRequestParam;
import com.fibo.ddp.common.service.analyse.AnalyseEngineSummaryService;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.utils.constant.AnalyseConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * (TAnalyseEngineSummary)表服务实现类
 */
@Service("tAnalyseEngineSummaryService")
public class AnalyseEngineSummaryServiceImpl extends ServiceImpl<AnalyseEngineSummaryMapper, AnalyseEngineSummary> implements AnalyseEngineSummaryService {

    @Autowired
    private AnalyseEngineSummaryMapper summaryMapper;

    @Override
    public Map<String, AnalyseEngineSummaryVo> getAnalyseData(AnalyseRequestParam param) {
        Map<String, AnalyseEngineSummaryVo> result = new HashMap<>();
        Date now = new Date();
        Date yesterday = this.dateAdd(now,-1);
        for (String key : AnalyseConst.KEYS) {
            AnalyseEngineSummary curData = this.getDataByEndDate(param.getVersionId(), key, now, false);
            long curCount = 0L;
            if (curData!=null){
                curCount = curData.getStatisticsCount();
            }
            AnalyseEngineSummary lastData = this.getDataByEndDate(param.getVersionId(), key, yesterday, false);
            long lastCount = 0L;
            if (lastData!=null&&lastData.getStatisticsCount()!=null){
                lastCount = lastData.getStatisticsCount();
            }
            AnalyseEngineSummary lastTotalData = this.getDataByEndDate(param.getVersionId(), key, yesterday, true);
            long lastTatol = 0L;
            if (lastTotalData!=null){
                lastTatol = lastTotalData.getStatisticsCount();
            }
            result.put(key,new AnalyseEngineSummaryVo(curCount, lastCount,lastTatol));
        }
        return result;
    }
    //获取指定时间所在日期的指定类型数据
    private AnalyseEngineSummary getDataByEndDate(Long versionId,String dimension, Date end,boolean istotal){
        LambdaQueryWrapper<AnalyseEngineSummary> wrapper = this.createWrapper(versionId, dimension, end);
        if (istotal){
            wrapper.lt(AnalyseEngineSummary::getCreateTime,this.clearTime(this.dateAdd(end,1)));
        }else {
            wrapper.le(AnalyseEngineSummary::getCreateTime,end);
        }
        wrapper.orderByDesc(AnalyseEngineSummary::getCreateTime);
        return this.getOne(wrapper,false);
    }
    //构造查询wrapper
    private LambdaQueryWrapper<AnalyseEngineSummary> createWrapper(Long versionId,String dimension, Date end){
        LambdaQueryWrapper<AnalyseEngineSummary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnalyseEngineSummary::getOrganId, SessionManager.getLoginAccount().getOrganId());
        wrapper.eq(AnalyseEngineSummary::getStatisticsDimension,dimension);
        wrapper.eq(AnalyseEngineSummary::getEngineVersionId, versionId);
        Date start = this.clearTime(end);
        wrapper.ge(AnalyseEngineSummary::getCreateTime,start);
        return wrapper;
    }
    //清除时间信息
    private Date clearTime(Date date){
        LocalDate localDate=date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date result=java.sql.Date.valueOf(localDate);
        return result;
    }
    //增加日期
    private Date dateAdd(Date date,Integer integer){
        Calendar c =new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.DATE,integer);
        return c.getTime();
    }
}
