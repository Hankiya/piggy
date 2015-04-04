package com.howbuy.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.howbuy.lib.utils.JsnUtils;

public class StockInf {
	public static String UpdateTime = "";
	public String StockName = null;
	public String StockValue = null;
	public String StockIncrease = null;
	public String StockPercent = null;

	public StockInf(String stockName, String stockValue, String stockIncrease, String stockPercent) {
		super();
		StockName = stockName;
		StockValue = stockValue;
		StockIncrease = stockIncrease;
		StockPercent = stockPercent;
	}

	public static ArrayList<StockInf> parseStock(String json) throws JSONException {
		ArrayList<StockInf> r = new ArrayList<StockInf>(3);
		JSONObject obj = JsnUtils.getObject(json);
		UpdateTime = JsnUtils.getString(obj, "dt");
		JSONArray ary = JsnUtils.getArray(obj, "pi");
		int n = ary.length();
		for (int i = 0; i < n; i++) {
			obj = ary.getJSONObject(i);
			String t = JsnUtils.getString(obj, "t");
			String cp = JsnUtils.getString(obj, "cp");
			String iv = JsnUtils.getString(obj, "iv");
			String ip = JsnUtils.getString(obj, "ip");
			r.add(new StockInf(t, cp, iv, ip));
		}
		return r;
	}

	@Override
	public String toString() {
		return "StockInf [StockName=" + StockName + ", StockValue=" + StockValue
				+ ", StockIncrease=" + StockIncrease + ", StockPercent=" + StockPercent + "]";
	}

}
