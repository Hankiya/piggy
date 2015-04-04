package howbuy.android.piggy.api.dto;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户资产信息
 * 
 * @ClassName: AccessInfo.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-12下午1:15:31
 */
public class AccessInfo extends TopHeaderDto implements Parcelable {
	private Float holdBalance;
	private ArrayList<Float> unconfirmAmt;
	private ArrayList<Float> unconfirmVol;

	public Float getHoldBalance() {
		return holdBalance;
	}

	public void setHoldBalance(Float holdBalance) {
		this.holdBalance = holdBalance;
	}

	public ArrayList<Float> getUnconfirmAmt() {
		return unconfirmAmt;
	}

	public void setUnconfirmAmt(ArrayList<Float> unconfirmAmt) {
		this.unconfirmAmt = unconfirmAmt;
	}

	public ArrayList<Float> getUnconfirmVol() {
		return unconfirmVol;
	}

	public void setUnconfirmVol(ArrayList<Float> unconfirmVol) {
		this.unconfirmVol = unconfirmVol;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeList(unconfirmVol);
		dest.writeFloat(holdBalance);
		dest.writeSerializable(unconfirmAmt);
		dest.writeSerializable(unconfirmAmt);
	}

	public static final Creator<AccessInfo> CREATOR = new Creator<AccessInfo>() {

		@Override
		public AccessInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			AccessInfo _newsListBean = new AccessInfo();
			_newsListBean.contentCode = source.readInt();
			_newsListBean.contentMsg = source.readString();
			_newsListBean.holdBalance = source.readFloat();
			_newsListBean.unconfirmAmt = (ArrayList<Float>) source.readSerializable();
			_newsListBean.unconfirmVol = (ArrayList<Float>) source.readSerializable();
			return _newsListBean;
		}

		@Override
		public AccessInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AccessInfo[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
