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

import com.tuya.iotapp.common.BuildConfig;
import com.tuya.iotapp.common.kv.KvManager;
import com.tuya.iotapp.network.executor.TYNetworkExecutorManager;
import com.tuya.iotapp.network.interceptor.token.AccessTokenManager;
import com.tuya.iotapp.sample.activator.WifiConfigurationActivity;
import com.tuya.iotapp.sample.assets.AssetsActivity;
import com.tuya.iotapp.sample.assets.AssetsManager;
import com.tuya.iotapp.sample.devices.DevicesInAssetActivity;
import com.tuya.iotapp.sample.env.Constant;

public class MainManagerActivity extends AppCompatActivity {
    private TextView mTvUserName;
    private TextView mTVCurrentAsset;
    private Button mBtnAssets;
    private Button mBtnAp;
    private Button mBtnEz;
    private Button mBtnQR;
    private Button mBtnDevices;
    private Button mBtnLogout;

    private Context mContext;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        initView(this);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mUserName = intent.getStringExtra(Constant.INTENT_KEY_USER_NAME);
        }

        if (TextUtils.isEmpty(mUserName)) {
            mUserName = KvManager.Companion.getString(Constant.KV_USER_NAME);
        }

        mTvUserName.setText("UserName : " + mUserName);

        mBtnAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig(Constant.CONFIG_TYPE_AP);
            }
        });

        mBtnEz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig(Constant.CONFIG_TYPE_EZ);
            }
        });

        mBtnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWifiConfig(Constant.CONFIG_TYPE_QR);
            }
        });

        mBtnDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeviceList();
            }
        });

        mBtnLogout.setOnClickListener(v -> {
            AccessTokenManager.Companion.getAccessTokenRepository().clearInfo();
            AssetsManager.INSTANCE.saveAssets("");
            KvManager.Companion.clear();
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            finish();
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
        mBtnLogout = (Button) findViewById(R.id.btn_logout);

        Toolbar toolbar = findViewById(R.id.topAppBar);

        mBtnAssets.setOnClickListener(v -> {
            AssetsActivity.launch(v.getContext(),
                    "",
                    getString(R.string.assets_title));
        });

        if (BuildConfig.DEBUG){
            mBtnAssets.setOnLongClickListener(v -> {
                TYNetworkExecutorManager.Companion.getBusinessExecutor().execute(() -> AccessTokenManager.Companion.getAccessTokenRepository().refreshToken());

                return true;
            });
        }
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
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
        intent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, configType);
        intent.putExtra(Constant.INTENT_KEY_UID, AccessTokenManager.Companion.getAccessTokenRepository().getUid());

        startActivity(intent);
    }

    private void startDeviceList() {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, DevicesInAssetActivity.class);
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());

        startActivity(intent);
    }

    private boolean hasCurrentAssetId() {
        if (TextUtils.isEmpty(AssetsManager.INSTANCE.getAssetId())) {
            Toast.makeText(mContext, "please choose current asset", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}