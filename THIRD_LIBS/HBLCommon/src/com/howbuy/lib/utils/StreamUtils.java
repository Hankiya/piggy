package com.howbuy.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * class for Stream operator.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午8:56:30
 */
public class StreamUtils {
	/**
	 * translate bytes data to serializable object.
	 * 
	 * @throws
	 */
	public static Object toObject(byte[] byts) throws Exception {
		Object obj = null;
		ByteArrayInputStream bi = null;
		ObjectInputStream oos = null;
		try {
			if (byts == null) {
				throw new NullPointerException("toObject:byte is null");
			} else {
				bi = new ByteArrayInputStream(byts);
				oos = new ObjectInputStream(bi);
				obj = oos.readObject();
			}

		} finally {
			closeSilently(oos);
			closeSilently(bi);
		}
		return obj;
	}

	/**
	 * translate InputStream data to serializable object.
	 * 
	 * @throws
	 */
	public static Object toObject(InputStream is) throws Exception {
		Object obj = null;
		BufferedInputStream bi = null;
		ObjectInputStream oos = null;
		try {
			if (is == null) {
				throw new NullPointerException("toObject:InputStream is null");
			} else {
				bi = new BufferedInputStream(is);
				oos = new ObjectInputStream(bi);
				obj = oos.readObject();
			}

		} finally {
			closeSilently(oos);
			closeSilently(bi);
			closeSilently(is);
		}
		return obj;
	}

	/**
	 * translate a serializable object to bytes.
	 * 
	 * @throws
	 */
	public static byte[] toBytes(Object obj) throws Exception {
		byte[] buffer = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			if (obj == null) {
				throw new NullPointerException("toBytes:Object is null");
			} else {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				buffer = baos.toByteArray();
			}
		} finally {
			closeSilently(oos);
			closeSilently(baos);
		}
		return buffer;
	}

	/**
	 * parse InputeStream to bytes;
	 * 
	 * @param is
	 *            T extends InputeStream;
	 * @return byte[] or null;
	 * @throws IOException
	 */
	public static byte[] toBytes(InputStream is) throws IOException {
		byte[] buffer = null;
		ByteArrayOutputStream baos = null;
		try {
			if (is == null) {
				throw new NullPointerException("toBytes:InputStream is null");
			} else {
				baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] b = new byte[1024];
				while ((len = is.read(b, 0, b.length)) != -1) {
					baos.write(b, 0, len);
				}
				buffer = baos.toByteArray();
			}
		} finally {
			closeSilently(baos);
			closeSilently(is);
		}
		return buffer;
	}

	/**
	 * parse an input stream into a string
	 * 
	 * @param is
	 *            T extends InputeStream;
	 * @return String or null;
	 * @throws IOException
	 */
	static public String toString(InputStream is) throws Exception {
		String result = null;
		BufferedReader read = null;
		try {
			if (is == null) {
				throw new NullPointerException("toString:InputStream is null");
			} else {
				read = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = read.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				result = sb.toString();
			}
		} finally {
			closeSilently(read);
			closeSilently(is);
		}
		return result;
	}

	/**
	 * get InputeStream from bytes;
	 * 
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream toStream(byte[] b) {
		if (b == null) {
			throw new NullPointerException("toStream:bytes is null");
		}
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

	
	public static void copyStream(InputStream is, OutputStream os)
			throws Exception {
		try {
			if (is == null || os == null) {
				throw new NullPointerException(
						"copyStream:InputStream or OutputStream is null");
			}
			byte[] bytes = new byte[8192];
			while (true) {
				int count = is.read(bytes, 0, 8192);
				if (count == -1) {
					break;
				}
				os.write(bytes, 0, count);
			}
		} finally {
			closeSilently(os);
			closeSilently(is);
		}
	}

	/**
	 * close Stream safely.
	 * 
	 * @param closeable
	 */
	public static void closeSilently(Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException ignored) {
			}
	}
}