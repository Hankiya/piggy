/**
 * @Title: SercurityInfoDto.java
 * @Package com.howbuy.trade.core.dto.piggybank
 * @Description: 
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:57:20
 * @version V1.0
 */
package howbuy.android.piggy.api.dto;

/**
 * @ClassName: SercurityInfoDto
 * @Description: 加密信息
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午04:57:20
 * 
 */
public class RespondCipher {
	private String returnEncMsg;

	private String returnSignMsg;

	/**
	 * @return the returnEncMsg
	 */
	public String getReturnEncMsg() {
		return returnEncMsg;
	}

	/**
	 * @param returnEncMsg
	 *            the returnEncMsg to set
	 */
	public void setReturnEncMsg(String returnEncMsg) {
		this.returnEncMsg = returnEncMsg;
	}

	/**
	 * @return the returnSignMsg
	 */
	public String getReturnSignMsg() {
		return returnSignMsg;
	}

	/**
	 * @param returnSignMsg
	 *            the returnSignMsg to set
	 */
	public void setReturnSignMsg(String returnSignMsg) {
		this.returnSignMsg = returnSignMsg;
	}

	@Override
	public String toString() {
		return "SercurityInfoDto [returnEncMsg=" + returnEncMsg + ", returnSignMsg=" + returnSignMsg + "]";
	}

}