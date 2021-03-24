package com.tuya.iotapp.sample.activator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.iotapp.activator.config.IQrCodeActivatorListener;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.activator.presenter.WifiConfigurationPresenter;

/**
 * QRConfigActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class QRConfigActivity extends AppCompatActivity implements IQrCodeActivatorListener{

    private Context mContext;
    private ImageView mIvQrCode;
    private Button mBtnQrNext;

    private WifiConfigurationPresenter mQrPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrconfig);
        initView();
        mContext = this;
        mQrPresenter = new WifiConfigurationPresenter(this, getIntent());
        mQrPresenter.createQrCode(this);

        mBtnQrNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MultiWifiConfigActivity.class);
                intent.putExtra("config_type", "QR");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mIvQrCode = (ImageView) findViewById(R.id.iv_qr_code);
        mBtnQrNext = (Button) findViewById(R.id.btn_qr_next);
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
}
