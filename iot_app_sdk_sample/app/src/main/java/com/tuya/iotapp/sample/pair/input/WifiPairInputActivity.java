package com.tuya.iotapp.sample.pair.input;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thingclips.iotapp.common.IndustryDataCallBack;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;
import com.tuya.iotapp.sample.pair.result.WifiPairResultActivity;

/**
 * WifiConfigurationActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:12 PM
 */
public class WifiPairInputActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etWifiSsid;
    private EditText etWifiPassword;
    private Button btnNext;
    private Button btnRegistrationToken;
    private TextView tvStepAP;
    private TextView tvApTips;

    private String mAssetId;
    private String mWifiType;
    private String mActivatorToken;

    private Context mContext;
    private boolean isSimpleInput = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_wifi_pair_input);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            mWifiType = intent.getStringExtra(Constant.INTENT_KEY_CONFIG_TYPE);
            isSimpleInput = intent.getBooleanExtra(Constant.INTENT_KEY_SIMPLE_INPUT, false);
        }

        mToolbar.setTitle(mWifiType);

        if (PairType.AP.equals(mWifiType)) {
            tvStepAP.setVisibility(View.VISIBLE);
            tvApTips.setVisibility(View.VISIBLE);
        }

        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        btnRegistrationToken.setOnClickListener(v -> {
            registrationToken();
        });

        btnNext.setOnClickListener(v -> startActivatorResult());

    }

    /**
     * 获取WiFi配网token
     */
    private void registrationToken() {
        ActivatorService.activatorToken(mAssetId, new IndustryDataCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mActivatorToken = s;
                Toast.makeText(mContext, "activator token ：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull String s, @NonNull String s1) {
                Toast.makeText(mContext, "activator token failure：" + s1, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView() {
        etWifiSsid = (EditText) findViewById(R.id.etWifiSsid);
        etWifiPassword = (EditText) findViewById(R.id.etWifiPassword);
        btnNext = (Button) findViewById(R.id.btnNext);
        mToolbar = findViewById(R.id.topAppBar);
        btnRegistrationToken = (Button) findViewById(R.id.btnRegistrationToken);
        tvStepAP = (TextView) findViewById(R.id.tvStepAP);
        tvApTips = (TextView) findViewById(R.id.tvApTips);
    }

    private void startActivatorResult() {
        String ssid = etWifiSsid.getText().toString();
        String password = etWifiPassword.getText().toString();

        if (TextUtils.isEmpty(ssid)) {
            Toast.makeText(mContext, "wifi name can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "wifi password can not null", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mActivatorToken)) {
            Toast.makeText(mContext, "token can not null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isSimpleInput) {
            Intent data = new Intent();
            data.putExtra(Constant.INTENT_KEY_SSID, ssid);
            data.putExtra(Constant.INTENT_KEY_WIFI_PASSWORD, password);
            data.putExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN, mActivatorToken);
            setResult(RESULT_OK, data);
            finish();
            return;
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        L.d("wifiInfo------", wifiInfo.toString());
        L.d("SSID------", wifiInfo.getSSID());
        L.d(TAG, "INTENT_KEY_SSID:" + ssid + ", INTENT_KEY_WIFI_PASSWORD:" + password + ", INTENT_KEY_ACTIVATOR_TOKEN:" + mActivatorToken + ", mWifiType:" + mWifiType);

        Intent wifiIntent;

        wifiIntent = new Intent(mContext, WifiPairResultActivity.class);
        wifiIntent.putExtra(Constant.INTENT_KEY_SSID, ssid);
        wifiIntent.putExtra(Constant.INTENT_KEY_WIFI_PASSWORD, password);
        wifiIntent.putExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN, mActivatorToken);
        wifiIntent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, mWifiType);
        startActivity(wifiIntent);
    }

}
