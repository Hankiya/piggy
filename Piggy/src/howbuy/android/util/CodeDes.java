package howbuy.android.util;

public interface CodeDes {
	public String getCode();

	public String getDescribe();

	final static CodeDes UNKNOW = new CodeDes() {
		@Override
		public String getCode() {
			return "-1";
		}

		@Override
		public String getDescribe() {
			return "未知状态";
		}

		public String toString() {
			return "code=" + getCode() + ",describe=" + getDescribe();
		};
	};

	public class Parser {
		public static boolean isUnKnow(CodeDes r) {
			return UNKNOW == r;
		}

		public static CodeDes parse(CodeDes[] enumValues, String code) {
			CodeDes r = null;
			if (enumValues != null) {
				for (CodeDes val : enumValues) {
					if (val.getCode().equals(code)) {
						return val;
					}
				}
			}
			return r == null ? UNKNOW : r;
		}

		public static String getDescribe(CodeDes[] enumValues, String code) {
			return parse(enumValues, code).getDescribe();
		}
	}

	/**
	 * 银行卡鉴权状态
	 * 
	 * 1-未鉴权；2-鉴权中；3-鉴权失败；4-鉴权通过
	 * 
	 * @author Administrator
	 * 
	 */
	public enum BankAuthState implements CodeDes {
		Auth_Success("4", "鉴权成功"), Auth_Fail("3", "鉴权失败"), Auth_Never("1", "未鉴权"), Auth_Await("2", "鉴权中");
		private BankAuthState(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		public String getCode() {
			return code;
		}

		public String getDescribe() {
			return codeDes;
		}

		private String code = null;
		private String codeDes = null;
	}

	/**
	 * 银行卡代扣状态
	 * 
	 * 1：已签，2：未签
	 * 
	 * @author Administrator
	 * 
	 */
	public enum BankSignState implements CodeDes {
		Sign_Success("1", "已签"), Sign_Never("2", "未签");
		private BankSignState(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		public String getCode() {
			return code;
		}

		public String getDescribe() {
			return codeDes;
		}

		private String code = null;
		private String codeDes = null;
	}

	public enum BusiType implements CodeDes {
		BT_RG("0", "认购"), BT_SG("1", "申购"), BT_SH("2", "赎回"), BT_FH("3", "分红"), BT_JH("4", "基金转换"), BT_DT("5", "定投"), BT_XS("6", "修改分红方式"), BT_QZ("7", "强增"), BT_QJ("8", "强减");
		private String code = null;
		private String codeDes = null;

		private BusiType(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		public String getCode() {
			return code;
		}

		public String getDescribe() {
			return codeDes;
		}
	}

	public enum FundType implements CodeDes {
		FT_GP("0", "股票型"), FT_HH("1", "混合型"), FT_ZQ("2", "债券型"), FT_HB("3", "货币型"), FT_QD("4", "QDII"), FT_FB("5", "封闭式"), FT_JG("6", "结构型"), FT_YD("7", "一对多专户");
		private String code = null;
		private String codeDes = null;

		FundType(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}

	public enum LevelType implements CodeDes {
		LT_LL("1", "低风险"), LT_LM("2", "低中风险"), LT_MM("3", "中风险"), LT_HM("4", "高中风险 "), LT_HH("5", "高风险"), LT_OTHER("9", "其它");
		private String code = null;
		private String codeDes = null;

		LevelType(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}

	// 0-无状态（收益发放无状态），1-交易失败，2-等待付款，3-付款中，4-付款成功，5-部分成功，6-确认中，7-交易成功
	// ，8-交易取消,未知状态(-1);
	public enum TradeState implements CodeDes {
		TS_TRADE_SEND("0", "无状态（收益发放无状态）"), TS_TRADE_FAILED("1", "交易失败"), TS_AFFORT_NEVER("2", "等待付款"), TS_AFFORT_CONFIRM("3", "付款中"), TS_AFFORT_SUCCESS("4", "付款成功"), TS_SUCCESS_PART(
				"5", "部分成交"), TS_AWAIT_CONFIRM("6", "待确中"), TS_SUCCESS_FULL("7", "交易成功"), TS_REVOKE("8", "交易取消");
		private String code = null;
		private String codeDes = null;

		TradeState(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}

	public enum PiggyTradeState implements CodeDes {
		TS_TRADE_SEND("0", "无状态（收益发放无状态）"), TS_TRADE_FAILED("1", "交易失败"), TS_AFFORT_NEVER("2", "等待付款"), TS_AFFORT_CONFIRM("3", "付款中"), TS_AFFORT_SUCCESS("4", "付款成功"), TS_SUCCESS_PART(
				"5", "部分成交"), TS_AWAIT_CONFIRM("6", "待确中"), TS_SUCCESS_FULL("7", "交易成功"), TS_REVOKE("8", "交易取消");
		private String code = null;
		private String codeDes = null;

		PiggyTradeState(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}

	public enum CPState implements CodeDes {
		code_cp_success("0000", "成功"), code_cp_timeout("9901", "网络连接超时"), code_cp_parseerror("9903", "报文解析错误"), code_cp_inter("9902", "中途退出"), code_cp_settingerror("9904",
				"配置文件验证失败"), code_cp_paramserror("9905", "调用参数不正确"), code_cp_businesserror("9906", "业务类型不支持"), code_cp_connerror1("2001", "通讯错误"), code_cp_connerror2("2002",
				"通讯错误"), code_cp_connerror3("2003", "通讯错误"), code_cp_orderserror1("2004", "订单错误"), code_cp_orderserror2("2005", "订单错误"), code_cp_sysytemerror1("2006", "系统异常"), code_cp_timeouterror(
				"2007", "通讯超时"), code_cp_sysytemerror2("9999", "系统异常"), code_hb_paramsnull("99991", "参数为空"), code_hb_parseerror("99992", "参数数据解析失败"), code_hb_back_exit("99993",
				"返回按钮触发SDK退出"), code_hb_exit_exit("99994", "关闭按钮触发SDK退出"), code_hb_success("99995", "成功界面点击“继续申购” 触发SDK退出"), code_hb_maxlogin("99996", "身份验证失败超过规定次数"), code_hb_unMatchBank(
				"99997", "不支持该银行卡"), code_hb_unknownerror("99998", "未知错误"), code_hb_unspportchannl("99999", "不支持该渠道");

		private String code = null;
		private String codeDes = null;

		CPState(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}

	public enum FenHongType implements CodeDes {
		F_HLZT("0", "红利再投"), F_XJHL("1", "现金红利");
		private String code = null;
		private String codeDes = null;

		FenHongType(String code, String codeDes) {
			this.code = code;
			this.codeDes = codeDes;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDescribe() {
			return codeDes;
		}
	}
}
