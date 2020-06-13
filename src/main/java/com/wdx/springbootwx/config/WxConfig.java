package com.wdx.springbootwx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    /**
     * 开发者id
     */
    private String appid;
    /**
     * 开发者秘钥
     */
    private String appsecret;
    /**
     * 回调
     */
    private String callback;
    /**
     *
     */
    private String token;
    /**
     * 添加菜单url
     */
    private String add_menu_url;
    /**
     * 获取token url
     */
    private String access_token_url;
    /**
     * 获取code url
     */
    private String code_url;
    /**
     * 获取网页授权access_token
     */
    private String webpage_access_token_url;
    /**
     * 拉取用户信息url
     */
    private String userinfo_url;

    /**
     * 机器人url
     */
    private String qing_url;


}
