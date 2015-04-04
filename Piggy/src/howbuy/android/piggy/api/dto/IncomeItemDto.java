package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class IncomeItemDto implements Parcelable {
	private String divDt;
	private String divVol;
	private String custBankId;
	private String bankName;
	private String bankCode;

	
	
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
	
	

	public String getCustBankId() {
		return custBankId;
	}

	public String getBankName() {
		return bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setCustBankId(String custBankId) {
		this.custBankId = custBankId;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
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
		dest.writeString(custBankId);
		dest.writeString(bankName);
		dest.writeString(bankCode);
	}

	public static final Creator<IncomeItemDto> CREATOR = new Creator<IncomeItemDto>() {

		@Override
		public IncomeItemDto createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			IncomeItemDto incomeItemDto=new IncomeItemDto();
			incomeItemDto.divDt=arg0.readString();
			incomeItemDto.divVol=arg0.readString();
			incomeItemDto.custBankId=arg0.readString();
			incomeItemDto.bankName=arg0.readString();
			incomeItemDto.bankCode=arg0.readString();
			return incomeItemDto;
		}

		@Override
		public IncomeItemDto[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new IncomeItemDto[arg0];
		}

	};

}
