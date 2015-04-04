package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.entity.NewsItem;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.ViewUtils;

public class ArticalCollectAdp extends AbsAdp<NewsItem> {
	private Drawable mSelectDrawabe = null;
	private int mSelectedCount = 0;

	public int getSelectedCount() {
		return mSelectedCount;
	}

	public void setSelectedCount(int count) {
		mSelectedCount = count;
	}

	public void cleanSelectedFlags(boolean cleanFlag) {
		if (cleanFlag) {
			int n = getCount();
			for (int i = 0; i < n; i++) {
				mItems.get(i).subFlag(NewsItem.ARTICAL_SELECTED);
			}
		}
		mSelectedCount = 0;
		notifyDataChanged(false);
	}

	public void setSelectedDrawable(Drawable d) {
		mSelectDrawabe = d;
	}

	public ArticalCollectAdp(Context cx, ArrayList<NewsItem> items) {
		super(cx, items);
	}

	public class ArticalHolder extends AbsViewHolder<NewsItem> {
		View root;
		TextView title, date, lable;
		@Override
		protected void initView(View v, int type) {
			root = v;
			title = (TextView) v.findViewById(R.id.newstitle);
			date = (TextView) v.findViewById(R.id.newsdate);
			lable = (TextView) v.findViewById(R.id.newslable);
		}

		@Override
		protected void initData(int index, int type, NewsItem item, boolean isReuse) {
			if (!TextUtils.isEmpty(item.getLabel())) {
				lable.setText(" " + item.getLabel());
			} else {
				lable.setText(null);
			}
			if (!TextUtils.isEmpty(item.getTitle())) {
				title.setText(item.getTitle().trim());
			}
			date.setText(NewsItem.timeState(item.getPublishTime()));
			Drawable d = item.hasFlag(NewsItem.ARTICAL_SELECTED) ? mSelectDrawabe : null;
			ViewUtils.setBackground(root, d);
		}

		@Override
		public int changeView(int arg) {
			Drawable d = mItem.hasFlag(NewsItem.ARTICAL_SELECTED) ? null : mSelectDrawabe;
			ViewUtils.setBackground(root, d);
			if (mItem.hasFlag(NewsItem.ARTICAL_SELECTED)) {
				mItem.subFlag(NewsItem.ARTICAL_SELECTED);
				mSelectedCount--;
			} else {
				mItem.addFlag(NewsItem.ARTICAL_SELECTED);
				mSelectedCount++;
			}
			return mSelectedCount;
		}
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_artical_item, null);
	}

	@Override
	protected AbsViewHolder<NewsItem> getViewHolder() {
		return new ArticalHolder();
	}

}
