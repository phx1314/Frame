//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.commons.verify;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
    private byte[] desKey;
    private static final byte[] iv1 = new byte[]{(byte)18, (byte)52, (byte)86, (byte)120, (byte)-112, (byte)-85, (byte)-51, (byte)-17};

    public DES() {
        String desKey = "udows!@#";
        this.desKey = desKey.getBytes();
    }

    public byte[] desEncrypt(byte[] plainText) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        byte[] rawKeyData = this.desKey;
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(1, key, iv);
        byte[] encryptedData = cipher.doFinal(plainText);
        return encryptedData;
    }

    public byte[] desDecrypt(byte[] encryptText) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(iv1);
        byte[] rawKeyData = this.desKey;
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, key, iv);
        byte[] decryptedData = cipher.doFinal(encryptText);
        return decryptedData;
    }
}
