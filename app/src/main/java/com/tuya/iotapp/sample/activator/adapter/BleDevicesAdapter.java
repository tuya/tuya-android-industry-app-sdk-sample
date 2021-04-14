package com.tuya.iotapp.sample.activator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.devices.bean.AssetDeviceBean;
import com.tuya.iotapp.devices.bean.AssetDeviceListBean;
import com.tuya.iotapp.sample.R;
import com.tuya.smart.sdk.config.ble.api.bean.BLEScanBean;

import java.util.ArrayList;
import java.util.List;

/**
 * BleDevicesAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class BleDevicesAdapter extends RecyclerView.Adapter<BleDevicesAdapter.DeviceHolder> {

    private Context mContext;
    private List<BLEScanBean> mList = new ArrayList<>();

    private OnRecyclerItemClickListener mListener;

    public BleDevicesAdapter(Context context) {
        mContext = context;
    }
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_ble_device, parent,false);
        DeviceHolder holder = new DeviceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.mTvBleDeviceName.setText(mList.get(position).deviceName);
        holder.mTvBleDeviceId.setText(mList.get(position).devUuId);
        holder.mBtnActivator.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onActivatorClick(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(BLEScanBean bean) {
        mList.add(bean);

        notifyDataSetChanged();
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView mTvBleDeviceName;
        private TextView mTvBleDeviceId;
        private Button mBtnActivator;
        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            mTvBleDeviceName = itemView.findViewById(R.id.tv_ble_device_name);
            mTvBleDeviceId = itemView.findViewById(R.id.tv_ble_device_id);
            mBtnActivator = itemView.findViewById(R.id.btn_ble_wifi_activator);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onActivatorClick(BLEScanBean deviceBean);
    }
}
