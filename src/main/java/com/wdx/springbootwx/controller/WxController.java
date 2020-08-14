package com.wdx.springbootwx.controller;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import com.wdx.springbootwx.config.WxConfig;
import com.wdx.springbootwx.service.impl.WxServiceImpl;
import com.wdx.springbootwx.utils.MessageUtil;
import com.wdx.springbootwx.utils.TaoBaoUtils;
import com.wdx.springbootwx.utils.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@Slf4j
public class WxController {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WxServiceImpl service;

    private final static String serverUrl = "http://gw.api.taobao.com/router/rest";

    private final static String appKey = "31007070";

    private final static String secert = "996b541c4613a0431ad48ced1ac81c65";


    @GetMapping("/wxLogin")
    public String wxLogin() {
        // 第一步：用户同意授权，获取code
        String wxLogin = WxUtil.wxLogin(wxConfig.getCode_url());
        return "redirect:" + wxLogin;
    }

    @GetMapping("/wxCallBack")
    public String wxCallBack(String code, ModelMap map) throws IOException {
        // 第二步：通过code换取网页授权access_token
        JSONObject jsonObject = WxUtil.wxCallBack(wxConfig.getWebpage_access_token_url(), code);
        String openId = jsonObject.getString("openid");
        String access_Token = jsonObject.getString("access_token");
        System.out.println("access_Token" + access_Token);

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        JSONObject userInfo = WxUtil.getUserInfo(wxConfig.getUserinfo_url(), access_Token, openId);
        System.out.println("UserInfo:" + userInfo);
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
    @GetMapping
    @ResponseBody
    public String checkSignature(String signature, String timestamp, String nonce, String echostr,
                                 HttpServletResponse response, HttpServletRequest request) {
        String method = request.getMethod();
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        if (WxUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("---------验证服务结束----------");
            return echostr;
        } else {
            log.info("---------不是微信服务器发来的请求，请小心----------");
            return null;
        }


    }

    @PostMapping
    @ResponseBody
    public String message(HttpServletResponse response, HttpServletRequest request) throws ApiException {
        log.info("---------接受消息和事件推送----------");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        //将微信请求xml转为map格式，获取所需的参数
        Map<String, String> map = MessageUtil.xmlToMap(request);
        String ToUserName = map.get("ToUserName");
        String FromUserName = map.get("FromUserName");
        String MsgType = map.get("MsgType");
        String Content = map.get("Content");
        //沙雕机器人
        JSONObject jsonObject = WxUtil.doGetstr(wxConfig.getQing_url().replace("MSG", Content));
        String contentB = jsonObject.getString("content");

        //物料搜索
        TbkDgMaterialOptionalResponse optional = TaoBaoUtils.optional(serverUrl, appKey, secert, Content);
        TbkDgMaterialOptionalResponse.MapData mapData = optional.getResultList().get(0);
        TbkTpwdCreateResponse tbkTpwdCreateResponse = TaoBaoUtils.create(serverUrl, appKey, secert, mapData.getTitle(),"https:" + mapData.getCouponShareUrl(), mapData.getPictUrl());
        if (optional.getTotalResults() > 0 ){
            contentB = mapData.getTitle() + "\n" +
                    "【在售价】"+mapData.getReservePrice() +" 元\n" +
                    "【券后价】"+mapData.getReservePrice() +"元\n" +
                    "-----------------\n" +
//                    "【立即领券】复制$pwhEc1W7zGL$打开手机淘宝领券并下单\n" +
                    "【立即下单】复制"+ tbkTpwdCreateResponse.getData().getModel() +"打开手机淘宝立即下单";
        }else {
            contentB = "SORRY，未查询到优惠券,请查看其他商品!";
        }
        String message = null;
        //处理文本类型,回复用户输入的内容
        if ("text".equals(MsgType)) {
            message = MessageUtil.initMessage(FromUserName, ToUserName, contentB);
        }
        log.info("消息体：{}", message);
        return message;
    }


    public static void main(String[] args) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31007070", "996b541c4613a0431ad48ced1ac81c65");
        //找到对应 类，比如taobao.trade.fullinfo.get接口对应的请求类为TradeFullinfoGetRequest
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        //设置业务参数
//      req.setUserId("123");
        req.setText("快买我");
//      req.setUrl("https://uland.taobao.com/");
        req.setLogo("https://uland.taobao.com/");
        req.setUrl("https://uland.taobao.com/coupon/edetail?e=SBqFy5IYW5cNfLV8niU3RxrSI%2FOabn6qNg4Gqf8CT4AKuDLwELihnSr%2B5TH%2FBzPHNSBSeq%2Bsk50F1ygf8uqQ6DDocfQoJ48C%2BJOLYARNiOav2kmLojfN6BLR9V68By6Nv5MBvZGSdn5s8quoD2oSyQ8O2wYVR3Y%2B2NUPkg%2BbDjKvLkDYcrKgNU%2BPZLGhy2fX59WqflzEvOXNa4fuEICbKf865raRWOux&&app_pvid=59590_11.15.221.126_702_1597311789932&ptl=floorId:2836;app_pvid:59590_11.15.221.126_702_1597311789932;tpp_pvid:100_11.179.213.231_95182_851597311789937315&xId=4RZ0M2W7vNvPIDrHEpHInEsP5wxUyWZJcDzJUatxiPNmAzZ2uxfswAphntcxZKgUEtSGdgxhMCycPngIoFIkOD6mtpw3h8RXYNmp0lDzsMCe&union_lens=lensId%3AMAPI%401597311790%400b0fdd7e_db2d_173e733abf1_5c0f%4001");
        req.setExt("{}");

        TbkTpwdCreateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }




}

