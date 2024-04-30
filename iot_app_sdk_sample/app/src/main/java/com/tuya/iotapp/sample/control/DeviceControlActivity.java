package com.tuya.iotapp.sample.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.device.api.IDeviceListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.adapter.DeviceControlAdapter;

/**
 * @author <a href="mailto:sunrw@tuya.com">乾启</a>
 * @since 2022/1/21 11:47 AM
 */
public class DeviceControlActivity extends AppCompatActivity {
    private static final String DEVICE_ID = "deviceId";

    private RecyclerView rvDp;
    private IDevice mDevice;
    private IDeviceListener listener = new IDeviceListener() {
        @Override
        public void onDpUpdate(@Nullable String s, @Nullable String s1) {
            rvDp.setAdapter(new DeviceControlAdapter(mDevice));
        }

        @Override
        public void onRemoved(@Nullable String s) {

        }

        @Override
        public void onStatusChanged(@Nullable String s, boolean b) {

        }

        @Override
        public void onNetworkStatusChanged(@Nullable String s, boolean b) {

        }

        @Override
        public void onDevInfoUpdate(@Nullable String s) {

        }
    };

    public static void launch(Context context, String deviceId) {
        Intent intent = new Intent(context, DeviceControlActivity.class);
        intent.putExtra(DEVICE_ID, deviceId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_device_control);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        rvDp = findViewById(R.id.rvDp);
        rvDp.setLayoutManager(new LinearLayoutManager(this));

        String deviceId = getIntent().getStringExtra(DEVICE_ID);
        loadData(deviceId);
    }

    private void loadData(String deviceId) {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    Toast.makeText(DeviceControlActivity.this, "加载设备成功：" + iDevice.getDeviceId(), Toast.LENGTH_SHORT).show();
                    rvDp.setAdapter(new DeviceControlAdapter(iDevice));
                    setupSync(iDevice);
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(DeviceControlActivity.this, "加载设备失败：" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSync(IDevice device) {
        this.mDevice = device;
        device.addDeviceListener(listener);
    }

    @Override
    protected void onDestroy() {
        if (mDevice != null) {
            mDevice.removeDeviceListener(listener);
        }
        super.onDestroy();
    }
}
