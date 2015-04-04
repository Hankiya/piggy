package howbuy.android.util;

import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.file.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 缓存工具类
 * 
 * @author yescpu
 * 
 */
public class CacheUtil {
	/* 60秒 */
	public static final Long cacheTime60Seconds = 60 * 1000l;
	/* 12小时 */
	public static final Long cacheTime12Hours = 12 * 60 * cacheTime60Seconds;
	/* 24小时 */
	public static final Long cacheTime24Hours = 2 * cacheTime12Hours;
	/* cache缓存最大60天 */
	public static final Long cacheTime60Day = 60 * cacheTime24Hours;

	/**
	 * 保存Inputstream;
	 * 
	 * @param gmEssage
	 * @param appContext
	 * @param httpUrl
	 */
	public static File saveGbufCacheFile(InputStream fInputStream, String httpUrl) {
		String appCachePath = ApplicationParams.getInstance().getCacheDir().getPath();
		String fileCacheName = MD5Utils.toMD5(httpUrl);
		File cacheFile = new File(appCachePath, fileCacheName);
		String path = cacheFile.getAbsolutePath();
		File f = FileManager.saveFile(path, fInputStream);
		return f;
	}

	/**
	 * 获取Gbuf一定时间内的缓存
	 * 
	 * @param appContext
	 * @param httpUrl
	 * @param maxTimes
	 * @return
	 */
	public static File getGbufCacheFile(String httpUrl, Long maxTimes) {
		File cacheFile = null;
		// cacheFile = getGbufCacheFile(httpUrl);
		Long fileModifyTime;
		Long currTime = System.currentTimeMillis();
		if (cacheFile != null && cacheFile.length() > 0) {
			fileModifyTime = cacheFile.lastModified();
			Long jian = currTime - fileModifyTime;
			if (maxTimes > jian) {
				return cacheFile;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取缓存文件的实现，暂只支持Get
	 * 
	 * @param path
	 * @param cacheTime
	 * @return
	 * @throws Exception
	 */
	public static InputStream doFileCacheFileGet(String path, Long cacheTime) throws Exception {
		InputStream inputStream = null;
		File cacheFile = null;
		boolean savaFlag = false;
		cacheFile = getGbufCacheFile(path, cacheTime);
		if (cacheFile != null) {
			inputStream = new FileInputStream(cacheFile);
		} else {
			savaFlag = true;
			// inputStream = UrlConnectionUtil.getInstance().getRequest(path);
		}
		if (inputStream != null && savaFlag) {
			File f = saveGbufCacheFile(inputStream, path);
			inputStream = new FileInputStream(f);
		}
		return inputStream;
	}

	/**
	 * 获取缓存文件的实现，暂只支持Get
	 * 
	 * @param path
	 * @param cacheTime
	 * @return
	 * @throws Exception
	 */
	public static InputStream doFileCacheFileGetNoGzip(String path, Long cacheTime) throws Exception {
		InputStream inputStream = null;
		File cacheFile = null;
		boolean savaFlag = false;
		cacheFile = getGbufCacheFile(path, cacheTime);
		if (cacheFile != null) {
			inputStream = new FileInputStream(cacheFile);
		} else {
			savaFlag = true;
			// inputStream =
			// UrlConnectionUtil.getInstance().getRequestNoGzip(path);
		}
		if (inputStream != null && savaFlag) {
			File f = saveGbufCacheFile(inputStream, path);
			inputStream = new FileInputStream(f);
		}
		return inputStream;
	}

}
