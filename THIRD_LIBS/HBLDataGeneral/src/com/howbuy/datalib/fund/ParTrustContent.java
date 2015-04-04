package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.TrustDetailInfoProto.TrustDetailInfo;
/**
 * 获取信托详情
 *返回 TrustDetailInfo
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午2:54:33
 */
public class ParTrustContent extends AbsParam{
	public static final String URL="trust/detail.protobuf";
	private String id=null;//		N	固定收益产品id;
	public ParTrustContent setParams(String id){
		this.id=id;
		return this;
	}
	public ParTrustContent(long cacheTime) {
		super(cacheTime);
	}
	public ParTrustContent(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParTrustContent(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParTrustContent(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParTrustContent(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  TrustDetailInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("id",id );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
