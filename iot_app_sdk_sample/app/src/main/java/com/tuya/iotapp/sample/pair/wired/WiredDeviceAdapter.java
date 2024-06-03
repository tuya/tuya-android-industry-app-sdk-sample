package com.tuya.iotapp.sample.pair.wired;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.pair.api.IWiredDevice;
import com.tuya.iotapp.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO feature
 *
 * @author 乾启 <a href="mailto:developer@tuya.com">Contact me.</a>
 * @since 2022/1/4 1:48 PM
 */
class WiredDeviceAdapter extends RecyclerView.Adapter<WiredDeviceAdapter.ViewHolder> {
    List<IWiredDevice> list = new ArrayList<>();
    IndustryValueCallBack<IWiredDevice> valueCallBack;

    public WiredDeviceAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_wired_device, parent, false));
        viewHolder.itemView.setOnClickListener(v -> {
            valueCallBack.onSuccess(list.get(viewHolder.getAdapterPosition()));
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvIp.setText(list.get(position).getGWId());
    }

    void setLaunchWiredPair(IndustryValueCallBack<IWiredDevice> valueCallBack) {
        this.valueCallBack = valueCallBack;
    }

    public List<IWiredDevice> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIp = itemView.findViewById(R.id.tvIp);
        }
    }
}
