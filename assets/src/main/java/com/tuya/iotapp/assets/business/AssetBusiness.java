package com.tuya.iotapp.assets.business;

import androidx.dynamicanimation.animation.SpringAnimation;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

import org.json.JSONObject;

import java.net.URLDecoder;

/**
 * AssetBusinessImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:48 PM
 */
public class AssetBusiness extends Business {

    private static final String ASSETS_API = "/v1.0/iot-03/users/assets";

    private String mCountryCode;

    public AssetBusiness(String countryCode) {
        mCountryCode = countryCode;
    }

    public void queryAssets() {
        String assetsApi = ASSETS_API;
        try {
            assetsApi = URLDecoder.decode(ASSETS_API, "utf-8");
        } catch (Exception e) {
            LogUtils.d("decode", "exception : " + e.getMessage());
        }

        IotApiParams params = new IotApiParams(assetsApi, "1.0", "GET", mCountryCode);
        params.putGetParams("page_no", "0");
        params.putGetParams("page_size", "10");
        asyncRequest(params, String.class, new ResultListener<String>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, String bizResult, String apiName) {
                LogUtils.d("assets", "=====false===" + bizResponse.getCode() + "  " + bizResponse.getCode());
            }

            @Override
            public void onSuccess(BusinessResponse bizResponse, String bizResult, String apiName) {
                LogUtils.d("assets", "======success====" + bizResult);
            }
        });
    }
}
