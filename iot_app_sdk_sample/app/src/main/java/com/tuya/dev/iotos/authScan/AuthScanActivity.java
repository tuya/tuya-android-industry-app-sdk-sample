package com.tuya.dev.iotos.authScan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tuya.dev.common.kv.KvGlobalManager;
import com.tuya.dev.iotos.LoginActivity;
import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.ka.qrscan.activity.CaptureActivity;

/**
 * 扫码处理逻辑
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2019-12-26 10:50
 */
public class AuthScanActivity extends CaptureActivity {
    private TextView tvAction;
    private static int REQUEST_CODE_PHOTO = 1;

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

        findViewById(R.id.ivBack).setOnClickListener(v->{
            finish();
        });

    }

    @Override
    protected void handlePermission() {

    }

    @Override
    protected void dealDecodeMsg(String rawResultText) {
        //扫码结果有误


        //扫码结果正确
        AuthManager.init(this, rawResultText);

        KvGlobalManager.set(AuthConst.KEY, rawResultText);

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
