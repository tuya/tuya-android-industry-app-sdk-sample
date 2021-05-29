package com.tuya.iotapp.sample.devices;

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
import com.tuya.iotapp.jsonparser.api.JsonParser;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.activator.AddZigBeeSubDevActivity;
import com.tuya.iotapp.sample.adapter.DeviceZigBeeAdapter;
import com.tuya.iotapp.sample.env.Constant;

import java.util.List;

/**
 * @description: DevicesZigBeeActivity
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/15/21 11:28 AM
 */
public class DevicesZigBeeActivity extends AppCompatActivity implements DeviceZigBeeAdapter.OnRecyclerItemClickListener {

    private Context mContext;
    private String assetId;

    private RecyclerView mRcList;
    private ProgressBar mProgressBar;
    private DeviceZigBeeAdapter mAdapter;

    private boolean loading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_zigbee);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            assetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mAdapter = new DeviceZigBeeAdapter(mContext);
        mAdapter.setListener(this);
        mRcList.setAdapter(mAdapter);

        loadData();
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

    private void loadData() {
        if (loading) {
            return;
        }
        loading = true;
        TYActivatorManager.getActivator().queryRegistrationGateways(assetId, new ResultListener<List<GatewayBean>>() {
            @Override
            public void onFailure(String s, String s1) {
                Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                loading = false;
            }

            @Override
            public void onSuccess(List<GatewayBean> gatewayList) {
                L.d("gatewayList", JsonParser.toJsonString(gatewayList));
                if (mAdapter != null) {
                    mProgressBar.setVisibility(View.GONE);
                    mRcList.setVisibility(View.VISIBLE);
                    mAdapter.setData(gatewayList);
                    loading = false;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, GatewayBean deviceBean) {

    }

    @Override
    public void onItemLongClick(View view, GatewayBean deviceBean) {
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

    @Override
    public void onAddSubDeviceClick(View view, GatewayBean deviceBean) {
        Intent intent = new Intent(mContext, AddZigBeeSubDevActivity.class);
        intent.putExtra(Constant.INTENT_KEY_DEVICE_ID, deviceBean.getId());

        startActivity(intent);
    }

    private void deleteDevice(GatewayBean deviceBean) {
        TYDeviceManager.getDeviceBusiness().removeDevice(deviceBean.getId(), new ResultListener<Boolean>() {
            @Override
            public void onFailure(String s, String s1) {
                L.d("delete device", s1);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
    }
}
