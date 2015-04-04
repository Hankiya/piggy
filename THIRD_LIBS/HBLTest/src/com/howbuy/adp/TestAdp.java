package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.libtest.R;

public class TestAdp extends AbsAdp<String> {

	public TestAdp(Context cx, ArrayList<String> items) {
		super(cx, items);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_item, null);
	}

	@Override
	protected AbsViewHolder<String> getViewHolder() {
		return new TestHolder();
	}

	@Override
	public boolean hasFlag(int pos, int flag) {
		if (pos % 2 == 1) {
			return true;
		}
		return super.hasFlag(pos, flag);
	}

	public class TestHolder extends AbsViewHolder<String> {
		public TextView TvName = null;
		public TextView TvValue = null;

		@Override
		protected void initView(View root, int type) {
			TvValue = (TextView) root.findViewById(R.id.tv_value);
			TvName = (TextView) root.findViewById(R.id.tv_cycle);
		}

		@Override
		protected void initData(int index, int type, String item, boolean isReuse) {
			TvName.setText(item);
			TvValue.setText("" + index);
		}
	}

}
