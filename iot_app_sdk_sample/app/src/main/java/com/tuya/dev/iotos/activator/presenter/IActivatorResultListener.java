package com.tuya.dev.iotos.activator.presenter;

import com.tuya.dev.devices.bean.ErrorDeviceBean;
import com.tuya.dev.devices.bean.SuccessDeviceBean;

import java.util.List;

/**
 * IActivatorResultListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 6:13 PM
 */
public interface IActivatorResultListener {
    void onActivatorSuccessDevice(List<SuccessDeviceBean> successDevices);

    void onActivatorErrorDevice(List<ErrorDeviceBean> errorDevices);
}
