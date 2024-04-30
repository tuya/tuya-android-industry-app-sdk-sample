package com.tuya.iotapp.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thingclips.iotapp.asset.api.AssetService;
import com.thingclips.iotapp.asset.api.IAssetDevice;
import com.thingclips.iotapp.asset.api.IAssetDeviceListResult;
import com.thingclips.iotapp.basis.user.api.IUser;
import com.thingclips.iotapp.basis.user.api.UserService;
import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.DeviceAssistToolExKt;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.network.interceptor.token.AccessTokenManager;
import com.thingclips.iotapp.space.api.ISpaceDevice;
import com.thingclips.iotapp.space.api.ISpaceDeviceListResult;
import com.thingclips.iotapp.space.api.SpaceService;
import com.tuya.iotapp.sample.assets.AssetsActivity;
import com.tuya.iotapp.sample.assets.AssetsManager;
import com.tuya.iotapp.sample.devices.DevicesInAssetActivity;
import com.tuya.iotapp.sample.devices.DevicesInSpaceActivity;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;
import com.tuya.iotapp.sample.pair.ble.BleScanActivity;
import com.tuya.iotapp.sample.pair.input.WifiPairInputActivity;
import com.tuya.iotapp.sample.pair.scan.ScanActivity;
import com.tuya.iotapp.sample.pair.subDevice.SubDeviceActivity;
import com.tuya.iotapp.sample.pair.wired.WiredActivity;
import com.tuya.iotapp.sample.space.SpacesActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainManagerActivity extends AppCompatActivity {
    private TextView tvUserId;
    private TextView tvCurrentAsset;
    private Button btnAssets;
    private Button btnPairAP;
    private Button btnPairEZ;
    private Button btnPairQR;
    private Button btnPairWired;
    private Button btnPairSubDevice;
    private Button btnPairQRCodeScan;
    private Button btnPairBle;

    private Button btnDeviceList;
    private Button btnLogout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_main_manager);
        initView(this);

        try {
            String[] list = getResources().getAssets().list("META-INF");
            Log.i("manage", list.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mContext = this;

        String uid = AccessTokenManager.INSTANCE.getUid();
        tvUserId.setText("UserId : " + uid);

        btnPairAP.setOnClickListener(v -> startWifiConfig(PairType.AP));
        btnPairEZ.setOnClickListener(v -> startWifiConfig(PairType.EZ));
        btnPairQR.setOnClickListener(v -> {
            startWifiConfig(PairType.QR);
        });

        btnDeviceList.setOnClickListener(v -> startDeviceList());

        btnLogout.setOnClickListener(v -> {
            UserService.logout(new IndustryCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(v.getContext(), "logout success", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(v.getContext(), "logout fail", Toast.LENGTH_SHORT).show();

                }
            });
        });

        btnPairWired.setOnClickListener(v -> {
            Intent wiredIntent = new Intent(mContext, WiredActivity.class);
            wiredIntent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
            startActivity(wiredIntent);
        });

        btnPairSubDevice.setOnClickListener(v -> {
            showGatewayDeviceDialog();
        });

        btnPairQRCodeScan.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ScanActivity.class);
            intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
            startActivity(intent);
        });

        btnPairBle.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, BleScanActivity.class);
            intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
            startActivity(intent);
        });
    }

    private void initView(Context context) {
        tvUserId = findViewById(R.id.tvUserId);
        tvCurrentAsset = findViewById(R.id.tvCurrentAsset);
        btnAssets = findViewById(R.id.btnAssets);
        btnPairAP = findViewById(R.id.btnPairAP);
        btnPairEZ = findViewById(R.id.btnPairEZ);
        btnPairQR = findViewById(R.id.btnPairQR);
        btnPairWired = findViewById(R.id.btnPairWired);
        btnPairSubDevice = findViewById(R.id.btnPairSubDevice);
        btnPairQRCodeScan = findViewById(R.id.btnPairQRCodeScan);
        btnPairBle = findViewById(R.id.btnPairBle);

        btnDeviceList = findViewById(R.id.btnDeviceList);
        btnLogout = findViewById(R.id.btnLogout);


        Toolbar toolbar = findViewById(R.id.topAppBar);

        btnAssets.setOnClickListener(v -> {
            IUser user = UserService.user();
            if (user != null && user.getSpaceType() == 1) {
                SpacesActivity.launch(v.getContext(), "", getString(R.string.spaces_title));
            } else {
                AssetsActivity.launch(v.getContext(),
                        "",
                        getString(R.string.assets_title));
            }
        });

        if (BuildConfig.DEBUG) {
            btnAssets.setOnLongClickListener(v -> {
                return true;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvCurrentAsset.setText(String.format("ID：%s", AssetsManager.INSTANCE.getAssetId()));
    }

    private void startWifiConfig(String configType) {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, WifiPairInputActivity.class);
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
        intent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, configType);

        startActivity(intent);
    }


    private void startDeviceList() {
        if (!hasCurrentAssetId()) {
            return;
        }
        Intent intent = new Intent(mContext, DevicesInAssetActivity.class);
        IUser user = UserService.user();
        if (user != null && user.getSpaceType() == 1) {
            intent = new Intent(mContext, DevicesInSpaceActivity.class);
        }
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());

        startActivity(intent);
    }

    private boolean hasCurrentAssetId() {
        if (TextUtils.isEmpty(AssetsManager.INSTANCE.getAssetId())) {
            Toast.makeText(mContext, "please choose current asset", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void showGatewayDeviceDialog() {
        if (TextUtils.isEmpty(AssetsManager.INSTANCE.getAssetId())) {
            return;
        }
        if (UserService.user() != null && UserService.user().getSpaceType() == 1) {
            SpaceService.devices(AssetsManager.INSTANCE.getAssetId(), null, new IndustryValueCallBack<ISpaceDeviceListResult>() {
                @Override
                public void onSuccess(ISpaceDeviceListResult iAssetDeviceListResult) {
                    List<String> list = new ArrayList<>();
                    for (ISpaceDevice assetDevice : iAssetDeviceListResult.getDevices()) {
                        IDevice device = DeviceService.device(assetDevice.getDeviceId());
                        // device may be null, use DeviceService.load() to fetch remotely.
                        if (device != null && DeviceAssistToolExKt.hasConfigZigbee(device)) {
                            list.add(device.getDeviceId());
                        }
                    }
                    if (list.size() == 0) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainManagerActivity.this);
                    builder.setTitle("choose gateway");
                    builder.setAdapter(new ArrayAdapter<>(MainManagerActivity.this, android.R.layout.simple_list_item_1, list), (dialog, which) -> {
                        String deviceId = list.get(which);
                        Intent intent = new Intent(mContext, SubDeviceActivity.class);
                        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
                        intent.putExtra(Constant.INTENT_KEY_DEVICE_ID, deviceId);
                        startActivity(intent);
                        dialog.dismiss();
                    });
                    builder.show();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AssetService.devices(AssetsManager.INSTANCE.getAssetId(), null, new IndustryValueCallBack<IAssetDeviceListResult>() {
                @Override
                public void onSuccess(IAssetDeviceListResult iAssetDeviceListResult) {
                    List<String> list = new ArrayList<>();
                    for (IAssetDevice assetDevice : iAssetDeviceListResult.getDevices()) {
                        IDevice device = DeviceService.device(assetDevice.getDeviceId());
                        // device may be null, use DeviceService.load() to fetch remotely.
                        if (device != null && DeviceAssistToolExKt.hasConfigZigbee(device)) {
                            list.add(device.getDeviceId());
                        }
                    }
                    if (list.size() == 0) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainManagerActivity.this);
                    builder.setTitle("choose gateway");
                    builder.setAdapter(new ArrayAdapter<>(MainManagerActivity.this, android.R.layout.simple_list_item_1, list), (dialog, which) -> {
                        String deviceId = list.get(which);
                        Intent intent = new Intent(mContext, SubDeviceActivity.class);
                        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, AssetsManager.INSTANCE.getAssetId());
                        intent.putExtra(Constant.INTENT_KEY_DEVICE_ID, deviceId);
                        startActivity(intent);
                        dialog.dismiss();
                    });
                    builder.show();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}