package howbuy.android.piggy.api.dto;

/**
 * 交易查询item
 * 
 * @ClassName: TradeQueryItemDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-25下午5:13:52
 */
public class TradeQueryItemDto {
	private String contractNo;// 交易流水
	private String tradeType;// 交易类型
	private String appAmt;// 金额
	private String tradeDt;// 日期
	private String optDt;// 操作日期
	private String txPmtFlag;// 好买状态
	private String txAckFlag;// 基金公司状态
	private String appVol;//份额
	private String txStatus;//交易状态
	private String tradeBal;//交易金额
	
//	   "appAmt": 1111.0,
//	      "appVol": 0.0,
//	      "contractNo": "413200201207310000139957",
//	      "fee": 0.0,
//	      "fundAbbr": "现金宝A",
//	      "fundCode": "240006",
//	      "tradeDt": "",
//	      "tradeType": "",
//	      "txAckFlag": "",
//	      "txPmtFlag": "2"

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAppAmt() {
		return appAmt;
	}

	public void setAppAmt(String appAmt) {
		this.appAmt = appAmt;
	}

	public String getTradeDt() {
		return tradeDt;
	}

	public void setTradeDt(String tradeDt) {
		this.tradeDt = tradeDt;
	}

	public String getTxPmtFlag() {
		return txPmtFlag;
	}

	public void setTxPmtFlag(String txPmtFlag) {
		this.txPmtFlag = txPmtFlag;
	}

	public String getTxAckFlag() {
		return txAckFlag;
	}

	public String getAppVol() {
		return appVol;
	}

	public void setAppVol(String appVol) {
		this.appVol = appVol;
	}

	public void setTxAckFlag(String txAckFlag) {
		this.txAckFlag = txAckFlag;
	}

	
	
	public String getOptDt() {
		return optDt;
	}

	public void setOptDt(String optDt) {
		this.optDt = optDt;
	}

	
	
	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}
	
	public String getTradeBal() {
		return tradeBal;
	}

	public void setTradeBal(String tradeBal) {
		this.tradeBal = tradeBal;
	}

	@Override
	public String toString() {
		return "TradeQueryItemDto [contractNo=" + contractNo + ", tradeType=" + tradeType + ", appAmt=" + appAmt + ", tradeDt=" + tradeDt + ", txPmtFlag=" + txPmtFlag
				+ ", txAckFlag=" + txAckFlag + "]";
	}

}
