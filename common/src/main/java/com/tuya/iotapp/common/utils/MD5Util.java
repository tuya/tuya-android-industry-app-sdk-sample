package com.tuya.iotapp.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * MD5Util
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 2:56 PM
 */
public class MD5Util {
    private static final int SIXTEEN_K = 1 << 14;

    /**
     * Computes the MD5 hash of the data in the given input stream and returns
     * it as an array of bytes.
     * Note this method closes the given input stream upon completion.
     */
    public static byte[] computeMD5Hash(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[SIXTEEN_K];
            int bytesRead;
            while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // should never get here
            throw new IllegalStateException(e);
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the MD5 in base64 for the data from the given input stream.
     * Note this method closes the given input stream upon completion.
     */
    public static String md5AsBase64(InputStream is) throws IOException {
        return HexUtil.bytesToHexString(computeMD5Hash(is));
    }

    /**
     * Computes the MD5 hash of the given data and returns it as an array of
     * bytes.
     */
    public static byte[] computeMD5Hash(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            // should never get here
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the MD5 in base64 for the given byte array.
     */
    public static String md5AsBase64(byte[] input) {
        return HexUtil.bytesToHexString(computeMD5Hash(input));
    }

    /**
     * Computes the MD5 of the given file.
     */
    public static byte[] computeMD5Hash(File file) throws IOException {
        return computeMD5Hash(new FileInputStream(file));
    }

    /**
     * Returns the MD5 in base64 for the given file.
     */
    public static String md5AsBase64(File file) throws IOException {
        return HexUtil.bytesToHexString(computeMD5Hash(file));
    }

    public static String md5AsBase64(String str) {
        return md5AsBase64(str.getBytes());
    }

    public static String md5AsBase64For16(String str) {
        return md5AsBase64(str).substring(8, 24);
    }

    public static String getHmacMd5Str(String s, String keyString) {
        String sEncodedString = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

            StringBuilder hash = new StringBuilder();

            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }
}
