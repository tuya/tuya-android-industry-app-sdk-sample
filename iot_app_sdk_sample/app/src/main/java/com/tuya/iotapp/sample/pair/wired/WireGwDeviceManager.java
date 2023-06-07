package com.tuya.iotapp.sample.pair.wired;

import com.thingclips.iotapp.pair.api.IWiredDevice;

public enum WireGwDeviceManager {
    INSTANCE;
    private final static String GW_DEVICE = "gwDevice";
    private IWiredDevice iWiredDevice;

    public void saveIWiredDevice(IWiredDevice device) {
        this.iWiredDevice = device;
    }

    public IWiredDevice getIWiredDevice() {
        return iWiredDevice;
    }
}
