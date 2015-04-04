package com.howbuy.lib.net;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午1:36:08
 */
public class ParDefault extends AbsParam {
	boolean mSpecialPrefixAways = false;
	boolean mSpecialPrefixDebug = false;
	protected String mUrlSuffix;// URL后缀。(可为格式化的后缀data/%1$s.html.)
	protected String mUrlPrefix;// URL前缀。
	protected String mUrlSuffixFormat;// 格式化后的后缀。

	public ParDefault(long cacheTime) {
		super(cacheTime);
	}

	public ParDefault(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParDefault(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	public ParDefault(String key, int handType, IReqNetFinished calbk, Handler uiHand,
			long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return null;
	}

	public ParDefault setArgMap(HashMap<String, String> argMap, boolean clean) {
		if (clean) {
			getArgMap().clear();
		}
		if (argMap != null) {
			getArgMap().putAll(argMap);
		}
		return this;
	}

	protected String getUrl() {
		boolean prefix = mSpecialPrefixAways || (LogUtils.mDebugUrl && mSpecialPrefixDebug);
		String suffix = mUrlSuffixFormat == null ? mUrlSuffix : mUrlSuffixFormat;
		return prefix ? UrlUtils.buildUrl(mUrlPrefix, suffix) : UrlUtils.buildUrl(suffix);
	}

	public ParDefault setSuffixArg(String... arg) {
		if (!StrUtils.isEmpty(mUrlSuffix)) {
			mUrlSuffixFormat = String.format(mUrlSuffix, arg);
		} else {
			mUrlSuffixFormat = null;
		}
		return this;
	}

	public ParDefault setUrl(String urlPrefix, String urlSuffix) {
		mUrlPrefix = urlPrefix;
		mUrlSuffix = urlSuffix;
		if (urlPrefix == null) {
			mSpecialPrefixAways = false;
			mSpecialPrefixDebug = false;
		}
		return this;
	}

	public ParDefault setEnablePrefix(boolean prefixAways,boolean prefixDebug){
		mSpecialPrefixAways=prefixAways;
		mSpecialPrefixDebug=prefixDebug;
		return this;
	}
	
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		//addArg("", mVersion);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		return opt;
	}

}
