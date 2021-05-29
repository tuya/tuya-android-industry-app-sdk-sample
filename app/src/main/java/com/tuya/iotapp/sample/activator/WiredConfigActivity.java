package com.tuya.iotapp.sample.activator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.activator.bean.ErrorDeviceBean;
import com.tuya.iotapp.activator.bean.RegistrationTokenBean;
import com.tuya.iotapp.activator.bean.SuccessDeviceBean;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.IoTCommonUtil;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.activator.adapter.ActivatorErrorDeviceAdapter;
import com.tuya.iotapp.sample.activator.adapter.ActivatorSuccessDeviceAdapter;
import com.tuya.iotapp.sample.activator.presenter.IActivatorResultListener;
import com.tuya.iotapp.sample.activator.presenter.WiredConfigPresenter;
import com.tuya.iotapp.sample.env.Constant;

import java.util.List;

/**
 * @description: WiredConfigActivity
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/15/21 10:28 AM
 */
public class WiredConfigActivity extends AppCompatActivity implements IActivatorResultListener {

    private WiredConfigPresenter wriedPresenter;

    private Button mBtnStartActivator;
    private ProgressBar progressBar;
    private TextView mTvSuccessDeviceInfo;
    private TextView mTvErrorDeviceInfo;
    private RecyclerView mRcSuccessDeviceList;
    private RecyclerView mRcErrorDeviceList;
    private ActivatorSuccessDeviceAdapter successDeviceAdapter;
    private ActivatorErrorDeviceAdapter errorDeviceAdapter;

    private String mUid;
    private String mAssetId;
    private String mToken; //配网令牌token
    private String mRegion;//配网令牌region
    private String mSecret;//配网令牌secret

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wired_config);
        initView();

        Intent intent = getIntent();
        if (intent != null) {
            mUid = intent.getStringExtra(Constant.INTENT_KEY_UID);
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }

        RecyclerView.LayoutManager successLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcSuccessDeviceList.setLayoutManager(successLayoutManager);
        RecyclerView.LayoutManager errorLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcErrorDeviceList.setLayoutManager(errorLayoutManager);

        successDeviceAdapter = new ActivatorSuccessDeviceAdapter(this);
        errorDeviceAdapter = new ActivatorErrorDeviceAdapter(this);

        mRcSuccessDeviceList.setAdapter(successDeviceAdapter);
        mRcErrorDeviceList.setAdapter(errorDeviceAdapter);
    }

    private void initView() {
        mBtnStartActivator = (Button) findViewById(R.id.btn_start_activator);
        mBtnStartActivator.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            registrationToken();
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvSuccessDeviceInfo = (TextView) findViewById(R.id.tv_device_success_info);
        mTvErrorDeviceInfo = (TextView) findViewById(R.id.tv_device_error_info);

        mRcSuccessDeviceList = (RecyclerView) findViewById(R.id.rc_activator_success_result);
        mRcErrorDeviceList = (RecyclerView) findViewById(R.id.rc_activator_error_result);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void registrationToken() {
        TYActivatorManager.getActivator().getRegistrationToken(mAssetId,
                mUid,
                Constant.CONFIG_TYPE_EZ,// EZ/AP Activator
                IoTCommonUtil.getTimeZoneId(),
                "",
                new ResultListener<RegistrationTokenBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        L.d("registrationToken", "onFail : " + s1);
                        Toast.makeText(getApplicationContext(), "activator token get fail：" + s1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(RegistrationTokenBean registrationTokenBean) {
                        try {
                            mRegion = registrationTokenBean.getRegion();
                            mToken = registrationTokenBean.getToken();
                            mSecret = registrationTokenBean.getSecret();
                            L.d("registrationToken", "get activator token success");

                            // Start Activator
                            Intent intent = new Intent();
                            intent.putExtra(Constant.INTENT_KEY_REGION, mRegion);
                            intent.putExtra(Constant.INTENT_KEY_TOKEN, mToken);
                            intent.putExtra(Constant.INTENT_KEY_SECRET, mSecret);
                            wriedPresenter = new WiredConfigPresenter(WiredConfigActivity.this, intent);
                            wriedPresenter.setActivatorResultListener(WiredConfigActivity.this);
                            wriedPresenter.startConfig();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wriedPresenter != null) {
            wriedPresenter.stopConfig();
            wriedPresenter = null;
        }
    }

    @Override
    public void onActivatorSuccessDevice(List<SuccessDeviceBean> successDevices) {
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
    public void onActivatorErrorDevice(List<ErrorDeviceBean> errorDevices) {
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
