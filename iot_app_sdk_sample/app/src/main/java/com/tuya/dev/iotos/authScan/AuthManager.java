package com.tuya.dev.iotos.authScan;

import android.content.Context;

import com.tuya.dev.common.kv.KvGlobalManager;
import com.tuya.dev.iotos.authScan.bean.AuthBean;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.dev.iotos.env.Endpoint;
import com.tuya.dev.iotos.env.EnvUrlProvider;
import com.tuya.dev.iotos.env.EnvUtils;
import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.dev.network.IotAppNetWork;
import com.tuya.dev.network.api.IApiUrlProvider;

/**
 * Auth Manager
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 11:38 AM
 */
public class AuthManager {
    static boolean init(Context context, String auth) {
        AuthBean bean = JsonParser.parseObject(auth, AuthBean.class);

        EnvUtils.setEndpoint(context, Endpoint.AZ);
        IApiUrlProvider provider = new EnvUrlProvider();

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
