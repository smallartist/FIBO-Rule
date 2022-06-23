package com.fibo.ddp.common.model.strategyx.scorecard.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListParam implements Serializable {

    private static final long serialVersionUID = 8131487634836541557L;

    protected Integer pageNum = 1;  // 第几页
    protected Integer pageSize = 10;  // 每页的数量

    protected List<Integer> status;  // 状态：1、-1

    protected Boolean search = false;  // 是否搜索
    protected String key;  //
    protected String value;  // 用户输入的搜索词

    protected Integer parentId = 0;  // 文件夹的id

}
