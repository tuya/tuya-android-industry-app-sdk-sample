package com.tuya.iotapp.sample;

import android.bluetooth.BluetoothClass;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuya.iotapp.common.utils.IotCommonUtil;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.common.utils.MD5Util;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

/**
 *  测试类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 6:07 PM
 */
public class TestBusiness extends Business {

    ///v1.0/developer-cloud/users/login
    private static final String LOGIN_API = "v1.0/iot-03/users/login";

    private static final String ASSETS_API = "v1.0/iot-03/users/assets?page_no=0&page_size=10";

    private static final String ACTIVITOR_TOKEN_API = "v1.0/iot-03/device-registration/token";

    private static final String ACTIVITOR_RESULT_API = "v1.0/iot-03/device-registration/tokens/{token}";

    private static final String DEVICE_RESULT_API = "v1.0/iot-03/devices";

    private static final String DEVICE_ASSET_API = "v1.0/iot-02/assets/{asset_id}/devices";

    /**
     *  小程序账密：user_name:18640825065   password:（b8a762334ab0c2f25a0503a86b152f77）Lbn123456  project-code：p1615796832753ggk9jt
     *
     *  新的账密：
     *
     * @param listener
     */
    public void login(ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(LOGIN_API, "1.0", "POST");
        String password = MD5Util.md5AsBase64("Lbn123456");
        //用户名：15068975916  密码：Lbn123456
        params.putPostData("user_name", "xiaoxiao.li@tuya.com");
        params.putPostData("password", "c679a76bc4315372c57ad1ba0a8e59f6");
        //params.putPostData("project_code", "p1615796832753ggk9jt");
        asyncRequest(params, listener);
    }
    /**
     * 或许 配网令牌token
     *
     * @param assetId
     * @param uid
     * @param listener
     */
    public void getDeviceRegistrationToken(String assetId, String uid, ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_TOKEN_API, "1.0", "POST");
        params.putPostData("pairing_type", "AP");
        params.putPostData("time_zone_id", IotCommonUtil.getTimeZoneId());
        params.putPostData("asset_id", "1372829753920290816");
        params.putPostData("uid", uid);
        params.putPostData("extension", null);

        asyncRequest(params, listener);

    }

    /**
     * 获取令牌token 查询设备结果
     *
     * @param token
     */
    public void getRegistrationResult(String token, ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_RESULT_API.replace("{token}", token), "1.0", "GET");
        asyncRequest(params, listener);
    }

    public void testQueryAssets() {
        //String queryParam = "/project_code/p1615796832753ggk9jt";
        IotApiParams params = new IotApiParams(ASSETS_API, "1.0", "GET");
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

    public void queryDevicesByAssetId(String assetId, ResultListener<JSONObject> listener) {
        //DEVICE_ASSET_API.replace("{asset_id}", assetId)
        IotApiParams params = new IotApiParams(DEVICE_ASSET_API.replace("{asset_id}", assetId), "1.0", "GET");
        asyncRequest(params, listener);
    }
}
