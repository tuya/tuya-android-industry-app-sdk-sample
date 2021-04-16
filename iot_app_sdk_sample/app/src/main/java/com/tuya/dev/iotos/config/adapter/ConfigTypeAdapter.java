package com.tuya.dev.iotos.config.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.config.bean.ConfigTypeBean;

import java.util.List;

/**
 * Main Page Config type adapter
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 3:33 PM
 */
public class ConfigTypeAdapter extends RecyclerView.Adapter<ConfigTypeAdapter.ViewHolder> {

    private List<ConfigTypeBean> list;

    public ConfigTypeAdapter(List<ConfigTypeBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConfigTypeBean bean = list.get(position);

        holder.ivConfig.setImageResource(bean.getIcon());
        holder.tvConfig.setText(bean.getName());
        holder.itemView.setOnClickListener(bean.getClick());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivConfig;
        TextView tvConfig;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivConfig = itemView.findViewById(R.id.ivConfig);
            tvConfig = itemView.findViewById(R.id.tvConfig);
        }
    }
}
