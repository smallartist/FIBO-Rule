package com.fibo.ddp.common.service.strategyx.decisiontable.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.strategyx.decisiontable.DecisionTablesResultMapper;
import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTablesResult;
import com.fibo.ddp.common.model.strategyx.decisiontable.vo.DecisionTablesResultVo;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.service.strategyx.decisiontable.DecisionTablesResultService;
import com.fibo.ddp.common.utils.constant.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (DecisionTablesResult)表服务实现类
 */
@Service("decisionTablesResultService")
public class DecisionTablesResultServiceImpl extends ServiceImpl<DecisionTablesResultMapper, DecisionTablesResult> implements DecisionTablesResultService {

    @Autowired
    private RedisManager redisManager;

    @Value("${switch.use.cache}")
    private String cacheSwitch;

//    @Override
//    public DecisionTablesResultVo queryByDecisionTablesVersionId(Long decisionTablesVersionId) {
//        if (decisionTablesVersionId==null){
//            return null;
//        }
//        DecisionTablesResult query = new DecisionTablesResult();
//        query.setVersionId(decisionTablesVersionId);
//        DecisionTablesResult result = this.getOne(new QueryWrapper<>(query));
//        if (result!=null){
//            return transferToVo(result);
//        }
//        return null;
//    }

    @Override
    public DecisionTablesResultVo queryByDecisionTablesVersionId(Long decisionTablesVersionId) {
        if (decisionTablesVersionId==null){
            return null;
        }

        DecisionTablesResult result = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getForeignKey(TableEnum.T_DECISION_TABLES_RESULT, decisionTablesVersionId);
            List<DecisionTablesResult> resultList = redisManager.getByForeignKey(key, DecisionTablesResult.class);
            if(resultList != null){
                result = resultList.get(0);
            }
        } else {
            DecisionTablesResult query = new DecisionTablesResult();
            query.setVersionId(decisionTablesVersionId);
            result = this.getOne(new QueryWrapper<>(query));
        }

        DecisionTablesResultVo decisionTablesResultVo = transferToVo(result);
        return decisionTablesResultVo;
    }

    @Override
    @Transactional
    public DecisionTablesResultVo updateDecisionTablesResult(Long decisionTablesVersionId, DecisionTablesResultVo resultVo) {

        boolean delete = this.deleteByDecisionTablesVersionId(decisionTablesVersionId);
        if (delete){
            this.insertDecisionTablesResult(decisionTablesVersionId,resultVo);
        }
        return this.queryByDecisionTablesVersionId(decisionTablesVersionId);
    }

    @Override
    @Transactional
    public DecisionTablesResultVo insertDecisionTablesResult(Long decisionTablesVersionId, DecisionTablesResultVo insert) {
        if (decisionTablesVersionId==null||insert==null){
            return null;
        }
        DecisionTablesResult resultInfo = transferToInfo(insert);
        resultInfo.setVersionId(decisionTablesVersionId);
        boolean save = this.save(resultInfo);
        if (save){
            return transferToVo(resultInfo);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteByDecisionTablesVersionId(Long decisionTablesVersionId) {
        if (decisionTablesVersionId!=null){
            DecisionTablesResult result = new DecisionTablesResult();
            result.setVersionId(decisionTablesVersionId);
            boolean remove = this.remove(new QueryWrapper<>(result));
            if (!remove){
                List<DecisionTablesResult> list = this.list(new QueryWrapper<>(result));
                if (list!=null&&list.size()!=0){
                    return false;
                }
            }
        }
        return true;
    }

    private  DecisionTablesResult transferToInfo(DecisionTablesResultVo vo){
        DecisionTablesResult resultInfo = new DecisionTablesResult();
        BeanUtils.copyProperties(vo,resultInfo);
        JSONArray resultList = vo.getResultList();
        resultInfo.setRows(resultList.size());
        JSONArray childList = JSON.parseArray(JSON.toJSONString(resultList.get(0)));
        resultInfo.setColumns(childList.size());
        String resultValue = JSON.toJSONString(resultList);
        resultInfo.setResultValue(resultValue);
        return resultInfo;
    }
    private  DecisionTablesResultVo transferToVo(DecisionTablesResult info){
        DecisionTablesResultVo resultVo = new DecisionTablesResultVo();
        BeanUtils.copyProperties(info,resultVo);
        String resultValue = info.getResultValue();
        JSONArray resultArray = JSON.parseArray(resultValue);
        resultVo.setResultList(resultArray);
        return resultVo;
    }

}
