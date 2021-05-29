package com.tuya.iotapp.sample.activator.presenter;

import com.tuya.iotapp.device.bean.SubDeviceBean;

import java.util.List;

/**
 * IActivatorResultListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 6:13 PM
 */
public interface IActivatorZBSubListener {
    void onActivatorSuccessDevice(List<SubDeviceBean> subDeviceList);
}
