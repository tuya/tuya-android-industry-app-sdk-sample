package com.tuya.dev.network.http;

import okhttp3.HttpUrl;

public interface SimpleResponseCallback {
    void failed(String errorMessage, String host);

    void handlingResponse(int responseCode, String bodyString, HttpUrl httpUrl);

    void handlingFailed(String errorCode, String errorMessage);
}
