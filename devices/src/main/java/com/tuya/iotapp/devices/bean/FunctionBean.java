package com.tuya.iotapp.devices.bean;

import java.io.Serializable;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 3:46 PM
 */
public class FunctionBean implements Serializable {
    private String name;
    private String desc;
    private String code;
    private String type;
    private String values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
