package com.tuya.dev.iotos.activator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.devices.bean.ErrorDeviceBean;
import com.tuya.dev.devices.bean.SuccessDeviceBean;
import com.tuya.dev.iotos.MainManagerActivity;
import com.tuya.dev.iotos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Activator result activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/21 2:40 PM
 */
public class ActivatorResultActivity extends AppCompatActivity {
    final static String SUCCESS_DEVICES = "successDevices";
    final static String ERROR_DEVICES = "errorDevices";

    static void launch(Context context,
                       ArrayList<SuccessDeviceBean> successDevices,
                       ArrayList<ErrorDeviceBean> errorDevices) {
        Intent intent = new Intent(context, ActivatorResultActivity.class);
        intent.putParcelableArrayListExtra(SUCCESS_DEVICES, successDevices);
        intent.putParcelableArrayListExtra(ERROR_DEVICES, errorDevices);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activator_result);

        RecyclerView rvDevices = findViewById(R.id.rvDevices);


        RecyclerView.LayoutManager successLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDevices.setLayoutManager(successLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.s_divider));
        rvDevices.addItemDecoration(dividerItemDecoration);

        ActivatorResultAdapter adapter = new ActivatorResultAdapter();

        List<Object> list = new ArrayList<Object>();

        List<SuccessDeviceBean> successDeviceBeans = getIntent().getParcelableArrayListExtra(SUCCESS_DEVICES);
        if (successDeviceBeans.size() > 0) {
            list.add(getString(R.string.activator_success_devices));
            list.addAll(successDeviceBeans);
        }

        List<ErrorDeviceBean> errorDeviceBeans = getIntent().getParcelableArrayListExtra(ERROR_DEVICES);
        if (errorDeviceBeans.size() > 0) {
            list.add(getString(R.string.activator_error_devices));
            list.addAll(errorDeviceBeans);
        }

        rvDevices.setAdapter(adapter);
        adapter.setList(list);

        findViewById(R.id.btnOk).setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainManagerActivity.class);
            startActivity(intent);
        });
    }
}
