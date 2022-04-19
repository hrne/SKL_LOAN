package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * FinReportRate 客戶財務報表.財務比率表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportRate`")
public class FinReportRate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035351624741443484L;

	@EmbeddedId
	private FinReportRateId finReportRateId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 識別碼
	@Column(name = "`UKey`", length = 32, insertable = false, updatable = false)
	private String uKey;

	// 是否為同業值
	/* 0:否,1:是 */
	@Column(name = "`IsSameTrade`", length = 1)
	private String isSameTrade;

	// 行業別
	@Column(name = "`TradeType`", length = 20)
	private String tradeType;

	// 流動比率
	@Column(name = "`Flow`")
	private BigDecimal flow = new BigDecimal("0");

	// 速動比率
	@Column(name = "`Speed`")
	private BigDecimal speed = new BigDecimal("0");

	// 利息保障倍數
	@Column(name = "`RateGuar`")
	private BigDecimal rateGuar = new BigDecimal("0");

	// 負債比率
	@Column(name = "`Debt`")
	private BigDecimal debt = new BigDecimal("0");

	// 淨值比率
	@Column(name = "`Net`")
	private BigDecimal net = new BigDecimal("0");

	// 現金流量比率
	@Column(name = "`CashFlow`")
	private BigDecimal cashFlow = new BigDecimal("0");

	// 固定長期適合率
	@Column(name = "`FixLong`")
	private BigDecimal fixLong = new BigDecimal("0");

	// 財務支出率
	@Column(name = "`FinSpend`")
	private BigDecimal finSpend = new BigDecimal("0");

	// 毛利率
	@Column(name = "`GrossProfit`")
	private BigDecimal grossProfit = new BigDecimal("0");

	// 稅後淨利率
	@Column(name = "`AfterTaxNet`")
	private BigDecimal afterTaxNet = new BigDecimal("0");

	// 淨值報酬率
	@Column(name = "`NetReward`")
	private BigDecimal netReward = new BigDecimal("0");

	// 總資產報酬率
	@Column(name = "`TotalAssetReward`")
	private BigDecimal totalAssetReward = new BigDecimal("0");

	// 存貨週轉率
	@Column(name = "`Stock`")
	private BigDecimal stock = new BigDecimal("0");

	// 應收帳款週轉率
	@Column(name = "`ReceiveAccount`")
	private BigDecimal receiveAccount = new BigDecimal("0");

	// 總資產週轉率
	@Column(name = "`TotalAsset`")
	private BigDecimal totalAsset = new BigDecimal("0");

	// 應付帳款週轉率
	@Column(name = "`PayAccount`")
	private BigDecimal payAccount = new BigDecimal("0");

	// 平均總資產週轉率：營業收入/((上期總資產+本期總資產)/2)
	@Column(name = "`AveTotalAsset`")
	private BigDecimal aveTotalAsset = new BigDecimal("0");

	// 平均淨營業週期：平均存貨週轉天數+平均應收帳款及應收票據週轉天數-平均應付帳款及應付票據週轉天數
	@Column(name = "`AveNetBusCycle`")
	private BigDecimal aveNetBusCycle = new BigDecimal("0");

	// 財務結構-財務槓桿：總負債/淨值
	@Column(name = "`FinLever`")
	private BigDecimal finLever = new BigDecimal("0");

	// 長期負債對淨值比：長期負債/淨值
	@Column(name = "`LoanDebtNet`")
	private BigDecimal loanDebtNet = new BigDecimal("0");

	// 營授比率：總借款/營收
	@Column(name = "`BusRate`")
	private BigDecimal busRate = new BigDecimal("0");

	// 償債能力-財務槓桿度：營業利益/(營業利益-利息支出)
	@Column(name = "`PayFinLever`")
	private BigDecimal payFinLever = new BigDecimal("0");

	// 借款依存度(ADE)：(短期借款+應付短期票券+股東墊款+長期負債+一年內到期之長期負債)/淨值合計
	@Column(name = "`ADE`")
	private BigDecimal aDE = new BigDecimal("0");

	// 現金保障倍數：營業活動現金流量/利息支出
	@Column(name = "`CashGuar`")
	private BigDecimal cashGuar = new BigDecimal("0");

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	public FinReportRateId getFinReportRateId() {
		return this.finReportRateId;
	}

	public void setFinReportRateId(FinReportRateId finReportRateId) {
		this.finReportRateId = finReportRateId;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getCustUKey() {
		return this.custUKey == null ? "" : this.custUKey;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 *
	 * @param custUKey 客戶識別碼
	 */
	public void setCustUKey(String custUKey) {
		this.custUKey = custUKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 * @return String
	 */
	public String getUKey() {
		return this.uKey == null ? "" : this.uKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 *
	 * @param uKey 識別碼
	 */
	public void setUKey(String uKey) {
		this.uKey = uKey;
	}

	/**
	 * 是否為同業值<br>
	 * 0:否,1:是
	 * 
	 * @return String
	 */
	public String getIsSameTrade() {
		return this.isSameTrade == null ? "" : this.isSameTrade;
	}

	/**
	 * 是否為同業值<br>
	 * 0:否,1:是
	 *
	 * @param isSameTrade 是否為同業值
	 */
	public void setIsSameTrade(String isSameTrade) {
		this.isSameTrade = isSameTrade;
	}

	/**
	 * 行業別<br>
	 * 
	 * @return String
	 */
	public String getTradeType() {
		return this.tradeType == null ? "" : this.tradeType;
	}

	/**
	 * 行業別<br>
	 * 
	 *
	 * @param tradeType 行業別
	 */
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * 流動比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFlow() {
		return this.flow;
	}

	/**
	 * 流動比率<br>
	 * 
	 *
	 * @param flow 流動比率
	 */
	public void setFlow(BigDecimal flow) {
		this.flow = flow;
	}

	/**
	 * 速動比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getSpeed() {
		return this.speed;
	}

	/**
	 * 速動比率<br>
	 * 
	 *
	 * @param speed 速動比率
	 */
	public void setSpeed(BigDecimal speed) {
		this.speed = speed;
	}

	/**
	 * 利息保障倍數<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRateGuar() {
		return this.rateGuar;
	}

	/**
	 * 利息保障倍數<br>
	 * 
	 *
	 * @param rateGuar 利息保障倍數
	 */
	public void setRateGuar(BigDecimal rateGuar) {
		this.rateGuar = rateGuar;
	}

	/**
	 * 負債比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDebt() {
		return this.debt;
	}

	/**
	 * 負債比率<br>
	 * 
	 *
	 * @param debt 負債比率
	 */
	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	/**
	 * 淨值比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getNet() {
		return this.net;
	}

	/**
	 * 淨值比率<br>
	 * 
	 *
	 * @param net 淨值比率
	 */
	public void setNet(BigDecimal net) {
		this.net = net;
	}

	/**
	 * 現金流量比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCashFlow() {
		return this.cashFlow;
	}

	/**
	 * 現金流量比率<br>
	 * 
	 *
	 * @param cashFlow 現金流量比率
	 */
	public void setCashFlow(BigDecimal cashFlow) {
		this.cashFlow = cashFlow;
	}

	/**
	 * 固定長期適合率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFixLong() {
		return this.fixLong;
	}

	/**
	 * 固定長期適合率<br>
	 * 
	 *
	 * @param fixLong 固定長期適合率
	 */
	public void setFixLong(BigDecimal fixLong) {
		this.fixLong = fixLong;
	}

	/**
	 * 財務支出率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFinSpend() {
		return this.finSpend;
	}

	/**
	 * 財務支出率<br>
	 * 
	 *
	 * @param finSpend 財務支出率
	 */
	public void setFinSpend(BigDecimal finSpend) {
		this.finSpend = finSpend;
	}

	/**
	 * 毛利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getGrossProfit() {
		return this.grossProfit;
	}

	/**
	 * 毛利率<br>
	 * 
	 *
	 * @param grossProfit 毛利率
	 */
	public void setGrossProfit(BigDecimal grossProfit) {
		this.grossProfit = grossProfit;
	}

	/**
	 * 稅後淨利率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAfterTaxNet() {
		return this.afterTaxNet;
	}

	/**
	 * 稅後淨利率<br>
	 * 
	 *
	 * @param afterTaxNet 稅後淨利率
	 */
	public void setAfterTaxNet(BigDecimal afterTaxNet) {
		this.afterTaxNet = afterTaxNet;
	}

	/**
	 * 淨值報酬率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getNetReward() {
		return this.netReward;
	}

	/**
	 * 淨值報酬率<br>
	 * 
	 *
	 * @param netReward 淨值報酬率
	 */
	public void setNetReward(BigDecimal netReward) {
		this.netReward = netReward;
	}

	/**
	 * 總資產報酬率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAssetReward() {
		return this.totalAssetReward;
	}

	/**
	 * 總資產報酬率<br>
	 * 
	 *
	 * @param totalAssetReward 總資產報酬率
	 */
	public void setTotalAssetReward(BigDecimal totalAssetReward) {
		this.totalAssetReward = totalAssetReward;
	}

	/**
	 * 存貨週轉率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getStock() {
		return this.stock;
	}

	/**
	 * 存貨週轉率<br>
	 * 
	 *
	 * @param stock 存貨週轉率
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * 應收帳款週轉率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getReceiveAccount() {
		return this.receiveAccount;
	}

	/**
	 * 應收帳款週轉率<br>
	 * 
	 *
	 * @param receiveAccount 應收帳款週轉率
	 */
	public void setReceiveAccount(BigDecimal receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	/**
	 * 總資產週轉率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAsset() {
		return this.totalAsset;
	}

	/**
	 * 總資產週轉率<br>
	 * 
	 *
	 * @param totalAsset 總資產週轉率
	 */
	public void setTotalAsset(BigDecimal totalAsset) {
		this.totalAsset = totalAsset;
	}

	/**
	 * 應付帳款週轉率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayAccount() {
		return this.payAccount;
	}

	/**
	 * 應付帳款週轉率<br>
	 * 
	 *
	 * @param payAccount 應付帳款週轉率
	 */
	public void setPayAccount(BigDecimal payAccount) {
		this.payAccount = payAccount;
	}

	/**
	 * 平均總資產週轉率：營業收入/((上期總資產+本期總資產)/2)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAveTotalAsset() {
		return this.aveTotalAsset;
	}

	/**
	 * 平均總資產週轉率：營業收入/((上期總資產+本期總資產)/2)<br>
	 * 
	 *
	 * @param aveTotalAsset 平均總資產週轉率：營業收入/((上期總資產+本期總資產)/2)
	 */
	public void setAveTotalAsset(BigDecimal aveTotalAsset) {
		this.aveTotalAsset = aveTotalAsset;
	}

	/**
	 * 平均淨營業週期：平均存貨週轉天數+平均應收帳款及應收票據週轉天數-平均應付帳款及應付票據週轉天數<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAveNetBusCycle() {
		return this.aveNetBusCycle;
	}

	/**
	 * 平均淨營業週期：平均存貨週轉天數+平均應收帳款及應收票據週轉天數-平均應付帳款及應付票據週轉天數<br>
	 * 
	 *
	 * @param aveNetBusCycle 平均淨營業週期：平均存貨週轉天數+平均應收帳款及應收票據週轉天數-平均應付帳款及應付票據週轉天數
	 */
	public void setAveNetBusCycle(BigDecimal aveNetBusCycle) {
		this.aveNetBusCycle = aveNetBusCycle;
	}

	/**
	 * 財務結構-財務槓桿：總負債/淨值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFinLever() {
		return this.finLever;
	}

	/**
	 * 財務結構-財務槓桿：總負債/淨值<br>
	 * 
	 *
	 * @param finLever 財務結構-財務槓桿：總負債/淨值
	 */
	public void setFinLever(BigDecimal finLever) {
		this.finLever = finLever;
	}

	/**
	 * 長期負債對淨值比：長期負債/淨值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanDebtNet() {
		return this.loanDebtNet;
	}

	/**
	 * 長期負債對淨值比：長期負債/淨值<br>
	 * 
	 *
	 * @param loanDebtNet 長期負債對淨值比：長期負債/淨值
	 */
	public void setLoanDebtNet(BigDecimal loanDebtNet) {
		this.loanDebtNet = loanDebtNet;
	}

	/**
	 * 營授比率：總借款/營收<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBusRate() {
		return this.busRate;
	}

	/**
	 * 營授比率：總借款/營收<br>
	 * 
	 *
	 * @param busRate 營授比率：總借款/營收
	 */
	public void setBusRate(BigDecimal busRate) {
		this.busRate = busRate;
	}

	/**
	 * 償債能力-財務槓桿度：營業利益/(營業利益-利息支出)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayFinLever() {
		return this.payFinLever;
	}

	/**
	 * 償債能力-財務槓桿度：營業利益/(營業利益-利息支出)<br>
	 * 
	 *
	 * @param payFinLever 償債能力-財務槓桿度：營業利益/(營業利益-利息支出)
	 */
	public void setPayFinLever(BigDecimal payFinLever) {
		this.payFinLever = payFinLever;
	}

	/**
	 * 借款依存度(ADE)：(短期借款+應付短期票券+股東墊款+長期負債+一年內到期之長期負債)/淨值合計<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getADE() {
		return this.aDE;
	}

	/**
	 * 借款依存度(ADE)：(短期借款+應付短期票券+股東墊款+長期負債+一年內到期之長期負債)/淨值合計<br>
	 * 
	 *
	 * @param aDE 借款依存度(ADE)：(短期借款+應付短期票券+股東墊款+長期負債+一年內到期之長期負債)/淨值合計
	 */
	public void setADE(BigDecimal aDE) {
		this.aDE = aDE;
	}

	/**
	 * 現金保障倍數：營業活動現金流量/利息支出<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCashGuar() {
		return this.cashGuar;
	}

	/**
	 * 現金保障倍數：營業活動現金流量/利息支出<br>
	 * 
	 *
	 * @param cashGuar 現金保障倍數：營業活動現金流量/利息支出
	 */
	public void setCashGuar(BigDecimal cashGuar) {
		this.cashGuar = cashGuar;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "FinReportRate [finReportRateId=" + finReportRateId + ", isSameTrade=" + isSameTrade + ", tradeType=" + tradeType + ", flow=" + flow + ", speed=" + speed + ", rateGuar=" + rateGuar
				+ ", debt=" + debt + ", net=" + net + ", cashFlow=" + cashFlow + ", fixLong=" + fixLong + ", finSpend=" + finSpend + ", grossProfit=" + grossProfit + ", afterTaxNet=" + afterTaxNet
				+ ", netReward=" + netReward + ", totalAssetReward=" + totalAssetReward + ", stock=" + stock + ", receiveAccount=" + receiveAccount + ", totalAsset=" + totalAsset + ", payAccount="
				+ payAccount + ", aveTotalAsset=" + aveTotalAsset + ", aveNetBusCycle=" + aveNetBusCycle + ", finLever=" + finLever + ", loanDebtNet=" + loanDebtNet + ", busRate=" + busRate
				+ ", payFinLever=" + payFinLever + ", aDE=" + aDE + ", cashGuar=" + cashGuar + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
