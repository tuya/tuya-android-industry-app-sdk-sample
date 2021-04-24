package com.tuya.dev.iotos.authScan;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tuya.dev.common.kv.KvGlobalManager;
import com.tuya.dev.iotos.LoginActivity;
import com.tuya.dev.iotos.MainManagerActivity;
import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.dev.network.accessToken.AccessTokenManager;

/**
 * First Auth Activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 10:50 AM
 */
public class AuthFirstActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!KvGlobalManager.getString(AuthConst.KEY).isEmpty()) {

            AuthManager.init(this, KvGlobalManager.getString(AuthConst.KEY));

            if (!TextUtils.isEmpty(AccessTokenManager.INSTANCE.getUid())) {
                startActivity(new Intent(this, MainManagerActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
        } else {
            setContentView(R.layout.activity_auth_first);

            findViewById(R.id.btnScan).setOnClickListener(v -> {
                new RxPermissions(AuthFirstActivity.this)
                        .request(Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) {
                                AuthScanActivity.launch(AuthFirstActivity.this);
                            }
                        });
            });
        }


    }
}
