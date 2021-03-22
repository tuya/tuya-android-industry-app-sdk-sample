package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private String mToken;
    private String mActivitorToken;
    private String mWifiType;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_config);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mToken = intent.getStringExtra("token");
            mActivitorToken = intent.getStringExtra("activitor_token");
            mWifiType = intent.getStringExtra("config_type");
        }

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wifiIntent;
                if ("AP".equals(mWifiType) || "EZ".equals(mWifiType)) {
                    wifiIntent = new Intent(mContext, MultiWifiConfigActivity.class);
                } else if ("QR".equals(mWifiType)){
                    wifiIntent = new Intent(mContext, QRConfigActivity.class);
                } else {
                    wifiIntent = new Intent();
                }
                wifiIntent.putExtra("ssid", ssid);
                wifiIntent.putExtra("password", password);
                wifiIntent.putExtra("token", mToken);
                wifiIntent.putExtra("activitor_token",mActivitorToken);
                wifiIntent.putExtra("config_type",mWifiType);
                startActivity(wifiIntent);
            }
        });
    }

    private void initView() {
        mEtWifiName = (EditText) findViewById(R.id.et_wifi_name);
        mEtWifiPassword = (EditText) findViewById(R.id.et_password);
        mBtnNext = (Button) findViewById(R.id.btn_next);
    }
}
