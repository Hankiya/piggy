package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.FundInfoSimusOfPageProto.FundInfoSimusOfPage;
/**
 * 分页获取私募基金净值
 * 返回 FundInfoSimusOfPage.
orderType：排序类型：
1：净值日期；2：净值；3：日增幅；4：周增幅；5：月增幅；6：季增福；7：半年增幅；8：年增幅；9：今年以来；10：万份收益；11：七日年化收益；12：月收益；13：半年收益；14：一年收益；15：累计净值
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午5:13:02
 */
public class ParSimuFundsValueByPage extends AbsParam{
	public static final String URL="start/simu/navupdatepage.protobuf";
	//type	int	N	基金分类(*)
	private int cstmFlag=0;//	int	N	自选开关（0为全部，1为自选）
	private String cstmCodes=null;//	String	Y	自选标志为1时才填充该字段，该字段由自选基金Code拼接而成，以分号分隔
	private int dateFlag=0;//	int	N	按时间分类开关，0关；1开
	private int orderType=0;//	Int	N	排序类型(*)
	private int orderIncrease=0;//	Int	N	增序为0，降序为1
	private int pageNum=1;//	N	页数
	private int pageCount=1;//			一页显示多少
	
	
	public ParSimuFundsValueByPage setParams(  int cstmFlag, String cstmCodes,
			int dateFlag, int orderType, int orderIncrease, int pageNum,
			int pageCount) { 
		this.cstmFlag = cstmFlag;
		this.cstmCodes = cstmCodes;
		this.dateFlag = dateFlag;
		this.orderType = orderType;
		this.orderIncrease = orderIncrease;
		this.pageNum = pageNum;
		this.pageCount = pageCount;
		return this;
	}
 
	public void setCstmFlag(int cstmFlag) {
		this.cstmFlag = cstmFlag;
	}
	public void setCstmCodes(String cstmCodes) {
		this.cstmCodes = cstmCodes;
	}
	public void setDateFlag(int dateFlag) {
		this.dateFlag = dateFlag;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public void setOrderIncrease(int orderIncrease) {
		this.orderIncrease = orderIncrease;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public ParSimuFundsValueByPage(long cacheTime) {
		super(cacheTime);
	}
	public ParSimuFundsValueByPage(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParSimuFundsValueByPage(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParSimuFundsValueByPage(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParSimuFundsValueByPage(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	 return FundInfoSimusOfPage.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("cstmFlag",cstmFlag );
		addArg("cstmCodes",cstmCodes );
		addArg("dateFlag", dateFlag);
		addArg("orderType",orderType );
		addArg("orderIncrease",orderIncrease );
		addArg("pageNum", pageNum);
		addArg("pageCount",pageCount );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
