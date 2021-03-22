package com.tuya.iotapp.sample.present;

import android.content.Context;
import android.content.Intent;

import com.tuya.iotapp.activitor.config.APConfigImpl;
import com.tuya.iotapp.activitor.config.EZConfigImpl;
import com.tuya.iotapp.activitor.config.IQrCodeActivitorListener;
import com.tuya.iotapp.activitor.config.QRCodeConfigImpl;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.network.utils.TimeStampManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String activitorToken;
    private String wifiType;
    private Context mContext;

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private DeviceBusiness business;

    IActivitorResultListener listener;

    public WifiConfigurationPresenter(Context context, Intent intent) {
        if (intent != null) {
            ssid = intent.getStringExtra("ssid");
            password = intent.getStringExtra("password");
            token = intent.getStringExtra("token");
            activitorToken = intent.getStringExtra("activitor_token");
            wifiType = intent.getStringExtra("config_type");
        }
        business = new DeviceBusiness(null);
        mContext = context;
    }

    public void startConfig() {
        if ("AP".equals(wifiType)) {
            APConfigImpl.startConfig(mContext, ssid, password, activitorToken);
        } else if ("EZ".equals(wifiType)) {
            EZConfigImpl.startConfig(ssid, password, activitorToken);
        }
        startLoop();
    }

    public void stopConfig() {
        if ("AP".equals(wifiType)) {
            APConfigImpl.stopConfig();
        } else if ("EZ".equals(wifiType)) {
            EZConfigImpl.stopConfig();
        }
        stopLoop();
    }

    public void setActivityResultListener(IActivitorResultListener listener) {
        this.listener = listener;
    }

    public void createQrCode(IQrCodeActivitorListener listener) {
        QRCodeConfigImpl.createQrCode(mContext, ssid, password, activitorToken, listener);
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
                LogUtils.d("registration result", "=====loop 循环调用==" + token + "==expireTime==:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100) {
                    stopConfig();
                    loopExpire = false;
                }
                business.getRegistrationResult(token, new ResultListener<JSONObject>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registration result", "=====false===" + bizResponse.getCode() + "  " + bizResponse.getCode());
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registration result", "======success====" + bizResult.toString());
                        JSONArray successDevices = null;
                        try {
                            successDevices = bizResult.getJSONArray("successDevices");
                            if (successDevices != null && successDevices.length() > 0) {
                                listener.onActivitySuccessDevice(successDevices.toString());
                                stopLoop();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        }, 0, 2000);
    }

    private void stopLoop() {
        timer.cancel();
        timer = null;
    }
}
