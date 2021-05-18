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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuya.dev.iotos.MainManagerActivity;
import com.tuya.dev.iotos.R;
import com.tuya.iotapp.activator.bean.ErrorDeviceBean;
import com.tuya.iotapp.activator.bean.SuccessDeviceBean;

import java.lang.reflect.Type;
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
    final static Gson gson = new Gson();

    static void launch(Context context,
                       ArrayList<SuccessDeviceBean> successDevices,
                       ArrayList<ErrorDeviceBean> errorDevices) {

        Intent intent = new Intent(context, ActivatorResultActivity.class);
        intent.putExtra(SUCCESS_DEVICES, gson.toJson(successDevices));
        intent.putExtra(ERROR_DEVICES, gson.toJson(errorDevices));
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

        Type listOfSuccess = new TypeToken<ArrayList<SuccessDeviceBean>>() {
        }.getType();
        List<SuccessDeviceBean> successDeviceBeans = gson.fromJson(getIntent().getStringExtra(SUCCESS_DEVICES), listOfSuccess);
        if (successDeviceBeans.size() > 0) {
            list.add(getString(R.string.activator_success_devices));
            list.addAll(successDeviceBeans);
        }

        Type listOfError = new TypeToken<ArrayList<ErrorDeviceBean>>() {
        }.getType();

        List<ErrorDeviceBean> errorDeviceBeans = gson.fromJson(getIntent().getStringExtra(ERROR_DEVICES), listOfError);
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
