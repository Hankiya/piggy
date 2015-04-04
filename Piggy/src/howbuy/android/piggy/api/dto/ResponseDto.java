/**
 * @Title: ResponseDto.java
 * @Package com.howbuy.trade.core.dto.piggybank
 * @Description: 
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:19:29
 * @version V1.0
 */
package howbuy.android.piggy.api.dto;

/**
 * @ClassName: ResponseDto
 * @Description: 响应信息
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:19:29
 * 
 */
public class ResponseDto {

	/**
	 * 响应内容类型，1：明文，2：密文
	 */
	private String contentType;

	/**
	 * 响应内容
	 */

	private Object content;

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(Object content) {
		this.content = content;
	}
}