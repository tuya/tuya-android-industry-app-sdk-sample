package com.tuya.iotapp.devices.bean;

import java.io.Serializable;

/**
 * CommandBean
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 4:02 PM
 */
public class CommandBean implements Serializable {
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
