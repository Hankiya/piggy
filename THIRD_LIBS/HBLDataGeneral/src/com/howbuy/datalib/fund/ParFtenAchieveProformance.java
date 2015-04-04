 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.PerformanceInfoProto.PerformanceInfo;
/**
 * get 业绩表现
 * return PerformanceInfo
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class ParFtenAchieveProformance extends AbsParam{
	public static final String URL="ften/yjbx/yjbx.protobuf";
	//参数	值	能否为空	描述
	private int isPrivate=0;//是否为私募
	private String code=null;//	000001	N	基金代码
	public ParFtenAchieveProformance setParams(int isPrivate,String code){
		this.isPrivate=isPrivate;
		this.code=code;
		return this;
	}
	public ParFtenAchieveProformance(long cacheTime) {
		super(cacheTime);
	}
	public ParFtenAchieveProformance(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFtenAchieveProformance(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFtenAchieveProformance(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFtenAchieveProformance(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  PerformanceInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code",code );
		addArg("isPrivate", isPrivate);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
