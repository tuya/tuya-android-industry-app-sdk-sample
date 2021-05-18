package com.tuya.dev.iotos.activator.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.activator.ez.EZConfigImpl;
import com.tuya.dev.iotos.env.Constant;
import com.tuya.dev.iotos.log.LogUtils;
import com.tuya.iotapp.activator.bean.DeviceRegistrationResultBean;
import com.tuya.iotapp.activator.builder.ActivatorBuilder;
import com.tuya.iotapp.activator.config.IAPActivator;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.network.response.ResultListener;

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
    private String region;
    private String secret;
    private String wifiType;
    private Context mContext;

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private IAPActivator iapActivator;


    private IActivatorResultListener listener;

    public WifiConfigurationPresenter(Context context, Intent intent) {
        if (intent != null) {
            ssid = intent.getStringExtra(Constant.INTENT_KEY_SSID);
            password = intent.getStringExtra(Constant.INTENT_KEY_WIFI_PASSWORD);
            token = intent.getStringExtra(Constant.INTENT_KEY_TOKEN);
            region = intent.getStringExtra(Constant.INTENT_KEY_REGION);
            secret = intent.getStringExtra(Constant.INTENT_KEY_SECRET);
            wifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        }
        mContext = context;
    }

    public void startConfig() {


        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            iapActivator = TYActivatorManager.Companion.newAPActivator(new ActivatorBuilder(mContext,
                    ssid,
                    password,
                    region,
                    token,
                    secret));
            iapActivator.start();
        } else if (Constant.CONFIG_TYPE_EZ.equals(wifiType)) {
            EZConfigImpl.startConfig(ssid, password, region + token + secret);
        }
        startLoop();
    }

    public void stopConfig() {
        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            iapActivator.stop();
        } else if (Constant.CONFIG_TYPE_EZ.equals(wifiType)) {
            EZConfigImpl.stopConfig();
        }
        stopLoop();
    }

    public void setActivatorResultListener(IActivatorResultListener listener) {
        this.listener = listener;
    }

    public void createQrCode(IQrCodeActivatorListener listener) {
        listener.onQrCodeSuccess(
                TYActivatorManager.Companion.newQRCodeActivator(new ActivatorBuilder(mContext,
                        ssid,
                        password,
                        region,
                        token,
                        secret)).generateQRCodeImage(300)
        );
    }

    public void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!loopExpire) {
            startLoopTime = System.currentTimeMillis();
            loopExpire = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                long endLoopTime = System.currentTimeMillis();
                LogUtils.d("registration result", "loop " + token + "expireTime:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100 * 1000) {
                    // config 100s
                    stopConfig();
                    loopExpire = false;

                    ((Activity) mContext).finish();
                    Toast.makeText(mContext,
                            mContext.getString(R.string.activator_config_over_time),
                            Toast.LENGTH_SHORT)
                            .show();
                }

                TYActivatorManager.Companion.getActivator().getRegistrationResultToken(token,
                        new ResultListener<DeviceRegistrationResultBean>() {
                            @Override
                            public void onFailure(String s, String s1) {
                                LogUtils.d("registration result", "false:" + s + "  " + s1);
                            }

                            @Override
                            public void onSuccess(DeviceRegistrationResultBean deviceRegistrationResultBean) {
                                LogUtils.d("registration result", "success");
                                if ((deviceRegistrationResultBean.getSuccessDevices() != null && deviceRegistrationResultBean.getSuccessDevices().size() > 0)
                                        || (deviceRegistrationResultBean.getErrorDevices() != null && deviceRegistrationResultBean.getErrorDevices().size() > 0)) {
                                    listener.onActivatorResultDevice(deviceRegistrationResultBean.getSuccessDevices(),
                                            deviceRegistrationResultBean.getErrorDevices());
                                    stopLoop();
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
