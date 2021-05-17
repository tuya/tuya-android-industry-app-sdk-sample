package com.tuya.iotapp.sample.activator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.activator.bean.GatewayBean;
import com.tuya.iotapp.activator.config.TYActivatorManager;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.device.api.TYDeviceManager;
import com.tuya.iotapp.device.bean.SubDeviceBean;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.activator.presenter.IActivatorZBSubListener;
import com.tuya.iotapp.sample.activator.presenter.ZigBeeSubConfigPresenter;
import com.tuya.iotapp.sample.adapter.DeviceZigBeeAdapter;
import com.tuya.iotapp.sample.adapter.DeviceZigBeeSubAdapter;
import com.tuya.iotapp.sample.devices.DeviceControllerActivity;
import com.tuya.iotapp.sample.env.Constant;

import java.util.List;

/**
 * @description: AddZigBeeSubDevActivity
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/15/21 11:28 AM
 */
public class AddZigBeeSubDevActivity extends AppCompatActivity implements DeviceZigBeeSubAdapter.OnRecyclerItemClickListener, IActivatorZBSubListener {

    private Context mContext;
    private String mDeviceId;
    private Long mDiscoveryTime;

    private RecyclerView mRcList;
    private ProgressBar mProgressBar;
    private DeviceZigBeeSubAdapter mAdapter;

    private ZigBeeSubConfigPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_zigbee_sub);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            mDeviceId = intent.getStringExtra(Constant.INTENT_KEY_DEVICE_ID);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mAdapter = new DeviceZigBeeSubAdapter(mContext);
        mAdapter.setListener(this);
        mRcList.setAdapter(mAdapter);

        discoverSubDev();
    }

    private void initView() {
        mRcList = (RecyclerView) findViewById(R.id.rc_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void discoverSubDev() {
        TYActivatorManager.Companion.getActivator().discoverSubDevices(mDeviceId, 100, new ResultListener<Boolean>() {
            @Override
            public void onFailure(String s, String s1) {
                Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mDiscoveryTime = System.currentTimeMillis() / 1000;
                    getSubDevices();
                } else {
                    Toast.makeText(mContext, "Discover sub devices failed", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getSubDevices() {
        mPresenter = new ZigBeeSubConfigPresenter(mDeviceId, mDiscoveryTime);
        mPresenter.setActivatorZBSubListener(this);
        mPresenter.startLoop();
    }

    @Override
    public void onItemClick(View view, SubDeviceBean deviceBean) {
        Intent intent = new Intent(mContext, DeviceControllerActivity.class);
        intent.putExtra(Constant.INTENT_KEY_DEVICE_ID, deviceBean.getId());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, SubDeviceBean deviceBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm to remove the Device?")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDevice(deviceBean);
                    }
                })
                .setNegativeButton("cancel", null);
        builder.show();
    }

    private void deleteDevice(SubDeviceBean deviceBean) {
        TYDeviceManager.Companion.getDeviceBusiness().removeDevice(deviceBean.getId(), new ResultListener<Boolean>() {
            @Override
            public void onFailure(String s, String s1) {
                L.Companion.d("delete device", s1);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivatorSuccessDevice(List<SubDeviceBean> subDeviceList) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setData(subDeviceList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.stopLoop();
        }
    }
}
