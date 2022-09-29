package com.atguigu.gmall.model.to;

import lombok.Data;

@Data
public class ValueSkuJsonTo {

    private Long skuId;
    private String valueJson;  //{"118|120":50,"119|121":49} 这样的json字符串

}
