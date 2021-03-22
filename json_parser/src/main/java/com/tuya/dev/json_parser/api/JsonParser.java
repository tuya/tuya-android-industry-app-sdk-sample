package com.tuya.dev.json_parser.api;

import java.util.ServiceLoader;

/**
 * Json Parser Manager Instance
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/19 10:43 AM
 */
public class JsonParser {
    private static IJsonParser jsonParser;

    static {
        //todo 兜底方案考虑
        jsonParser = ServiceLoader.load(IJsonParser.class).iterator().next();
    }

    public static String toJsonString(Object object) {
        return jsonParser.toJsonString(object);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return jsonParser.parseObject(text, clazz);
    }
}
