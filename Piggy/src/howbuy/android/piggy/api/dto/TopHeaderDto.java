package howbuy.android.piggy.api.dto;

/**
 * 为App交互的最后一层，把网络数据转换成最终app要使用的数据
 * 
 * @ClassName: TopHeaderDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-11下午4:46:35
 */
public class TopHeaderDto {
	/**
	 * 状态
	 */
	public int contentCode;
	/**
	 * 说明
	 */
	public String contentMsg;
	
	/**
	 * 特殊业务代码
	 */
	public String specialCode; 

	public int getContentCode() {
		return contentCode;
	}

	public void setContentCode(int contentCode) {
		this.contentCode = contentCode;
	}

	public String getContentMsg() {
		return contentMsg;
	}

	public void setContentMsg(String contentMsg) {
		this.contentMsg = contentMsg;
	}
	
	

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	@Override
	public String toString() {
		return "TopHeaderDto [contentCode=" + contentCode + ", contentMsg=" + contentMsg + ", specialCode=" + specialCode + "]";
	}

	

}
