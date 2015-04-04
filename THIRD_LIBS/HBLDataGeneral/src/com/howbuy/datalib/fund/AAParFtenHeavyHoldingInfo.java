 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.ZccgInfoListProtos.ZccgInfo;
/**
 * get 重仓持股列表
 * //8.19重仓持股列表 
message ZccgInfo {
	optional common.Common common=1;
	//股票列表
	repeated StockList stockList=2;
	
}
message StockList {
	//股票名称
	optional string stockName=1;
	//市值
	optional string marketValue=2;
	//占比
	optional string percentage=3;
}
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFtenHeavyHoldingInfo extends AbsParam{
	public static final String URL="fund/zccgInfoList.protobuf"; 
	//参数	值	能否为空	描述
	private String fundCode=null;//	000001	N	基金代码
	private String isPrivate=null;//是否为私募
	private String season=null;//季度
	
	public AAParFtenHeavyHoldingInfo setParams(String fundCode,String isPrivate,String season){
	    this.fundCode=fundCode;
	    this.isPrivate=isPrivate;
	    this.season=season;
		return this;
	} 
	public AAParFtenHeavyHoldingInfo(long cacheTime) {
		super(cacheTime);
	}
	public AAParFtenHeavyHoldingInfo(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFtenHeavyHoldingInfo(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFtenHeavyHoldingInfo(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFtenHeavyHoldingInfo(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  ZccgInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundCode",fundCode );
		addArg("isPrivate",isPrivate );
		addArg("season",season );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
