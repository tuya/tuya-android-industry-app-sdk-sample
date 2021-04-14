package com.tuya.iotapp.devices.business;

import com.tuya.iotapp.devices.bean.CommandBean;
import com.tuya.iotapp.devices.bean.DeviceFunctionBean;
import com.tuya.iotapp.devices.bean.DeviceSpecificationBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IRequest;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

import java.util.List;

/**
 * ControlerBusinsess
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 3:31 PM
 */
public class ControlerBusinsess extends Business {
    private static final String CONTROL_CATEGORY_FUNCTIONS_API = "/v1.0/iot-03/categories/{category}/functions";
    private static final String DEVICE_FUNCTIONS_API = "/v1.0/iot-03/devices/{device_id}/functions";
    private static final String DEVICE_SPECIFICATION_API = "/v1.0/iot-03/devices/{device_id}/specification";
    private static final String DEVICE_COMMANDS_API = "/v1.0/iot-03/devices/{device_id}/commands";

    /**
     * get functions by category
     * @param category
     * @param listener
     */
    public void getFunctionsByCategory(String category, ResultListener<DeviceFunctionBean> listener) {
        IotApiParams params = new IotApiParams(CONTROL_CATEGORY_FUNCTIONS_API.replace(
                "{category}", category), "1.0", IRequest.GET, null);

        asyncRequest(params, DeviceFunctionBean.class, listener);
    }

    /**
     * getFunctionByDeviceId
     * @param deviceId
     * @param listener
     */
    public void getFunctionByDeviceId(String deviceId,ResultListener<DeviceFunctionBean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_FUNCTIONS_API.replace(
                "{device_id}", deviceId), "1.0", IRequest.GET, null);

        asyncRequest(params, DeviceFunctionBean.class, listener);
    }

    /**
     * getSpecification
     * @param deviceId
     * @param listener
     */
    public void getSpecification(String deviceId, ResultListener<DeviceSpecificationBean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_SPECIFICATION_API.replace(
                "{device_id}", deviceId), "1.0", IRequest.GET, null);

        asyncRequest(params, DeviceSpecificationBean.class, listener);
    }

    /**
     * sendCommands
     * @param deviceId
     * @param commands
     * @param listener
     */
    public void sendCommands(String deviceId, List<CommandBean> commands, ResultListener<Boolean> listener) {
        IotApiParams params = new IotApiParams(DEVICE_COMMANDS_API.replace(
                "{device_id}", deviceId), "1.0", IRequest.GET, null);
        params.putPostData("commands", commands);
        asyncRequest(params, Boolean.class, listener);
    }

}
