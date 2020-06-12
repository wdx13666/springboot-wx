package com.wdx.springbootwx.controller;

import com.alibaba.fastjson.JSONObject;
import com.wdx.springbootwx.config.WxConfig;
import com.wdx.springbootwx.service.impl.WxServiceImpl;
import com.wdx.springbootwx.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class WxController {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WxServiceImpl service;

    private String access_Token;

    private String openId;

    @GetMapping
    public String wxlogin() {
        // 第一步：用户同意授权，获取code
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxConfig.g +
                "&redirect_uri=" + WxUtil.callback +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=STATE#wechat_redirect";
        return "redirect:" + url;
    }

    @GetMapping("/wxcallback")
    public String wxcallback(String code, ModelMap map) throws IOException {
        // 第二步：通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WxUtil.APPID +
                "&secret=" + WxUtil.APPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = WxUtil.doGetstr(url);

        openId = jsonObject.getString("openid");
        access_Token = jsonObject.getString("access_token");

        System.out.println("access_Token" + access_Token);

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_Token +
                "&openid=" + openId +
                "&lang=zh_CN";
        JSONObject userInfoJson = WxUtil.doGetstr(url);
        System.out.println("UserInfo:" + userInfoJson);

        // 微信帐号做来一个关联，来关联我们的账号体系
        // 此处实现自己的保存用户信息逻辑
        return "index.html";
    }


    @GetMapping("/register")
    public String register(String openid, ModelMap map) {
        map.put("openid", openid);
        return "/upload";  // 我这里是打开上传页面，可根据自己业务需要实际来跳转
    }

    @GetMapping("/success")
    public String register() {
        return "/success";  // 打开注册成功页面
    }

    /**
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return
     */
    @RequestMapping("/weixin")
    public void checkSignature(String signature, String timestamp, String nonce, String echostr,
                               HttpServletResponse response) {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (WxUtil.checkSignature(signature, timestamp, nonce)) {
                out.write(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }


    }


    /**
     * 添加微信公众号菜单
     *
     * @return
     */
    @PostMapping(value = "/menuAdd")
    public String menuAdd() {
        boolean b = service.menuAdd();
        if (b) {
            return "success";
        }
        return "unsuccess";

    }

}

