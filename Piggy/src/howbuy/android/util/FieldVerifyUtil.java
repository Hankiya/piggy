package howbuy.android.util;

import android.text.TextUtils;

/**
 * 验证字段
 * 
 * @author yescpu
 * 
 */
public class FieldVerifyUtil {
	public static final String Mach_Phone = "^1\\d{10}$";
	public static final String Mach_IdCode = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\\w$";
	public static final String Mach_LoginPwd = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
	// public static final String Mach_LoginPwd =
	// "^(?:([0-9])(?!\1{2})){6,12}$";
	public static final String Mach_TradePwd = "^\\d{6}$";
	public static final String Mach_UserName = "^\\w{2,10}$";
	public static final String Mach_BankCard = "^[0-9]{13,}$";

	public static class VerifyReslt {
		String msg;
		boolean success;

		public VerifyReslt(String msg, boolean success) {
			// TODO Auto-generated constructor stub
			this.msg = msg;
			this.success = success;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		@Override
		public String toString() {
			return "VerifyReslt [msg=" + msg + ", success=" + success + "]";
		}

	}

	/**
	 * 验证手机号码
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyPhone(String in) {
		if (isNull(in)) {
			return new VerifyReslt("手机号不能为空", false);
		}
		if (in.matches(Mach_Phone)) {
			return new VerifyReslt("", true);
		} else {
			return new VerifyReslt("手机号格式不正确", false);
		}
	}

	/**
	 * 验证身份证号码
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyId(String in) {
		if (isNull(in)) {
			return new VerifyReslt("身份证号不能为空", false);
		}
		if (in.matches(Mach_IdCode)) {
			String res=FieldVerifyCard.chekIdCard(0, in);
			if ("".equals(res)) {
				return new VerifyReslt("", true);
			}else {
				return new VerifyReslt(res, false);
			}
		} else {
			return new VerifyReslt("身份证号格式不正确", false);
		}

	}

	/**
	 * 验证登录密码
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyLoginPwd(String in) {
		if (isNull(in)) {
			return new VerifyReslt("登录密码不能为空", false);
		}
		if (in.matches(Mach_LoginPwd)) {
			return new VerifyReslt("", true);
		} else {
			return new VerifyReslt("登录密码应为6-12位字母与数字的组合", false);
		}
	}

	/**
	 * 验证交易密码
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyTradePwd(String in) {
		if (isNull(in)) {
			return new VerifyReslt("交易密码不能为空", false);
		}
		if (in.matches(Mach_TradePwd)) {
			if (validateSameNum(in)) {
				if (validateContinuousNum(in)) {
					return new VerifyReslt("", true);
				} else {
					return new VerifyReslt("交易密码应为6位不重复或不连续的数字", false);
				}
			} else {
				return new VerifyReslt("交易密码不能重复", false);
			}
		} else {
			return new VerifyReslt("交易密码应为6位不重复或不连续的数字", false);
		}

	}

	/**
	 * 交易密码重复
	 * 
	 * @param str
	 * @return
	 */
	public static boolean validateSameNum(String str) {
		final int max = str.length()-1;
		int overEdNum = str.length() - max;

		for (int i = 0; i < overEdNum ; i++) {
			String curr = str.substring(0 + i, max + i+1);
			String first = curr.substring(0, 1);
			if (curr.replace(first, "").equals("")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 交易密码连续
	 * 
	 * @param str
	 * @return
	 */
	public static boolean validateContinuousNum(String str) {
		//不能超过六个
		final int max = str.length();
		int overedNum = str.length() - max;
		for (int i = 0; i < overedNum+1; i++) {
			String curr = str.substring(0 + i, max + i);
			char[] c = curr.toCharArray();
			int s1 = 0,s2=0;
			for (int k = 0; k < c.length-1; k++) {
				int a=toUnicode(c[k]) + 1 ;
				int e=toUnicode(c[k]) - 1;
				int b=toUnicode(c[k+1]);
				if (a==b) {
					s1++;
				}
				if (b==e) {
					s2++;
				}
			}
			if (s1==max-1||s2==max-1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * toHexString
	 * @param a
	 * @return
	 */
	public static int toUnicode(char a) {
		// 十六进制显示
		String ch = Integer.toHexString((int) a);
		// 用0补齐四位
		ch = ch.substring(ch.length() - 2, ch.length());
		return Integer.parseInt(ch);
	}

	/**
	 * 验证验证码
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyMsgVer(String in) {
		if (isNull(in)) {
			return new VerifyReslt("验证码不能为空", false);
		}
		if (in.matches(Mach_TradePwd)) {
			return new VerifyReslt("", true);
		} else {
			return new VerifyReslt("验证码格式不正确", false);
		}

	}

	/**
	 * 验证银行卡号
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyBankCard(String in) {
		if (isNull(in)) {
			return new VerifyReslt("银行卡不能为空", false);
		}
		if (in.matches(Mach_BankCard)) {
			return new VerifyReslt("", true);
		} else {
			return new VerifyReslt("银行卡格式不正确", false);
		}
	}

	/**
	 * 验证用户名
	 * 
	 * @param in
	 * @param outMsg
	 * @return
	 */
	public static VerifyReslt verifyUserName(String in) {
		if (isNull(in)) {
			return new VerifyReslt("用户名不能为空", false);
		}
		if (in.matches(Mach_UserName)) {
			return new VerifyReslt("", true);
		} else {
			return new VerifyReslt("用户名格式不正确", false);
		}
	}
	
	/**
	 * 验证最低支持 @param decimalNum位小数
	 * @param in
	 * @param decimalNum
	 * @return
	 */
	public static VerifyReslt verifyAmount(String in,int decimalNum) {
		if (isNull(in)) {
			return new VerifyReslt("金额不能为空", false);
		}
		int index=in.indexOf(".")+1;
		if (index==0||in.startsWith(".")) {
			return new VerifyReslt("", true);
		}
		
		String res= in.substring(index, in.length());
		if (res.length()<=decimalNum) {
			return new VerifyReslt("", true);
		}else {
			return new VerifyReslt("最低支持"+decimalNum+"位小数", false);
		}
		
	}
	

	public static boolean isNull(String in) {
		if (TextUtils.isEmpty(in)) {
			return true;
		}
		return false;
	}
	
}
