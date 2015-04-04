package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;

import android.os.Handler;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
/**
 * 用户信息收集
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午11:40:00
 */
public class ParSendUserActions extends AbsParam{
	public static final String URL="behavior/addbehavior.protobuf";
	private String infoList=null;
	private JSONArray mJobj=null;
	private WrapException mErr=null;
	
	public void setInfoList(String infoList){
		this.infoList=infoList;
	}
	public ParSendUserActions setParams(String infoList){
		this.infoList=infoList;
		return this;
	}
	public ParSendUserActions setParams(JSONArray infoList){
		this.mJobj=infoList;
		return this;
	} 
	public ParSendUserActions(String infoList) {
		super(0);
		this.infoList=infoList;
	}
	public ParSendUserActions(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParSendUserActions(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParSendUserActions(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParSendUserActions(String key, IReqNetFinished calbk) {
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
	protected void checkArgs() throws WrapException {
		if(mErr!=null){
			throw mErr;
		}
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		if(mJobj==null){
			try{
				mJobj=new JSONArray(infoList);
				setArgByte(mJobj.toString().getBytes());
			}catch(Exception e){
				mJobj=null;
				setArgByte(null);
				mErr=WrapException.wrap(e, "同步参数格式不对,要JSON格式的列表."+e);
			}
		}else{
			setArgByte(mJobj.toString().getBytes());
		}	 
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, false, false);
		return opt;
	}


     

}
