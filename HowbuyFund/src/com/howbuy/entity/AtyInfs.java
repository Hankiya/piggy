package com.howbuy.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.aty.AbsSfAty;

public class AtyInfs implements Parcelable, Serializable {
	private static final long serialVersionUID = 1L;
	private static ArrayList<AtyInfs> mAtyInfs = new ArrayList<AtyInfs>();
	public int mIndex = -1;
	public Class<? extends Activity> mClass = null;
	public String mCurrentTitle = null;
	public String mCurrentFrag = null;
	public String mInitTitle = null;
	public String mInitFrag = null;
	public Bundle mInitFragArg = null;

	public AtyInfs(int index, Activity aty) {
		mIndex = index;
		if (aty != null) {
			parseAty(aty);
		}
	}

	public boolean hasTag(Class<? extends Activity> cls, String initFrag) {
		boolean has = false;
		if (mClass.equals(cls)) {
			has = initFrag == null ? true : initFrag.equals(mInitFrag);
		}
		return has;
	}

	public static AtyInfs hasAty(Class<? extends Activity> cls, String initFrag) {
		parseAtys(AbsAty.getAtys());
		AtyInfs inf = null;
		int n = mAtyInfs.size();
		for (int i = 0; i < n; i++) {
			if (mAtyInfs.get(i).hasTag(cls, initFrag)) {
				inf = mAtyInfs.get(i);
				break;
			}
		}
		return inf;
	}

	private void parseAty(Activity aty) {
		mClass = aty.getClass();
		CharSequence title = null;
		Fragment frag = null;
		if (aty instanceof AbsAty) {
			AbsAty spf = (AbsAty) aty;
			if (spf.getSupportActionBar() != null) {
				title = spf.getSupportActionBar().getTitle();
			} else {
				title = spf.getTitle();
			}
			frag = spf.getCurrentFragment();
		} else if (aty instanceof AbsSfAty) {
			AbsSfAty sf = (AbsSfAty) aty;
			if (sf.getSupportActionBar() != null) {
				title = sf.getSupportActionBar().getTitle();
			} else {
				title = sf.getTitle();
			}
			frag = sf.getCurrentFragment();
		} else {
			title = aty.getTitle();
		}
		if (title != null) {
			mCurrentTitle = title.toString().trim();
		}
		if (frag != null) {
			mCurrentFrag = frag.toString().trim();
		}
		if (aty.getIntent() != null) {
			parseIntent(aty.getIntent());
		}
	}

	private void parseIntent(Intent t) {
		mInitFrag = t.getStringExtra(AtyEmpty.KEY_FRAG_NAME);
		mInitFragArg = t.getBundleExtra(AtyEmpty.KEY_FRAG_ARG);
		if (mInitFragArg != null) {
			mInitTitle = mInitFragArg.getString(ValConfig.IT_NAME);
		}
	}

	public static ArrayList<AtyInfs> parseAtys(ArrayList<Activity> list) {
		mAtyInfs.clear();
		int n = list == null ? 0 : list.size();
		for (int i = 0; i < n; i++) {
			mAtyInfs.add(new AtyInfs(i, list.get(i)));
		}
		return mAtyInfs;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mIndex);
		dest.writeSerializable(mClass);
		dest.writeString(mCurrentTitle);
		dest.writeString(mCurrentFrag);
		dest.writeString(mInitTitle);
		dest.writeString(mInitFrag);
	}

	public static final Creator<AtyInfs> CREATOR = new Creator<AtyInfs>() {
		@SuppressWarnings("unchecked")
		@Override
		public AtyInfs createFromParcel(Parcel s) {
			AtyInfs u = new AtyInfs(s.readInt(), null);
			u.mClass = (Class<? extends Activity>) s.readSerializable();
			u.mCurrentTitle = s.readString();
			u.mCurrentFrag = s.readString();
			u.mInitTitle = s.readString();
			u.mInitFrag = s.readString();
			return u;
		}

		@Override
		public AtyInfs[] newArray(int size) {
			return new AtyInfs[size];
		}
	};

	@Override
	public String toString() {
		return "AtyInfs [mIndex=" + mIndex + ", mClass=" + mClass + ", mCurrentTitle="
				+ mCurrentTitle + ", mCurrentFrag=" + mCurrentFrag + ", mInitTitle=" + mInitTitle
				+ ", mInitFrag=" + mInitFrag + "]";
	}
}
