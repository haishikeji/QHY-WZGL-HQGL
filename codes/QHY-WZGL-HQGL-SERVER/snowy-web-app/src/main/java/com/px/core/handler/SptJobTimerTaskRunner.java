/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package com.px.core.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import com.px.biz.modular.codegys.entity.CgCodeGys;
import com.px.biz.modular.codegys.service.CgCodeGysService;
import com.px.biz.modular.jhshd.entity.CgJhShd;
import com.px.biz.modular.jhshd.service.CgJhShdService;
import com.px.biz.modular.jhspckmx.entity.CgJhSpckmx;
import com.px.biz.modular.jhspckmx.service.CgJhSpckmxService;
import com.px.biz.modular.jhspmx.entity.CgJhSpmx;
import com.px.biz.modular.jhspmx.service.CgJhSpmxService;
import com.px.biz.modular.kcspdm.entity.CgKcSpdm;
import com.px.biz.modular.kcspdm.service.CgKcSpdmService;
import com.px.biz.modular.org.entity.BizOrg;
import com.px.biz.modular.org.service.BizOrgService;
import com.px.biz.modular.sptckd.param.BizSptCkdAddParam;
import com.px.biz.modular.sptckd.service.BizSptCkdService;
import com.px.biz.modular.sptgys.param.BizSptGysAddParam;
import com.px.biz.modular.sptgys.service.BizSptGysService;
import com.px.biz.modular.sptrkd.param.BizSptRkdAddParam;
import com.px.biz.modular.sptrkd.service.BizSptRkdService;
import com.px.biz.modular.sptspdm.param.BizSptSpdmAddParam;
import com.px.biz.modular.sptspdm.service.BizSptSpdmService;
import com.px.biz.modular.zwbzrb.param.CgZwBzrbAddParam;
import com.px.biz.modular.zwbzrb.service.CgZwBzrbService;
import com.px.common.timer.CommonTimerTaskRunner;

import javax.annotation.Resource;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * 定时器的一个示例
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Slf4j
@Component
public class SptJobTimerTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private CgKcSpdmService cgKcSpdmService;

    @Resource
    private CgCodeGysService cgCodeGysService;

    @Resource
    private CgJhShdService cgJhShdService;

    @Resource
    private CgJhSpmxService cgJhSpmxService;

    @Resource
    private CgJhSpckmxService cgJhSpckmxService;

    @Resource
    private BizSptSpdmService bizSptSpdmService;

    @Resource
    private BizSptGysService bizSptGysService;

    @Resource
    private BizSptRkdService bizSptRkdService;

    @Resource
    private BizSptCkdService bizSptCkdService;

    @Resource
    private BizOrgService bizOrgService;

    @Resource
    private CgZwBzrbService cgZwBzrbService;
//    //测试环境
//    //PROJECT_ID
//    private String PROJECT_ID = "19ca14e7ea6328a42e0eb13d585e4c22";
//    //PRIV_KEY
//    private String PRIV_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC5ZSPmuqX5KZZP" +
//            "NNMJOCFET9ij90edFRRaVEuK0gb2/NYG5FCqCBHmwKVn5eAjty++42D7NGqWHgwd" +
//            "Cdv//JMAK+tz1fXwNUG8jedfe7r0hYQeD4OgQYkqIvGTTw9Rmb4aQ8iY6G2mbj5D" +
//            "GFLKyEa7VPFZZRDInuK7+7l/WOiU0nrtPo980FeliB4EZK7FWF2Ux9W5BtTHixjP" +
//            "mfHJtqOaHWCr4A7DTXRBZ/YhZw+vfT7ACgUe6QKIZcGaKW0hCXh4aPPW8P2XOw0Q" +
//            "HOmbtsBXhr1OIMf802hVbnk91CeVdYCNYLqnqAgTVuvexOQIfepqctAORNsfT4rd" +
//            "wal5F5GTAgMBAAECggEAC7/qthFHOpLVqWBuLYvmo9cxojamTcDLwOvNu9cSxO2C" +
//            "PP6UVCpdxBOaiAqsGWdfOchcWZ9ZT/lIiECbNk6SHHUsH3PEefxOFb6ils0XLK8X" +
//            "j/Yov+vXXxnG2LCuhlvpje2U1YgJhv4+qO7uNFbcELxR7Xn3xdc4BHTwLS970g6n" +
//            "O3qIjiLHgginOXfnDku7z2BaigfpC88RkkJI+10vIn35rGYkjdqFN0ZFFPdOwF5J" +
//            "bRNeSgcNSZk7p0iyhwEMloQ4157rkhvPkTec85nXHltVgAdF0P1QG36vuWBb+W8E" +
//            "M4rPwPkNgXjfuk1Av4EXVBFgPey8U+b+YHhy6FszVQKBgQDoCMJ49hI2tljQtgEe" +
//            "05+csRbugdv79KZivSSUK/WsptZxQfZxtCgoiXd7jEriYV8upFumM3p7E9U2Y0o6" +
//            "pb3BI7kItcRXNHTitDM8kG5H5Vv4E6YcpBXAkU4tMfueOC2f/hafhD2D1HVTkx/P" +
//            "/aVtGpPUnBAEsPq5kZs+XGXBtQKBgQDMizC8xpNgRorIagY3BhrelzEzfuHtpJFb" +
//            "ipAJ6DLF+Hp7hG1ErNVpr4bwKsQeSUbY1IlWLIch/seBqO+qUWkEjhyW2o48nmm7" +
//            "uRAIfzM5Sfvv05gMQEtwrS4PFesBYUmgHfE9wnIZVq3+iWdwwYqAQrrwBnB8CPvk" +
//            "aAzwoAAzJwKBgEf/79h5As3XZ1fcIhzerfiBOwxnX0ufbsHUzzTrLyCT5JbgUQiX" +
//            "Tp7FiT3dGp+Ed3SRq562+pqWXOKzjKUGKQt1ekUpofMtY9e08vEoIBBPK2qfLjxU" +
//            "7isC6686g0llRUMQJRT6g9wUYRO943E20843IQgoJRBPmn1aoUGIikrBAoGAcWP8" +
//            "ObpvuuCAUUgNd/VncdwpB14giyg++Jtg7E0P0JXF+LFaDccua+9Fg+CBZjTDFLMG" +
//            "C4KVyy+9k9B+RFHrQP4zNSGl1k8PWGToUX2KfQjZpIMNeEUwTR+EOBKze8leEmhm" +
//            "8xDXRdq6jC/J60UvrZjQnv1C4E/urBlOixp34eMCgYBD/K7DJ+Mj1oSI1RSgYyCp" +
//            "jqxkEqHpvXt79rUJWDNw34dldxlQIijoSIaYSyuN88grGTDYHvl8R76smq5mUF6O" +
//            "8KfB/ioGAZRygbfgZtsQYwBQKCGqeeuL8V5QN0LRHr67XsAUQAzCKAGtuQKKxIBq" +
//            "ZW3QNEB8kTiHItibX9odJA==";
//
//    //对接平台接口地址
//    private String API_URL = "https://jgpt.hqwzcg.com/";
//


//
    //生成环境
    //PROJECT_ID
    private String PROJECT_ID = "45c48cce2e2d7fbdea1afc51c7c6ad26";
    //PRIV_KEY
    private String PRIV_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDY7EFi5mZX2X7c"
    +"hVRpeXRbAbSV6WyXhvA5d8N2uxcL9xwLrOcg13q9ogRfFrPpiHQQT18TZNgp1WQA"
            +"8QJkv84j89Q3rAHWbIqT315w3BwouP6pswy1krSn/yNOEYmbYQpTxylHEw/McLYc"
            +"EO6pT/HeNujg9cU84NV+rGFV9Gt/m9jUIR8gSCKR/uK+/4kyUPZc5qyntqxoVcjy"
            +"MSmcxaYNnU73WqWCF2cL7wnDCURAz+hW2vkemukknhkAUuphDk7AG/uAJ583YdNJ"
            +"j2Mh0ylCd0zo1BavXIbaopvGCv96bV0R8x97/WFXXFCGzwiuT1kbESgRtZd1G1MO"
            +"AzSuB1R7AgMBAAECggEALeYXk8CiiRYZNLn1eXfceeErf8jrVEiWDeLcdgPyRXLc"
            +"+U91fEp7yyI8AI7CNLjNriHNQUJGRtvDFDvqwLyg9pSCI+F+ZguDavSJfCHmx2sV"
            +"X3zmRIktNprSspMdZcYrtDxg7IwI/L4gmmGuzh5/VACUV/1I5a/BHNOGqPRVxqMk"
            +"jS9JCki73LLJ/+uJtvIa+icY8LjbwgGwvnWmeOXfoS5bqYcRzJGbKgOkP6ZdHc/D"
            +"1pXddN/7v+try5EApgkKxEraQKK8v+tjgiFsfEeYpBwHQ6O6OMp+ZicU6KgvcQ66"
            +"rmmjNsk4dLD2mUuOpukpqDevIRyhJSUFU5oKv/u4YQKBgQDsgvtx78HK40CSI4uW"
            +"XNKkhRiQdw8jGnEcff7b16DWngUyKAzVPWYXgKCbCLdvF3sC6m+wZEZCu77Oyiv2"
            +"VJYCVyM88nw9S3o4m/x40oimMa0EchmVRqrlCimSslsDCkEtAKlxtvRCmE207cF2"
            +"1rpDni+/xg7/riZ/3Nt7DUdsCwKBgQDqzBBsdFYoBspzCuSDOktZnD+NRID7EGQd"
            +"fQg2uHRi5vIFXcXvEW/ocip74pviaczJ+94E3qKxJ+LSO6z+z5KNxkytSIOvHRnY"
            +"8A21BJ1LATJXYvA611Foy2fUHenEemWbSXWDJ1U7ETg7cp03aTmw553Ccd6i0jIz"
            +"DIo+17yPUQKBgQDpZp2DpArIERow3dksCvBpIVvLfWM3zilf58sx4wq7oXgZNkLr"
            +"0UrF2M8mECUOAcsIZpjQJzTbOHIlmohl8LWVG68Pv3DyAGFeKjmudn4aHJ3U7FDo"
            +"6rNNkojSVybPyjfllGijatyEN88/xP4OK/vnLN2MUIWOtqsRtb/3IHSiXwKBgQDq"
            +"CaAxSsD6QtKXW3stZvTmjH04Zdke6FayogeG69NvJf6F5NPAGaRvkSTRua5iP81W"
            +"ZA0I05ZdeYF4xu0ITIUrdkbFzusbqKkz+QvgrInQ0o+IVWFZdXh7OYzz/SEfrFUN"
            +"NWrB4XnuV5qyfutA6U0NDcHNqM0fhN9uzQgb3dThEQKBgQCvbgvGqcE1xjXlCxlz"
            +"6PBYUF6YMc9K1EcmmpBlr31oRR1uJw5l5zKpFp2MIBUq6R7mZXdXBhtqVr1ZlEov"
            +"1H7AJJ7IGUiBRdRJARBCliynqHcgrtts8d9eEH69Gs8E/eAVrPuYAUAECtYDefsl"
            +"Bgt3guguZIQJmbTKrgzK0/YeXg==";

    //对接平台接口地址
    private String API_URL = "https://gxygst.jsed.edu.cn/";

    //鉴权接口
    private String AUTH_GETTOKEN = API_URL + "/openapi/Auth/GetToken";

    //食材类别
    private String GET_STOCK_TYPE = API_URL + "/openapi/Stock/GetStockType";

    //食材新增接口
    private String ADD_STOCK = API_URL + "/openapi/Stock/AddStock";

    //食材编辑接口
    private String EDIT_STOCK = API_URL + "/openapi/Stock/EditStock";

    //食材删除接口
    private String DEL_STOCK = API_URL + "/openapi/Stock/DelStock";

    //供应商新增接口
    private String ADD_SUPPLY = API_URL + "/openapi/Supply/AddSupply";

    //新增入库
    private String ADD_STOREIN = API_URL + "openapi/StoreIn/AddStoreIn";

    //新增出库
    private String ADD_STOREOUT = API_URL + "openapi/StoreOut/AddStoreOut";

    //token
    private String token = "";


    @Override
    public void action() throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, String> headers = new HashMap<>();
        headers.put("ProjectId", PROJECT_ID);
        headers.put("Timestamp", timestamp);
        String msg = PROJECT_ID + timestamp;
        String sign = MakeSign(msg, PRIV_KEY);
        headers.put("Sign", sign);
        String result = HttpUtil.createPost(AUTH_GETTOKEN).addHeaders(headers).execute().body();
        Map res = (Map) JSON.parse(result);
        token = (String) res.get("token");



        //食材相关
        //AddStock();

        //供应商相关
        //AddSupply();

        //入库相关
        //AddStorein();

        //入库相关
        AddStoreout();

        //生成日报
        //genBzrb();
    }

    /************认证相关******************/
    // 生成签名
    private String MakeSign(String msg, String privateKey) throws Exception {


        byte[] keyBytes = base64String2Byte(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 创建一个RSA密钥对生成器实例
        Signature signature = Signature.getInstance("SHA256withRSA");
        // 初始化Signature实例，使用私钥进行签名
        signature.initSign(priKey);
        signature.update(msg.getBytes());
        return byte2Base64String(signature.sign());

    }

    //转换1
    public byte[] base64String2Byte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    //转换2
    public String byte2Base64String(byte[] bytes) {
        return new String(new Base64().encode(bytes));
    }

    /************食材相关******************/
    public void AddStock(){

        HashMap<String, String> headerstmp = new HashMap<>();
        headerstmp.put("ProjectId", PROJECT_ID);
        headerstmp.put("Token", token);
        HashMap<String, Object> bodytmp = new HashMap<>();
        String resulttmp = HttpUtil.createPost(GET_STOCK_TYPE).addHeaders(headerstmp).body(JSONUtil.toJsonStr(bodytmp)).execute().body();
        Map restmp = (Map) JSON.parse(resulttmp);


        QueryWrapper<CgKcSpdm> cgKcSpdmQueryWrapper = new QueryWrapper<>();
        //cgKcSpdmQueryWrapper.eq("(select parent_id from biz_splb_tree where id=cg_kc_spdm.lbdm)","01");
        cgKcSpdmQueryWrapper.notInSql("id","(select spid from biz_spt_spdm )");
        List<CgKcSpdm> cgKcSpdmList=cgKcSpdmService.list(cgKcSpdmQueryWrapper);
        cgKcSpdmList.forEach(cgKcSpdm -> {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProjectId", PROJECT_ID);
            headers.put("Token", token);
            HashMap<String, Object> body = new HashMap<>();
            body.put("stock_name",cgKcSpdm.getSpmc()!=null?cgKcSpdm.getSpmc():"");

            body.put("price_unit",cgKcSpdm.getJldw()!=null?"1000004000002":"");
            body.put("brand",cgKcSpdm.getPpcd()!=null?cgKcSpdm.getPpcd():"");
            body.put("standard",cgKcSpdm.getSpgg()!=null?cgKcSpdm.getSpgg():"");
            body.put("bulk_ingredients",0);
            body.put("state",cgKcSpdm.getQybz().equals("是")?"1":"0");
            body.put("code",cgKcSpdm.getId()!=null?cgKcSpdm.getId():"");


            String result = HttpUtil.createPost(ADD_STOCK).addHeaders(headers).body(JSONUtil.toJsonStr(body)).execute().body();
            Map res = (Map) JSON.parse(result);
            if(!res.get("data").equals("")){
                long spCode=(Long)res.get("data");
                BizSptSpdmAddParam bizSptSpdmAddParam=new BizSptSpdmAddParam();
                bizSptSpdmAddParam.setSpcode(spCode);
                bizSptSpdmAddParam.setSpid(Long.valueOf(cgKcSpdm.getId()));
                bizSptSpdmService.add(bizSptSpdmAddParam);
            }

        });

    }

    public void AddSupply(){



        QueryWrapper<CgCodeGys> cgCodeGysQueryWrapper = new QueryWrapper<>();
        cgCodeGysQueryWrapper.ne("gysdm","100001");
        List<CgCodeGys> cgCodeGysList=cgCodeGysService.list(cgCodeGysQueryWrapper);
        cgCodeGysList.forEach(cgCodeGys -> {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProjectId", PROJECT_ID);
            headers.put("Token", token);
            HashMap<String, Object> body = new HashMap<>();
            body.put("name",cgCodeGys.getGysmc());

            body.put("usci",cgCodeGys.getGysdm());
            body.put("legal_representative",cgCodeGys.getFrdb()!=null?cgCodeGys.getFrdb():"无");
            body.put("contact",cgCodeGys.getDh()!=null?cgCodeGys.getDh():"无");
            body.put("attribute",0);
            body.put("contact_person",cgCodeGys.getLxr()!=null?cgCodeGys.getLxr():"无");



            String result = HttpUtil.createPost(ADD_SUPPLY).addHeaders(headers).body(JSONUtil.toJsonStr(body)).execute().body();
            Map res = (Map) JSON.parse(result);
            if(!res.get("data").equals("")){
                long gysCode=(Long)res.get("data");
                BizSptGysAddParam bizSptGysAddParam=new BizSptGysAddParam();
                bizSptGysAddParam.setGyscode(gysCode);
                bizSptGysAddParam.setGysid(Long.valueOf(cgCodeGys.getId()));
                bizSptGysService.add(bizSptGysAddParam);
            }

        });

    }


    public void AddStorein(){



        QueryWrapper<CgJhShd> cgJhShdQueryWrapper = new QueryWrapper<>();
        cgJhShdQueryWrapper.in("cglx","班组订货","部门备货");

        cgJhShdQueryWrapper.ne("spje",0);
        cgJhShdQueryWrapper.notInSql("shdh","select shdh from biz_spt_rkd ");
        cgJhShdQueryWrapper.eq("workstate","已收货");
        //cgJhShdQueryWrapper.eq("shdh","SH202405221215105001380");
        cgJhShdQueryWrapper.select("shdh,shrq,(select name from sys_org where cg_jh_shd.bmdm =id)bmmc,gysmc,spje");
        List<CgJhShd> cgJhShdList=cgJhShdService.list(cgJhShdQueryWrapper);
        cgJhShdList.forEach(shd -> {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProjectId", PROJECT_ID);
            headers.put("Token", token);
            HashMap<String, Object> body = new HashMap<>();
            body.put("dep_name",shd.getBmmc());
            body.put("supply_name",shd.getGysmc());
            body.put("store_in_code",shd.getShdh());

            body.put("bulk_ingredients",0);
            body.put("buy_type",0);
            body.put("is_direct_out",0);
            body.put("store_in_date", DateUtil.format(shd.getShrq(),"yyyy-MM-dd"));
            body.put("buy_money",shd.getSpje());
            QueryWrapper<CgJhSpmx> cgJhSpmxQueryWrapper = new QueryWrapper<>();
            cgJhSpmxQueryWrapper.eq("shdh",shd.getShdh());
            cgJhSpmxQueryWrapper.select("spmc,jhdj,sum(shsl)shsl,sum(jhje)jhje");
            cgJhSpmxQueryWrapper.groupBy("spmc,jhdj");
            List<CgJhSpmx> cgJhSpmxList= cgJhSpmxService.list(cgJhSpmxQueryWrapper);
            List<Map> l=new ArrayList<>();
            cgJhSpmxList.forEach(spmx->{
                HashMap<String, Object> listmap = new HashMap<>();
                listmap.put("stock_name",spmx.getSpmc());
                listmap.put("order_amount",spmx.getShsl());
                listmap.put("in_amount",spmx.getShsl());
                listmap.put("in_price",spmx.getJhdj());
                listmap.put("in_money",spmx.getJhje());
                listmap.put("production_date",DateUtil.format(shd.getShrq(),"yyyy-MM-dd"));
                l.add(listmap);

            });
            body.put("list",l);



            String result = HttpUtil.createPost(ADD_STOREIN).addHeaders(headers).body(JSONUtil.toJsonStr(body)).execute().body();
            Map res = (Map) JSON.parse(result);
            if(res.get("data")!=null&&!res.get("data").equals("")){
                String rkd=(String)((Map)res.get("data")).get("data");
                BizSptRkdAddParam bizSptRkdAddParam=new BizSptRkdAddParam();
                bizSptRkdAddParam.setShdh(shd.getShdh());
                bizSptRkdAddParam.setRkd(rkd);
                bizSptRkdService.add(bizSptRkdAddParam);
            }

        });

    }


    public void AddStoreout(){

        QueryWrapper<CgJhSpckmx> cgJhSpckmxQueryWrapper = new QueryWrapper<>();
//        cgJhSpckmxQueryWrapper.eq("workstate", "已收货");
//        cgJhSpckmxQueryWrapper.gt("id","1808057072676261890");
//        cgJhSpckmxQueryWrapper.notIn("cklx","商品退货");

        cgJhSpckmxQueryWrapper.gt("ckrq","2024-09-01");
        cgJhSpckmxQueryWrapper.lt("ckrq","2024-09-10");
        cgJhSpckmxQueryWrapper.notIn("cklx","商品退货");
        //cgJhSpckmxQueryWrapper.inSql("id"," select ckid from biz_spt_ckd where ckd is null");
        cgJhSpckmxQueryWrapper.inSql("bmdm","'0401','0402','0404','0410','0411','0413','0414','0415'");
        cgJhSpckmxQueryWrapper.notInSql("id"," select id from  (select * from cg_jh_spckmx where  ckrq>'2024-09-01' and ckrq<'2024-09-10' and bmdm in ('0401','0402','0404','0410','0411','0413','0414','0415')  and cklx not in('商品退货'))a where id in(select ckid from biz_spt_ckd )");
        cgJhSpckmxQueryWrapper.select("id, (case when cklx!='库存退库' then cksl else -cksl end)cksl,(select name from sys_org where id=cg_jh_spckmx.bmdm)bmmc,(select spcode from cg_kc_spdm_spt where cg_jh_spckmx.spdm = spdm)spdm, (case when cklx!='库存退库' then je else -je end)je, gydj,spjhrq,ckrq");
        List<CgJhSpckmx> cgJhSpckmxList= cgJhSpckmxService.list(cgJhSpckmxQueryWrapper);
//
        cgJhSpckmxList.forEach(spckmx->{
            HashMap<String, String> headers = new HashMap<>();
            headers.put("ProjectId", PROJECT_ID);
            headers.put("Token", token);
            HashMap<String, Object> body = new HashMap<>();
            if(spckmx.getBmmc().equals("梦溪教工食堂")){
                body.put("dep_name","梦溪教工餐厅");
                body.put("use_dep_name","梦溪教工餐厅");
            }else{
                body.put("dep_name",spckmx.getBmmc());
                body.put("use_dep_name",spckmx.getBmmc());
            }

            body.put("store_out_date",DateUtil.format(spckmx.getCkrq(),"yyyy-MM-dd HH:mm:ss"));
            body.put("store_out_code",spckmx.getId());
            body.put("affair_type","2");
            List<Map> l=new ArrayList<>();
            HashMap<String, Object> listmap = new HashMap<>();
            listmap.put("stock_code",spckmx.getSpdm());
            listmap.put("stock_cur_code","0");
            listmap.put("out_amount",spckmx.getCksl());
                listmap.put("price",spckmx.getGydj());
            l.add(listmap);
            body.put("list",l);
String json=JSONUtil.toJsonStr(body);
System.out.println(json);
            String result = HttpUtil.createPost(ADD_STOREOUT).addHeaders(headers).body(JSONUtil.toJsonStr(body)).execute().body();
            System.out.println(result);
            Map res = (Map) JSON.parse(result);
            if(res.get("data")!=null&&!res.get("data").equals("")){
                String ckd=(String)((Map)res.get("data")).get("data");
                BizSptCkdAddParam bizSptCkdAddParam=new BizSptCkdAddParam();
                bizSptCkdAddParam.setCkid(spckmx.getId());
                bizSptCkdAddParam.setCkd(ckd);
                bizSptCkdAddParam.setResult(result);
                bizSptCkdService.add(bizSptCkdAddParam);
            }else{
                BizSptCkdAddParam bizSptCkdAddParam=new BizSptCkdAddParam();
                bizSptCkdAddParam.setCkid(spckmx.getId());
                bizSptCkdAddParam.setResult(result);
                bizSptCkdService.add(bizSptCkdAddParam);
            }

        });




    }

    private void genBzrb(){
        Date yesterday=DateUtil.yesterday();
        String yesterdayStr=DateUtil.format(yesterday,"yyyy-MM-dd");
        List<BizOrg> orgList=bizOrgService.list();
        orgList.forEach(bizOrg -> {
            CgZwBzrbAddParam cgZwBzrbAddParam=new CgZwBzrbAddParam();
            cgZwBzrbAddParam.setBmdm(bizOrg.getId());
            cgZwBzrbAddParam.setBh(yesterdayStr);
            cgZwBzrbService.genBzrb( cgZwBzrbAddParam);
        });
    }
}
