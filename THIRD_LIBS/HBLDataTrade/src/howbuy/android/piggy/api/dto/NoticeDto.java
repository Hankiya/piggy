package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通知类型
 * @author Administrator
 *
 */
public class NoticeDto implements Parcelable {
	private String tipType;
	private String tipId;
	private String tipVer;
	private String tipMsg;

	public String getTipType() {
		return tipType;
	}

	public String getTipId() {
		return tipId;
	}

	public String getTipVer() {
		return tipVer;
	}

	public String getTipMsg() {
		return tipMsg;
	}

	public void setTipType(String tipType) {
		this.tipType = tipType;
	}

	public void setTipId(String tipId) {
		this.tipId = tipId;
	}

	public void setTipVer(String tipVer) {
		this.tipVer = tipVer;
	}

	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(tipType);
		dest.writeString(tipId);
		dest.writeString(tipVer);
		dest.writeString(tipMsg);
	}

	public static final Creator<NoticeDto> CREATOR = new Creator<NoticeDto>() {

		@Override
		public NoticeDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NoticeDto td = new NoticeDto();
			td.tipType = source.readString();
			td.tipId = source.readString();
			td.tipVer = source.readString();
			td.tipMsg = source.readString();
			return td;
		}

		@Override
		public NoticeDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NoticeDto[size];
		}

	};
}
