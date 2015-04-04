package howbuy.android.piggy.api.dto;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014/8/22.
 */
public class UserCardListDto extends TopHeaderDto implements Parcelable {
    private List<UserCardDto> userCardDtos;


    @Override
    public int describeContents() {
        return 0;
    }
    
    

    public List<UserCardDto> getUserCardDtos() {
		return userCardDtos;
	}



	public void setUserCardDtos(List<UserCardDto> userCardDtos) {
		this.userCardDtos = userCardDtos;
	}



	@Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(userCardDtos);
        dest.writeInt(this.contentCode);
        dest.writeString(this.contentMsg);
        dest.writeString(this.specialCode);
    }

    public UserCardListDto() {
    }

    private UserCardListDto(Parcel in) {
        in.readTypedList(userCardDtos, UserCardDto.CREATOR);
        this.contentCode = in.readInt();
        this.contentMsg = in.readString();
        this.specialCode = in.readString();
    }

    public static final Parcelable.Creator<UserCardListDto> CREATOR = new Parcelable.Creator<UserCardListDto>() {
        public UserCardListDto createFromParcel(Parcel source) {
            return new UserCardListDto(source);
        }

        public UserCardListDto[] newArray(int size) {
            return new UserCardListDto[size];
        }
    };
    
    public UserCardDto getCardByIdOrAcct(String bankIdOrAcct){
    	if (userCardDtos!=null) {
			for (int i = 0; i < userCardDtos.size(); i++) {
				UserCardDto dto =userCardDtos.get(i);
				String id=dto.getCustBankId();
				String account=dto.getBankAcct();
				if (id.equals(bankIdOrAcct)||account.equals(bankIdOrAcct)) {
					return dto;
				}
			}
		}
    	return null;
    }
    
    public UserCardDto getDefaultCard(){
    	if (userCardDtos!=null) {
    		UserCardDto dto = null;
			for (int i = 0; i < userCardDtos.size(); i++) {
				UserCardDto tDto =userCardDtos.get(i);
				String df=tDto.getDefaultFlag();
				if (df!=null&&df.equals("1")) {
					dto=tDto;
					return dto;
				}
			}
			
			if (dto==null) {
				dto=userCardDtos.get(0);
			}
			return dto;
		}
    	return null;
    }



	@Override
	public String toString() {
		return "UserCardListDto [userCardDtos=" + userCardDtos + "]";
	}
    
    
}
