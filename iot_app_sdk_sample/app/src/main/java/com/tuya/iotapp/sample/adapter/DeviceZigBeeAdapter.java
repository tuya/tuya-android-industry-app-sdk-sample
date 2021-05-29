package com.tuya.iotapp.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.activator.bean.GatewayBean;
import com.tuya.iotapp.sample.R;

import java.util.List;

/**
 * @description: DeviceZigBeeAdapter
 * @author: mengzi.deng <a href="mailto:developer@tuya.com"/>
 * @since: 5/15/21 2:28 PM
 */
public class DeviceZigBeeAdapter extends RecyclerView.Adapter<DeviceZigBeeAdapter.DeviceHolder> {

    private Context mContext;
    private List<GatewayBean> mList;

    private OnRecyclerItemClickListener mListener;

    public DeviceZigBeeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_device_zigbee, parent, false);
        DeviceHolder holder = new DeviceHolder(view);

        holder.mBtnAddSubDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = (int) view.getTag();
                    mListener.onAddSubDeviceClick(view, mList.get(position));
                }
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    int position = (int) view.getTag();
                    mListener.onItemLongClick(view, mList.get(position));
                }
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }

        holder.itemView.setTag(position);
        holder.mTvDeviceId.setText(mList.get(position).getId());
        holder.mTvDeviceName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<GatewayBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView mTvDeviceId;
        private TextView mTvDeviceName;
        private Button mBtnAddSubDevice;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            mTvDeviceId = itemView.findViewById(R.id.tv_device_id);
            mTvDeviceName = itemView.findViewById(R.id.tv_device_name);
            mBtnAddSubDevice = (Button) itemView.findViewById(R.id.btn_add_zb_sub_device);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, GatewayBean deviceBean);

        void onItemLongClick(View view, GatewayBean deviceBean);

        void onAddSubDeviceClick(View view, GatewayBean deviceBean);
    }
}
