/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NO
 */

package com.tuya.iotapp.sample.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.login.bean.LoginBean;


/**
 * User Login
 *
 * @author chuanfeng <a href="mailto:developer@tuya.com"/>
 * @since 2021/2/9 2:01 PM
 */
public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginBusiness business = new LoginBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_login);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        EditText etAccount = findViewById(R.id.etAccount);
        String strAccount = "18640825065";
//                etAccount.getText().toString();
//        EditText etCountryCode = findViewById(R.id.etCountryCode);
//        String strCountryCode = etCountryCode.getText().toString();
        EditText etPassword = findViewById(R.id.etPassword);
        String strPassword = "b8a762334ab0c2f25a0503a86b152f77";
//                etPassword.getText().toString();

        if (v.getId() == R.id.btnLogin) {
            business.login(strAccount,
                    strPassword,
                    new ResultListener<LoginBean>() {
                        @Override
                        public void onFailure(BusinessResponse bizResponse, LoginBean bizResult, String apiName) {
                            LogUtils.d("TestBusiness", "====-failure======code: " + bizResponse.getCode() + " msg:" + bizResponse.getMsg());
                        }

                        @Override
                        public void onSuccess(BusinessResponse bizResponse, LoginBean bizResult, String apiName) {
                            LogUtils.d("TestBusiness", "====-onSuccess======" + bizResult);
                        }
                    });
        }
    }
}
