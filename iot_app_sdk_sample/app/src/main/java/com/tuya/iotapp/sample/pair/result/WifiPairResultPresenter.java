package com.tuya.iotapp.sample.pair.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.pair.api.ActivatorMode;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.IActivator;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.listener.IQRActivatorListener;
import com.thingclips.iotapp.pair.api.params.QRActivatorParams;
import com.thingclips.iotapp.pair.api.params.WiFiActivatorParams;
import com.thingclips.iotapp.pair.api.params.WiredActivatorParams;
import com.thingclips.iotapp.pair.api.params.ZigbeeActivatorParams;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;
import com.tuya.iotapp.sample.pair.QrCodeUtil;
import com.tuya.iotapp.sample.pair.wired.WireGwDeviceManager;

import java.util.ArrayList;

/**
 * WifiConfigurationPresenter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:13 PM
 */
public class WifiPairResultPresenter {
    private static final String TAG = WifiPairResultPresenter.class.getSimpleName();

    private String ssid;
    private String password;
    private String activatorToken;
    private String wifiType;

    private IActivatorResultListener listener;
    private IActivator activator;

    public WifiPairResultPresenter(Context context, Intent intent) {
        if (intent != null) {
            ssid = intent.getStringExtra(Constant.INTENT_KEY_SSID);
            password = intent.getStringExtra(Constant.INTENT_KEY_WIFI_PASSWORD);
            activatorToken = intent.getStringExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN);
            wifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);

            if (PairType.QR.equals(wifiType)) {
                activator = ActivatorService.activator(ActivatorMode.QR);
                activator.setParams(new QRActivatorParams.Builder()
                        .setWifi(ssid, password)
                        .setToken(activatorToken)
                        .setTimeout(600_000)
                        .build());

                activator.setListener(new IQRActivatorListener() {
                    @Override
                    public void onQRCodeSuccess(@NonNull String s) {
                        try {
                            Bitmap qrBitmap = QrCodeUtil.createQRBitMap(s, 900);
                            listener.onQRCodeSuccess(qrBitmap);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onSuccess(@Nullable IDevice iDevice) {
                        listener.onActivatorSuccessDevice(new ArrayList<IDevice>() {{
                            add(iDevice);
                        }});
                        stopConfig();
                    }

                    @Override
                    public void onError(@NonNull String s, @NonNull String s1) {
                        Log.e("WifiConfiguration", s);
                    }
                });

                return;
            }


            if (PairType.AP.equals(wifiType)) {
                activator = ActivatorService.activator(ActivatorMode.AP);
                activator.setParams(new WiFiActivatorParams.Builder()
                        .setWifi(ssid, password)
                        .setToken(activatorToken)
                        .setTimeout(600_000)
                        .build());

            } else if (PairType.EZ.equals(wifiType)) {
                activator = ActivatorService.activator(ActivatorMode.EZ);
                activator.setParams(new WiFiActivatorParams.Builder()
                        .setWifi(ssid, password)
                        .setToken(activatorToken)
                        .setTimeout(600_000)
                        .build());
            } else if (PairType.WIRED.equals(wifiType)) {
                activator = ActivatorService.activator(ActivatorMode.Wired);
                activator.setParams(new WiredActivatorParams.Builder()
                        .setToken(activatorToken)
                        .setTimeout(600_000)
                        .setGWDevice(WireGwDeviceManager.INSTANCE.getIWiredDevice())
                        .build());
            } else if (PairType.SUB_DEVICE.equals(wifiType)) {
                activator = ActivatorService.activator(ActivatorMode.Zigbee);
                activator.setParams(new ZigbeeActivatorParams.Builder()
                        .setTimeout(600_000)
                        .setGwDeviceId(intent.getStringExtra(Constant.INTENT_KEY_DEVICE_ID))
                        .build());
            }

            activator.setListener(new IActivatorListener() {
                @Override
                public void onSuccess(@Nullable IDevice iDevice) {
                    listener.onActivatorSuccessDevice(new ArrayList<IDevice>() {{
                        add(iDevice);
                    }});
                    stopConfig();
                }

                @Override
                public void onError(@NonNull String s, @NonNull String s1) {
                    Log.e("WifiConfiguration", s1);
                }
            });
        }

    }


    /**
     * startConfig
     * <p>
     * activator start device pairing
     */
    public void startConfig() {
        if (activator != null) {
            Log.i("Wifi P", "activation start");
            activator.start();
        } else {
            Log.e("Wifi P", "activator null");
        }
    }

    /**
     * stopConfig
     * <p>
     * activator stop device pairing
     */
    public void stopConfig() {
        if (activator != null) {
            Log.i("Wifi P", "activation start");
            activator.stop();
        } else {
            Log.e("Wifi P", "activator null");
        }
    }

    public void setActivatorResultListener(IActivatorResultListener listener) {
        this.listener = listener;
    }
}
