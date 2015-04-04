package howbuy.android.bean;

/**
 * 用于组成上传的Json
 * 
 * @author Administrator
 * 
 */
public class UpdateJsonBase {
	String version;;
	String channelId;
	String productId;
	String parPhoneModel;
	String subPhoneModel;
	String token;
	String iVer;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getParPhoneModel() {
		return parPhoneModel;
	}

	public void setParPhoneModel(String parPhoneModel) {
		this.parPhoneModel = parPhoneModel;
	}

	public String getSubPhoneModel() {
		return subPhoneModel;
	}

	public void setSubPhoneModel(String subPhoneModel) {
		this.subPhoneModel = subPhoneModel;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getiVer() {
		return iVer;
	}

	public void setiVer(String iVer) {
		this.iVer = iVer;
	}

}
