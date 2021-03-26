package com.tuya.smart.json_parser.fastjosn;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.tuya.dev.json_parser.api.IJsonParser;

import java.util.List;

@AutoService(IJsonParser.class)
public class JsonParserFastJsonImpl implements IJsonParser {

    @Override
    public String toJsonString(Object object) {

        return JSON.toJSONString(object);
    }

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {
        return JSONObject.parseObject(text, clazz);
    }

    @Override
    public <T> List<T> parseList(String text, Class<T> clazz) {
        return JSONObject.parseArray(text, clazz);
    }

}