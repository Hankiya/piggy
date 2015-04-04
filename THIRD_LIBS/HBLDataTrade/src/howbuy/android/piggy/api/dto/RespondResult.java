/**
 * @Title: ResponseDto.java
 * @Package com.howbuy.trade.core.dto.piggybank
 * @Description: 
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:19:29
 * @version V1.0
 */
package howbuy.android.piggy.api.dto;

import com.howbuy.datalib.trade.GsonUtils;

/**
 * @ClassName: ResponseDto
 * @Description: 响应信息
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:19:29
 */
public class RespondResult {
	/**
	 * 签名串过期
	 */
	public static final String VERIFICATION_CODE_EXPIRED = "9010";
	/**
	 * 签名串错误
	 */
	public static final String VERIFICATION_CODE_Error = "9006";
	/**
	 * 明文
	 */
	public static final String RESPONSE_CONTENT_TYPE_PLATEXT = "1";
	/**
	 * 密文
	 */
	public static final String RESPONSE_CONTENT_TYPE_CIPHERTEXT = "0";

	/**
	 * 响应成功
	 */
	public static final String RESPONSE_RES_SUCCESS = "0000";

	/**
	 * 强制登陆
	 */
	public static final String RESPONSE_RES_FORCELOGIN = "B9000";

	/**
	 * 非银联渠道B9800
	 */
	public static final String RESPONSE_RES_BindCard_Other = "B9800";

	/**
	 * 未鉴权限额
	 */
	public static final String RESPONSE_RES_Beyound = "1222";
	/**
	 * 未鉴权限额
	 */
	public static final String RESPONSE_RES_leater = "4007";

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

	public boolean isExpress() {
		return RESPONSE_CONTENT_TYPE_PLATEXT.equals(contentType);
	}

	public RespondCipher getCipher() {
		if (!RESPONSE_CONTENT_TYPE_PLATEXT.equals(contentType)) {
			return GsonUtils.getCiphertext(content);
		}
		return null;
	}

	public RespondExpress getExpress() {
		if (RESPONSE_CONTENT_TYPE_PLATEXT.equals(contentType)) {
			return GsonUtils.getExpressly(content);
		}
		return null;
	}
}