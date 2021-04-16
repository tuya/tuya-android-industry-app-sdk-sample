package com.tuya.dev.iotos.authScan;

import android.content.Context;

import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.dev.common.kv.KvGlobalManager;
import com.tuya.dev.network.IotAppNetWork;
import com.tuya.dev.network.api.IApiUrlProvider;
import com.tuya.dev.iotos.authScan.bean.AuthBean;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.dev.iotos.env.EnvUrlProvider;
import com.tuya.dev.iotos.env.EnvUtils;

/**
 * Auth Manager
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 11:38 AM
 */
public class AuthManager {
    static boolean init(Context context, String auth) {
        AuthBean bean = JsonParser.parseObject(auth, AuthBean.class);

        EnvUtils.setEnv(context, EnvUtils.ENV_PRE); //环境区分
        IApiUrlProvider provider = new EnvUrlProvider(context);

        IotAppNetWork.initialize(context.getApplicationContext(),
                bean.getAccessId(),
                bean.getAccessSecret(),
                "Android",
                provider);
        return true;
    }

    public static void clear() {
        KvGlobalManager.set(AuthConst.KEY, "");
    }

}
