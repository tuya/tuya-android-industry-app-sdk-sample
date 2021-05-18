package com.tuya.dev.iotos.activator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.activator.ap.ActivatorAPTipsActivity;
import com.tuya.dev.iotos.env.Constant;
import com.tuya.dev.iotos.log.LogUtils;
import com.tuya.iotapp.activator.bean.RegistrationTokenBean;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.network.interceptor.token.AccessTokenManager;
import com.tuya.iotapp.network.response.ResultListener;

import java.util.TimeZone;

/**
 * WifiConfigurationActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:12 PM
 */
public class ActivatorWifiSetActivity extends AppCompatActivity {

    private EditText mEtWifiName;
    private EditText mEtWifiPassword;
    private TextView tvNext;

    private String ssid;
    private String password;
    private String mAssetId;
    private String mWifiType;
    private String mToken; //配网令牌token
    private String region;
    private String secret;

    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activator_wifi_config);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            mWifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
        }

        ((TextView) findViewById(R.id.tvTitle)).setText(R.string.activator_wifi_set_title);
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivatorResult();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registrationToken();
    }

    private void registrationToken() {
        TYActivatorManager.Companion.getActivator().getRegistrationToken(mAssetId,
                AccessTokenManager.Companion.getAccessTokenRepository().getUid(),
                Constant.CONFIG_TYPE_EZ.equals(mWifiType) ? Constant.CONFIG_TYPE_EZ : Constant.CONFIG_TYPE_AP,
                TimeZone.getDefault().getID(),
                "",
                new ResultListener<RegistrationTokenBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        LogUtils.d("registrationToken", "onFail : " + s1);
                        Toast.makeText(mContext, "activator token get fail：" + s1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(RegistrationTokenBean registrationTokenBean) {
                        LogUtils.d("registrationToken", "onSuccess : " + registrationTokenBean.toString());

                        try {
                            region = registrationTokenBean.getRegion();
                            mToken = registrationTokenBean.getToken();
                            secret = registrationTokenBean.getSecret();

//                            Toast.makeText(mContext, "activator token ：" + mToken, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        mEtWifiName = (EditText) findViewById(R.id.et_wifi_name);
        mEtWifiPassword = (EditText) findViewById(R.id.et_wifi_password);
        tvNext = findViewById(R.id.tvNext);

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
        if (Constant.CONFIG_TYPE_EZ.equals(mWifiType)) {
            wifiIntent = new Intent(mContext, ActivatorProcessActivity.class);
        } else if (Constant.CONFIG_TYPE_AP.equals(mWifiType)) {
            wifiIntent = new Intent(mContext, ActivatorAPTipsActivity.class);
        } else if (Constant.CONFIG_TYPE_QR.equals(mWifiType)) {
            wifiIntent = new Intent(mContext, ActivatorQRConfigActivity.class);
        } else {
            wifiIntent = new Intent();
        }

        wifiIntent.putExtra(Constant.INTENT_KEY_SSID, ssid);
        wifiIntent.putExtra(Constant.INTENT_KEY_WIFI_PASSWORD, password);
        wifiIntent.putExtra(Constant.INTENT_KEY_TOKEN, mToken);
        wifiIntent.putExtra(Constant.INTENT_KEY_REGION, region);
        wifiIntent.putExtra(Constant.INTENT_KEY_SECRET, secret);
        wifiIntent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, mWifiType);
        startActivity(wifiIntent);
    }

}
