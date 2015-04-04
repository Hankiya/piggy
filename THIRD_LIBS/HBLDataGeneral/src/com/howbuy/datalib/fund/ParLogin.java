package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.LoginProto.Login;
/**
 * // 登陆
 * 返回：Login（对应proto文件：login.proto）
字段	值	能否为空	描述
responsecode	1,0，-1，-2	N	1：成功；0：失败，用户名已存在；-1：失败，验证码不匹配；-2：失败，验证码失效
responseContent		Y	操作结果成功或失败，失败时的原因
custNo			客户号
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午11:19:56
 */
public class ParLogin extends AbsParam{
	public static final String URL="login/login.protobuf";
	
	private String userName=null;
	private String loginPasswd=null; 
	
	 
	public ParLogin setPareams(String userName, String loginPasswd) {
		this.userName = userName;
		this.loginPasswd = loginPasswd;
		return this;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLoginPasswd(String loginPasswd) {
		this.loginPasswd = loginPasswd;
	}

	public ParLogin(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParLogin(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParLogin(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParLogin(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  Login.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("userName",userName );
		addArg("loginPasswd", loginPasswd);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
