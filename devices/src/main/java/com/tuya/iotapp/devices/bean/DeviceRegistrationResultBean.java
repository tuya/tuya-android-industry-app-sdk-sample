package com.tuya.iotapp.devices.bean;

import java.util.ArrayList;

/**
 * DeviceRegistrationResultBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 4:50 PM
 */
public class DeviceRegistrationResultBean {

    private ArrayList<SuccessDeviceBean> successLists;
    private ArrayList<ErrorDeviceBean> errorList;

    public ArrayList<SuccessDeviceBean> getSuccessLists() {
        return successLists;
    }

    public void setSuccessLists(ArrayList<SuccessDeviceBean> successLists) {
        this.successLists = successLists;
    }

    public ArrayList<ErrorDeviceBean> getErrorList() {
        return errorList;
    }

    public void setErrorList(ArrayList<ErrorDeviceBean> errorList) {
        this.errorList = errorList;
    }
}
