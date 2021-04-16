package com.tuya.dev.iotos.config.bean;

import android.view.View;

import androidx.annotation.DrawableRes;

/**
 * Config type bean
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 3:44 PM
 */
public class ConfigTypeBean {
    String name;
    @DrawableRes
    int icon;
    View.OnClickListener click;

    public ConfigTypeBean(String name, int icon, View.OnClickListener click) {
        this.name = name;
        this.icon = icon;
        this.click = click;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public View.OnClickListener getClick() {
        return click;
    }
}
