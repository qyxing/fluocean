package cn.originalstar.fluocean.vo;

import java.util.HashMap;
import java.util.Map;

public class OceanResponse {

    public static Map<String, Object> error(int code, String message){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> success(String message){
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", message);
        return response;
    }

    public static Map<String, Object> reward(int code, String message, boolean verify, int amount, String name){
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("verify", verify);
        response.put("amount", amount);
        response.put("name", name);
        return response;
    }
}