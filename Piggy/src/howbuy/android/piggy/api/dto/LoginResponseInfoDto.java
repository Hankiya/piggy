/**
 * @Title: LoginResponseInfoDto.java
 * @Package com.howbuy.trade.core.dto.piggybank
 * @Description: 
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午02:09:03
 * @version V1.0
 */
package howbuy.android.piggy.api.dto;

/**
 * @ClassName: LoginResponseInfoDto
 * @Description: 登录响应信息
 * @author Polo Yuan lizhuan.yuan@howbuy.com
 * @date 2013-9-6 下午02:09:03
 * 
 */
public class LoginResponseInfoDto {

	/**
	 * 登录
	 */
	private int remainingNumber;

	/**
	 * @return the remainingNumber
	 */
	public int getRemainingNumber() {
		return remainingNumber;
	}

	/**
	 * @param remainingNumber
	 *            the remainingNumber to set
	 */
	public void setRemainingNumber(int remainingNumber) {
		this.remainingNumber = remainingNumber;
	}
}
