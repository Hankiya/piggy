package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.howbuy.lib.frag.AbsHbFrag;
import com.tang.library.pulltorefresh.PullToRefreshBase.Mode;
import com.tang.library.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.tang.library.pulltorefresh.PullToRefreshListView;

public abstract class AbsFragList extends AbsHbFrag implements OnRefreshListener2<ListView>,
		OnItemClickListener {
	protected PullToRefreshListView mPullListView;
	protected TextView mEmpty;
	protected View mProgressLay;
	protected ListView mListView;

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
			if (mPullListView.isRefreshing()) {
				mPullListView.onRefreshComplete();
			}
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mPullListView = (PullToRefreshListView) mRootView.findViewById(R.id.listView);
		mEmpty = (TextView) mRootView.findViewById(R.id.empty);
		mProgressLay = mRootView.findViewById(R.id.lay_progress);
		mListView = mPullListView.getRefreshableView();
		mListView.setCacheColorHint(0);
		mPullListView.setMode(Mode.BOTH);
		mPullListView.setOnRefreshListener(this);
		mPullListView.setOnItemClickListener(this);
		mPullListView.setEmptyView(mEmpty);
		// mListView.setDivider(getResources().getDrawable(R.drawable.divider_padding));
	}

	public void setPullRefushMode(boolean pullDown, boolean pushUp) {
		if (!pullDown && !pushUp) {
			mPullListView.setMode(Mode.DISABLED);
		} else {
			if (mListView.getVisibility() != View.VISIBLE) {
				mListView.setVisibility(View.VISIBLE);
			}
			if (pullDown && pushUp) {
				mPullListView.setMode(Mode.BOTH);
			} else {
				if (pullDown) {
					mPullListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
				} else {
					mPullListView.setMode(Mode.PULL_UP_TO_REFRESH);
				}
			}
		}

	}
}
