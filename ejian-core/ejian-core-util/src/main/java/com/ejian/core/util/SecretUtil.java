package com.ejian.core.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * SecretUtil
 *
 * @author wjl
 * @since 1.0.0
 * @date  2023年11月3日10:14:03
 */
public class SecretUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final String CHAR_SET_ENCODING = "UTF-8";

    private static final String SECRET_TYPE_MD5 = "MD5";

    private static final String SECRET_TYPE_SHA1 = "SHA1";

    private static final String SECRET_TYPE_SHA256 = "SHA-256";
    private static final String SECRET_TYPE_CES = "DESede";

    public static final String SECRET_TYPE_RSA = "RSA";

    public static final int KEY_SIZE = 1024;

    public static final String PUBLIC_KEY = "publicKey";

    public static final String PRIVATE_KEY = "privateKey";

    /**
     * MD5加密算法
     *
     * @param str 待加密字符串
     * @return 加密字符串
     */
    public static String MD5(String str) {
        return MD5(str, CHAR_SET_ENCODING);
    }

    public static String md5(String str) {
        return Objects.requireNonNull(MD5(str, CHAR_SET_ENCODING)).toLowerCase();
    }

    public static String MD5(String str, String encoding) {
        try {
            byte[] btInput = str.getBytes(encoding);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(SECRET_TYPE_MD5);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                chars[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                chars[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            LoggerUtil.error(e.fillInStackTrace().toString());
            return "";
        }
    }

    /**
     * SHA1 机密算法
     *
     * @param str 待加密字符串
     * @return 加密字符串
     */
    public static String SHA1(String str) {
        return SHA(str, SECRET_TYPE_SHA1);
    }

    /**
     * SHA-256 机密算法
     *
     * @param str 待加密字符串
     * @return 加密字符串
     */
    public static String SHA256(String str) {
        return SHA(str, SECRET_TYPE_SHA256);
    }


    /**
     * 3DES解密
     *
     * @param value 待解密字符串
     * @param key   原始密钥字符串
     * @return 解密后字符串
     * @throws Exception
     */
    public static String Decrypt3DES(String value, String key) throws Exception {
        byte[] b = decryptMode(key.getBytes(), Base64.getDecoder().decode(value));
        return new String(b, StandardCharsets.UTF_8);
    }


    /**
     * 3des加密
     *
     * @param value 待加密字符串
     * @param key   原始密钥字符串
     * @return 加密后字符串
     * @throws Exception
     */
    public static String Encrypt3DES(String value, String key) {
        return byte2Base64(encryptMode(key.getBytes(), value.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 3des加密 可自己设置编码
     *
     * @param data   待加密串
     * @param key    24位秘钥
     * @param encode 编码
     * @return 输出结果为16进制
     * @throws Exception
     */
    public static String encrypt3desToHex(String data, String key, String encode) throws Exception {
        return getFormattedText(Objects.requireNonNull(encryptMode(key.getBytes(encode), data.getBytes(encode))));
    }

    /**
     * 3des解密 可以自己设置编码
     *
     * @param value  16进制字符
     * @param key    24位秘钥
     * @param encode 编码
     * @return
     * @throws Exception
     */
    public static String decrypt3desToHex(String value, String key, String encode) throws Exception {
        byte[] b = decryptMode(key.getBytes(encode), hexStringToBytes(value));
        return new String(b, encode);
    }

    /**
     * @param encryptdata 要加密的明码
     * @param encryptkey  加密的密钥
     * @return 加密后的暗码
     */
    public static String encrypt(String encryptdata, String encryptkey) {
        try {
            DesUtil desPlus = new DesUtil(encryptkey);
            return desPlus.encrypt(encryptdata);
        } catch (Exception e) {
            LoggerUtil.error(e.fillInStackTrace().toString());
        }
        return "";
    }

    /**
     * @param decryptdata 要解密的暗码
     * @param decryptkey  解密的密钥
     * @return 解密后的明码
     * @throws Exception
     */
    public static String decrypt(String decryptdata, String decryptkey)
            throws Exception {
        DesUtil desPlus = new DesUtil(decryptkey);
        return desPlus.decrypt(decryptdata);
    }

    // 非对称密钥算法

    /**
     * 初始化生成公钥私钥的类
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException {
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SECRET_TYPE_RSA);
        // 初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator;
    }

    /**
     * 生成秘钥对
     *
     * @return Map
     */
    public static Map<String, Object> createKey() {
        // 将生成的公钥私钥放到map中
        Map<String, Object> keyMap = new HashMap<>();
        try {
            KeyPairGenerator keyPairGenerator = initKeyPairGenerator();
            // 生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);

        } catch (NoSuchAlgorithmException e) {
            LoggerUtil.error("获取加密实例出错，没有找到对应的加密方式" + e.getMessage(), e);
        }
        return keyMap;

    }

    /**
     * 从秘钥对中获取公钥 Str
     *
     * @param keyMap 生成的秘钥对
     * @return
     */
    public static String getPublicKeyStr(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return byte2Base64(key.getEncoded());
    }

    /**
     * 从秘钥对中获取私钥 Str
     *
     * @param keyMap 生成的秘钥对
     * @return
     */
    public static String getPrivateKeyStr(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return byte2Base64(key.getEncoded());
    }

    /**
     * 公钥加密
     *
     * @param data 待加密字符串
     * @param key  公钥
     * @return
     */
    public static String rsaEncryptByPublicKey(String data, String key) {
        return Base64.getEncoder().encodeToString(encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(key)));
    }

    /**
     * 公钥解密
     *
     * @param data 待解密字符串
     * @param key  公钥
     * @return
     */
    public static String rsaDecryptByPublicKey(String data, String key) {

        try {
            return new String(decryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(key)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LoggerUtil.error("公钥解密异常", e);
        }
        return null;
    }


    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String rsaEncryptByPrivateKey(String data, String key) {
        try {
            byte[] decryptStr = encryptByPrivateKey(data.getBytes(), Base64.getDecoder().decode(key));
            return Base64.getEncoder().encodeToString(decryptStr);
        } catch (Exception e) {
            LoggerUtil.error("私钥加密异常", e);
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return String 解密数据
     */
    public static String rsaDecryptByPrivateKey(String data, String key) {
        byte[] decryptStr = decryptByPrivateKey(Base64.getDecoder().decode(data), Base64.getDecoder().decode(key));
        return new String(decryptStr, StandardCharsets.UTF_8);
    }

    /**
     * 加密解密共用核心代码，分段加密解密
     *
     * @param decryptData
     * @param cipher
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static byte[] doFinal(byte[] decryptData, Cipher cipher, int blockSize)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        int offSet = 0;
        byte[] cache = null;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (decryptData.length - offSet > 0) {
            if (decryptData.length - offSet > blockSize) {
                cache = cipher.doFinal(decryptData, offSet, blockSize);
            } else {
                cache = cipher.doFinal(decryptData, offSet, decryptData.length - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * blockSize;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 将十六进制字符串转换为字节
     *
     * @param hexString 需转换字符串
     * @return 字节
     */
    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
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

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, SECRET_TYPE_CES); // 加密
            Cipher c1 = Cipher.getInstance(SECRET_TYPE_CES);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e3) {
            LoggerUtil.error(e3.fillInStackTrace().toString());
        }
        return null;
    }

    private static String SHA(String str, String instanceType) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(instanceType);
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    private static byte[] decryptMode(byte[] keyByte, byte[] src) {
        try { // 生成密钥
            SecretKey desKey = new SecretKeySpec(keyByte, SECRET_TYPE_CES);
            // 解密
            Cipher c1 = Cipher.getInstance(SECRET_TYPE_CES);
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(src);
        } catch (Exception e3) {
            LoggerUtil.error(e3.fillInStackTrace().toString());
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    private static byte[] decryptByPrivateKey(byte[] data, byte[] key) {
        try {
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(SECRET_TYPE_RSA);
            // 生成私钥
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int blockSize = cipher.getOutputSize(data.length);
            return doFinal(data, cipher, blockSize);
        } catch (NoSuchAlgorithmException e) {
            LoggerUtil.error("私钥解密异常：NoSuchAlgorithmException", e);
        } catch (InvalidKeySpecException e) {
            LoggerUtil.error("私钥解密异常：InvalidKeySpecException", e);
        } catch (NoSuchPaddingException e) {
            LoggerUtil.error("私钥解密异常：NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            LoggerUtil.error("私钥解密异常：InvalidKeyException", e);
        } catch (IllegalBlockSizeException e) {
            LoggerUtil.error("私钥解密异常：IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            LoggerUtil.error("私钥解密异常：BadPaddingException", e);
        } catch (IOException e) {
            LoggerUtil.error("私钥解密异常：IOException", e);
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    private static byte[] encryptByPublicKey(byte[] data, byte[] key) {

        // 实例化密钥工厂
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(SECRET_TYPE_RSA);
            // 密钥材料转换
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
            // 产生公钥
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            // 数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            int blockSize = cipher.getOutputSize(data.length) - 11;
            return doFinal(data, cipher, blockSize);

        } catch (NoSuchAlgorithmException e) {
            LoggerUtil.error("公钥加密出错：NoSuchAlgorithmException", e);
        } catch (InvalidKeySpecException e) {
            LoggerUtil.error("公钥加密异常：InvalidKeySpecException", e);
        } catch (NoSuchPaddingException e) {
            LoggerUtil.error("公钥加密异常：NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            LoggerUtil.error("公钥加密异常：InvalidKeyException", e);
        } catch (IllegalBlockSizeException e) {
            LoggerUtil.error("公钥加密异常：IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            LoggerUtil.error("公钥加密异常：BadPaddingException", e);
        } catch (IOException e) {
            LoggerUtil.error("公钥加密异常：IOException", e);
        }
        return null;
    }

    private static byte[] decryptByPublicKey(byte[] data, byte[] key)
            throws Exception {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(SECRET_TYPE_RSA);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int blockSize = cipher.getOutputSize(data.length) - 11;

        return doFinal(data, cipher, blockSize);
    }

    /**
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(SECRET_TYPE_RSA);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    private static String byte2Base64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    public static void main(String[] args) throws Exception {
        String deStr = "l9MR2B/wuXChC7fDPf061WKmgLCbEdyly3/LEcGxz507/52VPDuHxtnHiXOkhoSRHjBeQJMeFfUWlqbdN5C+MOK+3Lf4tO67BcnJdXpPGJYA8sggtz2IbHhElz8Cmk+WAdBdSyWUYcauxrvPky5tCC5fDIZvxunCvwHsh4aKKpVSviMpnQPWwqnJpbqgcBvw9J+3nJDJHmJLVG3QxJz3agScziY2tSIsu1Y2gAMyQzwqIr3rbN1cIYIURktDO8/E6nVBNAT2psUm1PFaMD14dk3geQZqy0/8YRH0iuoLPsEGmjF4nvJ9DepQY8GmM1OybVxINW4Txgu31OLOrZgJEwtKv8mMiX3B2VGbuSxoUxwAxvdJa9gpjTwLWNgr11BNgVDaYYpHLZLEnZCj28qedHDIGbEzKR1ZhB4c2MjY/z/QZwYKmUBz9rY1CsCc1yG/snyHIgt/ZDMEIDkqrwkc4qq9vrXVFKtOXYRXhDBzNHTMxs5qj6EYUmCVJpPp1wF8";
        String str = rsaDecryptByPrivateKey(deStr,
                "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMzFDf35/P3HwdpTWVLLADA2ObZLkXCjaJo7g/wxTxWbHSO5zwLBBpFezsuN+8nNN3n4z83D6r64MW717PLYFvXXh0gA2MSAJ6c86aLya9Mvm50Ydrnp5DG1MaksmMxd2RAoEPwkn6LX6L/9v8Ai/cjxxndV31XSVEqWLatDC3FpAgMBAAECgYAeGFgWG8ezBMjY7acTJALDEzUoNbMKevnEsTqUtiqJYmLF/WkPXo84jlaWVCfNYPvmUUs5UrINlcSuKjbI+jllZNxA/P0VdAaM8al4SBQZXtOXe6Zl7nex7xMONUULrdhmJTrUg6dqZzdmEGRzk6WxBPMS3h2ClOjyQzONcajCAQJBANI/bM3S+WM3M0emY3I/Kb3ZwdKoeEotm67owEAbZHcKjct51CSZEPNQ2ixZFWuCfvR/EnpaYbLS2LCVQmhIP8UCQQD5VHVRsRqa61L+33m3+nGosfRh7a7ImaiOO1/FtKm9Bhxdaj8A+/2EcGv9WPQR0Db+3wAKtkOUkWnrZO/x/IFVAkAmtWqw0mD79VPJo0a36A0lWQwoXsAW2CLLpyqOIvzyNc05Yyq9uji2s3IXrCcXbiU1mpeqUy2doNgoWrKpRHQJAkBQDHayKKtDC8SUwckBcW3mXGP6wOW5z+OW7qccAZnxbzHSKBdcSIsnZzS0nUeXJINTpuMLjWL7Iwifo8OJrKkVAkAIA5D4oXX8bzCGxoCRD/fyLjmx4hVzPE4c2dnPos672ljQrMfvO5i7o6j0b14zzEVy4Di+XC06i+9zBTN3tuZ+");
        System.out.println(str);
    }
}
