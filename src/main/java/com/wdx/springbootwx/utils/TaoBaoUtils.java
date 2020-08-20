package com.wdx.springbootwx.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;

public class TaoBaoUtils {

    /**
     * 淘宝客-公用-淘口令生成
     * @return
     */
    public static TbkTpwdCreateResponse create (String serverUrl,String appKey,String secret,String text,String url,String logoUrl) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, secret);
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        //设置业务参数
//      req.setUserId("123");
        req.setText(text);
        req.setUrl(url);
        req.setLogo(logoUrl);
        TbkTpwdCreateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        return rsp;
    }

    /**
     * 淘宝客-推广者-物料搜索
     * @return
     */
    public static TbkDgMaterialOptionalResponse  optional (String serverUrl,String appkey,String secret,String q) throws ApiException {
        TaobaoClient taobaoClient = new DefaultTaobaoClient(serverUrl, appkey, secret);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        //链接形式：1：PC，2：无线，默认：１
        req.setPlatform(1L);
       /* req.setStartDsr(10L);
        req.setPageSize(20L);
        req.setPageNo(1L);
        req.setPlatform(1L);

        req.setEndPrice(10L);
        req.setStartPrice(10L);
        req.setIsOverseas(false);
        req.setIsTmall(false);
        req.setSort("tk_rate_des");*/
//        req.setItemloc("杭州");
//        req.setCat("16,18");
        //商品筛选-查询词
        req.setQ(q);
        //商品筛选-淘客佣金比率上限。如：1234表示12.34%
        req.setEndTkRate(1234L);
        //商品筛选-淘客佣金比率下限。如：1234表示12.34%
        req.setStartTkRate(1L);
        //优惠券筛选-是否有优惠券。true表示该商品有优惠券，false或不设置表示不限
        req.setHasCoupon(false);
        //ip参数影响邮费获取，如果不传或者传入不准确，邮费无法精准提供
        req.setIp("13.2.33.4");
        //	mm_xxx_xxx_12345678三段式的最后一段数字
        req.setAdzoneId(110762700106l);
        //商品筛选-淘客佣金比率上限。如：1234表示12.34%
        req.setEndKaTkRate(1234L);
        //商品筛选-淘客佣金比率下限。如：1234表示12.34%
        req.setStartKaTkRate(1L);
        //排序_des（降序），排序_asc（升序），销量（total_sales），淘客佣金比率（tk_rate）， 累计推广量（tk_total_sales），总支出佣金（tk_total_commi），价格（price）
        req.setSort("total_sales_des");
        //商品筛选-是否包邮。true表示包邮，false或不设置表示不限
        req.setNeedFreeShipment(true);
        //商品筛选-是否加入消费者保障。true表示加入，false或不设置表示不限
        req.setNeedPrepay(true);
        //	商品筛选(特定媒体支持)-成交转化是否高于行业均值。True表示大于等于，false或不设置表示不限
        req.setIncludePayRate30(true);
        //商品筛选-好评率是否高于行业均值。True表示大于等于，false或不设置表示不限
        req.setIncludeGoodRate(true);
        //商品筛选-好评率是否高于行业均值。True表示大于等于，false或不设置表示不限
        req.setIncludeRfdRate(true);
//        req.setMaterialId(17004L);
    /*

        req.setIncludeRfdRate(true);
        req.setNpxLevel(2L);
        req.setEndKaTkRate(1234L);
        req.setStartKaTkRate(1234L);
        req.setDeviceEncrypt("MD5");
        req.setDeviceValue("xxx");
        req.setDeviceType("IMEI");
        req.setLockRateEndTime(1567440000000L);
        req.setLockRateStartTime(1567440000000L);*/
//        req.setLongitude("121.473701");
//        req.setLatitude("31.230370");
//        req.setCityCode("310000");
//        req.setSellerIds("1,2,3,4");
//        req.setSpecialId("2323");
//        req.setRelationId("3243");
        TbkDgMaterialOptionalResponse rsp = taobaoClient.execute(req);
        System.out.println(rsp.getBody());
        return rsp;
    }


    public static TbkItemInfoGetResponse get(String serverUrl,String appKey,String secret,String s) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, secret);
        TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
        req.setNumIids(s);
        req.setPlatform(1L);
        req.setIp("11.22.33.43");
        TbkItemInfoGetResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        return rsp;
    }

    public static void main(String[] args) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "31007070", "996b541c4613a0431ad48ced1ac81c65");
//        get("http://gw.api.taobao.com/router/rest", "31007070", "996b541c4613a0431ad48ced1ac81c65");
        //找到对应 类，比如taobao.trade.fullinfo.get接口对应的请求类为TradeFullinfoGetRequest

    }
}
