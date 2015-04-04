package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.wireless.entity.protobuf.IssueProtos.Issue;

public class ManagerArticalAdp extends AbsAdp<Issue> {
	public ManagerArticalAdp(Context cx, ArrayList<Issue> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_heavy_item_simple, null);
	}

	@Override
	protected AbsViewHolder<Issue> getViewHolder() {
		return new ManagerArticalHolder();
	}

	public class ManagerArticalHolder extends AbsViewHolder<Issue> {
		TextView TvName;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
		}

		@Override
		protected void initData(int index, int type, Issue item, boolean isReuse) {
			TvName.setText(item.getWzbt());
		}

	}

}
