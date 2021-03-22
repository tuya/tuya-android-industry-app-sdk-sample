package com.tuya.iotapp.devices.business;

import com.tuya.iotapp.common.utils.IotCommonUtil;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

import org.json.JSONObject;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:44 PM
 */
public class DeviceBusiness extends Business {
    private static final String ACTIVITOR_TOKEN_API = "v1.0/iot-03/device-registration/token";

    private static final String ACTIVITOR_RESULT_API = "v1.0/iot-03/device-registration/tokens/{token}";

    private static final String DEVICE_RESULT_API = "v1.0/iot-03/devices";

    private static final String DEVICE_ASSET_API = "v1.0/iot-02/assets/{asset_id}/devices";

    private String mCountryCode;

    public DeviceBusiness(String countryCode) {
        mCountryCode = countryCode;
    }

    /**
     * 或许 配网令牌token
     *
     * @param assetId
     * @param uid
     * @param listener
     */
    public void getDeviceRegistrationToken(String assetId,
                                           String uid,
                                           String type,
                                           ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_TOKEN_API, "1.0", "POST", mCountryCode);
        params.putPostData("pairing_type", type);
        params.putPostData("time_zone_id", IotCommonUtil.getTimeZoneId());
        params.putPostData("asset_id", assetId);
        params.putPostData("uid", uid);
        params.putPostData("extension", null);

        asyncRequest(params, JSONObject.class, listener);

    }

    /**
     * 获取令牌token 查询设备结果
     *
     * @param token
     */
    public void getRegistrationResult(String token,
                                      ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_RESULT_API.replace("{token}", token), "1.0", "GET", mCountryCode);
        asyncRequest(params, JSONObject.class, listener);
    }

    public void queryDevicesByAssetId(String assetId, ResultListener<JSONObject> listener) {
        IotApiParams params = new IotApiParams(DEVICE_ASSET_API.replace("{asset_id}", assetId), "1.0", "GET", mCountryCode);
        asyncRequest(params, JSONObject.class, listener);
    }

}
