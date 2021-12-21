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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`YearlyHouseLoanInt`")
public class YearlyHouseLoanInt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 511275848152625286L;

	@EmbeddedId
	private YearlyHouseLoanIntId yearlyHouseLoanIntId;

	// 資料年月
	@Column(name = "`YearMonth`", insertable = false, updatable = false)
	private int yearMonth = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 資金用途別
	/* 02:購置不動產00:全部 */
	@Column(name = "`UsageCode`", length = 2, insertable = false, updatable = false)
	private String usageCode;

	// 業務科目代號
	/* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
	@Column(name = "`AcctCode`", length = 3)
	private String acctCode;

	// 繳款方式
	/* 1: 匯款轉帳2: 銀行扣款3: 員工扣薪4: 支票5: 特約金6: 人事特約金7: 定存特約8: 劃撥存款 */
	@Column(name = "`RepayCode`", length = 2)
	private String repayCode;

	// 撥款金額
	@Column(name = "`LoanAmt`")
	private BigDecimal loanAmt = new BigDecimal("0");

	// 放款餘額
	@Column(name = "`LoanBal`")
	private BigDecimal loanBal = new BigDecimal("0");

	// 初貸日
	@Column(name = "`FirstDrawdownDate`")
	private int firstDrawdownDate = 0;

	// 到期日
	@Column(name = "`MaturityDate`")
	private int maturityDate = 0;

	// 年度繳息金額
	@Column(name = "`YearlyInt`")
	private BigDecimal yearlyInt = new BigDecimal("0");

	// 房屋取得日期
	@Column(name = "`HouseBuyDate`")
	private int houseBuyDate = 0;

	// jason格式紀錄欄
	@Column(name = "`JsonFields`", length = 300)
	private String jsonFields;

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

	public YearlyHouseLoanIntId getYearlyHouseLoanIntId() {
		return this.yearlyHouseLoanIntId;
	}

	public void setYearlyHouseLoanIntId(YearlyHouseLoanIntId yearlyHouseLoanIntId) {
		this.yearlyHouseLoanIntId = yearlyHouseLoanIntId;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getYearMonth() {
		return this.yearMonth;
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param yearMonth 資料年月
	 */
	public void setYearMonth(int yearMonth) {
		this.yearMonth = yearMonth;
	}

	/**
	 * 戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 戶號<br>
	 * 
	 *
	 * @param custNo 戶號
	 */
	public void setCustNo(int custNo) {
		this.custNo = custNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 資金用途別<br>
	 * 02:購置不動產 00:全部
	 * 
	 * @return String
	 */
	public String getUsageCode() {
		return this.usageCode == null ? "" : this.usageCode;
	}

	/**
	 * 資金用途別<br>
	 * 02:購置不動產 00:全部
	 *
	 * @param usageCode 資金用途別
	 */
	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 業務科目代號<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 *
	 * @param acctCode 業務科目代號
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 繳款方式<br>
	 * 1: 匯款轉帳 2: 銀行扣款 3: 員工扣薪 4: 支票 5: 特約金 6: 人事特約金 7: 定存特約 8: 劃撥存款
	 * 
	 * @return String
	 */
	public String getRepayCode() {
		return this.repayCode == null ? "" : this.repayCode;
	}

	/**
	 * 繳款方式<br>
	 * 1: 匯款轉帳 2: 銀行扣款 3: 員工扣薪 4: 支票 5: 特約金 6: 人事特約金 7: 定存特約 8: 劃撥存款
	 *
	 * @param repayCode 繳款方式
	 */
	public void setRepayCode(String repayCode) {
		this.repayCode = repayCode;
	}

	/**
	 * 撥款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanAmt() {
		return this.loanAmt;
	}

	/**
	 * 撥款金額<br>
	 * 
	 *
	 * @param loanAmt 撥款金額
	 */
	public void setLoanAmt(BigDecimal loanAmt) {
		this.loanAmt = loanAmt;
	}

	/**
	 * 放款餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanBal() {
		return this.loanBal;
	}

	/**
	 * 放款餘額<br>
	 * 
	 *
	 * @param loanBal 放款餘額
	 */
	public void setLoanBal(BigDecimal loanBal) {
		this.loanBal = loanBal;
	}

	/**
	 * 初貸日<br>
	 * 
	 * @return Integer
	 */
	public int getFirstDrawdownDate() {
		return StaticTool.bcToRoc(this.firstDrawdownDate);
	}

	/**
	 * 初貸日<br>
	 * 
	 *
	 * @param firstDrawdownDate 初貸日
	 * @throws LogicException when Date Is Warn
	 */
	public void setFirstDrawdownDate(int firstDrawdownDate) throws LogicException {
		this.firstDrawdownDate = StaticTool.rocToBc(firstDrawdownDate);
	}

	/**
	 * 到期日<br>
	 * 
	 * @return Integer
	 */
	public int getMaturityDate() {
		return StaticTool.bcToRoc(this.maturityDate);
	}

	/**
	 * 到期日<br>
	 * 
	 *
	 * @param maturityDate 到期日
	 * @throws LogicException when Date Is Warn
	 */
	public void setMaturityDate(int maturityDate) throws LogicException {
		this.maturityDate = StaticTool.rocToBc(maturityDate);
	}

	/**
	 * 年度繳息金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getYearlyInt() {
		return this.yearlyInt;
	}

	/**
	 * 年度繳息金額<br>
	 * 
	 *
	 * @param yearlyInt 年度繳息金額
	 */
	public void setYearlyInt(BigDecimal yearlyInt) {
		this.yearlyInt = yearlyInt;
	}

	/**
	 * 房屋取得日期<br>
	 * 
	 * @return Integer
	 */
	public int getHouseBuyDate() {
		return StaticTool.bcToRoc(this.houseBuyDate);
	}

	/**
	 * 房屋取得日期<br>
	 * 
	 *
	 * @param houseBuyDate 房屋取得日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setHouseBuyDate(int houseBuyDate) throws LogicException {
		this.houseBuyDate = StaticTool.rocToBc(houseBuyDate);
	}

	/**
	 * jason格式紀錄欄<br>
	 * 
	 * @return String
	 */
	public String getJsonFields() {
		return this.jsonFields == null ? "" : this.jsonFields;
	}

	/**
	 * jason格式紀錄欄<br>
	 * 
	 *
	 * @param jsonFields jason格式紀錄欄
	 */
	public void setJsonFields(String jsonFields) {
		this.jsonFields = jsonFields;
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
		return "YearlyHouseLoanInt [yearlyHouseLoanIntId=" + yearlyHouseLoanIntId + ", acctCode=" + acctCode + ", repayCode=" + repayCode + ", loanAmt=" + loanAmt + ", loanBal=" + loanBal
				+ ", firstDrawdownDate=" + firstDrawdownDate + ", maturityDate=" + maturityDate + ", yearlyInt=" + yearlyInt + ", houseBuyDate=" + houseBuyDate + ", jsonFields=" + jsonFields
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
