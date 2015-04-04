package com.howbuy.adp;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.howbuy.config.ValConfig;
import com.howbuy.frag.FragCharLand;
import com.howbuy.frag.FragCharPort;
import com.howbuy.lib.adp.AbsFragPageAdp;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;

public class FragPageCharAdp extends AbsFragPageAdp {

	private boolean mPort = true;
	private CharCycleAdp mAdapter = null;
	private AbsHbFrag mTargetFragment = null;

	public FragPageCharAdp(AbsHbFrag fm, CharCycleAdp adp, boolean port) {
		super(fm.getChildFragmentManager());
		mTargetFragment = fm;
		mPort = port;
		mAdapter = adp;
		mAdapter.notifyDataSetChanged();
	}

	public CharCycleAdp getCharAdp() {
		return mAdapter;
	}

	@Override
	public Fragment getItem(int p) {
		Bundle b = new Bundle();
		b.putInt(ValConfig.IT_TYPE, mAdapter.getItem(p, true).mCycleType);
		String fragname = mPort ? FragCharPort.class.getName() : FragCharLand.class.getName();
		Fragment frag = Fragment.instantiate(GlobalApp.getApp(), fragname, b);
		frag.setTargetFragment(mTargetFragment, mPort ? 0 : 1);
		return frag;
	}

	@Override
	public int getCount() {
		return mAdapter.getEnableCount();
	}

	@Override
	protected String getTag(int position) {
		return null;
	}

	public CharCycleAdp getCycleAdp() {
		return mAdapter;
	}
}