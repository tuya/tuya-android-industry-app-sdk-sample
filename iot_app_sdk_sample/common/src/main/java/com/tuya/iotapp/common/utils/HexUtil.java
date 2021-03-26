package com.tuya.iotapp.common.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;

/**
 * HexUtil
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 2:57 PM
 */
public class HexUtil {
    public static String stringToHexString(String strPart) {
        String hexString = "";
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString = hexString + strHex;
        }
        return hexString;
    }

    private static String hexString = "0123456789ABCDEF";

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */

    public static String encode(String str) {

        // 根据默认编码获取字节数组

        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (byte aByte : bytes) {
            sb.append(hexString.charAt((aByte & 0xf0) >> 4));
            sb.append(hexString.charAt((aByte & 0x0f)));
        }
        return sb.toString();
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */

    public static String decode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());

    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 | _b1);
    }

    /**
     * Hex字串转换成字节流
     * <p/>
     * 　　* @param src Hex字串
     * <p/>
     * 　　* @return 字节流
     */
    public static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[6];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 6; ++i) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * Convert byte[] to hex
     * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * <p/>
     * 　　* @param src byte[] data
     * <p/>
     * 　　* @return hex string
     */

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 　　* Convert hex string to byte[]
     * <p/>
     * 　　* @param hexString the hex string
     * <p/>
     * 　　* @return byte[]
     */

    public static byte[] hexStringToBytes(String hexString) {

        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 　　* Convert char to byte
     * <p/>
     * 　　* @param c char
     * <p/>
     * 　　* @return byte
     */

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);

    }

    public static boolean checkHexString(String value) {
        if (TextUtils.isEmpty(value)) return false;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            if (!checkHexChar(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkHexChar(char ch) {
        return (ch >= '0') && (ch <= '9') || (ch >= 'A') && (ch <= 'F') || (ch >= 'a') && (ch <= 'f');
    }
}
