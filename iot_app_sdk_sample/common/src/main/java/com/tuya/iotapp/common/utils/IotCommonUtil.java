package com.tuya.iotapp.common.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * 国家、时区
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 2:15 PM
 */
public class IotCommonUtil {

    /**
     * 判断是否是中国
     * 1、手机卡国家区号
     * 2、时区判断
     *
     * @return
     */
    public static boolean isChina(Context ctx) {
        try {
            String countryCode = getCountryCode(ctx, null);
            if (TextUtils.isEmpty(countryCode)) {
                TimeZone tz = TimeZone.getDefault();
                String id = tz.getID();
                return TextUtils.equals(id, "Asia/Shanghai");
            }
            return "CN".equals(countryCode);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 是否中文语言
     *
     * @param context
     * @return
     */
    public static boolean isZh(Context context) {
        String lang = getLang(context);
        if (TextUtils.isEmpty(lang)) {
            return false;
        } else if (lang.contains("-hans") || "zh_cn".equals(lang.toLowerCase())) {
            return true;
        }
        return false;

    }

    /**
     * 获取语言标识
     *
     * @param context
     * @return
     */
    public static String getLang(Context context) {

        if (context == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }

        String language = locale.getLanguage();
        stringBuilder.append(language);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String script = locale.getScript();
            if (!TextUtils.isEmpty(script)) {
                stringBuilder.append("_").append(script);
            }
        }
        String country = locale.getCountry();
        if (!TextUtils.isEmpty(country)) {
            stringBuilder.append("_").append(country);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取国家代码
     *
     * @param context
     * @param def     默认值
     * @return
     */
    public static String getCountryCode(Context context, String def) {
        return IotAppUtil.getCountryCode(context, def);
    }


    public static boolean goToMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
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

        if (activityManager == null || keyguardManager == null) {
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
     * <p>
     * 规定：
     * 1、如果小于1M   =》1MB
     * 2、如果小于100M =》%dMB
     * 3、如果大于100M =》 %.2fGB
     * <p>
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
     * 获取手机本地时区
     */
    public static String getTimeZone() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("Z", Locale.US);
            String str = sdf.format(new Date());
            if (!TextUtils.isEmpty(str)) {
                return str.substring(0, 3) + ":" + str.substring(3);
            } else {
                return defaultZone();
            }
        } catch (Exception e) {
            return defaultZone();
        }
    }

    private static String defaultZone() {
        TimeZone timeZone = DateFormat.getInstance().getTimeZone();
        return getTimeZoneByRawOffset(timeZone.getRawOffset() + timeZone.getDSTSavings());
    }

    public static String getTimeZoneByRawOffset(int rawOffset) {
        String timeDisplay = rawOffset >= 0 ? "+" : "";
        int hour = rawOffset / 1000 / 3600;
        int minute = (rawOffset - hour * 1000 * 3600) / 1000 / 60;
        timeDisplay += String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        return timeDisplay;
    }


    public static String getCountryNumberCodeByTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        if (TextUtils.equals(id, "Asia/Shanghai")) {
            return "86";
        } else {
            return "1";
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimezoneGCMById(String timezoneId) {
        int timeZoneByRawOffset;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            android.icu.util.TimeZone timeZone = android.icu.util.TimeZone.getTimeZone(timezoneId);
            timeZoneByRawOffset = timeZone.getRawOffset() + timeZone.getDSTSavings();
        } else {
            TimeZone timeZone = SimpleTimeZone.getTimeZone(timezoneId);
            timeZoneByRawOffset = timeZone.getRawOffset() + timeZone.getDSTSavings();
        }
        return getTimeZoneByRawOffset(timeZoneByRawOffset);
    }


    public static String getTimeZoneId() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }
}
