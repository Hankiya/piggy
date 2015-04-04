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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZipTool is used for compress and uncompress file.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 下午2:14:36
 */
public class ZipUtils {
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

	/**
	 * compress file list to a zip file.
	 * 
	 * @param resFileList
	 *            file list to compress.
	 * @param zipFile
	 *            tartet zip file.
	 * @throws IOException
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile)
			throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.close();
	}

	/**
	 * compress file list to a zip file with a comment.
	 * 
	 * @param resFileList
	 *            file list to compress.
	 * @param zipFile
	 *            tartet zip file.
	 * @author zheng comment for zip file.
	 * @throws IOException
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile,
			String comment) throws IOException {
		ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(zipFile), BUFF_SIZE));
		for (File resFile : resFileList) {
			zipFile(resFile, zipout, "");
		}
		zipout.setComment(comment);
		zipout.close();
	}

	/**
	 * uncompress a zip file to a target file.
	 * 
	 * @param zipFile
	 *            zip file to uncompress.
	 * @param folderPath
	 *            tartet dir or file to hold the uncompressed zip file.
	 * @param del
	 *            true to delete the original zip file.
	 * @throws IOException
	 */
	public static void upZipFile(File zipFile, String folderPath, boolean del)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			String str = folderPath + File.separator + entry.getName();
			str = new String(str.getBytes("8859_1"), "GB2312");
			File desFile = new File(str);
			boolean notexist = !desFile.exists();
			if (entry.isDirectory()) {
				if (notexist)
					desFile.mkdirs();
				continue;
			} else {
				if (notexist) {
					File parent = desFile.getParentFile();
					if (parent != null && !parent.exists()) {
						parent.mkdirs();
					}
					desFile.createNewFile();
				}

			}
			InputStream in = zf.getInputStream(entry);
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
		if (del) {
			zipFile.delete();
		}
	}

	/**
	 * compress all the file in the zip file that contain the especial text
	 * likes nameContains.
	 * 
	 * @param zipFile
	 *            zipfile.
	 * @param folderPath
	 *            the target path to hold the compress the file.
	 * @param nameContains
	 *            the file name that should be selected to compress.
	 * @param del
	 *            true to delete the original zip file.
	 * @throws ZipException
	 *             IOException
	 */
	// str.getBytes("GB2312"),"8859_1" output
	// str.getBytes("8859_1"),"GB2312" input
	public static ArrayList<File> upZipSelectedFile(File zipFile,
			String folderPath, String nameContains) throws ZipException,
			IOException {
		ArrayList<File> fileList = new ArrayList<File>();

		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdir();
		}

		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			if (entry.getName().contains(nameContains)) {
				String path = folderPath + File.separator + entry.getName();
				path = new String(path.getBytes("8859_1"), "GB2312");
				File desFile = new File(path);
				boolean notexist = !desFile.exists();
				if (entry.isDirectory()) {
					if (notexist)
						desFile.mkdirs();
					continue;
				} else {
					if (notexist)
						desFile.createNewFile();
				}
				InputStream in = zf.getInputStream(entry);
				OutputStream out = new FileOutputStream(desFile);
				byte buffer[] = new byte[BUFF_SIZE];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}
				in.close();
				out.close();
				fileList.add(desFile);
			}
		}
		return fileList;
	}

	/**
	 * get the filename list from the zipfile.
	 * 
	 * @param zipFile
	 *            a compressed file.
	 * @return a list of file name.
	 * @throws ZipException
	 *             IOException
	 */
	public static ArrayList<String> getEntriesNames(File zipFile)
			throws ZipException, IOException {
		ArrayList<String> entryNames = new ArrayList<String>();
		Enumeration<?> entries = getEntriesEnumeration(zipFile);
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			entryNames.add(new String(getEntryName(entry).getBytes("GB2312"),
					"8859_1"));
		}
		return entryNames;
	}

	/**
	 * Returns an enumeration of the entries. The entries are listed in the
	 * order in which they appear in the ZIP archive
	 * 
	 * @param zipFile
	 *            zip file.
	 * @return enumeration collect.
	 * @throws ZipException
	 *             IOException
	 */
	public static Enumeration<?> getEntriesEnumeration(File zipFile)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(zipFile);
		return zf.entries();
	}

	/**
	 * get the comment of the zipentry.
	 * 
	 * @param entry
	 *            zip entry.
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryComment(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getComment().getBytes("GB2312"), "8859_1");
	}

	/**
	 * get the zipentry name.
	 * 
	 * @param entry
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryName(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getName().getBytes("GB2312"), "8859_1");
	}

	private static void zipFile(File resFile, ZipOutputStream zipout,
			String rootpath) throws FileNotFoundException, IOException {
		rootpath = rootpath
				+ (rootpath.trim().length() == 0 ? "" : File.separator)
				+ resFile.getName();
		rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
		if (resFile.isDirectory()) {
			File[] fileList = resFile.listFiles();
			for (File file : fileList) {
				zipFile(file, zipout, rootpath);
			}
		} else {
			byte buffer[] = new byte[BUFF_SIZE];
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(resFile), BUFF_SIZE);
			zipout.putNextEntry(new ZipEntry(rootpath));
			int realLength;
			while ((realLength = in.read(buffer)) != -1) {
				zipout.write(buffer, 0, realLength);
			}
			in.close();
			zipout.flush();
			zipout.closeEntry();
		}
	}
}
