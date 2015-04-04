package com.howbuy.config;

import java.io.IOException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.xml.XmlParse;

public class FundConfig {
	// KEY FOR MAP IN APPLICATION OBJ MAP.
	private static final String KEY_FUND_CONFIG = "com.howbuy.fund.fundconfig";
	// KEY FOR KEEP FLAG IN SHAREPREFERENCE FILE .
	private static final String SF_RANK_FLAG = "fund_rank_flag";
	// START DATA TYPES
	public static final int DATA_ALL = 0;
	public static final int DATA_OPEN = 1;
	public static final int DATA_MONEY = 2;
	public static final int DATA_SIMU = 3;
	public static final int DATA_CLOSE = 4;
	// END DATA TYPES

	// START FUND TYPES
	public static final String TYPE_OTHER = "o";
	public static final String TYPE_GUPIAO = "1";
	public static final String TYPE_HUNHE = "3";
	public static final String TYPE_ZHAIQUAN = "5";
	public static final String TYPE_QDII = "9";
	public static final String TYPE_JIEGOU = "t";
	public static final String TYPE_ZHISHU = "8";
	public static final String TYPE_BAOBEN = "b";
	public static final String TYPE_LICAI = "53";
	public static final String TYPE_HUOBI = "7";
	public static final String TYPE_SIMU = "sm";
	public static final String TYPE_FENGBI = "a";
	public static final String TYPE_ALL = "*";
	// END FUND TYPES
	// START RANK SETTING FLAG
	public static final int RANK_FLAG_OPTIONAL = 1;
	public static final int RANK_FLAG_ONSALE = 2;
	public static final int RANK_FLAG_CURRENCY_EXCEPT_BFLAG = 4;
	public static final int RANK_FLAG_ALL_EXCEPT_STRUCT = 8;
	public static final int RANK_FLAG_MERGE_STOCK_MIX = 16;
	public static final int RANK_FLAG_MERGE_CURRENCY_FINANCIAL = 32;
	// END RANK SETTING FLAG

	// ALL SORT TYPES
	private ArrayList<SortType> mSorts = null;
	// ALL FUND TYPES
	private ArrayList<FundType> mTypes = null;

	// RANK SETTING FLAG.
	private int mFlag = RANK_FLAG_ALL_EXCEPT_STRUCT | RANK_FLAG_CURRENCY_EXCEPT_BFLAG
			| RANK_FLAG_MERGE_STOCK_MIX;

	final public int getFlag() {
		return mFlag;
	}

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final public void addFlag(int flag) {
		if (flag != 0) {
			mFlag |= flag;
		}
	}

	final public void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}

	final protected void reverseFlag(int flag) {
		mFlag ^= flag;
	}

	public static FundConfig getConfig() {
		FundConfig con = (FundConfig) GlobalApp.getApp().getMapObj(KEY_FUND_CONFIG);
		if (con == null) {
			con = new FundConfig(ValConfig.ASSETS_FUNDTYPE);
		}
		return con;
	}

	private void wrapConfig(FundConfig con) {
		if (con != null) {
			mSorts = con.mSorts;
			mTypes = con.mTypes;
			mFlag = con.mFlag;
		}
	}

	public FundConfig(String configname) {
		FundConfig con = (FundConfig) GlobalApp.getApp().getMapObj(KEY_FUND_CONFIG);
		if (con != null && con.mSorts != null && mTypes != null) {
			wrapConfig(con);
		} else {
			try {
				XmlParse.parseFundConfig(this, GlobalApp.getApp().getAssets().open(configname));
				GlobalApp.getApp().getMapObj().put(KEY_FUND_CONFIG, this);
			} catch (IOException e) {
				mSorts = null;
				mTypes = null;
				e.printStackTrace();
			}
		}

	}

	public int readFlag(SharedPreferences sf) {
		return mFlag = sf.getInt(SF_RANK_FLAG, mFlag);
	}

	public void saveFlag(SharedPreferences sf) {
		sf.edit().putInt(SF_RANK_FLAG, mFlag).commit();
	}

	public void setFundTypes(ArrayList<FundType> fundTypes) {
		mTypes = fundTypes;
	}

	public void setSortType(ArrayList<SortType> sortTypes) {
		mSorts = sortTypes;
	}

	public FundType getType(String classtype) {
		int n = mTypes.size();
		for (int i = 0; i < n; i++) {
			FundType f = mTypes.get(i);
			if (f.ClassType.equals(classtype)) {
				return f;
			}
		}
		return getType(FundConfig.TYPE_OTHER);
	}

	public ArrayList<FundType> getFundTypes() {
		return mTypes;
	}

	protected ArrayList<SortType> getSortTypes(FundType fundType) {
		int[] indexs = fundType.mSortIndex;
		int n = indexs == null ? 0 : indexs.length;
		ArrayList<SortType> r = new ArrayList<FundConfig.SortType>(n + 1);
		for (int i = 0; i < n; i++) {
			r.add(mSorts.get(indexs[i]));
		}
		return r;
	}

	protected SortType getSortType(FundType fundType) {
		int[] indexs = fundType.mSortIndex;
		int n = indexs == null ? 0 : indexs.length;
		for (int i = 0; i < n; i++) {
			if (fundType.Selected == i) {
				return mSorts.get(indexs[i]);
			}
		}
		return null;
	}

	public SortType newSortType(String sortName, String columName, int valIndex) {
		return new SortType(sortName, columName, valIndex);
	}

	public FundType newFundType(String name, int dataType, String classType) {
		return new FundType(name, dataType, classType);
	}

	public static class SortType {
		public String SortName = null;
		public String ColumName = null;
		public int ValueIndex = 0;

		protected SortType(String sortName, String columName, int valIndex) {
			SortName = sortName;
			ColumName = columName;
			ValueIndex = valIndex;
		}

		@Override
		public String toString() {
			return "SortType [SortName=" + SortName + ", ColumName=" + ColumName + ", ValueIndex="
					+ ValueIndex + "]";
		}
	}

	public static class FundType implements Parcelable {
		public String FundName = null;
		public String ClassType = null;
		public int DataType = 0;
		public int Selected = 0;
		private int[] mSortIndex = null;
		public static final Creator<FundType> CREATOR = new Creator<FundType>() {
			@Override
			public FundType[] newArray(int size) {
				return new FundType[size];
			}

			@Override
			public FundType createFromParcel(Parcel s) {
				String name = s.readString();
				String classtype = s.readString();
				int datatype = s.readInt();
				int select = s.readInt();
				int len = s.readInt();
				FundType type = new FundType(name, datatype, classtype);
				type.Selected = select;
				if (len > 0) {
					int[] indexs = new int[len];
					s.readIntArray(indexs);
					type.setSortIndex(indexs);
				}
				return type;
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel in, int flags) {
			in.writeString(FundName);
			in.writeString(ClassType);
			in.writeInt(DataType);
			in.writeInt(Selected);
			in.writeInt(mSortIndex == null ? 0 : mSortIndex.length);
			in.writeIntArray(mSortIndex);

		}

		protected FundType(String name, int dataType, String classType) {
			FundName = name;
			DataType = dataType;
			ClassType = classType;
		}

		public boolean equalType(String type) {
			return ClassType.equals(type);
		}

		public void setSortIndex(int[] sort) {
			mSortIndex = sort;
		}

		public ArrayList<SortType> getSortTypes() {
			return FundConfig.getConfig().getSortTypes(this);
		}

		public SortType getSortType() {
			return FundConfig.getConfig().getSortType(this);
		}

		public int readSelected(SharedPreferences sf) {
			return Selected = sf.getInt(FundName + "_sort", Selected);
		}

		public boolean saveSelected(SharedPreferences sf) {
			return sf.edit().putInt(FundName + "_sort", Selected).commit();
		}

		public boolean isSimu() {
			return FundConfig.TYPE_SIMU.equals(ClassType);
		}

		public boolean isHuobi() {
			return 2 == DataType;
		}

		public boolean isFengbi() {
			return FundConfig.TYPE_FENGBI.equals(ClassType);
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof FundType) {
				return ((FundType) o).ClassType.equals(ClassType);
			}
			return false;
		}

		@Override
		public String toString() {
			return "FundType [FundName=" + FundName + ", DataType=" + DataType + ", ClassType="
					+ ClassType + ", Selected=" + Selected + ",IndexLenth="
					+ (mSortIndex == null ? 0 : mSortIndex.length) + "]";
		}

	}
}
