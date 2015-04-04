package howbuy.android.piggy.api.dto;

public class WebviewReqDto extends TopHeaderDto{

	/**
	 * 响应内容体
	 */
	private Object body;

	public Object getBody() {
		return body;
	}


	public void setBody(Object body) {
		this.body = body;
	}


	@Override
	public String toString() {
		return "WebviewReqDto [body=" + body + ", contentCode=" + contentCode + ", contentMsg=" + contentMsg + ", specialCode=" + specialCode + "]";
	}


	

	
	
	
	
}
