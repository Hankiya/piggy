package com.howbuy.frag;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import howbuy.android.palmfund.R;
import com.howbuy.lib.entity.AbsLoadList;
import com.howbuy.lib.frag.AbsHbFrag;
import com.tang.library.pulltorefresh.PullToRefreshBase.Mode;
import com.tang.library.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.tang.library.pulltorefresh.PullToRefreshListView;

public abstract class AbsFragPage<T extends AbsLoadList<?, ?>> extends AbsHbFrag implements
		OnRefreshListener2<ListView>, OnItemClickListener {
	protected PullToRefreshListView mPullListView;
	protected TextView mEmpty;
	protected View mProgressLay;
	protected int mPageSizeBasic = 25;
	protected int mPageNum = 0;// 第几页
	protected int mPageCount = 0;// 一页显示多少信息

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPageNum", mPageNum);
		outState.putInt("mPageCount", mPageCount);
	}

	void showProgress(boolean show) {
		if (show) {
			if (mProgressLay.getVisibility() != View.VISIBLE) {
				mProgressLay.setVisibility(View.VISIBLE);
				mEmpty.setVisibility(View.GONE);
			}
		} else {
			if (mProgressLay.getVisibility() == View.VISIBLE) {
				mProgressLay.setVisibility(View.GONE);
			}
		}
	}

	public ListView getListView() {
		return mPullListView.getRefreshableView();
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mPullListView = (PullToRefreshListView) mRootView.findViewById(R.id.listView);
		mEmpty = (TextView) mRootView.findViewById(R.id.empty);
		mProgressLay = mRootView.findViewById(R.id.lay_progress);
		getListView().setCacheColorHint(0);
		mPullListView.setMode(Mode.BOTH);
		mPullListView.setOnRefreshListener(this);
		mPullListView.setOnItemClickListener(this);
		mPullListView.setEmptyView(mEmpty);
		// getListView().setDivider(getResources().getDrawable(R.drawable.divider_padding));
		if (bundle != null) {
			mPageNum = bundle.getInt("mPageNum");
			mPageCount = bundle.getInt("mPageCount");
		}
	}

	/**
	 * 检测是否是最后一页
	 * 
	 * @param t
	 * @return
	 */
	public boolean checkIsLastPage(T t) {
		if (t == null) {
			return false;
		}
		int curr = t.size();
		if (t.getTotalNum() != 0) {
			int total = t.getTotalNum();
			if (total <= curr) {
				return true;
			}
		} else {
			int sum = mPageCount * mPageNum;
			if (sum <= curr) {
				return true;
			}
		}
		return false;
	}

	public void setPullRefushMode(boolean pullDown, boolean pullUp) {
		if (pullDown && pullUp) {
			mPullListView.setMode(Mode.BOTH);
		} else if (!pullDown && !pullUp) {
			mPullListView.setMode(Mode.DISABLED);
		} else {
			if (pullDown) {
				mPullListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
			} else {
				mPullListView.setMode(Mode.PULL_UP_TO_REFRESH);
			}
		}
	}
}
