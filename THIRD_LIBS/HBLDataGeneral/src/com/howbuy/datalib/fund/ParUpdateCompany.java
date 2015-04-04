package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundCompanyInfoListProto.FundCompanyInfoList;
/**
 *基金公司数据更新 FundCompanyInfoList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午3:37:37
 */
public class ParUpdateCompany extends AbsParam{
	public static final String URL="company/infoupdate.protobuf";
	private String  companyInfoVer	=null;//		基金经理信息版本号;
	
	public ParUpdateCompany setParams( String companyInfoVer) {
		this.companyInfoVer = companyInfoVer;
		return this;
	}
	public ParUpdateCompany(long cacheTime) {
		super(cacheTime);
	}
	public ParUpdateCompany(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParUpdateCompany(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParUpdateCompany(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParUpdateCompany(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundCompanyInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("companyInfoVer",companyInfoVer );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
