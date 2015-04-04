package com.howbuy.entity;

import java.util.Calendar;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.CharProvider;

public class CharRequest extends ReqOpt {
	public String StartTime = null;
	public String EndTime = null;
	private int mCharCount = 0;

	public CharRequest(CharRequest o) {
		super(o);
		StartTime = o.StartTime;
		EndTime = o.EndTime;
		mCharCount = o.mCharCount;
	}

	// 数据库中无数据时.
	public CharRequest(int mCharCount) {
		super(0,"cycle_" + 0, 0);
		this.mCharCount = mCharCount;
	}

	// 数据库中有数据时.
	public CharRequest(int cycle, String mStartTime, String mEndTime) {
		super(0,"cycle_" + cycle, cycle);
		this.StartTime = mStartTime;
		this.EndTime = mEndTime;
	}

	public static CharRequest getRequest(int mCycle, CharProvider p) {
		Calendar ca = Calendar.getInstance();
		long newTimes = p.getNewTimes();
		if (newTimes == 0) {
			newTimes = System.currentTimeMillis();
		}
		ca.setTimeInMillis(newTimes);
		int n = 7;
		switch (mCycle) {
		case CharCycleInf.CYCLE_DAY7:
			break;
		case CharCycleInf.CYCLE_MONTH1:
			ca.add(Calendar.MONTH, -1);
			n = 30;
			break;
		case CharCycleInf.CYCLE_MONTH3:
			ca.add(Calendar.MONTH, -3);
			n = 30 * 3;
			break;
		case CharCycleInf.CYCLE_YEAR1:
			ca.add(Calendar.YEAR, -1);
			n = 356;
			break;
		case CharCycleInf.CYCLE_YEAR:
			ca.set(Calendar.MONTH, 0);
			ca.set(Calendar.DAY_OF_MONTH, 1);
			n = (int) ((System.currentTimeMillis() - ca.getTimeInMillis()) / 86400000);
			break;
		case CharCycleInf.CYCLE_ALL:
			n = 36500;
		}
		String f = p.getOldDate(), t = p.getNewDate(), from = null;
		if (mCycle != CharCycleInf.CYCLE_DAY7) {
			from = n == 36500 ? f : StrUtils.timeFormat(ca.getTimeInMillis(), ValConfig.DATEF_YMD);
		}
		CharRequest req = new CharRequest(mCycle, from, t);
		req.setCharCount(n);
		return req;
	}

	public int getCycleType() {
		return mHandleType;
	}

	public int getCharCount() {
		return mCharCount;
	}

	public void setCycleType(int type) {
		mHandleType = type;
	}

	public void setCharCount(int count) {
		mCharCount = count;
	}

	@Override
	public String toString() {
		return "CharRequest=[from " + StartTime + " to " + EndTime + ", mCharCount="
				+ mCharCount+"]"  ;
	}

}