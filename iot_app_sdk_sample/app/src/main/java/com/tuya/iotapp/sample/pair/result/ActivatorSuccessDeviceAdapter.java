package com.tuya.iotapp.sample.pair.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.thingclips.iotapp.device.api.IDevice;
import com.tuya.iotapp.sample.R;

import java.util.List;

/**
 * ActivatorSuccessDeviceAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class ActivatorSuccessDeviceAdapter extends RecyclerView.Adapter<ActivatorSuccessDeviceAdapter.SuccessDeviceHolder> {

    private Context mContext;
    private List<IDevice> mList;

    public ActivatorSuccessDeviceAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SuccessDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ty_pair_activate_sucess_device, parent, false);
        SuccessDeviceHolder holder = new SuccessDeviceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuccessDeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.tvProductId.setText(mList.get(position).getProductId());
        holder.tvDeviceId.setText(mList.get(position).getDeviceId());
        holder.tvDeviceName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<IDevice> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public class SuccessDeviceHolder extends RecyclerView.ViewHolder {
        private TextView tvProductId;
        private TextView tvDeviceId;
        private TextView tvDeviceName;

        public SuccessDeviceHolder(@NonNull View itemView) {
            super(itemView);

            tvProductId = itemView.findViewById(R.id.tvProductId);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
        }
    }
}
