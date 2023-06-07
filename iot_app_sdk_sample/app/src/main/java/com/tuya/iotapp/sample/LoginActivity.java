package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.thingclips.iotapp.basis.user.api.IUser;
import com.thingclips.iotapp.basis.user.api.UserService;
import com.thingclips.iotapp.common.IndustryValueCallBack;

import com.thingclips.iotapp.common.kv.KvManager;

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
    private EditText mEtProject;

    private Button mBtnLogin;
    private Context context;

    private String userName;
    private String password;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
        boolean isLogin = UserService.isLogin();
        if (isLogin) {
            goToMainPage();
        }
    }

    private void initView() {
        mEtUserName = findViewById(R.id.et_user_name);
        mEtPassword = findViewById(R.id.et_password);
        mEtProject = findViewById(R.id.et_project);

        mBtnLogin = findViewById(R.id.btn_login);

        userName = mEtUserName.getText().toString();
        password = mEtPassword.getText().toString();
        projectId = mEtProject.getText().toString();

        mBtnLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(v.getContext(), "userName can not null", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(v.getContext(), "password can not null", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(projectId)){
                Toast.makeText(v.getContext(), "projectId can not null", Toast.LENGTH_SHORT).show();
            }

                UserService.loginWithParams(projectId, userName, password, new IndustryValueCallBack<IUser>() {
                    @Override
                    public void onSuccess(IUser iUser) {
                        if (null != iUser) {
                            goToMainPage();
                        }

                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(v.getContext(), "login fail : " + s, Toast.LENGTH_SHORT).show();
                    }
                });
        });
    }

    private void goToMainPage() {
        Intent intent = new Intent(this, MainManagerActivity.class);
        KvManager.set(Constant.KV_USER_NAME, userName);
        intent.putExtra(Constant.INTENT_KEY_USER_NAME, userName);
        startActivity(intent);
        finish();
    }


}
