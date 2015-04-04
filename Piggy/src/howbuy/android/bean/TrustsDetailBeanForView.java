package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信托详情
 * 
 * @author Administrator
 * 
 */
public class TrustsDetailBeanForView extends ItemBean implements Parcelable {

	@Override
	public String getmClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String itemKey;
	private String itemValue;

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	@Override
	public String toString() {
		return itemKey;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(itemKey);
		dest.writeString(itemValue);
		dest.writeInt(isNeedReload ? 0 : 1);
	}

	public static final Creator<TrustsDetailBeanForView> CREATOR = new Creator<TrustsDetailBeanForView>() {

		@Override
		public TrustsDetailBeanForView createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustsDetailBeanForView _bBean = new TrustsDetailBeanForView();
			_bBean.itemKey = source.readString();
			_bBean.itemValue = source.readString();
			_bBean.isNeedReload = source.readInt() == 0;
			return _bBean;
		}

		@Override
		public TrustsDetailBeanForView[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustsDetailBeanForView[size];
		}

	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
