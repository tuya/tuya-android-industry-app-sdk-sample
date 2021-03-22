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

import com.tuya.iotapp.assets.business.AssetBusiness;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.devices.business.DeviceBusiness;
import com.tuya.iotapp.login.bean.TokenBean;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String mToken; //配网令牌token
    private String mActivitorToken; //mActivitorToken：region + mToken + secret

    private DeviceBusiness mDeviceBusiness;
    private AssetBusiness mAssetBusiness;


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
        mDeviceBusiness = new DeviceBusiness(mCountryCode);
        mAssetBusiness = new AssetBusiness(mCountryCode);

        if (!TextUtils.isEmpty(mUserName)) {
            mTvUserName.setText("UserName : " + mUserName);
        }

        mBtnAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiConfig("AP");
            }
        });

        mBtnEz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiConfig("EZ");
            }
        });

        mBtnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiConfig("QR");
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

    private void wifiConfig(String configType) {
        if (mLoginToken == null) {
            return;
        }
        mDeviceBusiness.getDeviceRegistrationToken("1372829753920290816",
                mLoginToken.getUid(),
                "AP",
                new ResultListener<JSONObject>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {

            }

            @Override
            public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                LogUtils.d("registratinToken", "=====onSuccess====: " + bizResult.toString());
                String region = null;
                try {
                    region = bizResult.getString("region");
                    mToken = bizResult.getString("token");
                    String secret = bizResult.getString("secret");
                    StringBuilder builder = new StringBuilder();
                    builder.append(region);
                    builder.append(mToken);
                    builder.append(secret);
                    mActivitorToken = builder.toString();

                    Toast.makeText(mContext, "令牌 拼接 token ：" + mActivitorToken, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, WifiConfigurationActivity.class);
                    intent.putExtra("token", mToken);
                    intent.putExtra("activitor_token", mActivitorToken);
                    intent.putExtra("config_type", configType);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}