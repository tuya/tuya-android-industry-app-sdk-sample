package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.bean.RegistrationTokenBean;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;

/**
 * WifiConfigurationActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:12 PM
 */
public class WifiConfigurationActivity extends AppCompatActivity {

    private EditText mEtWifiName;
    private EditText mEtWifiPassword;
    private Button mBtnNext;

    private String ssid = "Tuya-Test";
    private String password = "Tuya.140616";
    private String mUid;
    private String mAssetId;
    private String mWifiType;
    private String mToken; //配网令牌token
    private String mActivitorToken; //mActivitorToken：region + mToken + secret
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

        mDeviceBusiness = new DeviceBusiness(mCountryCode);
        registrationToken();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivitorResult();
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

                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, RegistrationTokenBean bizResult, String apiName) {
                        LogUtils.d("registratinToken", "=====onSuccess====: " + bizResult.toString());
                        String region = null;
                        try {
                            region = bizResult.getRegion();
                            mToken = bizResult.getToken();
                            String secret = bizResult.getSecret();
                            StringBuilder builder = new StringBuilder();
                            builder.append(region);
                            builder.append(mToken);
                            builder.append(secret);
                            mActivitorToken = builder.toString();
                            Toast.makeText(mContext, "令牌 拼接 token ：" + mActivitorToken, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mEtWifiName = (EditText) findViewById(R.id.et_wifi_name);
        mEtWifiPassword = (EditText) findViewById(R.id.et_password);
        mBtnNext = (Button) findViewById(R.id.btn_next);
    }

    private void startActivitorResult() {

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
        wifiIntent.putExtra("activitor_token", mActivitorToken);
        wifiIntent.putExtra("config_type", mWifiType);
        startActivity(wifiIntent);
    }

}
