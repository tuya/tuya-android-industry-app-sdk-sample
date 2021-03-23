package com.tuya.iotapp.sample.present;

import com.tuya.iotapp.devices.bean.ErrorDeviceBean;
import com.tuya.iotapp.devices.bean.SuccessDeviceBean;

import java.util.List;

/**
 * IActivitorResultListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 6:13 PM
 */
public interface IActivitorResultListener {
    void onActivitySuccessDevice(List<SuccessDeviceBean> successDevices);

    void onActivityErrorDevice(List<ErrorDeviceBean> errorDevices);
}
