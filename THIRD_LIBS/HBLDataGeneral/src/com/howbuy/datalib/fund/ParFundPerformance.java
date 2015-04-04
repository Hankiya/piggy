package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.PerformanceInfoProto.PerformanceInfo;

/**
 * 获取公募基金业绩表现信息 返回 PerformanceInfo
 */
public class ParFundPerformance extends AbsParam {
	public static final String URLOPEN = "ften/fund/yjbx.protobuf";
	public static final String URLSIMU = "ften/simu/yjbx.protobuf";
	private int isPrivate=0;// 是否是私募.
	private String code = null;// 000001 N 基金代码;

	public ParFundPerformance setParams(String code, int issimu) {
		this.code = code;
		this.isPrivate = issimu;
		return this;
	}

	public ParFundPerformance(long cacheTime) {
		super(cacheTime);
	}

	public ParFundPerformance(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFundPerformance(int handType, IReqNetFinished calbk,
			long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFundPerformance(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFundPerformance(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return PerformanceInfo.parseFrom(is); // how to parse
	}

	@Override
	protected String getUrl() {
		 String s=getArgMap().get("isPrivate");
		 if(StrUtils.isNumeric(s)){
			isPrivate=Integer.parseInt(s); 
		 }
		return buildUrl(isPrivate==0 ? URLOPEN:URLSIMU);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code", code);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
