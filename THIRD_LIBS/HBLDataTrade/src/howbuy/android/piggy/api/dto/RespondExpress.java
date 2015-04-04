/**
 * 
 */
package howbuy.android.piggy.api.dto;


/**
 * @ClassName: ResponseContentDto
 * @Description: 响应内容
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-8-20 下午01:34:22
 * 
 */
public class RespondExpress {

	/**
	 * 响应内容头部
	 */
	private HeaderInfoDto header;

	/**
	 * 响应内容体
	 */
	private Object body;

	/**
	 * @return the header
	 */
	public HeaderInfoDto getHeader() {
		return header;
	}

	/**
	 * @param header
	 *            the header to set
	 */
	public void setHeader(HeaderInfoDto header) {
		this.header = header;
	}

	/**
	 * @return the body
	 */
	public Object getBody() {
		return body == null ? "{}" : body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ResponseContentDto [header=" + header + ", body=" + body + "]";
	}

}