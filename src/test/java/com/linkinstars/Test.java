package com.linkinstars;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试
 * @author LinkinStar
 */
public class Test {
    public static void main(String[] args) {
        String paramKey = "XXX";
        Map<String, String> map = new HashMap<>();
        map.put("username", "admin");
        map.put("name", "xxx");
        map.put("age", "2");
        map.put("test", "");
        
        JSONObject jsonObject = MessageSignHelper.sign(map, paramKey);
        System.out.println(jsonObject);
        
        boolean result = MessageSignHelper.inspect(jsonObject, paramKey);
        System.out.println(result);
    }
}
