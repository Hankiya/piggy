package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 交易日期
 * @ClassName: TradeDate.java
 * @Description: 
 * @author yescpu yes.cpu@gmail.com	
 * @date 2013-10-28下午3:07:26
 */
public class TradeDate extends TopHeaderDto implements Parcelable{
	private String confirmDt;
	private String paymentReceiptDt;
	
	/**
	 * 确认日期
	 * @return
	 */
	public String getConfirmDt() {
		return confirmDt;
	}

	public void setConfirmDt(String confirmDt) {
		this.confirmDt = confirmDt;
	}

	/**
	 * 赎回日期
	 * @return
	 */
	public String getPaymentReceiptDt() {
		return paymentReceiptDt;
	}

	public void setPaymentReceiptDt(String paymentReceiptDt) {
		this.paymentReceiptDt = paymentReceiptDt;
	}

	@Override
	public String toString() {
		return "TradeDate [confirmDt=" + confirmDt + ", paymentReceiptDt=" + paymentReceiptDt + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.confirmDt);
		dest.writeString(this.paymentReceiptDt);
		dest.writeString(this.contentMsg);
		dest.writeInt(this.contentCode);
	}
	
	public static final Creator<TradeDate> CREATOR = new Creator<TradeDate>() {

		@Override
		public TradeDate createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TradeDate td=new TradeDate();
			td.confirmDt=source.readString();
			td.paymentReceiptDt=source.readString();
			td.contentCode = source.readInt();
			td.contentMsg = source.readString();
			return td;
		}

		@Override
		public TradeDate[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TradeDate[size];
		}
		
	};

}
