package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.config.FundConfig.FundType;
import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;

public class RankTypeAdp extends AbsAdp<FundType> {
	//private Context mCx = null;

	public RankTypeAdp(Context cx, ArrayList<FundType> items) {
		super(cx, items);
		//mCx = cx;
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_rank_type_item, null);
	}

	@Override
	protected AbsViewHolder<FundType> getViewHolder() {
		return new RankTypeHolder();
	}

	public static class RankTypeHolder extends AbsViewHolder<FundType> {
		TextView mTvType = null;

		@Override
		protected void initView(View root, int type) {
			mTvType = (TextView) root.findViewById(R.id.tv_title);
		}

		@Override
		protected void initData(int index, int type, FundType item, boolean isReuse) {
			mItem = item;
			mTvType.setText(item.FundName);

		}

	}

}
