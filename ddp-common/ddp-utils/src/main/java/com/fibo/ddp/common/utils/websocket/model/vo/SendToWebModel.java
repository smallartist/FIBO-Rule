package com.fibo.ddp.common.utils.websocket.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendToWebModel {
    private String code;
    private Object data;

    public SendToWebModel(String code, Object data) {
        this.code = code;
        this.data = data;
    }
}
