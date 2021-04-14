package com.tuya.iotapp.sample.activator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.activator.bean.BLEScanObject;
import com.tuya.iotapp.activator.config.BleWifiConfigImpl;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.accessToken.AccessTokenManager;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.activator.adapter.BleDevicesAdapter;
import com.tuya.iotapp.sample.assets.AssetsManager;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.smart.sdk.config.ble.api.TuyaBleScanCallback;
import com.tuya.smart.sdk.config.ble.api.bean.BLEScanBean;

/**
 * BleWifiScanActivity
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/13 4:28 PM
 */
public class BleWifiScanActivity extends AppCompatActivity implements BleDevicesAdapter.OnRecyclerItemClickListener{
    private static final String TAG = "BleWifiScanActivity";
    private static final int BLE_SCAN_TIME_OUT = 60 * 1000;
    private Context mContext;
    private String countryCode;
    private String assetId;
    private String uid;

    private RecyclerView mRcList;
    private ProgressBar mProgressBar;
    private Button mBtnStartSearch;
    private Button mBtnStopSearch;
    private BleDevicesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan_devices);
        checkPermission();
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            countryCode = intent.getStringExtra(Constant.INTENT_KEY_COUNTRY_CODE);
            assetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
            uid = intent.getStringExtra(Constant.INTENT_KEY_UID);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mAdapter = new BleDevicesAdapter(mContext);
        mAdapter.setListener(this);
        mRcList.setAdapter(mAdapter);
        mBtnStartSearch.setOnClickListener(v -> {
            startBleScan();
        });

        mBtnStopSearch.setOnClickListener(v ->{

        });
    }

    private void initView() {
        mRcList = (RecyclerView) findViewById(R.id.rc_list);
        mBtnStartSearch = (Button) findViewById(R.id.btn_start_search);
        mBtnStopSearch = (Button) findViewById(R.id.btn_stop_search);
        mProgressBar =(ProgressBar) findViewById(R.id.progress_bar);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            stopBleScan();
            finish();
        });
    }

    private void startBleScan() {
        mProgressBar.setVisibility(View.VISIBLE);
        BleWifiConfigImpl.startScan(BLE_SCAN_TIME_OUT, new TuyaBleScanCallback() {
            @Override
            public void onResult(BLEScanBean bleScanBean) {
                if (bleScanBean == null) {
                    LogUtils.d(TAG, "ble scan onResult is null");
                    return;
                }
                mAdapter.setData(bleScanBean);
                mRcList.setVisibility(View.VISIBLE);
                LogUtils.d(TAG, "ble scan onResult success devUuid:"+bleScanBean.devUuId);
            }
        });
    }

    private void stopBleScan() {
        BleWifiConfigImpl.stopScan();
        if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivatorClick(BLEScanBean deviceBean) {
        BLEScanObject object = new BLEScanObject(deviceBean);
        Intent intent = new Intent(mContext, BleWifiConfigurationActivity.class);
        intent.putExtra(Constant.INTENT_KEY_ASSET_ID, assetId);
        intent.putExtra(Constant.INTENT_KEY_UID, uid);
        intent.putExtra(Constant.INTENT_KEY_COUNTRY_CODE, countryCode);
        intent.putExtra(Constant.INTENT_KEY_BLE_SCAN_BEAN, object);

        startActivity(intent);
    }

    // You need to check permissions before using Bluetooth devices
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length != 0 && grantResults[0] == 0) {
                Log.i("DeviceConfigBleActivity", "onRequestPermissionsResult: agree");
            } else {
                this.finish();
                Log.e("DeviceConfigBleActivity", "onRequestPermissionsResult: denied");
            }
        } else {
            throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopBleScan();
    }
}
