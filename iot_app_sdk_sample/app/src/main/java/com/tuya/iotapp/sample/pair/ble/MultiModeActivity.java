package com.tuya.iotapp.sample.pair.ble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.pair.api.ActivatorMode;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.IBluetoothDevice;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.params.BLEWIFIActivatorParams;
import com.thingclips.iotapp.pair.delegate.BLEWIFIActivator;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.utils.ToastUtil;

public class MultiModeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MultiModeActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Button btnStart;
    private ProgressBar pbLoading;
    private TextView tvResult;

    private IBluetoothDevice bleBean;
    private String mAssetId;
    private String token;
    private String ssid;
    private String pwd;
    private BLEWIFIActivator activator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_multi_mode_pair);

        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            token = intent.getStringExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN);
            ssid = intent.getStringExtra(Constant.INTENT_KEY_SSID);
            pwd = intent.getStringExtra(Constant.INTENT_KEY_WIFI_PASSWORD);
        }
        bleBean = BleConstant.scanDeviceBean;

        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.topAppBar);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        btnStart = findViewById(R.id.btnStart);
        pbLoading = findViewById(R.id.pbLoading);
        tvResult = findViewById(R.id.tvResult);

        btnStart.setOnClickListener(this);
    }


    private void start() {
        if (null == bleBean) {
            ToastUtil.show(this, "bleBean is null");
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        activator = (BLEWIFIActivator) ActivatorService.activator(ActivatorMode.BLE_WIFI);
        activator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                ToastUtil.show(MultiModeActivity.this, "ble pair success");
                Log.d(TAG, "ble pair success");
                pbLoading.setVisibility(View.GONE);
                String msg  = String.format("activator success: { deviceId:%1$s, deviceName:%2$s }",
                        iDevice.getDeviceId(), iDevice.getName());
                tvResult.setText(msg);
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                String message = String.format("ble pair onError <%1$s, %2$s>", s, s1);
                ToastUtil.show(MultiModeActivity.this, message);
                Log.d(TAG, message);
                pbLoading.setVisibility(View.GONE);
            }
        });

        BLEWIFIActivatorParams params = new BLEWIFIActivatorParams.Builder()
                .setAssetId(mAssetId)
                .setAddress(bleBean.getAddress())
                .setDeviceType(bleBean.getDeviceType())
                .setUuid(bleBean.getUUID())
                .setMac(bleBean.getMAC())
                .setToken(token)
                .setSsid(ssid)
                .setPwd(pwd)
                .setTimeout(600_000)
                .build();
        activator.setParams(params);
        activator.start();
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.btnStart) {
            start();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != activator) {
            activator.destroy();
        }
        super.onDestroy();
    }
}
