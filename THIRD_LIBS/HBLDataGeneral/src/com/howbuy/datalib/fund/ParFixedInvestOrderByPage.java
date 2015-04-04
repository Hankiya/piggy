 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundfixedProtos.Fundfixed;
/**
 *  分页获取定投排行数据
 *  返回  Fundfixed
type：14代表一年收益、16代表两年收益、17代表三年收益、18代表四年收益、19代表五年收益；
order:1代表降序、0代表升序；
kind: 1, 8, 5, 3, t, 9，b分别代表"股票型", "指数型", "债券型", "混合型", "结构型", "QDII型"，保本型
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午5:01:57
 */
public class ParFixedInvestOrderByPage extends AbsParam{
	public static final String URL="fund/dthb.protobuf";
	//参数	值	能否为空	描述
	private int pageNum=0;//		N	页数
	private int pageCount;//			一页显示多少
	private int type=0;//			分页字段（*）
	private int order=0;//			升降序（*）
	private String kind=null;	//		基金种类
	
	public ParFixedInvestOrderByPage setParams( int pageNum,
			int pageCount, int type, int order, String kind) {
		this.pageNum = pageNum;
		this.pageCount = pageCount;
		this.type = type;
		this.order = order;
		this.kind = kind;
		return this;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public ParFixedInvestOrderByPage(long cacheTime) {
		super(cacheTime);
	}
	public ParFixedInvestOrderByPage(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParFixedInvestOrderByPage(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParFixedInvestOrderByPage(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParFixedInvestOrderByPage(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  Fundfixed.parseFrom(is);//FundFixedUpdate
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("pageNum", pageNum);
		addArg("pageCount", pageCount);
		addArg("type",type );
		addArg("order", order);
		addArg("kind",kind ); 
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
