package com.tuya.dev.iotos.activator;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.activator.presenter.IActivatorResultListener;
import com.tuya.dev.iotos.activator.presenter.WifiConfigurationPresenter;
import com.tuya.iotapp.activator.bean.ErrorDeviceBean;
import com.tuya.iotapp.activator.bean.SuccessDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Activator Process
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class ActivatorProcessActivity extends AppCompatActivity implements IActivatorResultListener {

    private WifiConfigurationPresenter multiPresenter;

    private ImageView ivProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activator_multi_config);
        initView();

        multiPresenter = new WifiConfigurationPresenter(this, getIntent());
        multiPresenter.setActivatorResultListener(this);
        multiPresenter.startConfig();
    }

    private void initView() {
        ivProgress = findViewById(R.id.ivProgress);

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (multiPresenter != null) {
            multiPresenter.stopConfig();
            multiPresenter = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Animation rotateAnimation = AnimationUtils.loadAnimation(this,
                R.anim.anim_rotate);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        ivProgress.startAnimation(rotateAnimation);
    }

    @Override
    public void onActivatorResultDevice(List<SuccessDeviceBean> successDevices,
                                        List<ErrorDeviceBean> errorDevices) {
        runOnUiThread(() -> {
            finish();
            ActivatorResultActivity.launch(ActivatorProcessActivity.this,
                    (ArrayList<SuccessDeviceBean>) successDevices,
                    (ArrayList<ErrorDeviceBean>) errorDevices);

        });

    }
}
