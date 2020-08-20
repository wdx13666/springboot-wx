package com.wdx.springbootwx.controller;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.wdx.springbootwx.config.WxConfig;
import com.wdx.springbootwx.service.impl.WxServiceImpl;
import com.wdx.springbootwx.utils.DataokeUtils;
import com.wdx.springbootwx.utils.MessageUtil;
import com.wdx.springbootwx.utils.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public static void main(String[] args) {
        System.out.println(
                "欢迎小可爱来到剁手党～\n" +
                        "------------------------------\n" +
                        "👇【使用方法】👇\n" +
                        "复制淘宝商品分享链接发送至公众号，用公众号回复的链接直接复制至淘宝下单即可，免费领取隐藏优惠券哦😘 \n" +
                        "关注我一段时间，如果你没有爱上我，再取关也不迟哦~");
    }

    @PostMapping
    @ResponseBody
    public String message(HttpServletResponse response, HttpServletRequest request) throws ApiException {
        log.info("---------接受消息和事件推送----------");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        String contentB = null;
        String message = null;
        //将微信请求xml转为map格式，获取所需的参数
        Map<String, String> map = MessageUtil.xmlToMap(request);
        String ToUserName = map.get("ToUserName");
        String FromUserName = map.get("FromUserName");
        String MsgType = map.get("MsgType");
        String event = map.get("Event");
        String Content = map.get("Content");

        //用户关注公众号发送的消息
        if ("subscribe".equals(event)) {
            contentB = "欢迎小可爱来到剁手党～🌹\n\n" +
                    "👇【使用方法】👇\n" +
                    "复制淘宝商品的分享链接至公众号，复制公众号回复的链接至淘宝下单即可，免费领取隐藏优惠券哦😘\n" +
                    "关注我一段时间，如果你没有爱上我，再取关也不迟哦~";
            //处理文本类型,回复用户输入的内容
            if ("text".equals(MsgType) || "event".equals(MsgType)) {
                message = MessageUtil.initMessage(FromUserName, ToUserName, contentB, MsgType);
            }
            log.info("消息体：{}", message);
            return message;
        }

        //沙雕机器人
//        JSONObject jsonObject = WxUtil.doGetstr(wxConfig.getQing_url().replace("MSG", Content));
//        String contentB = jsonObject.getString("content");


        //大淘客 淘口令获取商品id
        JSONObject jsonObject = DataokeUtils.getGoodsId(Content);
//        String title = jsonObject.get("goodsId").toString();
//        String nowPrice = jsonObject.get("goodsId").toString();
//        String yPircele = jsonObject.get("goodsId").toString();
        if (jsonObject != null) {
            //高佣转链接
            String s = DataokeUtils.conventUrl(jsonObject.get("goodsId").toString());
            JSONObject goodDetails = DataokeUtils.getGoodDetails(jsonObject.get("goodsId").toString());
            //商品标题
            String title = goodDetails.get("title").toString();
            //图片
            String mainPic = goodDetails.get("mainPic").toString();
            //商品原价
            String originalPrice = goodDetails.get("originalPrice").toString();
            //券后价
            String actualPrice = goodDetails.get("actualPrice").toString();
            //优惠券额度
            String couponPrice = goodDetails.get("couponPrice").toString();
            //店铺名称
            String shopName = goodDetails.get("shopName").toString();
            String yh = "";
            if (Integer.parseInt(couponPrice) > 0){
                yh = "【预计优惠】" + couponPrice + "元\n";
            }

           /* TbkItemInfoGetResponse tbkItemInfoGetResponse = TaoBaoUtils.get(serverUrl, appKey, secert, jsonObject.get("goodsId").toString());
            String itemUrl = tbkItemInfoGetResponse.getResults().get(0).getItemUrl();
            String title = tbkItemInfoGetResponse.getResults().get(0).getTitle();
            String pictUrl = tbkItemInfoGetResponse.getResults().get(0).getPictUrl();
            String reservePrice = tbkItemInfoGetResponse.getResults().get(0).getReservePrice();
            String zkFinalPrice = tbkItemInfoGetResponse.getResults().get(0).getZkFinalPrice();*/
            //物料搜索
//        TbkDgMaterialOptionalResponse optional = TaoBaoUtils.optional(serverUrl, appKey, secert, Content);
//        TbkDgMaterialOptionalResponse.MapData mapData = optional.getResultList().get(0);
//        TbkTpwdCreateResponse tbkTpwdCreateResponse = TaoBaoUtils.create(serverUrl, appKey, secert, mapData.getTitle(),"https:" + mapData.getCouponShareUrl(), mapData.getPictUrl());

//        TbkTpwdCreateResponse tbkTpwdCreateResponse = TaoBaoUtils.create(serverUrl, appKey, secert, title,"https:" + itemUrl,pictUrl );
            if (StringUtils.isNotBlank(jsonObject.get("goodsId").toString())) {
                contentB ="店铺名称：" + shopName + "\n" +
                            title + "\n" +
                        "【在售价】" + originalPrice + " 元\n" +
                        "【券后价】" + actualPrice + "元\n" +
                        yh +
                        "-----------------\n" +
//                    "【立即领券】复制$pwhEc1W7zGL$打开手机淘宝领券并下单\n" +
                        "【立即下单】复制" + s + "打开手机淘宝立即下单\n" +
                        "【温馨提示】下单时候如果显示在售价格比自己看到的贵，点下单会自动减掉，最终价格以券后价为准。";
            } else {
                contentB = "SORRY，您查询的宝贝暂时没有活动，可以换销量高的宝贝试试!";
            }
        } else {
            contentB = "SORRY，您查询的宝贝暂时没有活动，可以换销量高的宝贝试试!";
//            contentB = "系统消息\n" +
//                    "——————\n" +
//                    "此宝贝暂时没有活动，可以换销量高的宝贝试试\n" +
//                    "发“找+商品”试试，如“找口红”";
        }


        //处理文本类型,回复用户输入的内容
        if ("text".equals(MsgType)) {
            message = MessageUtil.initMessage(FromUserName, ToUserName, contentB, MsgType);
        }
        log.info("消息体：{}", message);
        return message;
    }


}

