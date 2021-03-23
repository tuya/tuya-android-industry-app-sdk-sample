package com.tuya.iotapp.devices.bean;

import java.util.List;

/**
 * DeviceListBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:31 PM
 */
public class DeviceListBean {
    private List<DeviceBean> devices;
    private String last_row_key;
    private boolean has_more;
    private int total;

    public List<DeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceBean> devices) {
        this.devices = devices;
    }

    public String getLast_row_key() {
        return last_row_key;
    }

    public void setLast_row_key(String last_row_key) {
        this.last_row_key = last_row_key;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
