package com.tuya.iotapp.sample.ota;

import static com.tuya.iotapp.sample.env.Constant.INTENT_KEY_DEVICE_ID;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSONObject;
import com.thingclips.iotapp.common.IndustryDataCallBack;
import com.thingclips.iotapp.common.IndustryNormalCallback;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.device.api.IDeviceOtaManager;
import com.thingclips.iotapp.device.api.bean.DeviceUpgradeStatusBean;
import com.thingclips.iotapp.device.api.bean.FirmwareUpgradeInfo;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.utils.ToastUtil;

import java.util.List;

public class OTAActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = OTAActivity.class.getSimpleName();

    private Button btnCheck;
    private TextView tvUpgradeInfo;
    private LinearLayout layoutControl;
    private Button btnStart;
    private Button btnProgress;
    private Button btnCancel;
    private ProgressBar progressBar;
    private CardView cvSwitch;
    private SwitchCompat switchAutoUpgrade;

    private String deviceId;
    private List<FirmwareUpgradeInfo> mFirmwareUpgradeInfos;
    private int firmwareType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty_ota);

        deviceId = getIntent().getStringExtra(INTENT_KEY_DEVICE_ID);
        if (TextUtils.isEmpty(deviceId)) {
            ToastUtil.show(this, "deviceId is null!");
            finish();
        }

        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        btnCheck = findViewById(R.id.btnCheck);
        btnStart = findViewById(R.id.btnStart);
        btnProgress = findViewById(R.id.btnProgress);
        btnCancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.progressBar);
        tvUpgradeInfo = findViewById(R.id.tvUpgradeInfo);
        layoutControl = findViewById(R.id.layoutControl);
        cvSwitch = findViewById(R.id.cvSwitch);
        switchAutoUpgrade = findViewById(R.id.switchAutoUpgrade);
        btnCheck.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnProgress.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        cvSwitch.setOnClickListener(this);
    }

    private void initData() {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().fetchUpgradingInfo(new IndustryDataCallBack<DeviceUpgradeStatusBean>() {
                        @Override
                        public void onSuccess(DeviceUpgradeStatusBean deviceUpgradeStatusBean) {
                            if (deviceUpgradeStatusBean != null) {
                                switchAutoUpgrade.setChecked(deviceUpgradeStatusBean.getStatus() == 1);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull String s, @NonNull String s1) {
                            ToastUtil.show(OTAActivity.this, String.format("get auto upgrade state fail: code=%1$s, msg=%2$s", s, s1));

                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.btnCheck) {
            checkUpgradeInfo();
        } else if (viewId == R.id.btnStart) {
            startUpgrade();
        } else if (viewId == R.id.btnProgress) {
            getProgress();
        } else if (viewId == R.id.btnCancel) {
            cancelUpgrade();
        }
    }

    private void checkUpgradeInfo() {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().fetchFirmwareUpgradeInfo(new IndustryDataCallBack<List<FirmwareUpgradeInfo>>() {
                        @Override
                        public void onSuccess(List<FirmwareUpgradeInfo> firmwareUpgradeInfos) {
                            if (firmwareUpgradeInfos != null && firmwareUpgradeInfos.size() > 0) {
                                layoutControl.setVisibility(View.VISIBLE);
                                showUpgradeInfo(firmwareUpgradeInfos);
                                mFirmwareUpgradeInfos = firmwareUpgradeInfos;
                            } else {
                                layoutControl.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull String s, @NonNull String s1) {
                            String msg = String.format("checkUpdate failed: <%1$s, %2$s>", s, s1);
                            Log.d(TAG, msg);
                            ToastUtil.show(OTAActivity.this, msg);
                            layoutControl.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }

    /**
     * 展示设备升级信息
     *
     * @param firmwareUpgradeInfos
     */
    private void showUpgradeInfo(List<FirmwareUpgradeInfo> firmwareUpgradeInfos) {
        mFirmwareUpgradeInfos = firmwareUpgradeInfos;
        StringBuilder sb = new StringBuilder(getString(R.string.device_ota_upgrade_info_prefix));
        sb.append("\n[\n");
        for (FirmwareUpgradeInfo upgradeInfoBean : mFirmwareUpgradeInfos) {
            sb.append("{ version: ");
            sb.append(upgradeInfoBean.getVersion());
            sb.append(", type:");
            sb.append(upgradeInfoBean.getType());
            sb.append(", desc: ");
            sb.append(upgradeInfoBean.getDesc());
            sb.append(", can_upgrade: ");
            sb.append(upgradeInfoBean.getCanUpgrade());
            sb.append("},\n");
        }
        sb.append("]");
        tvUpgradeInfo.setText(sb.toString());
    }

    private void startUpgrade() {
        if (null == mFirmwareUpgradeInfos || mFirmwareUpgradeInfos.isEmpty()) {
            Toast.makeText(this, "Please get upgrade info first", Toast.LENGTH_SHORT).show();
            return;
        }
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().startOTA(mFirmwareUpgradeInfos);
                    iDevice.newOtaManager().registerOTAListener(deviceUpgradeStatusBean -> {
                        Log.d(TAG, "firmwareUpgradeStatus: " + JSONObject.toJSONString(deviceUpgradeStatusBean));
                        firmwareType = deviceUpgradeStatusBean.getFirmwareType();
                        switch (deviceUpgradeStatusBean.getStatus()) {
                            case 2:
                                progressBar.setProgress(deviceUpgradeStatusBean.getProgress());
                                break;
                            case 3:
                                upgradeSuccess();
                                break;
                            case 4:
                                upgradeFailed();
                                break;
                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }

    private void getProgress() {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().fetchUpgradingInfo(new IndustryDataCallBack<DeviceUpgradeStatusBean>() {
                        @Override
                        public void onSuccess(DeviceUpgradeStatusBean deviceUpgradeStatusBean) {
                            if (deviceUpgradeStatusBean != null) {
                                String msg = String.format("get upgrade progress success: %1$s", JSONObject.toJSONString(deviceUpgradeStatusBean.getProgress()));
                                Log.d(TAG, msg);
                                ToastUtil.show(OTAActivity.this, msg);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull String s, @NonNull String s1) {
                            String msg = String.format("get upgrade progress failed: <%1$s, %2$s>", s, s1);
                            Log.d(TAG, msg);
                            ToastUtil.show(OTAActivity.this, msg);

                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });

    }

    private void cancelUpgrade() {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().cancelOTA(firmwareType, new IndustryNormalCallback() {
                        @Override
                        public void onSuccess() {
                            String msg = "cancel upgrade success";
                            Log.d(TAG, msg);
                            ToastUtil.show(OTAActivity.this, msg);
                        }

                        @Override
                        public void onFailure(@NonNull String s, @NonNull String s1) {
                            String msg = String.format("cancel upgrade failed: <%1$s, %2$s>", s, s1);
                            Log.d(TAG, msg);
                            ToastUtil.show(OTAActivity.this, msg);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.d(TAG, s);

            }
        });

    }
    private void upgradeSuccess() {
        progressBar.setProgress(100);
        String msg = "upgrade success";
        Log.d(TAG, msg);
        ToastUtil.show(OTAActivity.this, msg);
    }

    private void upgradeFailed() {
        String msg = "upgrade failed";
        Log.d(TAG, msg);
        ToastUtil.show(OTAActivity.this, msg);
    }

    @Override
    protected void onDestroy() {
        DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                if (iDevice != null) {
                    iDevice.newOtaManager().cancelOTA(firmwareType, new IndustryNormalCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: 取消升级成功");

                        }

                        @Override
                        public void onFailure(@NonNull String s, @NonNull String s1) {
                            Log.d(TAG, "onSuccess: 取消升级失败");

                        }
                    });
                }
            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
        super.onDestroy();
    }

}
