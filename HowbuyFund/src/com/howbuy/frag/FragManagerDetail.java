package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.howbuy.adp.ManagerAchieveAdp;
import com.howbuy.adp.ManagerArticalAdp;
import com.howbuy.adp.ManagerArticalAdp.ManagerArticalHolder;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParFtenManagerInf;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.wireless.entity.protobuf.FundProtos.Fund;
import com.howbuy.wireless.entity.protobuf.IssueProtos.Issue;
import com.howbuy.wireless.entity.protobuf.ManagerDetailInfoListProto.ManagerDetailInfoList;

@SuppressLint("NewApi")
public class FragManagerDetail extends AbsHbFrag implements IReqNetFinished, OnItemClickListener {
	private View mLayProgress, mLayListAchieve, mLayListInfo;
	private TextView mTvResume, mTvName;
	private ListView mListAchieve;
	private ListView mListInfo;
	private ManagerDetailInfoList mMgrInfo;
	private String mMgrId = null;
	private FundType mFundType = null;

	ManagerAchieveAdp mAchieveAdp = null;
	ManagerArticalAdp mInfoAdp = null;

	private void findAllViews(View root) {
		mLayProgress = root.findViewById(R.id.lay_progress);
		mTvResume = (TextView) root.findViewById(R.id.tv_resume);
		mTvName = (TextView) root.findViewById(R.id.tv_name);
		mLayListAchieve = root.findViewById(R.id.lay_achieve);
		mLayListInfo = root.findViewById(R.id.lay_info);
		mListAchieve = (ListView) root.findViewById(R.id.lv_achieve);
		mListInfo = (ListView) root.findViewById(R.id.lv_info);
		mListInfo.setOnItemClickListener(this);
		mListAchieve.setFocusable(false);
		mLayListInfo.setFocusable(false);
	}

	private void obtainFragArg() {
		Bundle arg = getArguments();
		if (mTitleLable == null) {
			mTitleLable = arg.getString(ValConfig.IT_NAME);
		}
		mMgrId = arg.getString(ValConfig.IT_ID);
		mFundType = (FundType) arg.getParcelable(ValConfig.IT_TYPE);
	}

	private void requestManagerInf(boolean forceRefush) {
		if (mMgrInfo == null || forceRefush) {
			new ParFtenManagerInf(CacheOpt.TIME_DAY)
					.setParams(mMgrId, mFundType.isSimu() ? "1" : "0").setCallback(1, this)
					.execute();
			if (mMgrInfo == null) {
				showProgress(true);
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		findAllViews(root);
		obtainFragArg();
		requestManagerInf(false);
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			mMgrInfo = (ManagerDetailInfoList) result.mData;
			mLayProgress.setVisibility(View.GONE);
			mTvName.setText(mMgrInfo.getRyxm());
			mTvResume.setText(mMgrInfo.getRyjj() != null ? mMgrInfo.getRyjj() : "--");
			// 旗下基金列表
			if (mMgrInfo.getFundCount() > 0) {
				if (mAchieveAdp == null) {
					ArrayList<Fund> achieve = new ArrayList<Fund>();
					achieve.addAll(mMgrInfo.getFundList());
					mAchieveAdp = new ManagerAchieveAdp(getSherlockActivity(), achieve);
				}
				mListAchieve.setAdapter(mAchieveAdp);
				ViewUtils.setListViewHeightBasedOnChildren(mListAchieve);
				mLayListAchieve.setVisibility(View.VISIBLE);
				mListAchieve.setSelector(new ColorDrawable(0));
			} else {
				mLayListAchieve.setVisibility(View.GONE);
			}

			// 报告列表
			if (mMgrInfo.getIssueCount() > 0) {
				if (mInfoAdp == null) {
					ArrayList<Issue> info = new ArrayList<Issue>();
					info.addAll(mMgrInfo.getIssueList());
					mInfoAdp = new ManagerArticalAdp(getSherlockActivity(), info);
				}
				mListInfo.setAdapter(mInfoAdp);
				ViewUtils.setListViewHeightBasedOnChildren(mListInfo);
				mLayListInfo.setVisibility(View.VISIBLE);
			} else {
				mLayListInfo.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_manager_details;
	}

	void showProgress(boolean show) {
		if (show) {
			if (mLayProgress.getVisibility() != View.VISIBLE) {
				mLayProgress.setVisibility(View.VISIBLE);
			}
		} else {
			if (mLayProgress.getVisibility() == View.VISIBLE) {
				mLayProgress.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = view.getTag();
		if (obj instanceof ManagerArticalHolder) {
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_ID, ((ManagerArticalHolder) obj).mItem.getJlzj());
			b.putString(ValConfig.IT_NAME, "基金经理言论");
			b.putInt(ValConfig.IT_TYPE, FragArticalRead.ARTICLA_ISSUE);
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivity(t);
		}
	}
}
