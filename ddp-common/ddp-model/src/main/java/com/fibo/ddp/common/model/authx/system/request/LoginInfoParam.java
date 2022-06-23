package com.fibo.ddp.common.model.authx.system.request;

import lombok.Data;

@Data
public class LoginInfoParam {
    private String account;// 账号
    private String password;// 密码
}
