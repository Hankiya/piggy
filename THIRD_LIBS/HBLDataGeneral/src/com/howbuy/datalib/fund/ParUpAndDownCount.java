package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown;
/**
 * 获取某只基金顶和踩的次数 UpDown
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午2:42:32
 */
public class ParUpAndDownCount extends AbsParam{
	public static final String URL="updown/queryupdown.protobuf";
 	private String code=null;//基金代码
 	public ParUpAndDownCount setParams(String code){
 		this.code=code;
 		return this;
 	}
	public ParUpAndDownCount(long cacheTime) {
		super(cacheTime);
	}
	public ParUpAndDownCount(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParUpAndDownCount(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParUpAndDownCount(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParUpAndDownCount(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  UpDown.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
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
