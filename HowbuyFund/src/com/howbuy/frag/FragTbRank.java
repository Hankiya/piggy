package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.adp.FundRankAdp.RankHolder;
import com.howbuy.adp.RankSortAdp;
import com.howbuy.adp.RankSortAdp.SortTypeHolder;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.FundConfig.SortType;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.control.PagerAnimSlidTab;
import com.howbuy.entity.InitUpdateInfs;
import com.howbuy.entity.RequestState;
import com.howbuy.lib.adp.AbsFragPageAdp;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.Receiver;

public class FragTbRank extends AbsHbFrag implements OnPageChangeListener, OnItemClickListener {
	private static final int GUPIAO_HAS_SHOW_TYPE_SETTING = 1;
	private static final int GUPIAO_HAS_SWITCHED = 2;
	public static final String SFKEY_RANK_TB_MERGE_DLG = "fund_rank_merge_dlg";
	public static final String SFKEY_RANK_TB = "fund_rank_tabs";
	public static final String SFKEY_RANK_TB_CURRENT = "fund_rank_current";
	private ViewPager mViewPage = null;
	private PagerAnimSlidTab mIndicator = null;
	private CheckHeadText mTvFixedMore = null;
	private TextView mTvColumn = null;
	private PopupWindow mPop = null;
	private RankSortAdp mSortAdapter = null;
	private FragRankPageAdp mRankAdp = null;
	private int mSelectPage = 0, mMergeFlag = 0;
	private ArrayList<FundType> mFunds = new ArrayList<FundConfig.FundType>(2);
	private ArrayList<Fragment> mFrags = new ArrayList<Fragment>(3);
	private FundConfig mFundConfig = null;
	private boolean mColumnShow = true;
	private Animation mRightInAnim, mRightOutAnim;
	private RequestState mStateHuobi = new RequestState(60000);// 5mins.
	private RequestState mStateOpen = new RequestState(60000);// 5mins.
	private RequestState mStateSimu = new RequestState(60000);// 5mins.
	private int mShowDialogFlag = 0;

	private Animation getRightAnim(boolean inAnim, int duration) {
		Animation anim = null;
		if (inAnim) {
			if (mRightInAnim == null) {
				mRightInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
						Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				mRightInAnim.setFillAfter(true);
			}
			anim = mRightInAnim;
		} else {
			if (mRightOutAnim == null) {
				mRightOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				mRightOutAnim.setFillAfter(true);
			}
			anim = mRightOutAnim;
		}
		anim.setDuration(duration);
		return anim;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mFundConfig == null) {
			mFundConfig = FundConfig.getConfig();
		}
		mMergeFlag = mFundConfig.getFlag()
				& (FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL | FundConfig.RANK_FLAG_MERGE_STOCK_MIX);
		mShowDialogFlag = AppFrame.getApp().getsF().getInt(SFKEY_RANK_TB_MERGE_DLG, 0);
		updateNetValue(mFundConfig.getType(FundConfig.TYPE_ALL));
	}

	public Fragment getCurFrag() {
		if (mSelectPage >= 0 && mSelectPage < mFrags.size()) {
			return mFrags.get(mSelectPage);
		}
		return null;
	}

	public FundType getCurType() {
		if (mSelectPage >= 0 && mSelectPage < mFunds.size()) {
			return mFunds.get(mSelectPage);
		}
		return null;
	}

	private FragRankPageAdp getRankPageAdp(boolean forceCreated) {
		if (mFunds.size() == 0) {
			FundConfig con = FundConfig.getConfig();
			SharedPreferences sf = AppFrame.getApp().getsF();
			String tbs = sf.getString(SFKEY_RANK_TB, null);
			if (tbs == null) {
				String val = null;
				FundType f = con.getType(FundConfig.TYPE_ALL);
				val = f.ClassType;
				mFunds.add(f);
				f = con.getType(FundConfig.TYPE_HUOBI);
				val = val + "#" + f.ClassType;
				mFunds.add(f);
				sf.edit().putString(SFKEY_RANK_TB, val).commit();
			} else {
				String[] tb = tbs.split("#");
				mFunds.add(con.getType(tb[0]));
				mFunds.add(con.getType(tb[1]));
			}
			mFrags.add(null);
			mFrags.add(null);
			mFrags.add(null);
		}
		if (mRankAdp == null || forceCreated) {
			mRankAdp = new FragRankPageAdp(getChildFragmentManager());
		}
		return mRankAdp;
	}

	private RankSortAdp getSortAdp() {
		if (mSortAdapter == null) {
			mSortAdapter = new RankSortAdp(getSherlockActivity());
			mSortAdapter
					.setSelectedDrawable(getResources().getDrawable(R.drawable.background_tabs));
		}
		return mSortAdapter;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		findAllViews(root);
		mViewPage.setOffscreenPageLimit(4);
		mViewPage.setAdapter(getRankPageAdp(false));
		mIndicator.setViewPager(mViewPage);
		mIndicator.setOnPageChangeListener(this);
		mIndicator.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				mIndicator.copyTabText(mTvFixedMore, 2);
				mTvColumn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTvFixedMore.getTextSize());
				mTvColumn.setTextColor(mTvFixedMore.getTextColors());
				mTvColumn.setTypeface(mTvFixedMore.getTypeface());
				mTvColumn.getLayoutParams().height = mIndicator.getHeight();
				if (Build.VERSION.SDK_INT < 16) {
					mIndicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					mIndicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
			}
		});
		mSelectPage = AppFrame.getApp().getsF().getInt(SFKEY_RANK_TB_CURRENT, 0);
		if (mSelectPage < mFunds.size()) {
			SortType s = mFunds.get(mSelectPage).getSortType();
			if (s != null) {
				mTvColumn.setText(s.SortName);
			}
		}
		mViewPage.setCurrentItem(mSelectPage, false);
		final String requireType = getArguments().getString(ValConfig.IT_TYPE);
		if (!StrUtils.isEmpty(requireType)) {
			mViewPage.postDelayed(new Runnable() {

				@Override
				public void run() {
					onFundTypeClick(mFundConfig.getType(requireType));
				}
			}, 500);

		}
	}

	@SuppressLint("NewApi")
	private void findAllViews(View root) {
		mViewPage = (ViewPager) root.findViewById(R.id.pager);
		mIndicator = (PagerAnimSlidTab) root.findViewById(R.id.indicator);
		mTvColumn = (TextView) root.findViewById(R.id.tv_rank_column);
		mTvFixedMore = (CheckHeadText) root.findViewById(R.id.tv_fund_type_more);
	}

	@Override
	public void onPause() {
		super.onPause();
		AppFrame.getApp().getsF().edit().putInt(SFKEY_RANK_TB_CURRENT, mSelectPage).commit();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_tb_rank, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (mFundConfig == null) {
			mFundConfig = FundConfig.getConfig();
		}
		MenuItem it = menu.findItem(R.id.menu_optional);
		if (it != null) {
			it.setChecked(mFundConfig.hasFlag(FundConfig.RANK_FLAG_OPTIONAL));
		}
		it = menu.findItem(R.id.menu_onsale);
		if (it != null) {
			it.setChecked(mFundConfig.hasFlag(FundConfig.RANK_FLAG_ONSALE));
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			FundUtils.launchFundSearch(this, 0, 0);
			setRankNeedReloadOnResume(true, false);
			break;
		case R.id.menu_rank_setting:
			handRankSetting(item.getTitle().toString());
			break;
		case R.id.menu_optional:
			handRankOption(item, FundConfig.RANK_FLAG_OPTIONAL);
			break;
		case R.id.menu_onsale:
			handRankOption(item, FundConfig.RANK_FLAG_ONSALE);
			break;
		case R.id.menu_more:
			break;
		case R.id.menu_setting:
			break;
		case R.id.menu_about:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handled = true;
		switch (v.getId()) {
		case R.id.tv_rank_column:
			handRankSort(v);
			break;
		case R.id.cb_collect:
			handCollectFund(v);
			break;
		default:
			handled = false;
		}
		return handled ? true : super.onXmlBtClick(v);
	}

	private void handRankOption(MenuItem item, int flag) {
		Analytics.onEvent(getSherlockActivity(), Analytics.SWITCH_CLASSIFY_OPTION,
				Analytics.KEY_SWITCH_NAME, item.getTitle().toString());
		item.setChecked(!item.isChecked());
		if (item.isChecked()) {
			mFundConfig.addFlag(flag);
		} else {
			mFundConfig.subFlag(flag);
		}
		mFundConfig.saveFlag(AppFrame.getApp().getsF());
		if (mSelectPage < 2) {
			((FragRankList) mFrags.get(mSelectPage)).onFundSettingChanged(true);
			((FragRankList) mFrags.get(1 - mSelectPage)).onFundSettingChanged(true);
		} else {
			((FragRankList) mFrags.get(1)).onFundSettingChanged(true);
			((FragRankList) mFrags.get(0)).onFundSettingChanged(true);
		}
	}

	private void handRankSetting(String title) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, String.valueOf(title));
		FragOpt opt = new FragOpt(FragRankSettingList.class.getName(), b, FragOpt.FRAG_POPBACK);
		opt.setTargetFrag(FragTbRank.this, 1);
		((AtyEmpty) getSherlockActivity()).switchToFrag(opt);
		if ((GUPIAO_HAS_SHOW_TYPE_SETTING & mShowDialogFlag) == 0) {
			mShowDialogFlag |= GUPIAO_HAS_SHOW_TYPE_SETTING;
			AppFrame.getApp().getsF().edit().putInt(SFKEY_RANK_TB_MERGE_DLG, mShowDialogFlag)
					.commit();
		}
	}

	private void handCollectFund(View v) {
		Object obj = v.getTag();
		if (obj instanceof RankHolder) {
			RankHolder hod = (RankHolder) obj;
			int result = hod.changeView(0);
			if (result != 0) {
				Analytics.onEvent(getSherlockActivity(), result == 1 ? Analytics.ADD_CUSTOM_FUNDS
						: Analytics.DELETE_CUSTOM_FUNDS, Analytics.KEY_FROM, "排行列表");
			}
			int fragIndex = 1 - mSelectPage;
			if (fragIndex >= 0 && fragIndex < 2) {
				((FragRankList) mFrags.get(fragIndex)).onAddOption(hod.mItem.getJjdm(),
						hod.mItem.getXunan());
			}
		}
	}

	private void handRankSort(View v) {
		if (mSelectPage != 2) {
			RankSortAdp adp = getSortAdp().setSelected(mFunds.get(mSelectPage).Selected);
			ArrayList<SortType> sort = mFunds.get(mSelectPage).getSortTypes();
			adp.setItems(sort, false);
			int n = adp.getCount();
			if (n > 0) {
				showRankDialog(v, adp, /* maxStrLenIndex */0);
			}
		}
	}

	private void showRankDialog(View anchor, ListAdapter adapter, int maxWidthIndex) {
		final ListView lv = new ListView(getSherlockActivity());
		lv.setOnItemClickListener(this);
		lv.setAdapter(adapter);
		lv.setBackgroundColor(getResources().getColor(R.color.window_bg));
		mPop = new PopupWindow(lv, (int) (SysUtils.getDensity(getSherlockActivity()) * 110),
				WindowManager.LayoutParams.WRAP_CONTENT);
		mPop.setFocusable(true);
		mPop.setBackgroundDrawable(getResources().getDrawable(
				com.actionbarsherlock.R.drawable.abs__menu_dropdown_panel_holo_light));
		mPop.setTouchInterceptor(new OnTouchListener() {
			private boolean isOutsideTouch(MotionEvent event) {
				float x = event.getX();
				float y = event.getY();
				if (x < 0 || y < 0 || x > lv.getWidth() || y > lv.getHeight()) {
					return true;
				}
				return false;
			}

			public boolean onTouch(View v, MotionEvent event) {
				if (isOutsideTouch(event)) {
					return false;
				}
				return false;
			}
		});
		mPop.showAsDropDown(anchor, (int) SysUtils.getDensity(getSherlockActivity()) * -3,
				-(int) SysUtils.getDensity(getSherlockActivity()) * 8);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_tb_rank;
	}

	public ArrayList<FundType> getTabFunds() {
		return mFunds;
	}

	class FragRankPageAdp extends AbsFragPageAdp {
		public FragRankPageAdp(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment frag = null;
			if (position == mFunds.size()) {
				frag = Fragment.instantiate(getSherlockActivity(),
						FragRankTypeList.class.getName(), null);
				frag.setTargetFragment(FragTbRank.this, position);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString(SFKEY_RANK_TB, mFunds.get(position).ClassType);
				frag = Fragment.instantiate(getSherlockActivity(), FragRankList.class.getName(),
						bundle);
				frag.setTargetFragment(FragTbRank.this, position);
			}
			mFrags.set(position, frag);
			return frag;
		}

		@Override
		public int getCount() {
			return mFunds.size() + 1;
		}

		@Override
		protected String getTag(int position) {
			if (position == mFunds.size()) {
				return "FragRankTypeList";
			} else {
				return "FragRankList" + position;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == mFunds.size()) {
				return "更多";
			}
			return mFunds.get(position).FundName;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int pos) {
		mSelectPage = pos;
		if (mSelectPage < mFunds.size()) {
			FundType t = mFunds.get(mSelectPage);
			SortType s = t.getSortType();
			Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_ORDER_PART, Analytics.KEY_TYPE,
					t.FundName, Analytics.KEY_PART, s.SortName);
			if (s != null) {
				mTvColumn.setText(s.SortName);
				if (!mColumnShow) {
					mColumnShow = true;
					mTvColumn.startAnimation(getRightAnim(true, 200));
				}
			}
		} else {
			if (mColumnShow) {
				mColumnShow = false;
				mTvColumn.startAnimation(getRightAnim(false, 200));
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = parent.getAdapter();
		if (obj instanceof RankSortAdp) {
			SortType sortType = ((SortTypeHolder) view.getTag()).mItem;
			mTvColumn.setText(sortType.SortName);
			Fragment frag = getCurFrag();
			if (frag instanceof FragRankList) {
				((FragRankList) frag).setFundSortType(null, position);
				Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_ORDER_PART,
						Analytics.KEY_TYPE, mFunds.get(mSelectPage).FundName, Analytics.KEY_PART,
						sortType.SortName);
			}
			if (mPop != null) {
				mPop.dismiss();
			}
		}
	}

	private void saveFundTypeConfig(int flag) {
		StringBuffer sb = new StringBuffer(32);
		for (int i = 0; i < mFunds.size(); i++) {
			sb.append('#').append(mFunds.get(i).ClassType);
		}
		String value = sb.deleteCharAt(0).toString();
		AppFrame.getApp().getsF().edit().putString(SFKEY_RANK_TB, value).commit();
		notifyRankTypeChanged(flag);
	}

	private void notifyRankTypeChanged(int flag) {
		if (mFrags.size() > 2 && mFrags.get(2) != null) {
			((FragRankTypeList) mFrags.get(2)).onFundSettingChanged(flag);
		}
	}

	public void onAddOptional(FragRankList from, String code, int xuanType) {
		FragRankList frag = null;
		for (int i = 0; i < mFunds.size(); i++) {
			frag = (FragRankList) mFrags.get(i);
			// if (!frag.equals(from)) {
			frag.onAddOption(code, xuanType);
			// }
		}
	}

	public void onFundTypeClick(FundType fundType) {

		int n = mFunds.size();
		for (int i = 0; i < n; i++) {
			if (mFunds.get(i) == fundType) {
				mViewPage.setCurrentItem(i, true);
				return;
			}
		}
		mFunds.set(0, mFunds.get(1));
		mFunds.set(1, fundType);
		saveFundTypeConfig(0);
		mIndicator.startTransAnim(fundType.FundName, 300);
		if (mViewPage.getCurrentItem() == 1) {
			onPageSelected(1);
		}
		for (int i = n - 1; i >= 0; i--) {
			Fragment frag = mFrags.get(i);
			if (frag instanceof FragRankList) {
				((FragRankList) frag).setFundSortType(mFunds.get(i).ClassType, -1);
			}
		}
		if (GlobalApp.getApp().getNetType() > 1) {
			updateNetValue(fundType);
		}
		if ((mShowDialogFlag & GUPIAO_HAS_SWITCHED) == 0) {
			if (fundType.equalType(FundConfig.TYPE_GUPIAO)) {
				mShowDialogFlag |= GUPIAO_HAS_SWITCHED;
				AppFrame.getApp().getsF().edit().putInt(SFKEY_RANK_TB_MERGE_DLG, mShowDialogFlag)
						.commit();
				if ((mShowDialogFlag & GUPIAO_HAS_SHOW_TYPE_SETTING) == 0) {
					switchTypeSetting();
				}
			}
		}

	}

	private void updateNetValue(FundType fundType) {
		int requestFlag = 0;
		int curType = fundType.DataType;
		if (curType == 0) {
			if (mStateOpen.needRequest(true)) {
				requestFlag |= InitUpdateInfs.UPDATE_KFS;
			}
			if (mStateHuobi.needRequest(true)) {
				requestFlag |= InitUpdateInfs.UPDATE_HBS;
			}
		} else if (curType == 1) {
			if (mStateOpen.needRequest(true)) {
				requestFlag = InitUpdateInfs.UPDATE_KFS;
			}
		} else if (curType == 2) {
			if (mStateHuobi.needRequest(true)) {
				requestFlag = InitUpdateInfs.UPDATE_HBS;
			}
		} else if (curType == 3) {
			if (mStateSimu.needRequest(true)) {
				requestFlag = InitUpdateInfs.UPDATE_SIM;
			}
		}
		if (requestFlag != 0) {
			if (InitUpdateInfs.hasTask() == false) {
				ReqOpt opt = new ReqOpt(0, null, AppService.HAND_UPDATE_NETVALUE_BATCH);
				opt.setArgInt(requestFlag);
				d("updateNetValue", fundType.toString() + ",requestFlag=" + requestFlag);
				AppFrame.getApp().getGlobalServiceMger().executeTask(opt, null);
			}
		}
	}

	/*
	 * 这种是没有向上兼容,直接去掉,用默认填充. public void onFundSettingChanged() { int flag =
	 * checkMergeFlag(); FundType type1 = null, type2 = null; FragListRank frag1
	 * = (FragListRank) mFrags.get(0); FragListRank frag2 = (FragListRank)
	 * mFrags.get(1); ((FragListRankType)
	 * mFrags.get(2)).onFundSettingChanged(flag); boolean typechaged = false; if
	 * (flag != 0) { type1 = mFunds.get(0); type2 = mFunds.get(1); String type12
	 * = type1.ClassType + "_" + type2.ClassType; boolean huobi =
	 * type12.contains(FundConfig.TYPE_HUOBI); boolean licai =
	 * mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL) &&
	 * type12.contains(FundConfig.TYPE_LICAI); boolean gupiao =
	 * type12.contains(FundConfig.TYPE_GUPIAO); boolean hunhe =
	 * mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_STOCK_MIX) &&
	 * type12.contains(FundConfig.TYPE_HUNHE); FundType t_all =
	 * mFundConfig.getType(FundConfig.TYPE_ALL); FundType t_huobi =
	 * mFundConfig.getType(FundConfig.TYPE_HUOBI); if (typechaged = (licai ||
	 * hunhe)) {// 理财和混合有一个存在且各自有合并项 if (licai && hunhe) {//
	 * 两个需要合并的子项目都在,.合并后两个tab为空. type1 = t_all; type2 = t_huobi; } else { if
	 * (licai && huobi) {// lican huobi子父同在 合并后licai的tab为空. if
	 * (type1.equals(huobi)) { type2 = t_all; } else { type1 = t_all; }
	 * 
	 * } else if (hunhe && gupiao) {// hunhe gupiao子父同在合并后hunhe的tab为空. if
	 * (type1.equals(gupiao)) { type2 = t_all; } else { type1 = t_all; } } else
	 * {// 去掉子的,增加全部和贷币之一. if (licai) {
	 * if(type1.equalType(FundConfig.TYPE_LICAI)){ if(type2.equals(t_all)){
	 * type1=t_huobi; }else{ type1=t_all; } }else{ if(type1.equals(t_all)){
	 * type2=t_huobi; }else{ type2=t_all; } } } else {
	 * if(type1.equalType(FundConfig.TYPE_HUNHE)){ if(type2.equals(t_all)){
	 * type1=t_huobi; }else{ type1=t_all; } }else{ if(type1.equals(t_all)){
	 * type2=t_huobi; }else{ type2=t_all; } } } } } if (onTypeChaged(frag1,
	 * type1, 0) || onTypeChaged(frag2, type2, 1)) {
	 * mIndicator.notifyDataSetChanged(); saveFundTypeConfig(); } } } else {
	 * onNoTypeChanged(frag1, frag2); } }
	 */
	private boolean onTypeChaged(FragRankList frag, FundType newType, int i) {
		FundType curType = mFunds.get(i);
		if (curType.equalType(newType.ClassType)) {
			frag.onFundSettingChanged(false);
			return false;
		} else {
			mFunds.set(i, newType);
			frag.setFundSortType(newType.ClassType, -1);
			return true;
		}
	}

	private void onNoTypeChanged(FragRankList frag1, FragRankList frag2) {
		if (mSelectPage == 0) {
			frag1.onFundSettingChanged(false);
			frag2.onFundSettingChanged(false);
		} else {
			frag2.onFundSettingChanged(false);
			frag1.onFundSettingChanged(false);
		}
	}

	private void setRankNeedReloadOnResume(boolean needReloaded, boolean force) {
		for (int i = 0; i < mFunds.size(); i++) {
			((FragRankList) mFrags.get(i)).setNeedReloadedOnResume(needReloaded, force);
		}
	}

	private int checkMergeFlag() {
		int flag = FundConfig.getConfig().getFlag()
				& (FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL | FundConfig.RANK_FLAG_MERGE_STOCK_MIX);
		int gap = flag - mMergeFlag;
		mMergeFlag = flag;
		return gap;
	}

	public void onFundSettingChanged() {
		int flag = checkMergeFlag();
		FundType type1 = null, type2 = null;
		FragRankList frag1 = (FragRankList) mFrags.get(0);
		FragRankList frag2 = (FragRankList) mFrags.get(1);
		boolean typechaged = false;
		if (flag != 0) {
			type1 = mFunds.get(0);
			type2 = mFunds.get(1);
			String type12 = type1.ClassType + "_" + type2.ClassType;
			boolean huobi = type12.contains(FundConfig.TYPE_HUOBI);
			boolean licai = mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_CURRENCY_FINANCIAL)
					&& type12.contains(FundConfig.TYPE_LICAI);
			boolean gupiao = type12.contains(FundConfig.TYPE_GUPIAO);
			boolean hunhe = mFundConfig.hasFlag(FundConfig.RANK_FLAG_MERGE_STOCK_MIX)
					&& type12.contains(FundConfig.TYPE_HUNHE);
			if (typechaged = (licai || hunhe)) {
				boolean allFundChaged = true;
				if (licai) {
					if (huobi) {// licai huobi 同在需要合并为huobi,增加all.
						FundType f = mFundConfig.getType(FundConfig.TYPE_ALL);
						if (type2.ClassType.equals(FundConfig.TYPE_HUOBI)) {
							type1 = f;
							allFundChaged = false;
						} else {
							type2 = type1;
							type1 = f;
						}
						if (allFundChaged) {
							frag1.setFundSortType(type1.ClassType, -1);
							frag2.setFundSortType(type2.ClassType, -1);
						} else {
							frag2.onFundSettingChanged(false);
							frag1.setFundSortType(type1.ClassType, -1);
						}
						if (mSelectPage != 1) {
							mViewPage.setCurrentItem(1, true);
						}
					} else {// licai 要转变为huobi.
						if (type1.ClassType.equals(FundConfig.TYPE_LICAI)) {
							type1 = mFundConfig.getType(FundConfig.TYPE_HUOBI);
							frag1.setFundSortType(type1.ClassType, -1);
						} else {
							type2 = mFundConfig.getType(FundConfig.TYPE_HUOBI);
							frag2.setFundSortType(type2.ClassType, -1);
						}
					}
				}
				if (hunhe) {
					if (gupiao) { // hunhe gupiao 同在需要合并为gupiao,增加all.
						FundType f = mFundConfig.getType(FundConfig.TYPE_ALL);
						if (type2.ClassType.equals(FundConfig.TYPE_GUPIAO)) {
							type1 = f;
							allFundChaged = false;
						} else {
							type2 = type1;
							type1 = f;
						}
						if (allFundChaged) {
							frag1.setFundSortType(type1.ClassType, -1);
							frag2.setFundSortType(type2.ClassType, -1);
						} else {
							frag2.onFundSettingChanged(false);
							frag1.setFundSortType(type1.ClassType, -1);
						}
						if (mSelectPage != 1) {
							mViewPage.setCurrentItem(1, true);
						}
					} else {// hunhe 要转变为gupiao.
						if (type1.ClassType.equals(FundConfig.TYPE_HUNHE)) {
							type1 = mFundConfig.getType(FundConfig.TYPE_GUPIAO);
							frag1.setFundSortType(type1.ClassType, -1);
						} else {
							type2 = mFundConfig.getType(FundConfig.TYPE_GUPIAO);
							frag2.setFundSortType(type2.ClassType, -1);
						}
					}
				}
				mFunds.set(0, type1);
				mFunds.set(1, type2);
				mIndicator.notifyDataSetChanged();
				saveFundTypeConfig(flag);
			}
			notifyRankTypeChanged(flag);
			onNoTypeChanged(frag1, frag2);
		} else {
			onNoTypeChanged(frag1, frag2);
		}
	}

	@Override
	public boolean shouldEnableLocalBroadcast() {
		return true;
	}

	@Override
	public void onReceiveBroadcast(int from, Bundle b) {
		StringBuffer sb = new StringBuffer();
		boolean launch = false, synopt = false, netval = false;
		netval = Receiver.FROM_UPDATE_NETVAL_BETCH == from;
		if (!netval) {
			launch = Receiver.FROM_UPDATE_LAUNCH == from;
		}
		if (!launch) {
			synopt = Receiver.FROM_OPTIONAL_SYNC == from;
		}
		boolean needUpdate = launch || synopt || netval;
		sb.append("from=").append(from)
				.append(",launch=" + launch + ",netupdate=" + netval + ",synopt=" + synopt);
		if (launch || netval) {
			int fail = b.getInt(ValConfig.IT_TYPE, 0);
			int success = b.getInt(ValConfig.IT_ID, 0);
			needUpdate = false;
			if (success != 0) {
				if (InitUpdateInfs.UPDATE_KFS == (InitUpdateInfs.UPDATE_KFS & success)) {
					mStateOpen.setResult(needUpdate = true);
				}
				if (InitUpdateInfs.UPDATE_HBS == (InitUpdateInfs.UPDATE_HBS & success)) {
					mStateHuobi.setResult(needUpdate = true);
				}
				if (InitUpdateInfs.UPDATE_SIM == (InitUpdateInfs.UPDATE_SIM & success)) {
					mStateSimu.setResult(needUpdate = true);
				}
			}
			if (fail != 0) {
				if (InitUpdateInfs.UPDATE_KFS == (InitUpdateInfs.UPDATE_KFS & fail)) {
					mStateOpen.setResult(needUpdate = false);
				}
				if (InitUpdateInfs.UPDATE_HBS == (InitUpdateInfs.UPDATE_HBS & fail)) {
					mStateHuobi.setResult(needUpdate = false);
				}
				if (InitUpdateInfs.UPDATE_SIM == (InitUpdateInfs.UPDATE_SIM & fail)) {
					mStateSimu.setResult(needUpdate = false);
				}
			}
			sb.append("state open=").append(mStateOpen);
			sb.append(",state huobi=").append(mStateHuobi);
		}
		if (needUpdate) {
			sb.append(",last update=").append(needUpdate);
			setRankNeedReloadOnResume(false, true);
		}
		d("onReceiveBroadcast", sb.toString());
	}

	private void switchTypeSetting() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle("分类选项");
		builder.setMessage("混合型已合并入股票型排行，可在分类选项中修改。");
		builder.setNegativeButton("去修改", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				handRankSetting("分类选项");
			}
		});
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}
}
