package com.atguigu.gmall.common.constant;

public class SysRedisConst {
    public static final String NULL_VAL = "x";
    public static final Long NULL_VAL_TTL = 60*30L;
    public static final Long SKU_DETAIL_TTL = 60*60*24*7L;
    public static final String LOCK_SKU_DETAIL = "lock:sku:detail:";
    public static final String SKU_INFO_PREFIX = "sku:info:";
    public static final String BLOOM_SKUID = "bloom:skuid";
    public static final String CACHE_CATEGORYS = "categorys";
    public static final String SKU_HOTSCORE_PREFIX = "sku:hotscore:";
    public static final String LOGIN_USER = "user:login:";
    public static final String USERID_HEADER ="userid" ;


    public static final String USERTEMPID_HEADER ="usertempid"  ;
    public static final String CART_KEY = "cart:user:";//用户id或临时id
    public static final String ORDER_TEMP_TOKEN = "order:temp:token:";
    public static final Integer ORDER_CLOSE_TTL=60*45;//秒为单位
    public static final long ORDER_REFUND_TTL = 60*60*24*30;
    public static final String MQ_RETRY ="mq:message:" ;
    public static final String CACHE_SECKILL_GOODS = "seckill:goods:";
    public static final String CACHE_SECKILL_GOODS_STOCK = "seckill:goods:stock:";
    public static final String SECKILL_CODE ="seckill:code:" ;
    public static final String SECKILL_ORDER = "seckill:goods:order:";
}
