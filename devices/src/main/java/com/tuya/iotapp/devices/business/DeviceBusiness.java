package com.tuya.iotapp.devices.business;

import com.tuya.iotapp.common.utils.IotCommonUtil;
import com.tuya.iotapp.devices.bean.AssetDeviceListBean;
import com.tuya.iotapp.devices.bean.RegistrationTokenBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

/**
 * DeviceBusiness
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:44 PM
 */
public class DeviceBusiness extends Business {
    private static final String ACTIVITOR_TOKEN_API = "/v1.0/iot-03/device-registration/token";

    private static final String ACTIVITOR_RESULT_API = "/v1.0/iot-03/device-registration/tokens/{token}";

    private static final String DEVICE_RESULT_API = "/v1.0/iot-03/devices";

    private static final String DEVICE_ASSET_API = "/v1.0/iot-02/assets/{asset_id}/devices";

    private static final String DEVICE_DELETE_API = "/v1.0/iot-03/devices/{device_id}";

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
                                           ResultListener<RegistrationTokenBean> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_TOKEN_API, "1.0", "POST", mCountryCode);
        params.putPostData("pairing_type", type);
        params.putPostData("time_zone_id", IotCommonUtil.getTimeZoneId());
        params.putPostData("asset_id", assetId);
        params.putPostData("uid", uid);
        params.putPostData("extension", null);

        asyncRequest(params, RegistrationTokenBean.class, listener);

    }

    /**
     * 获取令牌token 查询设备结果
     *
     * @param token
     */
    public void getRegistrationResult(String token,
                                      ResultListener<String> listener) {
        IotApiParams params = new IotApiParams(ACTIVITOR_RESULT_API.replace("{token}", token), "1.0", "GET", mCountryCode);
        asyncRequest(params, String.class, listener);
    }

    /**
     *  query device by asset_id
     *
     * @param assetId
     * @param listener
     */
    public void queryDevicesByAssetId(String assetId, ResultListener<AssetDeviceListBean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_ASSET_API.replace("{asset_id}", assetId), "1.0", "GET", mCountryCode);
        asyncRequest(params, AssetDeviceListBean.class, listener);
    }

    /**
     *  delete devices
     *
     * @param deviceId
     * @param listener
     */
    public void deleteDeviceId(String deviceId, ResultListener<Boolean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_DELETE_API.replace("{device_id}", deviceId), "1.0", "DELETE", mCountryCode);
        asyncRequest(params, Boolean.class, listener);
    }

}
