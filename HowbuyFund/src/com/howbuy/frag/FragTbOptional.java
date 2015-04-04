package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CanScrollViewPager;
import com.howbuy.entity.InitUpdateInfs;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.adp.AbsFragPageAdp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.utils.NetToastUtils;
import com.howbuy.utils.OptionalMger;

public class FragTbOptional extends AbsHbFrag implements OnNavigationListener {
	private static final String SfLastRember = "SfLastRember";
	public static final String FUND = "自选基金";
	public static final String SIMU = "自选私募";
	public static final String[] Optional_Tab = new String[] { FUND, SIMU };
	private CanScrollViewPager mViewPager;
	private FragmentManager mFragmentManager;
	private boolean isFirstResume = true;
	private FragOptionalList mCurrFragment;

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_tb_optional, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		if (mCurrFragment != null) {
			if (mCurrFragment.getmAdp() != null) {
				boolean dataNullFlag = mCurrFragment.getmAdp().getCount() > 0;
				menu.findItem(R.id.menu_edit).setEnabled(dataNullFlag);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int index = mViewPager.getCurrentItem();
		String indexTag = Optional_Tab[index];
		mCurrFragment = (FragOptionalList) getChildFragmentManager().findFragmentByTag(indexTag);

		if (mCurrFragment == null) {
			return false;
		}

		switch (item.getItemId()) {
		case R.id.menu_search:
			mCurrFragment.menuSearchClick();
			break;
		case R.id.menu_edit:
			mCurrFragment.menuEditClick();
			break;
		default:
			break;
		}
		return true;
	}

	public FragOptionalList getmCurrFragment() {
		return mCurrFragment;
	}

	public void setmCurrFragment(FragOptionalList mCurrFragment) {
		this.mCurrFragment = mCurrFragment;
	}

	protected void onAbsBuildActionBar() {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		onAbsBuildActionBar();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mFragmentManager = getChildFragmentManager();
		buildPageNavigation();
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
			Fragment fragment = Fragment.instantiate(getActivity(),
					FragOptionalList.class.getName(), bundle);
			if (position == 0) {
				mCurrFragment = (FragOptionalList) fragment;
			}
			return fragment;
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

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}

	}

	private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
		@SuppressLint("NewApi")
		@Override
		public void onPageSelected(int position) {
			getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(position);
			FragOptionalList mFragment = (FragOptionalList) getChildFragmentManager()
					.findFragmentByTag(Optional_Tab[position]);
			if (mFragment != null) {
				mCurrFragment = mFragment;
			}
			getSherlockActivity().invalidateOptionsMenu();
			System.out.println("-------------" + position);
		}
	};

	@Deprecated
	protected void buildPage() {
		for (int i = 0; i < Optional_Tab.length; i++) {
			getSherlockActivity().getSupportActionBar().addTab(
					getSherlockActivity().getSupportActionBar().newTab().setText(Optional_Tab[i]));
		}
		mViewPager.setAdapter(new MPageAdapter(mFragmentManager));
		mViewPager.setOnPageChangeListener(onPageChangeListener);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
	}

	protected void buildPageNavigation() {
		ArrayAdapter<CharSequence> list = new ArrayAdapter<CharSequence>(getSherlockActivity(),
				R.layout.sherlock_spinner_item, Optional_Tab);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_LIST);
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(list, this);
		mViewPager.setAdapter(new MPageAdapter(mFragmentManager));
		mViewPager.setOnPageChangeListener(onPageChangeListener);
	}

	private void restorePagePostion() {
		int postion = AppFrame.getApp().getsF().getInt(SfLastRember, 0);
		if (postion != 0) {
			mViewPager.setCurrentItem(postion, false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isFirstResume == false) {
			// 第二次
		} else {
			sync();
			isFirstResume = false;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		AppFrame.getApp().getsF().edit().putInt(SfLastRember, mViewPager.getCurrentItem()).commit();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		if (mViewPager.getCurrentItem() != itemPosition) {
			mViewPager.setCurrentItem(itemPosition);
		}
		return false;
	}

	@Override
	protected int getFragLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_content_pager;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		// TODO Auto-generated method stub
		mViewPager = (CanScrollViewPager) root.findViewById(R.id.viewpager);
		buildPageNavigation();
		mViewPager.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				restorePagePostion();
			}
		});
	}

	public void setViewPagerCanHScroll(final boolean s) {
		mViewPager.setCanHScroll(s);
	}

	public void sync() {
		if (UserInf.getUser().isLogined()) {
			try {
				OptionalMger.getMger().exeSync(UserInf.getUser().getCustNo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateNetValueByIDs(String from, String ids) {
		if (InitUpdateInfs.hasTask() == false && ids != null) {
			d("updateNetValueByIDs", ids);
			ReqOpt opt = new ReqOpt(0, from, AppService.HAND_UPDATE_NETVALUE_IDS);
			opt.setArgObj(ids);
			AppFrame.getApp().getGlobalServiceMger().executeTask(opt, null);
		}
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		NetToastUtils.showNetToastIfNeed(this, netType, preNet);
		return super.onNetChanged(netType, preNet);
	}
}
