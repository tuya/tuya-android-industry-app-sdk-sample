package com.tuya.iotapp.sample.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.space.api.ISpaceDevice;
import com.thingclips.iotapp.space.api.ISpaceDeviceListResult;
import com.thingclips.iotapp.space.api.SpaceService;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.adapter.DevicesSpaceAdapter;
import com.tuya.iotapp.sample.control.DeviceControlActivity;
import com.tuya.iotapp.sample.env.Constant;

/**
 * DevicesInSpaceActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:46 PM
 */
public class DevicesInSpaceActivity extends AppCompatActivity implements DevicesSpaceAdapter.OnRecyclerItemClickListener {
    private static final int DEVICE_PAGE_SIZE = 20;
    private static final String TAG = "DevicesInSpaceActivity";

    private Context mContext;
    private String spaceId;

    private RecyclerView mRcList;
    private ProgressBar mProgressBar;
    private DevicesSpaceAdapter mAdapter;

    private boolean mHasNext = true;
    private boolean loading = false;
    private String mLastRowKey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_in_asset);
        initView();
        mContext = this;

        Intent intent = getIntent();
        if (intent != null) {
            spaceId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mRcList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() > mAdapter.getItemCount() - 20
                            && mHasNext) {
                        loadData();
                    }
                }
            }
        });
        mAdapter = new DevicesSpaceAdapter(mContext);
        mAdapter.setListener(this);
        mRcList.setAdapter(mAdapter);

        loadData();
    }

    private void initView() {
        mRcList = findViewById(R.id.rc_list);
        mProgressBar = findViewById(R.id.progress_bar);
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
        SpaceService.devices(spaceId, mLastRowKey, new IndustryValueCallBack<ISpaceDeviceListResult>() {
            @Override
            public void onSuccess(ISpaceDeviceListResult iSpaceDeviceListResult) {
                if (mAdapter != null) {
                    mLastRowKey = iSpaceDeviceListResult.getLastRowKey();
                    mHasNext = iSpaceDeviceListResult.getHasMore();
                    mProgressBar.setVisibility(View.GONE);
                    mRcList.setVisibility(View.VISIBLE);
                    mAdapter.setData(iSpaceDeviceListResult);
                    mAdapter.notifyDataSetChanged();
                    loading = false;
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(View view, ISpaceDevice deviceBean) {
        DeviceControlActivity.launch(mContext, deviceBean.getDeviceId());
    }

    @Override
    public void onItemLongClick(View view, ISpaceDevice deviceBean) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Confirm to remove or set the Device?")
                .setSingleChoiceItems(
                        new String[]{"reset", "remove"},
                        0,
                        (dialog, which) -> {
                            if (which == 0) {
                                resetDevice(deviceBean);
                            } else {
                                deleteDevice(deviceBean);
                            }
                            dialog.dismiss();
                        }
                );
        builder.show();
    }

    private void resetDevice(ISpaceDevice deviceBean) {
        DeviceService.resetFactory(deviceBean.getDeviceId(), new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
                loadData();
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(mContext, getString(R.string.failure) + ": " + s, Toast.LENGTH_SHORT).show();
                Log.i("Device operation error", "code" + i + ";msg:" + s);
            }
        });
    }

    private void deleteDevice(ISpaceDevice deviceBean) {
        DeviceService.remove(deviceBean.getDeviceId(), new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, R.string.success, Toast.LENGTH_SHORT).show();
                if (mAdapter.getItemCount() < DEVICE_PAGE_SIZE) {
                    mLastRowKey = "";
                }
                loadData();
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(mContext, getString(R.string.failure) + ": " + s, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "delete device: " + s);
            }
        });
    }
}
