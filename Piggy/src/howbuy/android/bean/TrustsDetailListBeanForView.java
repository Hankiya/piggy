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
public class TrustsDetailListBeanForView extends ListBean<TrustsDetailBeanForView, TrustsDetailListBeanForView> implements Parcelable {
	private static final long serialVersionUID = 1L;
	private List<TrustsDetailBeanForView> trustsDetailBeans = new ArrayList<TrustsDetailBeanForView>();
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<TrustsDetailBeanForView> getTrustsDetailBeans() {
		return trustsDetailBeans;
	}

	public void setTrustsDetailBeans(List<TrustsDetailBeanForView> trustsDetailBeans) {
		this.trustsDetailBeans = trustsDetailBeans;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return trustsDetailBeans.size();
	}

	@Override
	public TrustsDetailBeanForView getItem(int position) {
		// TODO Auto-generated method stub
		return trustsDetailBeans.get(position);
	}

	@Override
	public List<TrustsDetailBeanForView> getItemList() {
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
	public void addData(TrustsDetailListBeanForView newValue) {
		// TODO Auto-generated method stub
		if (newValue != null && newValue.getSize() != 0) {
			trustsDetailBeans.addAll(newValue.getTrustsDetailBeans());
			setTotal_number(newValue.getTotal_number());
		}
	}

	@Override
	public void replaceAll(TrustsDetailListBeanForView replaceValue) {
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
		dest.writeString(companyId);
		dest.writeTypedList(trustsDetailBeans);
	}

	public static final Creator<TrustsDetailListBeanForView> CREATOR = new Creator<TrustsDetailListBeanForView>() {

		@Override
		public TrustsDetailListBeanForView createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustsDetailListBeanForView _newsListBean = new TrustsDetailListBeanForView();
			_newsListBean.isNeedReload = source.readInt() == 0 ? true : false;
			_newsListBean.total_number = source.readInt();
			_newsListBean.companyId = source.readString();
			_newsListBean.trustsDetailBeans = new ArrayList<TrustsDetailBeanForView>();
			source.readTypedList(_newsListBean.trustsDetailBeans, TrustsDetailBeanForView.CREATOR);
			return _newsListBean;
		}

		@Override
		public TrustsDetailListBeanForView[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustsDetailListBeanForView[size];
		}
	};

}
