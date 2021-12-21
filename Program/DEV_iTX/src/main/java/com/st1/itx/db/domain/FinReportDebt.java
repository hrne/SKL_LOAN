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
 * FinReportDebt 客戶財務報表.資產負債表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportDebt`")
public class FinReportDebt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1262348889946360889L;

	@EmbeddedId
	private FinReportDebtId finReportDebtId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 識別碼
	@Column(name = "`UKey`", length = 32, insertable = false, updatable = false)
	private String uKey;

	// 年度
	@Column(name = "`StartYY`")
	private int startYY = 0;

	// 年度_起月
	@Column(name = "`StartMM`")
	private int startMM = 0;

	// 年度_迄年
	@Column(name = "`EndYY`")
	private int endYY = 0;

	// 年度_迄月
	@Column(name = "`EndMM`")
	private int endMM = 0;

	// 資產總額
	@Column(name = "`AssetTotal`")
	private BigDecimal assetTotal = new BigDecimal("0");

	// 流動資產
	@Column(name = "`FlowAsset`")
	private BigDecimal flowAsset = new BigDecimal("0");

	// 現金及約當現金
	@Column(name = "`Cash`")
	private BigDecimal cash = new BigDecimal("0");

	// 金融資產(含其他)-流動
	@Column(name = "`FinAsset`")
	private BigDecimal finAsset = new BigDecimal("0");

	// 應收票據(淨額)
	@Column(name = "`ReceiveTicket`")
	private BigDecimal receiveTicket = new BigDecimal("0");

	// 應收帳款(淨額)
	@Column(name = "`ReceiveAccount`")
	private BigDecimal receiveAccount = new BigDecimal("0");

	// 應收關係人款
	@Column(name = "`ReceiveRelation`")
	private BigDecimal receiveRelation = new BigDecimal("0");

	// 其他應收款
	@Column(name = "`OtherReceive`")
	private BigDecimal otherReceive = new BigDecimal("0");

	// 存貨
	@Column(name = "`Stock`")
	private BigDecimal stock = new BigDecimal("0");

	// 預付款項
	@Column(name = "`PrepayItem`")
	private BigDecimal prepayItem = new BigDecimal("0");

	// 其他流動資產
	@Column(name = "`OtherFlowAsset`")
	private BigDecimal otherFlowAsset = new BigDecimal("0");

	// 流動資產_會計科目01
	@Column(name = "`AccountItem01`", length = 20)
	private String accountItem01;

	// 流動資產_會計科目02
	@Column(name = "`AccountItem02`", length = 20)
	private String accountItem02;

	// 流動資產_會計科目03
	@Column(name = "`AccountItem03`", length = 20)
	private String accountItem03;

	// 流動資產_會計科目值01
	@Column(name = "`AccountValue01`")
	private BigDecimal accountValue01 = new BigDecimal("0");

	// 流動資產_會計科目值02
	@Column(name = "`AccountValue02`")
	private BigDecimal accountValue02 = new BigDecimal("0");

	// 流動資產_會計科目值03
	@Column(name = "`AccountValue03`")
	private BigDecimal accountValue03 = new BigDecimal("0");

	// 基金及長期投資
	@Column(name = "`LongInvest`")
	private BigDecimal longInvest = new BigDecimal("0");

	// 固定資產
	@Column(name = "`FixedAsset`")
	private BigDecimal fixedAsset = new BigDecimal("0");

	// 土地
	@Column(name = "`Land`")
	private BigDecimal land = new BigDecimal("0");

	// 房屋及建築
	@Column(name = "`HouseBuild`")
	private BigDecimal houseBuild = new BigDecimal("0");

	// 機器設備
	@Column(name = "`MachineEquip`")
	private BigDecimal machineEquip = new BigDecimal("0");

	// 運輸、辦公、其他設備
	@Column(name = "`OtherEquip`")
	private BigDecimal otherEquip = new BigDecimal("0");

	// 預付設備款
	@Column(name = "`PrepayEquip`")
	private BigDecimal prepayEquip = new BigDecimal("0");

	// 未完成工程
	@Column(name = "`UnFinish`")
	private BigDecimal unFinish = new BigDecimal("0");

	// 減︰累計折舊
	@Column(name = "`Depreciation`")
	private BigDecimal depreciation = new BigDecimal("0");

	// 無形資產
	@Column(name = "`InvisibleAsset`")
	private BigDecimal invisibleAsset = new BigDecimal("0");

	// 其他資產
	@Column(name = "`OtherAsset`")
	private BigDecimal otherAsset = new BigDecimal("0");

	// 其他資產_會計科目04
	@Column(name = "`AccountItem04`", length = 20)
	private String accountItem04;

	// 其他資產_會計科目05
	@Column(name = "`AccountItem05`", length = 20)
	private String accountItem05;

	// 其他資產_會計科目06
	@Column(name = "`AccountItem06`", length = 20)
	private String accountItem06;

	// 其他資產_會計科目值04
	@Column(name = "`AccountValue04`")
	private BigDecimal accountValue04 = new BigDecimal("0");

	// 其他資產_會計科目值05
	@Column(name = "`AccountValue05`")
	private BigDecimal accountValue05 = new BigDecimal("0");

	// 其他資產_會計科目值06
	@Column(name = "`AccountValue06`")
	private BigDecimal accountValue06 = new BigDecimal("0");

	// 負債及淨值總額
	@Column(name = "`DebtNetTotal`")
	private BigDecimal debtNetTotal = new BigDecimal("0");

	// 流動負債
	@Column(name = "`FlowDebt`")
	private BigDecimal flowDebt = new BigDecimal("0");

	// 短期借款
	@Column(name = "`ShortLoan`")
	private BigDecimal shortLoan = new BigDecimal("0");

	// 應付短期票券
	@Column(name = "`PayShortTicket`")
	private BigDecimal payShortTicket = new BigDecimal("0");

	// 應付票據(淨額)
	@Column(name = "`PayTicket`")
	private BigDecimal payTicket = new BigDecimal("0");

	// 應付帳款(淨額)
	@Column(name = "`PayAccount`")
	private BigDecimal payAccount = new BigDecimal("0");

	// 應付關係人款
	@Column(name = "`PayRelation`")
	private BigDecimal payRelation = new BigDecimal("0");

	// 其他應付款
	@Column(name = "`OtherPay`")
	private BigDecimal otherPay = new BigDecimal("0");

	// 預收款項
	@Column(name = "`PreReceiveItem`")
	private BigDecimal preReceiveItem = new BigDecimal("0");

	// 長期負債(一年內)
	@Column(name = "`LongDebtOneYear`")
	private BigDecimal longDebtOneYear = new BigDecimal("0");

	// 股東墊款
	@Column(name = "`Shareholder`")
	private BigDecimal shareholder = new BigDecimal("0");

	// 其他流動負債
	@Column(name = "`OtherFlowDebt`")
	private BigDecimal otherFlowDebt = new BigDecimal("0");

	// 流動負債 _會計科目07
	@Column(name = "`AccountItem07`", length = 20)
	private String accountItem07;

	// 流動負債 _會計科目08
	@Column(name = "`AccountItem08`", length = 20)
	private String accountItem08;

	// 流動負債 _會計科目09
	@Column(name = "`AccountItem09`", length = 20)
	private String accountItem09;

	// 流動負債 _會計科目值07
	@Column(name = "`AccountValue07`")
	private BigDecimal accountValue07 = new BigDecimal("0");

	// 流動負債 _會計科目值08
	@Column(name = "`AccountValue08`")
	private BigDecimal accountValue08 = new BigDecimal("0");

	// 流動負債 _會計科目值09
	@Column(name = "`AccountValue09`")
	private BigDecimal accountValue09 = new BigDecimal("0");

	// 長期負債
	@Column(name = "`LongDebt`")
	private BigDecimal longDebt = new BigDecimal("0");

	// 其它負債
	@Column(name = "`OtherDebt`")
	private BigDecimal otherDebt = new BigDecimal("0");

	// 負債總額
	@Column(name = "`DebtTotal`")
	private BigDecimal debtTotal = new BigDecimal("0");

	// 淨值
	@Column(name = "`NetValue`")
	private BigDecimal netValue = new BigDecimal("0");

	// 資本
	@Column(name = "`Capital`")
	private BigDecimal capital = new BigDecimal("0");

	// 資本公積
	@Column(name = "`CapitalSurplus`")
	private BigDecimal capitalSurplus = new BigDecimal("0");

	// 保留盈餘
	@Column(name = "`RetainProfit`")
	private BigDecimal retainProfit = new BigDecimal("0");

	// 其他權益
	@Column(name = "`OtherRight`")
	private BigDecimal otherRight = new BigDecimal("0");

	// 庫藏股票
	@Column(name = "`TreasuryStock`")
	private BigDecimal treasuryStock = new BigDecimal("0");

	// 非控制權益
	@Column(name = "`UnControlRight`")
	private BigDecimal unControlRight = new BigDecimal("0");

	// 淨值_會計科目10
	@Column(name = "`AccountItem10`", length = 20)
	private String accountItem10;

	// 淨值_會計科目11
	@Column(name = "`AccountItem11`", length = 20)
	private String accountItem11;

	// 淨值_會計科目值10
	@Column(name = "`AccountValue10`")
	private BigDecimal accountValue10 = new BigDecimal("0");

	// 淨值_會計科目值11
	@Column(name = "`AccountValue11`")
	private BigDecimal accountValue11 = new BigDecimal("0");

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

	public FinReportDebtId getFinReportDebtId() {
		return this.finReportDebtId;
	}

	public void setFinReportDebtId(FinReportDebtId finReportDebtId) {
		this.finReportDebtId = finReportDebtId;
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
	 * 年度<br>
	 * 
	 * @return Integer
	 */
	public int getStartYY() {
		return this.startYY;
	}

	/**
	 * 年度<br>
	 * 
	 *
	 * @param startYY 年度
	 */
	public void setStartYY(int startYY) {
		this.startYY = startYY;
	}

	/**
	 * 年度_起月<br>
	 * 
	 * @return Integer
	 */
	public int getStartMM() {
		return this.startMM;
	}

	/**
	 * 年度_起月<br>
	 * 
	 *
	 * @param startMM 年度_起月
	 */
	public void setStartMM(int startMM) {
		this.startMM = startMM;
	}

	/**
	 * 年度_迄年<br>
	 * 
	 * @return Integer
	 */
	public int getEndYY() {
		return this.endYY;
	}

	/**
	 * 年度_迄年<br>
	 * 
	 *
	 * @param endYY 年度_迄年
	 */
	public void setEndYY(int endYY) {
		this.endYY = endYY;
	}

	/**
	 * 年度_迄月<br>
	 * 
	 * @return Integer
	 */
	public int getEndMM() {
		return this.endMM;
	}

	/**
	 * 年度_迄月<br>
	 * 
	 *
	 * @param endMM 年度_迄月
	 */
	public void setEndMM(int endMM) {
		this.endMM = endMM;
	}

	/**
	 * 資產總額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAssetTotal() {
		return this.assetTotal;
	}

	/**
	 * 資產總額<br>
	 * 
	 *
	 * @param assetTotal 資產總額
	 */
	public void setAssetTotal(BigDecimal assetTotal) {
		this.assetTotal = assetTotal;
	}

	/**
	 * 流動資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFlowAsset() {
		return this.flowAsset;
	}

	/**
	 * 流動資產<br>
	 * 
	 *
	 * @param flowAsset 流動資產
	 */
	public void setFlowAsset(BigDecimal flowAsset) {
		this.flowAsset = flowAsset;
	}

	/**
	 * 現金及約當現金<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCash() {
		return this.cash;
	}

	/**
	 * 現金及約當現金<br>
	 * 
	 *
	 * @param cash 現金及約當現金
	 */
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	/**
	 * 金融資產(含其他)-流動<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFinAsset() {
		return this.finAsset;
	}

	/**
	 * 金融資產(含其他)-流動<br>
	 * 
	 *
	 * @param finAsset 金融資產(含其他)-流動
	 */
	public void setFinAsset(BigDecimal finAsset) {
		this.finAsset = finAsset;
	}

	/**
	 * 應收票據(淨額)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getReceiveTicket() {
		return this.receiveTicket;
	}

	/**
	 * 應收票據(淨額)<br>
	 * 
	 *
	 * @param receiveTicket 應收票據(淨額)
	 */
	public void setReceiveTicket(BigDecimal receiveTicket) {
		this.receiveTicket = receiveTicket;
	}

	/**
	 * 應收帳款(淨額)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getReceiveAccount() {
		return this.receiveAccount;
	}

	/**
	 * 應收帳款(淨額)<br>
	 * 
	 *
	 * @param receiveAccount 應收帳款(淨額)
	 */
	public void setReceiveAccount(BigDecimal receiveAccount) {
		this.receiveAccount = receiveAccount;
	}

	/**
	 * 應收關係人款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getReceiveRelation() {
		return this.receiveRelation;
	}

	/**
	 * 應收關係人款<br>
	 * 
	 *
	 * @param receiveRelation 應收關係人款
	 */
	public void setReceiveRelation(BigDecimal receiveRelation) {
		this.receiveRelation = receiveRelation;
	}

	/**
	 * 其他應收款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherReceive() {
		return this.otherReceive;
	}

	/**
	 * 其他應收款<br>
	 * 
	 *
	 * @param otherReceive 其他應收款
	 */
	public void setOtherReceive(BigDecimal otherReceive) {
		this.otherReceive = otherReceive;
	}

	/**
	 * 存貨<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getStock() {
		return this.stock;
	}

	/**
	 * 存貨<br>
	 * 
	 *
	 * @param stock 存貨
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * 預付款項<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrepayItem() {
		return this.prepayItem;
	}

	/**
	 * 預付款項<br>
	 * 
	 *
	 * @param prepayItem 預付款項
	 */
	public void setPrepayItem(BigDecimal prepayItem) {
		this.prepayItem = prepayItem;
	}

	/**
	 * 其他流動資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherFlowAsset() {
		return this.otherFlowAsset;
	}

	/**
	 * 其他流動資產<br>
	 * 
	 *
	 * @param otherFlowAsset 其他流動資產
	 */
	public void setOtherFlowAsset(BigDecimal otherFlowAsset) {
		this.otherFlowAsset = otherFlowAsset;
	}

	/**
	 * 流動資產_會計科目01<br>
	 * 
	 * @return String
	 */
	public String getAccountItem01() {
		return this.accountItem01 == null ? "" : this.accountItem01;
	}

	/**
	 * 流動資產_會計科目01<br>
	 * 
	 *
	 * @param accountItem01 流動資產_會計科目01
	 */
	public void setAccountItem01(String accountItem01) {
		this.accountItem01 = accountItem01;
	}

	/**
	 * 流動資產_會計科目02<br>
	 * 
	 * @return String
	 */
	public String getAccountItem02() {
		return this.accountItem02 == null ? "" : this.accountItem02;
	}

	/**
	 * 流動資產_會計科目02<br>
	 * 
	 *
	 * @param accountItem02 流動資產_會計科目02
	 */
	public void setAccountItem02(String accountItem02) {
		this.accountItem02 = accountItem02;
	}

	/**
	 * 流動資產_會計科目03<br>
	 * 
	 * @return String
	 */
	public String getAccountItem03() {
		return this.accountItem03 == null ? "" : this.accountItem03;
	}

	/**
	 * 流動資產_會計科目03<br>
	 * 
	 *
	 * @param accountItem03 流動資產_會計科目03
	 */
	public void setAccountItem03(String accountItem03) {
		this.accountItem03 = accountItem03;
	}

	/**
	 * 流動資產_會計科目值01<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue01() {
		return this.accountValue01;
	}

	/**
	 * 流動資產_會計科目值01<br>
	 * 
	 *
	 * @param accountValue01 流動資產_會計科目值01
	 */
	public void setAccountValue01(BigDecimal accountValue01) {
		this.accountValue01 = accountValue01;
	}

	/**
	 * 流動資產_會計科目值02<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue02() {
		return this.accountValue02;
	}

	/**
	 * 流動資產_會計科目值02<br>
	 * 
	 *
	 * @param accountValue02 流動資產_會計科目值02
	 */
	public void setAccountValue02(BigDecimal accountValue02) {
		this.accountValue02 = accountValue02;
	}

	/**
	 * 流動資產_會計科目值03<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue03() {
		return this.accountValue03;
	}

	/**
	 * 流動資產_會計科目值03<br>
	 * 
	 *
	 * @param accountValue03 流動資產_會計科目值03
	 */
	public void setAccountValue03(BigDecimal accountValue03) {
		this.accountValue03 = accountValue03;
	}

	/**
	 * 基金及長期投資<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongInvest() {
		return this.longInvest;
	}

	/**
	 * 基金及長期投資<br>
	 * 
	 *
	 * @param longInvest 基金及長期投資
	 */
	public void setLongInvest(BigDecimal longInvest) {
		this.longInvest = longInvest;
	}

	/**
	 * 固定資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFixedAsset() {
		return this.fixedAsset;
	}

	/**
	 * 固定資產<br>
	 * 
	 *
	 * @param fixedAsset 固定資產
	 */
	public void setFixedAsset(BigDecimal fixedAsset) {
		this.fixedAsset = fixedAsset;
	}

	/**
	 * 土地<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLand() {
		return this.land;
	}

	/**
	 * 土地<br>
	 * 
	 *
	 * @param land 土地
	 */
	public void setLand(BigDecimal land) {
		this.land = land;
	}

	/**
	 * 房屋及建築<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getHouseBuild() {
		return this.houseBuild;
	}

	/**
	 * 房屋及建築<br>
	 * 
	 *
	 * @param houseBuild 房屋及建築
	 */
	public void setHouseBuild(BigDecimal houseBuild) {
		this.houseBuild = houseBuild;
	}

	/**
	 * 機器設備<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getMachineEquip() {
		return this.machineEquip;
	}

	/**
	 * 機器設備<br>
	 * 
	 *
	 * @param machineEquip 機器設備
	 */
	public void setMachineEquip(BigDecimal machineEquip) {
		this.machineEquip = machineEquip;
	}

	/**
	 * 運輸、辦公、其他設備<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherEquip() {
		return this.otherEquip;
	}

	/**
	 * 運輸、辦公、其他設備<br>
	 * 
	 *
	 * @param otherEquip 運輸、辦公、其他設備
	 */
	public void setOtherEquip(BigDecimal otherEquip) {
		this.otherEquip = otherEquip;
	}

	/**
	 * 預付設備款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrepayEquip() {
		return this.prepayEquip;
	}

	/**
	 * 預付設備款<br>
	 * 
	 *
	 * @param prepayEquip 預付設備款
	 */
	public void setPrepayEquip(BigDecimal prepayEquip) {
		this.prepayEquip = prepayEquip;
	}

	/**
	 * 未完成工程<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getUnFinish() {
		return this.unFinish;
	}

	/**
	 * 未完成工程<br>
	 * 
	 *
	 * @param unFinish 未完成工程
	 */
	public void setUnFinish(BigDecimal unFinish) {
		this.unFinish = unFinish;
	}

	/**
	 * 減︰累計折舊<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDepreciation() {
		return this.depreciation;
	}

	/**
	 * 減︰累計折舊<br>
	 * 
	 *
	 * @param depreciation 減︰累計折舊
	 */
	public void setDepreciation(BigDecimal depreciation) {
		this.depreciation = depreciation;
	}

	/**
	 * 無形資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInvisibleAsset() {
		return this.invisibleAsset;
	}

	/**
	 * 無形資產<br>
	 * 
	 *
	 * @param invisibleAsset 無形資產
	 */
	public void setInvisibleAsset(BigDecimal invisibleAsset) {
		this.invisibleAsset = invisibleAsset;
	}

	/**
	 * 其他資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherAsset() {
		return this.otherAsset;
	}

	/**
	 * 其他資產<br>
	 * 
	 *
	 * @param otherAsset 其他資產
	 */
	public void setOtherAsset(BigDecimal otherAsset) {
		this.otherAsset = otherAsset;
	}

	/**
	 * 其他資產_會計科目04<br>
	 * 
	 * @return String
	 */
	public String getAccountItem04() {
		return this.accountItem04 == null ? "" : this.accountItem04;
	}

	/**
	 * 其他資產_會計科目04<br>
	 * 
	 *
	 * @param accountItem04 其他資產_會計科目04
	 */
	public void setAccountItem04(String accountItem04) {
		this.accountItem04 = accountItem04;
	}

	/**
	 * 其他資產_會計科目05<br>
	 * 
	 * @return String
	 */
	public String getAccountItem05() {
		return this.accountItem05 == null ? "" : this.accountItem05;
	}

	/**
	 * 其他資產_會計科目05<br>
	 * 
	 *
	 * @param accountItem05 其他資產_會計科目05
	 */
	public void setAccountItem05(String accountItem05) {
		this.accountItem05 = accountItem05;
	}

	/**
	 * 其他資產_會計科目06<br>
	 * 
	 * @return String
	 */
	public String getAccountItem06() {
		return this.accountItem06 == null ? "" : this.accountItem06;
	}

	/**
	 * 其他資產_會計科目06<br>
	 * 
	 *
	 * @param accountItem06 其他資產_會計科目06
	 */
	public void setAccountItem06(String accountItem06) {
		this.accountItem06 = accountItem06;
	}

	/**
	 * 其他資產_會計科目值04<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue04() {
		return this.accountValue04;
	}

	/**
	 * 其他資產_會計科目值04<br>
	 * 
	 *
	 * @param accountValue04 其他資產_會計科目值04
	 */
	public void setAccountValue04(BigDecimal accountValue04) {
		this.accountValue04 = accountValue04;
	}

	/**
	 * 其他資產_會計科目值05<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue05() {
		return this.accountValue05;
	}

	/**
	 * 其他資產_會計科目值05<br>
	 * 
	 *
	 * @param accountValue05 其他資產_會計科目值05
	 */
	public void setAccountValue05(BigDecimal accountValue05) {
		this.accountValue05 = accountValue05;
	}

	/**
	 * 其他資產_會計科目值06<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue06() {
		return this.accountValue06;
	}

	/**
	 * 其他資產_會計科目值06<br>
	 * 
	 *
	 * @param accountValue06 其他資產_會計科目值06
	 */
	public void setAccountValue06(BigDecimal accountValue06) {
		this.accountValue06 = accountValue06;
	}

	/**
	 * 負債及淨值總額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDebtNetTotal() {
		return this.debtNetTotal;
	}

	/**
	 * 負債及淨值總額<br>
	 * 
	 *
	 * @param debtNetTotal 負債及淨值總額
	 */
	public void setDebtNetTotal(BigDecimal debtNetTotal) {
		this.debtNetTotal = debtNetTotal;
	}

	/**
	 * 流動負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getFlowDebt() {
		return this.flowDebt;
	}

	/**
	 * 流動負債<br>
	 * 
	 *
	 * @param flowDebt 流動負債
	 */
	public void setFlowDebt(BigDecimal flowDebt) {
		this.flowDebt = flowDebt;
	}

	/**
	 * 短期借款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getShortLoan() {
		return this.shortLoan;
	}

	/**
	 * 短期借款<br>
	 * 
	 *
	 * @param shortLoan 短期借款
	 */
	public void setShortLoan(BigDecimal shortLoan) {
		this.shortLoan = shortLoan;
	}

	/**
	 * 應付短期票券<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayShortTicket() {
		return this.payShortTicket;
	}

	/**
	 * 應付短期票券<br>
	 * 
	 *
	 * @param payShortTicket 應付短期票券
	 */
	public void setPayShortTicket(BigDecimal payShortTicket) {
		this.payShortTicket = payShortTicket;
	}

	/**
	 * 應付票據(淨額)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayTicket() {
		return this.payTicket;
	}

	/**
	 * 應付票據(淨額)<br>
	 * 
	 *
	 * @param payTicket 應付票據(淨額)
	 */
	public void setPayTicket(BigDecimal payTicket) {
		this.payTicket = payTicket;
	}

	/**
	 * 應付帳款(淨額)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayAccount() {
		return this.payAccount;
	}

	/**
	 * 應付帳款(淨額)<br>
	 * 
	 *
	 * @param payAccount 應付帳款(淨額)
	 */
	public void setPayAccount(BigDecimal payAccount) {
		this.payAccount = payAccount;
	}

	/**
	 * 應付關係人款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPayRelation() {
		return this.payRelation;
	}

	/**
	 * 應付關係人款<br>
	 * 
	 *
	 * @param payRelation 應付關係人款
	 */
	public void setPayRelation(BigDecimal payRelation) {
		this.payRelation = payRelation;
	}

	/**
	 * 其他應付款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherPay() {
		return this.otherPay;
	}

	/**
	 * 其他應付款<br>
	 * 
	 *
	 * @param otherPay 其他應付款
	 */
	public void setOtherPay(BigDecimal otherPay) {
		this.otherPay = otherPay;
	}

	/**
	 * 預收款項<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPreReceiveItem() {
		return this.preReceiveItem;
	}

	/**
	 * 預收款項<br>
	 * 
	 *
	 * @param preReceiveItem 預收款項
	 */
	public void setPreReceiveItem(BigDecimal preReceiveItem) {
		this.preReceiveItem = preReceiveItem;
	}

	/**
	 * 長期負債(一年內)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongDebtOneYear() {
		return this.longDebtOneYear;
	}

	/**
	 * 長期負債(一年內)<br>
	 * 
	 *
	 * @param longDebtOneYear 長期負債(一年內)
	 */
	public void setLongDebtOneYear(BigDecimal longDebtOneYear) {
		this.longDebtOneYear = longDebtOneYear;
	}

	/**
	 * 股東墊款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getShareholder() {
		return this.shareholder;
	}

	/**
	 * 股東墊款<br>
	 * 
	 *
	 * @param shareholder 股東墊款
	 */
	public void setShareholder(BigDecimal shareholder) {
		this.shareholder = shareholder;
	}

	/**
	 * 其他流動負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherFlowDebt() {
		return this.otherFlowDebt;
	}

	/**
	 * 其他流動負債<br>
	 * 
	 *
	 * @param otherFlowDebt 其他流動負債
	 */
	public void setOtherFlowDebt(BigDecimal otherFlowDebt) {
		this.otherFlowDebt = otherFlowDebt;
	}

	/**
	 * 流動負債 _會計科目07<br>
	 * 
	 * @return String
	 */
	public String getAccountItem07() {
		return this.accountItem07 == null ? "" : this.accountItem07;
	}

	/**
	 * 流動負債 _會計科目07<br>
	 * 
	 *
	 * @param accountItem07 流動負債 _會計科目07
	 */
	public void setAccountItem07(String accountItem07) {
		this.accountItem07 = accountItem07;
	}

	/**
	 * 流動負債 _會計科目08<br>
	 * 
	 * @return String
	 */
	public String getAccountItem08() {
		return this.accountItem08 == null ? "" : this.accountItem08;
	}

	/**
	 * 流動負債 _會計科目08<br>
	 * 
	 *
	 * @param accountItem08 流動負債 _會計科目08
	 */
	public void setAccountItem08(String accountItem08) {
		this.accountItem08 = accountItem08;
	}

	/**
	 * 流動負債 _會計科目09<br>
	 * 
	 * @return String
	 */
	public String getAccountItem09() {
		return this.accountItem09 == null ? "" : this.accountItem09;
	}

	/**
	 * 流動負債 _會計科目09<br>
	 * 
	 *
	 * @param accountItem09 流動負債 _會計科目09
	 */
	public void setAccountItem09(String accountItem09) {
		this.accountItem09 = accountItem09;
	}

	/**
	 * 流動負債 _會計科目值07<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue07() {
		return this.accountValue07;
	}

	/**
	 * 流動負債 _會計科目值07<br>
	 * 
	 *
	 * @param accountValue07 流動負債 _會計科目值07
	 */
	public void setAccountValue07(BigDecimal accountValue07) {
		this.accountValue07 = accountValue07;
	}

	/**
	 * 流動負債 _會計科目值08<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue08() {
		return this.accountValue08;
	}

	/**
	 * 流動負債 _會計科目值08<br>
	 * 
	 *
	 * @param accountValue08 流動負債 _會計科目值08
	 */
	public void setAccountValue08(BigDecimal accountValue08) {
		this.accountValue08 = accountValue08;
	}

	/**
	 * 流動負債 _會計科目值09<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue09() {
		return this.accountValue09;
	}

	/**
	 * 流動負債 _會計科目值09<br>
	 * 
	 *
	 * @param accountValue09 流動負債 _會計科目值09
	 */
	public void setAccountValue09(BigDecimal accountValue09) {
		this.accountValue09 = accountValue09;
	}

	/**
	 * 長期負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongDebt() {
		return this.longDebt;
	}

	/**
	 * 長期負債<br>
	 * 
	 *
	 * @param longDebt 長期負債
	 */
	public void setLongDebt(BigDecimal longDebt) {
		this.longDebt = longDebt;
	}

	/**
	 * 其它負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherDebt() {
		return this.otherDebt;
	}

	/**
	 * 其它負債<br>
	 * 
	 *
	 * @param otherDebt 其它負債
	 */
	public void setOtherDebt(BigDecimal otherDebt) {
		this.otherDebt = otherDebt;
	}

	/**
	 * 負債總額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDebtTotal() {
		return this.debtTotal;
	}

	/**
	 * 負債總額<br>
	 * 
	 *
	 * @param debtTotal 負債總額
	 */
	public void setDebtTotal(BigDecimal debtTotal) {
		this.debtTotal = debtTotal;
	}

	/**
	 * 淨值<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getNetValue() {
		return this.netValue;
	}

	/**
	 * 淨值<br>
	 * 
	 *
	 * @param netValue 淨值
	 */
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}

	/**
	 * 資本<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCapital() {
		return this.capital;
	}

	/**
	 * 資本<br>
	 * 
	 *
	 * @param capital 資本
	 */
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	/**
	 * 資本公積<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCapitalSurplus() {
		return this.capitalSurplus;
	}

	/**
	 * 資本公積<br>
	 * 
	 *
	 * @param capitalSurplus 資本公積
	 */
	public void setCapitalSurplus(BigDecimal capitalSurplus) {
		this.capitalSurplus = capitalSurplus;
	}

	/**
	 * 保留盈餘<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getRetainProfit() {
		return this.retainProfit;
	}

	/**
	 * 保留盈餘<br>
	 * 
	 *
	 * @param retainProfit 保留盈餘
	 */
	public void setRetainProfit(BigDecimal retainProfit) {
		this.retainProfit = retainProfit;
	}

	/**
	 * 其他權益<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOtherRight() {
		return this.otherRight;
	}

	/**
	 * 其他權益<br>
	 * 
	 *
	 * @param otherRight 其他權益
	 */
	public void setOtherRight(BigDecimal otherRight) {
		this.otherRight = otherRight;
	}

	/**
	 * 庫藏股票<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTreasuryStock() {
		return this.treasuryStock;
	}

	/**
	 * 庫藏股票<br>
	 * 
	 *
	 * @param treasuryStock 庫藏股票
	 */
	public void setTreasuryStock(BigDecimal treasuryStock) {
		this.treasuryStock = treasuryStock;
	}

	/**
	 * 非控制權益<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getUnControlRight() {
		return this.unControlRight;
	}

	/**
	 * 非控制權益<br>
	 * 
	 *
	 * @param unControlRight 非控制權益
	 */
	public void setUnControlRight(BigDecimal unControlRight) {
		this.unControlRight = unControlRight;
	}

	/**
	 * 淨值_會計科目10<br>
	 * 
	 * @return String
	 */
	public String getAccountItem10() {
		return this.accountItem10 == null ? "" : this.accountItem10;
	}

	/**
	 * 淨值_會計科目10<br>
	 * 
	 *
	 * @param accountItem10 淨值_會計科目10
	 */
	public void setAccountItem10(String accountItem10) {
		this.accountItem10 = accountItem10;
	}

	/**
	 * 淨值_會計科目11<br>
	 * 
	 * @return String
	 */
	public String getAccountItem11() {
		return this.accountItem11 == null ? "" : this.accountItem11;
	}

	/**
	 * 淨值_會計科目11<br>
	 * 
	 *
	 * @param accountItem11 淨值_會計科目11
	 */
	public void setAccountItem11(String accountItem11) {
		this.accountItem11 = accountItem11;
	}

	/**
	 * 淨值_會計科目值10<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue10() {
		return this.accountValue10;
	}

	/**
	 * 淨值_會計科目值10<br>
	 * 
	 *
	 * @param accountValue10 淨值_會計科目值10
	 */
	public void setAccountValue10(BigDecimal accountValue10) {
		this.accountValue10 = accountValue10;
	}

	/**
	 * 淨值_會計科目值11<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAccountValue11() {
		return this.accountValue11;
	}

	/**
	 * 淨值_會計科目值11<br>
	 * 
	 *
	 * @param accountValue11 淨值_會計科目值11
	 */
	public void setAccountValue11(BigDecimal accountValue11) {
		this.accountValue11 = accountValue11;
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
		return "FinReportDebt [finReportDebtId=" + finReportDebtId + ", startYY=" + startYY + ", startMM=" + startMM + ", endYY=" + endYY + ", endMM=" + endMM + ", assetTotal=" + assetTotal
				+ ", flowAsset=" + flowAsset + ", cash=" + cash + ", finAsset=" + finAsset + ", receiveTicket=" + receiveTicket + ", receiveAccount=" + receiveAccount + ", receiveRelation="
				+ receiveRelation + ", otherReceive=" + otherReceive + ", stock=" + stock + ", prepayItem=" + prepayItem + ", otherFlowAsset=" + otherFlowAsset + ", accountItem01=" + accountItem01
				+ ", accountItem02=" + accountItem02 + ", accountItem03=" + accountItem03 + ", accountValue01=" + accountValue01 + ", accountValue02=" + accountValue02 + ", accountValue03="
				+ accountValue03 + ", longInvest=" + longInvest + ", fixedAsset=" + fixedAsset + ", land=" + land + ", houseBuild=" + houseBuild + ", machineEquip=" + machineEquip + ", otherEquip="
				+ otherEquip + ", prepayEquip=" + prepayEquip + ", unFinish=" + unFinish + ", depreciation=" + depreciation + ", invisibleAsset=" + invisibleAsset + ", otherAsset=" + otherAsset
				+ ", accountItem04=" + accountItem04 + ", accountItem05=" + accountItem05 + ", accountItem06=" + accountItem06 + ", accountValue04=" + accountValue04 + ", accountValue05="
				+ accountValue05 + ", accountValue06=" + accountValue06 + ", debtNetTotal=" + debtNetTotal + ", flowDebt=" + flowDebt + ", shortLoan=" + shortLoan + ", payShortTicket="
				+ payShortTicket + ", payTicket=" + payTicket + ", payAccount=" + payAccount + ", payRelation=" + payRelation + ", otherPay=" + otherPay + ", preReceiveItem=" + preReceiveItem
				+ ", longDebtOneYear=" + longDebtOneYear + ", shareholder=" + shareholder + ", otherFlowDebt=" + otherFlowDebt + ", accountItem07=" + accountItem07 + ", accountItem08=" + accountItem08
				+ ", accountItem09=" + accountItem09 + ", accountValue07=" + accountValue07 + ", accountValue08=" + accountValue08 + ", accountValue09=" + accountValue09 + ", longDebt=" + longDebt
				+ ", otherDebt=" + otherDebt + ", debtTotal=" + debtTotal + ", netValue=" + netValue + ", capital=" + capital + ", capitalSurplus=" + capitalSurplus + ", retainProfit=" + retainProfit
				+ ", otherRight=" + otherRight + ", treasuryStock=" + treasuryStock + ", unControlRight=" + unControlRight + ", accountItem10=" + accountItem10 + ", accountItem11=" + accountItem11
				+ ", accountValue10=" + accountValue10 + ", accountValue11=" + accountValue11 + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
