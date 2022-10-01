package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Jsons {
    private static ObjectMapper mapper = new ObjectMapper();

    //对象转json字符串
    public static String toStr(Object object) {
        //jackson
        if (StringUtils.isEmpty(object)){
            return null;
        }

        try {
            String s = mapper.writeValueAsString(object);
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    //带复杂泛型的json逆转对象  这个可以兼容下边的普通转对象
    public static <T> T toObj(String jsonStr, TypeReference<T> tTypeReference){
        if (StringUtils.isEmpty(jsonStr)){
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(jsonStr, tTypeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }


    //json转普通对象
    public static <T> T toObj(String jsonStr,Class<T> clz)  {
        if (StringUtils.isEmpty(jsonStr)){
            return null;
        }

        T t = null;
        try {
            t = mapper.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return t;

    }

}
