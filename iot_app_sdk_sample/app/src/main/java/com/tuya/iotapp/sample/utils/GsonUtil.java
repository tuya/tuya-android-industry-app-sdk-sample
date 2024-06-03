package com.tuya.iotapp.sample.utils;


import com.google.gson.Gson;

public class GsonUtil {
    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
