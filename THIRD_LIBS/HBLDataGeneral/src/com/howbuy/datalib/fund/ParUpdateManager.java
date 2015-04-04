package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundManagerInfoListProto.FundManagerInfoList;
/**
 * 基金经理数据更新 FundManagerInfoList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午3:37:37
 */
public class ParUpdateManager extends AbsParam{
	public static final String URL="manager/infoupdate.protobuf";
	private String  managerInfoVer	=null;//		基金经理信息版本号;
	
	public ParUpdateManager setParams( String managerInfoVer) {
		this.managerInfoVer = managerInfoVer;
		return this;
	}
	public ParUpdateManager(long cacheTime) {
		super(cacheTime);
	}
	public ParUpdateManager(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParUpdateManager(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParUpdateManager(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParUpdateManager(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundManagerInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("managerInfoVer",managerInfoVer );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
