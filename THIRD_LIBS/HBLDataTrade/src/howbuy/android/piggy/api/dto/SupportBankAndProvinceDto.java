package howbuy.android.piggy.api.dto;

import java.util.HashMap;
import java.util.Map;

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
	Map<String, SupportBankDto> sBankDto;
	Map<String, ProvinceInfoDto> sProvince;

	public Map<String, SupportBankDto> getsBankDto() {
		return sBankDto;
	}

	public void setsBankDto(Map<String, SupportBankDto> sBankDto) {
		this.sBankDto = sBankDto;
	}

	public Map<String, ProvinceInfoDto> getsProvince() {
		return sProvince;
	}

	public void setsProvince(Map<String, ProvinceInfoDto> sProvince) {
		this.sProvince = sProvince;
	}

	@Override
	public String toString() {
		return "SupportBankAndProvinceDto [sBankDto=" + sBankDto + ", sProvince=" + sProvince + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeMap(sBankDto);
		dest.writeMap(sProvince);
	}

	public static final Creator<SupportBankAndProvinceDto> CREATOR = new Creator<SupportBankAndProvinceDto>() {

		@Override
		public SupportBankAndProvinceDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			SupportBankAndProvinceDto _newsListBean = new SupportBankAndProvinceDto();
			_newsListBean.contentCode = source.readInt();
			_newsListBean.contentMsg = source.readString();
			_newsListBean.sBankDto = source.readHashMap(HashMap.class.getClassLoader());
			_newsListBean.sProvince = source.readHashMap(ProvinceInfoDto.class.getClassLoader());
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