package com.tuya.iotapp.sample.adapter;

import static com.tuya.iotapp.sample.env.Constant.INTENT_KEY_DEVICE_ID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thingclips.iotapp.asset.api.IAssetDevice;
import com.thingclips.iotapp.asset.api.IAssetDeviceListResult;
import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.BleToolService;
import com.thingclips.iotapp.device.api.DeviceService;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.device.api.IDeviceListener;
import com.thingclips.iotapp.device.api.bean.BleConnectBean;
import com.thingclips.iotapp.pair.api.ActivatorMode;
import com.thingclips.iotapp.pair.api.ActivatorService;
import com.thingclips.iotapp.pair.api.IActivator;
import com.thingclips.iotapp.pair.api.listener.IActivatorListener;
import com.thingclips.iotapp.pair.api.params.BLEWIFICloudActivatorParams;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.ota.OTAActivity;
import com.tuya.iotapp.sample.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DevicesAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceHolder> {

    private Context mContext;
    private List<IAssetDevice> mList;
    private HashMap<String, IDevice> tyDeviceHashMap = new HashMap<>();

    private OnRecyclerItemClickListener mListener;

    IDeviceListener deviceListener = new IDeviceListener() {
        @Override
        public void onDpUpdate(@Nullable String s, @Nullable String s1) {

        }

        @Override
        public void onRemoved(@Nullable String s) {
            for (int index = 0; index < mList.size(); index++) {
                IAssetDevice assetDevice = mList.get(index);
                if (assetDevice.getDeviceId().equals(s)) {
                    notifyItemRemoved(index);
                    mList.remove(index);
                }
            }
        }

        @Override
        public void onStatusChanged(@Nullable String s, boolean b) {

        }

        @Override
        public void onNetworkStatusChanged(@Nullable String s, boolean b) {

        }

        @Override
        public void onDevInfoUpdate(@Nullable String s) {
            for (int index = 0; index < mList.size(); index++) {
                IAssetDevice assetDevice = mList.get(index);
                if (assetDevice.getDeviceId().equals(s)) {
                    boolean isOnline = mList.get(index).isOnline();
                    assetDevice.setOnline(isOnline);
                    notifyItemChanged(index);
                }
            }
        }
    };

    public DevicesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ty_device, parent, false);
        DeviceHolder holder = new DeviceHolder(view);
        view.setOnClickListener(v -> {
            if (mListener != null) {
                int position = (int) view.getTag();
                mListener.onItemClick(view, mList.get(position));
            }
        });

        view.setOnLongClickListener(v -> {
            if (mListener != null) {
                int position = (int) view.getTag();
                mListener.onItemLongClick(view, mList.get(position));
            }
            return false;
        });

        //重命名设备名称
        holder.ivDeviceSet.setOnClickListener(v -> {
            IAssetDevice assetDeviceBean = mList.get(holder.getAdapterPosition());
            EditText etInput = (EditText) LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_ty_input_view, null, false);
            new MaterialAlertDialogBuilder(v.getContext())
                    .setView(etInput)
                    .setPositiveButton("ok", (dialog, which) -> {
                        String newName = etInput.getText().toString();
                        String deviceId = assetDeviceBean.getDeviceId();
                        DeviceService.rename(deviceId, newName, new IndustryCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(v.getContext(), "rename success", Toast.LENGTH_SHORT).show();
                                mList.get(holder.getAdapterPosition()).setDeviceName(newName);
                                notifyItemChanged(holder.getAdapterPosition());
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.e("Tuya", "error code:" + i + "; errorMsg:" + s);
                            }
                        });
                        dialog.dismiss();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });


        //设置信号强度
        holder.ivDeviceWifi.setOnClickListener(v -> {
            String deviceId = mList.get(holder.getAdapterPosition()).getDeviceId();
            DeviceService.load(deviceId, new IndustryValueCallBack<IDevice>() {
                @Override
                public void onSuccess(IDevice iDevice) {
                    if (iDevice != null) {
                        iDevice.getWifiSignalStrength(new IndustryValueCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                holder.tvDeviceWifi.setText(s);
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Toast.makeText(holder.itemView.getContext(), s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onError(int i, @NonNull String s) {

                }
            });
        });
        holder.ivWifiEnable.setOnClickListener(v -> {
            String deviceId = mList.get(holder.getAdapterPosition()).getDeviceId();
            showWifiCredentialsDialog(holder.itemView.getContext(), deviceId);
        });

        //ota 升级
        holder.ivDeviceOTA.setOnClickListener(v -> {
            String deviceId = mList.get(holder.getAdapterPosition()).getDeviceId();
            Intent intent = new Intent(v.getContext(), OTAActivity.class);
            intent.putExtra(INTENT_KEY_DEVICE_ID, deviceId);
            v.getContext().startActivity(intent);
        });
        return holder;
    }

    private void showWifiCredentialsDialog(Context context, String deviceId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_wifi_credentials, null);
        final EditText etSsid = dialogView.findViewById(R.id.etSsid);
        final EditText etPassword = dialogView.findViewById(R.id.etPassword);

        new AlertDialog.Builder(context)
                .setTitle(R.string.wifi_enter_title)
                .setView(dialogView)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    String ssid = etSsid.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    BLEWIFICloudActivatorParams params = new BLEWIFICloudActivatorParams.Builder()
                            .setSsid(ssid)
                            .setPwd(password)
                            .setDevId(deviceId)
                            .setTimeout(60 * 1000)
                            .build();

                    IActivator activator = ActivatorService.activator(ActivatorMode.BLE_WIFI_ENABLE);
                    activator.setParams(params);
                    activator.setListener(new IActivatorListener() {
                        @Override
                        public void onSuccess(@Nullable IDevice iDevice) {
                            ToastUtil.show(context, context.getString(R.string.success));
                        }

                        @Override
                        public void onError(@NonNull String s, @NonNull String s1) {
                            ToastUtil.show(context, context.getString(R.string.activator_error_msg) + "code:" + s + " msg:" + s1);
                        }
                    });
                    activator.start();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }
        IDevice device = tyDeviceHashMap.get(mList.get(position).getDeviceId());
        if (device != null && device.getMeta() != null && device.getMeta().containsKey("wifiEnable")) {
            Object wifiEnable = device.getMeta().get("wifiEnable");
            if (wifiEnable != null && wifiEnable.toString().equals("false")) {
                holder.ivWifiEnable.setVisibility(View.VISIBLE);
            } else {
                holder.ivWifiEnable.setVisibility(View.GONE);
            }
        } else {
            holder.ivWifiEnable.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
        holder.tvDeviceName.setText(mList.get(position).getDeviceName());
        holder.tvDeviceId.setText(mList.get(position).getDeviceId());
        if (device != null) {
            holder.tvOnline.setText(holder.itemView.getContext().getString(device.isOnline() ? R.string.device_online : R.string.device_offline));
        } else {
            holder.tvOnline.setText(holder.itemView.getContext().getString(mList.get(position).isOnline() ? R.string.device_online : R.string.device_offline));
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(IAssetDeviceListResult list) {
        for (IDevice device : tyDeviceHashMap.values()) {
            device.removeDeviceListener(deviceListener);
        }

        mList = list.getDevices();

        for (IAssetDevice device : mList) {
            DeviceService.load(device.getDeviceId(), new IndustryValueCallBack<IDevice>() {
                @Override
                public void onSuccess(IDevice iDevice) {
                    tyDeviceHashMap.put(iDevice.getDeviceId(), iDevice);
                    iDevice.addDeviceListener(deviceListener);
                    if (iDevice.getCapability() == 10) {
                        List<BleConnectBean> beans = new ArrayList<>();
                        BleConnectBean bleConnectBean = new BleConnectBean(
                                iDevice.getDeviceId(), // 设置 devId 属性
                                false, // 设置 directConnect 属性
                                0, // 设置 level 属性
                                30000, // 设置 scanTimeout 属性
                                false, // 设置 autoConnect 属性
                                null // 设置 extInfo 属性（可选，传入 null 表示没有 extInfo）
                        );
                        beans.add(bleConnectBean);
                        BleToolService.connectBleDevices(beans);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onError(int i, @NonNull String s) {

                }
            });
        }


        notifyDataSetChanged();
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView tvDeviceName;
        private TextView tvDeviceId;
        private TextView tvDeviceWifi;
        private ImageView ivDeviceSet;
        private ImageView ivDeviceWifi;
        private ImageView ivDeviceOTA;
        private ImageView ivWifiEnable;
        private TextView tvOnline;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            ivDeviceSet = itemView.findViewById(R.id.ivDeviceSet);
            ivDeviceWifi = itemView.findViewById(R.id.ivDeviceWifi);
            ivDeviceOTA = itemView.findViewById(R.id.ivDeviceOTA);
            ivWifiEnable = itemView.findViewById(R.id.ivWifiEnable);
            tvOnline = itemView.findViewById(R.id.tvOnline);

            ivDeviceSet.setVisibility(View.VISIBLE);

        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, IAssetDevice deviceBean);

        void onItemLongClick(View view, IAssetDevice deviceBean);
    }
}
