package com.tuya.iotapp.devices.bean;

/**
 * DeviceStatusBean
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 3:26 PM
 */
public class DeviceStatusBean {
    private String code;
    private Object value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
