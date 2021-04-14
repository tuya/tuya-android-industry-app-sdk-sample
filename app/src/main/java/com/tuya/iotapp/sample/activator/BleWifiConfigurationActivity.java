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

import com.tuya.iotapp.activator.bean.BLEScanObject;
import com.tuya.iotapp.activator.config.BleWifiConfigImpl;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.bean.AuthKeyBean;
import com.tuya.iotapp.devices.bean.RegistrationTokenBean;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.smart.sdk.config.ble.api.TuyaBleConfigListener;
import com.tuya.smart.sdk.config.ble.api.TuyaBleScanCallback;
import com.tuya.smart.sdk.config.ble.api.bean.BLEScanBean;
import com.tuya.smart.sdk.config.ble.api.bean.ConfigParams;
import com.tuya.smart.sdk.config.ble.api.bean.ConfigParamsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * BleWifiConfigurationActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:12 PM
 */
public class BleWifiConfigurationActivity extends AppCompatActivity {
    private static final String TAG = "BleWifiConfigurationActivity";

    private Toolbar mToolbar;
    private EditText mEtWifiName;
    private EditText mEtWifiPassword;
    private Button mBtnNext;
    private Button mBtnRegistrationToken;
    private Button mBtnAuthKey;

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
    private HashMap<String, BLEScanBean> map = new HashMap<String, BLEScanBean>();
    private BLEScanBean mBleScanBean;
    private AuthKeyBean mAuthKeyBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_wifi_config);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mUid = intent.getStringExtra(Constant.INTENT_KEY_UID);
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            mWifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
            mCountryCode = intent.getStringExtra(Constant.INTENT_KEY_COUNTRY_CODE);
            BLEScanObject object = (BLEScanObject)intent.getSerializableExtra(Constant.INTENT_KEY_BLE_SCAN_BEAN);
            mBleScanBean = object.castToBLEScanBean();
        }

        mToolbar.setTitle(mWifiType);
        mDeviceBusiness = new DeviceBusiness(mCountryCode);

        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        mBtnRegistrationToken.setOnClickListener(v -> {
            registrationToken();
        });

        mBtnAuthKey.setOnClickListener(v -> {
            getAuthKeyByUuid();
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivatorResult();
            }
        });

    }

    private void registrationToken() {
        Map<String,String> extension = new HashMap<>();
        extension.put("uuid", mBleScanBean.devUuId);
        mDeviceBusiness.getDeviceRegistrationToken(mAssetId,
                mUid,
                Constant.CONFIG_TYPE_BLE,
                extension,
                new ResultListener<RegistrationTokenBean>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, RegistrationTokenBean bizResult, String apiName) {
                        LogUtils.d("registrationToken", "onFail : " + bizResponse.getMsg());
                        Toast.makeText(mContext, "activator token get fail：" + bizResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, RegistrationTokenBean bizResult, String apiName) {
                        LogUtils.d("registrationToken", "onSuccess : " + bizResult.toString());
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
                            Toast.makeText(mContext, "Get token success", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAuthKeyByUuid() {
        mDeviceBusiness.getAuthKeyByUuid(mAssetId, mBleScanBean.devUuId, new ResultListener<AuthKeyBean>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, AuthKeyBean bizResult, String apiName) {
                LogUtils.d(TAG, "getAuthKeyByUuid onFail errorcode: " + bizResponse.getCode() +
                        "erromsg:" + bizResponse.getMsg());
                Toast.makeText(mContext, "GetAuthKey fail",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(BusinessResponse bizResponse, AuthKeyBean bizResult, String apiName) {
                if (bizResult != null) {
                    mAuthKeyBean = bizResult;
                    LogUtils.d(TAG, "getAuthKeyByUuid success key: "+mAuthKeyBean.getAuthKey()
                            + " random:"+mAuthKeyBean.getRandom());
                    Toast.makeText(mContext, "GetAuthKey success",Toast.LENGTH_SHORT).show();
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
        mBtnAuthKey = (Button) findViewById(R.id.btn_authKey);
    }

    private void startActivatorResult() {
//        ssid = mEtWifiName.getText().toString();
//        password = mEtWifiPassword.getText().toString();
        ssid = "Tuya-Test";
        password = "Tuya.140616";


        if (TextUtils.isEmpty(ssid)) {
            Toast.makeText(mContext, "wifi name can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "wifi password can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mToken)) {
            Toast.makeText(mContext, "token can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (mBleScanBean == null) {
            Toast.makeText(mContext, "scan choose device can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (mAuthKeyBean == null) {
            Toast.makeText(mContext, "device authKeyBean can not null", Toast.LENGTH_SHORT).show();
            return;
        }
        ConfigParams configParams = new ConfigParamsBuilder()
                .scanBean(mBleScanBean)
                .authKey(mAuthKeyBean.getAuthKey())
                .random(mAuthKeyBean.getRandom())
                .token(mActivatorToken)
                .ssid(ssid)
                .password(password)
                .build();


        BleWifiConfigImpl.startConfig(configParams, new TuyaBleConfigListener() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(mContext, "Ble activator success",Toast.LENGTH_SHORT).show();
                LogUtils.d(TAG, "start ble wifi config onSuccess");
            }

            @Override
            public void onFail(String s, String s1, String s2) {
                LogUtils.d(TAG, "start ble wifi config fail:s1" +s1 + "s2:"+s2);
                Toast.makeText(mContext, "Ble activator fail",Toast.LENGTH_SHORT).show();
                if (mBleScanBean != null && !TextUtils.isEmpty(mBleScanBean.devUuId)) {
                    BleWifiConfigImpl.stopConfig(mBleScanBean.devUuId);
                }
            }
        });
    }

}
