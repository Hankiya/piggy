package howbuy.android.piggy.api.dto;

public class WebviewReqDto{
	/**
	 * 状态
	 */
	public String contentCode;
	/**
	 * 说明
	 */
	public String contentMsg;

	/**
	 * 响应内容体
	 */
	private Object body;

	public String getContentCode() {
		return contentCode;
	}

	public String getContentMsg() {
		return contentMsg;
	}

	public Object getBody() {
		return body;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public void setContentMsg(String contentMsg) {
		this.contentMsg = contentMsg;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "WebviewReqDto [contentCode=" + contentCode + ", contentMsg=" + contentMsg + ", body=" + body + "]";
	}
	
	
	
}
