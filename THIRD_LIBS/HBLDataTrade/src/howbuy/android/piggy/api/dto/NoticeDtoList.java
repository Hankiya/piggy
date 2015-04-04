package howbuy.android.piggy.api.dto;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通知类型
 * @author Administrator
 *
 */
public class NoticeDtoList extends TopHeaderDto implements Parcelable {
	ArrayList<NoticeDto> listNotice;

	public ArrayList<NoticeDto> getListNotice() {
		return listNotice;
	}

	public void setListNotice(ArrayList<NoticeDto> listNotice) {
		this.listNotice = listNotice;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeTypedList(listNotice);
		dest.writeInt(contentCode);
		dest.writeString(contentMsg);
	}

	public static final Creator<NoticeDtoList> CREATOR = new Creator<NoticeDtoList>() {

		@Override
		public NoticeDtoList createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NoticeDtoList td = new NoticeDtoList();
			source.readTypedList(td.listNotice, NoticeDto.CREATOR);
			td.contentCode = source.readInt();
			td.contentMsg = source.readString();
			return td;
		}

		@Override
		public NoticeDtoList[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NoticeDtoList[size];
		}

	};
}
