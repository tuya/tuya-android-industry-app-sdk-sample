package com.tuya.iotapp.sample.activator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tuya.iotapp.activator.bean.SuccessDeviceBean;
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
    private List<SuccessDeviceBean> mList;

    public ActivatorSuccessDeviceAdapter(Context context) {
        mContext = context;
    }
    @Override
    public SuccessDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_activator_sucess_device, parent,false);
        SuccessDeviceHolder holder = new SuccessDeviceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuccessDeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.mTvProductId.setText(mList.get(position).getProductId());
        holder.mTvDeviceId.setText(mList.get(position).getId());
        holder.mTvDeviceName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<SuccessDeviceBean> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public class SuccessDeviceHolder extends RecyclerView.ViewHolder {
        private TextView mTvProductId;
        private TextView mTvDeviceId;
        private TextView mTvDeviceName;
        public SuccessDeviceHolder(@NonNull View itemView) {
            super(itemView);

            mTvProductId = itemView.findViewById(R.id.tv_product_id);
            mTvDeviceId = itemView.findViewById(R.id.tv_device_id);
            mTvDeviceName = itemView.findViewById(R.id.tv_device_name);
        }
    }
}
