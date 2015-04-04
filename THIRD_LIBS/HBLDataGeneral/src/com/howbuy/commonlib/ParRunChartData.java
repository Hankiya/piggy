package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.XttztjListProtos.XttztjList;

/**
 * 获取信托产业走势图(月均走势图) 
返回参数：XttztjList
yjzsList			月均走势数据列表
month			月份
income			收益
投资方向代号：全部=00,工商企业=20,房地产=11,基础设施=13,金融=12,工矿企业=22,其他=99
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParRunChartData extends AbsParam {
	private static final String URL = "trustdaquan/xttztjduration.protobuf"; // 月均走势图
	
	private String tzfxCode = null; // unnull  投资方向代号
	public ParRunChartData(String tzfxCode,long cacheTime) {
		super(cacheTime);
		this.tzfxCode=tzfxCode;
	}
	public ParRunChartData(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
	public ParRunChartData(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}
	public ParRunChartData(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	public ParRunChartData(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}


   
    
	/**
	 *  投资方向代号
	 * @param  version
	 * @throws
	 */
	public void setParams(String tzfxCode) {
		this.tzfxCode = tzfxCode;
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("tzfxCode", tzfxCode); // unnull 投资方向代号
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(false, true, false);
	  return opt;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return XttztjList.parseFrom(is);	
	}

}
