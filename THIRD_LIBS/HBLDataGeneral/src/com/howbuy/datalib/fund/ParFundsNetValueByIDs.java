package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;
/**
 *  单多条获取基金净值FundInfosList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午4:32:24
 */
public class ParFundsNetValueByIDs extends AbsParam{
	public static final String URL="start/navinfoquery.protobuf";
	//参数	值	能否为空	描述
	private String fundCode=null;//	000001-000002	M	基金代码
	/**
	 * 000001-000002	M	基金代码 
	 */
	public ParFundsNetValueByIDs setParams(String fundCode){
		this.fundCode=fundCode;
		return this;		
	}
	public ParFundsNetValueByIDs(long cacheTime) {
		super(cacheTime);
	}
	public ParFundsNetValueByIDs(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFundsNetValueByIDs(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFundsNetValueByIDs(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFundsNetValueByIDs(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundInfosList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		 addArg("fundCode",fundCode );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
