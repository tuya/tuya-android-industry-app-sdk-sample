package com.tuya.iotapp.sample.assets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.iotapp.asset.bean.AssetBean;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.assets.AssetsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Assets Adapter
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/22 4:47 PM
 */
public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ViewHolder> {
    private List<AssetBean> data = new ArrayList<>();

    public void setData(List<AssetBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_asset, parent, false));
        vh.itemView.setOnClickListener(v -> {
            // 跳转下级资产列表
            AssetBean assetBean = data.get(vh.getAdapterPosition());
            AssetsActivity.launch(v.getContext(),
                    assetBean.getAssetId(),
                    assetBean.getAssetName());
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetBean bean = data.get(position);
        holder.tvName.setText(bean.getAssetName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
