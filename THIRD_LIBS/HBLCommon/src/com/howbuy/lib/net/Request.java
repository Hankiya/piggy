package com.howbuy.lib.net;


/**
 * @author rexy 840094530@qq.com
 * @date 2014-6-27 上午10:02:25
 */
class Request {
	/*
	// private static String VERSION_KEY = "iVer";// 接口版本键。
	public static final int FLAG_PUBLIC_ARG = 1;// 有公共参数。
	public static final int FLAG_REQUEST_POST = 2;// 有些标志为POST请求，否则为GET请求。
	public static final int FLAG_SPECIAL_PREFIX_URL = 4;// 始终用特别前缀。
	public static final int FLAG_DEBUG_PREFIX_URL = 8;// DEBUG时特别前缀。

	protected int mFlag;// 各种开关。
	 
	protected int mRequestVersion = 0;// 接口版本号。
	protected int mRequestId;// 接口ID。
	//=====================================================//
	protected String mUrlSuffix;// URL后缀。(可为格式化的后缀data/%1$s.html.)
	protected String mUrlPrefix;// URL前缀。
	protected String mUrlSuffixFormat;// 格式化后的后缀。
	 
	protected byte[] mArgByte = null; // 接口参数，仅仅在post请求时。
	protected final HashMap<String, String> mArgMap = new HashMap<String, String>();// 接口参数。
	//=====================================================//
	protected int mArgInt = 0;// 请求附加int参数。
	protected Object mArgObj = null;// 请求附加obj参数。
	//=====================================================//
	protected int mFlagAdd = 0;// 请求标志增量。
	protected int mFlagSub = 0;// 请求标志减量。
	//=====================================================//
	protected IStreamParse mParser;// 解析接口。
	protected CacheOpt mCacheOpt;// 缓存策略。
	protected ReqNetOpt mReqOpt;// 请求对象。
	//=====================================================//
	protected IReqNetFinished mCalBak = null;// 回调接口。
	protected String mKey = null;// 请求KEY。
	protected int mHandType = 0;// 请求类型。
	protected Handler mUiHandler = null;// 回调handler.

	public Request reset() {
		mFlag = 0;
		mRequestVersion = 0;
		mRequestId = 0;
		mUrlSuffix = null;
		mUrlPrefix = null;
		mUrlSuffixFormat = null;
		mArgByte = null;
		mArgMap.clear();
		mArgInt = 0;
		mArgObj = null;
		mFlagAdd = 0;
		mFlagSub = 0;
		mParser = null;
		mCacheOpt = null;
		mReqOpt = null;
		mCalBak = null;
		mKey = null;
		mHandType = 0;
		mUiHandler = null;
		return this;
	}

	public final ReqResult<ReqNetOpt> execute() {
		ReqNetOpt opt = getReqOpt(false);
		ReqResult<ReqNetOpt> r = new ReqResult<ReqNetOpt>(opt);
		boolean emptyCaller = mCalBak == null && mUiHandler == null;
		try {
			if (emptyCaller) {
				r.mReqOpt.setTimeStartExecute(System.currentTimeMillis());
				r.mData = AsyReqHelper.request(opt);
				r.mReqOpt = opt;
				r.mSuccess = true;
				r.mReqOpt.setTimeComplete(System.currentTimeMillis());
			} else {
				r.mReqOpt = opt;
				r.mSuccess = AsyReqHelper.asyReq(opt, mCalBak, mUiHandler);
			}
		} catch (Exception e) {
			r.mSuccess = false;
			r.mErr = WrapException.wrap(e, getClass().getSimpleName() + " execute method err ");
		}
		if (emptyCaller) {
			if (LogUtils.mDebugLog) {
				LogUtils.d("AsyReq", "mResult<no callback>=" + r.toShortString(false, true)
						+ "\n\t");
			}
		}
		return r;
	}

	public final ReqNetOpt getReqOpt(boolean forceRefush) {
		if (mReqOpt == null || forceRefush) {
			mReqOpt = new ReqNetOpt(mRequestId, mKey, mHandType, getUrl());
			mReqOpt.setPareser(mParser);
			int flag = hasFlag(FLAG_PUBLIC_ARG) ? ReqNetOpt.FLAG_PUBLIC_PARAMS : 0;
			if (hasFlag(FLAG_REQUEST_POST)) {
				flag |= ReqNetOpt.FLAG_REQ_POST;
			}
			mReqOpt.addFlag(flag);
			mReqOpt.setArgByte(mArgByte);
		}
		mReqOpt.setCacheOpt(mCacheOpt);
		mReqOpt.setArgInt(mArgInt);
		mReqOpt.setArgObj(mArgObj);
		int common = mFlagAdd & mFlagSub;
		if (common != 0) {
			common = ~common;
			mFlagAdd &= common;
			mFlagSub &= common;
		}
		mReqOpt.addFlag(mFlagAdd);
		mReqOpt.subFlag(mFlagSub);
		return mReqOpt;
	}

	// ///////////////////////for request flag opt////////////////////////////
	public Request flag(int add, int sub) {
		mFlagAdd |= add;
		mFlagSub |= sub;
		return this;
	}

	// ///////////////////for request arg/////////////////////

	public Request setArgInt(int argInt) {
		mArgInt = argInt;
		return this;
	}

	public Request setArgObj(Object obj) {
		mArgObj = obj;
		return this;
	}

	// ///////////////////////////for cache and
	// parser/////////////////////////////////////
	public Request setParser(IStreamParse parser) {
		mParser = parser;
		return this;
	}

	public Request setCache(CacheOpt opt) {
		mCacheOpt = opt;
		return this;
	}

	public Request setCache(long cacheTime) {
		if (cacheTime > 0) {
			if (mCacheOpt == null) {
				mCacheOpt = new CacheOpt(cacheTime, true);
			}
			mCacheOpt.setCacheTime(cacheTime);
		} else {
			mCacheOpt = null;
		}
		return this;
	}

	// ///////////////////////for request args/////////////////////////////

	public Request addArg(String key, Object obj, boolean override) {
		String value = obj == null ? null : obj.toString();
		if (!StrUtils.isEmpty(value)) {
			if (override) {
				mArgMap.put(key, value);
			} else {
				if (!mArgMap.containsKey(key)) {
					mArgMap.put(key, value);
				}
			}
		}
		return this;
	}

	public Request setArg(String... args) {
		int n = args == null ? 0 : args.length;
		if (n == 0) {
			mArgMap.clear();
		} else {
			n -= n % 2;
			for (int i = 0; i < n; i += 2) {
				mArgMap.put(args[i], args[i + 1]);
			}
		}
		return this;
	}

	public Request setArg(byte[] bs) {
		mArgByte = bs;
		return this;
	}

	// ////////////////////////for key and callback///////////////////////////

	public Request setKey(int handType, String key) {
		mHandType = handType;
		mKey = key;
		return this;
	}

	public Request setCallback(IReqNetFinished callBack, Handler uiHandler) {
		mCalBak = callBack;
		mUiHandler = uiHandler;
		return this;
	}

	// ////////////////////for request id and
	// version/////////////////////////////

	public int getRequestId() {
		return mRequestId;
	}

	public Request setRequestInf(int requestId, int requestVersion) {
		mRequestId = requestId;
		mRequestVersion = requestVersion;
		return this;
	}

	// ////////////////for request url///////////////////////////////

	protected String getUrl() {
		boolean prefix = hasFlag(FLAG_SPECIAL_PREFIX_URL)
				|| (LogUtils.mDebugUrl && hasFlag(FLAG_DEBUG_PREFIX_URL));
		String suffix = mUrlSuffixFormat == null ? mUrlSuffix : mUrlSuffixFormat;
		return prefix ? UrlUtils.buildUrl(mUrlPrefix, suffix) : UrlUtils.buildUrl(suffix);
	}

	public Request setSuffixArg(String... arg) {
		if (!StrUtils.isEmpty(mUrlSuffix)) {
			mUrlSuffixFormat = String.format(mUrlSuffix, arg);
		} else {
			mUrlSuffixFormat = null;
		}
		return this;
	}

	public Request setUrl(String urlPrefix, String urlSuffix) {
		mUrlPrefix = urlPrefix;
		mUrlSuffix = urlSuffix;
		return this;
	}

	// /////////////////////////////////////////////////////////////

	 
	   public static void setVersionKey(String key) { if
	   (!StrUtils.isEmpty(key)) { VERSION_KEY = key; } }
	  

	// ///////////////////////for flag//////////////////////////////////////

	final public int getFlag() {
		return mFlag;
	}

	final public Request setFlag(int flag) {
		mFlag = flag;
		return this;
	}

	final public static boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final public Request addFlag(int flag) {
		mFlag |= flag;
		return this;
	}

	final public Request subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
		return this;
	}
*/
}
