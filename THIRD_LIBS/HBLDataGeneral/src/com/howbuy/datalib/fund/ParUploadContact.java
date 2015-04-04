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
 * 上传联系人数据 Common
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午4:19:45
 */
public class ParUploadContact extends AbsParam{
	public static final String URL="common/uploadContactInfo.htm";
	private String contactsInfoList=null;
	private JSONArray mJobj=null;
	private WrapException mErr=null;
    public ParUploadContact setParams(String contactsInfoList){
    	this.contactsInfoList=contactsInfoList;
    	return this;
    }
    public ParUploadContact setParams(JSONArray contactsInfoList){
    	this.mJobj=contactsInfoList;
    	return this;
    }
	public ParUploadContact(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParUploadContact(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParUploadContact(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParUploadContact(String key, IReqNetFinished calbk) {
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
				if(contactsInfoList!=null){
					mJobj=new JSONArray(contactsInfoList);
					setArgByte(mJobj.toString().getBytes());	
				}
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
		setParamType(true, true, false);
		return opt;
	}


     

}
