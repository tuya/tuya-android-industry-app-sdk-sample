package com.tuya.dev.devices.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * SuccessDeviceBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 4:51 PM
 */
public class SuccessDeviceBean implements Parcelable {
    private String id;
    private String product_id;
    private String name;
    private String category;
    private String lon;
    private String lat;
    private String ip;
    private String online;
    private String uuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.product_id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.lon);
        dest.writeString(this.lat);
        dest.writeString(this.ip);
        dest.writeString(this.online);
        dest.writeString(this.uuid);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.product_id = source.readString();
        this.name = source.readString();
        this.category = source.readString();
        this.lon = source.readString();
        this.lat = source.readString();
        this.ip = source.readString();
        this.online = source.readString();
        this.uuid = source.readString();
    }

    public SuccessDeviceBean() {
    }

    protected SuccessDeviceBean(Parcel in) {
        this.id = in.readString();
        this.product_id = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.lon = in.readString();
        this.lat = in.readString();
        this.ip = in.readString();
        this.online = in.readString();
        this.uuid = in.readString();
    }

    public static final Parcelable.Creator<SuccessDeviceBean> CREATOR = new Parcelable.Creator<SuccessDeviceBean>() {
        @Override
        public SuccessDeviceBean createFromParcel(Parcel source) {
            return new SuccessDeviceBean(source);
        }

        @Override
        public SuccessDeviceBean[] newArray(int size) {
            return new SuccessDeviceBean[size];
        }
    };
}
