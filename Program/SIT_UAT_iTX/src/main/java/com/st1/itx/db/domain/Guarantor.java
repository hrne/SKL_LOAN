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
 * Guarantor 保證人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Guarantor`")
public class Guarantor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5247229413651184893L;

	@EmbeddedId
	private GuarantorId guarantorId;

	// 核准號碼
	/* 規劃調整為ApplNo */
	@Column(name = "`ApproveNo`", insertable = false, updatable = false)
	private int approveNo = 0;

	// 保證人客戶識別碼
	@Column(name = "`GuaUKey`", length = 32, insertable = false, updatable = false)
	private String guaUKey;

	// 保證人關係代碼
	/* 保證人關係代碼檔CdGuarantor */
	@Column(name = "`GuaRelCode`", length = 2)
	private String guaRelCode;

	// 保證金額
	@Column(name = "`GuaAmt`")
	private BigDecimal guaAmt = new BigDecimal("0");

	// 保證類別代碼
	/*
	 * 共用代碼檔01:連帶保證人02:擔保品保證人兼連帶保證人03:一般保證人04:擔保品提供人兼一般保證人05:擔保品提供人06:共同借款人07:
	 * 共同發票人08:票據債務人09:連帶借款人10:連帶債務人11:擔保品提供人兼連帶債務人
	 */
	@Column(name = "`GuaTypeCode`", length = 2)
	private String guaTypeCode;

	// 對保日期
	@Column(name = "`GuaDate`")
	private int guaDate = 0;

	// 保證狀況碼
	/* 共用代碼檔0:解除1:設定2:全部解除3:向後解除 */
	@Column(name = "`GuaStatCode`", length = 1)
	private String guaStatCode;

	// 解除日期
	@Column(name = "`CancelDate`")
	private int cancelDate = 0;

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

	public GuarantorId getGuarantorId() {
		return this.guarantorId;
	}

	public void setGuarantorId(GuarantorId guarantorId) {
		this.guarantorId = guarantorId;
	}

	/**
	 * 核准號碼<br>
	 * 規劃調整為ApplNo
	 * 
	 * @return Integer
	 */
	public int getApproveNo() {
		return this.approveNo;
	}

	/**
	 * 核准號碼<br>
	 * 規劃調整為ApplNo
	 *
	 * @param approveNo 核准號碼
	 */
	public void setApproveNo(int approveNo) {
		this.approveNo = approveNo;
	}

	/**
	 * 保證人客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getGuaUKey() {
		return this.guaUKey == null ? "" : this.guaUKey;
	}

	/**
	 * 保證人客戶識別碼<br>
	 * 
	 *
	 * @param guaUKey 保證人客戶識別碼
	 */
	public void setGuaUKey(String guaUKey) {
		this.guaUKey = guaUKey;
	}

	/**
	 * 保證人關係代碼<br>
	 * 保證人關係代碼檔CdGuarantor
	 * 
	 * @return String
	 */
	public String getGuaRelCode() {
		return this.guaRelCode == null ? "" : this.guaRelCode;
	}

	/**
	 * 保證人關係代碼<br>
	 * 保證人關係代碼檔CdGuarantor
	 *
	 * @param guaRelCode 保證人關係代碼
	 */
	public void setGuaRelCode(String guaRelCode) {
		this.guaRelCode = guaRelCode;
	}

	/**
	 * 保證金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getGuaAmt() {
		return this.guaAmt;
	}

	/**
	 * 保證金額<br>
	 * 
	 *
	 * @param guaAmt 保證金額
	 */
	public void setGuaAmt(BigDecimal guaAmt) {
		this.guaAmt = guaAmt;
	}

	/**
	 * 保證類別代碼<br>
	 * 共用代碼檔 01:連帶保證人 02:擔保品保證人兼連帶保證人 03:一般保證人 04:擔保品提供人兼一般保證人 05:擔保品提供人 06:共同借款人
	 * 07:共同發票人 08:票據債務人 09:連帶借款人 10:連帶債務人 11:擔保品提供人兼連帶債務人
	 * 
	 * @return String
	 */
	public String getGuaTypeCode() {
		return this.guaTypeCode == null ? "" : this.guaTypeCode;
	}

	/**
	 * 保證類別代碼<br>
	 * 共用代碼檔 01:連帶保證人 02:擔保品保證人兼連帶保證人 03:一般保證人 04:擔保品提供人兼一般保證人 05:擔保品提供人 06:共同借款人
	 * 07:共同發票人 08:票據債務人 09:連帶借款人 10:連帶債務人 11:擔保品提供人兼連帶債務人
	 *
	 * @param guaTypeCode 保證類別代碼
	 */
	public void setGuaTypeCode(String guaTypeCode) {
		this.guaTypeCode = guaTypeCode;
	}

	/**
	 * 對保日期<br>
	 * 
	 * @return Integer
	 */
	public int getGuaDate() {
		return StaticTool.bcToRoc(this.guaDate);
	}

	/**
	 * 對保日期<br>
	 * 
	 *
	 * @param guaDate 對保日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setGuaDate(int guaDate) throws LogicException {
		this.guaDate = StaticTool.rocToBc(guaDate);
	}

	/**
	 * 保證狀況碼<br>
	 * 共用代碼檔 0:解除 1:設定 2:全部解除 3:向後解除
	 * 
	 * @return String
	 */
	public String getGuaStatCode() {
		return this.guaStatCode == null ? "" : this.guaStatCode;
	}

	/**
	 * 保證狀況碼<br>
	 * 共用代碼檔 0:解除 1:設定 2:全部解除 3:向後解除
	 *
	 * @param guaStatCode 保證狀況碼
	 */
	public void setGuaStatCode(String guaStatCode) {
		this.guaStatCode = guaStatCode;
	}

	/**
	 * 解除日期<br>
	 * 
	 * @return Integer
	 */
	public int getCancelDate() {
		return StaticTool.bcToRoc(this.cancelDate);
	}

	/**
	 * 解除日期<br>
	 * 
	 *
	 * @param cancelDate 解除日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setCancelDate(int cancelDate) throws LogicException {
		this.cancelDate = StaticTool.rocToBc(cancelDate);
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
		return "Guarantor [guarantorId=" + guarantorId + ", guaRelCode=" + guaRelCode + ", guaAmt=" + guaAmt + ", guaTypeCode=" + guaTypeCode + ", guaDate=" + guaDate + ", guaStatCode=" + guaStatCode
				+ ", cancelDate=" + cancelDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
