 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.RegisterProtos.Register;
/**
 * // 获取验证码
 * 返回Register
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午11:14:38
 */
public class ParCheckCode extends AbsParam{
	public static final String URL="register/register.protobuf";
	//参数	值	能否为空	描述
	private String regSource=null;//	2	N	注册来源
	private String phone=null;//	13333333333	N	用户名（手机号）
	
	public ParCheckCode setParams( String regSource, String phone) {
		this.regSource = regSource;
		this.phone = phone;
		return this;
	}
	public void setRegSource(String regSource) {
		this.regSource = regSource;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ParCheckCode(String regSource, String phone) {
		super(0);
		this.regSource = regSource;
		this.phone = phone;
	}
	public ParCheckCode(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParCheckCode(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParCheckCode(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParCheckCode(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception { 
	   return  Register.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("regSource", regSource);
		addArg("phone",phone );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
