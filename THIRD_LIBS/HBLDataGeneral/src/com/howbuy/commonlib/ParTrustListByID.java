package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.TrustInfoListProto.TrustInfoList;

/**
 * 单多条获取信托列表 
返回参数：TrustInfoList
参数	值	能否能为空	描述
productList			产品列表
{pid		N	产品ID
cpmc		N	产品名称
xszt		Y	销售状态
cpqxCode		Y	产品期限
yqsyCode		Y	预期收益
qszjCode		Y	起始资金
hmdp		Y	好买点评
cpjs		Y	产品介绍
clrq		Y	成立日期
fsrq		Y	发售日期
tzfxCode		Y	投资方向
fxkz		Y	风险控制
dycp		Y	抵押产品种类
cpsm		Y	产品说明
gsmc		Y	公司名称
cid		Y	公司id
gsbjCode		Y	公司背景
szd		Y	所在地
cdpjc		Y	产品简称
xtfl		Y	信托分类}
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParTrustListByID extends AbsParam {
	private static final String URL = "trustdaquan/querytrustinfolist.protobuf"; // 单多条
	
	private String pids = null; // unnull  想要获取的产品的id，多个产品id之间用分号隔开
	
	public ParTrustListByID(String pids,long cacheTime) {
		super(cacheTime);
		this.pids=pids;
	}
	public ParTrustListByID(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
	public ParTrustListByID(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}
	public ParTrustListByID(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	public ParTrustListByID(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}
	/**
	 * 想要获取的产品的id，多个产品id之间用分号隔开
	 * @param  pids
	 * @throws
	 */
	public void setParams(String pids) {
		this.pids = pids;
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("pids", pids); // unnull 想要获取的产品的id，多个产品id之间用分号隔开
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(false, true, false);
	  return opt;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return TrustInfoList.parseFrom(is);	
	}

}
