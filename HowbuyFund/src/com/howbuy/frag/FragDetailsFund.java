package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.adp.PerformaceAdp;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.AppFrame;
import com.howbuy.component.CardDrawable;
import com.howbuy.component.ScreenHelper;
import com.howbuy.component.ScreenHelper.IScreenChanged;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.control.DetailsCharPagerLayout;
import com.howbuy.control.ExpandGroup;
import com.howbuy.control.ExpandGroup.IExpandChanged;
import com.howbuy.control.HolderFundView;
import com.howbuy.control.QuickReturnFooter;
import com.howbuy.datalib.fund.AAParFtenHeavyHoldingInfo;
import com.howbuy.datalib.fund.AAParFtenSimuBasicInf;
import com.howbuy.datalib.fund.AAParFundTradeInfo;
import com.howbuy.datalib.fund.ParFtenCompanyInfo;
import com.howbuy.datalib.fund.ParFtenManagerList;
import com.howbuy.datalib.fund.ParFundPerformance;
import com.howbuy.datalib.fund.ParFundsNetValueByIDs;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.AtyInfs;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.Performace;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.ShareHelper;
import com.howbuy.wireless.entity.protobuf.CompanyInfoProto.CompanyInfo;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;
import com.howbuy.wireless.entity.protobuf.FundTradeInfoProtos.FundTradeInfo;
import com.howbuy.wireless.entity.protobuf.ManagerInfoListProto.ManagerInfoList;
import com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo;
import com.howbuy.wireless.entity.protobuf.PerformanceInfoProto.PerformanceInfo;
import com.howbuy.wireless.entity.protobuf.SimuBasicInfoProto.SimuBasicInfo;
import com.howbuy.wireless.entity.protobuf.ZccgInfoListProtos.ZccgInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public class FragDetailsFund extends AbsHbFrag implements IReqNetFinished, IExpandChanged,
		IScreenChanged, OnPageChangeListener {
	private static final int ATY_RESULT_LAND = 1;
	private static final int HAND_FUND_UPDATE = 2;// 更净值.
	private static final int HAND_FUND_PERFORMACE = 4;// 业绩表现.
	private static final int HAND_FUND_MANAGERLIST = 8;// 基金经理列表.
	private static final int HAND_FUND_COMPANYINF = 16;// 公司信息.
	private static final int HAND_FUND_HEAVYHOLD = 32;// 重创持股.
	private static final int HAND_FUND_TRADESTATE = 64;// 基金申购状态。
	private static final int HAND_FUND_SIMUINF = 128;// 好买评级私募信息。公司地，级数。

	private QuickReturnFooter mQuickReturn = null;
	private TextView mTvFundTitle, mTvFundCode, mTvFundType, mTvTradeState, mTvSubmittt;
	private TextView mTvFundDate, mTvFundValue, mTvFundValueType, mTvFundIncrease,
			mTvFundIncreaseType;
	private ViewStub mVSLevel;
	private RatingBar mRatBar;
	private CheckHeadText mTvFundRate;
	private PerformaceAdp mPerAdpFixed, mPerAdpExpand;
	private ArrayList<Performace> mPerformace = null;
	private ListView mLvFixed, mLvExpand = null;
	private ExpandGroup mExpand = null;
	private HolderFundView mFundHeavy;
	private HolderFundView mFundCompany;
	private HolderFundView mFundManager;
	private View mExpnadIndicator = null;
	private Animation mRotateUpAnim, mRotateDownAnim;
	private DetailsCharPagerLayout mCharView = null;
	private int mRequestArg = -1, mCurrentCharPage = -1;
	private CharProvider mCharProvider = new CharProvider(null);
	private ScreenHelper mSensorMgr = null;
	private boolean mForceScreen = false;
	private boolean mHasExpanded = false;
	private int mUpdateType = 0;
	private int mLaunchResource = 0;
	private long mLastLaunchTime = 0;

	private Animation getExpnadAnim(boolean isExpand, int duration) {
		Animation anim = null;
		if (isExpand) {
			if (mRotateUpAnim == null) {
				mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateUpAnim.setFillAfter(true);
			}
			anim = mRotateUpAnim;
		} else {
			if (mRotateDownAnim == null) {
				mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateDownAnim.setFillAfter(true);
			}
			anim = mRotateDownAnim;
		}
		anim.setDuration(duration);
		return anim;
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_details_fund;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mQuickReturn = new QuickReturnFooter(getSherlockActivity(), R.layout.frag_details_fund,
				R.layout.com_details_footer);
		mRootView = mQuickReturn.createView();
		return mRootView;
	}

	@SuppressLint("NewApi")
	private void findAllViews(View root) {
		float density = SysUtils.getDensity(root.getContext());
		mTvFundTitle = (TextView) root.findViewById(R.id.tv_fund_title);
		mTvFundCode = (TextView) root.findViewById(R.id.tv_fund_code);
		mTvFundType = (TextView) root.findViewById(R.id.tv_fund_type);

		View fundView = root.findViewById(R.id.lay_fund_value);

		StateListDrawable sd = new StateListDrawable();
		sd.addState(new int[] { android.R.attr.state_pressed },
				new CardDrawable(0xfff0f0f0).setShadeWidth(0, 0, 0, 2 * density));
		sd.addState(new int[] {}, new CardDrawable(0xffffffff).setShadeWidth(0, 0, 0, 2 * density));
		ViewUtils.setBackground(fundView, sd);

		mTvFundDate = (TextView) fundView.findViewById(R.id.tv_fund_date);
		mTvFundValue = (TextView) fundView.findViewById(R.id.tv_fund_value);
		mTvFundValueType = (TextView) fundView.findViewById(R.id.tv_fund_value_type);
		mTvFundIncrease = (TextView) fundView.findViewById(R.id.tv_fund_increase);
		mTvFundIncreaseType = (TextView) fundView.findViewById(R.id.tv_fund_increase_type);

		mVSLevel = (ViewStub) root.findViewById(R.id.vs_level);
		mTvTradeState = (TextView) root.findViewById(R.id.tv_fund_state);
		mCharView = (DetailsCharPagerLayout) root.findViewById(R.id.lay_char);
		mCharView.getIndicator().setUnderlineHeight(0);
		mCharView.setPageChangeListener(this);
		mFundHeavy = new HolderFundView().initView(root.findViewById(R.id.lay_fund_heavy_hold));
		mFundCompany = new HolderFundView().initView(root.findViewById(R.id.lay_fund_companyinf));
		mFundManager = new HolderFundView().initView(root.findViewById(R.id.lay_fund_manager));
		mTvFundRate = (CheckHeadText) root.findViewById(R.id.tv_fund_trade_rate);
		mTvSubmittt = (TextView) root.findViewById(R.id.tv_fund_buy);
		mExpand = (ExpandGroup) root.findViewById(R.id.lay_expand);
		ViewUtils.setBackground(mExpand,
				new CardDrawable(0xffffffff).setShadeWidth(0, 0, 0, 2 * density));
		ViewUtils.setBackground(root.findViewById(R.id.v_fund_bottom),
				new CardDrawable(0xffffffff).setShadeWidth(0, 0, 0, 2 * density));
		mExpnadIndicator = mExpand.findViewById(R.id.iv_expand);
		mLvFixed = (ListView) mExpand.findViewById(R.id.lv_per_fixed);
		mLvExpand = (ListView) mExpand.findViewById(R.id.lv_per_expand);
		if (mPerAdpFixed == null || mPerAdpExpand == null) {
			mPerAdpFixed = new PerformaceAdp(getSherlockActivity(),
					Performace.getDefPerformace(true));
			mPerAdpExpand = new PerformaceAdp(getSherlockActivity(), null);
		}
		mLvFixed.setAdapter(mPerAdpFixed);
		mLvExpand.setAdapter(mPerAdpExpand);
		ViewUtils.setListViewHeightBasedOnChildren(mLvFixed);
		mExpand.setOnExpnadChangedListener(this);
		mExpand.setBlockChildTouch(true);
		mTvTradeState.setClickable(false);
		mTvSubmittt.setEnabled(false);
		mFundHeavy.setEnable(false);
		mFundCompany.setEnable(false);
		mFundManager.setEnable(false);
		mTvFundRate.setEnabled(false);
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		findAllViews(root);
		NetWorthBean b = null;
		Bundle argB = getArguments();
		if (argB != null) {
			mLaunchResource = argB.getInt(ValConfig.IT_FROM);
			if (mTitleLable == null) {
				mTitleLable = argB.getString(ValConfig.IT_NAME);
			}
			b = (NetWorthBean) argB.get(ValConfig.IT_ENTITY);
			mRequestArg = argB.getInt(ValConfig.IT_URL);
		}
		if (b == null) {
			String code = argB == null ? null : argB.getString(ValConfig.IT_ID);
			if (code != null) {
				new AsyQueryFundTaks().execute(false, code);
			}
		} else {
			initViewFirst(b);
		}
	}

	@SuppressLint("NewApi")
	private void initViewFirst(NetWorthBean bean) {
		mCharProvider.setBean(bean);
		mCharView.setFragMger(this, mCharProvider, true);
		mCurrentCharPage = mCharView.getViewPage().getCurrentItem();
		FundType type = mCharProvider.getType();
		// d("initViewFirst", bean.toString());
		String jz = FundUtils.formatFundValue(null, bean, FundUtils.VALUE_JJJZ);
		if (type.isSimu()) {
			mTitleLable = "私募详请";
			getSherlockActivity().getSupportActionBar().setTitle(mTitleLable);
			mTvFundRate.setVisibility(View.GONE);
			mFundHeavy.setVisible(View.GONE);
			ViewUtils.setBackground(
					mVSLevel.inflate(),
					new CardDrawable(0xffffffff).setShadeWidth(0, 0, 0,
							2 * SysUtils.getDensity(mVSLevel.getContext())));
			mRatBar = (RatingBar) mRootView.findViewById(R.id.rb_level);
			mTvTradeState.setText("服务热线 400-700-9665");
			mTvTradeState.setClickable(true);
			mTvSubmittt.setText("预约");
			mTvSubmittt.setEnabled(true);
			mTvFundValue.setText(jz);
			mTvFundIncreaseType.setText("月涨幅");
			FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_HB1Y);
		} else {
			mTvFundRate.setHeadColor(0xffeeeeee);
			mTvFundRate.setFlag(CheckHeadText.HEAD_TOP);
			if (type.isHuobi()) {
				mFundHeavy.setVisible(View.GONE);
				mTvFundValueType.setText(mTvFundValueType.getTag() + "");
				mTvFundIncreaseType.setText(mTvFundIncreaseType.getTag() + "");
				mTvFundValue.setText(bean.getWfsy());
				FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_QRSY);
				mTvFundValue.setText(FundUtils.formatFundValue(null, bean, FundUtils.VALUE_WFSY));
			} else {
				if (type.isFengbi()) {
					mTvFundIncreaseType.setText("单位涨幅");
				} else {
					if (type.equalType(FundConfig.TYPE_ZHAIQUAN)
							|| type.equalType(FundConfig.TYPE_QDII)) {
						mFundHeavy.setVisible(View.GONE);
					}
				}
				mTvFundValue.setText(jz);
				FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_HBDR);
			}
			if (bean.getHbTradFlag() == 1) {
				mTvTradeState.setText(ValConfig.NULL_TXT0);
			} else {
				mTvTradeState.setText("未代销");
			}
		}
		mTvFundTitle.setText(bean.getJjmc());
		mTvFundCode.setText(String.format("(%1$s)", bean.getJjdm()));
		mTvFundType.setText(type.FundName);
		mTvFundDate.setText(StrUtils.timeFormat(bean.getJzrq(), ValConfig.DATEF_YMD, "yyyy-M-d"));
		mFundCompany.setText("基金公司", ValConfig.NULL_TXT0);
		mFundManager.setText("基金经理", ValConfig.NULL_TXT0);
		mFundHeavy.setText("重仓持股", ValConfig.NULL_TXT0);
		launchRequest(HAND_FUND_UPDATE, null);// 业绩.
		launchRequest(HAND_FUND_PERFORMACE, null);// 业绩.
		launchRequest(HAND_FUND_COMPANYINF, null);// 公司信息.
		launchRequest(HAND_FUND_MANAGERLIST, null);// 基金经理列表.
		if (type.isSimu()) {
			launchRequest(HAND_FUND_SIMUINF, null);// 持仓列表.
		} else {
			launchRequest(HAND_FUND_HEAVYHOLD, null);// 持仓列表.
			launchRequest(HAND_FUND_TRADESTATE, null);// 基金状态
		}
		getSherlockActivity().invalidateOptionsMenu();
		if (mSensorMgr.getCurScreen() == IScreenChanged.SENSOR_SCREEN_LAND) {
			gotoLand(false);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorMgr = new ScreenHelper();
		d("onCreate", "onCreate");
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorMgr.registerSensor(getSherlockActivity(), this);
		if (!getSherlockActivity().getSupportActionBar().isShowing()) {
			getSherlockActivity().getSupportActionBar().show();
		}
		d("onResume", "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorMgr.unregisterSensor(this);
		d("onPause", "onPause");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		d("onDestroyView", "onDestroyView");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_details, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem it = menu.findItem(R.id.menu_collect);
		if (it != null && mCharProvider.getBean() != null) {
			int resid = mCharProvider.getBean().getXunan() >= 1 ? R.drawable.ic_action_important
					: R.drawable.ic_action_not_important;
			it.setIcon(resid);
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case R.id.menu_share:
			if (mCharProvider.getBean() != null) {
				mShareTitle = mCharProvider.getBean().getJjmc() + " "
						+ mCharProvider.getBean().getJjdm();
				if (mCharProvider.getType().isSimu()) {
					mShareUrl = "http://wap.howbuy.com/simu/product/"
							+ mCharProvider.getBean().getJjdm() + "/";
				} else {
					mShareUrl = "http://wap.howbuy.com/fund/" + mCharProvider.getBean().getJjdm()
							+ "/";
				}
				ShareHelper.showSharePicker(getSherlockActivity(), mDlgClickListener);
			}
			break;
		case R.id.menu_collect:
			toggleCollect();
			break;
		case R.id.menu_setting:
			break;
		default:
			handled = false;
		}
		return handled;
	}

	public DetailsCharPagerLayout getPageChar() {
		return mCharView;
	}

	private void handSubmit(boolean subscribe, String title) {
		String fragName = null;
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		if (subscribe) {
			fragName = FragSubscribeDetails.class.getName();
			b.putBoolean(ValConfig.IT_TYPE, true);
			b.putString(ValConfig.IT_URL, mCharProvider.getBean().getJjmc());
		} else {
			fragName = FragTbTrade.class.getName();
			FundTradeInfo fi = (FundTradeInfo) mTvTradeState.getTag();
			String url = ValConfig.URL_TRADE_BASEURL_RELEASE;
			String jjdm = mCharProvider.getBean().getJjdm();
			if ("1".equals(fi.getRgStatus())) {
				url += ValConfig.URL_Trade_RENGOU + jjdm;
			} else {
				if (!"0".equals(fi.getSgStatus())) {
					url += ValConfig.URL_Trade_SHENGOU + jjdm;
				}
			}
			b.putString(ValConfig.IT_URL, url);
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, fragName);
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
			t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
			Analytics.onEvent(getSherlockActivity(), Analytics.CLICK_BUY_BUTTON);
			startActivity(t);
			getSherlockActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_none);
			return;
		}
		FragOpt opt = new FragOpt(fragName, b, FragOpt.FRAG_POPBACK);
		launch(null, opt);
	}

	private void checkNeedUpdate(int netType) {
		mCharProvider.firstQueryNet();
		if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_PERFORMACE)) {
			launchRequest(HAND_FUND_PERFORMACE, null);// 业绩.
		}

		if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_MANAGERLIST)) {
			launchRequest(HAND_FUND_MANAGERLIST, null);// 基金经理列表.
		}

		if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_COMPANYINF)) {
			launchRequest(HAND_FUND_COMPANYINF, null);// 公司信息.
		}
		if (mCharProvider.getType().isSimu()) {
			if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_SIMUINF)) {
				launchRequest(HAND_FUND_SIMUINF, null);// 持仓列表.
			}
		} else {
			if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_HEAVYHOLD)) {
				launchRequest(HAND_FUND_HEAVYHOLD, null);// 持仓列表.
			}
			if (!ViewUtils.hasFlag(mUpdateType, HAND_FUND_TRADESTATE)) {
				launchRequest(HAND_FUND_TRADESTATE, null);// 基金状态
			}
		}
	}

	@Override
	public boolean onXmlBtClick(View v) {
		int netType = GlobalApp.getApp().getNetType();
		switch (v.getId()) {
		case R.id.tv_fund_buy:
			if (mCharProvider.getType().isSimu()) {
				handSubmit(true, "在线预约");
			} else {
				handSubmit(false, getString(R.string.tb_title_trade));
			}
			break;
		case R.id.lay_fund_heavy_hold:
			handClickHeavyHold();
			break;
		case R.id.lay_fund_companyinf:
			handClickCompany();
			break;
		case R.id.lay_fund_manager:
			handClickManager();
			break;
		case R.id.tv_fund_history:
		case R.id.lay_fund_value:
			if (netType > 1) {
				if (mCharProvider.getBean() != null) {
					handClickHistory();
				}
			} else {
				pop("网络无法连接，稍后再试", false);
			}

			break;
		case R.id.lay_expand:
			if (mPerformace != null) {
				if (mExpand.toggleExpand(200, 15)) {
					if (ExpandGroup.EXPAND_STATE_EXPANDING == mExpand.getExpandState()) {
						mPerAdpFixed.setItems(Performace.filterPerformace(mPerformace, 1,
								mCharProvider.getType()), true);
					} else {
						mPerAdpFixed.setItems(Performace.filterPerformace(mPerformace, 0,
								mCharProvider.getType()), true);
					}
					if (!mHasExpanded) {
						mHasExpanded = true;
						Analytics.onEvent(getSherlockActivity(),
								Analytics.FUND_PERFORMANCE_EXPANDED);
					}
				}
			}
			break;
		case R.id.tv_fund_trade_rate:
			Object obj = mTvTradeState.getTag();
			if (obj != null) {
				handClickTradeRate(obj);
			} else {
				if (netType <= 1) {
					pop("网络无法连接，稍后再试", false);
				}
			}
			break;
		case R.id.chartview: {
			gotoLand(true);
			break;
		}
		case R.id.tv_fund_state: {
			handConsuleCall();
			break;
		}
		default:
			pop("onXmlBtClick " + v, true);
		}
		return super.onXmlBtClick(v);
	}

	private void launch(Intent t, FragOpt f) {
		long cur = System.currentTimeMillis();
		if (cur - mLastLaunchTime > 500) {
			if (t != null) {
				startActivity(t);
			} else {
				((AtyEmpty) getActivity()).switchToFrag(f);
			}
			mLastLaunchTime = cur;
		}
	}

	private void handConsuleCall() {
		Analytics.onEvent(getSherlockActivity(), Analytics.CALL_400, Analytics.KEY_FROM, "私募预约");
		Uri callUri = Uri.parse("tel:" + "4007009665");
		Intent it = new Intent(Intent.ACTION_DIAL, callUri);
		launch(it, null);
	}

	private void handClickManager() {// 如果列表只有一个，直接到经理详情。
		Bundle b = new Bundle();
		String fragName = null;
		b.putString(ValConfig.IT_NAME, "历任基金经理");
		b.putParcelable(ValConfig.IT_TYPE, mCharProvider.getType());
		fragName = FragManagerList.class.getName();
		b.putByteArray(ValConfig.IT_ENTITY, (byte[]) mFundManager.obj);
		FragOpt f = new FragOpt(fragName, b, FragOpt.FRAG_POPBACK | FragOpt.FRAG_CACHE);
		launch(null, f);
	}

	private void handClickTradeRate(Object obj) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, mTvFundRate.getText().toString());
		b.putByteArray(ValConfig.IT_ENTITY, ((FundTradeInfo) obj).toByteArray());
		FragOpt f = new FragOpt(FragNetrateList.class.getName(), b, FragOpt.FRAG_POPBACK
				| FragOpt.FRAG_CACHE);
		launch(null, f);
	}

	private void handClickCompany() {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, mFundCompany.mTvTitle.getText().toString());
		b.putString(ValConfig.IT_ID, mFundCompany.obj == null ? "" : mFundCompany.obj.toString());
		b.putParcelable(ValConfig.IT_TYPE, mCharProvider.getType());
		if (mCharProvider.getType().isSimu()) {
			SimuBasicInfo fi = (SimuBasicInfo) mRatBar.getTag();
			if (fi != null) {
				b.putString(ValConfig.IT_URL, fi.getGsszd());
			}
		}
		FragOpt f = new FragOpt(FragCompany.class.getName(), b, FragOpt.FRAG_POPBACK
				| FragOpt.FRAG_CACHE);
		launch(null, f);
	}

	private void handClickHeavyHold() {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, mFundHeavy.mTvTitle.getText().toString());
		b.putByteArray(ValConfig.IT_ENTITY, (byte[]) mFundHeavy.obj);
		b.putParcelable(ValConfig.IT_TYPE, mCharProvider.getType());
		b.putString(ValConfig.IT_ID, mCharProvider.getBean().getJjdm());
		FragOpt f = new FragOpt(FragHeaveDetailList.class.getName(), b, FragOpt.FRAG_POPBACK
				| FragOpt.FRAG_CACHE);
		launch(null, f);
	}

	private void handClickHistory() {
		Bundle bb = new Bundle();
		bb.putString(ValConfig.IT_NAME, "历史净值");
		bb.putSerializable(ValConfig.IT_ENTITY, mCharProvider.getBean());
		bb.putParcelable(ValConfig.IT_TYPE, mCharProvider.getType());
		FragOpt ff = new FragOpt(FragHistoryNetworthList.class.getName(), bb, FragOpt.FRAG_POPBACK);
		launch(null, ff);
	}

	private boolean checkPerformaceSpaceEnough(ArrayList<Performace> p) {
		int n = p == null ? 0 : p.size();
		float textSize = SysUtils.getDimension(AppFrame.getApp(), TypedValue.COMPLEX_UNIT_SP, 16);
		float maxW = SysUtils.getDisplay(GlobalApp.getApp())[0];
		maxW -= SysUtils.getDimension(AppFrame.getApp(), TypedValue.COMPLEX_UNIT_SP, 140);
		maxW -= SysUtils.getDimension(AppFrame.getApp(), TypedValue.COMPLEX_UNIT_DIP, 38);
		for (int i = 0; i < n; i++) {
			String text = p.get(i).formatRank(true);
			float wText = ViewUtils.getTxtWidth(text, textSize);
			if (wText >= maxW) {
				return false;
			}
		}
		return true;
	}

	private void handResultFundPerformace(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			PerformanceInfo pinf = (PerformanceInfo) result.mData;
			if (pinf != null) {
				boolean needUpdatedExpnad = mPerAdpExpand.isEmpty();
				mPerformace = Performace.parseData(pinf);
				boolean spaceEnough = checkPerformaceSpaceEnough(mPerformace);
				mPerAdpExpand.setFundType(mCharProvider.getType(), spaceEnough);
				mPerAdpFixed.setFundType(mCharProvider.getType(), spaceEnough);
				mPerAdpFixed.setItems(
						Performace.filterPerformace(mPerformace, 0, mCharProvider.getType()), true);
				mPerAdpExpand.setItems(
						Performace.filterPerformace(mPerformace, 2, mCharProvider.getType()), true);
				if (needUpdatedExpnad) {
					mExpand.setExpnadView(mLvExpand, 0);
				}
			} else {
				d("handResultFundPerformace", "PerformanceInfo null ");
			}
		} else {
			d("handResultFundPerformace", "err=" + result.mErr);
		}
	}

	private void handResultFundManagerList(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			ManagerInfoList mfl = (ManagerInfoList) result.mData;
			List<ManagerInfo> mf = mfl.getManagerInfoList();
			int n = mf == null ? 0 : mf.size();
			StringBuilder sb = new StringBuilder(32);
			String name = null;
			for (int i = 0; i < n; i++) {
				name = mf.get(i).getRyxm();
				if (!StrUtils.isEmpty(name)) {
					sb.append(name).append('，');
				}
			}
			if (n > 0) {
				mFundManager.setObject(mfl.toByteArray());
			}
			n = sb.length();
			if (n > 0) {
				sb.deleteCharAt(n - 1);
				mFundManager.setText(sb.toString());
				mFundManager.setEnable(true);
			} else {
				mFundManager.setText(ValConfig.NULL_TXT0);
				mFundManager.setEnable(false);
			}

		} else {
		}
	}

	private void handResultFundCompanyinf(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			CompanyInfo cif = (CompanyInfo) result.mData;
			String companyName = cif.getJgmc();
			if (!StrUtils.isEmpty(companyName)) {
				mFundCompany.setText(companyName);
				mFundCompany.setObject(mCharProvider.getBean().getJjdm());
				mFundCompany.setEnable(true);
			} else {
				mFundCompany.setText(ValConfig.NULL_TXT0);
				mFundCompany.setEnable(false);
			}
		} else {
		}
	}

	private void handResultFundHeavyHolding(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			StringBuilder sb = new StringBuilder();
			ZccgInfo inf = (ZccgInfo) result.mData;
			int n = inf.getStockListCount();
			for (int i = 0; i < n; i++) {
				sb.append(inf.getStockList(i).getStockName()).append("，");
			}
			n = sb.length();
			if (n > 0) {
				sb.deleteCharAt(sb.length() - 1);
				mFundHeavy.setText(sb.toString());
				mFundHeavy.setObject(inf.toByteArray());
				mFundHeavy.setEnable(true);
			} else {
				mFundHeavy.setText(ValConfig.NULL_TXT0);
				mFundHeavy.setEnable(false);
			}
		} else {
			d("handResultFundHeavyHolding", "err=" + result.mErr);
		}
	}

	private void handResultFundUpdate(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			FundInfosList fl = (FundInfosList) result.mData;
			ArrayList<NetWorthBean> r = FundUtils.getNetWorthBean(fl,
					mCharProvider.getType().DataType);
			if (r.size() > 0) {
				NetWorthBean bean = r.get(0);
				if (bean != null) {
					if (mCharProvider.updateBean(bean,
							mCharProvider.getType().DataType == FundConfig.DATA_MONEY)) {
						// DbOperat.getInstance().updateNetValue(mCharProvider.getBean());
						// 数据库不用更新。
					}
				}
				bean = mCharProvider.getBean();
				String jz = FundUtils.formatFundValue(null, bean, FundUtils.VALUE_JJJZ);
				if (mCharProvider.getType().isSimu()) {
					mTvFundValue.setText(jz);
					FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_HB1Y);
				} else {
					if (mCharProvider.getType().isHuobi()) {
						mTvFundValue.setText(bean.getWfsy());
						FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_QRSY);
						mTvFundValue.setText(FundUtils.formatFundValue(null, bean,
								FundUtils.VALUE_WFSY));
					} else {
						mTvFundValue.setText(jz);
						FundUtils.formatFundValue(mTvFundIncrease, bean, FundUtils.VALUE_HBDR);
					}
				}
				mTvFundDate.setText(StrUtils.timeFormat(bean.getJzrq(), ValConfig.DATEF_YMD,
						"yyyy-M-d"));
			}
		} else {
		}
		if (getSherlockActivity() != null) {
			Intent data = new Intent();
			data.putExtra(ValConfig.IT_ENTITY, mCharProvider.getBean());
			data.putExtra(ValConfig.IT_URL, mRequestArg);
			getSherlockActivity().setResult(ValConfig.ATY_REQUEST_FUND_DETAIL, data);
		}
	}

	private String parseState(String rgs, String discount) {
		if (mCharProvider.getBean().getHbTradFlag() == 1) {
			StringBuffer sb = new StringBuffer(24);
			sb.append(FundUtils.formatFundState(rgs, "申购"));
			try {
				float val = Float.parseFloat(discount) * 10;
				if (val != 0 && !"0".equals(rgs)) {
					sb.append("，费率").append(StrUtils.formatF(val, 1)).append("折");
				}
			} catch (Exception e) {
			}
			return sb.toString();

		} else {
			return "未代销";
		}
	}

	public void handResultFundState(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			FundTradeInfo fi = (FundTradeInfo) result.mData;
			if ("1".equals(fi.getCommon().getResponseCode())) {
				String rgs = fi.getSgStatus();
				String discount = fi.getDiscountRate();
				mTvTradeState.setText(parseState(rgs, discount));
				mTvTradeState.setTag(fi);
				mTvFundRate.setEnabled(true);
				if (!"0".equals(rgs) && mCharProvider.getBean().getHbTradFlag() == 1) {
					mTvSubmittt.setEnabled(true);
				}
			} else {
				mTvFundRate.setEnabled(false);
				// fi.getCommon().getResponseContent());
				d("handResultFundState", fi.getCommon().getResponseContent());
			}
		} else {
			d("handResultFundState", "err=" + result.mErr);
		}
	}

	public void handResultSimuInfo(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			SimuBasicInfo fi = (SimuBasicInfo) result.mData;
			if ("1".equals(fi.getCommon().getResponseCode())) {
				mRatBar.setProgress(fi.getHmpj());
				mRatBar.setTag(fi);
			} else {
				d("handResultSimuInfo", "" + fi.getCommon().getResponseContent());
			}
		} else {
			d("handResultSimuInfo", "err=" + result.mErr);
		}
	}

	public boolean launchRequest(int handType, Object obj) {
		AbsParam par = null;
		switch (handType) {
		case HAND_FUND_PERFORMACE:
			par = new ParFundPerformance(CacheOpt.TIME_WEEK).setParams(mCharProvider.getBean()
					.getJjdm(), mCharProvider.getType().isSimu() ? 1 : 0);
			par.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
			break;
		case HAND_FUND_COMPANYINF:
			par = new ParFtenCompanyInfo(CacheOpt.TIME_DAY).setParams(mCharProvider.getBean()
					.getJjdm(), "1");
			break;
		case HAND_FUND_MANAGERLIST:
			par = new ParFtenManagerList(CacheOpt.TIME_DAY).setParams(mCharProvider.getBean()
					.getJjdm());
			break;
		case HAND_FUND_HEAVYHOLD:
			par = new AAParFtenHeavyHoldingInfo(CacheOpt.TIME_DAY).setParams(mCharProvider
					.getBean().getJjdm(), mCharProvider.getType().isSimu() ? "1" : "0", null);
			break;

		case HAND_FUND_TRADESTATE:
			par = new AAParFundTradeInfo(CacheOpt.TIME_WEEK).setParams(mCharProvider.getBean()
					.getJjdm(), mCharProvider.getType().isSimu() ? "1" : "0");
			par.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
			break;
		case HAND_FUND_SIMUINF:
			par = new AAParFtenSimuBasicInf(0).setParams(mCharProvider.getBean().getJjdm());
			break;
		case HAND_FUND_UPDATE:
			par = new ParFundsNetValueByIDs(0).setParams(mCharProvider.getBean().getJjdm());
			break;
		}
		if (par != null) {
			par.setCallback(handType, this).execute();
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	public void toggleCollect() {
		NetWorthBean bean = mCharProvider.getBean();
		if (bean.getXunan() < 1) {
			bean.setXunan(SelfConfig.UNSynsAdd);
			FundUtils.updateOptional(AppFrame.getApp(), bean.getJjdm(), SelfConfig.UNSynsAdd, true);
			Analytics.onEvent(getSherlockActivity(), Analytics.ADD_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "详情页");
		} else if (bean.getXunan() >= 1) {
			bean.setXunan(SelfConfig.UNSynsDel);
			FundUtils.updateOptional(AppFrame.getApp(), bean.getJjdm(), SelfConfig.UNSynsDel, true);
			Analytics.onEvent(getSherlockActivity(), Analytics.DELETE_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "详情页");
		}
		getSherlockActivity().invalidateOptionsMenu();
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		if (isVisible()) {
			if (netType <= 1 && preNet > 1) {
				// pop("网络不可用", false);
			} else {
				if (netType > 1 && preNet <= 1) {
					checkNeedUpdate(netType);
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		final int handType = result.mReqOpt.getHandleType();
		if (result.isSuccess()) {
			mUpdateType |= handType;
		}
		switch (handType) {
		case HAND_FUND_PERFORMACE:
			handResultFundPerformace(result);
			break;
		case HAND_FUND_COMPANYINF:
			handResultFundCompanyinf(result);
			break;
		case HAND_FUND_MANAGERLIST:
			handResultFundManagerList(result);
			break;
		case HAND_FUND_HEAVYHOLD:
			handResultFundHeavyHolding(result);
			break;
		case HAND_FUND_UPDATE:
			handResultFundUpdate(result);
			break;
		case HAND_FUND_SIMUINF:
			handResultSimuInfo(result);
			break;
		case HAND_FUND_TRADESTATE:
			handResultFundState(result);
			break;
		}
	}

	private void gotoLand(boolean forced) {
		if (isVisible() && mCharView.getViewPage().getVisibility() == View.VISIBLE) {
			Bundle b = new Bundle();
			b.putInt(ValConfig.IT_ID, mCurrentCharPage);
			GlobalApp.getApp().getMapObj().put(CharProvider.TAG, mCharProvider);
			getArguments().putBoolean(ValConfig.IT_TYPE, forced);
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragDetailsFundLand.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivityForResult(t, ATY_RESULT_LAND);
		}
	}

	@Override
	public void onExpnadChanged(int changeType, int duration) {
		if (changeType == IExpandChanged.CHANGE_START_EXPAND) {
			mExpnadIndicator.startAnimation(getExpnadAnim(true, duration));
		} else if (changeType == IExpandChanged.CHANGE_START_UNEXPAND) {
			mExpnadIndicator.startAnimation(getExpnadAnim(false, duration));
		}
	}

	class AsyQueryFundTaks extends AsyPoolTask<String, Void, NetWorthBean> {
		@Override
		protected NetWorthBean doInBackground(String... params) {
			try {
				String condition = String.format("and code='%1$s'", params[0]);
				ArrayList<NetWorthBean> r = DbOperat.getInstance().queryAll(condition);
				if (r.size() > 0) {
					return r.get(0);
				}
			} catch (WrapException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(NetWorthBean result) {
			if (result != null) {
				getArguments().putSerializable(ValConfig.IT_ENTITY, result);
				initViewFirst(result);
			} else {
				// RenzhpopMsg("now fund find by code ");
			}
		}
	}

	@Override
	public void onScreenChanged(int preScreen, int curScreen) {
		if (curScreen == IScreenChanged.SENSOR_SCREEN_LAND) {
			if (curScreen == preScreen) {
				mForceScreen = true;
			}
			if (!mForceScreen && mCharProvider.hasFirstQureyDb()) {
				gotoLand(preScreen == IScreenChanged.SENSOR_SCREEN_UNKNOW);
			}
		} else {
			if (curScreen == IScreenChanged.SENSOR_SCREEN_PORT) {
				mForceScreen = false;
			} else {
				if (curScreen == preScreen) {
					mForceScreen = true;
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		mCurrentCharPage = arg0;
		Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_VERTICAL_CHART_VIEW,
				Analytics.KEY_PART, mCharView.getPageTitle(-1));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != -1) {
			mForceScreen = data == null ? false : data.getBooleanExtra(ValConfig.IT_TYPE, false);
			mCharView.getViewPage().setCurrentItem(resultCode, false);
		}
	}

	private String mShareTitle = null;
	private String mShareUrl = null;
	private int mShareType = -1;
	DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mShareType = which;
			String title = mShareTitle;
			String content = mShareTitle;
			String contentUrl = mShareUrl;
			Object bmp = R.drawable.my_start;
			switch (which) {
			case 0:// content为空认为是发图片，没有标题和内容。
				break;
			case 1:
				break;
			case 2:
				bmp = ViewUtils.getBitmap(mRootView, false);
				content = title + "\n\t" + contentUrl;
				break;
			default:
				content = title + "\n\t" + contentUrl;
			}
			ShareHelper.share(getSherlockActivity(), which, title, content, contentUrl, bmp,
					mListener, "基金详情");
		}
	};
	private SnsPostListener mListener = new SnsPostListener() {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
			if (mShareType == 0) {
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "成功", false);
				} else {
					if (eCode != StatusCode.ST_CODE_ERROR_CANCEL) {
						pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "失败", false);
					}
				}
			}
		}
	};

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (fromBar) {
			Intent upIntent = new Intent(getSherlockActivity(), AtyTbMain.class);//
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
				TaskStackBuilder.create(getSherlockActivity()).addNextIntent(upIntent)
						.startActivities();
				getSherlockActivity().overridePendingTransition(0, 0);
				return true;
			} else {
				// 如果前面有界面，基金详情不需要直接回首页。
				// NavUtils.navigateUpTo(getSherlockActivity(), upIntent);
			}
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}
}
