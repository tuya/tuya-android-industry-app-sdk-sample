package com.tuya.iotapp.activator.bean;

import com.tuya.smart.sdk.config.ble.api.bean.BLEScanBean;

import java.io.Serializable;

/**
 * BLEScanObject
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/13 5:07 PM
 */
public class BLEScanObject implements Serializable {
    public static final int TYPE_SINGLE_BLE = 100;
    public static final int TYPE_WIFI = 200;
    public static final int TYPE_ZIGBEE = 300;
    public byte[] scanRecord;
    public String address;
    public String deviceName;
    public int rssi;
    public int category;
    public int deviceType;
    public int protocolVersion;
    public boolean isBind = false;
    public String productId;
    public String devUuId;
    public String mac;
    public boolean support5G = false;
    public boolean isProductKey = false;
    public byte[] productIdRaw;

    public BLEScanObject(BLEScanBean bean) {
        scanRecord = bean.scanRecord;
        address = bean.address;
        deviceName = bean.deviceName;
        rssi = bean.rssi;
        category = bean.category;
        deviceType = bean.deviceType;
        protocolVersion = bean.protocolVersion;
        isBind = bean.isBind;
        productId = bean.productId;
        devUuId = bean.devUuId;
        mac = bean.mac;
        support5G = bean.support5G;
        isProductKey = bean.isProductKey;
        productIdRaw = bean.productIdRaw;
    }

    public BLEScanBean castToBLEScanBean() {
        BLEScanBean bean = new BLEScanBean(deviceName, address, rssi, scanRecord);
        bean.setBLEDevBeanInfo(isBind, protocolVersion, productId, productIdRaw, devUuId, mac, support5G, isProductKey, category, deviceType);
        return bean;
    }
}
