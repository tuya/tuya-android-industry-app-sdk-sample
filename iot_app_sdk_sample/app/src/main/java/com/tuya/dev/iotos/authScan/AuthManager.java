package com.tuya.dev.iotos.authScan;

import android.content.Context;

import com.google.gson.Gson;
import com.tuya.dev.iotos.authScan.bean.AuthBean;
import com.tuya.dev.iotos.authScan.enums.AuthConst;
import com.tuya.dev.iotos.env.Endpoint;
import com.tuya.dev.iotos.kv.KvGlobalManager;
import com.tuya.iotapp.network.api.TYNetworkManager;

import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

/**
 * Auth Manager
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/4/14 11:38 AM
 */
public class AuthManager {
    private static final String inner_key = "g7GKjj2Z";


    static boolean init(Context context, String auth) {
        try {
            URL url = new URL(auth);
            HashMap<String, String> queryMap = getQueryMap(url.getQuery());
            String t = queryMap.get("t");
            String c = queryMap.get("c");
            String original = xor(new String(Base64.getDecoder().decode(c.getBytes())), t + inner_key);

            AuthBean bean = new Gson().fromJson(original, AuthBean.class);
            TYNetworkManager.Companion.initialize(context,
                    bean.getAccessId(),
                    bean.getAccessSecret(),
                    Endpoint.AY);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    private static HashMap<String, String> getQueryMap(String query) {
        HashMap<String, String> queryMap = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            queryMap.put(keyValue[0], keyValue[1]);
        }
        return queryMap;
    }

    private static String xor(String data, String key) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char a = data.charAt(i);
            char b = key.charAt(i % key.length());

            builder.append((char) (a ^ b));
        }
        return builder.toString();
    }

    public static void clear() {
        KvGlobalManager.set(AuthConst.KEY, "");
    }

}
