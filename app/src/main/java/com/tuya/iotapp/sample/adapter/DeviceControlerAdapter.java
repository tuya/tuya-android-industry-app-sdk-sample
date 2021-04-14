package com.tuya.iotapp.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.devices.bean.AssetDeviceBean;
import com.tuya.iotapp.devices.bean.AssetDeviceListBean;
import com.tuya.iotapp.devices.bean.FunctionBean;
import com.tuya.iotapp.sample.R;

import java.util.List;

/**
 * DeviceControlerAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class DeviceControlerAdapter extends RecyclerView.Adapter<DeviceControlerAdapter.DeviceControlerHolder> {

    private Context mContext;
    private List<FunctionBean> mList;

    private OnRecyclerItemClickListener mListener;

    public DeviceControlerAdapter(Context context) {
        mContext = context;
    }
    @Override
    public DeviceControlerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_device_controler, parent,false);
        DeviceControlerHolder holder = new DeviceControlerHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceControlerHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.mTvFuncName.setText(mList.get(position).getName());
        holder.mTvFuncDes.setText(mList.get(position).getDesc());
        holder.mTvFuncCode.setText(mList.get(position).getCode());
        holder.mTvFuncValue.setText(mList.get(position).getValues());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<FunctionBean> list) {
        mList = list;

        notifyDataSetChanged();
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceControlerHolder extends RecyclerView.ViewHolder {
        private TextView mTvFuncName;
        private TextView mTvFuncDes;
        private TextView mTvFuncCode;
        private TextView mTvFuncValue;
        public DeviceControlerHolder(@NonNull View itemView) {
            super(itemView);

            mTvFuncName = itemView.findViewById(R.id.tv_func_name);
            mTvFuncDes = itemView.findViewById(R.id.tv_func_desc);
            mTvFuncCode = itemView.findViewById(R.id.tv_func_code);
            mTvFuncValue = itemView.findViewById(R.id.tv_func_value);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, AssetDeviceBean deviceBean);
        void onItemLongClick(View view, AssetDeviceBean deviceBean);
    }
}
