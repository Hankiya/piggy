package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
/**
 * get 
 * return 
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 下午1:19:05
 */
 public class AAAAAAAAAAAAAAAAA extends AbsParam{
	public static final String URL="ften/company/000001.protobuf";
	private String code=null; 
	public AAAAAAAAAAAAAAAAA setParams(String code){
		this.code=code;
		return this;
	}
	public AAAAAAAAAAAAAAAAA(long cacheTime) {
		super(cacheTime);
	}
	public AAAAAAAAAAAAAAAAA(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAAAAAAAAAAAAAAAA(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAAAAAAAAAAAAAAAA(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAAAAAAAAAAAAAAAA(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  Common.parseFrom(is);
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
		setParamType(false, true, false);
		return opt;
	}


     

}
