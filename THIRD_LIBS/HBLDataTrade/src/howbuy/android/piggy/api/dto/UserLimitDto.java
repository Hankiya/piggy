package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *	用户剩余额度查询
 * @author Administrator
 *
 */
public class UserLimitDto extends TopHeaderDto implements Parcelable {
	public static final String FlagOpen = "1";
	public static final String FlagClose = "0";
	private String allowdMaxAmt;//最大金额
	private int maxCount;//最大次数
	private int remainCount;//剩余次数
	private String remainAmt;//剩余额度
	private String onceAmt;//单笔限额
	private String redeemQuickTimeIsOpen;//T+0交易是否开通
	
	
	


	public String getAllowdMaxAmt() {
		return allowdMaxAmt;
	}




	public void setAllowdMaxAmt(String allowdMaxAmt) {
		this.allowdMaxAmt = allowdMaxAmt;
	}




	public int getMaxCount() {
		return maxCount;
	}




	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}




	public int getRemainCount() {
		return remainCount;
	}




	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}




	public String getRemainAmt() {
		return remainAmt;
	}




	public void setRemainAmt(String remainAmt) {
		this.remainAmt = remainAmt;
	}
	
	




	public String getRedeemQuickTimeIsOpen() {
		return redeemQuickTimeIsOpen;
	}




	public void setRedeemQuickTimeIsOpen(String redeemQuickTimeIsOpen) {
		this.redeemQuickTimeIsOpen = redeemQuickTimeIsOpen;
	}

	
	



	public String getOnceAmt() {
		return onceAmt;
	}




	public void setOnceAmt(String onceAmt) {
		this.onceAmt = onceAmt;
	}




	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeInt(contentCode);
		dest.writeString(allowdMaxAmt);
		dest.writeString(remainAmt);
		dest.writeInt(maxCount);
		dest.writeInt(remainCount);
		dest.writeString(redeemQuickTimeIsOpen);
		dest.writeString(onceAmt);
	}

	public static final Creator<UserLimitDto> CREATOR = new Creator<UserLimitDto>() {

		@Override
		public UserLimitDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			UserLimitDto oDto = new UserLimitDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.allowdMaxAmt = source.readString();
			oDto.remainAmt = source.readString();
			oDto.maxCount=source.readInt();
			oDto.remainCount=source.readInt();
			oDto.redeemQuickTimeIsOpen=source.readString();
			oDto.onceAmt=source.readString();
			return oDto;
		}

		@Override
		public UserLimitDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserLimitDto[size];
		}
	};




	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
