package com.tuya.iotapp.sample.pair.subDevice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.thingclips.iotapp.asset.api.AssetService;
import com.thingclips.iotapp.asset.api.IAssetDeviceListResult;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.pair.api.ActivatorMode;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.DiscoveryMode;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.params.ZigbeeActivatorParams;
import com.thingclips.iotapp.pair.delegate.ZigbeeActivator;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.assets.AssetsActivity;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;
import com.tuya.iotapp.sample.pair.result.WifiPairResultActivity;

import java.util.List;

/**
 * Wired Mode Pair
 *
 * @author 乾启 <a href="mailto:developer@tuya.com">Contact me.</a>
 * @since 2022/1/4 1:42 PM
 */
public class SubDeviceActivity extends AppCompatActivity {
    private RecyclerView rvGateway;
    private SubDeviceAdapter adapter;

    private Toolbar mToolbar;
    private Button btnQuery;

    private String mAssetId;
    private ZigbeeActivator activator;
    private String gwDeviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_sub_pair_input);


        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            gwDeviceId = intent.getStringExtra(Constant.INTENT_KEY_DEVICE_ID);
        }

        mToolbar = findViewById(R.id.topAppBar);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        rvGateway = findViewById(R.id.rvGateway);
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(v -> {
            startQuery();
        });

        adapter = new SubDeviceAdapter();
        rvGateway.setAdapter(adapter);
        rvGateway.setLayoutManager(new LinearLayoutManager(this));
    }

    private void startQuery() {
        activator = (ZigbeeActivator) ActivatorService.activator(ActivatorMode.Zigbee);
        activator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                if (null != iDevice) {
                    adapter.getList().clear();
                    adapter.getList().add(iDevice);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SubDeviceActivity.this, "网关子设备配网成功：" + iDevice.getDeviceId(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                Toast.makeText(SubDeviceActivity.this, "网关子设备失败：" + s1, Toast.LENGTH_SHORT).show();

            }
        });
        // TODO: 2023/6/6 传入网关Id 
        ZigbeeActivatorParams zigbeeActivatorParams = new ZigbeeActivatorParams.Builder()
                .setGwDeviceId(gwDeviceId)
                .setTimeout(600000)
                .build();
        activator.setParams(zigbeeActivatorParams);
        activator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != activator) {
            activator.stop();
            activator.destroy();
        }
    }
}
