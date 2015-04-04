package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
import com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew;
import com.howbuy.wireless.entity.protobuf.FundInfoMoneysProto.FundInfoMoneys;
import com.howbuy.wireless.entity.protobuf.FundInfoOpensProto.FundInfoOpens;
import com.howbuy.wireless.entity.protobuf.FundInfoSimusProto.FundInfoSimus;

/**
 * get return
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-3-6 下午1:19:05
 */
public class ParFundsNetValueBatch extends AbsParam {
	public static final String Type_Opens = "1";
	public static final String Type_Moneys = "2";
	public static final String Type_Licai = "5";
	public static final String Type_Simu = "3";
	public static final String Type_Close = "4";
	public static final String URL = "start/updatebytype.protobuf";
	private String type = null;//基金类型
	private String typeVer = null;//类型版本号

	public ParFundsNetValueBatch setParams(String type, String typeVer) {
		this.type = type;
		this.typeVer = typeVer;
		return this;
	}

	public ParFundsNetValueBatch(long cacheTime) {
		super(cacheTime);
	}

	public ParFundsNetValueBatch(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFundsNetValueBatch(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFundsNetValueBatch(String key, int handType, IReqNetFinished calbk, Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFundsNetValueBatch(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		type=getArgMap().get("type");
		if (type.equals(Type_Opens)) {
			return FundInfoOpens.parseFrom(is);
		} else if (type.equals(Type_Moneys)) {
			return FundInfoMoneys.parseFrom(is);
		} else if (type.equals(Type_Licai)) {
			return FundInfoMoneys.parseFrom(is);
		} else if (type.equals(Type_Simu)) {
			return FundInfoSimus.parseFrom(is);
		} else if (type.equals(Type_Close)) {
			return FundInfoClosesNew.parseFrom(is);
		}
		return Common.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("type", type);
		addArg("typeVer", typeVer);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
