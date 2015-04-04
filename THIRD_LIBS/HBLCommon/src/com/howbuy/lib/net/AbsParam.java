package com.howbuy.lib.net;

import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.interfaces.IStreamParse;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午1:36:25
 */
public abstract class AbsParam implements IStreamParse {
	protected int mVersion = 1;
	private byte[] mArgByte = null;
	private final HashMap<String, String> mArgMap = new HashMap<String, String>();
	private IReqNetFinished mCalbk;
	private boolean mEnableCache = false;
	private boolean mEnablePost = false;
	private boolean mEnablePublicParam = false;
	private String mKey = null;
	private int mHandType = 0;
	private Handler mUiHandler;
	private CacheOpt mCacheOpt = null;
	private ReqNetOpt mReqOpt = null;
	protected long mCacheTime = 0l;
	protected int mArgInt = 0;
	protected Object mArgObj = null;
	protected int mFlagAdd = 0;
	protected int mFlagSub = 0;

	public void setCacheTime(long time){
		mCacheTime=time;
	}
	public void setArgInt(int argInt) {
		mArgInt = argInt;
	}

	public void setArgObj(Object obj) {
		mArgObj = obj;
	}

	public void addFlag(int flag) {
		mFlagAdd |= flag;
	}

	public void subFlag(int flag) {
		mFlagSub |= flag;
	}

	/**
	 * this is not a asynchronous params obj.
	 * 
	 * @param cacheTime
	 */
	public AbsParam(long cacheTime) {
		this(null, 0, null, null, cacheTime);
	}

	public AbsParam(int handType, Handler uiHand, long cacheTime) {
		this(null, handType, null, uiHand, cacheTime);
	}

	public AbsParam(String key, IReqNetFinished calbk, long cacheTime) {
		this(key, 0, calbk, null, cacheTime);
	}

	public AbsParam(int handType, IReqNetFinished calbk, long cacheTime) {
		this(null, handType, calbk, null, cacheTime);
	}

	public AbsParam(String key, int handType, IReqNetFinished calbk, Handler uiHand, long cacheTime) {
		this.mKey = key;
		this.mHandType = handType;
		this.mCalbk = calbk;
		this.mUiHandler = uiHand;
		this.mCacheTime = cacheTime;
		if (StrUtils.isEmpty(mKey)) {
			mKey = getClass().getSimpleName();
		}
	}

	/**
	 * return a baseurl+suburl.
	 * 
	 * @param subUrl
	 *            must be a subUrl.
	 */
	protected String buildUrl(String subUrl) {
		return UrlUtils.buildUrl(subUrl);
	}

	private CacheOpt buildCache(long cacheTime) {
		if (cacheTime > 0) {
			if (mCacheOpt == null) {
				return new CacheOpt(cacheTime, true);
			} else {
				if (mCacheOpt.getCacheTime() == CacheOpt.TIME_NONE) {
					mCacheOpt.setCacheTime(cacheTime);
				}
			}
		}
		return mCacheOpt;
	}

	public AbsParam setKey(String key, int handType) {
		this.mKey = key;
		this.mHandType = handType;
		return this;
	}

	public AbsParam setCallback(int handType, IReqNetFinished calback) {
		this.mHandType = handType;
		this.mCalbk = calback;
		return this;
	}

	public AbsParam setCallback(IReqNetFinished calback, Handler uiHandler) {
		this.mCalbk = calback;
		this.mUiHandler = uiHandler;
		return this;
	}

	public AbsParam setParamType(boolean isPost, boolean hasPublicParam, boolean enableCache) {
		this.mEnableCache = enableCache;
		this.mEnablePost = isPost;
		this.mEnablePublicParam = hasPublicParam;
		return this;
	}

	protected byte[] getArgByte() {
		return mArgByte;
	}

	public AbsParam setArgByte(byte[] argByte) {
		this.mArgByte = argByte;
		return this;
	}

	protected HashMap<String, String> getArgMap() {
		return mArgMap;
	}

	/**
	 * this method will not override the old value if the key object exist.
	 * 
	 * @throws
	 */
	public AbsParam addArg(String key, Object obj) {
		if (!StrUtils.isEmpty(key)) {
			if (!mArgMap.containsKey(key)) {
				String value = obj == null ? null : obj.toString();
				if (!StrUtils.isEmpty(value)) {
					mArgMap.put(key, value);
				}
			}
		}
		return this;
	}

	/**
	 * @param override
	 *            true to override the old value.
	 */
	public AbsParam addArg(String key, Object obj, boolean override) {
		if (override) {
			String value = obj == null ? null : obj.toString();
			if (!StrUtils.isEmpty(key) && !StrUtils.isEmpty(value)) {
				mArgMap.put(key, value);
			}
			return this;
		} else {
			return addArg(key, obj);
		}
	}

	public AbsParam addArg(String ...args) {
		int n=args==null?0:args.length;
		for (int i = 0; i < n; i += 2) {
			mArgMap.put(args[i], args[i + 1]);
		}	
		return this;
	}
	
	protected abstract String getUrl();

	/**
	 * put each filed to the args map. use addArg(key,obj) to check empty.
	 * 
	 * @return void
	 * @throws
	 */
	protected abstract void buildArgs(final HashMap<String, String> args);

	/**
	 * here to custom-made you own CacheOpt.
	 * 
	 * @param opt
	 *            maybe null.
	 * @return CacheOpt
	 * @throws
	 */
	protected abstract CacheOpt initOptions(CacheOpt opt);

	/**
	 * if you want to check before execute request,any not allowed err to throw
	 * .
	 * 
	 * @param WrapException
	 * @return void
	 * @throws
	 */
	protected void checkArgs() throws WrapException {
	}

	/**
	 * set a CacheOpt from custom-made.
	 * 
	 * @param opt
	 * @return AbsParam
	 * @throws
	 */
	public AbsParam setCacheOpt(CacheOpt opt) {
		mCacheOpt = opt;
		return this;
	}

	public final ReqResult<ReqNetOpt> execute(boolean refushArg) {
		if (refushArg && mReqOpt != null) {
			mArgMap.clear();
			getReqOpt(true);
		}
		return execute();
	}

	/**
	 * if both
	 * 
	 * @return ReqResult<ReqNetOpt>
	 */
	public final ReqResult<ReqNetOpt> execute() {
		ReqNetOpt opt = getReqOpt(false);
		ReqResult<ReqNetOpt> r = new ReqResult<ReqNetOpt>(opt);
		boolean noCalback = mCalbk == null && mUiHandler == null;
		try {
			if (noCalback) {
				r.mReqOpt.setTimeStartExecute(System.currentTimeMillis());
				r.mData = AsyReqHelper.request(opt);
				r.mReqOpt = opt;
				r.mSuccess = true;
				r.mReqOpt.setTimeComplete(System.currentTimeMillis());
			} else {
				r.mReqOpt = opt;
				r.mSuccess = AsyReqHelper.asyReq(opt, mCalbk, mUiHandler);
			}
		} catch (Exception e) {
			r.mSuccess = false;
			r.mErr = WrapException.wrap(e, getClass().getSimpleName() + " execute method err ");
		}
		if (noCalback) {
			if (LogUtils.mDebugLog) {
				LogUtils.d("AsyReq", "mResult<no callback>=" + r.toShortString(false, true)
						+ "\n\t");
			}
		}
		return r;
	}

	public final ReqNetOpt getReqOpt(boolean forceRefush) {
		if (mReqOpt == null || forceRefush) {
			//mArgMap.put("", mVersion + "");
			setCacheOpt(initOptions(buildCache(mCacheTime))).buildArgs(mArgMap);
			mReqOpt = new ReqNetOpt(0,mKey, mHandType, getUrl());
			mReqOpt.setArgs(getArgMap());
			mReqOpt.setPareser(this);
			int flag = mEnablePublicParam ? ReqNetOpt.FLAG_PUBLIC_PARAMS : 0;
			flag |= (mEnableCache || mCacheTime > 0 ? ReqNetOpt.FLAG_ENABLE_CACHE : 0);
			byte[] arg = getArgByte();
			if (mEnablePost || arg != null) {
				flag |= ReqNetOpt.FLAG_REQ_POST;
			}
			mReqOpt.addFlag(flag);
			mReqOpt.setArgByte(arg);
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

	public final boolean cancle() {
		return AsyReqHelper.getInstance().cancleRequest(getReqOpt(false));
	}
}
