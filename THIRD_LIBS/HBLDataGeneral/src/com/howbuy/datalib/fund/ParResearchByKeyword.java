package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.OpinionInfoProto.OpinionInfo;
/**
 * 根据关键字获取研报.
返回：（对应proto文件：OpinionInfo.proto）
字段	值	能否为空	描述
responseCode		N	
responseContent		Y	操作结果成功或失败，失败时的原因
totalNum		N	总数
opinionList			
title		N	研报标题
label		Y	标签
publishTime		N	研报的发布时间
id		N	研报的id(拼接url)
tagName			关键字
 * @author rexy  840094530@qq.com 
 * @date 2014-3-4 下午5:30:08
 */
public class ParResearchByKeyword extends AbsParam{
	public static final String URL = "opinion/listbytag.protobuf";
	
	//参数	值	能否为空	描述
	private String keyword=null;//	0	N	关键字
	private int pageCount=1;//	20	N	每页条数
	private int currentPage	=1;// 0  N	当前请求页
	public ParResearchByKeyword(long cacheTime) {
		super(cacheTime);
	}
	public ParResearchByKeyword(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
 
	public ParResearchByKeyword(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParResearchByKeyword(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParResearchByKeyword(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	   public ParResearchByKeyword setParams(String  keyword,int currentPage,int pageCount){
	    	this.keyword=keyword;
	    	this.currentPage=currentPage;
	    	this.pageCount=pageCount;
	    	return this;
	    }
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return OpinionInfo.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("keyword",keyword);
		addArg("pageCount",pageCount);
		addArg("currentPage",currentPage);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);//maybe post.
		return opt;
	}


     

}
