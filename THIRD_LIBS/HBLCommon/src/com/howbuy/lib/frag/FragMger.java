package com.howbuy.lib.frag;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

public class FragMger extends AbsFragMger {

	public FragMger(FragmentActivity aty, int contentId) {
		super(aty, contentId);
	}

	public FragMger(FragmentActivity aty, FrameLayout content) {
		super(aty, content);
	}

	private AbsFrag switchTo(FragmentTransaction ft, Fragment fg, FragOpt fo) {
		print("\r\n----------------------------------\r\nrequire frag:"
				+ fo.getTag() + ".");
		if (fg != null) {
			if (fg.isDetached()) {
				ft.attach(fg);
				ft.show(fg);
				print("cache ,attach and show frag  .... \r\n");
			} else {
				ft.show(fg);
				fg.onResume();
				fg.setMenuVisibility(true);
				print("cache ,show and resume frag  .... \r\n");
			}
		} else {
			fg = fo.getFrag(mAty);
			if (fo.hasCache() || (mFragCur != null && mFragCur.mFragCached)) {
				ft.add(mContentId, fg, fo.getTag());
				print("new ,add frag .... \r\n");
			} else {
				ft.replace(mContentId, fg, fo.getTag());
				print("new ,replace frag .... \r\n");
			}
		}
		return (AbsFrag) fg;
	}

	private void commit(FragmentTransaction ft, FragOpt fo, AbsFrag af) {
		if (fo.hasCache()) {
			if (mFragCur != null) {
				ft.hide(mFragCur);
				if (mFragCur.mFragCached) {
					if (!mFragCur.isDetached()) {
						mFragCur.onPause();
						mFragCur.setMenuVisibility(false);
						print("hide and pause frag :"
								+ mFragCur.getClass().getName() + " .... \r\n");
					} else {
						print("hide frag :" + mFragCur.getClass().getName()
								+ " .... \r\n");
					}
				} else {
					ft.detach(mFragCur);
					print("hide and detach frag :"
							+ mFragCur.getClass().getName() + " .... \r\n");
				}
			}
			af.mFragCached = true;
		} else {
			af.mFragCached = false;
			if (mFragCur != null && mFragCur.mFragCached) {
				ft.hide(mFragCur);
				if (!mFragCur.isDetached()) {
					mFragCur.onPause();
					mFragCur.setMenuVisibility(false);
					print("hide and pause frag :"
							+ mFragCur.getClass().getName() + " .... \r\n");
				} else {
					print("hide frag :" + mFragCur.getClass().getName()
							+ " .... \r\n");
				}
			}
		}
		if (!fo.addBackStack(ft)) {
			mFragCur = af;
			notifyFragChanged(mFragCur, fo.getTag());
		} else {
			print("add to back stack \r\n");
		}
		fo.commit(ft);

	}

	@Override
	public void switchToFrag(FragOpt fo) {
		Fragment fg = mFragMger.findFragmentByTag(fo.getTag());
		FragmentTransaction ft = mFragMger.beginTransaction();
		fo.addTransAnim(ft);
		commit(ft, fo, switchTo(ft, fg, fo));
	}

	@Override
	public void onBackStackChanged() {
		int n = mFragMger.getBackStackEntryCount();
		boolean addBack = n > mBackStackCount;
		mBackStackCount = n;
		if (addBack) {// add back frag.
			String fragTag = mFragMger.getBackStackEntryAt(n - 1).getName();
			mFragCur = (AbsFrag) mFragMger.findFragmentByTag(fragTag);
			notifyFragChanged(mFragCur, fragTag);
		} else {// sub back frag.
			List<Fragment> list = mFragMger.getFragments();
			n = list == null ? 0 : list.size();
			print("remove from back cur=" + mFragCur + " frag list size="
					+ n);
			
			Fragment fg = null, lastNoNullFrag = null;
			for (int i = 0; i < n; i++) {
				fg = list.get(i);
				if (fg != null && fg.isVisible()) {
					lastNoNullFrag = fg;
					break;
				} 
			}
			print("remove from back cur=" + mFragCur + " lastNotNullFrag="
					+ lastNoNullFrag);
			if (lastNoNullFrag != null) {
				if (mFragCur != null && mFragCur != lastNoNullFrag
						&& mFragCur.isVisible()) {
					FragmentTransaction ft = mFragMger.beginTransaction();
					ft.hide(mFragCur);
					if (mFragCur.mFragCached) {
						mFragCur.onPause();
						mFragCur.setMenuVisibility(false);
						print("hide and pause frag:"
								+ mFragCur.getClass().getSimpleName());
					} else {
						ft.detach(mFragCur);
						print("hide and detach frag:"
								+ mFragCur.getClass().getSimpleName());
					}
					ft.commitAllowingStateLoss();
					mFragMger.executePendingTransactions();
				}
				mFragCur = (AbsFrag) lastNoNullFrag;
				if (mFragCur.mFragCached) {
					mFragCur.onResume();
					mFragCur.setMenuVisibility(true);
				}
				notifyFragChanged(mFragCur, mFragCur.getClass().getName());
			}
		}
		n = mFragMger.getBackStackEntryCount();
		notiffyFragStackChanged(mFragMger.getBackStackEntryCount());
		StringBuilder sb = new StringBuilder(64);
		sb.append("\r\n>>>BackStackChanged S===>addBack=+" + addBack
				+ "\r\n-------back stack list<" + n + ">:\r\n");
		for (int i = 0; i < n; i++) {
			BackStackEntry be = mFragMger.getBackStackEntryAt(i);
			sb.append("back_frag_").append(i).append(":").append(be.getName())
					.append("\r\n");
		}
		List<Fragment> list = mFragMger.getFragments();
		n = list == null ? 0 : list.size();
		sb.append("+++++++frag list <" + n + ">:\r\n");
		for (int i = 0; i < n; i++) {
			Fragment f = list.get(i);
			if (f != null) {
				sb.append("list_frag_").append(i)
						.append(":" + f.getClass().getName())
						.append(" visible=" + f.isVisible()).append("\r\n");
			} else {
				sb.append("list_frag_").append(i).append(": frag is null\r\n");
			}
		}
		sb.append(">>>BackStackChanged E===> view size="
				+ mContent.getChildCount() + " curfrag="
				+ mFragCur.getClass().getName() + "\r\n");
		print(sb.toString());

	}

	private void print(String msg) {
		Log.d(TAG, msg);
	}

}
