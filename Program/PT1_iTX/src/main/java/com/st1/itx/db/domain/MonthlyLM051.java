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
 * MonthlyLM051 月報LM051工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM051`")
public class MonthlyLM051 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7860014864490898010L;

	@EmbeddedId
	private MonthlyLM051Id monthlyLM051Id;

	// 借款人戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 帳冊別
	/* null 一般, */
	@Column(name = "`AcBookCode`", length = 3)
	private String acBookCode;

	// 本金餘額
	@Column(name = "`PrinBalance`")
	private BigDecimal prinBalance = new BigDecimal("0");

	// 額度業務科目
	/* CdAcCode會計科子細目設定檔310: 短期擔保放款 320: 中期擔保放款330: 長期擔保放款340: 三十年房貸 */
	@Column(name = "`FacAcctCode`", length = 3)
	private String facAcctCode;

	// 逾期期數
	@Column(name = "`OvduTerm`")
	private int ovduTerm = 0;

	// 主要擔保品地區別
	/* 地區別與鄉鎮區對照檔CdArea */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode;

	// 繳息迄日
	@Column(name = "`PrevIntDate`")
	private int prevIntDate = 0;

	// 戶況
	/*
	 * 00: 正常戶02: 催收戶03: 結案戶(結清日=本月)04: 逾期戶(改為00:正常戶)05: 催收結案戶(結清日=本月)06: 部分轉呆戶 07:
	 * 呆帳戶08: 債權轉讓戶(結清日=本月)09: 呆帳結案戶(結清日=本月)(不含債協)
	 */
	@Column(name = "`Status`")
	private int status = 0;

	// 展期記號
	/* 空白、1.展期一般 2.展期協議 */
	@Column(name = "`RenewCode`", length = 1)
	private String renewCode;

	// 有、無擔保金額記號
	/* 1.有擔保金額 */
	@Column(name = "`ClType`")
	private int clType = 0;

	// 資料分類代號
	/* 21=二之122=二之223=二之33=三 4=四 5=五 */
	@Column(name = "`AssetClass`", length = 2)
	private String assetClass;

	// 法務進度
	@Column(name = "`LegalProg`", length = 3)
	private String legalProg;

	// 金額
	@Column(name = "`Amount`")
	private BigDecimal amount = new BigDecimal("0");

	// 其他紀錄內容
	@Column(name = "`Memo`", length = 500)
	private String memo;

	// 商品代碼
	@Column(name = "`ProdNo`", length = 5)
	private String prodNo;

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

	public MonthlyLM051Id getMonthlyLM051Id() {
		return this.monthlyLM051Id;
	}

	public void setMonthlyLM051Id(MonthlyLM051Id monthlyLM051Id) {
		this.monthlyLM051Id = monthlyLM051Id;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 * @return Integer
	 */
	public int getCustNo() {
		return this.custNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param custNo 借款人戶號
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
	 * 帳冊別<br>
	 * null 一般,
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * null 一般,
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 本金餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrinBalance() {
		return this.prinBalance;
	}

	/**
	 * 本金餘額<br>
	 * 
	 *
	 * @param prinBalance 本金餘額
	 */
	public void setPrinBalance(BigDecimal prinBalance) {
		this.prinBalance = prinBalance;
	}

	/**
	 * 額度業務科目<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 * 
	 * @return String
	 */
	public String getFacAcctCode() {
		return this.facAcctCode == null ? "" : this.facAcctCode;
	}

	/**
	 * 額度業務科目<br>
	 * CdAcCode會計科子細目設定檔 310: 短期擔保放款 320: 中期擔保放款 330: 長期擔保放款 340: 三十年房貸
	 *
	 * @param facAcctCode 額度業務科目
	 */
	public void setFacAcctCode(String facAcctCode) {
		this.facAcctCode = facAcctCode;
	}

	/**
	 * 逾期期數<br>
	 * 
	 * @return Integer
	 */
	public int getOvduTerm() {
		return this.ovduTerm;
	}

	/**
	 * 逾期期數<br>
	 * 
	 *
	 * @param ovduTerm 逾期期數
	 */
	public void setOvduTerm(int ovduTerm) {
		this.ovduTerm = ovduTerm;
	}

	/**
	 * 主要擔保品地區別<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 主要擔保品地區別<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 *
	 * @param cityCode 主要擔保品地區別
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 繳息迄日<br>
	 * 
	 * @return Integer
	 */
	public int getPrevIntDate() {
		return StaticTool.bcToRoc(this.prevIntDate);
	}

	/**
	 * 繳息迄日<br>
	 * 
	 *
	 * @param prevIntDate 繳息迄日
	 * @throws LogicException when Date Is Warn
	 */
	public void setPrevIntDate(int prevIntDate) throws LogicException {
		this.prevIntDate = StaticTool.rocToBc(prevIntDate);
	}

	/**
	 * 戶況<br>
	 * 00: 正常戶 02: 催收戶 03: 結案戶(結清日=本月) 04: 逾期戶(改為00:正常戶) 05: 催收結案戶(結清日=本月) 06: 部分轉呆戶
	 * 07: 呆帳戶 08: 債權轉讓戶(結清日=本月) 09: 呆帳結案戶(結清日=本月) (不含債協)
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 戶況<br>
	 * 00: 正常戶 02: 催收戶 03: 結案戶(結清日=本月) 04: 逾期戶(改為00:正常戶) 05: 催收結案戶(結清日=本月) 06: 部分轉呆戶
	 * 07: 呆帳戶 08: 債權轉讓戶(結清日=本月) 09: 呆帳結案戶(結清日=本月) (不含債協)
	 *
	 * @param status 戶況
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 展期記號<br>
	 * 空白、1.展期一般 2.展期協議
	 * 
	 * @return String
	 */
	public String getRenewCode() {
		return this.renewCode == null ? "" : this.renewCode;
	}

	/**
	 * 展期記號<br>
	 * 空白、1.展期一般 2.展期協議
	 *
	 * @param renewCode 展期記號
	 */
	public void setRenewCode(String renewCode) {
		this.renewCode = renewCode;
	}

	/**
	 * 有、無擔保金額記號<br>
	 * 1.有擔保金額
	 * 
	 * @return Integer
	 */
	public int getClType() {
		return this.clType;
	}

	/**
	 * 有、無擔保金額記號<br>
	 * 1.有擔保金額
	 *
	 * @param clType 有、無擔保金額記號
	 */
	public void setClType(int clType) {
		this.clType = clType;
	}

	/**
	 * 資料分類代號<br>
	 * 21=二之1 22=二之2 23=二之3 3=三 4=四 5=五
	 * 
	 * @return String
	 */
	public String getAssetClass() {
		return this.assetClass == null ? "" : this.assetClass;
	}

	/**
	 * 資料分類代號<br>
	 * 21=二之1 22=二之2 23=二之3 3=三 4=四 5=五
	 *
	 * @param assetClass 資料分類代號
	 */
	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	/**
	 * 法務進度<br>
	 * 
	 * @return String
	 */
	public String getLegalProg() {
		return this.legalProg == null ? "" : this.legalProg;
	}

	/**
	 * 法務進度<br>
	 * 
	 *
	 * @param legalProg 法務進度
	 */
	public void setLegalProg(String legalProg) {
		this.legalProg = legalProg;
	}

	/**
	 * 金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * 金額<br>
	 * 
	 *
	 * @param amount 金額
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 其他紀錄內容<br>
	 * 
	 * @return String
	 */
	public String getMemo() {
		return this.memo == null ? "" : this.memo;
	}

	/**
	 * 其他紀錄內容<br>
	 * 
	 *
	 * @param memo 其他紀錄內容
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 * @return String
	 */
	public String getProdNo() {
		return this.prodNo == null ? "" : this.prodNo;
	}

	/**
	 * 商品代碼<br>
	 * 
	 *
	 * @param prodNo 商品代碼
	 */
	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
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
		return "MonthlyLM051 [monthlyLM051Id=" + monthlyLM051Id + ", acBookCode=" + acBookCode + ", prinBalance=" + prinBalance + ", facAcctCode=" + facAcctCode + ", ovduTerm=" + ovduTerm
				+ ", cityCode=" + cityCode + ", prevIntDate=" + prevIntDate + ", status=" + status + ", renewCode=" + renewCode + ", clType=" + clType + ", assetClass=" + assetClass + ", legalProg="
				+ legalProg + ", amount=" + amount + ", memo=" + memo + ", prodNo=" + prodNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
