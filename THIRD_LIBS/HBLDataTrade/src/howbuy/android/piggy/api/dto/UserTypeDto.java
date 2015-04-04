package howbuy.android.piggy.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class UserTypeDto extends TopHeaderDto implements Parcelable {

	private static final long serialVersionUID = 7261750447720734513L;

	private String needActive; // 是否要激活0、不要激活 1、需要激活
	private ArrayList<String> mobiles; // 手机号

	public String getNeedActive() {
		return needActive;
	}

	public void setNeedActive(String needActive) {
		this.needActive = needActive;
	}

	public List<String> getMobiles() {
		return mobiles;
	}

	public void setMobiles(ArrayList<String> mobiles) {
		this.mobiles = mobiles;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeString(needActive);
		dest.writeSerializable(mobiles);
	}

	public static final Creator<UserTypeDto> CREATOR = new Creator<UserTypeDto>() {

		@Override
		public UserTypeDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			UserTypeDto oDto = new UserTypeDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.needActive = source.readString();
			oDto.mobiles = (ArrayList<String>) source.readSerializable();
			return oDto;
		}

		@Override
		public UserTypeDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

	};

}