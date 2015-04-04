/**
 * 
 */
package howbuy.android.piggy.api.dto;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 绑卡以及鉴权返回结果
 * 身份
 * 
 * @ClassName: ProvinceInfoDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-9-27下午4:35:15
 */
public class BindCardDto extends TopHeaderDto implements Parcelable {

	BindCardInf encParam = null; // cp params
	List<BindCardAuthModeDto> authMode; // 鉴权方式
	String channelId; // 绑卡、验卡渠道号

	public BindCardDto(){}
	
	
	/**
	 * @param evn
	 *            生产环境：PRODUCT 测试环境：TEST
	 * @return
	 */
	public String buildOrderInf(String evn) {
		return encParam.buildOrderInf(evn);

	}

	/**
	 * 获取默认的鉴权方式
	 * @return
	 */
	public BindCardAuthModeDto getDefaultAuth() {
		List<BindCardAuthModeDto> authModeDtos = getAuthMode();
		for (BindCardAuthModeDto bindCardAuthModeDto : authModeDtos) {
			if (BindCardAuthModeDto.DEFAULT1.equals(bindCardAuthModeDto.getIsDefault())) {
				return bindCardAuthModeDto;
			}
		}
		return null;
	}

	public boolean hasOrderInf() {
		return encParam.hasOrderInf();
	}

	public BindCardInf getmBindCardInf() {
		return encParam;
	}

	public void setmBindCardInf(BindCardInf mBindCardInf) {
		this.encParam = mBindCardInf;
	}

	public BindCardInf getEncParam() {
		return encParam;
	}

	public void setEncParam(BindCardInf encParam) {
		this.encParam = encParam;
	}

	public List<BindCardAuthModeDto> getAuthMode() {
		return authMode;
	}

	public void setAuthMode(List<BindCardAuthModeDto> authMode) {
		this.authMode = authMode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "BindCardDto{" + "encParam=" + encParam + ", authMode=" + authMode + ", channelId='" + channelId + '\'' + '}';
	}

	public BindCardDto(BindCardInf encParam, List<BindCardAuthModeDto> authMode, String channelId) {
		this.encParam = encParam;
		this.authMode = authMode;
		this.channelId = channelId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.encParam, 0);
		dest.writeTypedList(authMode);
		dest.writeString(this.channelId);
		dest.writeString(this.contentMsg);
		dest.writeInt(this.contentCode);
	}

	private BindCardDto(Parcel in) {
		this.encParam = in.readParcelable(BindCardInf.class.getClassLoader());
		in.readTypedList(authMode, BindCardAuthModeDto.CREATOR);
		this.channelId = in.readString();
		this.contentMsg=in.readString();
		this.contentCode=in.readInt();
	}

	public static final Parcelable.Creator<BindCardDto> CREATOR = new Parcelable.Creator<BindCardDto>() {
		public BindCardDto createFromParcel(Parcel source) {
			return new BindCardDto(source);
		}

		public BindCardDto[] newArray(int size) {
			return new BindCardDto[size];
		}
	};
}