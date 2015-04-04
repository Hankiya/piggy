package com.howbuy.lib.net;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.howbuy.lib.interfaces.ISourceLoader;
import com.howbuy.lib.utils.CoderUtils;
import com.howbuy.lib.utils.FileUtils;
import com.howbuy.lib.utils.PathUtils;
import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午1:35:53
 */
public class CacheOpt {
	public static final int CACHE_SD_THIS = 0;
	public static final int CACHE_ANDR_FILE = 1;
	public static final int CACHE_ANDR_CACHE = 2;
	public static final int CACHE_PK_FILE = 3;
	public static final int CACHE_PK_CACHE = 4;

	public static final long TIME_NONE = 0l;
	public static final long TIME_MIN = 60000l;
	public static final long TIME_HOUR = 3600000l;
	public static final long TIME_DAY = 86400000l;
	public static final long TIME_WEEK = 604800000l;
	
	/**
	 * the file separator for this device.
	 */
	public static final String FSP = File.separator;
	public static final String DIR_COMMON = "Commons" + FSP;
	public static final String DIR_SCREENHOT = DIR_COMMON + "Screenhots" + FSP;
	public static final String DIR_FILE = "Files" + FSP;
	public static final String DIR_DATA = "Datas" + FSP;
	public static final String DIR_IMAGE = "Images" + FSP;
	public static final String DIR_LOG = "Logs" + FSP;
	public static final String DIR_EXTRAS = "Extras" + FSP;

	private int mCacheType = CACHE_ANDR_CACHE;
	private String mSubDir = DIR_DATA;
	private long mCacheTime = TIME_DAY;
	private boolean mMD5ed = true;
	private boolean mAppend = false;
	private ISourceLoader mLoader = null;

	public boolean hasResourceLoader() {
		return mLoader != null;
	}

	public InputStream getCacheStream(InputStream is, String fileName) throws Exception {
		if (mCacheTime > TIME_NONE) {
			if (mLoader != null) {
				if (is == null) {
					return mLoader.loadResource(this, getFileName(fileName));
				} else {
					return mLoader.saveResource(this, is, getFileName(fileName));
				}
			}
		}
		return null;
	}

	public CacheOpt(int cacheType, String subDir, long cacheTime, boolean md5ed, boolean append) {
		this.mCacheTime = cacheTime;
		this.mMD5ed = md5ed;
		this.mCacheType = cacheType;
		this.mSubDir = subDir;
		this.mAppend = append;
		if (mCacheType < 0 || mCacheType > 4) {
			mCacheType = CACHE_ANDR_CACHE;
		}
	}

	/**
	 * append=false;
	 */
	public CacheOpt(int cacheType, String subDir, long cacheTime, boolean md5ed) {
		this(cacheType, subDir, cacheTime, md5ed, false);
	}

	/**
	 * append=false;mMD5e=false;
	 */
	public CacheOpt(int cacheType, String subDir, long cacheTime) {
		this(cacheType, subDir, cacheTime, false, false);
	}

	/**
	 * append=false;mMD5e=false; mCacheTime=TIME_DAY;
	 */
	public CacheOpt(int cacheType, String subDir) {
		this(cacheType, subDir, TIME_DAY, false, false);
	}

	/**
	 * append=false;mSubDir=DIR_DATA;mCacheType=CACHE_ANDR_CACHE;
	 */
	public CacheOpt(long cacheTime, boolean md5ed) {
		this(-1, DIR_DATA, cacheTime, md5ed, false);
	}

	public CacheOpt setSourceLoader(ISourceLoader l) {
		mLoader = l;
		return this;
	}

	public void setCacheTime(long time) {
		mCacheTime = time;
	}

	public void setSubDir(String subDir) {
		if (!StrUtils.isEmpty(subDir)) {
			mSubDir = subDir;
		}
	}

	public void setAppended(boolean append) {
		mAppend = append;
	}

	public void setMD5ed(boolean md5ed) {
		mMD5ed = md5ed;
	}

	public String getRootPath() {
		return PathUtils.buildPath(mCacheType, null, true);
	}

	public String getSubPath(String fileName) {
		if (!StrUtils.isEmpty(fileName)) {
			return mSubDir + (mMD5ed ? CoderUtils.toMD5(fileName) : fileName);
		}
		return null;
	}

	public String getFileName(String fileName) {
		return (mMD5ed ? CoderUtils.toMD5(fileName) : fileName);
	}

	public boolean isAppendAble() {
		return mAppend;
	}

	public long getCacheTime() {
		return mCacheTime;
	}

	protected String getFilePath(String fileName) {
		String subPath = getSubPath(fileName);
		subPath = PathUtils.buildPath(mCacheType, subPath, true);
		return subPath;
	}

	protected File getFile(String fileName) {
		String filePath = getFilePath(fileName);
		if (filePath != null) {
			return new File(filePath);
		}
		return null;
	}

	protected InputStream getInStream(String fileName) {
		return FileUtils.getInStream(getFile(fileName));
	}

	protected OutputStream getOutStream(String fileName) {
		return FileUtils.getOutStream(getFile(fileName), mAppend);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		return dumpState(sb, true).toString();
	}

	public String toShortString() {
		StringBuffer sb = new StringBuffer();
		return dumpState(sb, false).toString();
	}

	public StringBuffer dumpState(StringBuffer sb, boolean allInf) {
		sb.append("CacheOpt[");
		if (mCacheType >= 0 && mCacheType <= 4) {
			sb.append("mCacheType=");
			parseCacheType(sb);
		}
		sb.append(",mSubDir=").append(mSubDir);
		sb.append(",mCacheTime=").append(mCacheTime).append("ms");
		if (allInf) {
			sb.append(",mMD5ed=").append(mMD5ed);
			sb.append(",mAppend=").append(mAppend);
		}
		sb.append("]");
		return sb;
	}

	protected StringBuffer parseCacheType(StringBuffer sb) {
		if (mCacheType == CACHE_SD_THIS) {
			sb.append("CACHE_SD_THIS");
		} else if (mCacheType == CACHE_ANDR_FILE) {
			sb.append("CACHE_ANDR_FILE");
		} else if (mCacheType == CACHE_ANDR_CACHE) {
			sb.append("CACHE_ANDR_CACHE");
		} else if (mCacheType == CACHE_PK_FILE) {
			sb.append("CACHE_PK_FILE");
		} else if (mCacheType == CACHE_PK_CACHE) {
			sb.append("CACHE_PK_CACHE");
		}
		return sb;
	}
}