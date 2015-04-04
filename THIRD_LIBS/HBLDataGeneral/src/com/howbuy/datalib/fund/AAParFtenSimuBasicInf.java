 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.SimuBasicInfoProto.SimuBasicInfo;
/**
//私募基金基本信息
message SimuBasicInfo{
	optional common.Common common=1;
	optional int32 hmpj=2;		// 好买评级(0-5)
	optional string gsszd=3;	// 公司所在地
}
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFtenSimuBasicInf extends AbsParam{
	public static final String URL="/ften/simu/baseInfo.protobuf"; 
	//参数	值	能否为空	描述
	private String code=null;//	000001	N	基金代码
	public AAParFtenSimuBasicInf setParams(String code ){
	    this.code=code;
		return this;
	} 
	public AAParFtenSimuBasicInf(long cacheTime) {
		super(cacheTime);
	}
	public AAParFtenSimuBasicInf(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFtenSimuBasicInf(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFtenSimuBasicInf(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFtenSimuBasicInf(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  SimuBasicInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code",code );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
