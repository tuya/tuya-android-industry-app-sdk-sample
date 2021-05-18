package com.tuya.dev.iotos.authScan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tuya.dev.iotos.LoginActivity;
import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.dev.iotos.kv.KvGlobalManager;
import com.tuya.ka.qrscan.activity.CaptureActivity;

/**
 * 扫码处理逻辑
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2019-12-26 10:50
 */
public class AuthScanActivity extends CaptureActivity {


    public static void launch(Context context) {
        context.startActivity(new Intent(context, AuthScanActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth_scan;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void handlePermission() {

    }

    @Override
    protected void dealDecodeMsg(String rawResultText) {

        boolean auth = AuthManager.init(this, rawResultText);
        if (auth) {
            //Scan Result correct
            KvGlobalManager.set(AuthConst.KEY, rawResultText);

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            //Scan Result error
            Toast.makeText(this,
                    getString(R.string.auth_first_scan_error_tips),
                    Toast.LENGTH_SHORT)
                    .show();

            new Handler(Looper.getMainLooper()).postDelayed((Runnable) this::releaseCamera, 2000);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
