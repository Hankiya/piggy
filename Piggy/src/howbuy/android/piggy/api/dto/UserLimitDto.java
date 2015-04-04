package howbuy.android.piggy.api.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 *	用户剩余额度查询
 * @author Administrator
 *
 */
public class UserLimitDto extends TopHeaderDto implements Parcelable {
	public static final String FlagOpen = "1";
	public static final String FlagClose = "0";
private String    blanceAmt  ;//实时取现总金额
private String            lastAmt  ;//实时取现剩余额度
private String    openFlag  ;//实时取现是否开通:0-关闭；1-开通
private String            maxAmtEach  ;//每笔金额上限/客户
private String    minAmtEach  ;//每笔金额下限/客户


    public String getBlanceAmt() {
        return blanceAmt;
    }

    public void setBlanceAmt(String blanceAmt) {
        this.blanceAmt = blanceAmt;
    }

    public String getLastAmt() {
        return lastAmt;
    }

    public void setLastAmt(String lastAmt) {
        this.lastAmt = lastAmt;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public String getMaxAmtEach() {
        return maxAmtEach;
    }

    public void setMaxAmtEach(String maxAmtEach) {
        this.maxAmtEach = maxAmtEach;
    }

    public String getMinAmtEach() {
        return minAmtEach;
    }

    public void setMinAmtEach(String minAmtEach) {
        this.minAmtEach = minAmtEach;
    }

    @Override
    public String toString() {
        return "UserLimitDto{" +
                "blanceAmt='" + blanceAmt + '\'' +
                ", lastAmt='" + lastAmt + '\'' +
                ", openFlag='" + openFlag + '\'' +
                ", maxAmtEach='" + maxAmtEach + '\'' +
                ", minAmtEach='" + minAmtEach + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blanceAmt);
        dest.writeString(this.lastAmt);
        dest.writeString(this.openFlag);
        dest.writeString(this.maxAmtEach);
        dest.writeString(this.minAmtEach);
        dest.writeString(this.contentMsg);
        dest.writeInt(this.contentCode);
    }

    public UserLimitDto() {
    }

    private UserLimitDto(Parcel in) {
        this.blanceAmt = in.readString();
        this.lastAmt = in.readString();
        this.openFlag = in.readString();
        this.maxAmtEach = in.readString();
        this.minAmtEach = in.readString();
        this.contentCode=in.readInt();
        this.contentMsg=in.readString();
    }

    public static final Creator<UserLimitDto> CREATOR = new Creator<UserLimitDto>() {
        public UserLimitDto createFromParcel(Parcel source) {
            return new UserLimitDto(source);
        }

        public UserLimitDto[] newArray(int size) {
            return new UserLimitDto[size];
        }
    };
}
