package com.fibo.ddp.common.model.strategyx.decisiontable.request;

import com.fibo.ddp.common.model.strategyx.decisiontable.DecisionTables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionTablesListParam {

    protected Integer pageNum = 1;  // 第几页
    protected Integer pageSize = 10;  // 每页的数量


//    protected Boolean search = false;  // 是否搜索

    protected DecisionTables decisionTables;//查询实体对象

//    protected Integer parentId = 0;  // 文件夹的id
}

