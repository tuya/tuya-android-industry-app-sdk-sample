package com.tuya.iotapp.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.slider.Slider;
import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.device.api.IDevice;
import com.thingclips.iotapp.device.api.bean.DpCommand;
import com.thingclips.iotapp.device.api.bean.DpMode;
import com.thingclips.iotapp.device.api.bean.DpSchema;
import com.thingclips.iotapp.device.api.bean.DpSchemaProperty;
import com.thingclips.iotapp.device.api.bean.DpsPublishMode;
import com.tuya.iotapp.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO feature
 *
 * @author <a href="mailto:sunrw@tuya.com">乾启</a>
 * @since 2022/1/21 11:47 AM
 */
public class DeviceControlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Pair<String, Pair<Object, DpSchema>>> dpList;
    private IDevice device;

    private DeviceControlAdapter() {
    }

    public DeviceControlAdapter(IDevice device) {
        this.device = device;

        dpList = new ArrayList<>();
        Map<String, Object> dps = device.getDps();
        for (String dpId : dps.keySet()) {
            Object value = device.getDps().get(dpId);
            Map<String, DpSchema> schemaMap = device.getSchemas();
            DpSchema schema = schemaMap.get(dpId);
            if (value == null || schema == null) {
                continue;
            }
            Pair<Object, DpSchema> valuePair = new Pair<>(value, schema);
            Pair<String, Pair<Object, DpSchema>> dpPair = new Pair<>(dpId, valuePair);
            dpList.add(dpPair);
        }
    }

    private interface DP_VIEW_TYPE {
        int BOOL = 0;
        int VALUE = 1;
        int ENUM = 2;
        int STRING = 4;
        int RAW = 5;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (DP_VIEW_TYPE.BOOL == viewType) {
            BoolViewHolder viewHolder = new BoolViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device_dp_bool, parent, false));
            viewHolder.sDp.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                String dpId = dpList.get(position).first;
                boolean value = !(boolean) dpList.get(position).second.first;
                DpCommand dps = new DpCommand.Builder()
                        .publishMode(DpsPublishMode.AUTO)
                        .addDp(dpId, value)
                        .build();
                device.publishDps(dps, new IndustryCallBack() {
                    @Override
                    public void onSuccess() {
                        // Dps send success, not means device dps changed.
                    }

                    @Override
                    public void onError(int i, @NonNull String s) {

                    }
                });
            });
            return viewHolder;
        } else if (DP_VIEW_TYPE.VALUE == viewType) {
            ValueViewHolder viewHolder = new ValueViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device_dp_value, parent, false));
            viewHolder.cvDp.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                String dpId = dpList.get(position).first;
                int value = (int) dpList.get(position).second.first;
                DpSchemaProperty valueRange = (DpSchemaProperty) dpList.get(position).second.second.getProperty();
                View vContainer = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_ty_dp_value, null, false);
                vContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Slider sValue = vContainer.findViewById(R.id.sValue);
                double scale = Math.pow(10, valueRange.getScale());
                String scaleFormat = "%." + valueRange.getScale() + "f";
                float from = Float.parseFloat(String.format(scaleFormat, valueRange.getMin() / scale));
                float to = Float.parseFloat(String.format(scaleFormat, valueRange.getMax() / scale));
                float step = Float.parseFloat(String.format(scaleFormat, valueRange.getStep() / scale));
                float curValue = Float.parseFloat(String.format(scaleFormat, (int) value / scale));
                sValue.setValueFrom(from);
                sValue.setValueTo(to);
                sValue.setStepSize(step);
                sValue.setValue(curValue);

                Button btnOk = vContainer.findViewById(R.id.btnOk);
                MaterialDialog valueDialog = new MaterialDialog.Builder(v.getContext())
                        .customView(vContainer, false)
                        .show();
                btnOk.setOnClickListener(v1 -> {
                    int dpValue = (int) (sValue.getValue() * scale);
                    DpCommand dps = new DpCommand.Builder()
                            .addDp(dpId, dpValue)
                            .build();

                    device.publishDps(dps, new IndustryCallBack() {
                        @Override
                        public void onSuccess() {
                            // Dps send success, not means device dps changed.
                        }

                        @Override
                        public void onError(int i, @NonNull String s) {

                        }
                    });
                    valueDialog.dismiss();
                });

            });
            return viewHolder;
        } else if (DP_VIEW_TYPE.ENUM == viewType) {
            EnumViewHolder viewHolder = new EnumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device_dp_enum, parent, false));
            viewHolder.cvDp.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                String dpId = dpList.get(position).first;
                DpSchemaProperty range = dpList.get(position).second.second.getProperty();
                List<String> enumRange = range.getRange();
                new MaterialDialog.Builder(v.getContext())
                        .items(enumRange)
                        .itemsCallback((dialog, itemView, position1, text) -> {

                            DpCommand dps = new DpCommand.Builder()
                                    .addDp(dpId, text)
                                    .build();
                            device.publishDps(dps, new IndustryCallBack() {
                                @Override
                                public void onSuccess() {
                                    // Dps send success, not means device dps changed.
                                }

                                @Override
                                public void onError(int i, @NonNull String s) {

                                }
                            });
                        })
                        .show();

            });
            return viewHolder;
        } else if (DP_VIEW_TYPE.RAW == viewType) {
            FaultViewHolder viewHolder = new FaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device_dp_fault, parent, false));
            return viewHolder;
        } else {
            StringViewHolder viewHolder = new StringViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ty_device_dp_string, parent, false));
            viewHolder.cvDp.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                String dpId = dpList.get(position).first;
                new MaterialDialog.Builder(v.getContext())
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                            }
                        })
                        .onPositive((dialog, which) -> {
                            String input = dialog.getInputEditText().getText().toString();
                            DpCommand dps = new DpCommand.Builder()
                                    .addDp(dpId, input)
                                    .build();
                            device.publishDps(dps, new IndustryCallBack() {
                                @Override
                                public void onSuccess() {
                                    // Dps send success, not means device dps changed.
                                }

                                @Override
                                public void onError(int i, @NonNull String s) {

                                }
                            });
                        })
                        .show();
            });
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Pair<Object, DpSchema> valuePair = dpList.get(position).second;
        if (DP_VIEW_TYPE.BOOL == getItemViewType(position)) {
            BoolViewHolder boolViewHolder = (BoolViewHolder) holder;

            boolViewHolder.tvDpName.setText(valuePair.second.getName());
            boolViewHolder.sDp.setChecked((boolean) valuePair.first);

            boolean writeable = DpMode.RW.equals(valuePair.second.getMode()) || DpMode.W.equals(valuePair.second.getMode());
            boolViewHolder.sDp.setClickable(writeable);
        } else if (DP_VIEW_TYPE.VALUE == getItemViewType(position)) {
            ValueViewHolder valueViewHolder = (ValueViewHolder) holder;

            valueViewHolder.tvDpName.setText(valuePair.second.getName());

            DpSchemaProperty valueRange = valuePair.second.getProperty();
            String realValue = String.format("%." + valueRange.getScale() + "f", 1.0 * (int) valuePair.first / Math.pow(10, valueRange.getScale()));
            valueViewHolder.tvDpValue.setText(realValue + valueRange.getUnit());

            boolean writeable = DpMode.RW.equals(valuePair.second.getMode()) || DpMode.W.equals(valuePair.second.getMode());
            valueViewHolder.ivWriteable.setVisibility(writeable ? View.VISIBLE : View.GONE);
            valueViewHolder.cvDp.setClickable(writeable);
        } else if (DP_VIEW_TYPE.ENUM == getItemViewType(position)) {
            EnumViewHolder enumViewHolder = (EnumViewHolder) holder;

            enumViewHolder.tvDpName.setText(valuePair.second.getName());
            enumViewHolder.tvDpValue.setText((String) valuePair.first);

            boolean writeable = DpMode.RW.equals(valuePair.second.getMode()) || DpMode.W.equals(valuePair.second.getMode());
            enumViewHolder.ivWriteable.setVisibility(writeable ? View.VISIBLE : View.GONE);
            enumViewHolder.cvDp.setClickable(writeable);
        } else if (DP_VIEW_TYPE.RAW == getItemViewType(position)) {
            FaultViewHolder faultViewHolder = (FaultViewHolder) holder;

            faultViewHolder.tvDpName.setText(valuePair.second.getName());
            faultViewHolder.tvDpValue.setText(String.valueOf(valuePair.first));
        } else {
            StringViewHolder stringViewHolder = (StringViewHolder) holder;

            stringViewHolder.tvDpName.setText(valuePair.second.getName());
            stringViewHolder.tvDpValue.setText((String) valuePair.first);

            boolean writeable = DpMode.RW.equals(valuePair.second.getMode()) || DpMode.W.equals(valuePair.second.getMode());
            stringViewHolder.ivWriteable.setVisibility(writeable ? View.VISIBLE : View.GONE);
            stringViewHolder.cvDp.setClickable(writeable);
        }
    }

    @Override
    public int getItemCount() {
        return dpList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (dpList.get(position).second.second.getType()) {
            case BOOL:
                return DP_VIEW_TYPE.BOOL;
            case VALUE:
                return DP_VIEW_TYPE.VALUE;
            case ENUM:
                return DP_VIEW_TYPE.ENUM;
            case RAW:
                return DP_VIEW_TYPE.RAW;
            case STRING:
            default:
                return DP_VIEW_TYPE.STRING;
        }
    }


    class BoolViewHolder extends RecyclerView.ViewHolder {
        CardView cvDp;
        TextView tvDpName;
        SwitchCompat sDp;

        public BoolViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDp = itemView.findViewById(R.id.cvDp);
            tvDpName = itemView.findViewById(R.id.tvDpName);
            sDp = itemView.findViewById(R.id.sDp);
        }
    }

    class ValueViewHolder extends RecyclerView.ViewHolder {
        CardView cvDp;
        TextView tvDpName;
        TextView tvDpValue;
        ImageView ivWriteable;

        public ValueViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDp = itemView.findViewById(R.id.cvDp);
            tvDpName = itemView.findViewById(R.id.tvDpName);
            tvDpValue = itemView.findViewById(R.id.tvDpValue);
            ivWriteable = itemView.findViewById(R.id.ivWriteable);
        }
    }

    class EnumViewHolder extends RecyclerView.ViewHolder {

        CardView cvDp;
        TextView tvDpName;
        TextView tvDpValue;
        ImageView ivWriteable;

        public EnumViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDp = itemView.findViewById(R.id.cvDp);
            tvDpName = itemView.findViewById(R.id.tvDpName);
            tvDpValue = itemView.findViewById(R.id.tvDpValue);
            ivWriteable = itemView.findViewById(R.id.ivWriteable);
        }
    }

    class StringViewHolder extends RecyclerView.ViewHolder {

        CardView cvDp;
        TextView tvDpName;
        TextView tvDpValue;
        ImageView ivWriteable;

        public StringViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDp = itemView.findViewById(R.id.cvDp);
            tvDpName = itemView.findViewById(R.id.tvDpName);
            tvDpValue = itemView.findViewById(R.id.tvDpValue);
            ivWriteable = itemView.findViewById(R.id.ivWriteable);
        }
    }

    class FaultViewHolder extends RecyclerView.ViewHolder {

        TextView tvDpName;
        TextView tvDpValue;

        public FaultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDpName = itemView.findViewById(R.id.tvDpName);
            tvDpValue = itemView.findViewById(R.id.tvDpValue);
        }
    }
}
