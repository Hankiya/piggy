 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundTradeInfoProtos.FundTradeInfo;
/**
//23 获取基金交易属性
message FundTradeInfo{
	optional common.Common common=1;
	//申购状态
	optional string sgStatus = 2;
	//赎回状态
	optional string shStatus = 3;
	//定投状态
	optional string dtStatus = 4;
	//起购金额，精确到分，单位为分
	optional string minAmount = 5;
	//前端申购费率
    repeated PurchaseFeeRate prePurchaseFeeRates=6;
    //前端赎回费率
    repeated RedeemFeeRate preRedeemFeeRates=7;
    //后端申购费率
    repeated PurchaseFeeRate sufPurchaseFeeRates=8;
    //后端赎回费率
    repeated RedeemFeeRate sufRedeemFeeRates=9;
}

message PurchaseFeeRate{
    //金额区间
	optional string amountSection=1;
	//费率
	optional string rate=2;
}

message RedeemFeeRate{
    //持有时间区间
	optional string durationSection=1;
	//费率
	optional string rate=2;
}

 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFundTradeInfo extends AbsParam{
	public static final String URL="fund/fundTradeInfo.protobuf"; 
	//参数	值	能否为空	描述
	private String fundCode=null;//	000001	N	基金代码
	private String isPrivate=null;//是否为私募
	
	public AAParFundTradeInfo setParams(String fundCode,String isPrivate ){
	    this.fundCode=fundCode;
	    this.isPrivate=isPrivate;
		return this;
	} 
	public AAParFundTradeInfo(long cacheTime) {
		super(cacheTime);
	}
	public AAParFundTradeInfo(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFundTradeInfo(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFundTradeInfo(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFundTradeInfo(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundTradeInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundCode",fundCode );
		addArg("isPrivate",isPrivate );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
