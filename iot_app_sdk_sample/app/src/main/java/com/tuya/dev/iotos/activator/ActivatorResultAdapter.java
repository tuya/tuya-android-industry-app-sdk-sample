package com.tuya.dev.iotos.activator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.devices.bean.ErrorDeviceBean;
import com.tuya.dev.devices.bean.SuccessDeviceBean;
import com.tuya.dev.iotos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Activator Result Adapter
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/22 10:32 AM
 */
class ActivatorResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_SUCCESS = 1;
    private static final int TYPE_ERROR = 2;


    private List<Object> list = new ArrayList<>();

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ERROR) {
            return new ErrorViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activator_result_sucess_device, parent, false));
        } else if (viewType == TYPE_SUCCESS) {
            return new SuccessViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activator_result_sucess_device, parent, false));
        } else {
            return new TitleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activator_result_title, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        Context context = holder.itemView.getContext();
        if (type == TYPE_TITLE) {
            ((TitleViewHolder) holder).tvTitle.setText((String) list.get(position));
        } else if (type == TYPE_SUCCESS) {
            SuccessDeviceBean successDeviceBean = (SuccessDeviceBean) list.get(position);
            ((SuccessViewHolder) holder).tvDeviceName.setText(context.getString(R.string.device_name) + successDeviceBean.getName());
            ((SuccessViewHolder) holder).tvDeviceId.setText(context.getString(R.string.device_id) + successDeviceBean.getId());
        } else if (type == TYPE_ERROR) {
            ErrorDeviceBean errorDeviceBean = (ErrorDeviceBean) list.get(position);
            ((ErrorViewHolder) holder).tvDeviceName.setText(context.getString(R.string.device_name) + errorDeviceBean.getName());
            ((ErrorViewHolder) holder).tvMsg.setText(context.getString(R.string.activator_error_msg) + errorDeviceBean.getMsg());
            ((ErrorViewHolder) holder).tvDeviceId.setText(context.getString(R.string.device_id) + errorDeviceBean.getDevice_id());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof SuccessDeviceBean) {
            return TYPE_SUCCESS;
        } else if (list.get(position) instanceof ErrorDeviceBean) {
            return TYPE_ERROR;
        }
        return TYPE_TITLE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SuccessViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceName;
        TextView tvDeviceId;

        public SuccessViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
        }
    }

    static class ErrorViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceName;
        TextView tvDeviceId;
        TextView tvMsg;

        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceId = itemView.findViewById(R.id.tvDeviceId);
            tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
            tvMsg = itemView.findViewById(R.id.tvMsg);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
