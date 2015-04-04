package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;

/**
检查应用程序是否需要更新
返回参数：HostDistribution
参数	值	能否能为空	描述
versionNeedUpdate		N	2不需要更新，0有更新
updateUrl		Y	新版本安装包地址
updateDesc		Y	版本更新提示
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParCheckAppUpdate extends AbsParam {
	private static final String URL = "start/checkupdate.protobuf"; // 更新接口
	public ParCheckAppUpdate(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}
	public ParCheckAppUpdate(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}
	public ParCheckAppUpdate(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	public ParCheckAppUpdate(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}
 
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
      return HostDistribution.parseFrom(is); 
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(true, true, false);
	  return opt;
	}

}
