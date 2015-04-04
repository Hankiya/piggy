package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;
/**
 * 启动接口，负责查询主要数据的版本信息
 * 返回：（对应proto文件：hostDistribution.proto）
字段	值	能否为空	描述
responseCode	0，1	N	成功：1；失败：0
responseContent		Y	成功或者失败的原因。
versionNeedUpdate	0,1	N	软件是否要更新。2：不需更新；0：有更新；1：必须更新
updateUrl		Y	版本更新的url
updateDesc			版本更新提示语
serverTime		N	服务器时间
basicInfoNeedUpdate	0,1	N	基金基本信息是否需要更新
kfsNeedUpdate		N	开放式基金净值是否需要更新
fbsNeedUpdate		N	封闭式基金净值是否需要更新
hbsNeedUpdate		N	货币式基金净值是否需要更新
smNeedUpdate		N	私募基金净值是否需要更新
managerNeedUpdate	0,1	N	基金经理是否需要更新
companyNeedUpdate	0,1	N	基金公司是否需要更新
newsTypeNeedUpdate	0,1	N	资讯分类是否需要更新
opinionTypeNeedUpdate	0，1	N	研报分类是否需要更新
jsVer	string	N	js版本号
newsTypeList			资讯分类list
typeName		N	资讯子类标题名
id		N	资讯子类id
opinionTypeList			研报分类list
typeName		N	研报子类标题名
id		N	研报子类id
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 上午9:30:20
 */
public class ParStart extends AbsParam{
private static final String URL="start/datasynchronize.protobuf";
	
	//参数	值	能否为空	描述
private String basicInfoVer=null;//	20110522	N	基金基本信息版本号
private String 	kfsVer	=null;//		N	开放式基金净值版本号
private String 	fbsVer	=null;//		N	封闭式基金净值版本号
private String 	hbsVer	=null;//		N	货币式基金净值版本号
private String 	smVer	=null;//		N	私募基金净值版本号
private String 	managerVer=null;//		20110522	N	基金经理版本号
private String 	companyVer	=null;//	20110522	N	基金公司版本号
private String 	newsTypeVer	=null;//	1L	N	资讯分类版本号
private String 	opinionTypeVer=null;//		1L	N	研报分类版本号
private String  jsVer=null;//	string	N	js版本号	
 
public ParStart setParams( String basicInfoVer, String kfsVer,
		String fbsVer, String hbsVer, String smVer, String managerVer,
		String companyVer, String newsTypeVer, String opinionTypeVer,String jsVer) {
	this.basicInfoVer = basicInfoVer;
	this.kfsVer = kfsVer;
	this.fbsVer = fbsVer;
	this.hbsVer = hbsVer;
	this.smVer = smVer;
	this.managerVer = managerVer;
	this.companyVer = companyVer;
	this.newsTypeVer = newsTypeVer;
	this.opinionTypeVer = opinionTypeVer;
	this.jsVer=jsVer;
	return this;
}
public void setBasicInfoVer(String basicInfoVer) {
	this.basicInfoVer = basicInfoVer;
}
public void setKfsVer(String kfsVer) {
	this.kfsVer = kfsVer;
}
public void setFbsVer(String fbsVer) {
	this.fbsVer = fbsVer;
}
public void setHbsVer(String hbsVer) {
	this.hbsVer = hbsVer;
}
public void setSmVer(String smVer) {
	this.smVer = smVer;
}
public void setManagerVer(String managerVer) {
	this.managerVer = managerVer;
}
public void setCompanyVer(String companyVer) {
	this.companyVer = companyVer;
}
public void setNewsTypeVer(String newsTypeVer) {
	this.newsTypeVer = newsTypeVer;
}
public void setOpinionTypeVer(String opinionTypeVer) {
	this.opinionTypeVer = opinionTypeVer;
}
	public ParStart(long cacheTime) {
		super(cacheTime);
	}
	public ParStart(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParStart(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParStart(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParStart(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
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
		addArg("basicInfoVer", basicInfoVer);
		addArg("kfsVer",kfsVer );
		addArg("fbsVer",fbsVer );
		addArg("hbsVer", hbsVer);
		addArg("smVer",smVer );
		addArg("managerVer",managerVer );
		addArg("companyVer", companyVer);
		addArg("newsTypeVer",newsTypeVer );
		addArg("opinionTypeVer",opinionTypeVer );
		addArg("jsVer",jsVer ); 
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
