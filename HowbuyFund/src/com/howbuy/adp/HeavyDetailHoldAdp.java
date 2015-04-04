package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.entity.HeavyHoldItem;
import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;

public class HeavyDetailHoldAdp extends AbsAdp<HeavyHoldItem> {
	public HeavyDetailHoldAdp(Context cx, ArrayList<HeavyHoldItem> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_heavy_item, null);
	}

	@Override
	protected AbsViewHolder<HeavyHoldItem> getViewHolder() {
		return new HeavyDetailHolder();
	}

	public class HeavyDetailHolder extends AbsViewHolder<HeavyHoldItem> {
		TextView TvPercent, TvProperty, TvName;
		View mColor;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
			TvProperty = (TextView) root.findViewById(R.id.tv_value);
			TvPercent = (TextView) root.findViewById(R.id.tv_increase);
			mColor = root.findViewById(R.id.v_color);
		}

		@Override
		protected void initData(int index, int type, HeavyHoldItem item, boolean isReuse) {
			TvName.setText(item.getValueStr(0));
			TvPercent.setText(StrUtils.formatF(item.getValue(0), FundUtils.FDECIMAL2) + "%");
			if (index != getCount() - 1) {
				TvProperty.setText(StrUtils.formatF(item.getValue(1), FundUtils.FDECIMAL2));
			} else {
				TvProperty.setText("");
			}

			mColor.setBackgroundColor(item.getColor(false));
		}

	}

}
