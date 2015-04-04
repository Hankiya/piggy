package howbuy.android.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信托详情列表
 * 
 * @author Administrator
 * 
 */
public class TrustsDetailListBean extends ListBean<TrustsDetailBean, TrustsDetailListBean> implements Parcelable {
	private static final long serialVersionUID = 1L;
	private List<TrustsDetailBean> trustsDetailBeans = new ArrayList<TrustsDetailBean>();

	public List<TrustsDetailBean> getTrustsDetailBeans() {
		return trustsDetailBeans;
	}

	public void setTrustsDetailBeans(List<TrustsDetailBean> trustsDetailBeans) {
		this.trustsDetailBeans = trustsDetailBeans;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return trustsDetailBeans.size();
	}

	@Override
	public TrustsDetailBean getItem(int position) {
		// TODO Auto-generated method stub
		return trustsDetailBeans.get(position);
	}

	@Override
	public List<TrustsDetailBean> getItemList() {
		// TODO Auto-generated method stub
		return trustsDetailBeans;
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		trustsDetailBeans.clear();
		setTotal_number(0);
	}

	@Override
	public void addData(TrustsDetailListBean newValue) {
		// TODO Auto-generated method stub
		if (newValue != null && newValue.getSize() != 0) {
			trustsDetailBeans.addAll(newValue.getTrustsDetailBeans());
			setTotal_number(newValue.getTotal_number());
		}
	}

	@Override
	public void replaceAll(TrustsDetailListBean replaceValue) {
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
		dest.writeTypedList(trustsDetailBeans);
	}

	public static final Creator<TrustsDetailListBean> CREATOR = new Creator<TrustsDetailListBean>() {

		@Override
		public TrustsDetailListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustsDetailListBean _newsListBean = new TrustsDetailListBean();
			_newsListBean.isNeedReload = source.readInt() == 0 ? true : false;
			_newsListBean.total_number = source.readInt();
			_newsListBean.trustsDetailBeans = new ArrayList<TrustsDetailBean>();
			source.readTypedList(_newsListBean.trustsDetailBeans, TrustsDetailBean.CREATOR);
			return _newsListBean;
		}

		@Override
		public TrustsDetailListBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustsDetailListBean[size];
		}
	};

}
