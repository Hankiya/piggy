package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.howbuy.adp.RankSettingAdp;
import com.howbuy.adp.RankSettingAdp.RankSettingHolder;
import com.howbuy.adp.RankSettingAdp.RankSettingItem;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.utils.ViewUtils;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragRankSettingList extends AbsFragList implements OnItemClickListener {
	private RankSettingAdp mAdapter = null;
	private FundConfig mFundConfig = null;

	private RankSettingAdp getAdapter() {
		if (mAdapter == null) {
			mFundConfig = FundConfig.getConfig();
			mAdapter = new RankSettingAdp(getSherlockActivity(), mFundConfig, getResources()
					.getStringArray(R.array.fund_type_setting));
		}
		return mAdapter;

	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		Bundle arg = getArguments();
		if (mTitleLable == null) {
			mTitleLable = arg.getString(ValConfig.IT_NAME);
		}
		mListView.setAdapter(getAdapter());
		mListView.setOnItemClickListener(this);
		setPullRefushMode(false, false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((RankSettingHolder) view.getTag()).changeView(0);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		notifyTargetFragment(fromBar);
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	private final void notifyTargetFragment(boolean fromBar) {
		int oriFlag = mAdapter.getOriFlag();
		if (oriFlag != mFundConfig.getFlag()) {
			parseChangedFlag(oriFlag);
			FundConfig.getConfig().saveFlag(AppFrame.getApp().getsF());
			Fragment f = getTargetFragment();
			if (f instanceof FragTbRank) {
				((FragTbRank) f).onFundSettingChanged();
			}
		}
	}

	private void parseChangedFlag(int oriFlag) {
		ArrayList<RankSettingItem> list = mAdapter.getItems();
		RankSettingItem it = null;
		for (int i = 0; i < list.size(); i++) {
			it = list.get(i);
			if (ViewUtils.hasFlag(oriFlag, it.Flag) != it.Checked) {
				Analytics.onEvent(getSherlockActivity(), Analytics.SWITCH_CLASSIFY_OPTION,
						Analytics.KEY_SWITCH_NAME, it.Name);
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

}
