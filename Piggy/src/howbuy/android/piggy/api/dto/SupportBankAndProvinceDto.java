package howbuy.android.piggy.api.dto;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 省份和分行
 * 
 * @ClassName: SupportBankAndProvinceDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-11下午6:00:44
 */
public class SupportBankAndProvinceDto extends TopHeaderDto implements Parcelable {
	List<SupportBankDto> bankList;
	List<ProvinceInfoDto> provList;

	public ProvinceInfoDto getProvinceById(String provinceCode){
        for (int i=0;i<provList.size();i++){
            if (provList.get(i).getCode().equals(provinceCode)){
                return  provList.get(i);
            }
        }
        return  null;
    }

	public List<SupportBankDto> getsBankDto() {
		return bankList;
	}

	public List<ProvinceInfoDto> getsProvince() {
		return provList;
	}

	public void setsBankDto(List<SupportBankDto> sBankDto) {
		this.bankList = sBankDto;
	}

	public void setsProvince(List<ProvinceInfoDto> sProvince) {
		this.provList = sProvince;
	}

	@Override
	public String toString() {
		return "SupportBankAndProvinceDto [sBankDto=" + bankList + ", sProvince=" + provList + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeList(bankList);
		dest.writeList(provList);
	}

	public static final Creator<SupportBankAndProvinceDto> CREATOR = new Creator<SupportBankAndProvinceDto>() {

		@Override
		public SupportBankAndProvinceDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			SupportBankAndProvinceDto _newsListBean = new SupportBankAndProvinceDto();
			_newsListBean.contentCode = source.readInt();
			_newsListBean.contentMsg = source.readString();
			source.readTypedList(_newsListBean.bankList, SupportBankDto.CREATOR);
			source.readTypedList(_newsListBean.provList, ProvinceInfoDto.CREATOR);
			return _newsListBean;
		}

		@Override
		public SupportBankAndProvinceDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SupportBankAndProvinceDto[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}