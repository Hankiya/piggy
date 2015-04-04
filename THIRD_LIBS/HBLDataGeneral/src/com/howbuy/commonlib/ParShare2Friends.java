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
  推荐好友
 *返回参数：（仅公共参数）Common
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParShare2Friends extends AbsParam {
	private static final String URL = "mgm/addmgm.protobuf"; // 好友分享
	
	private String phones=null;//电话号15028934723-17383777348;
	
	public ParShare2Friends(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}
	public ParShare2Friends(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}
	public ParShare2Friends(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	public ParShare2Friends(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return Common.parseFrom(is);
	}

	/**
	 * 好友手机号码，多个以‘-’隔开
	 * @throws
	 */
	public ParShare2Friends setParams(String phones){
		this.phones=phones;
		return this;
	}
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("phones", phones);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(true, true, false);
	  return opt;
	}

}
