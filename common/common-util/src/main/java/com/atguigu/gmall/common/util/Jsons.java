package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Jsons {
    private static ObjectMapper mapper = new ObjectMapper();

    //对象转json字符串
    public static String toStr(Object object) {
        //jackson


        try {
            String s = mapper.writeValueAsString(object);
            return s;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
    //json转对象
    public static <T> T toObj(String jsonStr,Class<T> clz)  {
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
