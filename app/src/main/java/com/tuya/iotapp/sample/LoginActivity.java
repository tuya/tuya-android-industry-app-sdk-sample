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

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.login.bean.TokenBean;
import com.tuya.iotapp.login.business.LoginBusiness;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.env.EnvUrlProvider;
import com.tuya.iotapp.sample.env.EnvUtils;

/**
 * LoginActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:43 PM
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mEtCountryCode;
    private EditText mEtUserName;
    private EditText mEtPassword;

    private Button mBtnLogin;
    private LoginBusiness mLoginBusiness;
    private TokenBean mTokenBean;
    private Context context;

    private String countryCode;
    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        initView();
        EnvUtils.setEnv(this, EnvUtils.ENV_PRE); //环境区分
        IApiUrlProvider provider = new EnvUrlProvider(this);
        IotAppNetWork.initialize(getApplicationContext(), "spjyeg4vafhb1ajs63oa", "7fee20315982485295a1dadac6c3fc50", "Android", provider);
        mLoginBusiness = new LoginBusiness();
    }

    private void initView() {
        mEtCountryCode = (EditText) findViewById(R.id.et_country_code);
        mEtUserName = (EditText) findViewById(R.id.et_user_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

        String countryCode = mEtCountryCode.getText().toString();
        userName = mEtUserName.getText().toString();
        password = mEtPassword.getText().toString();

        //todo:目前登录密码先写死 后续改造
        userName = "xiaoxiao.li@tuya.com";
        password = "c679a76bc4315372c57ad1ba0a8e59f6";

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        intent.putExtra("login_token", mTokenBean);
                        intent.putExtra("country_code", countryCode);
                        intent.putExtra("user_name", userName);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
