package com.simple.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES256 {

    public static String alg = "AES/CBC/PKCS5Padding";
    private final String key = "miraendevapril00devaprilmiraen00"; // 32byte
    private String iv = "miraen___project"; // 16byte

    // 암호화
    public String encryptAES256(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        String rtn = Base64.getEncoder().encodeToString(encrypted);

        rtn = rtn.contains("+") ? rtn.replace("+", "%2B") : rtn;
        rtn = rtn.contains("=") ? rtn.replace("=", "%3D") : rtn;
        rtn = rtn.contains("/") ? rtn.replace("/", "%2F") : rtn;

        return rtn;
    }

    // 복호화
    public String decryptAES256(String rtn) throws Exception {

        rtn = rtn.contains("%2B") ? rtn.replace("%2B", "+") : rtn;
        rtn = rtn.contains("%3D") ? rtn.replace("%3D", "=") : rtn;
        rtn = rtn.contains("%2F") ? rtn.replace("%2F", "/") : rtn;


        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(rtn);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }
}
