package com.fibo.ddp.common.model.enginex.risk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fibo.ddp.common.model.enginex.dataflow.EngineVersionContent;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_engine_version")
public class EngineVersion implements Comparable<EngineVersion>, Serializable {

    private static final long serialVersionUID = 2923432053414979455L;

    /**
     * 版本编号
     */
    @TableId(type = IdType.AUTO)
    private Long versionId;

    /**
     * 引擎编号
     */
    private Long engineId;
    /**
     * 引擎类型
     */
    private String engineType;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 子版本
     */
    private Integer subVersion;

    /**
     * 是否部署(0:未部署1:正在运行,2申请部署)
     */
    private Integer bootState;

    /**
     * 是否删除(0:在回收站中,可恢复,1:正常,2彻底删除)
     */
    private Integer status;

    /**
     * 布局方式(1,2,预留通用布局方式,0自定义布局)
     */
    private Integer layout;

    /**
     * 创建者
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最新修改者
     */
    private Long latestUser;

    /**
     * 最后修改时间
     */
    private String latestTime;

    @TableField(exist = false)
    private List<EngineNode> engineNodeList;
    @TableField(exist = false)
    private EngineVersionContent engineVersionContent;

	@Override
	public int compareTo(EngineVersion o) {
		if(version!=o.getVersion()){
            return version-o.getVersion();
        }else if(!(subVersion == o.getSubVersion())){
            return subVersion - o.getSubVersion();
        }else {
            return version-o.getVersion();
        }
	}
}