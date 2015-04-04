package howbuy.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String toMD5(String origin) throws RuntimeException {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("md5");
		}
		byte[] results = digest.digest(origin.getBytes());
		String md5String = toHex(results);
		return md5String;
	}

	private static String toHex(byte[] results) {
		if (results == null)
			return null;
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < results.length; i++) {
			int hi = (results[i] >> 4) & 0x0f;
			int lo = results[i] & 0x0f;
			hexString.append(Character.forDigit(hi, 16)).append(Character.forDigit(lo, 16));
		}
		return hexString.toString();
	}
}
