package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class IncomeItemDto implements Parcelable {
	private String divDt;
	private String divVol;

	
	
	public String getDivDt() {
		return divDt;
	}

	public void setDivDt(String divDt) {
		this.divDt = divDt;
	}

	public String getDivVol() {
		return divVol;
	}

	public void setDivVol(String divVol) {
		this.divVol = divVol;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "IncomeItemDto [divDt=" + divDt + ", divVol=" + divVol + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(divDt);
		dest.writeString(divVol);
	}

	public static final Creator<IncomeItemDto> CREATOR = new Creator<IncomeItemDto>() {

		@Override
		public IncomeItemDto createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			IncomeItemDto incomeItemDto=new IncomeItemDto();
			incomeItemDto.divDt=arg0.readString();
			incomeItemDto.divVol=arg0.readString();
			return incomeItemDto;
		}

		@Override
		public IncomeItemDto[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new IncomeItemDto[arg0];
		}

	};

}
