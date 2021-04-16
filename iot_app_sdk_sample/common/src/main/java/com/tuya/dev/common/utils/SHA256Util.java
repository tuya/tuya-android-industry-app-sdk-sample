package com.tuya.dev.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * SHA256 工具类
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


    /**
     * HMACSHA256
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());
            String hash = new HexBinaryAdapter().marshal(bytes).toUpperCase();
            return hash;
        } catch (Exception e) {
            LogUtils.d("mac", " error");
        }
        return null;
    }
}
