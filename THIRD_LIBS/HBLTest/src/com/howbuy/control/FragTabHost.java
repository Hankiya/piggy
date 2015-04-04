package com.howbuy.control;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.howbuy.libtest.R;
 

/**
 * @author rexy 840094530@qq.com
 * @date 2014-3-7 下午5:21:03
 */
public class FragTabHost extends FrameLayout {
	private ArrayList<FragTab> mFrags = new ArrayList<FragTabHost.FragTab>();
	private FragmentManager mFragMger;
	private int mContentId;
	private int mCurrentTab = -1;
	private IFragTabChanged mTabChangedListener;
	private IInnerFragTabChanged mTbWidget = null;
	private boolean mEnableAnim = false;
	private boolean mKeepState = true;
	private ViewGroup mContentLayout;

	public FragTabHost(Context context) {
		this(context, null, 0);
	}

	public FragTabHost(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FragTabHost(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setup(FragmentManager fragMger,boolean keepState) {
		setup(fragMger, 0,keepState);
	}

	public void setup(FragmentManager fragMger, int contentResId,boolean keepState) {
		if (mFragMger == null) {
			mFragMger = fragMger;
			mContentId = contentResId == 0 ? android.R.id.tabcontent
					: contentResId;
			mContentLayout = (ViewGroup) findViewById(mContentId);
			mTbWidget = (IInnerFragTabChanged) findViewById(android.R.id.tabs);
			mKeepState=keepState;
		}
	}

	public void addTab(Fragment frag, String fragTag) {
		if (frag != null) {
			mFrags.add(new FragTab(frag, fragTag));
		}
	}

	private boolean checkTabAdded(FragTab fragTab) {
		Fragment frag = mFragMger.findFragmentByTag(fragTab.mTag);
		if (frag != null) {
			fragTab.setFrag(frag);
			return true;
		}
		return false;
	}

	private void hideFrag(FragmentTransaction ft, Fragment fg) {
		if (mKeepState) {
			if (fg.isVisible()) {
				fg.setMenuVisibility(false);
				fg.setUserVisibleHint(false);
				fg.onPause();
				ft.hide(fg);
			}

		} else {
			if (!fg.isDetached()) {
				ft.hide(fg);
				ft.detach(fg);
			}

		}
	}

	private void showFrag(FragmentTransaction ft, Fragment fg) {
		if (mKeepState) {
			if (!fg.isVisible()) {// when not visible.
				fg.setMenuVisibility(true);
				fg.setUserVisibleHint(true);
				fg.onResume();
				ft.show(fg);
			}
		} else {
			if (fg.isDetached()) {// when detached.
				ft.attach(fg);
				ft.show(fg);
			}
		}
	}

	private void switchToTab(FragTab fragTab, int i, boolean anim) {
		FragmentTransaction ft = obtainFragTransaction(i, anim);
		if (!fragTab.mFragment.isAdded()) {// frag not added int the fragment.
			if (!checkTabAdded(fragTab)) {// frag not in the fragmentmanager.
				ft.add(mContentId, fragTab.mFragment, fragTab.mTag);
			} else {
				showFrag(ft, fragTab.mFragment);
			}
		} else {
			    showFrag(ft, fragTab.mFragment);
		}
		 
		if (mCurrentTab != -1) {// hide old fragment.
			fragTab = mFrags.get(mCurrentTab);
			hideFrag(ft, fragTab.mFragment);

		} else {
			int n = mFrags.size();
			for (int j = 0; j < n; j++) {
				if (i != j) {
					fragTab = mFrags.get(j);
					Fragment frag = mFragMger.findFragmentByTag(fragTab.mTag);
					if (frag != null) {
						hideFrag(ft, frag);
					}
				}
			}
		}
		ft.commit();
	}

	public void setContentVisible(int visibleType) {
		if (visibleType == View.VISIBLE || visibleType == View.GONE
				|| visibleType == View.INVISIBLE) {
			if (mContentLayout.getVisibility() != visibleType) {
				mContentLayout.setVisibility(visibleType);
			}
		}
	}

	public boolean isContentVisible() {
		return mContentLayout.getVisibility() == View.VISIBLE;
	}

	public void setCurrentTab(int newIndex) {
		setCurrentTab(newIndex, mEnableAnim);
	}

	public void setCurrentTab(int newIndex, boolean anim) {
		if (mCurrentTab == newIndex)
			return;
		if (mTabChangedListener == null
				|| !mTabChangedListener.onTabChangedBefore(mCurrentTab,
						newIndex)) {
			for (int i = 0; i < mFrags.size(); i++) {
				if (i == newIndex) {
					switchToTab(mFrags.get(i), i, anim);
					mCurrentTab = newIndex;
					mTbWidget.onTabChanged(newIndex);
					mContentLayout.requestFocus();
					if (null != mTabChangedListener) {
						mTabChangedListener.onTabChanged(newIndex);
					}
					break;
				}
			}
		}
	}

	/**
	 * 获取一个带动画的FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragTransaction(int index, boolean anim) {
		FragmentTransaction ft = mFragMger.beginTransaction();
		if (anim && getHandler() != null) {// 设置切换动画
			if (index > mCurrentTab) {
				ft.setCustomAnimations(R.anim.slide_left_in,
						R.anim.slide_left_out);
			} else {
				ft.setCustomAnimations(R.anim.slide_right_in,
						R.anim.slide_right_out);
			}
		}
		return ft;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mCurrentTab == -1) {
			setCurrentTab(0, false);
		}
	}

	public int getCurrentTab() {
		return mCurrentTab;
	}

	public Fragment getCurrentFragment() {
		return mFrags.get(mCurrentTab).mFragment;
	}

	public String getCurrentTag() {
		return mFrags.get(mCurrentTab).mTag;
	}

	public View getCurrentWidgetItem() {
		return mTbWidget.getCurrentWidgetItem();
	}

	public View getTabWidget() {
		return (View) mTbWidget;
	}

	public View getTabContent() {
		return mContentLayout;
	}

	public int getTabCount() {
		return mFrags.size();
	}

	public void setTabChangedListener(IFragTabChanged l) {
		this.mTabChangedListener = l;
	}

	// ui to realize it.
	public static interface IFragTabChanged {
		public boolean onTabChangedBefore(int cur, int next);

		public void onTabChanged(int cur);
	}

	// tabwidget to realize it.
	public static interface IInnerFragTabChanged {
		public void onTabChanged(int cur);

		public View getCurrentWidgetItem();
	}

	public static class FragTab {
		public Fragment mFragment = null;
		public String mTag = null;

		public FragTab(Fragment mFragment, String mTag) {
			this.mFragment = mFragment;
			this.mTag = mTag;
		}

		public void setFrag(Fragment frag) {
			this.mFragment = frag;
		}
	}

}
