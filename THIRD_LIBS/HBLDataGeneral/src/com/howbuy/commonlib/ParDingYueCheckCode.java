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
 * 获取订阅验证码 
 * 返回参数：（仅公共参数）Common
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParDingYueCheckCode extends AbsParam {
	private static final String URL = "trustdaquan/getverifycode.protobuf"; // 获取验证码
	
	private String phoneNum = null; // unnull  订阅短信接收手机号码
	public ParDingYueCheckCode(String phoneNum) {
		super(0);
		this.phoneNum=phoneNum;
	}
	public ParDingYueCheckCode(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}
	public ParDingYueCheckCode(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}
	public ParDingYueCheckCode(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	public ParDingYueCheckCode(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

   
    
	/**
	 * 订阅短信接收手机号码
	 * @param  version
	 * @throws
	 */
	public void setParams(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("phoneNum", phoneNum); // unnull 订阅短信接收手机号码
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(false, true, false);
	  return opt;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return Common.parseFrom(is);	
	}

}
