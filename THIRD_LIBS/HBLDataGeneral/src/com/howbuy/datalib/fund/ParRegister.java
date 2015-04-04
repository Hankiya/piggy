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
 *  // 注册
 *  返回Register
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午11:14:43
 */
public class ParRegister extends AbsParam{
	public static final String URL="register/register.protobuf";
	
	//参数	值	能否为空	描述
	private String phone=null;//	13333333333	N	用户名（手机号）
	private String password=null;//	abcdefg	N	密码
	private String verificationCode	=null;//1234	Y	验证码
	private int regSource=2;//	2		来源
	
	
	public ParRegister setParams( String phone, String password,
			String verificationCode, int regSource) {
		this.phone = phone;
		this.password = password;
		this.verificationCode = verificationCode;
		this.regSource = regSource;
		return this;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public void setRegSource(int regSource) {
		this.regSource = regSource;
	}

	public ParRegister(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParRegister(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParRegister(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParRegister(String key, IReqNetFinished calbk) {
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
		addArg("phone",phone );
		addArg("password", password);
		addArg("verificationCode", verificationCode);
		addArg("regSource",regSource );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
