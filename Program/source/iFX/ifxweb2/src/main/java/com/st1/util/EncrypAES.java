package com.st1.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.util.dump.HexDump;

public class EncrypAES {
	static final Logger logger = LoggerFactory.getLogger(EncrypAES.class);

	// KeyGenerator 提供對稱密鑰生成器的功能，支持各種演算法
	private static KeyGenerator keygen;
	// SecretKey 負責保存對稱密鑰
	private static SecretKey deskey;
	// Cipher 負責完成加密或解密工作
	private static Cipher c;
	// 該字節數組負責保存加密的結果
	private static byte[] cipherByte;

	// 潘 如果使用像CBC这样的块链模式，你需要为Cipher提供一个IvParameterSpec
	// build the initialization vector. This example is all zeros, but it
	// could be any value or generated using a random number generator.
	private static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private static IvParameterSpec ivspec = new IvParameterSpec(iv);

	/**
	 * 對字符串加密
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static byte[] Encrytor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// 根據密鑰，對 Cipher 物件進行初始化，ENCRYPT_MODE 表示加密模式
		c.init(Cipher.ENCRYPT_MODE, deskey, ivspec);
		// c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();

		// 加密，結果保存進 cipherByte
		cipherByte = c.doFinal(src);

		String temp = new String(cipherByte);
		String temp2 = new String(temp.getBytes());
		// System.out.println("temp:" + temp);
		// System.out.println("temp2:" + temp2);
		return cipherByte;
	}

	/**
	 * 對字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static byte[] Decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// 根據密鑰，對 Cipher 物件進行初始化，DECRYPT_MODE 表示加密模式
		c.init(Cipher.DECRYPT_MODE, deskey, ivspec);
		String temp3 = new String(buff);
		String temp4 = new String(temp3.getBytes());
		// System.out.println("temp3:" + temp3);
		// System.out.println("temp4" + temp4);
		cipherByte = c.doFinal(buff);
		return cipherByte;
	}

	/**
	 * @param args
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws Exception {
		EncrypSet();
		String msg = "我測試一下123qwdqw2131WD";
		System.out.println("明文是:" + msg);
		System.out.println("加密後:" + userEncrytor(msg));
		System.out.println("解密後:" + userDecryptor(userEncrytor(msg)));
	}

	/**
	 * 設定解碼相關參數 (從StartupServlet)
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public static void EncrypSet() throws NoSuchAlgorithmException, NoSuchPaddingException {
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 實例化支持 AES 演算法的密鑰生成器(演算法名稱命名需按規定，否則拋出異常)
		keygen = KeyGenerator.getInstance("AES");
		keygen.init(128);// 潘 20180719
		// 生成密鑰
		deskey = keygen.generateKey();
		// 生成 Cipher 物件,指定其支持的 AES 演算法
		c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	}

	/**
	 * 對字符串加密 (經過轉換)
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String userEncrytor(String str) {
		String result = "";
		try {
			try {
				result = HexDump.toHexString(Encrytor(str));
			} catch (InvalidAlgorithmParameterException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		} catch (InvalidKeyException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IllegalBlockSizeException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (BadPaddingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return result;
	}

	/**
	 * 對字符串解密 (經過轉換)
	 * 
	 * @param demsg
	 * @return
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String userDecryptor(String demsg) {
		String result = "";
		try {
			result = new String(Decryptor(HexDump.hexStringToByteArray(demsg)));
		} catch (InvalidKeyException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IllegalBlockSizeException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (BadPaddingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (InvalidAlgorithmParameterException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return result;
	}
}