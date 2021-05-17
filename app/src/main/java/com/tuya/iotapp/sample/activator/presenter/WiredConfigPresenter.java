package com.tuya.iotapp.sample.activator.presenter;

import android.content.Context;
import android.content.Intent;

import com.tuya.iotapp.activator.bean.DeviceRegistrationResultBean;
import com.tuya.iotapp.activator.builder.ActivatorBuilder;
import com.tuya.iotapp.activator.config.IWiredActivator;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.env.Constant;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @description: WiredConfigPresenter
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/15/21 9:28 AM
 */
public class WiredConfigPresenter {

    private String region;
    private String token;
    private String secret;
    private Context mContext;
    private IWiredActivator mWiredConfig;
    ActivatorBuilder mBuilder;

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private IActivatorResultListener listener;

    public WiredConfigPresenter(Context context, Intent intent) {
        if (intent != null) {
            region = intent.getStringExtra(Constant.INTENT_KEY_REGION);
            token = intent.getStringExtra(Constant.INTENT_KEY_TOKEN);
            secret = intent.getStringExtra(Constant.INTENT_KEY_SECRET);
        }
        mContext = context;
        mBuilder = new ActivatorBuilder(mContext,
                "",
                "",
                region != null ? region : "",
                token != null ? token : "",
                secret != null ? secret : "");
    }

    public void startConfig() {
        mWiredConfig = TYActivatorManager.Companion.newWiredActivator(mBuilder);
        mWiredConfig.start();
        startLoop();
    }

    public void stopConfig() {
        mWiredConfig.stop();
        stopLoop();
    }

    public void setActivatorResultListener(IActivatorResultListener listener) {
        this.listener = listener;
    }

    public void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!loopExpire) {
            startLoopTime = System.currentTimeMillis() / 1000;
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
