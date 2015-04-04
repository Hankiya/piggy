package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.actionbarsherlock.widget.SearchView.OnSuggestionListener;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CanScrollViewPager;
import com.howbuy.control.PagerSlidingTabStrip;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.adp.AbsFragPageAdp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;

public class FragSearch extends AbsHbFrag implements OnClickListener, OnQueryTextListener,
		OnFocusChangeListener {
	// 进入搜索的类型
	public static final int Type_Intent_Fund = 1;
	public static final int Type_Intent_Simu = 2;
	// 缓存全部基金
	public static final String Map_Search_GM = "Map_Search_gm";
	public static final String Map_Search_Sm = "Map_Search_sm";
	// tab名称
	public static final String FUND = "基金";
	public static final String SIMU = "私募";
	public static final String[] Optional_Tab = new String[] { FUND, SIMU };
	// search history common db key title
	// key=CommenDb_key
	// subKey=jjdm
	// add time=date
	public static final String CommenDb_Key = "search_history";

	private ProgressBar mProgressBar;
	private LinearLayout mContentLayout;
	private PagerSlidingTabStrip mIndicator;
	private CanScrollViewPager mViewPager;
	private FragmentManager mFragmentManager;
	private SearchView mSearchView;
	private MenuItem mSMenuItem;
	private AutoCompleteTextView mSEditText;
	private boolean isFirstResume = true;
	private FragSearchList mCurrFragment;
	private int mIntentType;
	private HistoryAdapter mHistoryAdapter;

	private Handler mHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				mSMenuItem.expandActionView();
				ViewUtils.showKeybord(mSEditText, false);
				clearSearchFocus();
				getSherlockActivity().getSupportActionBar().setIcon(R.drawable.ic_logo);

				mSearchView.setOnSuggestionListener(new OnSuggestionListener() {

					@Override
					public boolean onSuggestionSelect(int position) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onSuggestionClick(int position) {
						if (mHistoryAdapter != null) {
							Cursor s = (Cursor) mHistoryAdapter.getItem(position);
							if (s.getString(1).equals(HistoryTask.SpecialRow)) {
								clearHistory();
							} else {
								String jjdm = s.getString(2);
								FundUtils.launchFundDetails(getSherlockActivity(), jjdm, 0, 0);
								Analytics.onEvent(getActivity(), Analytics.VIEW_FUND_DETAIL,
										Analytics.KEY_FROM, "历史记录");
							}
						}
						return true;
					}
				});

				mSMenuItem.setOnActionExpandListener(new OnActionExpandListener() {

					@Override
					public boolean onMenuItemActionExpand(MenuItem item) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item) {
						// TODO Auto-generated method stub
						getActivity().finish();
						return false;
					}
				});

			}
		};
	};

	public FragSearchList getmCurrFragment() {
		return mCurrFragment;
	}

	public void setmCurrFragment(FragSearchList mCurrFragment) {
		this.mCurrFragment = mCurrFragment;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case com.actionbarsherlock.R.id.abs__search_src_text:
			mSEditText.setFocusable(true);
			if (mSEditText.hasFocus() && mSEditText.getEditableText().toString().equals("")) {
				if (!mSEditText.isPopupShowing()) {
					showHistory(true);
				} else {
					mSEditText.dismissDropDown();
				}
			}

			break;

		default:
			break;
		}
	}

	protected void onAbsBuildActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		// getSherlockActivity().getSupportActionBar().setTitle("搜索");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		onAbsBuildActionBar();
		// IntentFilter iFilter = new IntentFilter("");
		// LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
		// iFilter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);
		mSMenuItem = menu.findItem(R.id.menu_search);
		mSearchView = new SearchView(getSherlockActivity().getSupportActionBar().getThemedContext());
		mSEditText = (AutoCompleteTextView) mSearchView
				.findViewById(com.actionbarsherlock.R.id.abs__search_src_text);
		mSMenuItem.setActionView(mSearchView);
		// mSEditText.setInputType(EditorInfo.TYPE_NULL);

		// mSView.setIconifiedByDefault(false);
		// mSearchView.setIconified(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint(getSherlockActivity().getString(R.string.search_hint));
		mSEditText.setOnFocusChangeListener(this);
		mSEditText.setOnClickListener(this);
		mSEditText.setThreshold(100);// 防止
		mHander.sendEmptyMessageDelayed(0, 400);

		//
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			getSherlockActivity().finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		// TODO Auto-generated method stub
		getSherlockActivity().finish();
		return true;
	}

	class MPageAdapter extends AbsFragPageAdp {

		public MPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			bundle.putString(ValConfig.IT_TYPE, Optional_Tab[position]);
			Fragment fragment = Fragment.instantiate(getActivity(), FragSearchList.class.getName(),
					bundle);
			if (mIntentType == Type_Intent_Simu) {
				if (position == 1) {
					mCurrFragment = (FragSearchList) fragment;
				}
			} else {
				if (position == 0) {
					mCurrFragment = (FragSearchList) fragment;
				}

			}
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return Optional_Tab[position];
		}

		@Override
		protected String getTag(int position) {
			// TODO Auto-generated method stub
			return Optional_Tab[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return super.getItemId(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Optional_Tab.length;
		}

	}

	public void toggleGmSm() {
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() == 0 ? 1 : 0);
	}

	protected void buildPage() {
		mViewPager.setAdapter(new MPageAdapter(mFragmentManager));
		mIndicator.setViewPager(mViewPager);
		OnPageChangeListener opcl = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				mCurrFragment = (FragSearchList) getChildFragmentManager().findFragmentByTag(
						Optional_Tab[arg0]);
				if (mSEditText != null) {
					String keyWords = mSEditText.getText().toString();
					if (mCurrFragment != null && keyWords != null) {
						mCurrFragment.getmSAdp().getFilter().filter(keyWords);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		};
		mIndicator.setOnPageChangeListener(opcl);
		if (mIntentType == Type_Intent_Simu) {
			mViewPager.setCurrentItem(1);
			mCurrFragment = (FragSearchList) getChildFragmentManager().findFragmentByTag(
					Optional_Tab[1]);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		FundUtils.exeOptional(AppFrame.getApp());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isFirstResume == false) {
		} else {
			isFirstResume = false;
		}
	}

	@Override
	protected int getFragLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_search_fund;
	}

	private class CacheTask extends AsyPoolTask<String, Void, NetWorthListBean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
			mContentLayout.setVisibility(View.GONE);
		}

		@Override
		protected NetWorthListBean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				NetWorthListBean nBeanCache = (NetWorthListBean) AppFrame.getApp().getMapObj(
						Map_Search_GM);
				if (nBeanCache == null) {
					NetWorthListBean nBeanGm = DbOperat.getInstance().searchAll(null, false);
					NetWorthListBean nBeanSm = DbOperat.getInstance().searchAll(null, true);
					nBeanGm = numbersAndlettersToEnd(nBeanGm);
					nBeanSm = numbersAndlettersToEnd(nBeanSm);
					AppFrame.getApp().getMapObj().put(Map_Search_GM, nBeanGm);
					AppFrame.getApp().getMapObj().put(Map_Search_Sm, nBeanSm);
					return nBeanGm;
				} else {
					return nBeanCache;
				}
			} catch (WrapException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(NetWorthListBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.GONE);
			mContentLayout.setVisibility(View.VISIBLE);
			if (result != null) {
				// mListView.setAdapter(adapter)
				buildPage();
			}

		}

		private NetWorthListBean numbersAndlettersToEnd(NetWorthListBean nbBean) {
			List<NetWorthBean> listTemp = new ArrayList<NetWorthBean>();
			ArrayList<Integer> numIndexs = new ArrayList<Integer>();

			if (nbBean != null && nbBean.size() > 0) {
				String matchsFirstIsChar = "[0-9]{1}.*";
				for (int i = 0; i < nbBean.size(); i++) {
					NetWorthBean n = nbBean.getItem(i);
					String py = n.getPinyin();
					if (py.matches(matchsFirstIsChar)) {
						numIndexs.add(i);
					} else {
						break;
					}
				}

				for (int i = numIndexs.size() - 1; i >= 0; i--) {
					listTemp.add(nbBean.getItem(numIndexs.get(i)));
					nbBean.remove(i);
				}

				for (int i = 0; i < listTemp.size(); i++) {
					nbBean.addItem(listTemp.get(i));
				}
			}

			return nbBean;
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		// TODO Auto-generated method stub
		mViewPager = (CanScrollViewPager) root.findViewById(R.id.viewpager);
		mContentLayout = (LinearLayout) root.findViewById(R.id.content_lay);
		mIndicator = (PagerSlidingTabStrip) root.findViewById(R.id.indicator);
		mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar);
		mFragmentManager = getChildFragmentManager();
		NetWorthListBean nBeanCacheGm = (NetWorthListBean) AppFrame.getApp().getMapObj(
				Map_Search_GM);
		bundle = bundle == null ? getArguments() : bundle;

		mIntentType = bundle.getInt(ValConfig.IT_TYPE);

		if (nBeanCacheGm == null) {
			new CacheTask().execute(false);
		} else {
			mProgressBar.setVisibility(View.GONE);
			mContentLayout.setVisibility(View.VISIBLE);
			buildPage();
		}

	}

	/**
	 * add to search history
	 * 
	 * @param jjdm
	 */
	public void addToHistory(String jjdm) {
		new HistoryTask().execute(false, HistoryTask.SqlTypeAdd, jjdm);
	}

	public void queryHistroy() {
		new HistoryTask().execute(false, HistoryTask.SqlTypeQuery);
	}

	public void showHistory(boolean isReQuery) {
		if (isReQuery) {
			queryHistroy();
		}
	}

	public void dismissHistory() {
		mSEditText.dismissDropDown();
	}

	public void clearHistory() {
		new HistoryTask().execute(false, HistoryTask.SqlTypeClear);
	}

	public void clearSearchFocus() {
		if (mSearchView != null) {
			mSearchView.clearFocus();
		}
	}

	public void setViewPagerCanHScroll(final boolean s) {
		mViewPager.setCanHScroll(s);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		EditText e = (EditText) v;
		String queryString = e.getEditableText().toString();
		if (hasFocus && TextUtils.isEmpty(queryString)) {
			showHistory(true);
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		mSearchView.clearFocus();
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		// mSEditText
		if (mCurrFragment != null && mCurrFragment.getmSAdp() != null) {
			LogUtils.d("search" + "currfragment2--" + mCurrFragment);
			mCurrFragment.getmSAdp().getFilter().filter(newText);
			dismissHistory();
		}
		return true;
	}

	public CanScrollViewPager getmViewPager() {
		return mViewPager;
	}

	public void setmViewPager(CanScrollViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	public AutoCompleteTextView getmSEditText() {
		return mSEditText;
	}

	public void setmSEditText(AutoCompleteTextView mSEditText) {
		this.mSEditText = mSEditText;
	}

	private class HistoryAdapter extends CursorAdapter {

		public HistoryAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			TextView view = (TextView) arg0.findViewById(R.id.name);
			ImageView hisFlag = (ImageView) arg0.findViewById(R.id.del_flag);
			String jjmc = arg2.getString(3);
			String flag = arg2.getString(1);

			if (flag.equals(HistoryTask.SpecialRow)) {
				hisFlag.setVisibility(View.INVISIBLE);
				view.setTextColor(getResources().getColor(R.color.text_click_bg));
			} else {
				view.setTextColor(getResources().getColor(R.color.text_title));
				hisFlag.setVisibility(View.VISIBLE);
			}
			view.setText(jjmc);
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return LayoutInflater.from(arg0).inflate(R.layout.com_list_search_history_item, arg2, false);
		}
	}

	protected final class HistoryTask extends AsyPoolTask<String, Void, MergeCursor> {
		public static final String SqlTypeAdd = "1";
		public static final String SqlTypeClear = "2";
		public static final String SqlTypeQuery = "3";
		public static final String SpecialRow = "howbuybuy";
		// add del query
		private String mType;

		@Override
		protected MergeCursor doInBackground(String... params) {
			// TODO Auto-generated method stub
			mType = params[0];
			if (params[0].equals(SqlTypeAdd)) {
				DbUtils.exeSql(buildBeforAddSql(params[1]), false);// 先删除再添加
				DbUtils.exeSql(buildAddSql(params[1]), false);
			} else if (params[0].equals(SqlTypeClear)) {
				DbUtils.exeSql(new SqlExeObj(buildDeleteSql()), false);
			} else {
				try {
					Cursor cursor = DbUtils.query(buildQuerySql(), null, false);
					if (cursor != null && cursor.moveToFirst()) {
						MatrixCursor m = new MatrixCursor(new String[] { "_id", "subkey", "code",
								"name" });
						m.addRow(new String[] { "100", SpecialRow, "null", "清除浏览记录" });
						return new MergeCursor(new Cursor[] { cursor, m });
					}
				} catch (WrapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(MergeCursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (mType.equals(SqlTypeQuery) && result != null && getActivity() != null) {
				mHistoryAdapter = new HistoryAdapter(getSherlockActivity(), result, true);
				mSearchView.setSuggestionsAdapter(mHistoryAdapter);
				if (mSearchView.hasFocus()) {
					mSEditText.showDropDown();
				}
				// pop("mHistoryAdapter", false);
				// mSEditText.dismissDropDown();
			}
		}

		private String buildQuerySql() {
			StringBuilder sb = new StringBuilder();
			sb.append("select _id,subkey,code,name from (select _id,subkey,code,name,date from tb_common a,fundsinfo b where a.subkey=b.code");
			sb.append(" and a.key='" + CommenDb_Key + "' order by a.date desc limit 5)");
			sb.append(" order by date desc");
			LogUtils.d("sql", sb.toString());
			return sb.toString();
		}

		private SqlExeObj buildAddSql(String jjdm) {
			StringBuilder sb = new StringBuilder();
			String date = String.valueOf(System.currentTimeMillis());
			sb.append("insert into tb_common (key,subkey,date) values('" + CommenDb_Key + "',?,?)");
			return new SqlExeObj(sb.toString(), new Object[] { jjdm, date });
		}

		private SqlExeObj buildBeforAddSql(String jjdm) {
			StringBuilder sb = new StringBuilder();
			sb.append("delete from tb_common where key = '" + CommenDb_Key + "' and subkey=?");
			return new SqlExeObj(sb.toString(), new Object[] { jjdm });
		}

		private String buildDeleteSql() {
			StringBuilder sb = new StringBuilder("delete from tb_common where key='" + CommenDb_Key
					+ "'");
			return sb.toString();
		}

	}

}
