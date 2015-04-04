package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 只返回一个String 类型通用
 * 
 * @ClassName: OneStringDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-11下午4:49:54
 */
public class OneStringDto extends TopHeaderDto implements Parcelable {
	private String oneString;

	public String getOneString() {
		return oneString;
	}

	public void setOneString(String oneString) {
		this.oneString = oneString;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeString(oneString);
		dest.writeInt(contentCode);
	}

	public static final Creator<OneStringDto> CREATOR = new Creator<OneStringDto>() {

		@Override
		public OneStringDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			OneStringDto oDto = new OneStringDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.oneString = source.readString();
			return oDto;
		}

		@Override
		public OneStringDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new OneStringDto[size];
		}
	};

}
