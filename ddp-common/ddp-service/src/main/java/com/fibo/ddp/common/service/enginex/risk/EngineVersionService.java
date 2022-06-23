package com.fibo.ddp.common.service.enginex.risk;

import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.model.enginex.risk.EngineVersion;

import java.util.List;
import java.util.Map;

public interface EngineVersionService {

    /**
     * 申请部署引擎版本
     *
     * @param
     * @return
     */
    Map<String,Object> applyDeployEngine(Long versionId);

    boolean applyDeployFail(Long versionId);

    boolean approvalCallBack(Long versionId, int result);

    /**
     * 部署引擎版本
     *
     * @param versionId
     * @return
     */
    int deployEngine(Long versionId);

    /**
     * 取消部署引擎版本
     *
     * @param versionId
     * @return
     */
    int undeployEngine(Long versionId);

    // V2
    List<EngineVersion> getEngineVersionListByEngineIdV2(Long engineId);

    int update(EngineVersion engineVersion);

    /**
     * 获取此版本下最新的子版本
     *
     * @param engineVersion
     * @return
     */
    EngineVersion getLatestEngineSubVersion(EngineVersion engineVersion);

    /**
     * 新增版本
     *
     * @param engineVersion
     * @param nodeList
     */
    Long saveEngineVersion(EngineVersion engineVersion, List<EngineNode> nodeList);

    EngineVersion selectByPrimaryKey(Long versionId);

    boolean clear(Long versionId);

    /**
     * 获取引擎对应的版本及引擎名信息
     * yuanlinfeng
     *
     * @param paramMap
     * @return
     */
    public List<EngineVersion> getEngineVersionByEngineId(Map<String, Object> paramMap);

    // runner
    /**
     * 获取引擎正在运行中的版本
     * @param engineId
     * @return
     */
    EngineVersion getRunningVersion(Long engineId);
}
