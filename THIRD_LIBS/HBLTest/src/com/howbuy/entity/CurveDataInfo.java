package com.howbuy.entity;

import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.interfaces.ICharData;

public final class CurveDataInfo implements ICharData{
	public float mVal=0;
	public long mTime=0l;
	public Object mExtra;
	public CurveDataInfo(float val, Object extras, long time) {
		this(val, time);
		this.mExtra = extras;
	}
	public CurveDataInfo(float val, long time) {
		super();
		this.mVal = val;
		this.mTime = time;
	}
	
	@Override
	public float getValue(int type) {
		return mVal;
	}
	@Override
	public long getTime() { 
		return mTime;
	}
	@Override
	public String getValueStr(int type) {
		// TODO Auto-generated method stub
		return StrUtils.formatF(mVal, 2);
	}
	@Override
	public String getTime(String format) {
		// TODO Auto-generated method stub
		return StrUtils.timeFormat(mTime, StrUtils.isEmpty(format)?"yyyy/MM":format);
	}
	@Override
	public Object getExtras(int type) {
		return null;
	}
	@Override
	public int getColor(boolean normal) {
		return 0xff000000;
	}
	 
	 
}