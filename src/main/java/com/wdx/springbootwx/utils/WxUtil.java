package com.wdx.springbootwx.utils;

import com.alibaba.fastjson.JSONObject;
import com.wdx.springbootwx.menu.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * 微信检验签名
 */
public final class WxUtil {


    /**
     * 开发者id
     */

    private static String APPID ;
    /**
     * 开发者秘钥
     */
    private static String APPSECRET;

    /**
     * 回调
     */
    public static final String callback = "http://17mb642884.51mypc.cn/wxcallback";



    public static final String token = "wang...";
    //添加菜单接口
    public static final String ADD_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq" +
            ".com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] str = new String[]{token, timestamp, nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }

    /**
     * 获取凭证
     *
     * @return
     */
    public static AccessToken getAccessToken() {
        System.out.println("从接口中获取");
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject json = doGetstr(url);
        if (json != null) {
            token.setAccess_token(json.getString("access_token"));
            token.setExpires_in(json.getIntValue("expires_in"));
        }
        return token;
    }


    /**
     * 处理doget请求
     *
     * @param url
     * @return
     */
    public static JSONObject doGetstr(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    /**
     * 处理post请求
     *
     * @param url
     * @return
     */
    public static JSONObject doPoststr(String url, String outStr) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            httpPost.setEntity(new StringEntity(outStr, "utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 组装菜单
     *
     * @return
     */
    public static Menu initMenu() {
        Menu menu = new Menu();
        ViewButton button11 = new ViewButton();
        //注意按钮名字不要太长，不然会报40018错误
        button11.setName("技术博客");
        button11.setType("view");
        button11.setUrl("http://wdx13666.github.io");
        //注意链接不要少了https://  否则会报错40055
        ViewButton button22 = new ViewButton();
        //注意按钮名字不要太长，不然会报40018错误
        button22.setName("my");
        button22.setType("view");
        button22.setUrl("http://17mb642884.51mypc.cn");
        SendPicButton button21 = new SendPicButton();
        button21.setName("发图");
        button21.setType("pic_photo_or_album");
        button21.setKey("pic");

        SendLocalButton button32 = new SendLocalButton();
        button32.setName("发位置");
        button32.setType("location_select");
        button32.setKey("local");

        ClickButton button31 = new ClickButton();
        button31.setName("点赞");
        button31.setType("click");
        button31.setKey("strtest");//事件key

        Button buttonView = new Button();
        buttonView.setName("雪傻子");
        buttonView.setSub_button(new Button[]{button11,button22});
        buttonView.setSub_button(new Button[]{button11,button22});

        Button button = new Button();
        button.setName("服务");
        button.setSub_button(new Button[]{button31, button32});
        button.setSub_button(new Button[]{button31, button32});
        menu.setButton(new Button[]{buttonView, button21, button});
        return menu;


    }


}

