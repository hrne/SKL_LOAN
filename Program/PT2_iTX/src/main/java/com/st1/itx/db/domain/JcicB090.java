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
 * JcicB090 擔保品關聯檔資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicB090`")
public class JcicB090 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 32773888474081745L;

	@EmbeddedId
	private JcicB090Id jcicB090Id;

	// 資料日期
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 資料別
	/* "90":擔保品關聯檔資料 */
	@Column(name = "`DataType`", length = 2)
	private String dataType;

	// 總行代號
	/* 金融機構總機構之代號，三位數字 */
	@Column(name = "`BankItem`", length = 3)
	private String bankItem;

	// 分行代號
	/* 金融機構分支機構之代號，四位數字 */
	@Column(name = "`BranchItem`", length = 4)
	private String branchItem;

	// 空白
	/* 空白 */
	@Column(name = "`Filler4`", length = 2)
	private String filler4;

	// 授信戶IDN/BAN
	/* Key,左靠，身份證或統一證號 */
	@Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
	private String custId;

	// 擔保品控制編碼
	/* Key,左靠右補空白 */
	@Column(name = "`ClActNo`", length = 50, insertable = false, updatable = false)
	private String clActNo;

	// 額度控制編碼
	/* Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼 */
	@Column(name = "`FacmNo`", length = 50, insertable = false, updatable = false)
	private String facmNo;

	// 海外不動產擔保品資料註記
	/* 左靠右補空白 Y=海外不動產擔保品；N或空白=非海外不動產擔保品 */
	@Column(name = "`GlOverseas`", length = 2)
	private String glOverseas;

	// 資料所屬年月
	/* 請填報本筆授信資料所屬年月(民國年) */
	@Column(name = "`JcicDataYM`")
	private int jcicDataYM = 0;

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

	public JcicB090Id getJcicB090Id() {
		return this.jcicB090Id;
	}

	public void setJcicB090Id(JcicB090Id jcicB090Id) {
		this.jcicB090Id = jcicB090Id;
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
	 * "90":擔保品關聯檔資料
	 * 
	 * @return String
	 */
	public String getDataType() {
		return this.dataType == null ? "" : this.dataType;
	}

	/**
	 * 資料別<br>
	 * "90":擔保品關聯檔資料
	 *
	 * @param dataType 資料別
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 * 
	 * @return String
	 */
	public String getBankItem() {
		return this.bankItem == null ? "" : this.bankItem;
	}

	/**
	 * 總行代號<br>
	 * 金融機構總機構之代號，三位數字
	 *
	 * @param bankItem 總行代號
	 */
	public void setBankItem(String bankItem) {
		this.bankItem = bankItem;
	}

	/**
	 * 分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 * 
	 * @return String
	 */
	public String getBranchItem() {
		return this.branchItem == null ? "" : this.branchItem;
	}

	/**
	 * 分行代號<br>
	 * 金融機構分支機構之代號，四位數字
	 *
	 * @param branchItem 分行代號
	 */
	public void setBranchItem(String branchItem) {
		this.branchItem = branchItem;
	}

	/**
	 * 空白<br>
	 * 空白
	 * 
	 * @return String
	 */
	public String getFiller4() {
		return this.filler4 == null ? "" : this.filler4;
	}

	/**
	 * 空白<br>
	 * 空白
	 *
	 * @param filler4 空白
	 */
	public void setFiller4(String filler4) {
		this.filler4 = filler4;
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
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 * 
	 * @return String
	 */
	public String getClActNo() {
		return this.clActNo == null ? "" : this.clActNo;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 *
	 * @param clActNo 擔保品控制編碼
	 */
	public void setClActNo(String clActNo) {
		this.clActNo = clActNo;
	}

	/**
	 * 額度控制編碼<br>
	 * Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼
	 * 
	 * @return String
	 */
	public String getFacmNo() {
		return this.facmNo == null ? "" : this.facmNo;
	}

	/**
	 * 額度控制編碼<br>
	 * Key,左靠右補空白,請填本階額度控制編碼或撥款帳號，如屬房貸業務，請填報額度控制編碼
	 *
	 * @param facmNo 額度控制編碼
	 */
	public void setFacmNo(String facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 海外不動產擔保品資料註記<br>
	 * 左靠右補空白 Y=海外不動產擔保品；N或空白=非海外不動產擔保品
	 * 
	 * @return String
	 */
	public String getGlOverseas() {
		return this.glOverseas == null ? "" : this.glOverseas;
	}

	/**
	 * 海外不動產擔保品資料註記<br>
	 * 左靠右補空白 Y=海外不動產擔保品；N或空白=非海外不動產擔保品
	 *
	 * @param glOverseas 海外不動產擔保品資料註記
	 */
	public void setGlOverseas(String glOverseas) {
		this.glOverseas = glOverseas;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月(民國年)
	 * 
	 * @return Integer
	 */
	public int getJcicDataYM() {
		return this.jcicDataYM;
	}

	/**
	 * 資料所屬年月<br>
	 * 請填報本筆授信資料所屬年月(民國年)
	 *
	 * @param jcicDataYM 資料所屬年月
	 */
	public void setJcicDataYM(int jcicDataYM) {
		this.jcicDataYM = jcicDataYM;
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
		return "JcicB090 [jcicB090Id=" + jcicB090Id + ", dataType=" + dataType + ", bankItem=" + bankItem + ", branchItem=" + branchItem + ", filler4=" + filler4 + ", glOverseas=" + glOverseas
				+ ", jcicDataYM=" + jcicDataYM + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
