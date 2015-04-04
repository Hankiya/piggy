package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundDruationProtos.FundDruationProto;
/**
 *获取基金走势图
 *返回 FundDruationProto
 *字段	描述
responseCode	
responseContent	操作结果成功或失败，失败时的原因
jjdm	基金代码
jyzt	交易状态
jzrq	时间
jjjz	净值（货币型为净值回报，私募基金为累计净值）
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午11:45:52
 */
public class ParFundRunChartData extends AbsParam{
	public static final String URL="nav/duration.protobuf";
	
	//参数	值	能否为空	描述
	private String fundCode=null;//	000001-00002	M	基金代码
	private String startDate=null;//			M	基金数据开始时间
	private String endDate=null;//				基金数据结束时间
	
	
	public ParFundRunChartData setParams(  String fundCode,
			String startDate, String endDate) {
		this.fundCode = fundCode;
		this.startDate = startDate;
		this.endDate = endDate;
		return this;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public ParFundRunChartData(long cacheTime) {
		super(cacheTime);
	}
	public ParFundRunChartData(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFundRunChartData(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFundRunChartData(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFundRunChartData(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  FundDruationProto.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundCode",fundCode );
		addArg("startDate",startDate );
		addArg("endDate", endDate);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
