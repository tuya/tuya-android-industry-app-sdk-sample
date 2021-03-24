package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.accessToken.AccessTokenManager;
import com.tuya.iotapp.sample.activator.WifiConfigurationActivity;
import com.tuya.iotapp.sample.assets.AssetsActivity;
import com.tuya.iotapp.sample.assets.AssetsManager;
import com.tuya.iotapp.sample.devices.DevicesInAssetActivity;

public class MainManagerActivity extends AppCompatActivity {
    private TextView mTvUserName;
    private TextView mTVCurrentAsset;
    private Button mBtnAssets;
    private Button mBtnAp;
    private Button mBtnEz;
    private Button mBtnQR;
    private Button mBtnDevices;

    private Context mContext;
    private String mCountryCode;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        initView(this);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mCountryCode = intent.getStringExtra("country_code");
            mUserName = intent.getStringExtra("user_name");
        }

        if (!TextUtils.isEmpty(mUserName)) {
            mTvUserName.setText("UserName : " + mUserName);
        }

        mBtnAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig("AP");
            }
        });

        mBtnEz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig("EZ");
            }
        });

        mBtnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig("QR");
            }
        });

        mBtnDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeviceList();
            }
        });
    }

    private void initView(Context context) {
        mTvUserName = (TextView) findViewById(R.id.tv_userName);
        mTVCurrentAsset = (TextView) findViewById(R.id.tv_current_asset);
        mBtnAssets = (Button) findViewById(R.id.btn_assets);
        mBtnAp = (Button) findViewById(R.id.btn_ap);
        mBtnEz = (Button) findViewById(R.id.btn_ez);
        mBtnQR = (Button) findViewById(R.id.btn_qr);
        mBtnDevices = (Button) findViewById(R.id.btn_device_list);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        mBtnAssets.setOnClickListener(v -> {
            AssetsActivity.launch(v.getContext(),
                    "",
                    getString(R.string.assets_title));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTVCurrentAsset.setText(AssetsManager.INSTANCE.getAssetId());
    }

    private void startWifiConfig(String configType) {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, WifiConfigurationActivity.class);
        intent.putExtra("asset_id", AssetsManager.INSTANCE.getAssetId());
        intent.putExtra("config_type", configType);
        intent.putExtra("uid", AccessTokenManager.INSTANCE.getUid());
        intent.putExtra("country_code", mCountryCode);

        startActivity(intent);
    }


    private void startDeviceList() {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, DevicesInAssetActivity.class);
        intent.putExtra("country_code", mCountryCode);
        intent.putExtra("asset_id", AssetsManager.INSTANCE.getAssetId());

        startActivity(intent);
    }

    private boolean hasCurrentAssetId() {
        if (TextUtils.isEmpty(AssetsManager.INSTANCE.getAssetId())) {
            Toast.makeText(mContext, "please choose current asset", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}