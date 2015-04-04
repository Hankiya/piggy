package com.howbuy.lib.utils;

import java.io.File;

import android.os.Environment;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.net.CacheOpt;

/**
 * only for local path config.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午10:05:04
 */
public final class PathUtils {
	public static final int PATH_SD_THIS = 0;
	public static final int PATH_ANDR_FILE = 1;
	public static final int PATH_ANDR_CACHE = 2;
	public static final int PATH_PK_FILE = 3;
	public static final int PATH_PK_CACHE = 4;

	private static String SD_ROOT = null;
	private static String SD_THIS = null;
	private static String PK_CACHE = null;
	private static String PK_FILE = null;
	private static String PKSD_CACHE = null;
	private static String PKSD_FILE = null;

	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);
		return !(!canRead || onlyRead || unMounted);
	}

	public static File getRootDir(int pathType, boolean emptyForDefault) {
		File f = null;
		switch (pathType) {
		case PATH_ANDR_CACHE: {// 获取Sdcard/android/data/包名/cache目录
			if (PKSD_CACHE != null) {
				return new File(PKSD_CACHE);
			} else {
				f = GlobalApp.getApp().getExternalCacheDir();
				if (f != null) {
					PKSD_CACHE = f.getAbsolutePath();
					return f;
				}
			}

		}
			break;

		case PATH_ANDR_FILE: {// 获取Sdcard/android/data/包名/files目录
			if (PKSD_FILE != null) {
				return new File(PKSD_FILE);
			} else {
				f = GlobalApp.getApp().getExternalFilesDir(null);
				if (f != null) {
					PKSD_FILE = f.getAbsolutePath();
					return f;
				}
			}
		}
			break;

		case PATH_PK_CACHE: {// 于获取/data/data/包名/cache目录
			if (PK_CACHE != null) {
				return new File(PK_CACHE);
			} else {
				f = GlobalApp.getApp().getCacheDir();
				if (f != null) {
					PK_CACHE = f.getAbsolutePath();
					return f;
				}
			}
		}
			break;

		case PATH_PK_FILE: {// 获取/data/data/包名/files目录
			if (PK_FILE != null) {
				return new File(PK_FILE);
			} else {
				f = GlobalApp.getApp().getFilesDir();
				if (f != null) {
					PK_FILE = f.getAbsolutePath();
					return f;
				}
			}

		}
			break;
		default: {
			if (SD_THIS != null) {
				f = new File(SD_THIS);
			}
		}
		}
		if (f == null) {
			if (emptyForDefault) {
				return getRootDir(PATH_SD_THIS, false);
			}
		}
		return f;
	}

	public static boolean initPathConfig(boolean onlyUseContextCache, boolean allowDelContextCache) {
		String[] ss = SysUtils.parsePackageName(GlobalApp.getApp());
		File appCacheDir = null;
		boolean fromCache = false;
		if (!onlyUseContextCache
				&& Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED)) {

			String url = Environment.getExternalStorageDirectory().getAbsolutePath();
			if (android.os.Build.DEVICE.contains("Samsung")
					|| android.os.Build.MANUFACTURER.contains("Samsung")) {
				url = url + "/external_sd/";
			}

			appCacheDir = new File(url, ss[0]);
		}

		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			fromCache = true;
			appCacheDir = GlobalApp.getApp().getCacheDir();
		} else {
			if (allowDelContextCache) {
				try {
					new Thread() {
						public void run() {
							FileUtils.delFile(GlobalApp.getApp().getCacheDir());
						}
					}.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (appCacheDir == null) {
			fromCache = true;
			appCacheDir = GlobalApp.getApp().getCacheDir();
		}
		if (appCacheDir != null) {
			SD_THIS = SD_ROOT = appCacheDir.getAbsolutePath();
			if (ss[1] != null && !fromCache) {
				SD_THIS = buildPath(SD_ROOT, ss[1]);
			}
			resetAllPath();
			return true;
		}
		return false;
	}

	public static String buildPath(int pathType, String subfile, boolean emptyForDefault) {
		String root = null;
		switch (pathType) {
		case PATH_ANDR_CACHE: {// 获取Sdcard/android/data/包名/cache目录
			if (PKSD_CACHE != null) {
				root = PKSD_CACHE;
			}
		}
			break;

		case PATH_ANDR_FILE: {// 获取Sdcard/android/data/包名/files目录
			if (PKSD_FILE != null) {
				root = PKSD_FILE;
			}

		}
			break;

		case PATH_PK_CACHE: {// 于获取/data/data/包名/cache目录
			if (PK_CACHE != null) {
				root = PK_CACHE;
			}
		}
			break;

		case PATH_PK_FILE: {// 获取/data/data/包名/files目录
			if (PK_FILE == null) {
				root = PK_FILE;
			}
		}
			break;
		case PATH_SD_THIS: {
		}
		default: {
			if (SD_THIS != null) {
				root = SD_THIS;
			}
		}
		}
		if (root == null) {
			File f = getRootDir(pathType, emptyForDefault);
			if (f != null) {
				root = f.getAbsolutePath();
			}
		}
		if (root != null) {
			if (subfile == null) {
				return root;
			} else {
				return buildPath(root, subfile);
			}

		}
		return null;
	}

	/**
	 * 可以指定根目录路径.
	 * 
	 * @param @param root
	 * @param @param subDir
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String buildPath(String root, String subfile) {
		if (root == null || subfile == null) {
			return null;
		}
		if (root.endsWith(CacheOpt.FSP)) {
			if (subfile.startsWith(CacheOpt.FSP)) {
				return root.substring(0, root.lastIndexOf(CacheOpt.FSP)) + subfile;
			} else {
				return root + subfile;
			}
		} else {
			if (subfile.startsWith(CacheOpt.FSP)) {
				return root + subfile;
			} else {
				return root + CacheOpt.FSP + subfile;
			}
		}
	}

	public static String PATH_SCREENHOT = null;
	public static String PATH_FILE = null;
	public static String PATH_DATA = null;
	public static String PATH_IMAGE = null;
	public static String PATH_LOG = null;
	public static String PATH_EXTRA = null;
	public static String PATH_COMMON = null;

	protected static void resetAllPath() {
		PATH_COMMON = buildPath(SD_ROOT, CacheOpt.DIR_COMMON);
		PATH_SCREENHOT = buildPath(SD_ROOT, CacheOpt.DIR_SCREENHOT);
		PATH_FILE = buildPath(SD_THIS, CacheOpt.DIR_FILE);
		PATH_DATA = buildPath(SD_THIS, CacheOpt.DIR_DATA);
		PATH_IMAGE = buildPath(SD_THIS, CacheOpt.DIR_IMAGE);
		PATH_LOG = buildPath(SD_THIS, CacheOpt.DIR_LOG);
		PATH_EXTRA = buildPath(SD_THIS, CacheOpt.DIR_EXTRAS);
	}
}
