package com.fibo.ddp.common.dao.enginex.personas;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.ddp.common.model.enginex.personas.PersonasEngineResultDetail;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReport;
import com.fibo.ddp.common.model.enginex.personas.vo.PersonasReportParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (PersonasEngineResultDetail)表数据库访问层
 *
 * @author jgp
 * @since 2022-01-06 14:24:57
 */
@Mapper
public interface PersonasEngineResultDetailMapper extends BaseMapper<PersonasEngineResultDetail> {

    List<PersonasReport> selectReportList(PersonasReportParam param);
}

