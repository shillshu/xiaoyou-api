package cn.sibetech.core.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密类
 * SHA-1
 */
public class CryptUtils {

	/**
	 * 获取salt
	 * @return
	 */
	public static String generateSalt(){
		byte[] salt = Digests.generateSalt(8);
		return Encodes.encodeHex(salt);
	}

	/**
	 * 默认加密
	 * salt 加密
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String encode(String password, String salt){
		if(StringUtils.isEmpty(password)){
			throw new RuntimeException("password is null");
		}
		if(StringUtils.isEmpty(salt)){
			throw new RuntimeException("salt is null");
		}
		byte[] saltByte = Encodes.decodeHex(salt);
		byte[] hashPassword = Digests.sha1(password.getBytes(), saltByte, 1024);
		return Encodes.encodeHex(hashPassword);
	}

	public static String sha1(String plain) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(plain.getBytes("UTF-8"));
			byte[] array = md.digest();
			String byte2Str = byte2Str(array);
			byte[] newArray = byte2Str.getBytes();
			result = new String(Base64.encodeBase64(newArray));
			if (result != null) {
				result = result.trim();
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return result;

	}

	public static String md5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			}
			else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}
	public static String md5UpperCase(String inStr){
		return md5(inStr).toUpperCase();
	}

  	private static String byte2Str(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(
				Integer.toHexString(
					(0x000000ff & bytes[i]) | 0xffffff00).substring(
					6));
		}
		return sb.toString().trim();
	}

}
