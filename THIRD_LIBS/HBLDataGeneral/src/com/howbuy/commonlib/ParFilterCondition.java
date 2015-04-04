package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.QueryConditionsProtos.QueryConditionsProto;

/**
 * 获取查询条件
返回参数：QueryConditionsProto
参数	值	能否能为空	描述
version			当前最新版本
gsbjList		Y	公司背景列表
{gsbjCode	String	N	公司背景代号
gsbjValue	String	N	公司背景描述}
qszjList		Y	起始资金列表
{qszjCode	String	N	起始资金代号
qszjValue	String	N	起始资金描述}
xtflList		N	信托分类列表
{xtflCode	String	N	信托分类代号
xtlfValue	String	N	信托分类描述}
tzfxList		N	投资方向列表
{tzfxCode	String	N	投资方向代号
tzfxValue	String	N	投资方向描述
yqsyList		N	预期收益列表
yqsyCode	String	N	预期收益代号
yqsyValue	String	N	预期收益描述}
cpqxList		N	产品期限列表
{cpqxCode	String	N	产品期限代号
cpqxValue	String	N	产品期限描述}
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParFilterCondition extends AbsParam {
	private static final String URL = "trustdaquan/getquerycondtions.protobuf"; // 获取查询条件
	
	private String version = null; // unnull  信托条件版本
	
	public ParFilterCondition(String version,long cacheTime) {
		super(cacheTime);
		this.version=version;
	}
	public ParFilterCondition(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
	public ParFilterCondition(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}
	public ParFilterCondition(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	public ParFilterCondition(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

   
    
	/**
	 * 信托条件版本
	 * @param  version
	 * @throws
	 */
	public void setParams(String version) {
		this.version = version;
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("version", version); // unnull 信托条件版本
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(false, true, false);
	  return opt;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return QueryConditionsProto.parseFrom(is);	
	}

}
