package com.tw.gpsd.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by wei.tian
 * 2019/4/4
 */
public final class GsonUtil {
    private GsonUtil() {
        throw new IllegalStateException("No instance!");
    }

    private static Gson customGson;
    private static Gson gson;

    static {
        customGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        gson = new Gson();
    }

    public static String toCustomJson(Object object) {
        return customGson.toJson(object);
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromCustomJson(String json, Class<T> clazz) {
        return customGson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
