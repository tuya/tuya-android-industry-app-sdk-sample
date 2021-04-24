package com.tuya.dev.devices.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ErrorDeviceBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 4:51 PM
 */
public class ErrorDeviceBean implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.device_id);
        dest.writeString(this.code);
        dest.writeString(this.msg);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.device_id = source.readString();
        this.code = source.readString();
        this.msg = source.readString();
        this.name = source.readString();
    }

    public ErrorDeviceBean() {
    }

    protected ErrorDeviceBean(Parcel in) {
        this.device_id = in.readString();
        this.code = in.readString();
        this.msg = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ErrorDeviceBean> CREATOR = new Parcelable.Creator<ErrorDeviceBean>() {
        @Override
        public ErrorDeviceBean createFromParcel(Parcel source) {
            return new ErrorDeviceBean(source);
        }

        @Override
        public ErrorDeviceBean[] newArray(int size) {
            return new ErrorDeviceBean[size];
        }
    };
}
