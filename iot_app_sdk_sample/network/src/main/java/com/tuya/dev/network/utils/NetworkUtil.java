package com.tuya.dev.network.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.SparseArray;

import java.util.HashMap;

/**
 * 网络工具类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 12:13 PM
 */
public class NetworkUtil {
    public static final int CHINA_MOBILE = 1; // 中国移动
    public static final int CHINA_UNICOM = 2; // 中国联通
    public static final int CHINA_TELECOM = 3; // 中国电信


    public static final int SIM_OK = 0;
    public static final int SIM_NO = -1;
    public static final int SIM_UNKNOW = -2;
    /**
     * 无网络
     */
    public static final String CONN_TYPE_NONE = "none";
    /**
     * 未知网络类型
     */
    public static final String CONN_TYPE_UNKNOWN = "unknown";
    /**
     * wifi 网络
     */
    public static final String CONN_TYPE_WIFI = "wifi";
    /**
     * 移动网络
     */
    public static final String CONN_TYPE_GPRS = "gprs";
    /**
     * 有线网络
     */
    public static final String CONN_TYPE_ETHERNET = "ethernet";
    /**
     * 蓝牙网络（通过蓝牙共享上网）
     */
    public static final String CONN_TYPE_BLUETOOTH = "bluetooth";
    /**
     * vpn网络
     */
    public static final String CONN_TYPE_VPN = "vpn";
    /**
     * 无网络
     */
    public static final int MOBILE_NERWORK_TYPE_NONE = -2;
    /**
     * 非移动网络
     */
    public static final int MOBILE_NERWORK_TYPE_NOT_MOBILE = -1;
    /**
     * 2G移动网络
     */
    public static final int MOBILE_NERWORK_TYPE_2G = 2;
    /**
     * 3G移动网络
     */
    public static final int MOBILE_NERWORK_TYPE_3G = 3;
    /**
     * 4G移动网络
     */
    public static final int MOBILE_NERWORK_TYPE_4G = 4;
    /**
     * 5G移动网络
     */
    public static final int MOBILE_NERWORK_TYPE_5G = 5;
    /**
     * 未知移动网络
     */
    public static final int MOBILE_NERWORK_UNKNOWN = 999;
    /**
     * Current network is LTE_CA, 4G+网络的网络类型
     */
    public static final int NETWORK_TYPE_LTE_CA = 19;
    /**
     * Current network is NR(New Radio) 5G.
     */
    public static final int NETWORK_TYPE_NR = 20;
    public static boolean proxy = false;
    private static BroadcastReceiver connChangerRvr;
    private static SparseArray<String> connectionTypeArray;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            connectionTypeArray = new SparseArray<>();
            // 蜂窝数据
            connectionTypeArray.put(NetworkCapabilities.TRANSPORT_CELLULAR, CONN_TYPE_GPRS);
            // wifi网络
            connectionTypeArray.put(NetworkCapabilities.TRANSPORT_WIFI, CONN_TYPE_WIFI);
            // 蓝牙网络
            connectionTypeArray.put(NetworkCapabilities.TRANSPORT_BLUETOOTH, CONN_TYPE_BLUETOOTH);
            // 有线网络
            connectionTypeArray.put(NetworkCapabilities.TRANSPORT_ETHERNET, CONN_TYPE_ETHERNET);
            // vpn
            connectionTypeArray.put(NetworkCapabilities.TRANSPORT_VPN, CONN_TYPE_VPN);
        }

    }

    /**
     * 判断网络连接有效
     * <p>
     * Android P中弃用了判断网络的部分API，请使用 {@link #networkAvailable(Context context)} 代替
     *
     * @param context Context对象
     * @return 网络处于连接状态（3g or wifi)
     */
    @Deprecated
    public static boolean isNetworkAvailable(Context context) {
        return networkAvailable(context);
    }

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static boolean networkAvailable(Context context) {
        return !getNetConnType(context).equals(CONN_TYPE_NONE);
    }

    /**
     * 判断是否是WIFI
     */
    public static boolean isWifi(Context context) {
        return getNetConnType(context).equals(CONN_TYPE_WIFI);
    }

    /**
     * 判断是否是移动网络
     */
    public static boolean isMobile(Context context) {
        return getNetConnType(context).equals(CONN_TYPE_GPRS);
    }

    /**
     * 获取网络类型
     * <p>
     * Android P中弃用了判断网络的部分API，请使用 {@link #getNetConnType(Context)} 代替
     *
     * @param context Context对象
     * @return 当前处于连接状态的网络类型
     */
    @Deprecated
    private static String getNetworkType(Context context) {
        String result;
        ConnectivityManager connectivity = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity == null) {
            result = null;
        } else {

            try {
                NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
                if (networkInfo == null) {
                    result = null;
                } else {
                    String temp = networkInfo.getExtraInfo();
                    result = networkInfo.getTypeName() + " "
                            + networkInfo.getSubtypeName() + temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
        }
        return result;
    }

    /**
     * 获取SIM卡状态
     *
     * @return SIM_OK sim卡正常
     * SIM_NO		不存在sim卡
     * SIM_UNKNOW	sim卡状态未知
     */
    public static int getSimState(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            return SIM_OK;
        } else if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return SIM_NO;
        } else {
            return SIM_UNKNOW;
        }
    }

    /**
     * 使支持wap网络
     * 请再程序退出时调用unRegNetWorkRev方法
     */
    public static void supportWap(Context context) {
        supportWap(context, new DefaultApnUriGetter());

    }

    /**
     * 使客户端支持wap网络
     * 请在程序退出时调用unRegNetWorkRev方法
     *
     * @param context   Context实例
     * @param uriGetter 可采用DefaultApnUriGetter，对特定手机适配需自己实现
     */
    public static void supportWap(Context context, ApnUriGetter uriGetter) {
        HashMap<String, String> param = null;
        Uri[] uris = uriGetter.getUriList();
        for (Uri uri : uris) {
            if (uri != null)
                param = getProxyInfo(context, uri);
            if (param != null) {
                setProxy(param.get("host"), param.get("port"));
                break;
            }
        }
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connChangerRvr = new ConnectionChangeReceiver(uriGetter);
        context.registerReceiver(connChangerRvr, filter);

    }

    /**
     * 清空wap代理设置
     * 注销广播回调
     *
     * @param context Context实例
     */
    public static void unRegNetWorkRev(Context context) {
        setProxy(null, null);
        try {
            if (connChangerRvr != null)
                context.unregisterReceiver(connChangerRvr);
        } catch (Exception e) {  //register未完成时调用此方法可能报错
            e.printStackTrace();
            return;
        }
    }

    /**
     * 设置代理服务器，在wap拨号网络时手动设定
     * if host == null or host's length is 0
     * set unuse proxy
     *
     * @param host 代理服务器地址
     * @param port 代理端口
     */
    public static void setProxy(String host, String port) {
        if (host == null || host.length() == 0) {
            System.getProperties().put("proxySet", "false");
            proxy = false;
        } else {
            proxy = true;
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("proxyHost", host);
            if (port != null && port.length() > 0)
                System.getProperties().put("proxyPort", port);
            else
                System.getProperties().put("proxyPort", "80");
        }
    }

    /**
     * 获取当前网络的代理信息
     * 如果当前无网络 、网络为wifi 或mobile umts 则返回null
     * 如果当前apn找不到 则返回null
     */
    public static HashMap<String, String> getProxyInfo(Context context, Uri uri) {
        String result = getNetworkType(context);
        HashMap<String, String> proxy = new HashMap<>();
        if (result == null)
            return null;
        if (result.contains("WIFI") || result.compareToIgnoreCase("MOBILE UMTS") == 0) {
            return proxy;
        }
        Cursor cr = null;
        try {
            cr = context.getContentResolver().query(uri, null, "mcc ='460'", null, null);
            if (cr != null && cr.moveToFirst()) {
                do {
                    if (cr.getCount() > 0) {
                        proxy.put("host", cr.getString(cr.getColumnIndex("proxy")));
                        proxy.put("port", cr.getString(cr.getColumnIndex("port")));
                        String apn = cr.getString(cr.getColumnIndex("apn"));
                        if (result.contains(apn)) {
                            return proxy;
                        }
                    }

                } while (cr.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cr != null) {
                cr.close();
            }

        }
        return null;
    }

    /**
     * 判断当前的网络连接类型
     *
     * @param context context
     * @return 当前的网络连接类型
     * @see #CONN_TYPE_NONE
     * @see #CONN_TYPE_UNKNOWN
     * @see #CONN_TYPE_WIFI
     * @see #CONN_TYPE_VPN
     * @see #CONN_TYPE_GPRS
     * @see #CONN_TYPE_ETHERNET
     * @see #CONN_TYPE_BLUETOOTH
     */
    public static String getNetConnType(Context context) {
        if (context == null) {
            return CONN_TYPE_NONE;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return CONN_TYPE_NONE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return CONN_TYPE_NONE;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) {
                return CONN_TYPE_NONE;
            }
            for (int i = 0; i < connectionTypeArray.size(); i++) {
                int key = connectionTypeArray.keyAt(i);
                String connectionType = connectionTypeArray.get(key);
                if (networkCapabilities.hasTransport(key)) {
                    return connectionType;
                }
            }
        } else {
            // 以下部分API在Android P中弃用
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable() || !activeNetInfo.isConnectedOrConnecting()) {
                return CONN_TYPE_NONE;
            }
            switch (activeNetInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return CONN_TYPE_WIFI;
                case ConnectivityManager.TYPE_VPN:
                    return CONN_TYPE_VPN;
                case ConnectivityManager.TYPE_MOBILE:
                    return CONN_TYPE_GPRS;
                case ConnectivityManager.TYPE_ETHERNET:
                    return CONN_TYPE_ETHERNET;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    return CONN_TYPE_BLUETOOTH;
            }
        }
        return CONN_TYPE_UNKNOWN;
    }


    /**
     * 获取移动网络时的网络类型
     *
     * @param context
     * @return 使用移动数据时的网络类型
     * @see #MOBILE_NERWORK_TYPE_NONE
     * @see #MOBILE_NERWORK_TYPE_NOT_MOBILE
     * @see #MOBILE_NERWORK_TYPE_2G
     * @see #MOBILE_NERWORK_TYPE_3G
     * @see #MOBILE_NERWORK_TYPE_4G
     * @see #MOBILE_NERWORK_TYPE_5G
     * @see #MOBILE_NERWORK_UNKNOWN
     */
    public static int getMobileNetworkType(Context context) {
        String netConnType = getNetConnType(context);
        if (netConnType.equals(CONN_TYPE_GPRS)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return MOBILE_NERWORK_TYPE_NONE;
            }
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                case TelephonyManager.NETWORK_TYPE_GSM:
                    return MOBILE_NERWORK_TYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    return MOBILE_NERWORK_TYPE_3G;
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                case TelephonyManager.NETWORK_TYPE_LTE:
                case NETWORK_TYPE_LTE_CA:// 此类型是4G+
                    return MOBILE_NERWORK_TYPE_4G;
                case NETWORK_TYPE_NR:
                    return MOBILE_NERWORK_TYPE_5G;
                default:
                    return MOBILE_NERWORK_UNKNOWN;
            }
        } else if (netConnType.equals(CONN_TYPE_NONE)) {
            return MOBILE_NERWORK_TYPE_NONE;
        }
        return MOBILE_NERWORK_TYPE_NOT_MOBILE;
    }

    public static boolean networkUsable(Context ctx) {
        String connType = NetworkUtil.getNetConnType(ctx);
        return !connType.equals(NetworkUtil.CONN_TYPE_NONE);
    }

    @Deprecated
    public static boolean isWifiConnected(Context context) {
        return isWifi(context);
    }

    /**
     * 接口，获取手机的uri列表，适配各手机的Uri列表不同，在设置wap代理时使用
     */
    public interface ApnUriGetter {
        /**
         * 获取所有的Uri列表
         *
         * @return Uri列表
         */
        Uri[] getUriList();
    }

    public static class DefaultApnUriGetter implements ApnUriGetter {

        @Override
        public Uri[] getUriList() {
            Uri[] uris = new Uri[2];
            uris[0] = Uri.parse("content://telephony/carriers/preferapn");
            uris[1] = Uri.parse("content://telephony/carriers/current");
            return uris;
        }

    }

    public static class ConnectionChangeReceiver extends BroadcastReceiver {

        private ApnUriGetter uriGetter;

        public ConnectionChangeReceiver(ApnUriGetter uriGetter) {
            this.uriGetter = uriGetter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap<String, String> param = null;
            Uri[] uris = uriGetter.getUriList();
            for (int i = 0; i < uris.length; i++) {
                if (uris[i] != null)
                    param = getProxyInfo(context, uris[i]);
                if (param != null) {
                    setProxy(param.get("host"), param.get("port"));
                    break;
                }
            }
        }

    }
}
