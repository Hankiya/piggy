package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.UpDownProtos.UpDown;
/**
 * 顶或踩某只基金 UpDown
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午2:43:00
 */
public class ParUpOrDownFund extends AbsParam{
	public static final String URL="updown/addupdown.protobuf";
	//参数	类型 	是否能为 描述
	private int code=0;//	Int	 	基金代码
	private String  type=null;//	 	 	类型
	private String custid=null;//	 	 	用户id
	private int action	=1;// 	 	0:踩 1: 顶.
 
	
	public ParUpOrDownFund setParams( int code, String type,
			String custid, int action) {
		this.code = code;
		this.type = type;
		this.custid = custid;
		this.action = action;
		return this;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public ParUpOrDownFund(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParUpOrDownFund(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParUpOrDownFund(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParUpOrDownFund(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  UpDown.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code",code );
		addArg("type", type);
		addArg("custid",custid );
		addArg("action",action );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
