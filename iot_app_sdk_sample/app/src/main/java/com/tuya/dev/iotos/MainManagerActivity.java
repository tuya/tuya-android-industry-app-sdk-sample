package com.tuya.dev.iotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuya.dev.common.kv.KvManager;
import com.tuya.dev.iotos.assets.AssetsActivity;
import com.tuya.dev.network.accessToken.AccessTokenManager;
import com.tuya.dev.iotos.activator.WifiConfigurationActivity;
import com.tuya.dev.iotos.assets.AssetsManager;
import com.tuya.dev.iotos.config.adapter.ConfigTypeAdapter;
import com.tuya.dev.iotos.config.bean.ConfigTypeBean;
import com.tuya.dev.iotos.devices.DevicesInAssetActivity;
import com.tuya.dev.iotos.env.Constant;
import com.tuya.dev.iotos.view.SpacesItemDecoration;

import java.util.ArrayList;

public class MainManagerActivity extends AppCompatActivity {
    private TextView mTvUserName;
    private Button mBtnDevices;
    private Button mBtnLogout;
    private RecyclerView rvConfig;

    private Context mContext;
    private String mCountryCode;
    private String mUserName;

    private TextView tvCurAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);
        initView(this);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mCountryCode = intent.getStringExtra(Constant.INTENT_KEY_COUNTRY_CODE);
            mUserName = intent.getStringExtra(Constant.INTENT_KEY_USER_NAME);
        }

        if (TextUtils.isEmpty(mUserName)) {
            mUserName = KvManager.getString(Constant.KV_USER_NAME);
        }

        mTvUserName.setText("UserName : " + mUserName);
        rvConfig = findViewById(R.id.rvConfig);
        tvCurAsset = findViewById(R.id.tvCurAsset);

        ArrayList<ConfigTypeBean> configTypeBeans = new ArrayList<>();
        configTypeBeans.add(new ConfigTypeBean(getString(R.string.config_type_ap),
                R.drawable.ic_config_ap,
                v -> {
                    startWifiConfig(Constant.CONFIG_TYPE_AP);
                }));
        configTypeBeans.add(new ConfigTypeBean(getString(R.string.config_type_qr),
                R.drawable.ic_config_qr,
                v -> {
                    startWifiConfig(Constant.CONFIG_TYPE_QR);
                }));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvConfig.setLayoutManager(layoutManager);
        rvConfig.setAdapter(new ConfigTypeAdapter(configTypeBeans));
        rvConfig.addItemDecoration(new SpacesItemDecoration(15));

        mBtnDevices.setOnClickListener(v -> startDeviceList());

        mBtnLogout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(v.getContext())
                    .setMessage(getString(R.string.user_logout))
                    .setNeutralButton(getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton(R.string.ok, ((dialog, which) -> {
                        AccessTokenManager.INSTANCE.clearInfo();
                        AssetsManager.INSTANCE.saveAssets("");
                        KvManager.clear();
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    })).show();
        });

        findViewById(R.id.btnAsset).setOnClickListener(v->{
            AssetsActivity.launch(v.getContext(),
                    "",
                    getString(R.string.assets_title));
        });
    }

    private void initView(Context context) {
        mTvUserName = (TextView) findViewById(R.id.tv_userName);

        mBtnDevices = (Button) findViewById(R.id.btn_device_list);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);

    }

    private void startWifiConfig(String configType) {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, WifiConfigurationActivity.class);
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
        intent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, configType);
        intent.putExtra(Constant.INTENT_KEY_UID, AccessTokenManager.INSTANCE.getUid());
        intent.putExtra(Constant.INTENT_KEY_COUNTRY_CODE, mCountryCode);

        startActivity(intent);
    }


    private void startDeviceList() {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, DevicesInAssetActivity.class);
        intent.putExtra(Constant.INTENT_KEY_COUNTRY_CODE, mCountryCode);
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
    protected void onResume() {
        super.onResume();
        tvCurAsset.setText(getString(R.string.current_asset) + AssetsManager.INSTANCE.getAssetId());
    }

    @Override
    public void onBackPressed() {
        return;
    }
}