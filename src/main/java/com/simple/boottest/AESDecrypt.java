package com.simple.boottest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

@RestController
public class AESDecrypt {
    private static final String M_AES_ENCRYPT_KEY	= "xtrmAdviserVoc012345678910111213";
    public static String alg = "AES/CBC/PKCS5Padding";
    private static byte[] aes_ivBytes				= { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    private static final int ARIA_BLOCK_SIZE		= 16;

    @GetMapping("/decryptTest")
    public static String decryptTest() throws Exception {
        return "test";
    }

    @GetMapping("/decryptAES")
    public static String decryptAES() throws Exception {
        String strEnc = "07585cb8e86cd7f087790559d88e7df4ef7cd0deab18b2ba41b5a7733dfe3eb2";
        String strKey = "cffe3d2dd6f61e515f7480a2c7b95f7a";
        byte[] textBytes = Base64.decodeBase64(strEnc);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes_ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

    @GetMapping("/decryptAES_basic")
    public String decryptAES256() throws Exception {

        String rtn = "07585cb8e86cd7f087790559d88e7df4ef7cd0deab18b2ba41b5a7733dfe3eb2";
//        String strKey = "cffe3d2dd6f61e515f7480a2c7b95f7a";
        String strKey = "xtrmAdviserVoc012345678910111213";

        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(aes_ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        byte[] encrypted = cipher.doFinal("1q2w3e4r!".getBytes());
        System.out.println("암호화된 데이터: " + new String(encrypted));

        // 복호화
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        byte[] original = cipher.doFinal(encrypted);
        System.out.println("복호화된 데이터: " + new String(original));

        return "hhhh";


//        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
//
//        byte[] decodedBytes = java.util.Base64.getDecoder().decode(rtn);
//        byte[] decrypted = cipher.doFinal(decodedBytes);
//        return new String(decrypted, "UTF-8");
    }


    @GetMapping("/decryptAES2")
    public String decryptAES2562() throws Exception {

        String rtn = "07585cb8e86cd7f087790559d88e7df4ef7cd0deab18b2ba41b5a7733dfe3eb2";
//        String strKey = "cffe3d2dd6f61e515f7480a2c7b95f7a";

//        Cipher cipher = Cipher.getInstance(alg);
/*        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParamSpec = new IvParameterSpec(aes_ivBytes);
        SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        byte[] encrypted = cipher.doFinal("1q2w3e4r!".getBytes());
        byte[] textBytes = "1q2w3e4r!".getBytes("UTF-8");
        System.out.println("암호화된 데이터: ");
        System.out.println("암호화된 데이터 1: " + Base64.encodeBase64String(cipher.doFinal(textBytes)) );
        System.out.println("암호화된 데이터 2: " + new String(encrypted));*/
        String strOrg = "1q2w3e4r!";
        String strKey = M_AES_ENCRYPT_KEY;  //M_AES_ENCRYPT_KEY
        encryptAES(strOrg, strKey);
        System.out.println("암호화된 데이터 1: " + encryptAES(strOrg, strKey));
        System.out.println("복호화된 데이터 2: " + decrypt( encryptAES(strOrg, strKey), strKey ) );


        String companyCode = "1000";
        //회사코드와 기본 키값을 연결후 특수문자 제외
        String encryptKey = replaceSpecialCharacters(companyCode + strKey);
        //공통 AES Key로 키 값을 암호화
        String newEncryptKey = encryptAES(encryptKey, M_AES_ENCRYPT_KEY);
        // 암호화 키 제한 ( 32 byte ) 초과시 키 Length 제한
        newEncryptKey = newEncryptKey.length() > 32 ? newEncryptKey.substring(0,32) : newEncryptKey ;
        //암호화 값 리턴
        encryptAES(strOrg, newEncryptKey);
        System.out.println("암호화된 데이터 2: " + encryptAES(strOrg, newEncryptKey));
        System.out.println("복호화된 데이터 2: " + decrypt( encryptAES(strOrg, newEncryptKey), newEncryptKey ) );

        System.out.println("복호화된 데이터 3: " + decrypt( "7a13e3071d349c85e7fde15302d8a4853050045a43afdd36a3ea5d3f86735f75") );
        return "hhhh//";
    }

    //암호화
    public static String encryptAES(String strOrg, String strKey) throws Exception {
//        String strOrg = "1q2w3e4r!";
//        String strKey = M_AES_ENCRYPT_KEY;  //M_AES_ENCRYPT_KEY
        String companyCode = "1000";

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParamSpec = new IvParameterSpec(aes_ivBytes);
        SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        byte[] encrypted = cipher.doFinal("1q2w3e4r!".getBytes());
        byte[] textBytes = strOrg.getBytes("UTF-8");
//        System.out.println("암호화된 데이터: ");
//        System.out.println("암호화된 데이터 1: " + Base64.encodeBase64String(cipher.doFinal(textBytes)) );
//        System.out.println("암호화된 데이터 2: " + new String(encrypted));

        return Base64.encodeBase64String(cipher.doFinal(textBytes));
    }

    public static String decrypt(String strEnc, String strKey) throws Exception {
        byte[] textBytes = Base64.decodeBase64(strEnc);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes_ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(textBytes), "UTF-8");
    }

    // -----------------------
    public static String decrypt(String encryptedString) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, generateKey(null));
        byte[] original = cipher.doFinal(toByteArray(encryptedString));
        return new String(original);
    }
    // -----------------------

    private static Key generateKey(String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (secretKey == null) {
            secretKey = M_AES_ENCRYPT_KEY;
        }
        byte[] key = (secretKey).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, ARIA_BLOCK_SIZE); // use only the first 128 bit
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available
        return new SecretKeySpec(key, "AES");
    }

    public static byte[] generateAESKey(int intBit) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(intBit);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec newKey = new SecretKeySpec(raw, "AES");
        return newKey.getEncoded();
    }

    /**
     * 모든 특수문자를 제거
     * @param strValue 특수문자가 포함된 문자열
     * @return 특수문자가 제거된 값
     */
    private static String replaceSpecialCharacters(String strValue) {
        return strValue.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "");
    }


    private static byte[] toByteArray(String hexString) {
        int arrLength = hexString.length() >> 1;
        byte buf[] = new byte[arrLength];
        for (int ii = 0; ii < arrLength; ii++) {
            int index = ii << 1;
            String l_digit = hexString.substring(index, index + 2);
            buf[ii] = (byte) Integer.parseInt(l_digit, 16);
        }
        return buf;
    }

}
