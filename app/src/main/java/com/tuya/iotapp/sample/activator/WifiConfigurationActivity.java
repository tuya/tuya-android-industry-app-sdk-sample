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

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.bean.RegistrationTokenBean;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.R;

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
    private String mActivatorToken; //mActivatorToken：region + mToken + secret
    private String mCountryCode;

    private Context mContext;

    private DeviceBusiness mDeviceBusiness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mUid = intent.getStringExtra("uid");
            mAssetId = intent.getStringExtra("asset_id");
            mWifiType = intent.getStringExtra("config_type");
            mCountryCode = intent.getStringExtra("country_code");
        }

        mToolbar.setTitle(mWifiType);
        mDeviceBusiness = new DeviceBusiness(mCountryCode);

        if ("AP".equals(mWifiType)) {
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
        mDeviceBusiness.getDeviceRegistrationToken(mAssetId,
                mUid,
                "EZ".equals(mWifiType) ? "EZ" : "AP",
                new ResultListener<RegistrationTokenBean>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, RegistrationTokenBean bizResult, String apiName) {
                        LogUtils.d("registrationToken", "=====onFail====: " + bizResponse.getMsg());
                        Toast.makeText(mContext, "activator token get fail：" + bizResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, RegistrationTokenBean bizResult, String apiName) {
                        LogUtils.d("registrationToken", "=====onSuccess====: " + bizResult.toString());
                        String region = null;
                        try {
                            region = bizResult.getRegion();
                            mToken = bizResult.getToken();
                            String secret = bizResult.getSecret();
                            StringBuilder builder = new StringBuilder();
                            builder.append(region);
                            builder.append(mToken);
                            builder.append(secret);
                            mActivatorToken = builder.toString();
                            Toast.makeText(mContext, "activator token ：" + mActivatorToken, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
        if ("AP".equals(mWifiType) || "EZ".equals(mWifiType)) {
            wifiIntent = new Intent(mContext, MultiWifiConfigActivity.class);
        } else if ("QR".equals(mWifiType)) {
            wifiIntent = new Intent(mContext, QRConfigActivity.class);
        } else {
            wifiIntent = new Intent();
        }
        wifiIntent.putExtra("ssid", ssid);
        wifiIntent.putExtra("password", password);
        wifiIntent.putExtra("token", mToken);
        wifiIntent.putExtra("activator_token", mActivatorToken);
        wifiIntent.putExtra("config_type", mWifiType);
        startActivity(wifiIntent);
    }

}
