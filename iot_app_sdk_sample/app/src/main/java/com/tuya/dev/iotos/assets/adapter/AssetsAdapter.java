package com.tuya.dev.iotos.assets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.assets.AssetsActivity;
import com.tuya.dev.iotos.assets.bean.AssetBean;

import java.util.ArrayList;

/**
 * Assets Adapter
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/22 4:47 PM
 */
public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ViewHolder> {
    private ArrayList<AssetBean.AssetsBean> data = new ArrayList<>();

    public void setData(ArrayList<AssetBean.AssetsBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_asset, parent, false));
        vh.itemView.setOnClickListener(v -> {
            // 跳转下级资产列表
            AssetBean.AssetsBean assetBean = data.get(vh.getAdapterPosition());
            AssetsActivity.launch(v.getContext(),
                    assetBean.getAsset_id(),
                    assetBean.getAsset_name());
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetBean.AssetsBean bean = data.get(position);
        holder.tvName.setText(bean.getAsset_name());

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
