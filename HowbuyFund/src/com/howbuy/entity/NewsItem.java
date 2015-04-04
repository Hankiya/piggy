package com.howbuy.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

import com.howbuy.lib.entity.AbsLoadItem;
import com.howbuy.wireless.entity.protobuf.NewsProtos.News;
import com.howbuy.wireless.entity.protobuf.OpinionProtos.Opinion;

/**
 * 新闻和研报
 * 
 * @author yescpu
 * 
 */
public class NewsItem extends AbsLoadItem implements Parcelable {
	public static final int ARTICAL_READED = 1;
	public static final int ARTICAL_COLLECTED = 2;
	public static final int ARTICAL_SELECTED = 3;

	private String mTitle;
	private String mLable;
	private long mPubTime;
	private long mId;
	private String mTagName;
	private int mNewsType = -1;
	private int mFlag = 0;

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final public void addFlag(int flag) {
		if (flag != 0) {
			mFlag |= flag;
		}
	}

	final public void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}
	public NewsItem() {
	}

	public NewsItem(News n) {
		mTitle = n.getTitle();
		mLable = n.getLabel();
		mPubTime = n.getPublishTime();
		mId = n.getId();
		mTagName = n.getTagName();
		mNewsType = n.getNewsType();
	}

	public NewsItem(Opinion o) {
		mTitle = o.getTitle();
		mLable = o.getLabel();
		mPubTime = o.getPublishTime();
		mId = o.getId();
		mTagName = o.getTagName();
		mNewsType = o.getOpinionType();
	}

	public News toNews(int newType) {
		return News.newBuilder().setTitle(mTitle).setLabel(mLable).setPublishTime(mPubTime)
				.setId(mId).setTagName(mTagName).setNewsType(newType == -1 ? mNewsType : newType)
				.build();
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getLabel() {
		return mLable;
	}

	public void setLabel(String label) {
		this.mLable = label;
	}

	public long getPublishTime() {
		return mPubTime;
	}

	public void setPublishTime(long publishTime) {
		this.mPubTime = publishTime;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}

	public String getTagName() {
		return mTagName;
	}

	public void setTagName(String tagName) {
		this.mTagName = tagName;
	}

	public int getNewsType() {
		return mNewsType;
	}

	public void setNewsType(int newsType) {
		this.mNewsType = newsType;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel i, int flags) {
		i.writeString(mTitle);
		i.writeString(mLable);
		i.writeLong(mPubTime);
		i.writeLong(mId);
		i.writeString(mTagName);
		i.writeInt(mFlag);
		i.writeInt(isNeedReload ? 0 : 1);
		i.writeInt(mNewsType);
	}

	public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
		@Override
		public NewsItem createFromParcel(Parcel s) {
			// TODO Auto-generated method stub
			NewsItem item = new NewsItem();
			item.mTitle = s.readString();
			item.mLable = s.readString();
			item.mPubTime = s.readLong();
			item.mId = s.readLong();
			item.mTagName = s.readString();
			item.mFlag = s.readInt();
			item.isNeedReload = s.readInt() == 0;
			item.mNewsType = s.readInt();
			return item;
		}

		@Override
		public NewsItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewsItem[size];
		}
	};

	/**
	 * format the time as time state of day.
	 * 
	 * @throws
	 */
	public static String timeState(long time) {
		long gap = System.currentTimeMillis() - time;
		String[] msgs = new String[] { "刚刚", "分钟前", "今天 H:mm", "昨天 H:mm", "d天前 H:mm" };
		if (gap < 60000) {// 1分钟内的.
			return msgs[0];
		} else if (gap < 1800000) {// 30分钟内的.
			return (gap / 60000) + msgs[1];
		} else {
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			boolean year = c.get(Calendar.YEAR) == now.get(Calendar.YEAR);
			int datGap = now.get(Calendar.DAY_OF_YEAR) - c.get(Calendar.DAY_OF_YEAR);
			if (year) {
				if (datGap == 0) {
					return new SimpleDateFormat(msgs[2]).format(c.getTime());
				} else if (datGap == 1) {
					return new SimpleDateFormat(msgs[3]).format(c.getTime());
				} else {
					/*
					 * if (datGap < 8 && (c.get(Calendar.MONTH) == now
					 * .get(Calendar.MONTH))) {// 7天及以内. c.set(Calendar.DATE,
					 * datGap); return new
					 * SimpleDateFormat(msgs[4]).format(c.getTime()); } else {//
					 * 今年以内. return new
					 * SimpleDateFormat("M-d").format(c.getTime()); }
					 */return new SimpleDateFormat("M-d").format(c.getTime());
				}
			}
			return new SimpleDateFormat("yyyy-M-d").format(c.getTime());
		}
	}
}
