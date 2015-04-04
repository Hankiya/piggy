package com.howbuy.entity;

import java.util.ArrayList;

import com.howbuy.config.FundConfig.FundType;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.PerformanceInfoProto.PerformanceInfo;

/**
 * @author zheng
 * 
 */
public class Performace {
	private static String DEFTEXT = "暂无排名";
	private int id = -1;
	private String type;
	private String shouyi;
	private String itemOrder;
	private String itemSum;

	public Performace(int id, String type, String shouyi, String itemOrder, String itemSum) {
		this.id = id;
		this.type = type;
		this.shouyi = shouyi;
		this.itemOrder = itemOrder;
		this.itemSum = itemSum;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShouyi() {
		return shouyi;
	}

	public void setShouyi(String shouyi) {
		this.shouyi = shouyi;
	}

	public String getItemSum() {
		return itemSum;
	}

	public void setItemSum(String itemSum) {
		this.itemSum = itemSum;
	}

	public String getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String formatRank(boolean isSpaceEnough) {
		if (!StrUtils.isEmpty(itemOrder)) {
			String state = null;
			if (!StrUtils.isEmpty(itemSum)) {
				state = String.format(isSpaceEnough ? "第%1$s名，总%2$s只" : "%1$s/%2$s", itemOrder,
						itemSum);
			} else {
				state = String.format(isSpaceEnough ? "第%1$s名" : "排名%1$s", itemOrder);
			}
			return state;
		}
		return DEFTEXT;
	}

	public static ArrayList<Performace> parseData(PerformanceInfo p) {
		ArrayList<Performace> r = new ArrayList<Performace>();
		r.add(new Performace(0, "近一周", p.getHb1Z(), p.getPm1Z(), p.getTl1Z()));
		r.add(new Performace(1, "近一个月", p.getHb1Y(), p.getPm1Y(), p.getTl1Y()));//
		r.add(new Performace(2, "近三个月", p.getHb3Y(), p.getPm3Y(), p.getTl3Y()));
		r.add(new Performace(3, "近半年", p.getHb6Y(), p.getPm6Y(), p.getTl6Y()));
		r.add(new Performace(4, "近一年", p.getHb1N(), p.getPm1N(), p.getTl1N()));//
		r.add(new Performace(5, "今年以来", p.getHbjn(), p.getPmjn(), p.getTljn()));//
		r.add(new Performace(6, "成立以来", p.getHbcl(), p.getPmcl(), p.getTlcl()));//
		return r;
	}

	public static ArrayList<Performace> getDefPerformace(boolean fixed) {
		ArrayList<Performace> r = new ArrayList<Performace>();
		r.add(new Performace(1, "近一个月", null, null, null));//
		r.add(new Performace(4, "近一年", null, null, null));//
		r.add(new Performace(5, "今年以来", null, null, null));//
		if (!fixed) {
			r.add(new Performace(6, "成立以来", null, null, null));//
		}
		return r;
	}

	/**
	 * @param r
	 * @param type
	 *            type 0 fixed for unexpand, 1 fixed for expand ,2 expand.
	 * @return
	 */
	public static ArrayList<Performace> filterPerformace(ArrayList<Performace> r, int type,
			FundType fund) {
		ArrayList<Performace> res = new ArrayList<Performace>();
		if (type == 0) {
			res.add(r.get(1));
			res.add(r.get(4));
			res.add(r.get(5));
		} else if (type == 1) {
			if (fund == null || !fund.isSimu()) {
				res.add(r.get(0));
				res.add(r.get(1));
				res.add(r.get(2));
			} else {
				res.add(r.get(1));
				res.add(r.get(2));
				res.add(r.get(3));
			}
		} else {
			if (fund == null || !fund.isSimu()) {
				res.add(r.get(3));
				res.add(r.get(4));
				res.add(r.get(5));
				res.add(r.get(6));
			} else {
				res.add(r.get(4));
				res.add(r.get(5));
				res.add(r.get(6));
			}
		}
		return res;
	}
}
