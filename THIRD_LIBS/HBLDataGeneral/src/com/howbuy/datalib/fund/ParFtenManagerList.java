package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.ManagerInfoListProto.ManagerInfoList;
/**
 * get 基金经理列表
 * return ManagerInfoList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:40:41
 */
public class ParFtenManagerList extends AbsParam{
	public static final String URL="ften/manager/XXXX.protobuf";
	//参数	值	能否为空	描述
	private String code=null;//	000001	N	基金代码
	public ParFtenManagerList setParams(String code){
		this.code=code;
		return this;
	}
	public ParFtenManagerList(long cacheTime) {
		super(cacheTime);
	}
	public ParFtenManagerList(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFtenManagerList(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFtenManagerList(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFtenManagerList(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  ManagerInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		String x=getArgMap().get("code");
		x=(x==null?this.code:x);
	    if(StrUtils.isEmpty(x)){
	      return buildUrl(URL);
	    }
		return buildUrl(URL.replace("XXXX", x));
	}
	
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		//addArg("code",code );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
