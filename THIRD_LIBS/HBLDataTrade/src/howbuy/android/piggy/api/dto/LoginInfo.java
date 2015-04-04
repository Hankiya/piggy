package howbuy.android.piggy.api.dto;

public class LoginInfo {
	String idNo;
	String password;

	public LoginInfo(String mIdNo, String mPassword) {
		this.idNo = mIdNo;
		this.password = mPassword;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
