package com.tuya.iotapp.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.activitor.config.IQrCodeActivitorListener;
import com.tuya.iotapp.sample.present.IActivitorResultListener;
import com.tuya.iotapp.sample.present.WifiConfigurationPresenter;

/**
 * QRConfigActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class QRConfigActivity extends AppCompatActivity implements IQrCodeActivitorListener, IActivitorResultListener {

    private ImageView mIvQrCode;
    private TextView mTvDeviceInfo;

    private WifiConfigurationPresenter mQrPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrconfig);
        initView();
        mQrPresenter = new WifiConfigurationPresenter(this, getIntent());
        mQrPresenter.createQrCode(this);
        mQrPresenter.setActivityResultListener(this);

        mQrPresenter.startLoop();
    }

    private void initView() {
        mIvQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        mTvDeviceInfo = (TextView) findViewById(R.id.tv_device_info);
    }

    @Override
    public void onQrCodeSuccess(Bitmap bitmap) {
        mIvQrCode.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQrPresenter != null) {
            mQrPresenter = null;
        }
    }

    @Override
    public void onActivitySuccessDevice(String successDevice) {
        mIvQrCode.setVisibility(View.GONE);
        mTvDeviceInfo.setVisibility(View.VISIBLE);
        mTvDeviceInfo.setText(successDevice);
    }
}
