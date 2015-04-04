package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.FundHiddenStockProtos.FundYxzcgMain;
/**
 * get 隐性重仓股列表
 * return FundYxzcgMain
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class ParFtenRecessiveHeavyHolding extends AbsParam{
	public static final String URL="ften/yxzcg/XXXX.protobuf";
	//参数	值	能否为空	描述
	private String code=null;//	000001	N	基金代码
	private int pageCount=1;	//		每页数量
	private int currentPage	=1;	//	当前页
	public ParFtenRecessiveHeavyHolding setParams(String code,int currentPage,int pageCount){
		this.pageCount=pageCount;
		this.currentPage=currentPage;
		this.code=code;
		return this;
	} 
	public ParFtenRecessiveHeavyHolding(long cacheTime) {
		super(cacheTime);
	}
	public ParFtenRecessiveHeavyHolding(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFtenRecessiveHeavyHolding(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFtenRecessiveHeavyHolding(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFtenRecessiveHeavyHolding(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundYxzcgMain.parseFrom(is);
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
		addArg("pageCount",pageCount );
		addArg("currentPage",currentPage );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
