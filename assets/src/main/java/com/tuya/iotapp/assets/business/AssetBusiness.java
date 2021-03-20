package com.tuya.iotapp.assets.business;

import com.alibaba.fastjson.JSONObject;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

/**
 *  AssetBusinessImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:48 PM
 */
public class AssetBusiness extends Business {

    private static final String ASSETS_API = "v1.0/iot-03/users/assets?page_no=0&page_size=10";

    private String mCountryCode;

    public AssetBusiness(String countryCode) {
        mCountryCode = countryCode;
    }

    public void queryAssets() {
        IotApiParams params = new IotApiParams(ASSETS_API, "1.0", "GET", mCountryCode);
        asyncRequest(params, new ResultListener<JSONObject>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                LogUtils.d("assets", "=====false==="+bizResponse.getCode()+"  " +bizResponse.getCode());
            }

            @Override
            public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                LogUtils.d("assets","======success===="+bizResult.toJSONString());
            }
        });
    }
}
