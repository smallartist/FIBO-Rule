package com.fibo.ddp.common.model.common.requestParam;

import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.utils.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryListParam<T> {
    private Integer pageNum = 1;  // 第几页
    private Integer pageSize = 10;  // 每页的数量
    private T entity;//查询实体对象

    public static boolean checkIsPage(QueryListParam<?> param) {
        if (param == null) {
            throw new ApiException(ErrorCodeEnum.PARAMS_EXCEPTION.getCode(), "分页查询参数异常");
        }
        if (param.getPageNum() != null && param.getPageSize() != null && param.getPageSize() > 0 && param.getPageNum() > 0){
            return true;
        }
        return false;
    }
}
