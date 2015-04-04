package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.TrustInfoListOfPageProto.TrustInfoListOfPage;

/**
 * 分页获取信托列表
 返回参数：TrustInfoListOfPage
参数	值	能否能为空	描述
totalNum		N	总条目数
productList{			产品列表
pid		N	产品ID
cpmc		N	产品名称
xszt		N	销售状态
cpnx		N	产品年限
yqsy		N	预期收益
hmdp		Y	好买点评
cpmd		Y	产品卖点
}
备注：销售状态1：在售、2：售完、3：结束、其他：其他
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParTrustListByPage extends AbsParam {
	private static final String URL = "trustdaquan/querytrustinfolistofpage.protobuf"; // 分页获取
	private int pageCount = 1; // unnull 每页条数
	private int currentPage = 1; // nunull 当前请求页（从1开始）
	private String dataVersion = null; // 数据版本
	private int bestFlag = 0; // 无该字段或该字段为1表示请求精品，0表示请求所有符合其他条件的产品
	private String xtgs = null; // 信托公司id
	private String gsbj = null; // 公司背景（动态）
	private String xtfl = null; // 信托分类（动态）
	private String tzfx = null; // 投资方向（动态）
	private String yqsy = null; // 预期收益（动态）
	private String cpnx = null; // 产品年限（动态）
	private String qszj = null; // 起始资金（动态）
	private String clrq = null; // 成立日期（暂无）
	private String fsrq = null; // 发售日期（暂无）
	private String fszt = null; // 发售状态（暂无）
	private String gjz = null; // 关键字（为空时可以忽略该关键字）
	public ParTrustListByPage(long cacheTime) {
		super(cacheTime);
	}
	public ParTrustListByPage(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}
	public ParTrustListByPage(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}
	public ParTrustListByPage(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	public ParTrustListByPage(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}
	public ParTrustListByPage setPageRange(int currentPage, int pageCount) {
		this.pageCount = pageCount;
		this.currentPage = currentPage;
		return this;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public void setBestFlag(int bestFlag) {
		this.bestFlag = bestFlag;
	}

	public void setXtgs(String xtgs) {
		this.xtgs = xtgs;
	}

	public void setGsbj(String gsbj) {
		this.gsbj = gsbj;
	}

	public void setXtfl(String xtfl) {
		this.xtfl = xtfl;
	}

	public void setTzfx(String tzfx) {
		this.tzfx = tzfx;
	}

	public void setYqsy(String yqsy) {
		this.yqsy = yqsy;
	}

	public void setCpnx(String cpnx) {
		this.cpnx = cpnx;
	}

	public void setQszj(String qszj) {
		this.qszj = qszj;
	}

	public void setClrq(String clrq) {
		this.clrq = clrq;
	}

	public void setFsrq(String fsrq) {
		this.fsrq = fsrq;
	}

	public void setFszt(String fszt) {
		this.fszt = fszt;
	}

	public void setGjz(String gjz) {
		this.gjz = gjz;
	}

	/**
	 * 获取信托精品或者全部信托
	 * 
	 * @param pageCount
	 *            一页数量
	 * @param currentPage
	 *            当前页数
	 * @param bestFlag
	 *            1精品 2普通
	 * @param cacheTime
	 *            缓存时间
	 * @param mDataVersion
	 *            数据版本
	 * @param xtgs
	 *            信托公司Id
	 * @param gsbj
	 *            公司背景**
	 * @param xtfl
	 *            信托分类*
	 * @param tzfx
	 *            投资方向*
	 * @param yqsy
	 *            预期收益**
	 * @param cpnx
	 *            产品年限**
	 * @param qszj
	 *            起始资金*
	 * @param gjz
	 *            关键字*
	 * @param clrq
	 *            成立日期
	 * @param fsrq
	 *            发售日期
	 * @param fszt
	 *            发售状态
	 * @return
	 * @throws Exception
	 */
	public AbsParam setParams(int pageCount, int currentPage, int bestFlag,
			String dataVersion, String xtgs, String gsbj, String xtfl,
			String tzfx, String yqsy, String cpnx, String qszj, String clrq,
			String fsrq, String fszt, String gjz) {
		setPageRange(currentPage, pageCount);
		this.bestFlag = bestFlag;
		this.dataVersion = dataVersion;
		this.xtgs = xtgs;
		this.gsbj = gsbj;
		this.xtfl = xtfl;
		this.tzfx = tzfx;
		this.yqsy = yqsy;
		this.cpnx = cpnx;
		this.qszj = qszj;
		this.clrq = clrq;
		this.fsrq = fsrq;
		this.fszt = fszt;
		this.gjz = gjz;
		return this;
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	  return TrustInfoListOfPage.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
    
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("pageCount", pageCount); // unnull 每页条数
		addArg("currentPage", currentPage); // nunull 当前请求页（从1开始）
		addArg("dataVersion", dataVersion); // 数据版本
		addArg("bestFlag", bestFlag); // 无该字段或该字段为1表示请求精品，0表示请求所有符合其他条件的产品
		addArg("xtgs", xtgs); // 信托公司id
		addArg("gsbj", gsbj); // 公司背景（动态）
		addArg("xtfl", xtfl); // 信托分类（动态）
		addArg("tzfx", tzfx); // 投资方向（动态）
		addArg("yqsy", yqsy); // 预期收益（动态）
		addArg("cpnx", cpnx); // 产品年限（动态）
		addArg("qszj", qszj); // 起始资金（动态）
		addArg("clrq", clrq); // 成立日期（暂无）
		addArg("fsrq", fsrq); // 发售日期（暂无）
		addArg("fszt", fszt); // 发售状态（暂无）
		addArg("gjz", gjz); // 关键字（为空时可以忽略该关键字）
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
	  setParamType(false, true, false);
	  return opt;
	}

}
