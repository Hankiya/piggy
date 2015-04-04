package com.howbuy.lib.frag;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.howbuy.lib.utils.LogUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-3-21 上午11:06:34
 */
@Deprecated
public class FragMger1 extends AbsFragMger {
	private AbsFrag mFragF = null;
	public FragMger1(FragmentActivity aty, int contentId) {
		super(aty, contentId);
	}
	public FragMger1(FragmentActivity aty, FrameLayout content) {
		super(aty, content);
	}

	public boolean handBackPressed(boolean fromBackKey) {
		int n = mFragMger.getBackStackEntryCount();
		if (n > 0) {
			String fragTag = mFragMger.getBackStackEntryAt(n - 1).getName();
			Fragment ft = mFragMger.findFragmentByTag(fragTag);
			if (!mFragCur.equals(ft)) {
				switchToFrag(new FragOpt(fragTag, 0));
				return true;
			} else {
				if (n == 1 && mFragF == ft) {
					mAty.finish();
					return true;
				} else {
					if (!fromBackKey) {
						mAty.onBackPressed();
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onBackStackChanged() {
		int n =mBackStackCount= mFragMger.getBackStackEntryCount();
		StringBuilder sb = new StringBuilder(64);
		sb.append("\r\n>>>BackStackChanged S===>\r\n-------back stack list<"
				+ n + ">-------\r\n");
		for (int i = 0; i < n; i++) {
			BackStackEntry be = mFragMger.getBackStackEntryAt(i);
			sb.append("back_frag_").append(i).append(":").append(be.getName())
					.append("\r\n");
		}
		List<Fragment> list = mFragMger.getFragments();
		n = list == null ? 0 : list.size();
		sb.append("+++++++frag list <" + n + ">++++++\r\n");
		for (int i = 0; i < n; i++) {
			Fragment f = list.get(i);
			if (f != null) {
				if (f.isVisible()) {
					mFragCur = (AbsFrag) f;
				}
				sb.append("list_frag_").append(i)
						.append(":" + f.getClass().getName()).append("\r\n");
			} else {
				sb.append("list_frag_").append(i).append(": frag is null\r\n");
			}
		}
		sb.append(">>>BackStackChanged E===> view size="
				+ mContent.getChildCount() + "cur frag=" + mFragCur + "\r\n");
		print(sb.toString());
		notifyFragChanged(mFragCur,mFragCur.getClass().getName());
		notiffyFragStackChanged(mFragMger.getBackStackEntryCount());
	}

	private boolean cleanTopNoStackFrag() {
		int n = mFragMger.getBackStackEntryCount() - 1;
		if (n >= 0) {
			if (mFragCur != mFragMger.findFragmentByTag(mFragMger
					.getBackStackEntryAt(n).getName())) {
				handBackPressed(true);
				return true;
			}
		}
		return false;
	}

	private Fragment switchToExistFrag(FragmentTransaction ft, FragOpt fo,
			Fragment fg) {
		boolean cleanTop = false;// mFragMger.popBackStackImmediate();
		if (cleanTop) {
			fo.subAction(FragOpt.FRAG_POPBACK);
		}
		if (fg.isVisible()) {
			print("find required frag from cache  . cleanTop=" + cleanTop
					+ " visible=" + fg.isVisible() + " resume=");
			return null;
		} else {
			print("find required frag from cache  . attach and show it. cleanTop="
					+ cleanTop);
			ft.attach(fg);
			ft.show(fg);
		}
		return fg;
	}

	private Fragment switchToNewFrag(FragmentTransaction ft, FragOpt fo) {
		fo.addTransAnim(ft);
		Fragment fg = fo.getFrag(mAty);
		if (mFragF == null) {
			ft.add(mContentId, fg, fo.getTag());
			mFragF = (AbsFrag) fg;
			fo.subAction(FragOpt.FRAG_POPBACK);
			print("create required frag  . add it first created");
		} else {
			ft.add(mContentId, fg, fo.getTag());
			print("create required frag . add it ");
		}
		return fg;
	}

	public void switchToFrag(FragOpt fo) {
		print("\r\n===switchFrag S--->require frag: " + fo.getTag());
		boolean cleanTop = false;
		Fragment fg = mFragMger.findFragmentByTag(fo.getTag());
		FragmentTransaction ft = mFragMger.beginTransaction();
		if (fg != null) {
			fg = switchToExistFrag(ft, fo, fg);
		} else {
			if (mFragCur != null && fo.hasCleanTop() && fo.hasBackStack()) {
				if (cleanTop = cleanTopNoStackFrag()) {
				}
			}
			fg = switchToNewFrag(ft, fo);
		}
		if (fg != null) {
			if (mFragCur != null && fg != mFragCur) {
				ft.hide(mFragCur);
				ft.detach(mFragCur);
				print("handle old frag:" + mFragCur + "  hide and detach it");
			}
			mFragCur = (AbsFrag) fg;
			if (fo.addBackStack(ft)) {
				print("add " + fo.getTag() + " to backstack");
			} else {
				notifyFragChanged(mFragCur,fo.getTag());
			}
			fo.addAction(FragOpt.FRAG_ALLOW_LOSS_STATE);
			fo.commit(ft);
			if (cleanTop) {
				mFragMger.executePendingTransactions();
			}
			
			print("===switchFrag E---> mFragMger.size="
					+ (mFragMger.getFragments() == null ? 0 : mFragMger
							.getFragments().size()) + " view size="
					+ mContent.getChildCount() + " cur frag:"
					+ mFragCur.getClass().getName() + "\r\n");
		}

	}

	private void print(String msg) {
		LogUtils.d(TAG, msg);
	} 

}
