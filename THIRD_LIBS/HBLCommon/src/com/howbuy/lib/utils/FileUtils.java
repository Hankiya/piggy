package com.howbuy.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * class for operator of any files such as save ,delete,remove,copy,and size for
 * files.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午8:55:33
 */
public class FileUtils {

	public static final long SIZE_BT = 1024L;

	public static final long SIZE_KB = SIZE_BT * 1024L;

	public static final long SIZE_MB = SIZE_KB * 1024L;

	public static final long SIZE_GB = SIZE_MB * 1024L;

	public static final long SIZE_TB = SIZE_GB * 1024L;

	public static final int SACLE = 2;

	/**
	 * mCbCheck whether the file or dir exist.
	 * 
	 * @param filePath
	 *            full path of the file.
	 * @return
	 */
	public static boolean checkFile(String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get the size of any files include file and dir.
	 * 
	 * @param filePath
	 *            full path of the file.
	 * @return byts
	 * @throws Exception
	 */
	public static long getFileSize(String filePath) throws Exception {
		File f = new File(filePath);
		return getFileSize(f);
	}

	/**
	 * get the size of any files include file and dir.
	 * 
	 * @param filePath
	 *            full path of the file.
	 * @param f
	 *            file
	 * @return bytes
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		if (f.isDirectory()) {
			return getDirSize(f);
		} else {
			return f.length();
		}
	}

	/**
	 * get the size of any files include file and dir.
	 */
	private static long getDirSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		if (flist != null) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size += getDirSize(flist[i]);
				} else {
					size += flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * delete any files include file and dir.
	 * 
	 * @param path
	 *            full path of file.
	 * @return
	 */
	public static boolean delFile(String path) {
		return delFile(new File(path));
	}

	/**
	 * delete any files include file and dir.
	 * 
	 * @param f
	 *            file
	 * @return
	 */
	public static boolean delFile(File f) {
		if (f == null || f.exists() == false) {
			return false;
		}
		if (f.isFile()) {
			f.delete();
		} else {
			File[] fs = f.listFiles();
			if (fs != null) {
				for (int i = 0; i < fs.length; i++) {
					delFile(fs[i]);
				}
			}
			f.delete();
		}
		return true;
	}

	/**
	 * save file from InputStream.
	 * 
	 * @param is
	 *            T extends InputStream;
	 * @param filePath
	 *            full path of file.
	 * @param append
	 *            whether to append the data if file exist.
	 * @return true if success els false.
	 * @throws IOException
	 */
	public static void saveFile(InputStream is, String filePath,
			boolean append) throws Exception {
		 saveFile(is, new File(filePath), append);
	}

	/**
	 * save file from InputStream.
	 * 
	 * @param is
	 *            T extends InputStream;
	 * @param f
	 *            file.
	 * @param append
	 *            whether to append the data if file exist.
	 * @return true if success els false.
	 * @throws IOException
	 */
	public static void saveFile(InputStream is, File f, boolean append)
			throws Exception {
		StreamUtils.copyStream(is, getOutStream(f, append));
	}

	public static void saveFile(byte[] bytes, File f, boolean append)
			throws Exception {
		OutputStream fout = null;
		try {
			fout = getOutStream(f, append);
			fout.write(bytes);
			fout.flush();
		} finally {
			StreamUtils.closeSilently(fout);
		}
	}

	public static void saveFile(String content, File f, boolean append)
			throws Exception {
		 saveFile(content.getBytes(), f, append);
	}

	/**
	 * copy a file from a given file ,all file must be pure file.
	 * 
	 * @param fromFile
	 *            original file.
	 * @param toFile
	 *            target file.
	 * @param override
	 * @return
	 * @throws IOException
	 */
	private static boolean copyFileOnly(File fromFile, File toFile,
			boolean override) throws Exception {
		if (fromFile.exists()) {
			if (toFile.exists() && !override) {
				return true;
			}
			saveFile(getInStream(fromFile), toFile, false);
			return true;
		}
		return false;
	}

	/**
	 * copy a file from a given file,both must be pure file or dir.
	 * 
	 * @param fromFile
	 *            original files.
	 * @param toFile
	 *            target file.
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(File fromFile, File toFile, boolean override)
			throws Exception {
		if ((fromFile.isFile() && toFile.isFile())) {
			return copyFileOnly(fromFile, toFile, override);
		} else {
			boolean result = true;
			if (fromFile.exists()) {
				File[] child = fromFile.listFiles();
				if (child != null) {
					for (int i = 0; i < child.length; i++) {
						result = result
								&& copyFile(child[i],
										new File(toFile, child[i].getName()),
										override);
					}
				}
			}
			return result;
		}
	}

	/**
	 * copy a file from a given file,both must be pure file or dir.
	 * 
	 * @param fromFilePath
	 *            original path of file.
	 * @param toFile
	 *            target file.
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String fromFilePath, File toFile,
			boolean override) throws Exception {
		return copyFile(new File(fromFilePath), toFile, override);
	}

	/**
	 * copy a file from a given file,both must be pure file or dir.
	 * 
	 * @param fromFile
	 *            original file.
	 * @param toFilePath
	 *            target file path.
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(File fromFile, String toFilePath,
			boolean override) throws Exception {
		return copyFile(fromFile, new File(toFilePath), override);
	}

	/**
	 * copy a file from a given file,both must be pure file or dir.
	 * 
	 * @param fromFilePath
	 *            original path of file.
	 * @param toFilePath
	 *            target file path.
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String fromFilePath, String toFilePath,
			boolean override) throws Exception {
		return copyFile(new File(fromFilePath), new File(toFilePath), override);
	}

	/**
	 * get InputStream from a given file .
	 * 
	 * @param @param f
	 * @return InputStream or null;
	 */
	public static InputStream getInStream(File f) {
		try {
			if (f != null && f.exists()) {
				return new BufferedInputStream(new FileInputStream(f));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get InputStream from a given file path .
	 */
	public static InputStream getInStream(String filePath) {
		return getInStream(new File(filePath));
	}

	/**
	 * get OutputStream from a given file .
	 */
	public static OutputStream getOutStream(File f, boolean append) {
		try {
			if (f != null) {
				f.getParentFile().mkdirs();
				return new BufferedOutputStream(new FileOutputStream(f, append));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get OutputStream from a given file path .
	 */
	public static OutputStream getOutStream(String filePath, boolean append) {
		return getOutStream(new File(filePath), append);
	}

	/**
	 * get a file if it exist and its last modify time interval must less
	 * cacheTime. 0 for a max cacheTime.
	 * 
	 * @param filePath
	 *            full path of the file .
	 * @param cacheTime
	 * @return File failed for null.
	 * @throws
	 */
	public static File getFile(String filePath, long cacheTime) {
		File f = new File(filePath);
		if (f != null) {
			if (cacheTime <= 0) {
				cacheTime = 31536000000000l;
			}
			if (cacheTime < System.currentTimeMillis() - f.lastModified()
					|| !f.exists()) {
				f = null;
			}
		}
		return f;
	}

	/**
	 * get text from a given file .
	 * 
	 * @return string or null;
	 * @throws Exception
	 */
	public static String getFileString(File f) throws Exception {
		return StreamUtils.toString(getInStream(f));
	}

	public static String formatDiskSize(long fileSize) {
		if (fileSize >= 0 && fileSize < SIZE_BT) {
			return fileSize + "B";
		} else if (fileSize >= SIZE_BT && fileSize < SIZE_KB) {
			return fileSize / SIZE_BT + "KB";
		} else if (fileSize >= SIZE_KB && fileSize < SIZE_MB) {
			return fileSize / SIZE_KB + "MB";
		} else if (fileSize >= SIZE_MB && fileSize < SIZE_GB) {
			BigDecimal longs = new BigDecimal(Double.valueOf(fileSize + "")
					.toString());
			BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "")
					.toString());
			String result = longs.divide(sizeMB, SACLE,
					BigDecimal.ROUND_HALF_UP).toString();
			// double result=this.longSize/(double)SIZE_MB;
			return result + "GB";
		} else {
			BigDecimal longs = new BigDecimal(Double.valueOf(fileSize + "")
					.toString());
			BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "")
					.toString());
			String result = longs.divide(sizeMB, SACLE,
					BigDecimal.ROUND_HALF_UP).toString();
			return result + "TB";
		}
	}
}
