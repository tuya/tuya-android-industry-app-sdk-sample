package com.tuya.dev.iotos.activator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.activator.presenter.IQrCodeActivatorListener;
import com.tuya.dev.iotos.activator.presenter.WifiConfigurationPresenter;

/**
 * QRConfigActivity
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 5:22 PM
 */
public class ActivatorQRConfigActivity extends AppCompatActivity implements IQrCodeActivatorListener {

    private Context mContext;
    private ImageView mIvQrCode;

    private WifiConfigurationPresenter mQrPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activator_qrconfig);
        initView();
        mContext = this;
        mQrPresenter = new WifiConfigurationPresenter(this, getIntent());
        mQrPresenter.createQrCode(this);

        findViewById(R.id.tvQrNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivatorProcessActivity.class);
                intent.putExtra("config_type", getIntent().getStringExtra("config_type"));
                intent.putExtra("token", getIntent().getStringExtra("token"));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mIvQrCode = (ImageView) findViewById(R.id.ivQrCode);

        ((TextView) findViewById(R.id.tvTitle)).setText(R.string.activator_qr);
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });

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
