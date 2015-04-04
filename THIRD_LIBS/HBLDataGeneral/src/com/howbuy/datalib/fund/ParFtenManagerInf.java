package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.ManagerDetailInfoListProto.ManagerDetailInfoList;
/**
 * get 基金经理详细信息页
 * return ManagerDetailInfoList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:44:14
 */
public class ParFtenManagerInf extends AbsParam{
	public static final String URL="ften/manager/detail/XXXX.protobuf?type=YYYY";
	//参数	值	能否为空	描述
	private String manageid=null;//	000001	N	基金经理代码
	private String isPrivate="0";
	public ParFtenManagerInf setParams(String manageid,String isPrivate){
		this.manageid=manageid;
		this.isPrivate=isPrivate;
		return this;
	}
	public ParFtenManagerInf(long cacheTime) {
		super(cacheTime);
	}
	public ParFtenManagerInf(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFtenManagerInf(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFtenManagerInf(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFtenManagerInf(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  ManagerDetailInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		String x=getArgMap().get("manageid");
		String y=getArgMap().get("isPrivate");
		x=(x==null?this.manageid:x);
		y=(y==null?this.isPrivate:y);
		y="1".equals(y)?"sm":"gm";
	    if(StrUtils.isEmpty(x)){
	      return buildUrl(URL);
	    }
		return buildUrl(URL.replace("XXXX", x).replace("YYYY", y));
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		//addArg("code",manageid );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
