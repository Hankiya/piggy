package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common;

/**
预约信托或私募
返回参数：（仅公共参数）Common
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParMakeAppointment extends AbsParam {
	private static final String URL = "trust/preContract.protobuf"; // 预约
	private String custName=null;	//	N	用户称呼
	private String mobile	=null;	//N	用户联系方式
	private int fundType=0;	//	N	预约产品种类:固定收益:0；私募：1（无该字段时为固定收益预约）
	private String  memo	=null;	//Y	用户备注信息
	
	public ParMakeAppointment(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}
	public ParMakeAppointment(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}
	public ParMakeAppointment(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	public ParMakeAppointment(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

 
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return Common.parseFrom(is);
	}

	public ParMakeAppointment setParams(String name, String phone,
			String memo, int fundType){
		this.custName=name;
		this.mobile=phone;
		this.memo=memo;
		this.fundType=fundType;
		return this;
	}
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("custName", custName);
		addArg("mobile", mobile);
		addArg("fundType", fundType);
		addArg("memo", memo);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(true, true, false);
	  return opt;
	}

}
