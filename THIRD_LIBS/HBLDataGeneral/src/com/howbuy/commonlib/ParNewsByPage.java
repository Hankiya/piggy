package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.NewsInfoProto.NewsInfo;

/**
 * 分页按类型获取新闻
 返回：（对应proto文件：NewsInfo.proto）
字段	值	能否为空	描述
responseCode		N	
responseContent		Y	操作结果成功或失败，失败时的原因
totalNum		N	总数
newsList			
title		N	资讯标题
label		Y	标签
publishTime		N	资讯的发布时间
newsType		N	资讯的类型
id		N	资讯的id(拼接url)
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParNewsByPage extends AbsParam {
	private static final String URL = "news/listbytype.protobuf"; // 按类型获取新闻
	//参数	值	能否为空	描述
	private String newsType	=null;//0	N	分类编码
	private String basicType=null;//		Y	子类型（信托资讯传209，不传则为非信托资讯）
	private int pageCount=1;//	20	N	每页条数
	private int currentPage=1;//	0	N	当前请求页
	private int newestVer=0;//	int	N	当前批次请求最新一条的时间戳
	public ParNewsByPage(long cacheTime) {
		super( cacheTime);
	}
	public ParNewsByPage(int handType, Handler uiHand,long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
	public ParNewsByPage(int handType, IReqNetFinished calbk,long cacheTime) {
		super(handType, calbk, cacheTime);
	}
	public ParNewsByPage(String key, IReqNetFinished calbk,long cacheTime) {
		super(key, calbk, cacheTime);
	}
	public ParNewsByPage(String key, int handType, IReqNetFinished calbk,
			Handler uiHand,long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}
 
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setNewestVer(int newestVer) {
		this.newestVer = newestVer;
	}
	/*
	 * @param  newsType:129016, basicType:209
	 */
	public ParNewsByPage setParams(String newstype,String basicType,int currentPage,int pageCount,int newestVer){
		this.newsType=newstype;
		this.basicType=basicType;
		this.currentPage=currentPage;
		this.pageCount=pageCount;
		this.newestVer=newestVer;
		return this;
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return NewsInfo.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("newsType", newsType);
		addArg("basicType", basicType);
		addArg("currentPage",currentPage );
		addArg("pageCount",pageCount );
		addArg("newestVer",newestVer );
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}

}
