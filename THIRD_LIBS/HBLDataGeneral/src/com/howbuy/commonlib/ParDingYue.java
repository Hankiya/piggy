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
 *
 *返回参数：（仅公共参数）Common
    订阅类型：推送订阅：1；短信订阅：2；取消推送订阅：3；取消短信订阅：4；
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParDingYue extends AbsParam {
	private static final String URL = "trustdaquan/trustproductbook.protobuf"; // 订阅
	
	private int type = 0; // N 订阅类型：推送，短信，取消推送订阅，取消短信订阅
	private String authCode = null;// 验证码
	private String yqnh = null; // Y 预期年化收益
	private String gsbj = null;// Y 公司背景
	private String cpnx = null;// Y 产品年限
	private String mDeviceTag = null; // Y 订阅推送时设备令牌
	private String phoneNum = null; // Y 订阅短信接收手机号码
	
	public ParDingYue(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}
	public ParDingYue(int handType, IReqNetFinished calbk ) {
		super(handType, calbk, 0);
	}
	public ParDingYue(String key, IReqNetFinished calbk ) {
		super(key, calbk, 0);
	}
	
	public ParDingYue(String key, int handType, IReqNetFinished calbk, Handler uiHand  ) {
		super(key, handType, calbk, uiHand, 0);
	}

    
	public void setType(int type) {
		this.type = type;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public void setYqnh(String yqnh) {
		this.yqnh = yqnh;
	}
	public void setGsbj(String gsbj) {
		this.gsbj = gsbj;
	}
	public void setCpnx(String cpnx) {
		this.cpnx = cpnx;
	}
	public void setmDeviceTag(String mDeviceTag) {
		this.mDeviceTag = mDeviceTag;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * @param mOrderType
	 *            短信 0，推送 1
	 * @param columns
	 *            栏目多个栏目用"；"
	 * @param phoneNum
	 *            用户手机号码
	 * @param yqnh
	 *            预期年化收益
	 * @param cpnx
	 *            产品年限
	 * @param gsbj
	 *            公司背景
	 * @param authCode
	 *            验证码
	 * @param mDeviceTag
	 *            推送设备tag
	 * @return
	 * @throws Exception
	 */
	public ParDingYue setParams(int orderType, String yqnh,
			String cpnx, String gsbj, String phoneNum, String authCode,
			String notifyToken) {
		this.type=orderType;
		this.yqnh=yqnh;
		this.cpnx=cpnx;
		this.gsbj=gsbj;
		this.phoneNum=phoneNum;
		this.authCode=authCode;
		this.mDeviceTag=notifyToken;
		return this;
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg( "authCode", authCode);
		addArg( "type", String.valueOf(type));
		addArg( "yqnh", yqnh);
		addArg( "gsbj", gsbj);
		addArg( "cpnx", cpnx);
		addArg( "mDeviceTag", mDeviceTag);
		addArg( "phoneNum", phoneNum);
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
