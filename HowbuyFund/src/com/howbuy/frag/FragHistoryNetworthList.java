package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.howbuy.adp.FundHistoryAdp;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.AAParFundHistoryValue;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueOfPageProtos.HistoryFundNetValueOfPage;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueOfPageProtos.Info;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragHistoryNetworthList extends AbsFragList implements OnItemClickListener,
		IReqNetFinished {
	private FundHistoryAdp mAdapter = null;
	private NetWorthBean mBean = null;
	private FundType mFundType = null;
	private int mPageNum = 1;
	private int mPageCount = 25;

	private FundHistoryAdp getAdapter() {
		if (mAdapter == null) {
			Bundle arg = getArguments();
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			mFundType = (FundType) arg.getParcelable(ValConfig.IT_TYPE);
			mBean = (NetWorthBean) arg.getSerializable(ValConfig.IT_ENTITY);
			mAdapter = new FundHistoryAdp(getSherlockActivity(), mFundType, mBean.getDanWei());
		}
		if (mAdapter.getCount() == 0) {
			requestListData(ValConfig.LOAD_LIST_FIRST);
		}
		return mAdapter;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		mListView.setAdapter(getAdapter());
		mListView.setOnItemClickListener(this);
		mListView.setSelector(new ColorDrawable(0));
		int resid = mFundType.isHuobi() ? R.layout.com_list_history_title_huobi
				: R.layout.com_list_history_title;
		View title = LayoutInflater.from(getSherlockActivity()).inflate(resid, null);
		if (mFundType.isFengbi()) {
			((TextView) title.findViewById(R.id.tv_increase)).setText("单位涨幅");
		} else if (mFundType.isSimu()) {
			((TextView) title.findViewById(R.id.tv_increase)).setText("月涨幅");
		}
		RelativeLayout.LayoutParams lp = (LayoutParams) mPullListView.getLayoutParams();
		lp.topMargin = getSherlockActivity().getResources().getDimensionPixelSize(
				R.dimen.details_company_head_height);
		((RelativeLayout) mRootView).addView(title, -1, lp.topMargin);
		mEmpty.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestListData(ValConfig.LOAD_LIST_REFUSH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		requestListData(ValConfig.LOAD_LIST_PAGE);
	}

	private void requestListData(int loadType) {
		long cacheTime = 0;
		if (loadType != ValConfig.LOAD_LIST_PAGE) {
			mPageNum = 1;
			if (loadType == ValConfig.LOAD_LIST_FIRST) {
				showProgress(true);
			}
		}
		if (mAdapter == null || mAdapter.getCount() == 0) {
			cacheTime = CacheOpt.TIME_WEEK;
		}
		AAParFundHistoryValue par = new AAParFundHistoryValue(cacheTime);
		par.setParams(mBean.getJjdm(), mFundType.isSimu() ? "1" : "0", mPageNum, mPageCount);
		par.setArgInt(loadType);
		if (mAdapter == null || mAdapter.getCount() == 0) {
			par.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
		}
		par.setCallback(1, this).execute();
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		mPullListView.onRefreshComplete();
		int loadType = result.mReqOpt.getArgInt();
		if (result.isSuccess()) {
			HistoryFundNetValueOfPage hp = (HistoryFundNetValueOfPage) result.mData;
			int count = mPageCount;
			if (!StrUtils.isEmpty(hp.getCount())) {
				count = Integer.parseInt(hp.getCount());
			}
			ArrayList<Info> info = new ArrayList<Info>();
			info.addAll(hp.getInfoList());
			if (loadType == ValConfig.LOAD_LIST_PAGE && mAdapter.getCount() != 0) {
				mAdapter.addItems(info, true, true);
			} else {
				mAdapter.setItems(info, true);
			}
			mPageNum++;
			if (count > mAdapter.getCount()) {
				setPullRefushMode(true, true);
			} else {
				setPullRefushMode(true, false);
				if (mAdapter.getCount() == 0) {
					mEmpty.setVisibility(View.VISIBLE);
				}
			}
		} else {
			if (mAdapter.getCount() == 0) {
				mEmpty.setText("" + result.mErr.getError(null, false));
			}
		}
		showProgress(false);
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		if (isVisible()) {
			if (netType <= 1 && preNet > 1) {
				// pop("网络不可用", false);
			} else {
				if (netType > 1 && preNet <= 1) {
					if (mAdapter.isEmpty()) {
						onPullDownToRefresh(mPullListView);
					}
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}
}
