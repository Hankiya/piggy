package com.howbuy.entity;

import java.util.ArrayList;
import java.util.List;

import com.howbuy.curve.ICharData;
import com.howbuy.wireless.entity.protobuf.ZccgInfoListProtos.StockList;

public class HeavyHoldItem implements ICharData {
	private float mPercent = 0;
	private float mProperty = 0;
	private String mName = null;
	private int mColor = 0xff000000;

	public HeavyHoldItem(float mPercent, float mProperty, String mName, int mColor) {
		this.mPercent = mPercent;
		this.mProperty = mProperty;
		this.mName = mName;
		this.mColor = mColor;
	}

	public HeavyHoldItem(String mPercent, String mProperty, String mName, int mColor) {
		this(Float.parseFloat(mPercent), Float.parseFloat(mProperty), mName, mColor);
	}

	@Override
	public Object getExtras(int type) {
		return null;
	}

	@Override
	public int getColor(boolean selected) {
		return mColor;
	}

	@Override
	public float getValue(int type) {
		return type == 0 ? mPercent : mProperty;
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public String getValueStr(int type) {
		return mName;
	}

	@Override
	public String getTime(String format) {
		return null;
	}

	public static ArrayList<HeavyHoldItem> parse(List<StockList> ls, int[] color) {
		int n = Math.min(ls.size(), 10);
		ArrayList<HeavyHoldItem> r = new ArrayList<HeavyHoldItem>(n);
		StockList it = null;
		HeavyHoldItem item = null;
		float other = 100;
		for (int i = 0; i < n; i++) {
			it = ls.get(i);
			item = new HeavyHoldItem(it.getPercentage(), it.getMarketValue(), it.getStockName(),
					color[i]);
			other -= item.getValue(0);
			r.add(item);
		}
		r.add(new HeavyHoldItem(other, -1, "其它", color[color.length - 1]));
		return r;
	}

}
