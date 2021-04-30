package com.tuya.iotapp.sample.activator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.IoTCommonUtil;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.activator.bean.RegistrationTokenBean;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;

/**
 * WifiConfigurationActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:12 PM
 */
public class WifiConfigurationActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEtWifiName;
    private EditText mEtWifiPassword;
    private Button mBtnNext;
    private Button mBtnRegistrationToken;
    private TextView mTvStepThree;
    private TextView mTVStepContent;

    private String ssid;
    private String password;
    private String mUid;
    private String mAssetId;
    private String mWifiType;
    private String mToken; //配网令牌token
    private String mRegion;//配网令牌region
    private String mSecret;//配网令牌secret
    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mUid = intent.getStringExtra(Constant.INTENT_KEY_UID);
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            mWifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        }

        mToolbar.setTitle(mWifiType);

        if (Constant.CONFIG_TYPE_AP.equals(mWifiType)) {
            mTvStepThree.setVisibility(View.VISIBLE);
            mTVStepContent.setVisibility(View.VISIBLE);
        }

        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        mBtnRegistrationToken.setOnClickListener(v -> {
            registrationToken();
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivatorResult();
            }
        });

    }

    private void registrationToken() {
        TYActivatorManager.Companion.getActivator().getRegistrationToken(mAssetId,
                mUid,
                Constant.CONFIG_TYPE_EZ.equals(mWifiType) ? Constant.CONFIG_TYPE_EZ : Constant.CONFIG_TYPE_AP,
                IoTCommonUtil.Companion.getTimeZoneId(),
                "",
                new ResultListener<RegistrationTokenBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        L.Companion.d("registrationToken", "onFail : " + s1);
                        Toast.makeText(mContext, "activator token get fail：" + s1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(RegistrationTokenBean registrationTokenBean) {
                        String region = null;
                        try {
                            mRegion = registrationTokenBean.getRegion();
                            mToken = registrationTokenBean.getToken();
                            mSecret = registrationTokenBean.getSecret();
                            Toast.makeText(mContext, "get activator token success", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void initView() {
        mEtWifiName = (EditText) findViewById(R.id.et_wifi_name);
        mEtWifiPassword = (EditText) findViewById(R.id.et_wifi_password);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mToolbar = findViewById(R.id.topAppBar);
        mBtnRegistrationToken = (Button) findViewById(R.id.btn_registration_token);
        mTvStepThree = (TextView) findViewById(R.id.tv_step3);
        mTVStepContent = (TextView) findViewById(R.id.tv_ap_activator_tips);
    }

    private void startActivatorResult() {
        ssid = mEtWifiName.getText().toString();
        password = mEtWifiPassword.getText().toString();

        if (TextUtils.isEmpty(ssid)) {
            Toast.makeText(mContext, "wifi name can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "wifi password can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mToken)) {
            Toast.makeText(mContext, "token can not null", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent wifiIntent;
        if (Constant.CONFIG_TYPE_AP.equals(mWifiType) || Constant.CONFIG_TYPE_EZ.equals(mWifiType)) {
            wifiIntent = new Intent(mContext, MultiWifiConfigActivity.class);
        } else if (Constant.CONFIG_TYPE_QR.equals(mWifiType)) {
            wifiIntent = new Intent(mContext, QRConfigActivity.class);
        } else {
            wifiIntent = new Intent();
        }
        wifiIntent.putExtra(Constant.INTENT_KEY_SSID, ssid);
        wifiIntent.putExtra(Constant.INTENT_KEY_WIFI_PASSWORD, password);
        wifiIntent.putExtra(Constant.INTENT_KEY_REGION, mRegion);
        wifiIntent.putExtra(Constant.INTENT_KEY_TOKEN, mToken);
        wifiIntent.putExtra(Constant.INTENT_KEY_SECRET, mSecret);
        wifiIntent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, mWifiType);
        startActivity(wifiIntent);
    }

}
