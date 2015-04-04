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
public class ChartListBean extends ListBean<ChartItemBean, ChartListBean> implements Parcelable {
	private static final long serialVersionUID = 1L;
	private List<ChartItemBean> ChartItemBeans = new ArrayList<ChartItemBean>();

	public List<ChartItemBean> getChartItemBeans() {
		return ChartItemBeans;
	}

	public void setChartItemBeans(List<ChartItemBean> ChartItemBeans) {
		this.ChartItemBeans = ChartItemBeans;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return ChartItemBeans.size();
	}

	@Override
	public ChartItemBean getItem(int position) {
		// TODO Auto-generated method stub
		return ChartItemBeans.get(position);
	}

	@Override
	public List<ChartItemBean> getItemList() {
		// TODO Auto-generated method stub
		return ChartItemBeans;
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		ChartItemBeans.clear();
		setTotal_number(0);
	}

	@Override
	public void addData(ChartListBean newValue) {
		// TODO Auto-generated method stub
		if (newValue != null && newValue.getSize() != 0) {
			ChartItemBeans.addAll(newValue.getChartItemBeans());
			setTotal_number(newValue.getTotal_number());
		}
	}

	@Override
	public void replaceAll(ChartListBean replaceValue) {
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
		dest.writeInt(total_number);
		dest.writeInt(isNeedReload ? 0 : 1);
		dest.writeTypedList(ChartItemBeans);
	}

	public static final Creator<ChartListBean> CREATOR = new Creator<ChartListBean>() {

		@Override
		public ChartListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ChartListBean _newsListBean = new ChartListBean();
			_newsListBean.isNeedReload = source.readInt() == 0 ? true : false;
			_newsListBean.total_number = source.readInt();
			_newsListBean.ChartItemBeans = new ArrayList<ChartItemBean>();
			source.readTypedList(_newsListBean.ChartItemBeans, ChartItemBean.CREATOR);
			return _newsListBean;
		}

		@Override
		public ChartListBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChartListBean[size];
		}
	};
}
