 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo;
/**
//22获取指定特性产品
message FundInfo {
	optional common.Common common=1;
	//基金列表
	repeated FundList fundList=2;
	
}
message FundList {
	//基金code
	optional string fundCode=1;
}
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFundInfoByType extends AbsParam{
	public static final String URL="fund/fundInfoByType.protobuf"; 
	//参数	值	能否为空	描述
	private int  type=1;//1:热销,2:首页推荐,3推荐
	
	public AAParFundInfoByType setParams(int type){
	    this.type=type;
		return this;
	} 
	public AAParFundInfoByType(long cacheTime) {
		super(cacheTime);
	}
	public AAParFundInfoByType(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFundInfoByType(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFundInfoByType(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFundInfoByType(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("type",type );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
