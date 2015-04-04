package com.howbuy.lib.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

/**
 * CoderTool is used for encode and decode data and translate data format.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午9:03:44
 */
public class CoderUtils {
	private static final byte[] HEXBYTES = { (byte) '0', (byte) '1', (byte) '2', (byte) '3',
			(byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A',
			(byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };
	private static final String HEXINDEX = "0123456789abcdef0123456789ABCDEF";

	/**
	 * Converts a String into a byte array by using a big-endian two byte
	 * representation of each char value in the string.
	 */
	byte[] toFullBytes(String s) {
		int length = s.length();
		byte[] buffer = new byte[length * 2];
		for (int i = 0, c = 0; i < length; i++) {
			c = s.charAt(i);
			buffer[i * 2] = (byte) ((c & 0x0000ff00) >> 8);
			buffer[i * 2 + 1] = (byte) (c & 0x000000ff);
		}
		return buffer;
	}

	/**
	 * Compacts a hexadecimal string into a byte array
	 * 
	 * @param s
	 *            hexadecimal string
	 * @return byte array for the hex string
	 * @throws IOException
	 */
	public static byte[] hexToBytes(String s) throws IOException {
		if (s.length() % 2 != 0) {
			throw new IOException("hexadecimal string with odd number of characters");
		}
		int l = s.length() / 2, j = 0;
		byte[] data = new byte[l];
		for (int i = 0; i < l; i++) {
			char c = s.charAt(j++);
			int n, b;
			n = HEXINDEX.indexOf(c);
			if (n == -1) {
				throw new IOException("hexadecimal string contains non hex character");
			}
			b = (n & 0xf) << 4;
			c = s.charAt(j++);
			n = HEXINDEX.indexOf(c);
			b += (n & 0xf);
			data[i] = (byte) b;
		}

		return data;
	}

	/**
	 * Converts a byte array into a hexadecimal string
	 * 
	 * @param b
	 *            byte array
	 * @return hex string
	 */
	public static String bytesToHex(byte[] b) {
		int len = b.length;
		char[] s = new char[len * 2];
		for (int i = 0, j = 0; i < len; i++) {
			int c = ((int) b[i]) & 0xff;

			s[j++] = (char) HEXBYTES[c >> 4 & 0xf];
			s[j++] = (char) HEXBYTES[c & 0xf];
		}
		return new String(s);
	}

	/**
	 * Hsqldb specific decoding used only for log files.
	 * 
	 * This method converts the 7 bit escaped ASCII strings in a log file back
	 * into Java Unicode strings. See unicodeToAccii() above,
	 * 
	 * @param s
	 *            encoded ASCII string in byte array
	 * @param offset
	 *            position of first byte
	 * @param length
	 *            number of bytes to use
	 * 
	 * @return Java Unicode string
	 */
	public static String asciiToUnicode(byte[] s, int offset, int length) {
		if (length == 0) {
			return "";
		}
		char[] b = new char[length];
		int j = 0;
		for (int i = 0; i < length; i++) {
			byte c = s[offset + i];

			if (c == '\\' && i < length - 5) {
				byte c1 = s[offset + i + 1];

				if (c1 == 'u') {
					i++;
					// 4 characters read should always return 0-15
					int k = HEXINDEX.indexOf(s[offset + (++i)]) << 12;
					k += HEXINDEX.indexOf(s[offset + (++i)]) << 8;
					k += HEXINDEX.indexOf(s[offset + (++i)]) << 4;
					k += HEXINDEX.indexOf(s[offset + (++i)]);
					b[j++] = (char) k;
				} else {
					b[j++] = (char) c;
				}
			} else {
				b[j++] = (char) c;
			}
		}
		return new String(b, 0, j);
	}

	public static String asciiToUnicode(String s) {
		if ((s == null) || (s.indexOf("\\u") == -1)) {
			return s;
		}
		int len = s.length();
		char[] b = new char[len];
		int j = 0;
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);

			if (c == '\\' && i < len - 5) {
				char c1 = s.charAt(i + 1);

				if (c1 == 'u') {
					i++;
					// 4 characters read should always return 0-15
					int k = HEXINDEX.indexOf(s.charAt(++i)) << 12;

					k += HEXINDEX.indexOf(s.charAt(++i)) << 8;
					k += HEXINDEX.indexOf(s.charAt(++i)) << 4;
					k += HEXINDEX.indexOf(s.charAt(++i));
					b[j++] = (char) k;
				} else {
					b[j++] = c;
				}
			} else {
				b[j++] = c;
			}
		}
		return new String(b, 0, j);
	}

	public static int getUTFSize(String s) {

		int len = (s == null) ? 0 : s.length();
		int l = 0;
		for (int i = 0; i < len; i++) {
			int c = s.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				l++;
			} else if (c > 0x07FF) {
				l += 3;
			} else {
				l += 2;
			}
		}
		return l;
	}

	/**
	 * translate a string to base64encoding string.
	 * 
	 * @param s
	 * @return
	 */
	public static String toBASE64(String s) {
		byte[] b;
		if (s == null) {
			return null;
		} else {
			b = Base64.encode(s.getBytes(), Base64.NO_WRAP);
			return new String(b);
		}
	}

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
