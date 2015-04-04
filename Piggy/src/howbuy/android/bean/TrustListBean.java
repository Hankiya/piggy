package howbuy.android.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信托产品
 * 
 * @author yescpu
 * 
 */
public class TrustListBean extends ListBean<TrustBean, TrustListBean> implements Parcelable {
	private List<TrustBean> newsValueBeans = new ArrayList<TrustBean>();

	public List<TrustBean> getNewsValueBeans() {
		return newsValueBeans;
	}

	public void setNewsValueBeans(List<TrustBean> newsValueBeans) {
		this.newsValueBeans = newsValueBeans;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return newsValueBeans.size();
	}

	@Override
	public TrustBean getItem(int position) {
		// TODO Auto-generated method stub
		return newsValueBeans.get(position);
	}

	@Override
	public List<TrustBean> getItemList() {
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
	public void addData(TrustListBean newValue) {
		// TODO Auto-generated method stub
		if (newValue != null && newValue.getSize() != 0) {
			getNewsValueBeans().addAll(newValue.getNewsValueBeans());
			setTotal_number(newValue.getTotal_number());
		}
	}

	@Override
	public void replaceAll(TrustListBean replaceValue) {
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

	public static final Creator<TrustListBean> CREATOR = new Creator<TrustListBean>() {

		@Override
		public TrustListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustListBean _newsListBean = new TrustListBean();
			_newsListBean.isNeedReload = source.readInt() == 0 ? true : false;
			_newsListBean.total_number = source.readInt();
			_newsListBean.newsValueBeans = new ArrayList<TrustBean>();
			source.readTypedList(_newsListBean.newsValueBeans, TrustBean.CREATOR);
			return _newsListBean;
		}

		@Override
		public TrustListBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustListBean[size];
		}
	};
}
