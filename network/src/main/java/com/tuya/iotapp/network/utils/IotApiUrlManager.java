package com.tuya.iotapp.network.utils;

import android.text.TextUtils;

import com.tuya.iotapp.common.utils.MD5Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.HttpUrl;

/**
 *  url处理类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 2:51 PM
 */
public class IotApiUrlManager {
    public static String getUrlWithQueryString(boolean shouldEncodeUrl, String url, final Map<String, String> params) {
        if (url == null) return null;
        if (shouldEncodeUrl) url = url.replace(" ", "%20");
        if (params != null) {
            HttpUrl parse = HttpUrl.parse(url);
            HttpUrl.Builder builder;
            String domain;
            if (parse.isHttps())
                domain = url.substring(8);
            else
                domain = url.substring(7);
            if (domain.contains(":")) {
                try {
                    URL mUrl = new URL(url);
                    builder = new HttpUrl.Builder().port(mUrl.getPort());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    builder = new HttpUrl.Builder();
                }
            } else {
                builder = new HttpUrl.Builder();
            }
            for (ConcurrentHashMap.Entry<String, String> entry : params.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            for (String path : parse.pathSegments()) {
                builder.addPathSegment(path);
            }
            return builder.scheme(parse.scheme()).host(parse.host()).build().toString();
        }

        return url;
    }

    public static String getRequestKeyBySorted(Map<String, String> params) {
        List<String> keys = new LinkedList<>(params.keySet());
        Collections.sort(keys);
        String str = "";
        for (String key : keys) {
            if (TextUtils.isEmpty(params.get(key))) continue;
            if (!"".equals(str)) str += "||";
            str += key + "=" + params.get(key);
        }
        return MD5Util.md5AsBase64(str);
    }
}
