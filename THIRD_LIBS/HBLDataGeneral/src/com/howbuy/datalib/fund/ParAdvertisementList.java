 package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
/**
 * 获取广告列表
 * 返回 AdvertList
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午1:34:20
 */
public class ParAdvertisementList extends AbsParam{
	public static final String URL="advert/advertlist.protobuf";
	private int imageWidth=720;
	private int imageHeight=300;
	
	public ParAdvertisementList setParams( int imageWidth,int imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		return this;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public ParAdvertisementList(long cacheTime) {
		super(cacheTime);
	}
	public ParAdvertisementList(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParAdvertisementList(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParAdvertisementList(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParAdvertisementList(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  AdvertList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("imageWidth", imageWidth);
		addArg("imageHeight", imageHeight);
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
