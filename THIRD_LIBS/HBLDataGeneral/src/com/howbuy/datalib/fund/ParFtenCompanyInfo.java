package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.CompanyInfoProto.CompanyInfo;

/**
 * get 基金公司 信息. return CompanyInfo
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-3-6 上午9:39:17
 */
public class ParFtenCompanyInfo extends AbsParam {
	public static final String URL = "ften/company/XXXX.protobuf";
	// 参数 值 能否为空 描述
	private String code = null;// 000001 N 基金代码
	private String justName = "0"; // 0或不传，则下发全部信息，1时，只下发公司名称字段

	public ParFtenCompanyInfo setParams(String code, String justName) {
		this.code = code;
		this.justName = justName;
		return this;
	}

	public ParFtenCompanyInfo(long cacheTime) {
		super(cacheTime);
	}

	public ParFtenCompanyInfo(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFtenCompanyInfo(int handType, IReqNetFinished calbk,
			long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFtenCompanyInfo(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFtenCompanyInfo(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return CompanyInfo.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		String x = getArgMap().get("code");
		x = (x == null ? this.code : x);
		if (StrUtils.isEmpty(x)) {
			return buildUrl(URL);
		}
		return buildUrl(URL.replace("XXXX", x));
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		 addArg("justName",justName );
		 addArg("code",code );
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
