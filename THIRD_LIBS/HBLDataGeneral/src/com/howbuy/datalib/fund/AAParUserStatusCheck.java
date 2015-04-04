 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.UserStatusCheck.UserCheck;
/**
 //2.2用户身份验证 
message UserCheck {
	optional common.Common common=1;
	//注销标志
	required string logoutFlag=2;
}
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParUserStatusCheck extends AbsParam{
	public static final String URL="fund/userStatusCheck.protobuf"; 
	//参数	值	能否为空	描述
	private String  custNo=null;//登录返回的用户唯一标识
	
	public AAParUserStatusCheck setParams(String custNo){
	    this.custNo=custNo;
		return this;
	} 
	public AAParUserStatusCheck(long cacheTime) {
		super(cacheTime);
	}
	public AAParUserStatusCheck(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParUserStatusCheck(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParUserStatusCheck(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParUserStatusCheck(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  UserCheck.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("custNo",custNo );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
