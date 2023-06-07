package com.tuya.iotapp.sample.pair.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.king.zxing.CameraScan;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.pair.api.ActivatorMode;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.params.QRScanActivatorParams;
import com.thingclips.iotapp.pair.delegate.QRScanActivator;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.king.zxing.CaptureActivity;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SCAN = 0xF01;
    private Toolbar mToolbar;
    private Button btnScan;
    private ProgressBar pbLoading;
    private TextView tvScanResult;

    private String mAssetId;
    private QRScanActivator activator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_scan_pair);

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
        pbLoading = findViewById(R.id.pbLoading);
        tvScanResult = findViewById(R.id.tvResult);
        btnScan.setOnClickListener(this);
    }

    private void scanPair(String code) {
        showLoading();
        activator = (QRScanActivator) ActivatorService.activator(ActivatorMode.QRScan);
        activator.setParams(new QRScanActivatorParams.Builder()
                .setAssetId(mAssetId)
                .setCode(code)
                .setTimeout(600_000)
                .build());
        activator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                String result = String.format("Pair Success: {deviceId: %1$s, name: %3$s}",
                        iDevice.getDeviceId(), iDevice.getName());
                tvScanResult.setText(result);
                hideLoading();

            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                tvScanResult.setText(String.format("Pair Failed: code=%1$s, msg=%2$s", s, s1));
                hideLoading();

            }
        });
        activator.start();
    }

    private void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.btnScan) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_SCAN);
                return;
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SCAN);
                return;
            }
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    String result = CameraScan.parseScanResult(data);
                    scanPair(result);
                    break;
            }

        }


//        if (requestCode == REQUEST_CODE_SCAN) {
//
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(this, "result:" + result, Toast.LENGTH_LONG).show();
//                    scanPair(result);
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(this, "Failed to parse QR code", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        if (null != activator) activator.destroy();
        super.onDestroy();
    }
}
