package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.assets.business.AssetBusiness;
import com.tuya.iotapp.login.bean.TokenBean;

public class MainManagerActivity extends AppCompatActivity {
    private TextView mTvUserName;
    private TextView mTVCurrentAsset;
    private Button mBtnAssets;
    private Button mBtnAp;
    private Button mBtnEz;
    private Button mBtnQR;
    private Button mBtnDevices;

    private Context mContext;
    private TokenBean mLoginToken;
    private String mCountryCode;
    private String mUserName;

    private AssetBusiness mAssetBusiness;

    private String mAssetId = "1372829753920290816";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        initView(this);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mLoginToken = (TokenBean) intent.getSerializableExtra("login_token");
            mCountryCode = intent.getStringExtra("country_code");
            mUserName = intent.getStringExtra("user_name");
        }
        mAssetBusiness = new AssetBusiness(mCountryCode);

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

        mBtnAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssetBusiness.queryAssets();
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
    }

    private void startWifiConfig(String configType) {
        if (mLoginToken == null) {
            return;
        }
        Intent intent = new Intent(mContext, WifiConfigurationActivity.class);
        intent.putExtra("asset_id", mAssetId);
        intent.putExtra("config_type", configType);
        intent.putExtra("uid", mLoginToken.getUid());
        intent.putExtra("country_code", mCountryCode);

        startActivity(intent);
    }


    private void startDeviceList() {
        Intent intent = new Intent(mContext, DevicesInAssetActivity.class);
        intent.putExtra("country_code", mCountryCode);
        intent.putExtra("asset_id", mAssetId);

        startActivity(intent);
    }
}