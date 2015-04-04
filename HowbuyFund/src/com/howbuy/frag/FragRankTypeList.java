package com.howbuy.frag;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.howbuy.adp.RankTypeAdp;
import com.howbuy.adp.RankTypeAdp.RankTypeHolder;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import howbuy.android.palmfund.R;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragRankTypeList extends AbsFragList {
	private RankTypeAdp mAdapter = null;
	private int mMergeFlag = 0;

	private ArrayList<String> getFilter(int flag) {
		ArrayList<String> r = new ArrayList<String>();
		//r.add(FundConfig.TYPE_SIMU);
		r.add(FundConfig.TYPE_FENGBI);
		r.add(FundConfig.TYPE_OTHER);
		Fragment f = getTargetFragment();
		if (!(f instanceof FragTbRank)) {
			f = getParentFragment();
		}
		if (f instanceof FragTbRank) {
			ArrayList<FundType> tabs = ((FragTbRank) f).getTabFunds();
			r.add(tabs.get(0).ClassType);
			r.add(tabs.get(1).ClassType);
		}
		FundConfig con = FundConfig.getConfig();
		mMergeFlag = con.getFlag()
				& (FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL | FundConfig.RANK_FLAG_MERGE_STOCK_MIX);
		if (mMergeFlag != 0) {
			if (con.hasFlag(FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL)) {
				r.add(FundConfig.TYPE_LICAI);
			}
			if (con.hasFlag(FundConfig.RANK_FLAG_MERGE_STOCK_MIX)) {
				r.add(FundConfig.TYPE_HUNHE);
			}
		}
		return r;
	}

	private RankTypeAdp getAdapter(boolean forceCreated) {
		if (mAdapter == null || forceCreated) {
			FundConfig con = FundConfig.getConfig();
			mMergeFlag = con.getFlag()
					& (FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL | FundConfig.RANK_FLAG_MERGE_STOCK_MIX);
			ArrayList<String> r = getFilter(mMergeFlag);
			ArrayList<FundType> list = null, ori = con.getFundTypes();
			if (r.isEmpty()) {
				list = ori;
			} else {
				int n = ori.size();
				list = new ArrayList<FundConfig.FundType>(n);
				for (int i = 0; i < n; i++) {
					FundType f = ori.get(i);
					if (!r.contains(f.ClassType)) {
						list.add(f);
					}
				}
			}
			mAdapter = new RankTypeAdp(getSherlockActivity(), list);
		}
		return mAdapter;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		mListView.setAdapter(getAdapter(false));
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Fragment f = getTargetFragment();
		if (!(f instanceof FragTbRank)) {
			f = getParentFragment();
		}
		if (f instanceof FragTbRank) {
			((FragTbRank) f).onFundTypeClick(((RankTypeHolder) view.getTag()).mItem);
		}
	}

	public void onFundSettingChanged(int flag) {
		mMergeFlag += flag;
		if (mListView != null) {
			mListView.setAdapter(getAdapter(true));
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

}
