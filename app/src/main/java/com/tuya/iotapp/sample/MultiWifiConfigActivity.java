package com.tuya.iotapp.sample;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.sample.present.IActivitorResultListener;
import com.tuya.iotapp.sample.present.WifiConfigurationPresenter;

/**
 * MultiWifiConfigActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class MultiWifiConfigActivity extends AppCompatActivity implements IActivitorResultListener {

    private WifiConfigurationPresenter multiPresenter;

    private ProgressBar progressBar;
    private TextView mTvDeviceInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_config);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);

        progressBar.setVisibility(View.VISIBLE);

        multiPresenter = new WifiConfigurationPresenter(this, getIntent());
        multiPresenter.setActivityResultListener(this);
        multiPresenter.startConfig();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (multiPresenter != null) {
            multiPresenter.stopConfig();
            multiPresenter = null;
        }
    }

    @Override
    public void onActivitySuccessDevice(String successDevice){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                mTvDeviceInfo.setText(successDevice);
                mTvDeviceInfo.setVisibility(View.VISIBLE);
            }
        });
    }
}
