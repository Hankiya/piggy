package com.howbuy.adp;

import howbuy.android.palmfund.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueOfPageProtos.Info;

public class FundHistoryAdp extends AbsAdp<Info> {
	private String mDanWei = null;
	private static String mDateFormat = "M-d";
	private static String mFormatY = "yyyy";
	private static String mYear = null;
	private FundType mFundType = null;
	private boolean isSimu = false;

	public FundHistoryAdp(Context cx, FundType type, String danwei) {
		super(cx, null);
		mYear = StrUtils.timeFormat(System.currentTimeMillis(), mFormatY);
		mDanWei = danwei;
		mFundType = type;
		isSimu = mFundType.isSimu();
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		int resId = 0;
		if (type == 0) {
			if (mFundType.isHuobi()) {
				resId = R.layout.com_list_history_item_huobi;
			} else {
				resId = R.layout.com_list_history_item;
			}

		} else {
			if (mFundType.isHuobi()) {
				resId = R.layout.com_list_history_head_huobi;
			} else {
				resId = R.layout.com_list_history_head;
			}
		}
		return mLf.inflate(resId, null);
	}

	@Override
	protected AbsViewHolder<Info> getViewHolder() {
		return new HeavySimpleHolder();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0 || mItems.get(position).getJzrq().startsWith(mYear)) {
			return 0;
		} else {
			String preY = mItems.get(position - 1).getJzrq().substring(0, 4);
			String curY = mItems.get(position).getJzrq();
			if (curY.startsWith(preY)) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != 1;
	}

	public class HeavySimpleHolder extends AbsViewHolder<Info> {
		TextView TvDate, TvValue, TvValued, TvIncrease;
		CheckHeadText TvHead;

		@Override
		protected void initView(View root, int type) {
			if (type == 1) {
				TvHead = (CheckHeadText) root.findViewById(R.id.tv_head);
				TvHead.setHeadColor(0xf5f5f5);
				TvHead.addFlag(CheckHeadText.HEAD_TOP);
			}
			if (!mFundType.isHuobi()) {
				TvIncrease = (TextView) root.findViewById(R.id.tv_increase);
			}
			TvDate = (TextView) root.findViewById(R.id.tv_date);
			TvValue = (TextView) root.findViewById(R.id.tv_value);
			TvValued = (TextView) root.findViewById(R.id.tv_valued);
		}

		@Override
		protected void initData(int index, int type, Info item, boolean isReuse) {
			if (type == 1) {
				TvHead.setText(StrUtils.timeFormat(item.getJzrq(), ValConfig.DATEF_YMD, mFormatY));
			}
			TvDate.setText(StrUtils.timeFormat(item.getJzrq(), ValConfig.DATEF_YMD, mDateFormat));
			if (!mFundType.isHuobi()) {
				FundUtils.formatFundValue(TvValue, item.getJjjz(), mDanWei, isSimu,
						FundUtils.VALUE_JJJZ);
				FundUtils.formatFundValue(TvValued, item.getLjjz(), mDanWei, isSimu,
						FundUtils.VALUE_LJJZ);
				FundUtils.formatFundValue(TvIncrease, item.getHbxx(), mDanWei, isSimu,
						FundUtils.VALUE_HBDR);

			} else {
				FundUtils.formatFundValue(TvValue, item.getJjjz(), null, false,
						FundUtils.VALUE_QRSY);
				/*
				 * TvValued.setText(FundUtils.formatFundValue(null,
				 * item.getLjjz(), null, false, FundUtils.VALUE_WFSY));
				 */
				FundUtils.formatFundValue(TvValued, item.getLjjz(), null, false,
						FundUtils.VALUE_WFSY);
			}
		}

	}

}
