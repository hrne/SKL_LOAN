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
 * JcicB204 聯徵授信餘額日報檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB204`")
public class JcicB204 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -373427457314937839L;

	@EmbeddedId
	private JcicB204Id jcicB204Id;

	// 資料日期
	@Column(name = "`DataYMD`", insertable = false, updatable = false)
	private int dataYMD = 0;

	// 總行代號
	/* Key,金融機構總機構之代號，三位數字 */
	@Column(name = "`BankItem`", length = 3, insertable = false, updatable = false)
	private String bankItem;

	// 分行代號
	/* Key,金融機構分支機構之代號，四位數字 */
	@Column(name = "`BranchItem`", length = 4, insertable = false, updatable = false)
	private String branchItem;

	// 新增核准額度日期／清償日期／額度到期或解約日期
	/* Key,以'YYYMMDD'(民國)表示 */
	@Column(name = "`DataDate`", insertable = false, updatable = false)
	private int dataDate = 0;

	// 額度控制編碼／帳號
	/* Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。 */
	@Column(name = "`AcctNo`", length = 50, insertable = false, updatable = false)
	private String acctNo;

	// 授信戶IDN/BAN
	/* Key,左靠，身份證或統一證號 */
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 科目別
	/* Key,請參考附件三授信科目代號表 */
	@Column(name = "`AcctCode`", length = 1, insertable = false, updatable = false)
	private String acctCode;

	// 科目別註記
	/*
	 * Key,以 S
	 * 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...請參考附件三之一科目別註記代號表;
	 * 如無前述狀況請填X
	 */
	@Column(name = "`SubAcctCode`", length = 1, insertable = false, updatable = false)
	private String subAcctCode;

	// 交易別
	/*
	 * L:新增授信額度P:每筆撥款清償後額度未解約(第10欄清償金額需大於0)D:刪除A:每筆撥款清償後額度解約(第10欄清償金額需大於0)B:額度到期或解約(
	 * 第10欄清償金額需等於0)
	 */
	@Column(name = "`SubTranCode`", length = 1, insertable = false, updatable = false)
	private String subTranCode;

	// 訂約金額
	/* 右靠左補0，單位新台幣千元，核准額度如有降低時，請於本欄位第一碼加註'-'負號，例如核准額度減少120千元，請填'-000000120' */
	@Column(name = "`LineAmt`")
	private BigDecimal lineAmt = new BigDecimal("0");

	// 新增核准額度當日動撥／清償金額
	/* 右靠左補0，單位新台幣千元 */
	@Column(name = "`DrawdownAmt`")
	private BigDecimal drawdownAmt = new BigDecimal("0");

	// 本筆新增核准額度應計入DBR22倍規範之金額
	/* 右靠左補0，單位新台幣千元 … */
	@Column(name = "`DBR22Amt`")
	private BigDecimal dBR22Amt = new BigDecimal("0");

	// 1~7欄資料值相同之交易序號
	/* Key … */
	@Column(name = "`SeqNo`", length = 1, insertable = false, updatable = false)
	private String seqNo;

	// 空白
	/* 備用 */
	@Column(name = "`Filler13`", length = 20)
	private String filler13;

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

	public JcicB204Id getJcicB204Id() {
		return this.jcicB204Id;
	}

	public void setJcicB204Id(JcicB204Id jcicB204Id) {
		this.jcicB204Id = jcicB204Id;
	}

	/**
	 * 資料日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataYMD() {
		return this.dataYMD;
	}

	/**
	 * 資料日期<br>
	 * 
	 *
	 * @param dataYMD 資料日期
	 */
	public void setDataYMD(int dataYMD) {
		this.dataYMD = dataYMD;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getBankItem() {
		return this.bankItem == null ? "" : this.bankItem;
	}

	/**
	 * 總行代號<br>
	 * Key,金融機構總機構之代號，三位數字
	 *
	 * @param bankItem 總行代號
	 */
	public void setBankItem(String bankItem) {
		this.bankItem = bankItem;
	}

	/**
	 * 分行代號<br>
	 * Key,金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getBranchItem() {
		return this.branchItem == null ? "" : this.branchItem;
	}

	/**
	 * 分行代號<br>
	 * Key,金融機構分支機構之代號，四位數字
	 *
	 * @param branchItem 分行代號
	 */
	public void setBranchItem(String branchItem) {
		this.branchItem = branchItem;
	}

	/**
	 * 新增核准額度日期／清償日期／額度到期或解約日期<br>
	 * Key,以'YYYMMDD'(民國)表示
	 * 
	 * @return Integer
	 */
	public int getDataDate() {
		return this.dataDate;
	}

	/**
	 * 新增核准額度日期／清償日期／額度到期或解約日期<br>
	 * Key,以'YYYMMDD'(民國)表示
	 *
	 * @param dataDate 新增核准額度日期／清償日期／額度到期或解約日期
	 */
	public void setDataDate(int dataDate) {
		this.dataDate = dataDate;
	}

	/**
	 * 額度控制編碼／帳號<br>
	 * Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。
	 * 
	 * @return String
	 */
	public String getAcctNo() {
		return this.acctNo == null ? "" : this.acctNo;
	}

	/**
	 * 額度控制編碼／帳號<br>
	 * Key,左靠，填報本筆撥款帳號且為一號到底;若僅核准額度尚未動撥，本欄請填授信額度檔之第6欄'本階共用額度控制編碼'。
	 *
	 * @param acctNo 額度控制編碼／帳號
	 */
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * Key,左靠，身份證或統一證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * Key,左靠，身份證或統一證號
	 *
	 * @param custId 授信戶IDN/BAN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 科目別<br>
	 * Key,請參考附件三授信科目代號表
	 * 
	 * @return String
	 */
	public String getAcctCode() {
		return this.acctCode == null ? "" : this.acctCode;
	}

	/**
	 * 科目別<br>
	 * Key,請參考附件三授信科目代號表
	 *
	 * @param acctCode 科目別
	 */
	public void setAcctCode(String acctCode) {
		this.acctCode = acctCode;
	}

	/**
	 * 科目別註記<br>
	 * Key,以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...
	 * 請參考附件三之一科目別註記代號表; 如無前述狀況請填X
	 * 
	 * @return String
	 */
	public String getSubAcctCode() {
		return this.subAcctCode == null ? "" : this.subAcctCode;
	}

	/**
	 * 科目別註記<br>
	 * Key,以 S 註記為十足擔保之授信，辦理保證或承兌業務發生墊款時，以M註記為已墊款有擔保之授信，以N註記為已墊款無擔保之授信...
	 * 請參考附件三之一科目別註記代號表; 如無前述狀況請填X
	 *
	 * @param subAcctCode 科目別註記
	 */
	public void setSubAcctCode(String subAcctCode) {
		this.subAcctCode = subAcctCode;
	}

	/**
	 * 交易別<br>
	 * L:新增授信額度 P:每筆撥款清償後額度未解約(第10欄清償金額需大於0) D:刪除 A:每筆撥款清償後額度解約(第10欄清償金額需大於0)
	 * B:額度到期或解約(第10欄清償金額需等於0)
	 * 
	 * @return String
	 */
	public String getSubTranCode() {
		return this.subTranCode == null ? "" : this.subTranCode;
	}

	/**
	 * 交易別<br>
	 * L:新增授信額度 P:每筆撥款清償後額度未解約(第10欄清償金額需大於0) D:刪除 A:每筆撥款清償後額度解約(第10欄清償金額需大於0)
	 * B:額度到期或解約(第10欄清償金額需等於0)
	 *
	 * @param subTranCode 交易別
	 */
	public void setSubTranCode(String subTranCode) {
		this.subTranCode = subTranCode;
	}

	/**
	 * 訂約金額<br>
	 * 右靠左補0，單位新台幣千元，核准額度如有降低時，請於本欄位第一碼加註'-'負號，例如核准額度減少120千元，請填'-000000120'
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLineAmt() {
		return this.lineAmt;
	}

	/**
	 * 訂約金額<br>
	 * 右靠左補0，單位新台幣千元，核准額度如有降低時，請於本欄位第一碼加註'-'負號，例如核准額度減少120千元，請填'-000000120'
	 *
	 * @param lineAmt 訂約金額
	 */
	public void setLineAmt(BigDecimal lineAmt) {
		this.lineAmt = lineAmt;
	}

	/**
	 * 新增核准額度當日動撥／清償金額<br>
	 * 右靠左補0，單位新台幣千元
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDrawdownAmt() {
		return this.drawdownAmt;
	}

	/**
	 * 新增核准額度當日動撥／清償金額<br>
	 * 右靠左補0，單位新台幣千元
	 *
	 * @param drawdownAmt 新增核准額度當日動撥／清償金額
	 */
	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		this.drawdownAmt = drawdownAmt;
	}

	/**
	 * 本筆新增核准額度應計入DBR22倍規範之金額<br>
	 * 右靠左補0，單位新台幣千元 …
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDBR22Amt() {
		return this.dBR22Amt;
	}

	/**
	 * 本筆新增核准額度應計入DBR22倍規範之金額<br>
	 * 右靠左補0，單位新台幣千元 …
	 *
	 * @param dBR22Amt 本筆新增核准額度應計入DBR22倍規範之金額
	 */
	public void setDBR22Amt(BigDecimal dBR22Amt) {
		this.dBR22Amt = dBR22Amt;
	}

	/**
	 * 1~7欄資料值相同之交易序號<br>
	 * Key …
	 * 
	 * @return String
	 */
	public String getSeqNo() {
		return this.seqNo == null ? "" : this.seqNo;
	}

	/**
	 * 1~7欄資料值相同之交易序號<br>
	 * Key …
	 *
	 * @param seqNo 1~7欄資料值相同之交易序號
	 */
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller13() {
		return this.filler13 == null ? "" : this.filler13;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler13 空白
	 */
	public void setFiller13(String filler13) {
		this.filler13 = filler13;
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
		return "JcicB204 [jcicB204Id=" + jcicB204Id + ", lineAmt=" + lineAmt + ", drawdownAmt=" + drawdownAmt + ", dBR22Amt=" + dBR22Amt + ", filler13=" + filler13 + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
