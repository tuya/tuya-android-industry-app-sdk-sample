package com.tuya.dev.iotos.devices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.devices.adapter.DevicesAdapter;
import com.tuya.dev.iotos.devices.bean.DeviceListBean;
import com.tuya.dev.iotos.env.Constant;
import com.tuya.dev.iotos.log.LogUtils;
import com.tuya.dev.iotos.view.DividerDecoration;
import com.tuya.iotapp.asset.api.TYAssetManager;
import com.tuya.iotapp.asset.bean.AssetDeviceBean;
import com.tuya.iotapp.asset.bean.AssetDeviceListBean;
import com.tuya.iotapp.device.api.TYDeviceManager;
import com.tuya.iotapp.network.api.TYNetworkManager;
import com.tuya.iotapp.network.request.IRequest;
import com.tuya.iotapp.network.request.TYRequest;
import com.tuya.iotapp.network.response.ResultListener;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * DevicesInAssetActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:46 PM
 */
public class DevicesInAssetActivity extends AppCompatActivity implements DevicesAdapter.OnRecyclerItemClickListener {

    private Context mContext;
    private String assetId;


    private RecyclerView mRcList;
    private DevicesAdapter mAdapter;
    private String lastKey = "";

    private static final String DEVICES_API = "/v1.0/iot-03/devices";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_in_asset);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            assetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mAdapter = new DevicesAdapter(mContext);
        mAdapter.setListener(this);
        mRcList.setAdapter(mAdapter);
        DividerDecoration dividerItemDecoration = new DividerDecoration(new InsetDrawable(ContextCompat.getDrawable(this, R.drawable.bg_tuya_divider),
                48,
                0,
                48,
                0));
        mRcList.addItemDecoration(dividerItemDecoration);

        loadData();
    }

    private void initView() {
        mRcList = findViewById(R.id.rc_list);
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
    }

    private void loadData() {
        TYAssetManager.Companion.getAssetBusiness().queryDevicesByAssetId(assetId,
                lastKey,
                20,
                new ResultListener<AssetDeviceListBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(AssetDeviceListBean assetDeviceListBean) {
                        if (mAdapter != null) {
                            mAdapter.setData(assetDeviceListBean);
                            lastKey = assetDeviceListBean.getLastRowKey();

                            String deviceIds = assetDeviceListBean.getList().stream()
                                    .map(bean -> bean.getDeviceId())
                                    .collect(Collectors.joining(","));

                            HashMap<String, String> params = new HashMap<>();
                            params.put("device_ids", deviceIds);
                            TYNetworkManager.Companion.newRequest(
                                    new TYRequest(
                                            IRequest.GET,
                                            DEVICES_API,
                                            null,
                                            params
                                    )
                            ).asyncRequest(DeviceListBean.class,
                                    new ResultListener<DeviceListBean>() {
                                        @Override
                                        public void onFailure(String s, String s1) {

                                        }

                                        @Override
                                        public void onSuccess(DeviceListBean deviceListBean) {
                                            HashMap<String, String> iconMap = new HashMap<>();
                                            for (DeviceListBean.ListBean bean : deviceListBean.getList()) {
                                                iconMap.put(bean.getId(), bean.getIcon());
                                            }
                                            mAdapter.getDeviceIconsMap().putAll(iconMap);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, AssetDeviceBean deviceBean) {

    }

    @Override
    public void onItemLongClick(View view, AssetDeviceBean deviceBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.device_remove_tips))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDevice(deviceBean);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    private void deleteDevice(AssetDeviceBean deviceBean) {
        TYDeviceManager.Companion.getDeviceBusiness().removeDevice(deviceBean.getDeviceId(),
                new ResultListener<Boolean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        LogUtils.d("delete device", s1);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
                        mAdapter.getList().remove(deviceBean);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
