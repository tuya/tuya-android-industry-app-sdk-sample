package com.tuya.iotapp.sample.activator.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.tuya.iotapp.activator.bean.DeviceRegistrationResultBean;
import com.tuya.iotapp.activator.builder.ActivatorBuilder;
import com.tuya.iotapp.activator.config.IActivatorConfig;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.common.utils.NetworkUtil;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.env.Constant;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * WifiConfigurationPresenter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:13 PM
 */
public class WifiConfigurationPresenter {

    private String ssid;
    private String password;
    private String token;
    private String activatorToken;
    private String wifiType;
    private Context mContext;
    private IActivatorConfig mApConfig;
    ActivatorBuilder mBuilder;

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private IActivatorResultListener listener;

    public WifiConfigurationPresenter(Context context, Intent intent) {
        if (intent != null) {
            ssid = intent.getStringExtra(Constant.INTENT_KEY_SSID);
            password = intent.getStringExtra(Constant.INTENT_KEY_WIFI_PASSWORD);
            token = intent.getStringExtra(Constant.INTENT_KEY_TOKEN);
            activatorToken = intent.getStringExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN);
            wifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        }
        mContext = context;
        mBuilder = new ActivatorBuilder(mContext,
                ssid != null ? ssid : "",
                password != null ? password : "",
                activatorToken != null ? activatorToken : "");
        mApConfig = TYActivatorManager.Companion.newAPActivator(mBuilder);
    }

    public void startConfig() {
        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            mApConfig.start();
        }
        startLoop();
    }

    public void stopConfig() {
        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            mApConfig.stop();
        }
        stopLoop();
    }

    public void setActivatorResultListener(IActivatorResultListener listener) {
        this.listener = listener;
    }

    public Bitmap createQrCode() {
        return TYActivatorManager.Companion.newQRActivator(mBuilder).createQRCode(300);
    }

    public void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!loopExpire) {
            startLoopTime = System.currentTimeMillis()/1000;
            loopExpire = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                long endLoopTime = System.currentTimeMillis() / 1000;
                L.Companion.d("registration result", "loop 循环调用" + token + "expireTime:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100) {
                    stopConfig();
                    loopExpire = false;
                }
                TYActivatorManager.Companion.getActivator().getRegistrationResultToken(token, new ResultListener<DeviceRegistrationResultBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        L.Companion.d("registration result", "false:" + s + ":" + s1);
                    }

                    @Override
                    public void onSuccess(DeviceRegistrationResultBean deviceRegistrationResultBean) {
                        L.Companion.d("registration result", "success");
                        if (deviceRegistrationResultBean != null) {
                            if ((deviceRegistrationResultBean.getSuccessDevices() != null && deviceRegistrationResultBean.getSuccessDevices().size() > 0)
                                    || (deviceRegistrationResultBean.getErrorDevices() != null && deviceRegistrationResultBean.getErrorDevices().size() > 0)) {
                                listener.onActivatorSuccessDevice(deviceRegistrationResultBean.getSuccessDevices());
                                listener.onActivatorErrorDevice(deviceRegistrationResultBean.getErrorDevices());
                                stopLoop();
                            }
                        }
                    }
                });
            }

        }, 0, 2000);
    }

    private void stopLoop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
