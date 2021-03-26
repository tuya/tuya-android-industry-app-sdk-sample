package com.tuya.iotapp.devices.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * DeviceRegistrationResultBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 4:50 PM
 */
public class DeviceRegistrationResultBean {

    private List<SuccessDeviceBean> success_devices;
    private List<ErrorDeviceBean> error_devices;

    public List<SuccessDeviceBean> getSuccess_devices() {
        return success_devices;
    }

    public void setSuccess_devices(List<SuccessDeviceBean> success_devices) {
        this.success_devices = success_devices;
    }

    public List<ErrorDeviceBean> getError_devices() {
        return error_devices;
    }

    public void setError_devices(List<ErrorDeviceBean> error_devices) {
        this.error_devices = error_devices;
    }
}
