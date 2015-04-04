package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundBasicInfoProto.FundBasicInfoList;
/**
 *私募基金基本信息更新 FundBasicInfoList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午3:37:37
 */
public class ParUpdateSimuFundBasicInfo extends AbsParam{
	public static final String URL="simu/jjxx.protobuf";
	private String  fundInfoVer	=null;//		基金经理信息版本号;
	
	public ParUpdateSimuFundBasicInfo setParams( String fundInfoVer) {
		this.fundInfoVer = fundInfoVer;
		return this;
	}
	public ParUpdateSimuFundBasicInfo(long cacheTime) {
		super(cacheTime);
	}
	public ParUpdateSimuFundBasicInfo(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParUpdateSimuFundBasicInfo(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParUpdateSimuFundBasicInfo(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParUpdateSimuFundBasicInfo(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundBasicInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundInfoVer",fundInfoVer );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
