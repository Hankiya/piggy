package com.howbuy.entity;

import java.io.Serializable;

/**
 * 基金净值bean
 * 
 * @author yescpu
 * 
 */

public class NetWorthBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String jjmc; // 基金名称
	private String jjdm; // 代码
	private String jzrq; // 净值日期
	private String jjjz; // 净值
	private String ljjz; // 累计净值
	private String hbdr; // 日增幅
	private String hb3y; // 季涨幅
	private String hb6y; // 半年涨幅
	private String hb1n; // 年涨幅
	private String hb3n; // 年涨幅
	private String hbjn; // 今年以来涨幅
	private String zfxz; // 风险等级
	private int hbtradflag;// 好买代销.
	private int mbflag; // 代币中为B类的.
	private String found_date;// 成立时间.
	private String jjfl;// 基金分类
	private String jjfl2;// 基金分类2,债券下的理财类，放到货币式里面
	private String wfsy; // 万份收益
	private String qrsy; // 七日年华收益
	private String hb1y; // 月增幅
	private String danWei;// 私募单位
	private String xzjl; // 折价益率
	private String dqrq;// 到期日期
	private int xunan; // 自选状态.
	private String xuanTime;// 添加自选的时间.
	private String pinyin;// 拼音简码.
	private int sortindex = -1;

	public NetWorthBean(String jjmc, String jjdm, String jzrq, String jjjz, String ljjz,
			String hbdr, String hb3y, String hb6y, String hb1n,String hb3n, String hbjn, String zfxz,
			String status, int hbtradeFlag, int mbFlag, String date, String jjfl, String jjfl2,
			String wfsy, String qrsy, String hb1y, String danWei, String xzjl, String dqrq,
			int xunan, String order, String xuanTime) {
		super();
		this.jjmc = jjmc;
		this.jjdm = jjdm;
		this.jzrq = jzrq;
		this.jjjz = jjjz;
		this.ljjz = ljjz;
		this.hbdr = hbdr;
		this.hb3y = hb3y;
		this.hb6y = hb6y;
		this.hb1n = hb1n;
		this.hb3n=hb3n;
		this.hbjn = hbjn;
		this.zfxz = zfxz;
		this.hbtradflag = hbtradeFlag;
		this.mbflag = mbFlag;
		this.found_date = date;
		this.jjfl = jjfl;
		this.jjfl2 = jjfl2;
		this.wfsy = wfsy;
		this.qrsy = qrsy;
		this.hb1y = hb1y;
		this.danWei = danWei;
		this.xzjl = xzjl;
		this.dqrq = dqrq;
		this.xunan = xunan;
		this.pinyin = order;
		this.xuanTime = xuanTime;
	}

	public NetWorthBean() {
		// TODO Auto-generated constructor stub
	}

	public String getJjmc() {
		return jjmc;
	}

	public void setJjmc(String jjmc) {
		this.jjmc = jjmc;
	}

	public String getJjdm() {
		return jjdm;
	}

	public void setJjdm(String jjdm) {
		this.jjdm = jjdm;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getJjjz() {
		return jjjz;
	}

	public void setJjjz(String jjjz) {
		this.jjjz = jjjz;
	}

	public String getLjjz() {
		return ljjz;
	}

	public void setLjjz(String ljjz) {
		this.ljjz = ljjz;
	}

	public String getHbdr() {
		return hbdr;
	}

	public void setHbdr(String hbdr) {
		this.hbdr = hbdr;
	}

	public String getHb3y() {
		return hb3y;
	}

	public void setHb3y(String hb3y) {
		this.hb3y = hb3y;
	}

	public String getHb6y() {
		return hb6y;
	}

	public void setHb6y(String hb6y) {
		this.hb6y = hb6y;
	}

	public String getHb1n() {
		return hb1n;
	}

	public void setHb1n(String hb1n) {
		this.hb1n = hb1n;
	}

	public String getHb3n() {
		return hb3n;
	}

	public void setHb3n(String hb3n) {
		this.hb3n = hb3n;
	}
	public String getHbjn() {
		return hbjn;
	}

	public void setHbjn(String hbjn) {
		this.hbjn = hbjn;
	}

	public String getZfxz() {
		return zfxz;
	}

	public void setZfxz(String zfxz) {
		this.zfxz = zfxz;
	}

	public int getMbFlag() {
		return mbflag;
	}

	public void setMbFlag(int hbFlag) {
		this.mbflag = hbFlag;
	}

	public String getFoundDate() {
		return found_date;
	}

	public void setFoundDate(String date) {
		found_date = date;
	}

	public int getHbTradFlag() {
		return hbtradflag;
	}

	public void setHbTradFlag(int status_t) {
		this.hbtradflag = status_t;
	}

	public void setSortIndex(int i) {
		sortindex = i;
	}

	public int getSortIndex() {
		return sortindex;
	}

	public String getJjfl() {
		return jjfl;
	}

	public void setJjfl(String jjfl) {
		this.jjfl = jjfl;
	}

	public String getJjfl2() {
		return jjfl2;
	}

	public void setJjfl2(String jjfl2) {
		this.jjfl2 = jjfl2;
	}

	public String getWfsy() {
		return wfsy;
	}

	public void setWfsy(String wfsy) {
		this.wfsy = wfsy;
	}

	public String getQrsy() {
		return qrsy;
	}

	public void setQrsy(String qrsy) {
		this.qrsy = qrsy;
	}

	public String getHb1y() {
		return hb1y;
	}

	public void setHb1y(String hb1y) {
		this.hb1y = hb1y;
	}

	public String getDanWei() {
		return danWei;
	}

	public void setDanWei(String danWei) {
		this.danWei = danWei;
	}

	public String getXzjl() {
		return xzjl;
	}

	public void setXzjl(String xzjl) {
		this.xzjl = xzjl;
	}

	public String getDqrq() {
		return dqrq;
	}

	public void setDqrq(String dqrq) {
		this.dqrq = dqrq;
	}

	public int getXunan() {
		return xunan;
	}

	public void setXunan(int xunan) {
		this.xunan = xunan;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getXuanTime() {
		return xuanTime;
	}

	public void setXuanTime(String xuanTime) {
		this.xuanTime = xuanTime;
	}

	@Override
	public String toString() {
		return "NetWorthBean [jjmc=" + jjmc + ", jjdm=" + jjdm + ", jzrq=" + jzrq + ", jjjz="
				+ jjjz + ", ljjz=" + ljjz + ", hbdr=" + hbdr + ", hb3y=" + hb3y + ", hb6y=" + hb6y
				+ ", hb1n=" + hb1n + ", hb3n=" + hb3n + ", hbjn=" + hbjn + ", zfxz=" + zfxz + ", hbtradflag="
				+ hbtradflag + ", mbflag=" + mbflag + ", found_date=" + found_date + ", jjfl="
				+ jjfl + ", jjfl2=" + jjfl2 + ", wfsy=" + wfsy + ", qrsy=" + qrsy + ", hb1y="
				+ hb1y + ", danWei=" + danWei + ", xzjl=" + xzjl + ", dqrq=" + dqrq + ", xunan="
				+ xunan + ", xuanTime=" + xuanTime + ", pinyin=" + pinyin + ", sortindex="
				+ sortindex + "]";
	}
}
