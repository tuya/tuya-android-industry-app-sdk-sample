package com.tuya.iotapp.devices.bean;

import java.io.Serializable;
import java.util.List;

/**
 * DeviceSpecificationBean
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 3:54 PM
 */
public class DeviceSpecificationBean implements Serializable {
    private String category;
    private List<SpecificationBean> functions;
    private List<SpecificationBean> status;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SpecificationBean> getFunctions() {
        return functions;
    }

    public void setFunctions(List<SpecificationBean> functions) {
        this.functions = functions;
    }

    public List<SpecificationBean> getStatus() {
        return status;
    }

    public void setStatus(List<SpecificationBean> status) {
        this.status = status;
    }
}
