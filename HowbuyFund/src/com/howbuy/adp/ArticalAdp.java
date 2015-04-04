package com.howbuy.adp;

import howbuy.android.palmfund.R;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.entity.NewsItem;
import com.howbuy.entity.NewsList;
import com.howbuy.lib.adp.AbsListAdp;
import com.howbuy.lib.adp.AbsViewHolder;

public class ArticalAdp extends AbsListAdp<NewsList, NewsItem> {
	private static int mColorNormal;
	private static int mColorReaded;

	public ArticalAdp(LayoutInflater lf, NewsList items) {
		super(lf, items);
		mColorNormal = 0xff333333;
		mColorReaded = 0xff999999;
	}

	public class ArticalHolder extends AbsViewHolder<NewsItem> {
		TextView title, date, lable;

		@Override
		protected void initView(View v, int type) {
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
			if (item.hasFlag(NewsItem.ARTICAL_READED)) {
				title.setTextColor(mColorReaded);
			} else {
				title.setTextColor(mColorNormal);
			}
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
