package com.tuya.iotapp.sample.pair.ble;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.DiscoveryMode;
import com.thingclips.iotapp.pair.api.IBluetoothDevice;
import com.thingclips.iotapp.pair.api.IDiscovery;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.pair.input.WifiPairInputActivity;
import com.tuya.iotapp.sample.utils.GsonUtil;
import com.tuya.iotapp.sample.utils.ToastUtil;

public class BleScanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = BleScanActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CODE = 0xA01;

    private Toolbar mToolbar;
    private Button btnScan;
    private Button btnNext;
    private ProgressBar pbLoading;
    private TextView tvScanResult;

    private String mAssetId;
    private IBluetoothDevice bleBean;
    private IDiscovery discovery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_ble_scan);

        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }

        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.topAppBar);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        btnScan = findViewById(R.id.btnScan);
        btnNext = findViewById(R.id.btnNext);
        pbLoading = findViewById(R.id.pbLoading);
        tvScanResult = findViewById(R.id.tvScanResult);

        btnScan.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void scan() {
        if (!permissionCheck()) {
            return;
        }

        pbLoading.setVisibility(View.VISIBLE);
        discovery = ActivatorService.discovery(DiscoveryMode.BLE);
        discovery.setListener(iDiscoveryDevice -> {
            bleBean = (IBluetoothDevice) iDiscoveryDevice;
            pbLoading.setVisibility(View.GONE);
            Log.d(TAG, String.format("scan success: {\"uuid\": \"%1$s\"}", bleBean.getUUID()));
            tvScanResult.setText("Scan Result: " + GsonUtil.toJson(bleBean));
            discovery.stopDiscovery();
        });
        discovery.startDiscovery();
    }

    private void doNext() {
        if (null == bleBean) {
            final String message = "Scan result is null, you should call scan method first!";
            ToastUtil.show(BleScanActivity.this,
                    message);
            Log.d(TAG, message);
            return;
        }
        BleConstant.scanDeviceBean = bleBean;
        boolean isMultiMode = false;
        switch (bleBean.getConfigType()) {
            case "config_type_single":
                isMultiMode = false;
                break;
            case "config_type_wifi":
                isMultiMode = true;
                break;
            case "config_type_beacon":
                break;
        }

        if (isMultiMode) {
            Intent intent = new Intent(this, WifiPairInputActivity.class);
            intent.putExtra(Constant.INTENT_KEY_SIMPLE_INPUT, true);
            intent.putExtra(Constant.INTENT_KEY_ASSET_ID, mAssetId);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, BleActivity.class);
            intent.putExtra(Constant.INTENT_KEY_ASSET_ID, mAssetId);
            startActivity(intent);
        }
    }

    private boolean permissionCheck() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
            return true;
        } else {
            // 未授权，需要请求用户授权
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.btnScan) {
            scan();
        } else if (viewId == R.id.btnNext) {
            doNext();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MultiModeActivity.class);
            intent.putExtra(Constant.INTENT_KEY_ASSET_ID, mAssetId);
            intent.putExtras(data);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scan();
            } else {
                // permission denied
            }
        }
    }
}
