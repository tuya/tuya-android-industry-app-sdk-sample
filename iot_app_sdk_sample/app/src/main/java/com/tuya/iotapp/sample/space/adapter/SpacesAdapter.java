package com.tuya.iotapp.sample.space.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.space.api.ISpace;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.space.SpacesActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Assets Adapter
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/22 4:47 PM
 */
public class SpacesAdapter extends RecyclerView.Adapter<SpacesAdapter.ViewHolder> {
    private List<ISpace> data = new ArrayList<>();

    public void setData(List<ISpace> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_asset, parent, false));
        vh.itemView.setOnClickListener(v -> {
            ISpace space = data.get(vh.getAdapterPosition());
            SpacesActivity.launch(v.getContext(),
                    space.getSpaceId(),
                    space.getSpaceName());
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ISpace bean = data.get(position);
        holder.tvName.setText(bean.getSpaceName());
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
