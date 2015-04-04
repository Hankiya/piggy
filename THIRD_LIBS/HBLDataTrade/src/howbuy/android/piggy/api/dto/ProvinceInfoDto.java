/**
 * 
 */
package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 身份
 * 
 * @ClassName: ProvinceInfoDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-9-27下午4:35:15
 */
public class ProvinceInfoDto implements Parcelable {

	private String code;
	private String name;
	private String defaultCityCode;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the defaultCityCode
	 */
	public String getDefaultCityCode() {
		return defaultCityCode;
	}

	/**
	 * @param defaultCityCode
	 *            the defaultCityCode to set
	 */
	public void setDefaultCityCode(String defaultCityCode) {
		this.defaultCityCode = defaultCityCode;
	}

	@Override
	public String toString() {
		return "ProvinceInfoDto [code=" + code + ", name=" + name + ", defaultCityCode=" + defaultCityCode + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(code);
		dest.writeString(name);
		dest.writeString(defaultCityCode);

	}

	public static final Creator<ProvinceInfoDto> CREATOR = new Creator<ProvinceInfoDto>() {

		@Override
		public ProvinceInfoDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ProvinceInfoDto _newsListBean = new ProvinceInfoDto();
			_newsListBean.code = source.readString();
			_newsListBean.name = source.readString();
			_newsListBean.defaultCityCode = source.readString();
			return _newsListBean;
		}

		@Override
		public ProvinceInfoDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProvinceInfoDto[size];
		}
	};

}