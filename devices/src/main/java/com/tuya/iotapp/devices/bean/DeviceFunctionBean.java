package com.tuya.iotapp.devices.bean;

import java.io.Serializable;
import java.util.List;

/**
 * DeviceFunctionBean
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 3:44 PM
 */
public class DeviceFunctionBean implements Serializable {
    private String category;
    private List<FunctionBean> functions;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<FunctionBean> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionBean> functions) {
        this.functions = functions;
    }
}
