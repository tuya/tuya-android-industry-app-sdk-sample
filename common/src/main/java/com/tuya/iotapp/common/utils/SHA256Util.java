package com.tuya.iotapp.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  SHA256 工具类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 5:35 PM
 */
public class SHA256Util {
    private static byte[] getHash(String password) {
        return getHash(password.getBytes());
    }

    private static byte[] getHash(byte[] password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        if (digest != null) {
            digest.reset();
            return digest.digest(password);
        }
        return null;
    }

    public static String sha256(byte[] shaData) {
        byte[] data = getHash(shaData);
        if (data == null) return null;
        String s = HexUtil.bytesToHexString(data);
        if (s != null) return s.toLowerCase();
        return null;
    }

    public static String sha256(String strForEncrypt) {
        return sha256(strForEncrypt.getBytes());
    }
}
