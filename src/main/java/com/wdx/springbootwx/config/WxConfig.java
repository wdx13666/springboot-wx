package com.wdx.springbootwx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    /**
     * 开发者id
     */
    private String appid ;
    /**
     * 开发者秘钥
     */
    private  String appsecret;
    /**
     * 回调
     */
    public  String callback;
}
