package com.tuya.dev.json_parser.api;

import java.util.List;

public interface IJsonParser {

    public String toJsonString(Object object);

    public <T> T parseObject(String text, Class<T> clazz);

    public <T> List<T> parseList(String text, Class<T> clazz);
}