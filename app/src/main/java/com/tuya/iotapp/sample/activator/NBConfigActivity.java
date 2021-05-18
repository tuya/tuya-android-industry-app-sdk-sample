package com.tuya.iotapp.sample.activator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.IoTCommonUtil;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;

/**
 * @description: NBConfigActivity
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/17/21 5:24 PM
 */
public class NBConfigActivity extends AppCompatActivity {

    private String mQrCode;
    private String mAssetId;
    private Context mContext;

    private ProgressBar mProgressBar;
    private TextView mTvDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nb);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }
        mProgressBar = findViewById(R.id.progress_bar);
        mTvDevice = findViewById(R.id.tv_device_id);
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(NBConfigActivity.this);
                integrator.initiateScan();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(mContext, "scan failed", Toast.LENGTH_SHORT).show();
            } else {
                mQrCode = result.getContents();
                L.Companion.d("scan result", mQrCode);
                bindNBDevice();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void bindNBDevice() {
        mProgressBar.setVisibility(View.VISIBLE);
        TYActivatorManager.Companion.getActivator().bindNBDevice(mQrCode, mAssetId, IoTCommonUtil.Companion.getTimeZoneId(), new ResultListener<String>() {

            @Override
            public void onSuccess(String s) {
                mProgressBar.setVisibility(View.GONE);
                mTvDevice.setText("deviceId : " + s);
                Toast.makeText(mContext, "bindNBDevice success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String s, String s1) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "bindNBDevice fail：" + s1, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
