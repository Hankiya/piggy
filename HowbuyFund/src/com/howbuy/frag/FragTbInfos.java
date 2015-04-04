package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.ValConfig;
import com.howbuy.control.PagerSlidingTabStrip;
import com.howbuy.lib.adp.AbsFragPageAdp;
import com.howbuy.lib.frag.AbsHbFrag;

public class FragTbInfos extends AbsHbFrag {
	private static final String[] CONTENT = new String[] { "新闻资讯", "研究报告" };

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_tb_infos;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		PagerAdapter adapter = new FragNewsPageAdp(getChildFragmentManager());
		ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
		pager.setAdapter(adapter);
		PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) root.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_tb_infos, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case R.id.menu_collect:
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_NAME, "文章收藏");
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalCollectList.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivity(t);
			break;
		case R.id.menu_setting:
			break;
		case R.id.menu_about:
			break;
		default:
			handled = false;
		}
		return handled;
	}

	class FragNewsPageAdp extends AbsFragPageAdp {
		public FragNewsPageAdp(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Bundle b = new Bundle();
			b.putInt(ValConfig.IT_ID, position);
			b.putString(ValConfig.IT_NAME, CONTENT[position]);
			return Fragment.instantiate(getActivity(), FragArticalList.class.getName(), b);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		protected String getTag(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		if (isVisible()) {
			if (netType <= 1 && preNet > 1) {
				//pop("网络不可用", false);
			} else {
				if (netType > 1 && preNet <= 1) {
					FragmentManager fm = getChildFragmentManager();
					List<Fragment> fs = fm.getFragments();
					Fragment frag = null;
					int n = fs == null ? 0 : fs.size();
					for (int i = 0; i < n; i++) {
						frag = fs.get(i);
						if (frag instanceof FragArticalList) {
							((FragArticalList) frag).checkNeedUpdate(netType);
						}
					}
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}
}
