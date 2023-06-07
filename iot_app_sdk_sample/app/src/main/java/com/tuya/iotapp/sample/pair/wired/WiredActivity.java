package com.tuya.iotapp.sample.pair.wired;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.common.IndustryDataCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.DiscoveryMode;
import com.thingclips.iotapp.pair.api.IDiscovery;
import com.thingclips.iotapp.pair.api.IDiscoveryDevice;
import com.thingclips.iotapp.pair.api.IWiredDevice;
import com.thingclips.iotapp.pair.api.listener.IDiscoveryListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.PairType;
import com.tuya.iotapp.sample.pair.result.WifiPairResultActivity;

/**
 * Wired Mode Pair
 *
 * @author 乾启 <a href="mailto:developer@tuya.com">Contact me.</a>
 * @since 2022/1/4 1:42 PM
 */
public class WiredActivity extends AppCompatActivity {
    private RecyclerView rvWiredDevice;
    private WiredDeviceAdapter adapter;

    private Toolbar mToolbar;
    private Button btnToken;
    private Button btnSearch;

    private String mAssetId;
    private String mActivatorToken;
    private IDiscovery discovery;
    private ProgressBar wireLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_wired_pair_input);


        Intent intent = getIntent();
        if (intent != null) {
            mAssetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }
        wireLoading = findViewById(R.id.wire_Loading);
        mToolbar = findViewById(R.id.topAppBar);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        rvWiredDevice = findViewById(R.id.rvWiredDevice);
        btnToken = findViewById(R.id.btnToken);
        btnToken.setOnClickListener(v -> getToken());

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            startSearch();
        });

        adapter = new WiredDeviceAdapter();
//        rvWiredDevice.setAdapter(adapter);
        rvWiredDevice.setLayoutManager(new LinearLayoutManager(this));

        adapter.setLaunchWiredPair(new IndustryValueCallBack<IWiredDevice>() {
            @Override
            public void onSuccess(IWiredDevice wiredDevice) {
                if (TextUtils.isEmpty(mActivatorToken)) {
                    Toast.makeText(WiredActivity.this, "token can not null", Toast.LENGTH_SHORT).show();
                    return;
                }
                WireGwDeviceManager.INSTANCE.saveIWiredDevice(wiredDevice);
                Intent wifiIntent;

                wifiIntent = new Intent(WiredActivity.this, WifiPairResultActivity.class);
                wifiIntent.putExtra(Constant.INTENT_KEY_ACTIVATOR_TOKEN, mActivatorToken);
                wifiIntent.putExtra(Constant.INTENT_KEY_CONFIG_TYPE, PairType.WIRED);
                startActivity(wifiIntent);
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }

    /**
     * 获取token
     */
    private void getToken() {
        ActivatorService.activatorToken(mAssetId, new IndustryDataCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mActivatorToken = s;
                Toast.makeText(WiredActivity.this, "activator token ：" + mActivatorToken, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull String s, @NonNull String s1) {
                Log.e("WiredActivity", "getToken error" + s + ", msg=" + s1);
            }
        });
    }

    /**
     * 搜索
     */
    private void startSearch() {
        wireLoading.setVisibility(View.VISIBLE);
        discovery = ActivatorService.discovery(DiscoveryMode.WIRED);
        discovery.setListener(iDiscoveryDevice -> {
            IWiredDevice wiredDevice = (IWiredDevice) iDiscoveryDevice;
            for (IWiredDevice device : adapter.getList()) {
                Log.e("WiredActivity", "wiredDevice startDiscovery222222");
                if (wiredDevice.getGWId().equals(device.getGWId())) {
                    return;
                }
            }

            wireLoading.setVisibility(View.GONE);
            adapter.getList().add(wiredDevice);
            rvWiredDevice.setAdapter(adapter);
            Log.e("WiredActivity", "wiredDevice startDiscovery333333");
            adapter.notifyDataSetChanged();
            discovery.stopDiscovery();
        });
        Log.e("WiredActivity", "wiredDevice startDiscovery111111");
        discovery.startDiscovery();
        Log.e("WiredActivity", "wiredDevice startDiscovery444444");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != discovery) {
            discovery.stopDiscovery();
        }
    }
}
