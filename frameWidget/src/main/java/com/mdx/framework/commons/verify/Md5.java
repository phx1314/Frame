//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.commons.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5 {
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Md5() {
    }

    public static String md5(byte[] bytes) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes, 0, bytes.length);
        return bufferToHex(messageDigest.digest());
    }

    public static String md5(File file) throws Exception {
        return md5((InputStream)(new FileInputStream(file)));
    }

    public static String md5(InputStream in) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bt = new byte[1024];
            boolean ilen = false;

            int ilen1;
            while((ilen1 = in.read(bt)) >= 0) {
                messageDigest.update(bt, 0, ilen1);
            }

            String var4 = bufferToHex(messageDigest.digest());
            return var4;
        } finally {
            in.close();
        }
    }

    public static String mD5(String str) {
        try {
            return md5(str.getBytes());
        } catch (Exception var2) {
            return str;
        }
    }

    public static String md5(String str) throws Exception {
        return md5(str.getBytes());
    }

    public static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[bt >> 4 & 15];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
