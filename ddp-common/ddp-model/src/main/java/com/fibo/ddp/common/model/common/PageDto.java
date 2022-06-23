package com.fibo.ddp.common.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

}
