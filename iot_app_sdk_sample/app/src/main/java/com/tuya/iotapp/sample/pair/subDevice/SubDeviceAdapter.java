package com.tuya.iotapp.sample.pair.subDevice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.asset.api.IAssetDevice;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.device.api.IDevice;
import com.tuya.iotapp.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 乾启 <a href="mailto:developer@tuya.com">Contact me.</a>
 * @since 2022/1/4 1:48 PM
 */
class SubDeviceAdapter extends RecyclerView.Adapter<SubDeviceAdapter.DeviceHolder> {
    List<IDevice> list = new ArrayList<>();

    public SubDeviceAdapter() {

    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceHolder viewHolder = new DeviceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        holder.tvDeviceName.setText(list.get(position).getDeviceId());
        holder.tvDeviceId.setText(list.get(position).getDeviceId());
        holder.tvOnline.setText(holder.itemView.getContext().getString(list.get(position).isOnline() ? R.string.device_online : R.string.device_offline));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<IDevice> getList() {
        return list;
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView tvDeviceName;
        private TextView tvDeviceId;
        private TextView tvOnline;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            tvOnline = itemView.findViewById(R.id.tvOnline);
        }
    }
}
