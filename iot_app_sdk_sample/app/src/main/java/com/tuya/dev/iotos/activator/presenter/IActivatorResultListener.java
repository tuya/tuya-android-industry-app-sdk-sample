package com.tuya.dev.iotos.activator.presenter;


import com.tuya.iotapp.activator.bean.ErrorDeviceBean;
import com.tuya.iotapp.activator.bean.SuccessDeviceBean;

import java.util.List;

/**
 * IActivatorResultListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 6:13 PM
 */
public interface IActivatorResultListener {
    void onActivatorResultDevice(List<SuccessDeviceBean> successDevices,
                                 List<ErrorDeviceBean> errorDevices);
}
