package com.wdx.springbootwx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.TreeMap;

public class DataokeUtils {

    /**
     * 淘口令解析出商品id
     * @param content
     * @return
     */
    public static JSONObject getGoodsId(String content){
        String url = "https://openapi.dataoke.com/api/tb-service/parse-taokouling";
        String appKey = "5f3bb88a9d465";
        String appSecret = "430b3bb77a24b25d3d7931be915d48a7";
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.0.0");
        paraMap.put("appKey", appKey);
        paraMap.put("content", content);
        String data = ApiClient.sendReq(url, appSecret, paraMap);
        System.out.println(data);
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject data1 = (JSONObject) jsonObject.get("data");
        return data1;
    }

    /**
     * 高佣转链
     * @param goodsId
     * @return
     */
    public static String conventUrl(String goodsId){
        String url = "https://openapi.dataoke.com/api/tb-service/get-privilege-link";
        String appKey = "5f3bb88a9d465";
        String appSecret = "430b3bb77a24b25d3d7931be915d48a7";
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.2.0");
        paraMap.put("appKey", appKey);
        paraMap.put("goodsId",goodsId );
        String data = ApiClient.sendReq(url, appSecret, paraMap);
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject data1 = (JSONObject) jsonObject.get("data");
        return data1.get("tpwd").toString();
    }

    /**
     * 高佣转链
     * @param goodsId
     * @return
     */
    public static JSONObject getGoodDetails(String goodsId){
        String url = "https://openapi.dataoke.com/api/goods/get-goods-details";
        String appKey = "5f3bb88a9d465";
        String appSecret = "430b3bb77a24b25d3d7931be915d48a7";
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.2.3");
        paraMap.put("appKey", appKey);
        paraMap.put("goodsId", goodsId);
        String data = ApiClient.sendReq(url, appSecret, paraMap);
        JSONObject jsonObject = JSON.parseObject(data);
        JSONObject data1 = (JSONObject) jsonObject.get("data");
        return data1;
    }

    public static void main(String[] args) {
        String s = conventUrl("613200652268");
        JSONObject goodDetails = getGoodDetails("613200652268");
        String title = goodDetails.get("title").toString();
        String mainPic = goodDetails.get("mainPic").toString();
        //商品原价
        String originalPrice = goodDetails.get("originalPrice").toString();
        //券后价
        String actualPrice = goodDetails.get("actualPrice").toString();
        //优惠券额度
        String couponPrice = goodDetails.get("couponPrice").toString();

        System.out.println(goodDetails);
    }

}
