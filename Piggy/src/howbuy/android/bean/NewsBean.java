package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新闻和研报
 * 
 * @author yescpu
 * 
 */
public class NewsBean extends ItemBean implements Parcelable {
	private String title;
	private String label;
	private String publishTime;
	private String id;
	private String tagName;
	private boolean reader;
	private boolean favorite;
	private int newsType;

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public boolean isReader() {
		return reader;
	}

	public void setReader(boolean reader) {
		this.reader = reader;
	}

	public int getNewsType() {
		return newsType;
	}

	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}

	@Override
	public String toString() {
		return "News [title=" + title + ", label=" + label + ", publishTime=" + publishTime + ", id=" + id + ", tagName=" + tagName + ", reader=" + reader + "]";
	}

	@Override
	public String getmClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(label);
		dest.writeString(publishTime);
		dest.writeString(id);
		dest.writeString(tagName);
		dest.writeInt(reader ? 0 : 1);
		dest.writeInt(favorite ? 0 : 1);
		dest.writeInt(isNeedReload ? 0 : 1);
		dest.writeInt(newsType);
	}

	public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {

		@Override
		public NewsBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NewsBean _bBean = new NewsBean();
			_bBean.title = source.readString();
			_bBean.label = source.readString();
			_bBean.publishTime = source.readString();
			_bBean.id = source.readString();
			_bBean.tagName = source.readString();
			_bBean.reader = source.readInt() == 0;
			_bBean.favorite = source.readInt() == 0;
			_bBean.isNeedReload = source.readInt() == 0;
			_bBean.newsType = source.readInt();
			return _bBean;
		}

		@Override
		public NewsBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewsBean[size];
		}

	};
}
