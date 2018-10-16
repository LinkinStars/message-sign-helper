package com.linkinstars;

import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 消息签名
 * @author LinkinStar
 */
public class MessageSignHelper {
    
    /**
     * 签名
     * @param map 包含参数的map
     * @param paramKey 私钥
     * @return 签名之后的json
     */
    public static JSONObject sign(Map<String, String> map, String paramKey) {
        if (map.size() == 0) {
            return new JSONObject();
        }

        TreeMap<String, String> sortedMap = new TreeMap<>(map);

        StringBuilder signedStr = new StringBuilder();
        sortedMap.forEach(
                (key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        signedStr.append(key);
                        signedStr.append("=");
                        signedStr.append(value);
                        signedStr.append("&");
                    }
                }
        );
        signedStr.append("key");
        signedStr.append("=");
        signedStr.append(paramKey);
        
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(map);
        jsonObject.put("sign", md5Str(signedStr.toString()));
        return jsonObject;
    }

    /**
     * 验签
     * @param jsonObject 包含签名的json数据
     * @param paramKey 私钥
     * @return 签名正确：true； 签名错误：false
     */
    public static boolean inspect(JSONObject jsonObject, String paramKey) {
        String originSign = jsonObject.getString("sign");
        if (originSign == null || originSign.isEmpty()) {
            return false;
        }

        TreeMap<String, Object> sortedMap = new TreeMap<>(jsonObject.getInnerMap());

        StringBuilder signedStr = new StringBuilder();
        sortedMap.forEach(
                (key, value) -> {
                    if (!key.equals("sign") && value != null && !String.valueOf(value).isEmpty()) {
                        signedStr.append(key);
                        signedStr.append("=");
                        signedStr.append(value);
                        signedStr.append("&");
                    }
                }
        );
        signedStr.append("key");
        signedStr.append("=");
        signedStr.append(paramKey);

        String sign = md5Str(signedStr.toString());
        return originSign.equals(sign);
    }

    private static String md5Str(String paramStr){
        byte[] btInput = paramStr.getBytes(StandardCharsets.UTF_8);
        MessageDigest mdInst = null;
        try {
            mdInst = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        mdInst.update(btInput);
        byte[] mDbyte = mdInst.digest();

        StringBuilder md5Str = new StringBuilder();
        for (byte paramByte : mDbyte) {
            if (Integer.toHexString(0xFF & paramByte).length() == 1) {
                md5Str.append("0").append(Integer.toHexString(0xFF & paramByte));
            } else {
                md5Str.append(Integer.toHexString(0xFF & paramByte));
            }
        }
        return md5Str.toString().toUpperCase();
    }
}
