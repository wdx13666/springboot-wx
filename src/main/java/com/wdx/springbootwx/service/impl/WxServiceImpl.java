package com.wdx.springbootwx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wdx.springbootwx.service.WxService;
import com.wdx.springbootwx.utils.AccessToken;
import com.wdx.springbootwx.utils.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxServiceImpl implements WxService {

   /* @Override
    public AccessToken getAccessToken(String url,String appid,String appsecret) {
        System.out.println("从接口中获取");
        AccessToken token = new AccessToken();
        url = url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject json = WxUtil.doGetstr(url);
        if (json != null) {
            token.setAccess_token(json.getString("access_token"));
            token.setExpires_in(json.getIntValue("expires_in"));
        }
        return token;
    }


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

    }*/



}
