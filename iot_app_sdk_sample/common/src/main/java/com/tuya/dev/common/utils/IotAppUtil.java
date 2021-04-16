package com.tuya.dev.common.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * IotAppUtil 语言国家
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 2:13 PM
 */
public class IotAppUtil {
    private static final String TAG = "IotAppUtil";
    private static boolean mqttBackgroundConnectEnable;
    private static boolean hardwareBackgroundConnectEnable;

    /**
     * 是否中文语言
     *
     * @param context
     * @return
     */
    public static boolean isZh(Context context) {
        return getLang(context).endsWith("zh");

    }

    /**
     * 获取语言标识
     *
     * @param context
     * @return
     */
    public static String getLang(Context context) {
        return IotCommonUtil.getLang(context);
    }

    /**
     * 获取国家代码
     *
     * @param context
     * @param def     默认值
     * @return
     */
    public static String getCountryCode(Context context, String def) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                return tm.getSimCountryIso().toUpperCase();
            }
        } catch (Exception ignored) {
        }
        return def;
    }

    public static boolean goToMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException ignored) {

            }
        }
        return false;
    }

    /**
     * 应用是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        String processName = context.getApplicationContext().getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance
                        != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.importance
                        != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
    }


    /**
     * 基础知识:
     * 最小是位 <其次字节(byte)<KB<MB<GB<TB
     * 一个字节 ＝8位
     * 1KB=1024B(字节)
     * 1MB=1024KB
     * 1GB=1024MB
     * 1TB=1024GB
     * <p/>
     * 规定：
     * 1、如果小于1M   =》1MB
     * 2、如果小于100M =》%dMB
     * 3、如果大于100M =》 %.2fGB
     * <p/>
     * 入参:
     * value => KB
     */
    public static String[] spaceFormat(long number) {
        String[] str = new String[]{"0", "MB"};
        if (number <= 0) {
            str[0] = "0";
            str[1] = "MB";
        } else if (number / 1024.f < 1) {
            str[0] = "1";
            str[1] = "MB";
        } else if (number / 1024.f < 100) {
            str[0] = String.format("%d", (long) (number / 1024.f));
            str[1] = "MB";
        } else if (number / 1024.f / 1024.f < 100) {
            str[0] = String.format("%.2f", (number / 1024.f / 1024.f));
            str[1] = "GB";
        } else {
            str[0] = String.format("%d", (long) (number / 1024.f / 1024.f));
            str[1] = "GB";
        }

        return str;
    }


    /**
     * 获取现实字符串， 10485760 ==> 10GB
     *
     * @param number
     * @return
     */
    public static String getSpaceString(long number) {
        String[] strings = spaceFormat(number);
        String[] strings1 = TextUtils.split(strings[0], "\\.");

        return strings1[0] + strings[1];
    }

    /**
     * 规则：
     * 等于 0%    => 0%
     * 小于 0.01% => 0.01%
     * 小于 10%   => %.2f%%
     * 大于 10%   => %d%%
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 10%
     */
    public static String spacePercent(long numerator, long denominator) {

        float percent = (float) numerator / denominator * 100;

        String str;

        if (percent == 0) {
            str = "0%";
        } else if (percent <= 0.01) {
            str = "0.01%";
        } else if (percent < 10) {
            str = String.format("%.2f%%", percent);
        } else {
            str = String.format("%d%%", (long) percent);
        }

        return str;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 计算屏幕大小
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (context == null) return null;
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 判断是否安装某个APP
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return installed;
    }

    /**
     * drawable2bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] drawable2bytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    /**
     * 通过seesionid获取uid
     *
     * @param sessionId
     * @return
     */
    public static String getUidBySessionId(String sessionId) {
        String str = sessionId.substring(0, sessionId.length() - 32);
        String str1 = str.substring(0, str.length() - 1);
        Integer str2 = Integer.valueOf(str.substring(str.length() - 1, str.length()));
        Integer uidLength = str1.length() - str2;
        float ff = (float) uidLength / 8;
        int div = (int) Math.ceil(ff);
        Integer start = 0;
        String str3 = "";
        for (int i = 0; i < div; i++) {
            Integer end = (i + 1) * 8;
            if (end > uidLength) {
                end = uidLength;
            }
            str3 += str1.substring(start, end + i);
            start = end + i + 1;
        }
        return str3;
    }

    /**
     * 获取Intent
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        for (ResolveInfo serviceInfo : resolveInfo) {
//            L.d(TAG,"serviceInfo: "+serviceInfo.serviceInfo.packageName);
            if (checkServiceProcess(context, serviceInfo.serviceInfo.packageName)) {
                Intent explicitIntent = new Intent(implicitIntent);
                explicitIntent.setPackage(serviceInfo.serviceInfo.packageName);
                return explicitIntent;
            }
        }
        return null;
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent, String className) {
        if (context == null) return null;
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager == null) return null;
        List<ActivityManager.RunningServiceInfo> runningServices = mActivityManager
                .getRunningServices(Integer.MAX_VALUE);
        //可能为空
        if (runningServices == null) return null;
        for (ActivityManager.RunningServiceInfo appProcess : runningServices) {
            if (TextUtils.equals(className, appProcess.service.getClassName())) {
                Intent explicitIntent = new Intent(implicitIntent);
//                L.d(TAG,"appProcess "+appProcess.service.getPackageName());
                explicitIntent.setPackage(appProcess.service.getPackageName());
                return explicitIntent;
            }
        }
        return null;
    }

    public static boolean checkServiceProcess(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager == null) return false;
        for (ActivityManager.RunningServiceInfo appProcess : mActivityManager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (TextUtils.equals(packageName, appProcess.service.getPackageName())) {
//                L.d(TAG,"appProcess: true "+appProcess.service.getPackageName());
                return true;
            }
//            L.d(TAG,"appProcess:  "+appProcess.service.getPackageName());
        }
        return false;
    }


    /**
     * stirng to date
     *
     * @param value
     * @param format
     * @return
     */
    public static Date formatDate(String value, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(int time, String format) {
        return formatDate(time * 1000l, format);
    }

    public static String formatDate(long mill, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return dateFormat.format(new Date(mill));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized static String getApplicationName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            appName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(appName)) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
        }
        return appName;
    }

    public static int stringToInt(String progress) {
        try {
            return Integer.valueOf(progress);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean checkBvVersion(String v1, float v2) {
        return !TextUtils.isEmpty(v1) && Float.valueOf(v1) >= v2;
    }

    public static boolean checkPvVersion(String v1, float v2) {
        return !TextUtils.isEmpty(v1) && Float.valueOf(v1) >= v2;
    }

    public static boolean checkPvLastVersion(String v1, float v2) {
        return !TextUtils.isEmpty(v1) && Float.valueOf(v1) > v2;
    }

    public static boolean isHgwVersionEquals(String v1, String v2) {
        if (TextUtils.isEmpty(v1)) return false;
        v1 = v1.replace("v", "");
        return TextUtils.equals(v1, v2);
    }

    public static boolean checkHgwVersion(String v1, float v2) {
        try {
            if (TextUtils.isEmpty(v1)) return false;
            v1 = v1.replace("v", "");
            return Float.valueOf(v1) >= v2;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkHgwLastVersion(String v1, float v2) {
        if (TextUtils.isEmpty(v1)) return false;
        v1 = v1.replace("v", "");
        return Float.valueOf(v1) > v2;
    }

    // "x.x" > "x.x"
    public static boolean checkServiceVersion(String v1, String v2) {
        return TextUtils.isEmpty(v1) || Float.valueOf(v1) < Float.valueOf(v2);
    }

    public static boolean hasLatOrLon(String lat, String lon) {
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) return false;
        try {
            double latD = Double.valueOf(lat);
            double lonD = Double.valueOf(lon);
            if (latD <= 0.00001 && lonD <= 0.00001) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static long absoluteValue(long value) {
        return value > 0 ? value : -value;
    }

    public static String getString(Context context, String resId, String defaultValue) {
        try {
            return context.getString(context.getResources().getIdentifier(resId, "string", context.getPackageName()));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getDrawableResId(Context context, String resId, int defaultValue) {
        try {
            return context.getResources().getIdentifier(resId, "drawable", context.getPackageName());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param version1
     * @param version2
     * @return if version1 > version2, return 1, if equal, return 0, else return
     * -1
     */
    public static int compare(String version1, String version2) {
        if (version1 == null || version1.length() == 0 || version2 == null
                || version2.length() == 0) {
            throw new IllegalArgumentException("Invalid parameter!");
        }
        int[] number1 = getValue(version1);
        int[] number2 = getValue(version2);
        int index = 0;
        while (index < number1.length && index < number2.length) {
            if (number1[index] < number2[index]) {
                return -1;
            } else if (number1[index] > number2[index]) {
                return 1;
            } else {
                index++;
            }
        }
        if (number1.length == number2.length) {
            return 0;
        } else if (number1.length > number2.length) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int[] getValue(String version) {
        String[] strings = version.split("\\.");
        int[] versionArray = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            try {
                versionArray[i] = Integer.valueOf(strings[i]);
            } catch (Exception e) {
                versionArray[i] = 0;
            }
        }
        return versionArray;
    }

    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version1.length() == 0 || version2 == null
                || version2.length() == 0) {
            return -1;
        }
        return compare(version1, version2);
    }

    /**
     * 应用是否处于前台
     *
     * @return
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (manager == null) return false;
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = manager.getRunningAppProcesses();
        if (appProcessInfos == null || appProcessInfos.isEmpty()) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : appProcessInfos) {
            //当前应用处于运行中，并且在前台
            if (info.processName.equals(context.getPackageName()) && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static byte[] getAssetsData(Context context, String path, byte[] defaultValue) {
        try {
            InputStream mAssets = context.getApplicationContext().getAssets().open(path);
            int lenght = mAssets.available();
            byte[] buffer = new byte[lenght];
            mAssets.read(buffer);
            mAssets.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * startForegroundService() was introduced in O, just call startService
     * for before O.
     *
     * @param context Context to start Service from.
     * @param intent  The description of the Service to start.
     */
    public static void startForegroundService(@NonNull Context context, @NonNull Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            // Pre-O behavior.
            context.startService(intent);
        }
    }

    public static boolean isMainProgress(Context context) {
        try {
            return TextUtils.equals(getProcessName(context), context.getPackageName());
        } catch (Exception e) {
            LogUtils.d(TAG, "check main progress error: " + e);
        }
        return false;
    }


    private static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        try {
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (mActivityManager != null) {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager
                        .getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
                        if (appProcess.pid == pid) {
                            return appProcess.processName;
                        }
                    }
                }
            }
        } catch (Throwable ignore) {
        }
        return getProcessNameFromActivityThread();
    }

    public static String getProcessNameFromActivityThread() {
        if (Build.VERSION.SDK_INT >= 28)
            return Application.getProcessName();
        else {
            try {
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                // Before API 18, the method was incorrectly named "currentPackageName", but it still returned the process name
                // See https://github.com/aosp-mirror/platform_frameworks_base/commit/b57a50bd16ce25db441da5c1b63d48721bb90687
                String methodName = Build.VERSION.SDK_INT >= 18 ? "currentProcessName" : "currentPackageName";
                Method getProcessName = activityThread.getDeclaredMethod(methodName);
                return (String) getProcessName.invoke(null);
            } catch (Throwable e) {
                LogUtils.d(TAG, "get progressName error: " + e);
            }
        }
        return "";
    }

    public static Application getSystemApp() {
        Application application = null;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            Field initialApplicationField = activityThread.getDeclaredField("mInitialApplication");
            initialApplicationField.setAccessible(true);
            Object current = currentActivityThread.invoke(null);
            application = (Application) initialApplicationField.get(current);
        } catch (Throwable ignore) {
        }
        try {
            if (application == null) {
                application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null, (Object[]) null);
            }
        } catch (Throwable ignore) {
        }
        try {
            if (application == null) {
                application = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication").invoke(null, (Object[]) null);
            }
        } catch (Throwable ignore) {
        }
        return application;
    }

    public static void enableMqttBackgroundConnect() {
        mqttBackgroundConnectEnable = true;
    }

    public static boolean isMqttBackgroundConnectEnable() {
        return mqttBackgroundConnectEnable;
    }

    public static boolean isHardwareBackgroundConnectEnable() {
        return hardwareBackgroundConnectEnable;
    }

    public static void enableHardwareBackgroundConnect() {
        hardwareBackgroundConnectEnable = true;
    }
}
