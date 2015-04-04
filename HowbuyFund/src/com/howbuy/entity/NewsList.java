package com.howbuy.entity;

import android.os.Parcel;

import com.howbuy.lib.entity.AbsLoadList;

/**
 * 新闻ListBean
 * 
 * @author yescpu
 * 
 */
public class NewsList extends AbsLoadList<NewsItem, NewsList> {
	@Override
	public void addItems(NewsList valueList) {
		addItems(valueList.getItems(), true);
		setTotalNum(valueList.getTotalNum());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getTotalNum());
		dest.writeInt(isNeedReload() ? 0 : 1);
		dest.writeTypedList(getItems());
	}

	public static final Creator<NewsList> CREATOR = new Creator<NewsList>() {
		@Override
		public NewsList createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NewsList _newsListBean = new NewsList();
			_newsListBean.setNeedReload(source.readInt() == 0 ? true : false);
			_newsListBean.setTotalNum(source.readInt());
			source.readTypedList(_newsListBean.getItems(), NewsItem.CREATOR);
			return _newsListBean;
		}

		@Override
		public NewsList[] newArray(int size) {
			return new NewsList[size];
		}
	};

}
