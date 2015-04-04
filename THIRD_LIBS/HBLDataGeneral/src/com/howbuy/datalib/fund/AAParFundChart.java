package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.HistoryFundNetValueChart;

/**
//28 获取净值图表
message HistoryFundNetValueChart{
    optional common.Common common=1;
	optional string now=2;
	repeated NetValueInfo netValueInfo=3;
	repeated ShareInfo shareInfo=4;
	repeated PartitionInfo partitionInfo=5;
}

message NetValueInfo{
    //净值日期
    optional string jzrq=1;
    //净值
    optional string jjjz=2;
    //7日年化
    optional string qrnh=3;
}

message ShareInfo{
    //分红日期
    optional string fhrq=1;
    //分红
    optional string dwfh=2;
}

message PartitionInfo{
    //分拆日期
    optional string fcrq=1;
    //分拆
    optional string fcbl=2;
}
 * @author rexy 840094530@qq.com
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFundChart extends AbsParam {

	public static final String URL = "fund/historyFundNetValueChart.protobuf";
	// 参数 值 能否为空 描述
	private String fundCode = null;// 000001 N 基金代码
	private String isPrivate = null;// 是否为私募
	private String startTime = null;// yymmdd N
									// 需要的数据起始时间（本地无数据，不传；不知道起始时间是何时，不传，代表需要获取到最老时间的数据）
	private String endTime = null;// yymmdd N
									// 需要数据的结束时间（本地无数据，不传；不知道结束时间是何时，不传，代表需要获取到最新时间的数据）
	private int needDataCount = 0;// int N需要的数据条数（仅在本地数据库没有数据时，传该值）

	public AAParFundChart setParams(String fundCode, String isPrivate, String fromTime, String toTime, int needCount) {
		this.fundCode = fundCode;
		this.isPrivate = isPrivate;
		startTime = fromTime;
		endTime = toTime;
		needDataCount = needCount;
		return this;
	}

	public AAParFundChart(long cacheTime) {
		super(cacheTime);
	}

	public AAParFundChart(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFundChart(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFundChart(String key, int handType, IReqNetFinished calbk, Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFundChart(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return HistoryFundNetValueChart.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundCode", fundCode);
		addArg("isPrivate", isPrivate);
		addArg("startTime", startTime);
		addArg("endTime", endTime);
		addArg("needDataCount", needDataCount);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
