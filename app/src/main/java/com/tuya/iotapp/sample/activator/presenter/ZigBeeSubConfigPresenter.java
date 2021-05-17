package com.tuya.iotapp.sample.activator.presenter;

import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.device.bean.SubDeviceBean;
import com.tuya.iotapp.network.response.ResultListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description: ZigBeeSubConfigPresenter
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/17/21 9:28 AM
 */
public class ZigBeeSubConfigPresenter {

    private String mDeviceId;
    private Long mDiscoveryTime;

    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    private IActivatorZBSubListener listener;

    public ZigBeeSubConfigPresenter(String deviceId, Long discoveryTime) {
        mDeviceId = deviceId;
        mDiscoveryTime = discoveryTime;
    }

    public void setActivatorZBSubListener(IActivatorZBSubListener listener) {
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
                L.Companion.d("registration result sub dev", "loop 循环调用" + mDeviceId + "expireTime:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100) {
                    stopLoop();
                    loopExpire = false;
                }

                TYActivatorManager.Companion.getActivator().getRegistrationSubDevices(mDeviceId, mDiscoveryTime, new ResultListener<List<SubDeviceBean>>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        L.Companion.d("registration result sub dev", "false:" + s + ":" + s1);
                    }

                    @Override
                    public void onSuccess(List<SubDeviceBean> subDeviceList) {
                        L.Companion.d("registration result sub dev", "success");
                        if (listener != null && subDeviceList != null && !subDeviceList.isEmpty()) {
                            listener.onActivatorSuccessDevice(subDeviceList);
                            stopLoop();
                        }

                    }
                });
            }

        }, 0, 2000);
    }

    public void stopLoop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
