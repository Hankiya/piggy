package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.howbuy.adp.FundRankAdp;
import com.howbuy.adp.FundRankAdp.RankHolder;
import com.howbuy.component.AppFrame;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragRankList extends AbsFragPage<NetWorthListBean> {// 排行用到的.
	private static final int CHANGE_FUND_TYPE = 1;
	private static final int CHANGE_SORT_TYPE = 2;
	private static final int CHANGE_RANK_SETTING = 3;
	private static final int CHANGE_FROM_OTHER = 4;
	private FundRankAdp mAdapter = null;
	private FundType mType = null;
	private FundConfig mFundConfig = null;
	private int mOrigionalDistinctDate = -1;
	private int[] mOrigionalIndexs = null;// 有就是两个长度,没有就是空.
	private int[] mOrigionalIndexsCount = null;
	private ArrayList<NetWorthBean> mOrigionalList = null;
	private boolean mNeedReloaded = false;

	public void filterAdSetAdapter() {
		boolean hasOptional = mFundConfig.hasFlag(FundConfig.RANK_FLAG_OPTIONAL);
		boolean hasOnsale = mFundConfig.hasFlag(FundConfig.RANK_FLAG_ONSALE) && !mType.isSimu();
		if (!(hasOptional || hasOnsale)) {
			mAdapter.setItems(mOrigionalList, false);
			mAdapter.setHeadIndex(mOrigionalIndexs, mOrigionalIndexsCount);
			mAdapter.notifyDataSetChanged();
		} else {
			boolean add = true;
			boolean hasIndex1 = mOrigionalIndexs != null && mOrigionalIndexsCount[0] > 0;
			boolean hasIndex2 = mOrigionalIndexs != null && mOrigionalIndexsCount[1] > 0;
			boolean hasIndex = hasIndex1 || hasIndex2;
			int n = mOrigionalList.size(), indexs[] = null, indexscount[] = null, toIndex[] = null;
			NetWorthBean b = null;
			ArrayList<NetWorthBean> r = new ArrayList<NetWorthBean>(mOrigionalList.size());
			if (hasIndex) {
				indexs = new int[] { -1, -1 };
				indexscount = new int[] { 0, 0 };
				toIndex = new int[] { mOrigionalIndexs[0] + mOrigionalIndexsCount[0],
						mOrigionalIndexs[1] + mOrigionalIndexsCount[1] };
			}
			for (int i = 0; i < n; i++) {
				b = mOrigionalList.get(i);
				if (hasOptional && hasOnsale) {
					add = b.getXunan() >= 1 && b.getHbTradFlag() == 1;
				} else {
					if (hasOptional) {
						add = b.getXunan() >= 1;
					} else {
						add = b.getHbTradFlag() == 1;
					}
				}
				if (add) {
					if (hasIndex) {
						if (hasIndex1) {
							if (i >= mOrigionalIndexs[0]) {
								if (i < toIndex[0]) {
									if (indexs[0] == -1) {
										indexs[0] = r.size();
										indexscount[0] = 1;
									} else {
										indexscount[0]++;
									}
								} else {
									hasIndex1 = false;
								}
							}
						}
						if (hasIndex2) {
							if (i >= mOrigionalIndexs[1]) {
								if (i < toIndex[1]) {
									if (indexs[1] == -1) {
										indexs[1] = r.size();
										indexscount[1] = 1;
									} else {
										indexscount[1]++;
									}
								} else {
									hasIndex2 = false;
								}
							}
						}
					}
					r.add(b);
				}
			}
			if (hasIndex) {
				if (indexscount[0] <= 0) {
					indexs[0] = -1;
				}
				if (indexscount[1] <= 0) {
					indexs[1] = -1;
				}
			}
			mAdapter.setItems(r, false);
			mAdapter.setHeadIndex(indexs, indexscount);
			mAdapter.notifyDataSetChanged();
		}
	}

	// name changed or select changed . pre for type the latter for sort. null
	// or -1 for no changed.
	public boolean setFundSortType(String fundTag, int sortIndex) {
		if (sortIndex != -1) {// sort key changed.
			mType.Selected = sortIndex;
			mType.saveSelected(AppFrame.getApp().getsF());
			new QueryFundTask(CHANGE_SORT_TYPE).execute(false);
		} else {// fund type changed.
			mType = mFundConfig.getType(fundTag);
			new QueryFundTask(CHANGE_FUND_TYPE).execute(false);
		}
		return true;
	}

	public void setNeedReloadedOnResume(boolean needReloaded, boolean force) {
		mNeedReloaded = needReloaded;
		if (force) {
			new QueryFundTask(CHANGE_FROM_OTHER).execute(false);
			mNeedReloaded = false;
		}
	}

	public void onFundSettingChanged(boolean fromMenu) {
		if (fromMenu) {
			if (mOrigionalList != null) {
				filterAdSetAdapter();
			}
		} else {
			new QueryFundTask(CHANGE_RANK_SETTING).execute(false);
		}
	}

	public void onAddOption(String code, int type) {
		int n = mAdapter.getCount();
		NetWorthBean item = null;
		int index = -1;
		for (int i = 0; i < n; i++) {
			item = (NetWorthBean) mAdapter.getItem(i);
			if (item.getJjdm().equals(code)) {
				item.setXunan(type);
				index = i;
				break;
			}
		}
		if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_OPTIONAL)) {
			if (index >= 0 && type < 1) {
				mAdapter.removeItem(index, false);
			}
		}
		mAdapter.notifyDataSetInvalidated();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPullListView.onRefreshComplete();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFundConfig = FundConfig.getConfig();
		mType = mFundConfig.getType(getArguments().getString(FragTbRank.SFKEY_RANK_TB));
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mNeedReloaded) {
			mNeedReloaded = false;
			new QueryFundTask(CHANGE_FROM_OTHER).execute(false);
		}
	}

	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		if (mAdapter == null) {
			mAdapter = new FundRankAdp(getSherlockActivity(), new NetWorthListBean());
			mAdapter.registerDataSetObserver(new DataSetObserver() {

				@Override
				public void onChanged() {
					if (mAdapter.getCount() == 0
							&& mFundConfig.hasFlag(FundConfig.RANK_FLAG_OPTIONAL)) {
						mEmpty.setText("没有自选");
					} else {
						mEmpty.setText(null);
					}
				}
			});
		}
		getListView().setFastScrollEnabled(false);
		mPullListView.setAdapter(mAdapter);
		if (mAdapter.isEmpty()) {
			new QueryFundTask(CHANGE_FUND_TYPE).execute(false);
		}
		setPullRefushMode(false, false);
	}

	private void buildFundTypeCondition(StringBuffer sb) {
		sb.append("and type ");
		if (mType.equalType(FundConfig.TYPE_ALL)) {
			if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_ALL_EXCEPT_STRUCT)) {
				sb.append("not in('").append(FundConfig.TYPE_FENGBI).append("\',\'");
				sb.append(FundConfig.TYPE_SIMU).append("\',\'").append(FundConfig.TYPE_JIEGOU)
						.append("\')");
			} else {
				sb.append("not in('").append(FundConfig.TYPE_FENGBI).append("\',\'");
				sb.append(FundConfig.TYPE_SIMU).append("\')");
			}
		} else if (mType.equalType(FundConfig.TYPE_GUPIAO)) {
			if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_STOCK_MIX)) {
				sb.append("in('").append(FundConfig.TYPE_GUPIAO).append("\',\'");
				sb.append(FundConfig.TYPE_HUNHE).append("\')");
			} else {
				sb.append("=='").append(mType.ClassType).append('\'');
			}
		} else if (mType.equalType(FundConfig.TYPE_HUOBI)) {
			if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL)) {
				sb.append("in('").append(FundConfig.TYPE_HUOBI).append("\',\'");
				sb.append(FundConfig.TYPE_LICAI).append("\')");
			} else {
				sb.append("=='").append(mType.ClassType).append('\'');
			}
		} else {
			sb.append("=='").append(mType.ClassType).append('\'');
		}
	}

	private void buildExtrasCondition(StringBuffer sb, String extraCondition) {
		if (mType.equalType(FundConfig.TYPE_HUOBI)) {
			if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_CURRENCY_EXCEPT_BFLAG)) {
				sb.append(" and mbflag<>").append(1);
			}
		}
		if (extraCondition != null) {
			sb.append(" and ").append(extraCondition);
		}
		/*
		 * if (mFundConfig.hasFlag(FundConfig.RANK_FLAG_OPTIONAL)) {
		 * sb.append(" and xuan in(").append(SelfConfig.SynsShowAdd)
		 * .append(',').append(SelfConfig.UNSynsAdd).append(')'); } if
		 * (mFundConfig.hasFlag(FundConfig.RANK_FLAG_ONSALE)) {
		 * sb.append(" and tradeflag==").append(1); }
		 */
	}

	private void buildSortCondition(StringBuffer sb) {
		sb.append(" order by ").append(mType.getSortType().ColumName).append(" desc");
		if (!mType.isHuobi()) {
			sb.append(",jjjz desc");
		}
		sb.append(",pinyin asc");// ,code asc

	}

	private String buildCondition(String extraCondition) {
		StringBuffer sb = new StringBuffer(256);
		buildFundTypeCondition(sb);
		buildExtrasCondition(sb, extraCondition);
		buildSortCondition(sb);
		return sb.toString();
	}

	protected ArrayList<NetWorthBean> adjustResult(int[] index, int[] indexcound,
			ArrayList<ArrayList<NetWorthBean>> lss) {
		ArrayList<NetWorthBean> r = new ArrayList<NetWorthBean>(), temp;
		for (int i = 0; i < lss.size(); i++) {
			temp = lss.get(i);
			if (temp.size() > 0) {
				temp = sortResult(temp);
				if (i != 0 && index != null) {// first list don't add into head
					index[i - 1] = r.size();
					indexcound[i - 1] = temp.size();
				}
				r.addAll(temp);
			}
		}
		return r;
	}

	protected ArrayList<NetWorthBean> sortResult(ArrayList<NetWorthBean> r) {
		return r;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FundUtils.launchFundDetails(this, ((RankHolder) view.getTag()).mItem, position - 1,
				ValConfig.ATY_REQUEST_FUND_DETAIL);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.notifyDataSetChanged();
		mPullListView.onRefreshComplete();
	}

	class QueryFundTask extends AsyPoolTask<Void, Void, ReqResult<ReqOpt>> {
		private int mTaskType = 0;
		private int mSortValueIndex = 0;
		private int mExtraValueIndex = FundUtils.VALUE_JJJZ;
		private int[] mIndexs = null;
		private int[] mIndexsCount = null;

		public QueryFundTask(int taskType) {
			this.mTaskType = taskType;
			if (mTaskType == CHANGE_FUND_TYPE) {
				showProgress(true);
			}
		}

		private int mergeNullList(ArrayList<NetWorthBean> des, ArrayList<NetWorthBean> res) {
			int size = des.size();
			int n = res.size();
			NetWorthBean b = null;
			for (int i = 0; i < n; i++) {
				b = res.get(i);
				b.setSortIndex(size++);
				des.add(b);
			}
			return size;
		}

		@Override
		protected ReqResult<ReqOpt> doInBackground(Void... p) {
			ReqResult<ReqOpt> result = new ReqResult<ReqOpt>(new ReqOpt(0, null, mTaskType));
			try {
				ArrayList<ArrayList<NetWorthBean>> r = null;
				String sql = null;
				mSortValueIndex = mType.getSortType().ValueIndex;
				mOrigionalDistinctDate = -1;
				if (mType.DataType == 2) {
					mExtraValueIndex = 0;
				}
				if (mType.isSimu()) {
					mIndexs = new int[] { -1, -1 };
					mIndexsCount = new int[] { 0, 0 };
					Calendar now = Calendar.getInstance();
					now.add(Calendar.DAY_OF_YEAR, -45);
					String date = StrUtils.timeFormat(now.getTimeInMillis(), ValConfig.DATEF_YMD);
					sql = buildCondition("jzrq>='" + date + "'");
					ArrayList<ArrayList<NetWorthBean>> pre = DbOperat.getInstance().queryAll(sql,
							mOrigionalDistinctDate, mSortValueIndex, false);
					sql = buildCondition("jzrq<'" + date + "'");
					ArrayList<ArrayList<NetWorthBean>> nex = DbOperat.getInstance().queryAll(sql,
							mOrigionalDistinctDate, mSortValueIndex, false);

					r = new ArrayList<ArrayList<NetWorthBean>>(3);
					r.add(new ArrayList<NetWorthBean>());
					r.add(pre.get(0));
					mIndexs[1] = r.get(1).size();
					mIndexsCount[1] = mergeNullList(nex.get(0), pre.get(1));
					mIndexsCount[1] = mergeNullList(nex.get(0), nex.get(1));
					r.add(nex.get(0));
				} else {
					sql = buildCondition(null);
					if (mSortValueIndex == FundUtils.VALUE_WFSY
							|| mSortValueIndex == FundUtils.VALUE_QRSY
							|| mSortValueIndex == FundUtils.VALUE_HBDR) {
						mIndexs = new int[] { -1, -1 };
						mIndexsCount = new int[] { 0, 0 };
						mOrigionalDistinctDate = 2;
					}
					r = DbOperat.getInstance().queryAll(sql, mOrigionalDistinctDate,
							mSortValueIndex, true);
				}

				result.setData(adjustResult(mIndexs, mIndexsCount, r));
			} catch (Exception e) {
				WrapException err = null;
				if (e instanceof WrapException) {
					err = (WrapException) e;
				} else {
					err = WrapException.wrap(e, "" + mType);
				}
				result.setErr(err);
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ReqResult<ReqOpt> r) {
			if (r.isSuccess()) {
				mAdapter.setSortIndex(mSortValueIndex, mExtraValueIndex, mType);
				mOrigionalIndexs = mIndexs;
				mOrigionalIndexsCount = mIndexsCount;
				mOrigionalList = (ArrayList<NetWorthBean>) r.mData;
				if (mOrigionalList != null) {
					filterAdSetAdapter();
				}
			}
			showProgress(false);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ValConfig.ATY_REQUEST_FUND_DETAIL && data != null) {
			if (mOrigionalDistinctDate != 2) {
				NetWorthBean b = (NetWorthBean) data.getSerializableExtra(ValConfig.IT_ENTITY);
				int pos = data.getIntExtra(ValConfig.IT_URL, -1);
				if (b != null && pos != -1) {
					android.support.v4.app.Fragment fgg = getTargetFragment();
					if (!(fgg instanceof FragTbRank)) {
						fgg = getParentFragment();
					}

					if (fgg instanceof FragTbRank) {
						((FragTbRank) fgg).onAddOptional(this, b.getJjdm(), b.getXunan());
					}
				}
			} else {
				mNeedReloaded = true;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
