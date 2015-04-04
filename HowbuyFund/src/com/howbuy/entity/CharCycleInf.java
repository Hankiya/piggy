package com.howbuy.entity;

public class CharCycleInf {
	public final static int CYCLE_DAY7 = 1;
	public final static int CYCLE_MONTH1 = 2;
	public final static int CYCLE_MONTH3 = 4;
	public final static int CYCLE_YEAR1 = 8;
	public final static int CYCLE_YEAR = 16;
	public final static int CYCLE_ALL = 32;

	public int mCycleType = -1;
	public String mCycleName;
	public boolean mEnabled = true;
	public boolean mChecked = false;

	public CharCycleInf(int type, String name, boolean enable) {
		mCycleType = type;
		mCycleName = name;
		mEnabled = enable;
	}
}
