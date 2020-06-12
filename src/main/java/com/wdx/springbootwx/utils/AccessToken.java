package com.wdx.springbootwx.utils;

import lombok.Data;

@Data
public class AccessToken {

    private String access_token;//获取到的凭证

    private int expires_in;//凭证有效时间

}
