package com.tuya.iotapp.sample.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.asset.bean.AssetDeviceBean;
import com.tuya.iotapp.device.api.TYDeviceManager;
import com.tuya.iotapp.device.bean.DeviceBean;
import com.tuya.iotapp.device.bean.DeviceFunctionsBean;
import com.tuya.iotapp.device.bean.DeviceStatusBean;
import com.tuya.iotapp.device.bean.FunctionBean;
import com.tuya.iotapp.jsonparser.api.JsonParser;
import com.tuya.iotapp.network.response.ResultListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.adapter.DeviceControlerAdapter;
import com.tuya.iotapp.sample.env.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DeviceControlerActivity
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/14 4:08 PM
 */
public class DeviceControllerActivity extends AppCompatActivity implements DeviceControlerAdapter.OnRecyclerItemClickListener{
    private String mDeviceId;
    private String mCategory;

    private TextView mTvDeviceName;
    private TextView mTvOnline;
    private RecyclerView mRcList;

    private Context mContext;
    private DeviceControlerAdapter mControlerAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.arg1 == 1) {
                DeviceBean bizResult = (DeviceBean)msg.obj;
                StringBuilder nameBuilder = new StringBuilder();
                nameBuilder.append(getString(R.string.device_name));
                nameBuilder.append(bizResult.getName());
                mTvDeviceName.setText(nameBuilder.toString());
                StringBuilder onlineBuilder = new StringBuilder();
                onlineBuilder.append(getString(R.string.device_online));
                onlineBuilder.append(bizResult.getOnline());
                mTvOnline.setText(onlineBuilder.toString());
                mCategory = bizResult.getCategory();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_controler);
        mContext = this;
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            mDeviceId = intent.getStringExtra(Constant.INTENT_KEY_DEVICE_ID);
        }
        mControlerAdapter = new DeviceControlerAdapter(mContext);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRcList.setLayoutManager(layoutManager);
        mControlerAdapter.setListener(this);
        mRcList.setAdapter(mControlerAdapter);

        loadDeviceInfo();

        loadDeviceControler();
    }

    private void initView() {
        mTvDeviceName = (TextView) findViewById(R.id.tv_device_name);
        mTvOnline = (TextView) findViewById(R.id.tv_online);

        mRcList = (RecyclerView) findViewById(R.id.rc_controler);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void loadDeviceInfo() {
        TYDeviceManager.Companion.getDeviceBusiness().getDeviceInfo(mDeviceId, new ResultListener<DeviceBean>() {
            @Override
            public void onFailure(String s, String s1) {
                Toast.makeText(mContext, "query deviceInfo error" + s1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(DeviceBean deviceBean) {
                if (deviceBean == null) {
                    return;
                }
                Message msg  = new Message();
                msg.arg1 = 1;
                msg.obj = deviceBean;
                handler.dispatchMessage(msg);
            }
        });
    }

    private void loadDeviceControler() {
        TYDeviceManager.Companion.getDeviceBusiness().queryCommandSetWithDeviceId(mDeviceId, new ResultListener<DeviceFunctionsBean>() {
            @Override
            public void onFailure(String s, String s1) {
                Toast.makeText(mContext, "query functions error" + s1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(DeviceFunctionsBean deviceFunctionsBean) {
                if (deviceFunctionsBean == null ||  deviceFunctionsBean.getFunctions() == null || deviceFunctionsBean.getFunctions().length == 0) {
                    return;
                }
                mControlerAdapter.setData(Arrays.asList(deviceFunctionsBean.getFunctions()));
            }
        });
    }

    @Override
    public void onItemClick(View view, FunctionBean functionBean) {

    }

    @Override
    public void onItemLongClick(View view, FunctionBean functionBean) {
        showModifyCommandDialog(functionBean);
    }

    private void showModifyCommandDialog(FunctionBean functionBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(this, R.layout.item_command_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        TextView tvName = view.findViewById(R.id.tv_command_title);
        EditText editText = view.findViewById(R.id.et_command_modify);
        tvName.setText(functionBean.getName());

        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dialog.cancel();
        });

        view.findViewById(R.id.tv_confirm).setOnClickListener(v->{
            String commandString = editText.getText().toString();
            Object command = null;
            if ("Boolean".equals(functionBean.getType())) {
                command = Boolean.valueOf(commandString);
            } else if ("Integer".equals(functionBean.getType())) {
                command = Integer.valueOf(commandString);
            } else {
                command = commandString;
            }

            List<DeviceStatusBean> commandList = new ArrayList<>();
            DeviceStatusBean bean = new DeviceStatusBean(functionBean.getCode(), command);
            commandList.add(bean);
            TYDeviceManager.Companion.getDeviceBusiness().sendCommands(mDeviceId, commandList, new ResultListener<Boolean>() {
                @Override
                public void onFailure(String s, String s1) {
                    Toast.makeText(mContext, "send command fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    Toast.makeText(mContext, "send command success", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

}
