package com.tuya.dev.devices.bean;

import java.util.List;

/**
 * AssetDeviceListBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:42 PM
 */
public class AssetDeviceListBean {
    private List<AssetDeviceBean> list;
    private String last_row_key;
    private String page_size;
    private boolean has_next;

    public List<AssetDeviceBean> getList() {
        return list;
    }

    public void setList(List<AssetDeviceBean> list) {
        this.list = list;
    }

    public String getLast_row_key() {
        return last_row_key;
    }

    public void setLast_row_key(String last_row_key) {
        this.last_row_key = last_row_key;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public boolean isHas_next() {
        return has_next;
    }

    public void setHas_next(boolean has_next) {
        this.has_next = has_next;
    }
}
