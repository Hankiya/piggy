package com.howbuy.adp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.howbuy.frag.FragAdvPage;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.ICAdvert;

public class FragAdvPageAdp extends FragCyclePagerAdp {
	private AdvertList mAdvertList;
	private int mRealSize = 0;

	public FragAdvPageAdp(FragmentManager fragAty) {
		super(fragAty);
	}

	public FragAdvPageAdp(FragmentManager fragAty, AdvertList list) {
		super(fragAty);
		mAdvertList = list;
		mRealSize = list == null ? 0 : list.getIcAdvertsCount();
	}

	public void setData(AdvertList list) {
		mAdvertList = list;
		mAdvertList = list;
		mRealSize = list == null ? 0 : list.getIcAdvertsCount();
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) { 
		Fragment fragment = null;
		ICAdvert icAdvert = mAdvertList.getIcAdvertsList().get(position);
		Bundle bundle = new Bundle();
		bundle.putString("advID", icAdvert.getAdvID());
		bundle.putString("advTitle", icAdvert.getAdvTitle());
		bundle.putString("advImageUrl", icAdvert.getAdvImageUrl());
		bundle.putString("advEventCode", icAdvert.getAdvEventCode());
		fragment = Fragment.instantiate(GlobalApp.getApp(), FragAdvPage.class.getName(), bundle);
		return fragment;
	}

	@Override
	public int getRealCount() {
		return mRealSize;
	}

}