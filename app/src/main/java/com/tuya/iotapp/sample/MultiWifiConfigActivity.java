package com.tuya.iotapp.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.devices.bean.ErrorDeviceBean;
import com.tuya.iotapp.devices.bean.SuccessDeviceBean;
import com.tuya.iotapp.sample.adapter.ActivitorErrorDeviceAdapter;
import com.tuya.iotapp.sample.adapter.ActivitorSuccessDeviceAdapter;
import com.tuya.iotapp.sample.present.IActivitorResultListener;
import com.tuya.iotapp.sample.present.WifiConfigurationPresenter;

import java.util.List;

/**
 * MultiWifiConfigActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class MultiWifiConfigActivity extends AppCompatActivity implements IActivitorResultListener {

    private WifiConfigurationPresenter multiPresenter;

    private ProgressBar progressBar;
    private TextView mTvSuccessDeviceInfo;
    private TextView mTvErrorDeviceInfo;
    private RecyclerView mRcSuccessDeviceList;
    private RecyclerView mRcErrorDeviceList;
    private ActivitorSuccessDeviceAdapter successDeviceAdapter;
    private ActivitorErrorDeviceAdapter errorDeviceAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_config);
        initView();

        multiPresenter = new WifiConfigurationPresenter(this, getIntent());
        multiPresenter.setActivityResultListener(this);
        multiPresenter.startConfig();

        RecyclerView.LayoutManager successLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcSuccessDeviceList.setLayoutManager(successLayoutManager);
        RecyclerView.LayoutManager errorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcErrorDeviceList.setLayoutManager(errorLayoutManager);

        successDeviceAdapter = new ActivitorSuccessDeviceAdapter(this);
        errorDeviceAdapter = new ActivitorErrorDeviceAdapter(this);

        mRcSuccessDeviceList.setAdapter(successDeviceAdapter);
        mRcErrorDeviceList.setAdapter(errorDeviceAdapter);
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvSuccessDeviceInfo = (TextView) findViewById(R.id.tv_device_success_info);
        mTvErrorDeviceInfo = (TextView) findViewById(R.id.tv_device_error_info);
        progressBar.setVisibility(View.VISIBLE);

        mRcSuccessDeviceList = (RecyclerView) findViewById(R.id.rc_activitor_success_result);
        mRcErrorDeviceList = (RecyclerView) findViewById(R.id.rc_activitor_error_result);
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
    public void onActivitySuccessDevice(List<SuccessDeviceBean> successDevices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if (successDevices != null && successDevices.size() > 0
                       && successDeviceAdapter != null) {
                   progressBar.setVisibility(View.GONE);
                   mTvSuccessDeviceInfo.setVisibility(View.VISIBLE);
                   mRcSuccessDeviceList.setVisibility(View.VISIBLE);
                   successDeviceAdapter.setData(successDevices);
               }
            }
        });
    }

    @Override
    public void onActivityErrorDevice(List<ErrorDeviceBean> errorDevices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (errorDevices != null && errorDevices.size() > 0
                        && errorDeviceAdapter != null) {
                    progressBar.setVisibility(View.GONE);
                    mTvErrorDeviceInfo.setVisibility(View.VISIBLE);
                    mRcErrorDeviceList.setVisibility(View.VISIBLE);
                    errorDeviceAdapter.setData(errorDevices);
                }
            }
        });
    }
}
