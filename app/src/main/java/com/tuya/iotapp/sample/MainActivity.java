package com.tuya.iotapp.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.sample.env.EnvUrlProvider;
import com.tuya.iotapp.sample.env.EnvUtils;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EnvUtils.setEnv(this, EnvUtils.ENV_PRE);
        IApiUrlProvider provider = new EnvUrlProvider(this);
        IotAppNetWork.initialize(getApplicationContext(), "7ajcrgmsnm8xwe5tx83j", "pyyxvyrdqktw4rh7j759k5a9qxqxsddu", "Android", provider);

        TestBusiness business = new TestBusiness();
        business.test();
    }
}