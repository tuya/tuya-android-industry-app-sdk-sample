package com.tuya.iotapp.devices.bean;

/**
 * ErrorDeviceBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 4:51 PM
 */
public class ErrorDeviceBean {
    private String device_id;
    private String code;
    private String msg;
    private String name;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
