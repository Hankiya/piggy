package howbuy.android.piggy.api.dto;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class IncomeDto extends TopHeaderDto implements Parcelable {
	private float totalDividend;//总收益
	private int listNumber;//总页数
	private String carryoverDate;//最后一次结转时间

	private ArrayList<IncomeItemDto> list;

	private IncomeDto(Parcel in) {
		// TODO Auto-generated constructor stub
		contentCode = in.readInt();
		contentMsg = in.readString();
		carryoverDate = in.readString();
		totalDividend = in.readFloat();
		listNumber = in.readInt();
		in.readTypedList(list, IncomeItemDto.CREATOR);
	}

	public IncomeDto() {
		// TODO Auto-generated constructor stub
	}

	public double getTotalDividend() {
		return totalDividend;
	}

	public void setTotalDividend(float totalDividend) {
		this.totalDividend = totalDividend;
	}

	public int getListNumber() {
		return listNumber;
	}

	public void setListNumber(int listNumber) {
		this.listNumber = listNumber;
	}

	public ArrayList<IncomeItemDto> getIncomeItemDtos() {
		return list;
	}

	public void setIncomeItemDtos(ArrayList<IncomeItemDto> incomeItemDtos) {
		this.list = incomeItemDtos;
	}

	
	@Override
	public String toString() {
		return "IncomeDto [totalDividend=" + totalDividend + ", listNumber=" + listNumber + ", carryoverDate=" + carryoverDate + ",  incomeItemDtos="
				+ list + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeFloat(totalDividend);
		dest.writeInt(listNumber);
		dest.writeTypedList(list);
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeString(carryoverDate);
	}

	public static final Creator<IncomeDto> CREATOR = new Creator<IncomeDto>() {

		@Override
		public IncomeDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new IncomeDto(source);
		}

		@Override
		public IncomeDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new IncomeDto[size];
		}
	};

}
