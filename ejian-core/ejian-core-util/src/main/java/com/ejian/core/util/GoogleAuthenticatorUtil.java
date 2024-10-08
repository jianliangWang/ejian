package com.ejian.core.util;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 
 * 
 * google身份验证器，java服务端实现
 * 
 * @author yangbo
 * 
 * @version 创建时间：2023-03-11
 *
 * 
 */
public class GoogleAuthenticatorUtil {

	// 生成的key长度( Generate secret key length)
	private static final int SECRET_SIZE = 10;

	private static final String SEED = "g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
	// Java实现随机数算法
	private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
	// 最多可偏移的时间
	// int window_size = 0; // default 3 - max 17

	/**
	 * Generate a random secret key. This must be saved by the server and associated
	 * with the users account to verify the code displayed by Google Authenticator.
	 * The user must register this secret on their device. 生成一个随机秘钥
	 * 
	 * @return secret key
	 */
	public static String generateSecretKey() {
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
			sr.setSeed(Base64.getDecoder().decode(SEED));
			byte[] buffer = sr.generateSeed(SECRET_SIZE);
			Base32 codec = new Base32();
			byte[] bEncodedKey = codec.encode(buffer);
			String encodedKey = new String(bEncodedKey);
			return encodedKey;
		} catch (NoSuchAlgorithmException e) {
			// should never occur... configuration error
		}
		return null;
	}

	/**
	 * Return a URL that generates and displays a QR barcode. The user scans this
	 * bar code with the Google Authenticator application on their smartphone to
	 * register the auth code. They can also manually enter the secret if desired
	 * 
	 * @param user
	 *            user id (e.g. fflinstone)
	 * @param host
	 *            host or system that the code is for (e.g. myapp.com)
	 * @param secret
	 *            the secret that was previously generated for this user
	 * @return the URL for the QR code to scan
	 */
	public static String getQRBarcodeURL(String user, String host, String secret) {
		String format = "http://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s?secret=%s";
		return String.format(format, user, host, secret);
	}

	/**
	 * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
	 * 
	 * @param user
	 *            账号
	 * @param secret
	 *            密钥
	 * @return
	 */
	public static String getQRBarcode(String user, String secret) {
		String format = "otpauth://totp/%s?secret=%s";
		return String.format(format, user, secret);
	}

	/**
	 * Check the code entered by the user to see if it is valid 验证code是否合法
	 * 
	 * @param secret
	 *            The users secret.
	 * @param code
	 *            The code displayed on the users device
	 * @param timeMsec
	 *            The time in msec (System.currentTimeMillis() for example)
	 * @return
	 */
	public static boolean checkCode(String secret, long code, long timeMsec, int windowSize) {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);
		// convert unix msec time into a 30 second "window"
		// this is per the TOTP spec (see the RFC for details)
		long t = (timeMsec / 1000L) / 30L;
		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		for (int i = -windowSize; i <= windowSize; ++i) {
			long hash;
			try {
				hash = verifyCode(decodedKey, t + i);
				System.out.println(hash);
			} catch (Exception e) {
				// Yes, this is bad form - but
				// the exceptions thrown would be rare and a static
				// configuration problem
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
				// return false;
			}
			if (hash == code) {
				return true;
			}
		}
		// The validation code is invalid.
		return false;
	}

	public static boolean checkCode(String secret, long code, long timeMsec) {
		return checkCode(secret, code, timeMsec, 0);
	}

	public static boolean checkCode(String secret, long code) {
		return checkCode(secret, code, System.currentTimeMillis());
	}

	private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[20 - 1] & 0xF;
		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

	public static void main(String[] args) {
		System.out.println(checkCode("D732FJIODOIOSLLJ",65515));
	}
}
