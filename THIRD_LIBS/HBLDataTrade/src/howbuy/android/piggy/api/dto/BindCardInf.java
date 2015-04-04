
package howbuy.android.piggy.api.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


/**
 * 绑定银行卡
 * @author Administrator
 *
 */
public class BindCardInf implements Parcelable {
	private String merchantId;
	private String orderNo;
	private String authDate;
	private String cpAttributeNo;
	private String sign;
	private String channelId;

	/**
	 * @param evn
	 *            生产环境：PRODUCT 测试环境：TEST
	 * @return
	 */
	public String buildOrderInf(String evn) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<CpPay application=\"LunchPay.Req\">" + "<env>" + evn + "</env>" + "<merchantId>" + merchantId + "</merchantId>"
				+ "<merchantOrderId>" + orderNo + "</merchantOrderId>" + "<merchantOrderTime>" + authDate + "</merchantOrderTime>" + "<orderKey>" + cpAttributeNo
				+ "</orderKey>" + "<sign>" + sign + "</sign>" + "</CpPay>";

	}

	public boolean hasOrderInf() {
		return !(TextUtils.isEmpty(merchantId) || TextUtils.isEmpty(orderNo) || TextUtils.isEmpty(authDate) || TextUtils.isEmpty(cpAttributeNo) || TextUtils
				.isEmpty(sign));
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantOrderId() {
		return orderNo;
	}

	public void setMerchantOrderId(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMerchantOrderTime() {
		return authDate;
	}

	public void setMerchantOrderTime(String authDate) {
		this.authDate = authDate;
	}

	public String getOrderKey() {
		return cpAttributeNo;
	}

	public void setOrderKey(String cpAttributeNo) {
		this.cpAttributeNo = cpAttributeNo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public String getCpAttributeNo() {
		return cpAttributeNo;
	}

	public void setCpAttributeNo(String cpAttributeNo) {
		this.cpAttributeNo = cpAttributeNo;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public static Creator<BindCardInf> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(merchantId);
		dest.writeString(orderNo);
		dest.writeString(authDate);
		dest.writeString(cpAttributeNo);
		dest.writeString(sign);
		dest.writeString(channelId);
	}

	public static final Creator<BindCardInf> CREATOR = new Creator<BindCardInf>() {

		@Override
		public BindCardInf createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			BindCardInf _newsListBean = new BindCardInf();
			_newsListBean.merchantId = source.readString();
			_newsListBean.orderNo = source.readString();
			_newsListBean.authDate = source.readString();
			_newsListBean.cpAttributeNo = source.readString();
			_newsListBean.sign = source.readString();
			_newsListBean.channelId = source.readString();
			return _newsListBean;
		}

		@Override
		public BindCardInf[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BindCardInf[size];
		}
	};

	@Override
	public String toString() {
		return "BindCardInf [merchantId=" + merchantId + ", orderNo=" + orderNo + ", authDate=" + authDate + ", cpAttributeNo=" + cpAttributeNo + ", sign=" + sign + ", channelId="
				+ channelId + "]";
	}

	
	
}