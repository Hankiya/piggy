package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.Cons;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

public class QueryFragment extends AbstractFragment implements OnPageChangeListener {
	public static final String TypeToIncome = "TypeToIncome";
	private ViewPager mViewPager;
	private TabPageIndicator mtabInd;
	private String jumpType;
	public static int income = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b= getArguments();
		if (b!=null) {
			jumpType=b.getString(Cons.Intent_type);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.viewpager_layout, null);
		mViewPager = (ViewPager) layout.findViewById(R.id.viewpager);
		mtabInd = (TabPageIndicator) layout.findViewById(R.id.indicator);
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// ACTIVITY_DESTROY_AND_CREATE
		MPageAdapter ma;
		switch (getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			ma = new MPageAdapter(getChildFragmentManager());
			mViewPager.setAdapter(ma);
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			ma = new MPageAdapter(getChildFragmentManager());
			mViewPager.setAdapter(ma);
			break;
		case SCREEN_ROTATE:

			break;
		default:
			break;
		}
		mtabInd.setViewPager(mViewPager);
		if (TypeToIncome.equals(jumpType)) {
			mViewPager.setCurrentItem(1);
		}
	}

	class MPageAdapter extends FragmentPagerAdapter {
		public MPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			bundle.putInt("mNewsType", position);
			Fragment f;
			if (position == 0) {
				income = 0;
				f = QueryTradeFragment.newInstance();
			} else {
				income = 1;
				f = QueryIncomeFragment.newInstance();
			}
			return f;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return getString(R.string.tab_trade_history);
			} else {
				return getString(R.string.tab_trade_income);
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

}