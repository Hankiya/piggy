 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
/**
 * 产品信息订阅
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午1:54:32
 */
public class ParBook extends AbsParam{
	public static final String URL="common/addbook.htm";
	
	//参数	值	是否能为空	描述
	private int type=0;//Int	N	订阅类型：短信：0；推送：1
	private String columns=null;//	 	N	订阅多个栏目时，栏目ID用分号隔开，如：1234;1238;1287
	private String phoneNum=null;//	 	Y	短信订阅时接受订阅短信的号码
	private String token=null;//	 	Y	推送订阅时设备令牌
	private String deviceId	=null;// 	Y	推送订阅时设备ID
	private String customNo	=null;// 	Y	推送订阅时用户ID
	private String productId=null;//	Y	推送订阅时产品ID
 
	/**
	 * @param  type 短信 0，推送 1
	 * @param  columns 多个栏目时，栏目ID用分号隔开，如：1234;1238;1287
	 * @throws
	 */
	public ParBook setParams(  int type, String columns, String phoneNum,
			String token, String deviceId, String customNo, String productId) {
		this.type = type;
		this.columns = columns;
		this.phoneNum = phoneNum;
		this.token = token;
		this.deviceId = deviceId;
		this.customNo = customNo;
		this.productId = productId;
		return this;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setCustomNo(String customNo) {
		this.customNo = customNo;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public ParBook(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParBook(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParBook(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParBook(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  Common.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("type",type );
		addArg("columns",columns );
		addArg("phoneNum", phoneNum);
		addArg("token", token);
		addArg("deviceId", deviceId);
		addArg("customNo", customNo);
		addArg("productId", productId);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
