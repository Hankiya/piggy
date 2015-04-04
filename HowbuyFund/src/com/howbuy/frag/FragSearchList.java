package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howbuy.adp.SearchAdapter;
import com.howbuy.adp.SearchAdapter.QueryCallBack;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.AAParFundInfoByType;
import com.howbuy.db.DbUtils;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.Receiver;
import com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo;

public class FragSearchList extends AbsHbFrag implements AdapterView.OnItemClickListener,
		View.OnTouchListener, IReqNetFinished, QueryCallBack, OnClickListener {
	public static final String IntentAnotherFragBroadcast = "IntentAnotherFragBroadcast";
	public static final String ReqType = "4";
	private StickyListHeadersListView mListView;
	private SearchAdapter mSAdp;
	NetWorthListBean mBean;
	private String mTabType;
	private AAParFundInfoByType mFundInfoByType;
	private LinearLayout mNodataLay;
	private TextView mNortTips;
	private LinearLayout mNodataOtherClick;
	private TextView mOtherTipsTitle;
	private TextView mOtherTipsConent;
	private AnotherFragReciver mAnotherFragReciver;
	private static final int FIXED_TOP_NUM = 5;

	private FragSearch getPrantFrag() {
		return (FragSearch) getParentFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}


	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_search_list;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(ValConfig.IT_TYPE, mTabType);
		outState.putParcelable(ValConfig.IT_ENTITY, mBean);
	}

	@SuppressLint("NewApi")
	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mListView = (StickyListHeadersListView) root.findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
		mListView.setAreHeadersSticky(true);
		mListView.setOnTouchListener(this);
		mListView.getWrappedList().setScrollbarFadingEnabled(false);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			mListView.getWrappedList().setFastScrollAlwaysVisible(true);
		}

		mNodataLay = (LinearLayout) root.findViewById(R.id.nodata_lay);
		mNortTips = (TextView) root.findViewById(R.id.no_rt_tips);
		mNodataOtherClick = (LinearLayout) root.findViewById(R.id.nodata_other_click);
		mOtherTipsTitle = (TextView) root.findViewById(R.id.other_tips_title);
		mOtherTipsConent = (TextView) root.findViewById(R.id.other_tips_content);
		mNodataOtherClick.setOnClickListener(this);

		bundle = bundle == null ? getArguments() : bundle;
		mTabType = bundle.getString(ValConfig.IT_TYPE);
		mBean = bundle.getParcelable(ValConfig.IT_ENTITY);

		if (mBean == null) {
			mBean = new NetWorthListBean();
			if (mTabType != null && mTabType != null) {
				NetWorthListBean nb;
				if (mTabType.equals(FragSearch.FUND)) {
					nb = (NetWorthListBean) AppFrame.getApp().getMapObj(FragSearch.Map_Search_GM);
					// mSAdp.getFilter().filter(SearchFillter.FilterGm);
				} else {
					nb = (NetWorthListBean) AppFrame.getApp().getMapObj(FragSearch.Map_Search_Sm);
					// mSAdp.getFilter().filter(SearchFillter.FilterSm);
				}
				if (nb != null) {
					mBean.addItems(nb);
				}
			}
		}
		if (mSAdp == null) {
			mSAdp = new SearchAdapter(getSherlockActivity(), mBean, this, this, isSimu());
		}
		mListView.setAdapter(mSAdp);
	}

	private boolean isSimu() {
		return mTabType.equals(FragSearch.SIMU);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		if (mTabType.equals(FragSearch.FUND)) {
			mFundInfoByType = new AAParFundInfoByType(ReqType, this, CacheOpt.TIME_DAY);
			mFundInfoByType.setParams(4);
			mFundInfoByType.execute();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			FragSearch frag = getPrantFrag();
			if (frag != null && frag.getmSEditText() != null) {
				ViewUtils.showKeybord(frag.getmSEditText(), false);
				getPrantFrag().clearSearchFocus();
			}
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = mSAdp.getItem(position);
		NetWorthBean nB = (NetWorthBean) obj;
		String jjdm = nB.getJjdm();
		if (obj instanceof NetWorthBean) {
			FundUtils.launchFundDetails(this, jjdm, 0, 0);
			getPrantFrag().addToHistory(jjdm);
		}

		String alys = "";
		if (mSAdp.isQueryStatus()) {
			alys = "搜索结果";
		} else if (nB.getHbdr() != null) {
			alys = "热搜";
		} else {
			alys = "字母表";
		}
		Analytics.onEvent(getActivity(), Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, alys);
	}

	public SearchAdapter getmSAdp() {
		return mSAdp;
	}

	public void setmSAdp(SearchAdapter mSAdp) {
		this.mSAdp = mSAdp;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			FundInfo fund = (FundInfo) result.mData;
			if (fund != null && fund.getFundListCount() != 0) {
				new HandleHotTask().execute(false, fund);
			}
		}
	}

	final class HandleHotTask extends AsyPoolTask<FundInfo, Void, NetWorthListBean> {
		@Override
		protected NetWorthListBean doInBackground(FundInfo... params) {
			NetWorthListBean n = new NetWorthListBean();
			ArrayList<NetWorthBean> list = queryOptional(params[0]);
			n.addItems(list, false);
			return n;
		}

		private String buildQueryOptional(FundInfo mListFund) {
			StringBuilder sb = new StringBuilder();
			sb.append("select code,name,type,xuan,pinyin from fundsinfo where code ");
			sb.append("in(");
			for (int i = 0; i < FIXED_TOP_NUM; i++) {
				sb.append("'").append(mListFund.getFundList(i).getFundCode()).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
			return sb.toString();
		}

		protected ArrayList<NetWorthBean> queryOptional(FundInfo mListFund) {
			Cursor c = null;
			String sql = buildQueryOptional(mListFund);
			try {
				c = DbUtils.query(sql, null, false);
				if (c != null && c.moveToFirst()) {
					ArrayList<NetWorthBean> list = new ArrayList<NetWorthBean>();
					NetWorthListBean listBean = new NetWorthListBean();
					do {
						String jjdm = c.getString(0);
						String jjmc = c.getString(1);
						String jjfl = c.getString(2);
						int xuan = c.getInt(3);
						String pinyin = c.getString(4);
						NetWorthBean b = new NetWorthBean();
						b.setJjdm(jjdm);
						b.setJjmc(jjmc);
						b.setJjfl(jjfl);
						b.setXunan(xuan);
						b.setPinyin(TextUtils.isEmpty(pinyin) ? "--" : pinyin);
						b.setHbdr("-1");// to select
						list.add(b);
					} while (c.moveToNext());
					c.close();
					listBean.addItems(list, false);
					return list;
				}
				LogUtils.d("ranks", "query empty sql=" + sql);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(NetWorthListBean result) {
			super.onPostExecute(result);
			// mBean.addItems(result.getItems(), false);
			if (result != null) {
				mSAdp.updateBean(result);
			}
		}
	}

	private void listPostionToTop() {
		if (mListView.getWrappedList().isStackFromBottom()) {
			mListView.getWrappedList().setStackFromBottom(true);
		}
	}

	@Override
	public void haveRes(boolean resFlag) {
		listPostionToTop();
		if (resFlag) {
			mListView.setVisibility(View.VISIBLE);
			mNodataLay.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.GONE);
			mNodataLay.setVisibility(View.VISIBLE);

			String text = getResources().getString(
					isSimu() ? R.string.search_no_result_sm : R.string.search_no_result_gm);
			mNortTips.setText(String.format(text, getPrantFrag().getmSEditText().getText()));

			// FragSearchList anotherFrag = (FragSearchList)
			// getPrantFrag().getChildFragmentManager().findFragmentByTag(isSimu()?FragSearch.SIMU:FragSearch.FUND);
			FragSearchList anotherFrag = (FragSearchList) getPrantFrag().getChildFragmentManager()
					.findFragmentByTag(isSimu() ? FragSearch.FUND : FragSearch.SIMU);
			FragSearchList currFrag = (FragSearchList) getPrantFrag().getmCurrFragment();
			if (anotherFrag != null && currFrag != null && currFrag == this) {
				LogUtils.d("anotherFrag" + anotherFrag + "--issimu--");
				mAnotherFragReciver = new AnotherFragReciver();
				LocalBroadcastManager.getInstance(getSherlockActivity()).registerReceiver(
						mAnotherFragReciver,
						new IntentFilter(FragSearchList.IntentAnotherFragBroadcast));
				anotherFrag.getmSAdp().getFilter().filter(mSAdp.getQueryString());
			}

		}
	}

	public StickyListHeadersListView getmListView() {
		return mListView;
	}

	public void setmListView(StickyListHeadersListView mListView) {
		this.mListView = mListView;
	}

	/**
	 * 搜索无结果时接受另外一个fragment的结果
	 * 
	 * @author Administrator
	 * 
	 */
	private class AnotherFragReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (getActivity() == null) {
				return;
			}
			if (mListView.getVisibility() == View.GONE
					&& mNodataLay.getVisibility() == View.VISIBLE) {
				NetWorthListBean n = intent.getParcelableExtra(ValConfig.IT_ENTITY);
				StringBuilder otherB = new StringBuilder();
				if (n != null) {
					int size = n.size();
					if (size > 3) {
						size = 3;
					}
					// String.format(format, args)
					for (int i = 0; i < size; i++) {
						otherB.append(n.getItem(i).getJjmc()).append("，");
					}
					otherB.deleteCharAt(otherB.length() - 1);
					mNodataOtherClick.setVisibility(View.VISIBLE);
					mOtherTipsConent.setText(otherB.toString());

					String fmtStr;
					if (isSimu()) {
						fmtStr = String.format(
								getResources().getString(R.string.search_else_product_gm),
								getPrantFrag().getmSEditText().getText());
					} else {
						fmtStr = String.format(
								getResources().getString(R.string.search_else_product_sm),
								getPrantFrag().getmSEditText().getText());
					}
					mOtherTipsTitle.setText(fmtStr);
				} else {
					mNodataOtherClick.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mAnotherFragReciver != null) {
			try {
				getSherlockActivity().unregisterReceiver(mAnotherFragReciver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nodata_other_click:
			getPrantFrag().toggleGmSm();
			break;

		default:
			break;
		}

	}

	@Override
	public void onReceiveBroadcast(int from,Bundle b) {
		if (from==Receiver.FROM_OPTIONAL_CHNAGE) {
			String sb = b.getString(ValConfig.IT_ID);
			String[] sbArry = sb.split("--");
			if(sbArry!=null){
				NetWorthListBean nb = mSAdp.getBean();
				for (int i = 0; i < nb.size(); i++) {
					for (int j = 0; j < sbArry.length; j++) {
						String netVS = sbArry[j];
						int fengIndex = netVS.indexOf(":");
						String jjdm = netVS.substring(0, fengIndex);
						String status = netVS.substring(fengIndex + 1, netVS.length());
						if (nb.getItem(i).getJjdm().equals(jjdm)) {
							nb.getItem(i).setXunan(Integer.parseInt(status));
							break;
						}
					}
				}
				mSAdp.notifyDataSetChanged();	
			}
		}
	}

	@Override
	public boolean shouldEnableLocalBroadcast() {
		return true;
	}

}
