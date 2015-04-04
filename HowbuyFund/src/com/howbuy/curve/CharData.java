package com.howbuy.curve;

import java.util.ArrayList;
import java.util.List;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;

public class CharData implements ICharData {
	CharValue mFund = null;
	private long mTime = 0;
	private float mValue = 0;
	private boolean isHuobi = false;
    private float mDanWei=1;
	public CharData(CharValue f, boolean huobi,float danwei) {
		mFund = f;
		isHuobi = huobi;
		mDanWei=danwei;
	}

	@Override
	public Object getExtras(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColor(boolean selected) {
		return 0xffff0000;
	}

	@Override
	public float getValue(int type) {
		if (mValue == 0) {
			try {
				mValue = Float.parseFloat(isHuobi ? mFund.increase : mFund.value);
				if(mDanWei>0){
					mValue=mValue/mDanWei;
				}
			} catch (Exception e) {
				mValue = 0.00000f;
				LogUtils.d("CharData", "CharValue+" + mFund + "e, =" + e);
			}
		}
		return mValue;
	}

	@Override
	public long getTime() {
		if (mTime == 0) {
			mTime = StrUtils.getTimeFormatLong(mFund.date, ValConfig.DATEF_YMD);
		}
		return mTime;
	}

	@Override
	public String getValueStr(int type) {
		return isHuobi ? mFund.increase : mFund.value;
	}

	@Override
	public String getTime(String format) {
		return mFund.date;
	}

	public CharValue getCharValue() {
		return mFund;
	}

	public static int initCharData(ArrayList<CharData> mCharData, List<CharValue> infos,
			boolean isHuobi,float danWei) {
		int r = infos == null ? 1 : 0;
		if (r == 0) {
			int n = infos.size();
			if (n > 0) {
				mCharData.clear();
				for (int i = 0; i < n; i++) {
					mCharData.add(new CharData(infos.get(i), isHuobi,danWei));
				}
			} else {
				r = 2;
			}
		}
		return r;
	}

	@Override
	public String toString() {
		return "CharData [mFund=" + mFund + ", mTime=" + mTime + ", mValue=" + mValue
				+ ", isHuobi=" + isHuobi + ", mDanWei=" + mDanWei + "]";
	}
}
