package com.howbuy.datalib.trade;

import howbuy.android.piggy.api.dto.HeaderInfoDto;
import howbuy.android.piggy.api.dto.RespondCipher;
import howbuy.android.piggy.api.dto.RespondExpress;
import howbuy.android.piggy.api.dto.RespondResult;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.UrlUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.StreamUtils;

public class ParTrade extends AbsParam {
	boolean mSpecialPrefixAways = false;
	boolean mSpecialPrefixDebug = false;
	protected String mUrlSuffix;// URL后缀。(可为格式化的后缀data/%1$s.html.)
	protected String mUrlPrefix;// URL前缀。
	protected String mUrlSuffixFormat;// 格式化后的后缀。
	protected boolean mHasSecret = false;
	
	private static String mTradeUrl;
	private static String mTradeDebugUrl;
	
	private static final int DefaulMaxNum = 2;
	private static int selfNum = 0;

	protected static void setBaseUrl(String tradeUrl, String tradeDebugUrl) {
		mTradeUrl = tradeUrl;
		mTradeDebugUrl = tradeDebugUrl;
	}
	public static boolean checkMaxNum() {
		selfNum++;
		if (selfNum > DefaulMaxNum) {
			if (selfNum + 1 == DefaulMaxNum) {
				selfNum = 0;
			}
			return false;
		}
		return true;
	}
	
	
	public ParTrade(long cacheTime) {
		super(cacheTime);
	}

	public ParTrade(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParTrade(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	public ParTrade(String key, int handType, IReqNetFinished calbk, Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParTrade setHasSecret(boolean hasSecret) {
		mHasSecret = hasSecret;
		if (mHasSecret) {
			setParamType(false, false, false);
		} else {
			setParamType(false, true, false);
		}
		return this;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		RespondResult r = GsonUtils.getRespond(StreamUtils.toString(is));
		if (r != null) {
			RespondExpress re = null;
			if (r.isExpress()) {
				re = r.getExpress();
			} else {
				RespondCipher rc = r.getCipher();
				if (rc != null) {
					String resJson = DesUtilForNetParam.dencryptParam(rc);
					if (resJson != null) {
						re = GsonUtils.getExpressly(resJson);
					}
				}
			}
			if (re != null) {
				HeaderInfoDto rh = re.getHeader();
				if (re != null && RespondResult.RESPONSE_RES_SUCCESS.equals(rh.getResponseCode())
						&& RespondResult.RESPONSE_RES_SUCCESS.equals(rh.getContentCode())) {
					return handSuccess(re.getBody(), rh);
				} else {
					return HandFailed(rh, opt);
				}
			}
		}
		return null;
	}

	protected Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {// 返回成功。
		if (mTradeHandler != null) {
			return mTradeHandler.handSuccess(body, rh);
		}
		return body == null ? null : body.toString();
	}

	protected Object HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
		if (rh != null) {
			String respondCode = rh.getResponseCode();
			String contentCode = rh.getContentCode();

			if (RespondResult.VERIFICATION_CODE_EXPIRED.equals(contentCode)
					|| RespondResult.VERIFICATION_CODE_Error.equals(respondCode)) {
				if (checkMaxNum() && DesUtilForNetParam.doResetRsaKay()) {// 需要重新请求rsa.。
					execute();
				}
			} else {
				if (mTradeHandler != null) {
					mTradeHandler.HandFailed(rh, opt);
				}
			}
		}
		throw WrapException.wrap(new Throwable("TRADE"), rh.toString());
	}

	public ParTrade setArgMap(HashMap<String, String> argMap, boolean clean) {
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
		return prefix ? UrlUtils.buildUrl(mUrlPrefix, suffix) : UrlUtils.buildUrl(
				LogUtils.mDebugUrl ? mTradeDebugUrl : mTradeUrl, suffix);
	}

	public ParTrade setSuffixArg(String... arg) {
		if (!StrUtils.isEmpty(mUrlSuffix)) {
			mUrlSuffixFormat = String.format(mUrlSuffix, arg);
		} else {
			mUrlSuffixFormat = null;
		}
		return this;
	}

	public ParTrade setUrl(String urlPrefix, String urlSuffix) {
		mUrlPrefix = urlPrefix;
		mUrlSuffix = urlSuffix;
		if (urlPrefix == null) {
			mSpecialPrefixAways = false;
			mSpecialPrefixDebug = false;
		}
		return this;
	}

	public ParTrade setUrl(String url, boolean isPrefix) {
		if (isPrefix) {
			return setUrl(url, null);
		} else {
			return setUrl(null, url);
		}
	}

	public ParTrade setEnablePrefix(boolean prefixAways, boolean prefixDebug) {
		mSpecialPrefixAways = prefixAways;
		mSpecialPrefixDebug = prefixDebug;
		return this;
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		if (mHasSecret) {
			String token = args.get("tokenId");
			if (token != null) {
				args.remove("tokenId");
			}
			String source = GsonUtils.toJson(args);
			String encMsgBase64 = DesUtilForNetParam.encryptParam(source);
			String signMsgBase64 = DesUtilForNetParam.encryptMd5(source);
			args.clear();
			args.put("tokenId", token);
			args.put("encMsg", encMsgBase64);
			args.put("signMsg", signMsgBase64);
		}
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		return opt;
	}

	interface ITradeHandler {
		Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException;

		void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException;
	}

	private ITradeHandler mTradeHandler = null;

	public void setTradeHandler(ITradeHandler l) {
		mTradeHandler = l;
	}
}
