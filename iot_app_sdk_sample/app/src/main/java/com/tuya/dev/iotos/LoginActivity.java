package com.tuya.dev.iotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuya.dev.iotos.assets.AssetsManager;
import com.tuya.dev.iotos.authScan.AuthFirstActivity;
import com.tuya.dev.iotos.authScan.AuthManager;
import com.tuya.dev.iotos.env.Constant;
import com.tuya.dev.iotos.env.Endpoint;
import com.tuya.dev.iotos.kv.KvManager;
import com.tuya.dev.iotos.log.LogUtils;
import com.tuya.iotapp.asset.api.TYAssetManager;
import com.tuya.iotapp.asset.bean.AssetsBean;
import com.tuya.iotapp.jsonparser.api.JsonParser;
import com.tuya.iotapp.network.api.TYNetworkManager;
import com.tuya.iotapp.network.interceptor.token.AccessTokenManager;
import com.tuya.iotapp.network.interceptor.token.bean.TokenBean;
import com.tuya.iotapp.network.response.BizResponse;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.user.api.TYUserManager;

/**
 * LoginActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:43 PM
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mEtUserName;
    private EditText mEtPassword;

    private Context context;

    private String userName;
    private String password;

    private TokenBean mTokenBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        initView();
    }

    private void initView() {
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);

        userName = mEtUserName.getText().toString();
        password = mEtPassword.getText().toString();


        AppCompatSpinner spEndpoint = findViewById(R.id.spEndpoint);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.auth_endpoint,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEndpoint.setAdapter(adapter);
        spEndpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                if (position == 0) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.AZ);
                } else if (position == 1) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.AY);
                } else if (position == 2) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.EU);
                } else if (position == 3) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.IN);
                } else if (position == 4) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.UE);
                } else if (position == 5) {
                    TYNetworkManager.Companion.setRegionHost(Endpoint.WE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.tvSwitch).setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(v.getContext())
                    .setTitle(R.string.tips)
                    .setMessage(getString(R.string.auth_switch_tips))
                    .setNeutralButton(getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton(R.string.ok, ((dialog, which) -> {
                        AuthManager.clear();
                        Intent loginIntent = new Intent(this, AuthFirstActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        this.startActivity(loginIntent);
                        finish();
                    })).show();

        });

        findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mEtUserName.getText().toString();
                password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(v.getContext(), "userName can not null", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(v.getContext(), "password can not null", Toast.LENGTH_SHORT).show();
                } else {
                    TYUserManager.Companion.getUserBusiness().login(userName,
                            password,
                            new ResultListener<BizResponse>() {
                                @Override
                                public void onFailure(String s, String s1) {
                                    LogUtils.d("login", "fail code: " + s + " msg:" + s1);
                                    Toast.makeText(v.getContext(), "login fail : " + s1, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(BizResponse bizResponse) {
                                    String convertString = JsonParser.Companion.convertUnderLineToHump(bizResponse.getResult().toString());
                                    mTokenBean = JsonParser.Companion.getJsonParser().parseAny(convertString, TokenBean.class);

                                    AccessTokenManager.Companion.getAccessTokenRepository().storeInfo(mTokenBean,
                                            bizResponse.getT());

                                    TYAssetManager.Companion.getAssetBusiness().queryAssets("",
                                            0,
                                            20,
                                            new ResultListener<AssetsBean>() {
                                                @Override
                                                public void onFailure(String s, String s1) {

                                                }

                                                @Override
                                                public void onSuccess(AssetsBean assetsBean) {
                                                    if (assetsBean.getAssets().size() == 0) {
                                                        Toast.makeText(LoginActivity.this,
                                                                getString(R.string.user_login_bind_asset_tips),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                        return;
                                                    }

                                                    // Store Token
                                                    KvManager.set(Constant.KV_USER_NAME, userName);

                                                    // Store First AssetId
                                                    AssetsManager.INSTANCE.saveAssets(assetsBean.getAssets().get(0).getAssetId(),
                                                            assetsBean.getAssets().get(0).getAssetName());

                                                    Intent intent = new Intent(context, MainManagerActivity.class);
                                                    intent.putExtra(Constant.INTENT_KEY_USER_NAME, userName);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }
}
