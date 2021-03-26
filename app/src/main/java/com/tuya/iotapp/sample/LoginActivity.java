package com.tuya.iotapp.sample;

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

import com.tuya.iotapp.common.kv.KvManager;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.login.business.LoginBusiness;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.accessToken.AccessTokenManager;
import com.tuya.iotapp.network.accessToken.bean.TokenBean;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.env.Constant;

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

    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //todo disable switch
        LogUtils.setLogSwitcher(true);

        if (!TextUtils.isEmpty(AccessTokenManager.INSTANCE.getUid())) {
            startActivity(new Intent(this, MainManagerActivity.class));
            finish();
        }

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

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mEtUserName.getText().toString();
                password = mEtPassword.getText().toString();

                //todo:目前登录密码先写死 后续改造
//                if (BuildConfig.DEBUG) {
//                    userName = "13924610476";
//                    password = "Tuya123456";
//                }

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
                        if (mTokenBean != null) {
                            IotAppNetWork.setAccessToken(mTokenBean.getAccess_token());
                        }
                        Intent intent = new Intent(context, MainManagerActivity.class);

                        // Store Token
                        AccessTokenManager.INSTANCE.storeInfo(mTokenBean,
                                bizResponse.getT());
                        KvManager.set(Constant.KV_USER_NAME, userName);

                        intent.putExtra(Constant.INTENT_KEY_COUNTRY_CODE, "");
                        intent.putExtra(Constant.INTENT_KEY_USER_NAME, userName);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
