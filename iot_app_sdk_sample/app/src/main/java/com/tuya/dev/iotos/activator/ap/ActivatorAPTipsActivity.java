package com.tuya.dev.iotos.activator.ap;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.activator.ActivatorProcessActivity;

/**
 * AP Tips Activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/5/18 11:08 AM
 */
public class ActivatorAPTipsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activator_ap_tips);

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.tvNext).setOnClickListener(v -> {
            startActivity(getIntent().setClass(v.getContext(),
                    ActivatorProcessActivity.class));
        });
    }
}
