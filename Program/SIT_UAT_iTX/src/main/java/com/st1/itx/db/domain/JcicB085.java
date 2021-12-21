package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * JcicB085 聯徵帳號轉換資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB085`")
public class JcicB085 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4511015397592984875L;

	@EmbeddedId
	private JcicB085Id jcicB085Id;

	// 資料日期
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 資料別
	/* "85":帳號轉換資料 */
	@Column(name = "`DataType`", length = 2)
	private String dataType;

	// 轉換帳號年月
	/* 以'YYYMMDD'(民國)表示，填報本筆資料帳號轉換年月 */
	@Column(name = "`RenewYM`")
	private int renewYM = 0;

	// 授信戶IDN/BAN
	/* 左靠，身份證或統一證號 */
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 轉換前總行代號
	/* 金融機構總機構之代號，三位數字 */
	@Column(name = "`BefBankItem`", length = 3)
	private String befBankItem;

	// 轉換前分行代號
	/* 金融機構分支機構之代號，四位數字 */
	@Column(name = "`BefBranchItem`", length = 4)
	private String befBranchItem;

	// 空白
	/* 備用 */
	@Column(name = "`Filler6`", length = 2)
	private String filler6;

	// 轉換前帳號
	/* Key,左靠，填報本筆撥款轉換前帳號。 */
	@Column(name = "`BefAcctNo`", length = 50, insertable = false, updatable = false)
	private String befAcctNo;

	// 轉換後總行代號
	/* 金融機構總機構之代號，三位數字 */
	@Column(name = "`AftBankItem`", length = 3)
	private String aftBankItem;

	// 轉換後分行代號
	/* 金融機構分支機構之代號，四位數字 */
	@Column(name = "`AftBranchItem`", length = 4)
	private String aftBranchItem;

	// 空白
	/* 備用 */
	@Column(name = "`Filler10`", length = 2)
	private String filler10;

	// 轉換後帳號
	/* Key,左靠，填報本筆撥款轉換後帳號。 */
	@Column(name = "`AftAcctNo`", length = 50, insertable = false, updatable = false)
	private String aftAcctNo;

	// 空白
	/* 備用 */
	@Column(name = "`Filler12`", length = 25)
	private String filler12;

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

	public JcicB085Id getJcicB085Id() {
		return this.jcicB085Id;
	}

	public void setJcicB085Id(JcicB085Id jcicB085Id) {
		this.jcicB085Id = jcicB085Id;
	}

	/**
	 * 資料日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料日期<br>
	 * 
	 *
	 * @param dataYM 資料日期
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 資料別<br>
	 * "85":帳號轉換資料
	 * 
	 * @return String
	 */
	public String getDataType() {
		return this.dataType == null ? "" : this.dataType;
	}

	/**
	 * 資料別<br>
	 * "85":帳號轉換資料
	 *
	 * @param dataType 資料別
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 轉換帳號年月<br>
	 * 以'YYYMMDD'(民國)表示，填報本筆資料帳號轉換年月
	 * 
	 * @return Integer
	 */
	public int getRenewYM() {
		return this.renewYM;
	}

	/**
	 * 轉換帳號年月<br>
	 * 以'YYYMMDD'(民國)表示，填報本筆資料帳號轉換年月
	 *
	 * @param renewYM 轉換帳號年月
	 */
	public void setRenewYM(int renewYM) {
		this.renewYM = renewYM;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * 左靠，身份證或統一證號
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 授信戶IDN/BAN<br>
	 * 左靠，身份證或統一證號
	 *
	 * @param custId 授信戶IDN/BAN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 轉換前總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getBefBankItem() {
		return this.befBankItem == null ? "" : this.befBankItem;
	}

	/**
	 * 轉換前總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 *
	 * @param befBankItem 轉換前總行代號
	 */
	public void setBefBankItem(String befBankItem) {
		this.befBankItem = befBankItem;
	}

	/**
	 * 轉換前分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getBefBranchItem() {
		return this.befBranchItem == null ? "" : this.befBranchItem;
	}

	/**
	 * 轉換前分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 *
	 * @param befBranchItem 轉換前分行代號
	 */
	public void setBefBranchItem(String befBranchItem) {
		this.befBranchItem = befBranchItem;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller6() {
		return this.filler6 == null ? "" : this.filler6;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler6 空白
	 */
	public void setFiller6(String filler6) {
		this.filler6 = filler6;
	}

	/**
	 * 轉換前帳號<br>
	 * Key,左靠，填報本筆撥款轉換前帳號。
	 * 
	 * @return String
	 */
	public String getBefAcctNo() {
		return this.befAcctNo == null ? "" : this.befAcctNo;
	}

	/**
	 * 轉換前帳號<br>
	 * Key,左靠，填報本筆撥款轉換前帳號。
	 *
	 * @param befAcctNo 轉換前帳號
	 */
	public void setBefAcctNo(String befAcctNo) {
		this.befAcctNo = befAcctNo;
	}

	/**
	 * 轉換後總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getAftBankItem() {
		return this.aftBankItem == null ? "" : this.aftBankItem;
	}

	/**
	 * 轉換後總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 *
	 * @param aftBankItem 轉換後總行代號
	 */
	public void setAftBankItem(String aftBankItem) {
		this.aftBankItem = aftBankItem;
	}

	/**
	 * 轉換後分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getAftBranchItem() {
		return this.aftBranchItem == null ? "" : this.aftBranchItem;
	}

	/**
	 * 轉換後分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 *
	 * @param aftBranchItem 轉換後分行代號
	 */
	public void setAftBranchItem(String aftBranchItem) {
		this.aftBranchItem = aftBranchItem;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller10() {
		return this.filler10 == null ? "" : this.filler10;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler10 空白
	 */
	public void setFiller10(String filler10) {
		this.filler10 = filler10;
	}

	/**
	 * 轉換後帳號<br>
	 * Key,左靠，填報本筆撥款轉換後帳號。
	 * 
	 * @return String
	 */
	public String getAftAcctNo() {
		return this.aftAcctNo == null ? "" : this.aftAcctNo;
	}

	/**
	 * 轉換後帳號<br>
	 * Key,左靠，填報本筆撥款轉換後帳號。
	 *
	 * @param aftAcctNo 轉換後帳號
	 */
	public void setAftAcctNo(String aftAcctNo) {
		this.aftAcctNo = aftAcctNo;
	}

	/**
	 * 空白<br>
	 * 備用
	 * 
	 * @return String
	 */
	public String getFiller12() {
		return this.filler12 == null ? "" : this.filler12;
	}

	/**
	 * 空白<br>
	 * 備用
	 *
	 * @param filler12 空白
	 */
	public void setFiller12(String filler12) {
		this.filler12 = filler12;
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
		return "JcicB085 [jcicB085Id=" + jcicB085Id + ", dataType=" + dataType + ", renewYM=" + renewYM + ", custId=" + custId + ", befBankItem=" + befBankItem + ", befBranchItem=" + befBranchItem
				+ ", filler6=" + filler6 + ", aftBankItem=" + aftBankItem + ", aftBranchItem=" + aftBranchItem + ", filler10=" + filler10 + ", filler12=" + filler12 + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
