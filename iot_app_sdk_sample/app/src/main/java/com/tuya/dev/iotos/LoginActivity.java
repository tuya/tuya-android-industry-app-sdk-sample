package com.tuya.dev.iotos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tuya.dev.common.kv.KvManager;
import com.tuya.dev.common.utils.LogUtils;
import com.tuya.dev.login.business.LoginBusiness;
import com.tuya.dev.network.accessToken.AccessTokenManager;
import com.tuya.dev.network.accessToken.bean.TokenBean;
import com.tuya.dev.network.business.BusinessResponse;
import com.tuya.dev.network.request.ResultListener;
import com.tuya.dev.iotos.assets.AssetsManager;
import com.tuya.dev.iotos.assets.bean.AssetBean;
import com.tuya.dev.iotos.assets.business.AssetBusiness;
import com.tuya.dev.iotos.authScan.AuthFirstActivity;
import com.tuya.dev.iotos.authScan.AuthManager;
import com.tuya.dev.iotos.env.Constant;

/**
 * LoginActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:43 PM
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mEtUserName;
    private EditText mEtPassword;

    private Button mBtnLogin;
    private LoginBusiness mLoginBusiness;
    private TokenBean mTokenBean;
    private Context context;

    private AssetBusiness business = new AssetBusiness();

    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //todo disable switch

        context = this;
        initView();

        mLoginBusiness = new LoginBusiness();
    }

    private void initView() {
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        userName = mEtUserName.getText().toString();
        password = mEtPassword.getText().toString();

        findViewById(R.id.btnSwitch).setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(v.getContext())
                    .setTitle(R.string.tips)
                    .setMessage(getString(R.string.auth_switch_tips))
                    .setNeutralButton(getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton(R.string.ok, ((dialog, which) -> {
                        AuthManager.clear();
                        startActivity(new Intent(v.getContext(), AuthFirstActivity.class));
                        finish();
                    })).show();

        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mEtUserName.getText().toString();
                password = mEtPassword.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(v.getContext(), "userName can not null", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(v.getContext(), "password can not null", Toast.LENGTH_SHORT).show();
                }
                mLoginBusiness.login(null, userName, password, new ResultListener<TokenBean>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, TokenBean bizResult, String apiName) {
                        LogUtils.d("login", "fail code: " + bizResponse.getCode() + " msg:" + bizResponse.getMsg());
                        Toast.makeText(v.getContext(), "login fail : " + bizResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, TokenBean bizResult, String apiName) {
                        LogUtils.d("login", "success : " + bizResult.getAccess_token());
                        mTokenBean = bizResult;


                        // Cache Token
                        AccessTokenManager.INSTANCE.storeInfo(mTokenBean,
                                bizResponse.getT());

                        business.queryAssets("",
                                new ResultListener<AssetBean>() {
                                    @Override
                                    public void onFailure(BusinessResponse bizResponse, AssetBean bizResult, String apiName) {
                                        Toast.makeText(LoginActivity.this,
                                                bizResponse.getMsg(),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    @Override
                                    public void onSuccess(BusinessResponse bizResponse, AssetBean bizResult, String apiName) {
                                        if (bizResult.getAssets().size() == 0) {
                                            Toast.makeText(LoginActivity.this,
                                                    getString(R.string.user_login_bind_asset_tips),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            return;
                                        }

                                        // Store Token
                                        KvManager.set(Constant.KV_USER_NAME, userName);

                                        // Store First AssetId
                                        AssetsManager.INSTANCE.saveAssets(bizResult.getAssets().get(0).getAsset_id());

                                        Intent intent = new Intent(context, MainManagerActivity.class);
                                        intent.putExtra(Constant.INTENT_KEY_COUNTRY_CODE, "");
                                        intent.putExtra(Constant.INTENT_KEY_USER_NAME, userName);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                    }
                });
            }
        });
    }
}
