package com.tuya.dev.json_parser.api;

public interface IJsonParser {

    public String toJsonString(Object object);

    public <T> T parseObject(String text, Class<T> clazz);
}