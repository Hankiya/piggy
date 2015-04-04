package howbuy.android.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新闻ListBean
 * 
 * @author yescpu
 * 
 */
public class NewsListBean extends ListBean<NewsBean, NewsListBean> implements Parcelable {
	private List<NewsBean> newsValueBeans = new ArrayList<NewsBean>();

	public List<NewsBean> getNewsValueBeans() {
		return newsValueBeans;
	}

	public void setNewsValueBeans(List<NewsBean> newsValueBeans) {
		this.newsValueBeans = newsValueBeans;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return newsValueBeans.size();
	}

	@Override
	public NewsBean getItem(int position) {
		// TODO Auto-generated method stub
		return newsValueBeans.get(position);
	}

	@Override
	public List<NewsBean> getItemList() {
		// TODO Auto-generated method stub
		return newsValueBeans;
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		getNewsValueBeans().clear();
		setTotal_number(0);
	}

	@Override
	public void addData(NewsListBean newValue) {
		// TODO Auto-generated method stub
		if (newValue != null && newValue.getSize() != 0) {
			getNewsValueBeans().addAll(newValue.getNewsValueBeans());
			setTotal_number(newValue.getTotal_number());
		}
	}

	@Override
	public void replaceAll(NewsListBean replaceValue) {
		// TODO Auto-generated method stub
		clearData();
		addData(replaceValue);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		total_number = 0;
		dest.writeInt(total_number);
		dest.writeInt(isNeedReload ? 0 : 1);
		dest.writeTypedList(newsValueBeans);
	}

	public static final Creator<NewsListBean> CREATOR = new Creator<NewsListBean>() {

		@Override
		public NewsListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NewsListBean _newsListBean = new NewsListBean();
			_newsListBean.isNeedReload = source.readInt() == 0 ? true : false;
			_newsListBean.total_number = source.readInt();
			_newsListBean.newsValueBeans = new ArrayList<NewsBean>();
			source.readTypedList(_newsListBean.newsValueBeans, NewsBean.CREATOR);
			return _newsListBean;
		}

		@Override
		public NewsListBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewsListBean[size];
		}
	};

}
