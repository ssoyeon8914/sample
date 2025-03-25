package com.simple.boottest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

@RestController
public class AESDecrypt2 {
    private static final String M_AES_ENCRYPT_KEY = "xtrmAdviserVoc012345678910111213";
    public static String alg = "AES/CBC/PKCS5Padding";
    private static byte[] aes_ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final int ARIA_BLOCK_SIZE = 16;

    @GetMapping("/decryptHash")
    public static void decryptHash() throws Exception {
        String i_companyCode = "1000"; // 예시 값
        String i_userId = "itx202500594"; // 예시 값

        String result = generateHash(i_companyCode, i_userId);
        System.out.println("결과: " + result);
    }

    public static String generateHash(String i_companyCode, String i_userId) {
        try {
            // 입력 문자열 결합
            String input = i_companyCode + i_userId;

            // SHA-256 해시 생성
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // HEX로 인코딩
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // 첫 32자 추출
            return hexString.substring(0, 32);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }



    @GetMapping("/pp")
    public static void password() throws Exception {
        String i_companyCode = "1000"; // 예시 값
        String i_userId = "itx202500594"; // 예시 값
        String strEncryptKey = "cffe3d2dd6f61e515f7480a2c7b95f7a";
        String passwordEncrypt = "07585cb8e86cd7f087790559d88e7df4ef7cd0deab18b2ba41b5a7733dfe3eb2";
        String password = passwordEncrypt.replace(" ", "+");

        decryptAES(password, strEncryptKey);
    }


    public static String decryptAES(String strEnc, String strKey) throws Exception {
        byte[] textBytes = Base64.decodeBase64(strEnc);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes_ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

}
