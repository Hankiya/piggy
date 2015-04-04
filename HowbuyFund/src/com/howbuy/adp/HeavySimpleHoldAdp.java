package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundZccgInfoProtos.FundZccgInfo;

public class HeavySimpleHoldAdp extends AbsAdp<FundZccgInfo> {
	public HeavySimpleHoldAdp(Context cx, ArrayList<FundZccgInfo> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_heavy_item_simple, null);
	}

	@Override
	protected AbsViewHolder<FundZccgInfo> getViewHolder() {
		return new HeavySimpleHolder();
	}

	public class HeavySimpleHolder extends AbsViewHolder<FundZccgInfo> {
		TextView TvName;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
		}

		@Override
		protected void initData(int index, int type, FundZccgInfo item, boolean isReuse) {
			String[] strs = FundUtils.formatQuarter(item.getJzrq());
			if (strs != null) {
				TvName.setText(strs[0] + "第" + strs[1]);
			}
		}

	}

}
