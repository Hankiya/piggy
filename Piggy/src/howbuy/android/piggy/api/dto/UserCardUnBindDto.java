package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014/8/23.
 */
public class UserCardUnBindDto extends  TopHeaderDto implements Parcelable {
    /**
     * //注销结果. 1: 注销成功,否则失败.
     */
      private String   unregisterRslt     ;              //注销结果. 1: 注销成功,否则失败.
    /**
     * 注销描述
     */
      private String      unregisterDesc     ;           //注销描述

    public String getUnregisterDesc() {
        return unregisterDesc;
    }

    public void setUnregisterDesc(String unregisterDesc) {
        this.unregisterDesc = unregisterDesc;
    }

    public String getUnregisterRslt() {
        return unregisterRslt;
    }

    public void setUnregisterRslt(String unregisterRslt) {
        this.unregisterRslt = unregisterRslt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "UserCardUnBindDto{" +
                "unregisterRslt='" + unregisterRslt + '\'' +
                ", unregisterDesc='" + unregisterDesc + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unregisterRslt);
        dest.writeString(this.unregisterDesc);
        dest.writeInt(this.contentCode);
        dest.writeString(this.contentMsg);
        dest.writeString(this.specialCode);
    }

    public UserCardUnBindDto() {
    }

    private UserCardUnBindDto(Parcel in) {
        this.unregisterRslt = in.readString();
        this.unregisterDesc = in.readString();
        this.contentCode = in.readInt();
        this.contentMsg = in.readString();
        this.specialCode = in.readString();
    }

    public static final Parcelable.Creator<UserCardUnBindDto> CREATOR = new Parcelable.Creator<UserCardUnBindDto>() {
        public UserCardUnBindDto createFromParcel(Parcel source) {
            return new UserCardUnBindDto(source);
        }

        public UserCardUnBindDto[] newArray(int size) {
            return new UserCardUnBindDto[size];
        }
    };
}
