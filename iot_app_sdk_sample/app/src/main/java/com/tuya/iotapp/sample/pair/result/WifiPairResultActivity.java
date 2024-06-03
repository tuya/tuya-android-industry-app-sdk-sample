package com.tuya.iotapp.sample.pair.result;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.device.api.IDevice;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;

import java.util.List;

/**
 * MultiWifiConfigActivity
 * Real activate wifi device page
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class WifiPairResultActivity extends AppCompatActivity implements IActivatorResultListener {

    private WifiPairResultPresenter multiPresenter;

    private ProgressBar pbLoading;
    private TextView tvDeviceSuccess;
    private TextView tvDeviceFail;
    private RecyclerView rvSuccessResult;
    private RecyclerView rvFailResult;
    private ImageView ivQR;

    private ActivatorSuccessDeviceAdapter successDeviceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_wifi_pair_result);
        initView();

        multiPresenter = new WifiPairResultPresenter(this, getIntent());
        multiPresenter.setActivatorResultListener(this);
        multiPresenter.startConfig();

        RecyclerView.LayoutManager successLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSuccessResult.setLayoutManager(successLayoutManager);
        RecyclerView.LayoutManager errorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFailResult.setLayoutManager(errorLayoutManager);

        successDeviceAdapter = new ActivatorSuccessDeviceAdapter(this);

        rvSuccessResult.setAdapter(successDeviceAdapter);
    }

    private void initView() {
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        tvDeviceSuccess = (TextView) findViewById(R.id.tvDeviceSuccess);
        tvDeviceFail = (TextView) findViewById(R.id.tvDeviceFail);
        pbLoading.setVisibility(View.VISIBLE);

        rvSuccessResult = (RecyclerView) findViewById(R.id.rvSuccessResult);
        rvFailResult = (RecyclerView) findViewById(R.id.rvFailResult);
        ivQR = findViewById(R.id.ivQR);

        String wifiType = getIntent().getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        if (PairType.QR.equals(wifiType)){
            ivQR.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (multiPresenter != null) {
            multiPresenter.stopConfig();
            multiPresenter = null;
        }
    }

    public void onActivatorSuccessDevice(List<IDevice> successDevices) {
        runOnUiThread(() -> {
            if (successDevices != null && successDevices.size() > 0
                    && successDeviceAdapter != null) {
                pbLoading.setVisibility(View.GONE);
                tvDeviceSuccess.setVisibility(View.VISIBLE);
                rvSuccessResult.setVisibility(View.VISIBLE);
                successDeviceAdapter.setData(successDevices);
            }
        });
    }

    @Override
    public void onQRCodeSuccess(Bitmap qrBitmap) {
        runOnUiThread(() -> {
            ivQR.setImageBitmap(qrBitmap);
        });
    }
}
