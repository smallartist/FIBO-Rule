package com.fibo.ddp.common.model.enginex.risk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {
    private Map<String,Integer> success;//成功数据 key为结果。value为对应结果条数，空结果key为空字符串
    private Integer successNum;//成功条数
    private Integer failNum;//失败条数
    private Integer total;//总数
}
