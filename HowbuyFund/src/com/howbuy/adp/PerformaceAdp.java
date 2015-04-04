package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.config.FundConfig.FundType;
import com.howbuy.entity.Performace;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;

public class PerformaceAdp extends AbsAdp<Performace> {
	private FundType mFundType = null;
	private boolean isSpaceEnough = true;

	public PerformaceAdp(Context cx, ArrayList<Performace> list) {
		super(cx, list);
	}

	public void setFundType(FundType type, boolean spaceEnough) {
		mFundType = type;
		isSpaceEnough = spaceEnough;
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_details_performace_item, null);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	protected AbsViewHolder<Performace> getViewHolder() {
		return new HeavyDetailHolder();
	}

	public class HeavyDetailHolder extends AbsViewHolder<Performace> {
		public TextView TvCycle, TvState, TvValue;

		@Override
		protected void initView(View root, int type) {
			TvCycle = (TextView) root.findViewById(R.id.tv_cycle);
			TvState = (TextView) root.findViewById(R.id.tv_state);
			TvValue = (TextView) root.findViewById(R.id.tv_value);
		}

		@Override
		protected void initData(int index, int type, Performace item, boolean isReuse) {
			TvCycle.setText(item.getType());
			String strIncrease = item.getShouyi();
			if (mFundType == null || !mFundType.isSimu() || StrUtils.isEmpty(strIncrease)) {
				FundUtils.formatFundValue(TvValue, strIncrease, null, false, FundUtils.VALUE_HBDR);
			} else {

				FundUtils.formatFundValue(TvValue, strIncrease, null, true, FundUtils.VALUE_HB1Y);
			}
			TvState.setText(item.formatRank(isSpaceEnough));
		}
	}

}
