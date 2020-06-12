package com.wdx.springbootwx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wdx.springbootwx.service.WxService;
import com.wdx.springbootwx.utils.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxServiceImpl implements WxService {

    @Override
    public boolean menuAdd() {
        String url = WxUtil.ADD_MENU_URL.replace("ACCESS_TOKEN", WxUtil.getAccessToken().getAccess_token());
        String menu = JSONObject.toJSONString(WxUtil.initMenu()).toString();
        JSONObject result = WxUtil.doPoststr(url,menu);
        if("ok".equals(result.getString("errmsg"))){
            log.info("添加菜单结果：{}", result);
            return true;
        }
        log.info("添加菜单结果：{}", result);
        return false;

    }



}
