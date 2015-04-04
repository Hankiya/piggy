package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.SimuRecommendProto.SimuRecommend;

/**
 * get return
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-3-6 下午1:19:05
 */
public class AAParSimuRecommend extends AbsParam {
	public static final String URL = "/simu/simuRecommend.protobuf";

	public AAParSimuRecommend(long cacheTime) {
		super(cacheTime);
	}

	public AAParSimuRecommend(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParSimuRecommend(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParSimuRecommend(String key, int handType, IReqNetFinished calbk, Handler uiHand,
			long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParSimuRecommend(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return SimuRecommend.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}
}
