package com.howbuy.adp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.howbuy.adp.RankSettingAdp.RankSettingItem;
import com.howbuy.config.FundConfig;
import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;

public class RankSettingAdp extends AbsAdp<RankSettingItem> {
	FundConfig mFundConfig;
	private int mOriFlag = 0;

	public RankSettingAdp(Context cx, FundConfig config, String[] ranks) {
		super(cx, null);
		mFundConfig = config;
		mOriFlag = mFundConfig.getFlag();
		int n = ranks == null ? 0 : ranks.length;
		if (ranks != null) {
			for (int i = 0; i < n; i++) {
				addItem(new RankSettingItem(ranks[i]), true, false);
			}
		}

	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_rank_setting_item, null);
	}

	@Override
	protected AbsViewHolder<RankSettingItem> getViewHolder() {
		return new RankSettingHolder();
	}

	public int getOriFlag() {
		return mOriFlag;
	}

	public class RankSettingItem {
		public String Name;
		public int Flag;
		public boolean Checked = false;

		public RankSettingItem(String nameflag) {
			String[] ss = nameflag.split("#");
			if (ss != null && ss.length == 2) {
				Name = ss[0];
				Flag = Integer.parseInt(ss[1]);
				Checked = mFundConfig.hasFlag(Flag);
			}
		}

		public void setChecked(boolean checked) {
			Checked = checked;
			if (Checked) {
				mFundConfig.addFlag(Flag);
			} else {
				mFundConfig.subFlag(Flag);
			}
		}
	}

	public static class RankSettingHolder extends AbsViewHolder<RankSettingItem> implements
			OnCheckedChangeListener {
		TextView mTvTitle = null;
		CheckBox mCbCheck = null;

		@Override
		protected void initView(View root, int type) {
			mTvTitle = (TextView) root.findViewById(R.id.tv_title);
			mCbCheck = (CheckBox) root.findViewById(R.id.cb_collect);
			mCbCheck.setOnCheckedChangeListener(this);
		}

		@Override
		protected void initData(int index, int type, RankSettingItem item, boolean isReuse) {
			mItem = item;
			mTvTitle.setText(item.Name);
			mCbCheck.setChecked(item.Checked);
		}

		@Override
		public int changeView(int arg) {
			mCbCheck.setChecked(!mCbCheck.isChecked());
			return mCbCheck.isChecked() ? 1 : 0;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mItem.setChecked(isChecked);
		}
	}

}
