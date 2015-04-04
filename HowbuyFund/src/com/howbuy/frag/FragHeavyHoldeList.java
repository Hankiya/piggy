package com.howbuy.frag;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.howbuy.adp.HeavySimpleHoldAdp;
import com.howbuy.adp.HeavySimpleHoldAdp.HeavySimpleHolder;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.AAParFtenHeavyHoldingInfo;
import com.howbuy.datalib.fund.ParFtenHeavyHolding;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundZccgInfoProtos.FundZccgInfo;
import com.howbuy.wireless.entity.protobuf.FundZccgInfoProtos.FundZccgMain;
import com.howbuy.wireless.entity.protobuf.ZccgInfoListProtos.ZccgInfo;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragHeavyHoldeList extends AbsFragList implements OnItemClickListener, IReqNetFinished {
	private final static int HANDLE_HEAVY_LIST = 1;
	private final static int HANDLE_HEAVY_DETAIL = 2;
	private HeavySimpleHoldAdp mAdapter = null;
	private FundType mFundType = null;
	private String mDate = null;
	private String mJjdm = null;
	private int mPageNum = 1;
	private int mPageCount = 5;

	private HeavySimpleHoldAdp intiAdapter() {
		if (mAdapter == null) {
			Bundle arg = getArguments();
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			mFundType = (FundType) arg.getParcelable(ValConfig.IT_TYPE);
			mJjdm = arg.getString(ValConfig.IT_ID);
			mDate = arg.getString(ValConfig.IT_ENTITY);
			mAdapter = new HeavySimpleHoldAdp(getSherlockActivity(), null);
		}
		return mAdapter;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		mListView.setAdapter(intiAdapter());
		mListView.setOnItemClickListener(this);
		if (mAdapter.getCount() == 0) {
			requestListData(ValConfig.LOAD_LIST_FIRST);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = view.getTag();
		if (obj instanceof HeavySimpleHolder) {
			FundZccgInfo inf = ((HeavySimpleHolder) obj).mItem;
			{
				mDate = inf.getJzrq();
				String jjdm = inf.getJjdm();
				if (StrUtils.isEmpty(jjdm)) {
					jjdm = mJjdm;
				}
				new AAParFtenHeavyHoldingInfo(0)
						.setParams(jjdm, mFundType.isSimu() ? "1" : "0", mDate)
						.setCallback(HANDLE_HEAVY_DETAIL, this).execute();
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (mAdapter.getCount() == 0) {
			requestListData(ValConfig.LOAD_LIST_FIRST);
		} else {
			requestListData(ValConfig.LOAD_LIST_REFUSH);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (mAdapter.getCount() > 0) {
			requestListData(ValConfig.LOAD_LIST_PAGE);
		}
	}

	private void requestListData(int loadType) {
		if (loadType != ValConfig.LOAD_LIST_PAGE) {
			mPageNum = 1;
			if (loadType == ValConfig.LOAD_LIST_FIRST) {
				showProgress(true);
			}
		}
		ParFtenHeavyHolding par = new ParFtenHeavyHolding(0);
		par.setParams(mJjdm, mPageNum, mPageCount).setArgInt(loadType);
		par.setCallback(HANDLE_HEAVY_LIST, this).execute();
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		int handType = result.mReqOpt.getHandleType();
		if (handType == HANDLE_HEAVY_DETAIL) {
			handReqHeavyDetails(result);
		} else {
			handReqHeavyList(result);
		}
	}

	private void onHeavyListResult(List<FundZccgInfo> r, int loadType) {
		if (r == null || r.isEmpty())
			return;
		if (loadType == ValConfig.LOAD_LIST_PAGE) {
			mAdapter.addItems(new ArrayList<FundZccgInfo>(r), true, true);
		} else {
			int n = Math.min(r.size(), 3), remove = -1;
			for (int i = 0; i < n; i++) {
				String season = r.get(i).getJzrq();
				if (!StrUtils.isEmpty(season)) {
					if (season.equals(mDate)) {
						remove = i;
						break;
					}
				}
			}
			ArrayList<FundZccgInfo> result = new ArrayList<FundZccgInfo>(r);
			if (remove != -1) {
				result.remove(remove);
			}
			mAdapter.setItems(result, true);
		}
	}

	private void handReqHeavyList(ReqResult<ReqNetOpt> result) {
		showProgress(false);
		if (mPullListView.isRefreshing()) {
			mPullListView.onRefreshComplete();
		}
		int loadType = result.mReqOpt.getArgInt();
		if (result.isSuccess()) {
			FundZccgMain hp = (FundZccgMain) result.mData;
			onHeavyListResult(hp.getFundZccgInfoList(), loadType);
			int count = hp.getTotalNum();
			mPageNum++;
			if (count > mAdapter.getCount()) {
				setPullRefushMode(false, true);
			} else {
				setPullRefushMode(false, false);
			}
		} else {
			if (mAdapter.getCount() == 0) {
				mEmpty.setText(FundUtils.formatStr(result.mErr.getError(null, false), 0,
						ValConfig.NET_ERR));
				setPullRefushMode(true, false);
			}
		}
	}

	private void handReqHeavyDetails(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			ZccgInfo inf = (ZccgInfo) result.mData;
			if (inf.getStockListCount() > 0) {
				Bundle b = new Bundle();
				b.putString(ValConfig.IT_NAME, "重仓持股");
				b.putByteArray(ValConfig.IT_ENTITY, inf.toByteArray());
				b.putParcelable(ValConfig.IT_TYPE, mFundType);
				Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
				t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragHeaveDetailList.class.getName());
				t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
				t.putExtra(AtyEmpty.KEY_EXIT_NOANIM, true);
				t.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(t);
			} else {
				pop("内容为空", false);
			}
		} else {
			pop("请求失败,请重试", false);
		}
	}

}
