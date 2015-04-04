package howbuy.android.piggy.api.dto;

public class EstimatesIncomeItem {
	private String date;
	private String income;
	private String custBankId;
	private String bankName;
	private String bankCode;



	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getCustBankId() {
		return custBankId;
	}

	public String getBankName() {
		return bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setCustBankId(String custBankId) {
		this.custBankId = custBankId;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	
	
}
