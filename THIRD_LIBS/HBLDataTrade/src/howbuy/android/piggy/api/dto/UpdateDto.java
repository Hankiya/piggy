package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Administrator
 *
 */
public class UpdateDto extends TopHeaderDto implements Parcelable {
	private String versionNeedUpdate;
	private String updateUrl;
	private String updateDesc;
	

	public String getVersionNeedUpdate() {
		return versionNeedUpdate;
	}

	public void setVersionNeedUpdate(String versionNeedUpdate) {
		this.versionNeedUpdate = versionNeedUpdate;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getUpdateDesc() {
		return updateDesc;
	}

	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}

	
	
	@Override
	public String toString() {
		return "UpdateDto [versionNeedUpdate=" + versionNeedUpdate + ", updateUrl=" + updateUrl + ", updateDesc=" + updateDesc + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(contentMsg);
		dest.writeString(updateUrl);
		dest.writeString(updateDesc);
		dest.writeString(versionNeedUpdate);
		dest.writeInt(contentCode);
	}

	public static final Creator<UpdateDto> CREATOR = new Creator<UpdateDto>() {

		@Override
		public UpdateDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			UpdateDto oDto = new UpdateDto();
			oDto.contentCode = source.readInt();
			oDto.contentMsg = source.readString();
			oDto.updateDesc = source.readString();
			oDto.updateUrl = source.readString();
			oDto.versionNeedUpdate= source.readString();
			return oDto;
		}

		@Override
		public UpdateDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UpdateDto[size];
		}
	};



	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
