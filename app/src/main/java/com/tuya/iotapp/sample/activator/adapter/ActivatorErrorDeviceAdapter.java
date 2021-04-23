package com.tuya.iotapp.sample.activator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.activator.bean.ErrorDeviceBean;
import com.tuya.iotapp.sample.R;

import java.util.List;

/**
 * ActivatorErrorDeviceAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class ActivatorErrorDeviceAdapter extends RecyclerView.Adapter<ActivatorErrorDeviceAdapter.ErrorDeviceHolder> {

    private Context mContext;
    private List<ErrorDeviceBean> mList;

    public ActivatorErrorDeviceAdapter(Context context) {
        mContext = context;
    }
    @Override
    public ErrorDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_activator_error_device, parent,false);
        ErrorDeviceHolder holder = new ErrorDeviceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorDeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.mTvErrorMsg.setText(mList.get(position).getMsg());
        holder.mTvDeviceId.setText(mList.get(position).getDeviceId());
        holder.mTvDeviceName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<ErrorDeviceBean> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public class ErrorDeviceHolder extends RecyclerView.ViewHolder {
        private TextView mTvDeviceId;
        private TextView mTvDeviceName;
        private TextView mTvErrorMsg;
        public ErrorDeviceHolder(@NonNull View itemView) {
            super(itemView);

            mTvErrorMsg = itemView.findViewById(R.id.tv_error_msg);
            mTvDeviceId = itemView.findViewById(R.id.tv_device_id);
            mTvDeviceName = itemView.findViewById(R.id.tv_device_name);
        }
    }
}
