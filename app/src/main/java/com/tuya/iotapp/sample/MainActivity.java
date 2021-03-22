package com.tuya.iotapp.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.sample.env.EnvUrlProvider;
import com.tuya.iotapp.sample.env.EnvUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnvUtils.setEnv(this, EnvUtils.ENV_PRE);
        IApiUrlProvider provider = new EnvUrlProvider(this);
        IotAppNetWork.initialize(getApplicationContext(), "vu4j8j38f45f3qht5gpk", "sgwf9mtw779hwfkkmpsphekgy9r7vtge", "Android", provider);

        TestBusiness business = new TestBusiness();
        business.test();
    }
}