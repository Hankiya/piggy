package com.howbuy.lib.entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-9 下午11:22:55
 */
public class CharDataType {
	public ICharData mData = null;
	public ICharType mType = null;
	public Object mObj = null;
	public int mIndex = -1;
	public float mWhat = -1;

	public CharDataType(ICharData mData, ICharType mType) {
		set(mData, mType);
	}

	public void set(ICharData mData, ICharType mType) {
		this.mData = mData;
		this.mType = mType;
	}

	public void clear() {
		set(null, null);
		this.mObj = null;
		this.mIndex = -1;
		this.mWhat = -1;
	}

	public boolean hasDataType() {
		return mData != null && mType != null;
	}
 
	public String toString(int dataType) {
		if (hasDataType()) {
			/*
				return String.format("type=%1$s,data=%2$s,extras{%3$s}",
					mType.getName(), mData.toString(), "index=" + mIndex
							+ ",what=" + mWhat+",obj="+mObj);
			 */
			return String.format("%1$s:%2$s",
					mType.getName(), mData.getValueStr(dataType));
		} else {
			return "--";
		}
	}

	@Override
	public String toString(  ) {
		if (hasDataType()) {
				return String.format("type=%1$s,data=%2$s,extras{%3$s}",
					mType.getName(), mData.toString(), "index=" + mIndex
							+ ",what=" + mWhat+",obj="+mObj); 
		} else {
			return "--";
		}
	}
	public static void sort(ArrayList<CharDataType> list) {
		Collections.sort(list, mComper);
	}

	public static Comparator mComper = new Comparator<CharDataType>() {
		@Override
		public int compare(CharDataType a, CharDataType b) {

			float af = a.mData.getValue(0);
			float bf = b.mData.getValue(0);
			if (af > bf) {
				return -1;
			} else if (af < bf) {
				return 1;
			}
			return 0;
		}
	};

}
