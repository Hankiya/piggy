package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChartItemBean extends ItemBean implements Parcelable {

	@Override
	public String getmClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String itemDate;
	private float itemValue;

	public String getItemDate() {
		return itemDate;
	}

	public void setItemDate(String itemDate) {
		this.itemDate = itemDate;
	}

	public float getItemValue() {
		return itemValue;
	}

	public void setItemValue(float itemValue) {
		this.itemValue = itemValue;
	}

	@Override
	public String toString() {
		return "ChartItemBean [itemDate=" + itemDate + ", itemValue=" + itemValue + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(itemDate);
		dest.writeFloat(itemValue);
		dest.writeInt(isNeedReload ? 0 : 1);
	}

	public static final Creator<ChartItemBean> CREATOR = new Creator<ChartItemBean>() {

		@Override
		public ChartItemBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ChartItemBean _bBean = new ChartItemBean();
			_bBean.itemDate = source.readString();
			_bBean.itemValue = source.readFloat();
			_bBean.isNeedReload = source.readInt() == 0;
			return _bBean;
		}

		@Override
		public ChartItemBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChartItemBean[size];
		}

	};

}
