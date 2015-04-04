package com.howbuy.lib.net;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

import com.howbuy.lib.interfaces.IStreamParse;
import com.howbuy.lib.utils.FileUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.StreamUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-15 下午3:03:01
 */
public class ReqNetOpt extends ReqOpt {
	public static final int FLAG_REQ_GET = 16;
	public static final int FLAG_REQ_POST = 32;
	public static final int FLAG_REQ_lOCAL = 64;
	public static final int FLAG_OUT_STRING = 128;
	public static final int FLAG_OUT_BYTES = 256;
	public static final int FLAG_OUT_STREAM = 512;
	public static final int FLAG_OUT_OBJECT = 1024;
	public static final int FLAG_PUBLIC_PARAMS = 2048;

	protected long mTimeStartedParse;
	/**
	 * url for net request without params.
	 */
	protected String mUrl = null;
	/**
	 * request params .
	 */
	protected HashMap<String, String> mArgMap = null;
	protected byte[] mArgByte = null;

	/**
	 * parser to parse the InputStream when it successful get a InputStream from
	 * url.
	 */
	protected IStreamParse mParser = null;
	private static CacheOpt DEFCACHEOPT = new CacheOpt(CacheOpt.TIME_DAY, true);
	protected CacheOpt mCacheOpt = null;

	public ReqNetOpt(ReqNetOpt opt) {
		super(opt);
		mUrl = opt.mUrl;
		mArgMap = opt.mArgMap;
		mArgByte = opt.mArgByte;
		mParser = opt.mParser;
		mCacheOpt = opt.mCacheOpt;
		mOFlg = opt.mOFlg;
	}

	public ReqNetOpt(int reqId,String key, int handType, String url) {
		super(reqId,key, handType);
		this.mUrl = url;
		this.mOFlg = FLAG_REQ_GET;
	}

	public void setCacheOpt(CacheOpt opt) {
		mCacheOpt = opt;
		if (mCacheOpt != null) {
			mOFlg |= FLAG_ENABLE_CACHE;
		}
	}

	public InputStream getCacheStream(String fileName, InputStream is) throws Exception {
		if (hasFlag(FLAG_FORCE_UNCACHE) && is == null)
			return null;
		CacheOpt opt = mCacheOpt == null ? DEFCACHEOPT : mCacheOpt;
		InputStream s = opt.getCacheStream(is, fileName);
		if (s == null) {
			if (opt.getCacheTime() > CacheOpt.TIME_NONE) {
				if (is == null) {
					return readStream(opt, fileName);
				} else {
					try {
						FileUtils.saveFile(is, opt.getFile(fileName), opt.isAppendAble());
						return readStream(opt, fileName);
					} catch (Exception e) {
					}
					return is;
				}
			} else {
				return is;
			}
		}
		return s;
	}

	private InputStream readStream(CacheOpt opt, String fileName) {
		File f = FileUtils.getFile(opt.getFilePath(fileName),
				hasFlag(FLAG_CACHE_THEN_REQ) ? System.currentTimeMillis() : opt.getCacheTime());
		return FileUtils.getInStream(f);
	}

	public String getUrlPath() throws Exception {
		String urlPath = null;
		if (hasFlag(ReqNetOpt.FLAG_PUBLIC_PARAMS)) {
			urlPath = UrlUtils.buildUrlRawPublic(mUrl, mArgMap);
		} else {
			urlPath = UrlUtils.buildUrlRaw(mUrl, mArgMap);
		}
		return urlPath;
	}

	protected int getReqFlag() {
		return mOFlg & (FLAG_REQ_GET | FLAG_REQ_POST | FLAG_REQ_lOCAL);
	}

	protected int getOutFlag() {
		return mOFlg & (FLAG_OUT_STRING | FLAG_OUT_BYTES | FLAG_OUT_STREAM | FLAG_OUT_OBJECT);
	}

	protected boolean hasReqFlag(int val) {
		return 0 != (val & (FLAG_REQ_GET | FLAG_REQ_POST | FLAG_REQ_lOCAL));
	}

	protected boolean hasOutFlag(int val) {
		return 0 != (val & (FLAG_OUT_STRING | FLAG_OUT_BYTES | FLAG_OUT_STREAM | FLAG_OUT_OBJECT));
	}

	public void addFlag(int flag) {
		if (flag != 0) {
			if (hasReqFlag(flag)) {
				subFlag(getReqFlag());
			}
			if (hasOutFlag(flag)) {
				subFlag(getOutFlag());
			}
			mOFlg |= flag;
		}
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public HashMap<String, String> getArgs() {
		return mArgMap;
	}

	protected HashMap<String, String> getPostArg() {
		if (hasFlag(FLAG_PUBLIC_PARAMS)) {
			if (mArgMap != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.putAll(mArgMap);
				map.putAll(UrlUtils.getPublicParams());
				return map;
			}
			return UrlUtils.getPublicParams();
		}
		return mArgMap;

	}

	public void setArgs(HashMap<String, String> arg) {
		this.mArgMap = arg;
	}

	public byte[] getArgByte() {
		return mArgByte;
	}

	public void setArgByte(byte[] arg) {
		this.mArgByte = arg;
		if (mArgByte != null) {
			addFlag(FLAG_REQ_POST);
		}
	}

	public IStreamParse getParser() {
		return mParser;
	}

	public void setPareser(IStreamParse parser) {
		this.mParser = parser;
	}

	protected Object parse(InputStream is) throws Exception {
		mTimeStartedParse = System.currentTimeMillis();
		Object obj = null;
		if (mParser != null) {
			if (is == null) {
				throw new NullPointerException("parse:InputStream is null");
			}
			obj = mParser.parseToObj(is, this);
			if (!(obj instanceof InputStream)) {
				StreamUtils.closeSilently(is);
			}
		}else{
			if (hasFlag(FLAG_OUT_STRING)) {
				obj = StreamUtils.toString(is);
			}
			if (hasFlag(FLAG_OUT_BYTES)) {
				obj = StreamUtils.toBytes(is);
			}
			if (hasFlag(FLAG_OUT_STREAM)) {
				obj = is;
			}
			if (hasFlag(FLAG_OUT_OBJECT)) {
				obj = StreamUtils.toObject(is);
			}
		}
		return obj == null ? is : obj;
	}

	public long getTimeStartParse() {
		return mTimeStartedParse;
	}

	public long getTimeRequest() {
		return mTimeStartedParse - mTimeStartedExecute;
	}

	public long getTimeParse() {
		return mTimeCompleted - mTimeStartedParse;
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

	protected StringBuffer dumpState(StringBuffer sb, boolean allInf) {
		sb.append("ReqNetOpt[");
		sb.append("mKey=").append(mKey);
		sb.append(",mHandleType=").append(mHandleType);
		sb.append(",mOFlg={");
		if (mOFlg != 0) {
			dumpFlag(sb);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		sb.append(",mUrl=").append(mUrl);
		sb.append(",mArgi=").append(mArgi);
		if (allInf) {
			sb.append(",mArgo=").append(mArgo);
			sb.append(",mArgs=").append(mArgMap);
			sb.append(",mArgBytes=").append(Arrays.toString(mArgByte));
			sb.append(",mCacheOpt=").append(mCacheOpt);
			if (mParser != null) {
				sb.append(",mParse=").append(mParser);
			} else if (hasFlag(FLAG_ENABLE_CACHE)) {
				sb.append(",def cache=").append(DEFCACHEOPT);
			}

		} else {
			if (mArgo != null) {
				sb.append(",mArgo=").append(mArgo);
			}
			if (mArgMap != null && mArgMap.size() > 0) {
				sb.append(",mArgs=").append(mArgMap);
			}
			if (mArgByte != null) {
				sb.append(",mArgBytes=").append(Arrays.toString(mArgByte));
			}
			if (mCacheOpt != null) {
				sb.append(",mCacheOpt=").append(mCacheOpt.toShortString());
			} else if (hasFlag(FLAG_ENABLE_CACHE)) {
				sb.append(",def cache=").append(DEFCACHEOPT.toShortString());
			}
			if (mParser != null) {
				sb.append(",mParse=").append(mParser.getClass().getSimpleName());
			}
		}
		dumpTime(sb, allInf);
		if (hasFlag(FLAG_PUBLIC_PARAMS)) {
			sb.append(",public arg=").append(UrlUtils.getPublicParams());
		}
		return sb.append("]");
	}

	private StringBuffer dumpTime(StringBuffer sb, boolean allInf) {
		if (allInf) {
			if (mTimeStartedExecute != 0) {
				sb.append(",mTimeStartedExecute=").append(
						StrUtils.timeFormat(mTimeStartedExecute, "mm:ss.SSS"));
			} else {
				sb.append(",mTimeStartedExecute=").append(mTimeStartedExecute);
			}

			if (mTimeStartedParse != 0) {
				sb.append(",mTimeStartedParse=").append(
						StrUtils.timeFormat(mTimeStartedParse, "mm:ss.SSS"));
			} else {
				sb.append(",mTimeStartedExecute=").append(mTimeStartedExecute);
			}

			if (mTimeCompleted != 0) {
				sb.append(",mTimeCompleted=").append(
						StrUtils.timeFormat(mTimeCompleted, "mm:ss.SSS"));
			} else {
				sb.append(",mTimeCompleted=").append(mTimeCompleted);
			}
			if (mTimeCompleted != 0) {
				if (mTimeStartedParse != 0) {
					sb.append(",parse cost:").append(mTimeCompleted - mTimeStartedParse)
							.append("ms");
				}
				if (mTimeStartedExecute != 0) {
					sb.append(",execute cost:").append(mTimeCompleted - mTimeStartedExecute)
							.append("ms");
				}
			}
		} else {
			if (mTimeStartedExecute != 0) {
				sb.append(",mTimeStartedExecute=").append(
						StrUtils.timeFormat(mTimeStartedExecute, "mm:ss.SSS"));
				if (mTimeCompleted != 0) {
					if (mTimeStartedParse != 0) {
						sb.append(",parse cost:").append(mTimeCompleted - mTimeStartedParse)
								.append("ms");
					}
					sb.append(",execute cost:").append(mTimeCompleted - mTimeStartedExecute)
							.append("ms");
				}
			}
		}
		return sb;
	}

	protected StringBuffer dumpFlag(StringBuffer sb) {
		super.dumpFlag(sb);
		if (hasFlag(FLAG_REQ_GET)) {
			sb.append("FLAG_REQ_GET,");
		}
		if (hasFlag(FLAG_REQ_POST)) {
			sb.append("FLAG_REQ_POST,");
		}
		if (hasFlag(FLAG_REQ_lOCAL)) {
			sb.append("FLAG_REQ_lOCAL,");
		}
		if (hasFlag(FLAG_OUT_STRING)) {
			sb.append("FLAG_OUT_STRING,");
		}
		if (hasFlag(FLAG_OUT_BYTES)) {
			sb.append("FLAG_OUT_BYTES,");
		}
		if (hasFlag(FLAG_OUT_STREAM)) {
			sb.append("FLAG_OUT_STREAM,");
		}
		if (hasFlag(FLAG_OUT_OBJECT)) {
			sb.append("FLAG_OUT_OBJECT,");
		}
		if (hasFlag(FLAG_PUBLIC_PARAMS)) {
			sb.append("FLAG_PUBLIC_PARAMS,");
		}
		return sb;
	}
}
