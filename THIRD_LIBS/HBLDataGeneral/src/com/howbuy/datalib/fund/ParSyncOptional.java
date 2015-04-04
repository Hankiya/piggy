package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.ICSynFavFundProtos.ICSynFavFund;
/**
 * get 
 * return 
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 下午1:19:05
 */
 public class ParSyncOptional extends AbsParam{
	public static final String URL="favfund/datasynchronize.protobuf";
	private String paramsJson=null; 
	public ParSyncOptional setParams(String paramsJson){
		this.paramsJson=paramsJson;
		return this;
	}
	public ParSyncOptional(long cacheTime) {
		super(cacheTime);
	}
	public ParSyncOptional(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParSyncOptional(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParSyncOptional(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParSyncOptional(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  ICSynFavFund.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
	}
	
	@Override
	protected byte[] getArgByte() {
		// TODO Auto-generated method stub
		return paramsJson.getBytes();
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
