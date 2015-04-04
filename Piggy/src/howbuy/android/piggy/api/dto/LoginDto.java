package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginDto extends TopHeaderDto implements Parcelable {
	private String custNo;
	private String leftTimes;

	public String getCustNo() {
		return custNo;
	}

	public String getLeftTimes() {
		return leftTimes;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setLeftTimes(String leftTimes) {
		this.leftTimes = leftTimes;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.custNo);
		dest.writeString(this.leftTimes);
		dest.writeString(this.contentMsg);
		dest.writeInt(this.contentCode);
	}

	public static final Creator<LoginDto> CREATOR = new Creator<LoginDto>() {

		@Override
		public LoginDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			LoginDto td = new LoginDto();
			td.custNo = source.readString();
			td.leftTimes = source.readString();
			td.contentCode = source.readInt();
			td.contentMsg = source.readString();
			return td;
		}

		@Override
		public LoginDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LoginDto[size];
		}
	};
}