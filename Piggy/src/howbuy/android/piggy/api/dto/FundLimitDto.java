package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *	用户剩余额度查询
 * @author Administrator
 *
 */
public class FundLimitDto extends TopHeaderDto implements Parcelable {
	
	private String minAppVol;
	private String maxAppVol;
	private String minSuppleVol;
	private String minAppAmt;
	private String minSuppleAmt;
	private String maxAppAmt;
	private String maxSumAmt;


	public String getMinAppVol() {
		return minAppVol;
	}




	public void setMinAppVol(String minAppVol) {
		this.minAppVol = minAppVol;
	}




	public String getMaxAppVol() {
		return maxAppVol;
	}




	public void setMaxAppVol(String maxAppVol) {
		this.maxAppVol = maxAppVol;
	}




	public String getMinSuppleVol() {
		return minSuppleVol;
	}




	public void setMinSuppleVol(String minSuppleVol) {
		this.minSuppleVol = minSuppleVol;
	}




	public String getMinAppAmt() {
		return minAppAmt;
	}




	public void setMinAppAmt(String minAppAmt) {
		this.minAppAmt = minAppAmt;
	}




	public String getMinSuppleAmt() {
		return minSuppleAmt;
	}




	public void setMinSuppleAmt(String minSuppleAmt) {
		this.minSuppleAmt = minSuppleAmt;
	}




	public String getMaxAppAmt() {
		return maxAppAmt;
	}




	public void setMaxAppAmt(String maxAppAmt) {
		this.maxAppAmt = maxAppAmt;
	}




	public String getMaxSumAmt() {
		return maxSumAmt;
	}




	public void setMaxSumAmt(String maxSumAmt) {
		this.maxSumAmt = maxSumAmt;
	}




	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeInt(contentCode);
		dest.writeString(this.maxAppAmt);
		dest.writeString(this.maxAppVol);
		dest.writeString(this.maxSumAmt);
		dest.writeString(this.minAppAmt);
		dest.writeString(this.minAppVol);
		dest.writeString(this.minSuppleAmt);
		dest.writeString(this.minSuppleVol);
	}

	public static final Creator<FundLimitDto> CREATOR = new Creator<FundLimitDto>() {

		@Override
		public FundLimitDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			FundLimitDto oDto = new FundLimitDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.maxAppAmt = source.readString();
			oDto.maxAppVol = source.readString();
			oDto.maxSumAmt = source.readString();
			oDto.minAppAmt = source.readString();
			oDto.maxAppVol = source.readString();
			oDto.minSuppleAmt = source.readString();
			oDto.minSuppleVol = source.readString();
			return oDto;
		}

		@Override
		public FundLimitDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FundLimitDto[size];
		}
	};




	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
