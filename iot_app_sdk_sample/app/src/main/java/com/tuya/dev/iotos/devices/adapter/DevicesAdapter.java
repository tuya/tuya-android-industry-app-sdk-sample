package com.tuya.dev.iotos.devices.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tuya.dev.iotos.R;
import com.tuya.iotapp.asset.bean.AssetDeviceBean;
import com.tuya.iotapp.asset.bean.AssetDeviceListBean;
import com.tuya.iotapp.network.api.TYNetworkManager;

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
    private List<AssetDeviceBean> mList;
    private HashMap<String, String> deviceIconsMap = new HashMap();

    private OnRecyclerItemClickListener mListener;
    private String urlImageCdn;

    public DevicesAdapter(Context context) {
        mContext = context;
        String[] paths = TYNetworkManager.Companion.getServiceHostUrl()
                .split("\\.");
        StringBuilder builder = new StringBuilder("https://images.");
        for (int i = 1; i < paths.length; i++) {
            builder.append(paths[i] + ".");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("/");
        urlImageCdn = builder.toString();

    }

    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_device, parent, false);
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

        AssetDeviceBean bean = mList.get(position);

        holder.itemView.setTag(position);
        holder.tvAssetId.setText(holder.itemView.getContext().getString(R.string.asset_id) + bean.getAssetId());
        holder.tvDeviceId.setText(holder.itemView.getContext().getString(R.string.device_id) + bean.getDeviceId());

        if (deviceIconsMap.containsKey(bean.getDeviceId())) {
            String iconUrl = deviceIconsMap.get(bean.getDeviceId());
            Glide.with(holder.itemView.getContext())
                    .load(urlImageCdn + iconUrl)
                    .placeholder(R.drawable.ic_tuya_default_device)
                    .into(holder.ivIcon);
        }

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(AssetDeviceListBean list) {
        mList = list.getList();

        notifyDataSetChanged();
    }

    public List<AssetDeviceBean> getList() {
        return mList;
    }

    public HashMap<String, String> getDeviceIconsMap() {
        return deviceIconsMap;
    }

    public OnRecyclerItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnRecyclerItemClickListener mListener) {
        this.mListener = mListener;
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView tvAssetId;
        private TextView tvDeviceId;
        private ImageView ivIcon;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);

            tvAssetId = itemView.findViewById(R.id.tvAssetId);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view, AssetDeviceBean deviceBean);

        void onItemLongClick(View view, AssetDeviceBean deviceBean);
    }
}
