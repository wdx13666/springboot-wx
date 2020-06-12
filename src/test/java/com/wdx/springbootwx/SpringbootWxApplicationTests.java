package com.wdx.springbootwx;

import com.wdx.springbootwx.service.impl.WxServiceImpl;
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
    @Test
    void contextLoads() {
        wxService.menuAdd();
    }

}
