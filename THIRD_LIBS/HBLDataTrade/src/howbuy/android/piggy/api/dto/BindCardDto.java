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
public class BindCardDto extends TopHeaderDto implements Parcelable {
	BindCardInf mBindCardInf = null;

	/**
	 * @param evn
	 *            生产环境：PRODUCT 测试环境：TEST
	 * @return
	 */
	public String buildOrderInf(String evn) {
		return mBindCardInf.buildOrderInf(evn);

	}

	public boolean hasOrderInf() {
		return mBindCardInf.hasOrderInf();
	}

	public BindCardInf getmBindCardInf() {
		return mBindCardInf;
	}

	public void setmBindCardInf(BindCardInf mBindCardInf) {
		this.mBindCardInf = mBindCardInf;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
		dest.writeParcelable(mBindCardInf, 0);
	}

	public static final Creator<BindCardDto> CREATOR = new Creator<BindCardDto>() {

		@Override
		public BindCardDto createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			BindCardDto _newsListBean = new BindCardDto();
			_newsListBean.contentCode = source.readInt();
			_newsListBean.contentMsg = source.readString();
			_newsListBean.mBindCardInf = source.readParcelable(BindCardInf.class.getClassLoader());
			return _newsListBean;
		}

		@Override
		public BindCardDto[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BindCardDto[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "BindCardDto [mBindCardInf=" + mBindCardInf + ", toString()=" + super.toString() + "]";
	}
	
	
	
}