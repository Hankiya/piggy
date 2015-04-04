/**
 * 
 */
package howbuy.android.piggy.api.dto;

/**
 * 
 * @ClassName: HeaderInfoDto
 * @Description: 响应消息头部信息
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-8-20 下午01:33:19
 * 
 */
public class HeaderInfoDto {
	/**
	 * 响应码
	 */
	private String responseCode;

	/**
	 * 响应描述
	 */
	private String responseDesc;
	
	/**
	 * 响应码
	 */
	private String contentCode;
	
	/**
	 * 响应描述
	 */
	private String contentDesc;

	public String getResponseCode() {
		if (responseCode==null) {
			responseCode="";
		}
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public String getContentCode() {
		if (responseCode==null) {
			responseCode="";
		}
		return contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	@Override
	public String toString() {
		return "HeaderInfoDto [responseCode=" + responseCode + ", responseDesc=" + responseDesc + ", contentCode=" + contentCode + ", contentDesc=" + contentDesc + "]";
	}


	

}
