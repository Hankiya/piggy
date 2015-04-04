package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howbuy.adp.TradeRateAdp.TradeRate;
import com.howbuy.control.CheckHeadText;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.SysUtils;

public class TradeRateAdp extends AbsAdp<TradeRate> {
	private int mHeadHeight = 0;
	private int mPaddingLeft = 0;
	private Context mCx = null;

	public TradeRateAdp(Context cx, ArrayList<TradeRate> list) {
		super(cx, list);
		mCx = cx;
		mHeadHeight = (int) SysUtils.getDimension(cx, TypedValue.COMPLEX_UNIT_DIP, 30);
		mPaddingLeft = cx.getResources().getDimensionPixelOffset(R.dimen.aty_horizontal_margin);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		if (type == 0) {
			return mLf.inflate(R.layout.com_list_netrate_item, null);
		} else {
			LinearLayout ll = new LinearLayout(mCx);
			CheckHeadText tv = new CheckHeadText(mCx);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, mHeadHeight);
			lp.weight = 1;
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setPadding(mPaddingLeft, 0, 0, 0);
			tv.setBackgroundColor(0xffeeeeee);
			tv.setFlag(0);
			ll.addView(tv, lp);
			return ll;
		}
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != 1;
	}

	@Override
	protected AbsViewHolder<TradeRate> getViewHolder() {
		return new HeavySimpleHolder();
	}

	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).getType();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public class HeavySimpleHolder extends AbsViewHolder<TradeRate> {
		TextView TvTitle, TvState;

		@Override
		protected void initView(View root, int type) {
			if (type == 1) {
				TvTitle = (TextView) ((LinearLayout) root).getChildAt(0);
			} else {
				TvTitle = (TextView) root.findViewById(R.id.tv_title);
				TvState = (TextView) root.findViewById(R.id.tv_state);
			}

		}

		@Override
		protected void initData(int index, int type, TradeRate item, boolean isReuse) {
			TvTitle.setText(item.getTitle());
			if (type == 0) {
				TvState.setText(item.getState());
			}

		}

	}

	public static class TradeRate {
		private int mType = 0;
		private String mTitle = null;
		private String mState = null;

		public TradeRate(String title, String state) {
			this.mTitle = title;
			this.mState = state;
		}

		public TradeRate(String title) {
			this.mTitle = title;
			mType = 1;
		}

		public String getTitle() {
			return mTitle;
		}

		public String getState() {
			return mState;
		}

		public int getType() {
			return mType;
		}
	}
}
