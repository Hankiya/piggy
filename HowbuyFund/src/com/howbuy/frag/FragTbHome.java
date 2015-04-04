package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.HomeAdvLayout;
import com.howbuy.control.HomePiggyLayout;
import com.howbuy.control.HomeSimuLayout;
import com.howbuy.control.HomeStockLayout;
import com.howbuy.control.HomeTopView;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.push.PushDispatch;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.NetToastUtils;
import com.howbuy.utils.Receiver;

@SuppressLint("NewApi")
public class FragTbHome extends AbsHbFrag {
	HomeAdvLayout mAdv;
	HomeStockLayout mStock;
	HomePiggyLayout mPiggyLayout;
	HomeSimuLayout mSimuLayout;
	HomeTopView mTopRecomend, mTopScale;
	boolean mHasPaused = false;
	boolean mNeedCheckOpt = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalApp.getApp().getGlobalServiceMger().toggleTimer(true, 250, 500);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mAdv != null) {
			mAdv.toggleTimer(0);
			mStock.toggleTimer(0);
		}
		mHasPaused = true;
		d("onPause", "onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdv != null) {
			mAdv.toggleTimer(5000);
			mStock.toggleTimer(5000);
		}
		if (mHasPaused && mNeedCheckOpt) {
			mTopRecomend.checkOptional();
			mTopScale.checkOptional();
			mSimuLayout.checkOptional();
		} else {
			mNeedCheckOpt = true;
		}
		getSherlockActivity().invalidateOptionsMenu();
		d("onResume", "onResume");
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		// AbsAty.exitAppAlerm(null, "确定要退出掌上基金?", true);
		return false;
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		boolean handled = NetToastUtils.showNetToastIfNeed(this, netType, preNet);
		if (!handled) {
			if (isVisible()) {
				if (netType > 1 && preNet <= 1) {
					mStock.refush(true);
					mAdv.refush(false);
					mTopRecomend.refush(false);
					mTopScale.refush(false);
					mSimuLayout.refush(true);
					mPiggyLayout.refush(false);
					handled = true;
				}
			}
		}
		d("onNetChanged", "cur=" + netType + ",pre=" + preNet + ",handled=" + handled);
		return handled ? true : super.onNetChanged(netType, preNet);
	}

	private void findAllViews(View root) {
		mAdv = (HomeAdvLayout) root.findViewById(R.id.lay_home_adv);
		mStock = (HomeStockLayout) root.findViewById(R.id.lay_home_stock_index);
		mPiggyLayout = (HomePiggyLayout) root.findViewById(R.id.lay_home_piggy);
		mSimuLayout = (HomeSimuLayout) root.findViewById(R.id.lay_home_simu);
		mStock.setProgress(root.findViewById(R.id.pb_home_stock));
		mAdv.setFragMger(getChildFragmentManager());
		if (mTopScale == null) {
			mTopScale = new HomeTopView(HomeTopView.TOP_SCALE,
					root.findViewById(R.id.lay_home_top_sales));
		} else {
			mTopScale
					.resetupView(HomeTopView.TOP_SCALE, root.findViewById(R.id.lay_home_top_sales));
		}
		if (mTopRecomend == null) {
			mTopRecomend = new HomeTopView(HomeTopView.TOP_RECOMMAND,
					root.findViewById(R.id.lay_home_top_recommend));
		} else {
			mTopRecomend.resetupView(HomeTopView.TOP_RECOMMAND,
					root.findViewById(R.id.lay_home_top_recommend));
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		findAllViews(root);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_tb_home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		UserInf user = UserInf.getUser();
		if (user.isLogined()) {
			menu.findItem(R.id.menu_account).setVisible(false);
		} else {
			menu.findItem(R.id.menu_account).setVisible(true);
		}
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			FundUtils.launchFundSearch(this, 0, 0);
			break;
		case R.id.menu_recommend:
			launchItem(item.getTitle().toString(), FragSetRecommend.class.getName());
			break;
		case R.id.menu_feedback:
			launchItem(item.getTitle().toString(), FragSetFeedback.class.getName());
			break;
		case R.id.menu_subscribe:
			launchItem(item.getTitle().toString(), FragSetSubscribe.class.getName());
			break;
		case R.id.menu_account:
			launchItem(item.getTitle().toString(), FragSetLogin.class.getName());
			break;
		case R.id.menu_more:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void launchItem(String title, String fragName) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, fragName);
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
	}

	public boolean toggleCollect(NetWorthBean b, boolean fromSale) {
		if (b != null) {
			if (b.getXunan() < 1) {
				b.setXunan(SelfConfig.UNSynsAdd);
				FundUtils
						.updateOptional(AppFrame.getApp(), b.getJjdm(), SelfConfig.UNSynsAdd, true);
			} else if (b.getXunan() >= 1) {
				b.setXunan(SelfConfig.UNSynsDel);
				FundUtils
						.updateOptional(AppFrame.getApp(), b.getJjdm(), SelfConfig.UNSynsDel, true);
			}
			String title = null;
			String eId = null;
			int result = -1;
			if (fromSale) {
				title = "热销";
				result = mTopScale.setChecked(b.getJjdm(), b.getXunan());
				mTopRecomend.setChecked(b.getJjdm(), b.getXunan());
			} else {
				title = "推荐";
				result = mTopRecomend.setChecked(b.getJjdm(), b.getXunan());
				mTopScale.setChecked(b.getJjdm(), b.getXunan());
			}
			if (result != 0) {
				eId = result == 1 ? Analytics.ADD_CUSTOM_FUNDS : Analytics.DELETE_CUSTOM_FUNDS;
				Analytics.onEvent(getSherlockActivity(), eId, Analytics.KEY_FROM, title);
			}
			return true;
		}
		return false;
	}

	private void launchActivity(String fragName, Bundle arg) {
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, fragName);
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, arg);
		startActivity(t);
	}

	private Bundle obtainBundle(String title) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		return b;
	}

	private void launchNavi(String fragName, String title) {
		launchActivity(fragName, obtainBundle(title));
	}

	private void launchRank() {
		Bundle b = obtainBundle(getString(R.string.tb_title_rank));
		b.putString(ValConfig.IT_TYPE, FundConfig.TYPE_SIMU);
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbRank.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
	}

	private void launchPiggy() {
		Bundle b = obtainBundle(getString(R.string.set_title_app_chuxuguan));
		b.putString(ValConfig.IT_URL, "https://trade.ehowbuy.com/wap2/piggy/piggyPurchase.htm");
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbTrade.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
	}

	@Override
	public boolean onXmlBtClick(View v) {

		boolean handled = true;
		switch (v.getId()) {
		case R.id.lay_navi_optional:
			launchNavi(FragTbOptional.class.getName(), getString(R.string.tb_title_optional));
			break;
		case R.id.lay_navi_rank:
			launchNavi(FragTbRank.class.getName(), getString(R.string.tb_title_rank));
			break;
		case R.id.lay_navi_infos:
			mNeedCheckOpt = false;
			launchNavi(FragTbInfos.class.getName(), getString(R.string.tb_title_infos));
			break;
		case R.id.lay_navi_trade:
			mNeedCheckOpt = false;
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbTrade.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, obtainBundle(getString(R.string.tb_title_trade)));
			t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
			t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
			startActivity(t);
			getSherlockActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_none);
			break;
		case R.id.lay_home_stock_index:
			if (GlobalApp.getApp().getNetType() > 1) {
				mStock.refush(true);
			} else {
				pop("无可用网络,刷新失败", false);
			}
			break;
		case R.id.cb_collect:
			handled = handClickTopCollect(v.getTag());
			break;
		case R.id.tv_submit:
			Analytics.onEvent(getSherlockActivity(), Analytics.CXG_CLICK, null, null);
			launchPiggy();
			break;
		case R.id.lay_simu_one:
			Analytics
					.onEvent(getSherlockActivity(), Analytics.SMJX_CLICK, Analytics.KEY_ORDER, "1");
			FundUtils.launchFundDetails(this, mSimuLayout.getData().get(0), 0, 0);
			Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_FUND_DETAIL,
					Analytics.KEY_FROM, "私募精选");

			break;
		case R.id.lay_simu_two:
			Analytics
					.onEvent(getSherlockActivity(), Analytics.SMJX_CLICK, Analytics.KEY_ORDER, "2");
			FundUtils.launchFundDetails(this, mSimuLayout.getData().get(1), 1, 0);
			Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_FUND_DETAIL,
					Analytics.KEY_FROM, "私募精选");
			break;
		case R.id.tv_home_simu_more:
			Analytics.onEvent(getSherlockActivity(), Analytics.SMJX_CLICK, Analytics.KEY_ORDER,
					"更多");
			launchRank();
			break;
		case R.id.adimage:
			// String advId = (String) v.getTag(R.id.key_tag0);
			String eventCode = (String) v.getTag(R.id.key_tag1);
			String title = (String) v.getTag(R.id.key_tag2);
			new PushDispatch(getSherlockActivity()).doAdvEvent(eventCode, title);
			break;
		case R.id.lay_top_char1:
		case R.id.lay_top_char2:
			handled = handClickTopLaunch(v.getTag());
			break;
		default:
			handled = false;
		}
		return handled ? true : super.onXmlBtClick(v);
	}

	private boolean handClickTopCollect(Object tag) {
		int position = (Integer) tag;
		int index = -1;
		boolean fromSale = true;
		NetWorthBean b = null;
		if ((index = mTopScale.parsePosition(position)) == -1) {
			index = mTopRecomend.parsePosition(position);
			b = mTopRecomend.getFund(index);
			fromSale = false;
		} else {
			b = mTopScale.getFund(index);
		}
		if (b != null) {
			if (!toggleCollect(b, fromSale)) {
				toggleCollect(b, fromSale);
			}
			return true;
		}
		return false;
	}

	private boolean handClickTopLaunch(Object tag) {
		String eveId = null;
		int position = (Integer) tag;
		int index = -1;
		NetWorthBean b = null;
		if ((index = mTopScale.parsePosition(position)) == -1) {
			index = mTopRecomend.parsePosition(position);
			eveId = Analytics.TUIJIAN_CLICK;
			b = mTopRecomend.getFund(index);
			Analytics.onEvent(getActivity(), Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, "推荐");
		} else {
			eveId = Analytics.REXIAO_CLICK;
			b = mTopScale.getFund(index);
			Analytics.onEvent(getActivity(), Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, "热销");
		}
		if (b != null) {
			Analytics.onEvent(getSherlockActivity(), eveId, Analytics.KEY_ORDER, "" + (index + 1));
			FundUtils.launchFundDetails(this, b, position, 0);
			return true;
		}
		return false;
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_tb_home;
	}

	@Override
	public void onReceiveBroadcast(int from, Bundle b) {
		if (from == Receiver.FROM_UPDATE_LAUNCH) {
			if (mStock != null) {
				mTopRecomend.refush(false);
				mTopScale.refush(false);
				mSimuLayout.checkFund();
			}
		} else if (from == Receiver.FROM_OPTIONAL_SYNC) {
			if (mStock != null) {
				mTopRecomend.checkOptional();
				mTopScale.checkOptional();
				mSimuLayout.checkOptional();
			}
		}
	}

	@Override
	public boolean shouldEnableLocalBroadcast() {
		return true;
	}
}
