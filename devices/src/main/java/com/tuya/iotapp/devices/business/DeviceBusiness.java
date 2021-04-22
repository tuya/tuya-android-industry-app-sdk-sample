package com.tuya.iotapp.devices.business;

import com.tuya.iotapp.common.utils.IotCommonUtil;
import com.tuya.iotapp.devices.bean.AssetDeviceListBean;
import com.tuya.iotapp.devices.bean.AuthKeyBean;
import com.tuya.iotapp.devices.bean.DeviceBean;
import com.tuya.iotapp.devices.bean.DeviceRegistrationResultBean;
import com.tuya.iotapp.devices.bean.DeviceStatusBean;
import com.tuya.iotapp.devices.bean.RegistrationTokenBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IRequest;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

import java.util.List;

/**
 * DeviceBusiness
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:44 PM
 */
public class DeviceBusiness extends Business {
    private static final String ACTIVATOR_TOKEN_API = "/v1.0/iot-03/device-registration/token";

    private static final String ACTIVATOR_RESULT_API = "/v1.0/iot-03/device-registration/tokens/{token}";

    private static final String ACTIVATOR_BLE_AUHTKEY_API = "/v1.0/iot-03/device-registration/single-bluetooth/auth-key";

    private static final String DEVICE_RESULT_API = "/v1.0/iot-03/devices";

    private static final String DEVICE_ASSET_API = "/v1.0/iot-02/assets/{asset_id}/devices";

    private static final String DEVICE_API = "/v1.0/iot-03/devices/{device_id}";

    private static final String DEVICE_STATUS_API = "/v1.0/iot-03/devices/{deviceId}/status";

    private String mCountryCode;

    public DeviceBusiness(String countryCode) {
        mCountryCode = countryCode;
    }

    /**
     * activator registration token
     *
     * @param assetId
     * @param uid
     * @param uid
     * @param extension
     */
    public void getDeviceRegistrationToken(String assetId,
                                           String uid,
                                           String type,
                                           Object extension,
                                           ResultListener<RegistrationTokenBean> listener) {
        IotApiParams params = new IotApiParams(ACTIVATOR_TOKEN_API, "1.0", "POST", mCountryCode);
        params.putPostData("pairing_type", type);
        params.putPostData("time_zone_id", IotCommonUtil.getTimeZoneId());
        params.putPostData("asset_id", assetId);
        params.putPostData("uid", uid);
        params.putPostData("extension", extension);

        asyncRequest(params, RegistrationTokenBean.class, listener);

    }

    /**
     * get token query result devices
     *
     * @param token
     */
    public void getRegistrationResult(String token,
                                      ResultListener<DeviceRegistrationResultBean> listener) {
        IotApiParams params = new IotApiParams(ACTIVATOR_RESULT_API.replace("{token}", token), "1.0", IRequest.GET, mCountryCode);
        asyncRequest(params, DeviceRegistrationResultBean.class, listener);
    }

    public void getAuthKeyByUuid(String assetId, String uuid, ResultListener<AuthKeyBean> listener) {
        IotApiParams params =new IotApiParams(ACTIVATOR_BLE_AUHTKEY_API, "1.0", IRequest.POST, mCountryCode);
        params.putPostData("uuid", uuid);
        params.putPostData("asset_id", assetId);
        asyncRequest(params, AuthKeyBean.class, listener);
    }

    /**
     *  query device by asset_id
     *
     * @param assetId
     * @param listener
     */
    public void queryDevicesByAssetId(String assetId, ResultListener<AssetDeviceListBean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_ASSET_API.replace("{asset_id}", assetId), "1.0", IRequest.GET, mCountryCode);
        asyncRequest(params, AssetDeviceListBean.class, listener);
    }

    /**
     *  delete devices
     *
     * @param deviceId
     * @param listener
     */
    public void deleteDeviceId(String deviceId, ResultListener<Boolean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_API.replace("{device_id}", deviceId), "1.0", IRequest.DELETE, mCountryCode);
        asyncRequest(params, Boolean.class, listener);
    }

    /**
     * get deviceInfo
     * @param deviceId
     * @param listener
     */
    public void getDeviceInfo(String deviceId, ResultListener<DeviceBean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_API.replace("{device_id}", deviceId), "1.0", IRequest.GET, mCountryCode);
        asyncRequest(params, DeviceBean.class, listener);
    }

    /**
     * get Device Status
     * @param deviceId
     * @param listener
     */
    public void getDeviceStatus(String deviceId, ResultListener<List<DeviceStatusBean>> listener) {
        IotApiParams params = new IotApiParams(DEVICE_STATUS_API.replace("{device_id}", deviceId), "1.0", IRequest.GET, mCountryCode);
        asyncRequestList(params, DeviceStatusBean.class, listener);
    }

}
