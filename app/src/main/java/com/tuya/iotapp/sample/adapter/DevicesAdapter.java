package com.tuya.iotapp.sample.adapter;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.devices.bean.AssetDeviceBean;
import com.tuya.iotapp.devices.bean.AssetDeviceListBean;
import com.tuya.iotapp.sample.R;

import java.util.List;

/**
 * DevicesAdapter
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 7:55 PM
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceHolder> {

    private Context mContext;
    private List<AssetDeviceBean> mList;

    private OnRecyclerItemClickListener mListener;

    public DevicesAdapter(Context context) {
        mContext = context;
    }
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_device, parent,false);
        DeviceHolder holder = new DeviceHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        holder.mTvAssetId.setText(mList.get(position).getAsset_id());
        holder.mTvDeviceId.setText(mList.get(position).getDevice_id());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(AssetDeviceListBean list) {
        mList = list.getList();

        notifyDataSetChanged();
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView mTvAssetId;
        private TextView mTvDeviceId;
        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            mTvAssetId = itemView.findViewById(R.id.tv_asset_id);
            mTvDeviceId = itemView.findViewById(R.id.tv_device_id);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, AssetDeviceBean deviceBean);
        void onItemLongClick(View view, AssetDeviceBean deviceBean);
    }
}
