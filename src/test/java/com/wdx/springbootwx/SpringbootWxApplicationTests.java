package com.wdx.springbootwx;

import com.wdx.springbootwx.config.WxConfig;
import com.wdx.springbootwx.service.impl.WxServiceImpl;
import com.wdx.springbootwx.utils.AccessToken;
import com.wdx.springbootwx.utils.WxUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootWxApplicationTests {

    @Autowired
    private WxServiceImpl wxService;
    @Autowired
    private WxConfig config;
    @Test
    void contextLoads() {
        AccessToken accessToken = WxUtil.getAccessToken(config.getAccess_token_url());
        WxUtil.send_template_message(config.getAppid(),config.getAppsecret(),"oi2aR0qkoNyy2ov_AMzxMgs2wHiA",accessToken.getAccess_token());
//        boolean b = WxUtil.menuAdd(config.getAdd_menu_url(), accessToken.getAccess_token());
//        wxService.menuAdd();qwewqeqw
    }

}
