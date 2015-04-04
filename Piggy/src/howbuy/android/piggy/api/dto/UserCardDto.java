package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class UserCardDto implements Parcelable {
	//1-未开通；2-开通；3-关闭
	public static final String Flag_BANKACCTUN = "1";
	public static final String Flag_BANKACCTSuccess = "2";
	public static final String Flag_BANKACCTClose = "3";


	private String bankAcct; // 银行卡卡号（只显示4位尾号，其他为*号）
	private String bankCode; // 银行代码
	private String bankName; // 银行卡所属银行
	private String paySign; // 是否支持代扣
    private String provCode;//省份Id
	private String acctIdentifyStat; // 银行卡鉴权状态（1:未鉴权、2:鉴权中、3:已鉴权、锁定状态）
	private String bankAcctVrfyStat; // 银行账户验证状态（1-未验证，2-验证通过，3-验证失败）
	private String bankRegionCode; // 支行号
	private String bankRegionName; // 支行名称
	/**
	 * 银行卡Id
	 */
	private String custBankId; //银行卡Id
	private String spNumber; // 银行客服电话
	private String limitPerTime; // 单笔支付限额
	private String limitPerDay; // 日限额
    private String minPayLimit;//支付额度下限

	private String defaultFlag; // 1表示该卡为默认卡，0表示非默认卡
	
	//以下字段为银行卡份额用的字段
	private String availAmt;			//可用份额
	private String totalIncome;	//累计收益
	private String unCarryOverIncome;//	未付收益
	
	private String provName;//身份名称

	public UserCardDto(String bankAcct, String bankCode, String bankName, String provCode, String provName, String bankRegionCode,
			String bankRegionName, String custBankId) {
		super();
		this.bankAcct = bankAcct;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.provCode = provCode;
		this.bankRegionCode = bankRegionCode;
		this.bankRegionName = bankRegionName;
		this.custBankId = custBankId;
		this.provName = provName;
	}
	

    public UserCardDto() {
	}

    public String getBankAcct() {
		return bankAcct;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public String getPaySign() {
		return paySign;
	}

	public String getAcctIdentifyStat() {
		return acctIdentifyStat;
	}

	public String getBankAcctVrfyStat() {
		return bankAcctVrfyStat;
	}

	public String getBankRegionCode() {
		return bankRegionCode;
	}

	public String getBankRegionName() {
		return bankRegionName;
	}

	public String getCustBankId() {
		return custBankId;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public String getLimitPerTime() {
		return limitPerTime;
	}

	public String getLimitPerDay() {
		return limitPerDay;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public void setAcctIdentifyStat(String acctIdentifyStat) {
		this.acctIdentifyStat = acctIdentifyStat;
	}

	public void setBankAcctVrfyStat(String bankAcctVrfyStat) {
		this.bankAcctVrfyStat = bankAcctVrfyStat;
	}

	public void setBankRegionCode(String bankRegionCode) {
		this.bankRegionCode = bankRegionCode;
	}

	public void setBankRegionName(String bankRegionName) {
		this.bankRegionName = bankRegionName;
	}

	public void setCustBankId(String custBankId) {
		this.custBankId = custBankId;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public void setLimitPerTime(String limitPerTime) {
		this.limitPerTime = limitPerTime;
	}

	public void setLimitPerDay(String limitPerDay) {
		this.limitPerDay = limitPerDay;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }
    
    

    public String getAvailAmt() {
		return availAmt;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public String getUnCarryOverIncome() {
		return unCarryOverIncome;
	}

	public void setAvailAmt(String availAmt) {
		this.availAmt = availAmt;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}



	public void setUnCarryOverIncome(String unCarryOverIncome) {
		this.unCarryOverIncome = unCarryOverIncome;
	}

    public String getMinPayLimit() {
        return minPayLimit;
    }

    public void setMinPayLimit(String minPayLimit) {
        this.minPayLimit = minPayLimit;
    }

    
    @Override
    public String toString() {
        return "UserCardDto{" +
                "bankAcct='" + bankAcct + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", paySign='" + paySign + '\'' +
                ", provCode='" + provCode + '\'' +
                ", acctIdentifyStat='" + acctIdentifyStat + '\'' +
                ", bankAcctVrfyStat='" + bankAcctVrfyStat + '\'' +
                ", bankRegionCode='" + bankRegionCode + '\'' +
                ", bankRegionName='" + bankRegionName + '\'' +
                ", custBankId='" + custBankId + '\'' +
                ", spNumber='" + spNumber + '\'' +
                ", limitPerTime='" + limitPerTime + '\'' +
                ", limitPerDay='" + limitPerDay + '\'' +
                ", minPayLimit='" + minPayLimit + '\'' +
                ", defaultFlag='" + defaultFlag + '\'' +
                ", availAmt='" + availAmt + '\'' +
                ", totalIncome='" + totalIncome + '\'' +
                ", unCarryOverIncome='" + unCarryOverIncome + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankAcct);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankName);
        dest.writeString(this.paySign);
        dest.writeString(this.provCode);
        dest.writeString(this.acctIdentifyStat);
        dest.writeString(this.bankAcctVrfyStat);
        dest.writeString(this.bankRegionCode);
        dest.writeString(this.bankRegionName);
        dest.writeString(this.custBankId);
        dest.writeString(this.spNumber);
        dest.writeString(this.limitPerTime);
        dest.writeString(this.limitPerDay);
        dest.writeString(this.minPayLimit);
        dest.writeString(this.defaultFlag);
        dest.writeString(this.availAmt);
        dest.writeString(this.totalIncome);
        dest.writeString(this.unCarryOverIncome);
    }

    private UserCardDto(Parcel in) {
        this.bankAcct = in.readString();
        this.bankCode = in.readString();
        this.bankName = in.readString();
        this.paySign = in.readString();
        this.provCode = in.readString();
        this.acctIdentifyStat = in.readString();
        this.bankAcctVrfyStat = in.readString();
        this.bankRegionCode = in.readString();
        this.bankRegionName = in.readString();
        this.custBankId = in.readString();
        this.spNumber = in.readString();
        this.limitPerTime = in.readString();
        this.limitPerDay = in.readString();
        this.minPayLimit = in.readString();
        this.defaultFlag = in.readString();
        this.availAmt = in.readString();
        this.totalIncome = in.readString();
        this.unCarryOverIncome = in.readString();
    }

    public static final Creator<UserCardDto> CREATOR = new Creator<UserCardDto>() {
        public UserCardDto createFromParcel(Parcel source) {
            return new UserCardDto(source);
        }

        public UserCardDto[] newArray(int size) {
            return new UserCardDto[size];
        }
    };
}
