package com.howbuy.adp;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public abstract class FragCyclePagerAdp extends PagerAdapter {
	public int CYCLE = 100;// 请设置成偶数。
	private final FragmentManager mFragmentManager;
	private FragmentTransaction mCurTransaction = null;
	private final ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
	private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public FragCyclePagerAdp(FragmentManager fm) {
		super();
		mFragmentManager = fm;
	}

	public abstract Fragment getItem(int position);

	public abstract int getRealCount();

	@Override
	final public int getCount() {// 保证可以循环。
		return getRealCount() * CYCLE;
	}

	private int getRealPos(int position) {// 返回真实的索引。
		int n = getRealCount();
		if (n > 0) {
			return position % n;
		}
		return position;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		if (mFragments.size() > position) {
			Fragment f = mFragments.get(position);
			if (f != null) {
				return f;
			}
		}
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		Fragment fragment = getItem(getRealPos(position));
		if (mSavedState.size() > position) {
			Fragment.SavedState fss = mSavedState.get(position);
			if (fss != null) {
				fragment.setInitialSavedState(fss);
			}
		}
		while (mFragments.size() <= position) {
			mFragments.add(null);
		}
		mFragments.set(position, fragment);
		mCurTransaction.add(container.getId(), fragment);
		return fragment;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		while (mSavedState.size() <= position) {
			mSavedState.add(null);
		}
		mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment));
		mFragments.set(position, null);

		mCurTransaction.remove(fragment);
	}

	@Override
	public void finishUpdate(View container) {
		if (mCurTransaction != null) {
			// mCurTransaction.commit();
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		Bundle state = null;
		if (mSavedState.size() > 0) {
			state = new Bundle();
			Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState.size()];
			mSavedState.toArray(fss);
			state.putParcelableArray("states", fss);
		}
		for (int i = 0; i < mFragments.size(); i++) {
			Fragment f = mFragments.get(i);
			if (f != null) {
				if (state == null) {
					state = new Bundle();
				}
				String key = "f" + i;
				mFragmentManager.putFragment(state, key, f);
			}
		}
		return state;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		if (state != null) {
			Bundle bundle = (Bundle) state;
			bundle.setClassLoader(loader);
			Parcelable[] fss = bundle.getParcelableArray("states");
			mSavedState.clear();
			mFragments.clear();
			if (fss != null) {
				for (int i = 0; i < fss.length; i++) {
					mSavedState.add((Fragment.SavedState) fss[i]);
				}
			}
			Iterable<String> keys = bundle.keySet();
			for (String key : keys) {
				if (key.startsWith("f")) {
					int index = Integer.parseInt(key.substring(1));
					Fragment f = mFragmentManager.getFragment(bundle, key);
					if (f != null) {
						while (mFragments.size() <= index) {
							mFragments.add(null);
						}
						mFragments.set(index, f);
					}
				}
			}
		}
	}
}