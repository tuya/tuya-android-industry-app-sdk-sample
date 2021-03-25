package com.tuya.iotapp.sample.activator.presenter;

import android.content.Context;
import android.content.Intent;

import com.tuya.iotapp.activator.config.APConfigImpl;
import com.tuya.iotapp.activator.config.EZConfigImpl;
import com.tuya.iotapp.activator.config.IQrCodeActivatorListener;
import com.tuya.iotapp.activator.config.QRCodeConfigImpl;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.bean.DeviceRegistrationResultBean;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.network.utils.TimeStampManager;
import com.tuya.iotapp.sample.env.Constant;

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

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private DeviceBusiness business;

    private IActivatorResultListener listener;

    public WifiConfigurationPresenter(Context context, Intent intent) {
        if (intent != null) {
            ssid = intent.getStringExtra(Constant.INTENT_KEY_SSID);
            password = intent.getStringExtra(Constant.INTENT_KEY_WIFI_PASSWORD);
            token = intent.getStringExtra(Constant.INTENT_KEY_TOKEN);
            activatorToken = intent.getStringExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN);
            wifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        }
        business = new DeviceBusiness(null);
        mContext = context;
    }

    public void startConfig() {
        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            APConfigImpl.startConfig(mContext, ssid, password, activatorToken);
        } else if (Constant.CONFIG_TYPE_EZ.equals(wifiType)) {
            EZConfigImpl.startConfig(ssid, password, activatorToken);
        }
        startLoop();
    }

    public void stopConfig() {
        if (Constant.CONFIG_TYPE_AP.equals(wifiType)) {
            APConfigImpl.stopConfig();
        } else if (Constant.CONFIG_TYPE_EZ.equals(wifiType)) {
            EZConfigImpl.stopConfig();
        }
        stopLoop();
    }

    public void setActivatorResultListener(IActivatorResultListener listener) {
        this.listener = listener;
    }

    public void createQrCode(IQrCodeActivatorListener listener) {
        QRCodeConfigImpl.createQrCode(mContext, ssid, password, activatorToken, listener);
    }

    public void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!loopExpire) {
            startLoopTime = TimeStampManager.instance().getCurrentTimeStamp();
            loopExpire = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                long endLoopTime = TimeStampManager.instance().getCurrentTimeStamp();
                LogUtils.d("registration result", "loop 循环调用" + token + "expireTime:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100) {
                    stopConfig();
                    loopExpire = false;
                }
                business.getRegistrationResult(token, new ResultListener<DeviceRegistrationResultBean>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, DeviceRegistrationResultBean bizResult, String apiName) {
                        LogUtils.d("registration result", "false:" + bizResponse.getCode() + "  " + bizResponse.getCode());
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, DeviceRegistrationResultBean bizResult, String apiName) {
                        LogUtils.d("registration result", "success");
                        if (bizResult != null) {
                            if ((bizResult.getSuccess_devices() != null && bizResult.getSuccess_devices().size() > 0)
                            || (bizResult.getError_devices() != null && bizResult.getError_devices().size() > 0)) {
                                listener.onActivatorSuccessDevice(bizResult.getSuccess_devices());
                                listener.onActivatorErrorDevice(bizResult.getError_devices());
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
