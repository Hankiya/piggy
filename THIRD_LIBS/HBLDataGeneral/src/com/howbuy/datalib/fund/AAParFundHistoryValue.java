 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueOfPageProtos.HistoryFundNetValueOfPage;
/**

//22 获取净值历史
message HistoryFundNetValueOfPage{
    optional common.Common common=1;
	optional string count=2;
	repeated Info info=3;
}

message Info{
    //净值日期
    optional string jzrq=1;
    //净值
    optional string jjjz=2;
    //累计净值
    optional string ljjz=3;
    //单位增幅
    optional string hbxx=4;
}
 * @author rexy  840094530@qq.com 
 * @date 2014-3-6 上午9:48:27
 */
public class AAParFundHistoryValue extends AbsParam{
	public static final String URL="fund/historyFundNetValueOfPage.protobuf"; 
	//参数	值	能否为空	描述
	private String fundCode=null;//	000001	N	基金代码
	private String isPrivate=null;//是否为私募
	private int pageNum=1;
	private int pageCount=1;
	
	public AAParFundHistoryValue setParams(String fundCode,String isPrivate,int pageNum,int pageCount ){
	    this.fundCode=fundCode;
	    this.isPrivate=isPrivate;
	    this.pageNum=pageNum;
	    this.pageCount=pageCount;
		return this;
	} 
	public AAParFundHistoryValue(long cacheTime) {
		super(cacheTime);
	}
	public AAParFundHistoryValue(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public AAParFundHistoryValue(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public AAParFundHistoryValue(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public AAParFundHistoryValue(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  HistoryFundNetValueOfPage.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
	    return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("fundCode",fundCode );
		addArg("isPrivate",isPrivate );
		addArg("pageNum", pageNum);
		addArg("pageCount", pageCount);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
