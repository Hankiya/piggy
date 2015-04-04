package com.howbuy.adp;

import howbuy.android.palmfund.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.howbuy.config.FundConfig.SortType;
import com.howbuy.control.CheckHeadText;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.ViewUtils;

public class RankSortAdp extends AbsAdp<SortType> {
	private Drawable mSelectDrawabe = null;
	private int mSelected = -1;

	public RankSortAdp(Context cx) {
		super(cx, null);
		mSelected = 0;
	}

	public void setSelectedDrawable(Drawable d) {
		mSelectDrawabe = d;
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_menu_item, null);
	}

	@Override
	protected AbsViewHolder<SortType> getViewHolder() {
		return new SortTypeHolder();
	}

	public RankSortAdp setSelected(int selected) {
		mSelected = selected;
		return this;
	}

	public class SortTypeHolder extends AbsViewHolder<SortType> {
		CheckHeadText mTvType = null;
		View mRoot = null;

		@Override
		protected void initView(View root, int type) {
			mRoot = root;
			mTvType = (CheckHeadText) root.findViewById(R.id.tv_title);
			mTvType.setFlag(0);
		}

		@Override
		protected void initData(int index, int type, SortType item, boolean isReuse) {
			Drawable d = null;
			if (index == mSelected) {
				mTvType.setChecked(true);
				d = mSelectDrawabe;
			} else {
				mTvType.setChecked(false);
			}
			ViewUtils.setBackground(mTvType, d);
			mTvType.setText(item.SortName);
		}

		@Override
		public int changeView(int arg) {
			mSelected = arg;
			return mSelected;
		}

	}

}
