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
import com.thingclips.iotapp.pair.api.DiscoveryMode;
import com.thingclips.iotapp.pair.api.IBluetoothDevice;
import com.thingclips.iotapp.pair.api.IDiscovery;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.params.BLEActivatorParams;
import com.thingclips.iotapp.pair.api.params.DiscoveryParams;
import com.thingclips.iotapp.pair.delegate.BLEActivator;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.utils.ToastUtil;

public class BleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BleActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Button btnStart;
    private ProgressBar pbLoading;
    private TextView tvResult;

    private IBluetoothDevice bleBean;
    private IDiscovery iDiscovery;
    private String mAssetId;
    private BLEActivator activator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_ble_pair);

        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }

        bleBean = BleConstant.scanDeviceBean;


        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.topAppBar);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //开始配网
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
        activator = (BLEActivator) ActivatorService.activator(ActivatorMode.BLE);
        activator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                ToastUtil.show(BleActivity.this, "ble pair success");
                Log.d(TAG, "ble pair success");
                pbLoading.setVisibility(View.GONE);
                String msg  = String.format("activator success: { deviceId:%1$s, deviceName:%2$s }",
                        iDevice.getDeviceId(), iDevice.getName());
                tvResult.setText(msg);
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                String message = String.format("ble pair onError <%1$s, %2$s>", s, s1);
                ToastUtil.show(BleActivity.this, message);
                Log.d(TAG, message);
                pbLoading.setVisibility(View.GONE);
                tvResult.setText(message);
            }
        });

        BLEActivatorParams params = new BLEActivatorParams.Builder()
                .setAssetId(mAssetId)
                .setAddress(bleBean.getAddress())
                .setDeviceType(bleBean.getDeviceType())
                .setUuid(bleBean.getUUID())
                .setProductId(bleBean.getProductId())
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

    /**
     * 开始扫描
     */
    private void startDiscovery() {
        iDiscovery = ActivatorService.discovery(DiscoveryMode.BLE);
        DiscoveryParams.Builder builder = new DiscoveryParams.Builder();
        builder.setTimeout(600_000);
        DiscoveryParams discoveryParams = new DiscoveryParams(builder);
        iDiscovery.setParams(discoveryParams);
        iDiscovery.setListener(iDiscoveryDevice -> bleBean = (IBluetoothDevice) iDiscoveryDevice);
        iDiscovery.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        if (iDiscovery != null){
            iDiscovery.stopDiscovery();
        }
        super.onDestroy();
    }
}
