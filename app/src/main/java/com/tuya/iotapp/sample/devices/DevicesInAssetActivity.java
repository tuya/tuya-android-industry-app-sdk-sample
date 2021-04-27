package com.tuya.iotapp.sample.devices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.asset.api.TYAssetManager;
import com.tuya.iotapp.common.utils.L;
import com.tuya.iotapp.asset.bean.AssetDeviceBean;
import com.tuya.iotapp.asset.bean.AssetDeviceListBean;
import com.tuya.iotapp.device.api.TYDeviceManager;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.adapter.DevicesAdapter;
import com.tuya.iotapp.sample.env.Constant;

/**
 * DevicesInAssetActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:46 PM
 */
public class DevicesInAssetActivity extends AppCompatActivity implements DevicesAdapter.OnRecyclerItemClickListener{
    private static final int DEVICE_PAGE_SIZE = 20;

    private Context mContext;
    private String assetId;

    private RecyclerView mRcList;
    private ProgressBar mProgressBar;
    private DevicesAdapter mAdapter;

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
            assetId = intent.getStringExtra(Constant.INTENT_KEY_ASSET_ID);
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
        mAdapter = new DevicesAdapter(mContext);
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
        TYAssetManager.Companion.getAssetBusiness().queryDevicesByAssetId(assetId, mLastRowKey, DEVICE_PAGE_SIZE, new ResultListener<AssetDeviceListBean>() {
            @Override
            public void onFailure(String s, String s1) {
                Toast.makeText(mContext, s1, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(AssetDeviceListBean assetDeviceListBean) {
                if (mAdapter != null) {
                    mLastRowKey = assetDeviceListBean.getLastRowKey();
                    mHasNext = assetDeviceListBean.getHasNext();
                    mProgressBar.setVisibility(View.GONE);
                    mRcList.setVisibility(View.VISIBLE);
                    mAdapter.setData(assetDeviceListBean);
                    loading = false;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, AssetDeviceBean deviceBean) {
        Intent intent = new Intent(mContext, DeviceControllerActivity.class);
        intent.putExtra(Constant.INTENT_KEY_DEVICE_ID, deviceBean.getDeviceId());

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, AssetDeviceBean deviceBean) {
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

   private void deleteDevice(AssetDeviceBean deviceBean) {
        TYDeviceManager.Companion.getDeviceBusiness().removeDevice(deviceBean.getDeviceId(), new ResultListener<Boolean>() {
            @Override
            public void onFailure(String s, String s1) {
                L.Companion.d("delete device", s1);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(mContext, "delete success", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
   }
}
