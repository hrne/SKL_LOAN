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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CreditRating 信用評等檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CreditRating`")
public class CreditRating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3919462373902368398L;

	@EmbeddedId
	private CreditRatingId creditRatingId;

	// 資料年月
	/* 資料轉入月份YYYYMM */
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 企業戶/個人戶
	/* 1: 企業戶2: 個人戶(依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶) */
	@Column(name = "`CreditRatingCode`", length = 1)
	private String creditRatingCode;

	// 原始認列時信用評等模型
	/* A/B */
	@Column(name = "`OriModel`", length = 1)
	private String oriModel;

	// 原始認列信用評等日期
	/* YYYYMMDD */
	@Column(name = "`OriRatingDate`")
	private int oriRatingDate = 0;

	// 原始認列時時信用評等
	/* 1/2/3/4/5……/D */
	@Column(name = "`OriRating`", length = 1)
	private String oriRating;

	// 財務報導日時信用評等模型
	/* A/B */
	@Column(name = "`Model`", length = 1)
	private String model;

	// 財務報導日信用評等評定日期
	/* YYYYMMDD */
	@Column(name = "`RatingDate`")
	private int ratingDate = 0;

	// 財務報導日時信用評等
	/* 1/2/3/4/5……/D */
	@Column(name = "`Rating`", length = 1)
	private String rating;

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

	public CreditRatingId getCreditRatingId() {
		return this.creditRatingId;
	}

	public void setCreditRatingId(CreditRatingId creditRatingId) {
		this.creditRatingId = creditRatingId;
	}

	/**
	 * 資料年月<br>
	 * 資料轉入月份 YYYYMM
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料年月<br>
	 * 資料轉入月份 YYYYMM
	 *
	 * @param dataYM 資料年月
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
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
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 企業戶/個人戶<br>
	 * 1: 企業戶 2: 個人戶 (依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶)
	 * 
	 * @return String
	 */
	public String getCreditRatingCode() {
		return this.creditRatingCode == null ? "" : this.creditRatingCode;
	}

	/**
	 * 企業戶/個人戶<br>
	 * 1: 企業戶 2: 個人戶 (依現行代碼，唯企業戶與個人戶之分類需參考信用評等模型。自然人採用企金自然人評等模型者，應歸類為企業戶)
	 *
	 * @param creditRatingCode 企業戶/個人戶
	 */
	public void setCreditRatingCode(String creditRatingCode) {
		this.creditRatingCode = creditRatingCode;
	}

	/**
	 * 原始認列時信用評等模型<br>
	 * A/B
	 * 
	 * @return String
	 */
	public String getOriModel() {
		return this.oriModel == null ? "" : this.oriModel;
	}

	/**
	 * 原始認列時信用評等模型<br>
	 * A/B
	 *
	 * @param oriModel 原始認列時信用評等模型
	 */
	public void setOriModel(String oriModel) {
		this.oriModel = oriModel;
	}

	/**
	 * 原始認列信用評等日期<br>
	 * YYYYMMDD
	 * 
	 * @return Integer
	 */
	public int getOriRatingDate() {
		return StaticTool.bcToRoc(this.oriRatingDate);
	}

	/**
	 * 原始認列信用評等日期<br>
	 * YYYYMMDD
	 *
	 * @param oriRatingDate 原始認列信用評等日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setOriRatingDate(int oriRatingDate) throws LogicException {
		this.oriRatingDate = StaticTool.rocToBc(oriRatingDate);
	}

	/**
	 * 原始認列時時信用評等<br>
	 * 1/2/3/4/5……/D
	 * 
	 * @return String
	 */
	public String getOriRating() {
		return this.oriRating == null ? "" : this.oriRating;
	}

	/**
	 * 原始認列時時信用評等<br>
	 * 1/2/3/4/5……/D
	 *
	 * @param oriRating 原始認列時時信用評等
	 */
	public void setOriRating(String oriRating) {
		this.oriRating = oriRating;
	}

	/**
	 * 財務報導日時信用評等模型<br>
	 * A/B
	 * 
	 * @return String
	 */
	public String getModel() {
		return this.model == null ? "" : this.model;
	}

	/**
	 * 財務報導日時信用評等模型<br>
	 * A/B
	 *
	 * @param model 財務報導日時信用評等模型
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 財務報導日信用評等評定日期<br>
	 * YYYYMMDD
	 * 
	 * @return Integer
	 */
	public int getRatingDate() {
		return StaticTool.bcToRoc(this.ratingDate);
	}

	/**
	 * 財務報導日信用評等評定日期<br>
	 * YYYYMMDD
	 *
	 * @param ratingDate 財務報導日信用評等評定日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setRatingDate(int ratingDate) throws LogicException {
		this.ratingDate = StaticTool.rocToBc(ratingDate);
	}

	/**
	 * 財務報導日時信用評等<br>
	 * 1/2/3/4/5……/D
	 * 
	 * @return String
	 */
	public String getRating() {
		return this.rating == null ? "" : this.rating;
	}

	/**
	 * 財務報導日時信用評等<br>
	 * 1/2/3/4/5……/D
	 *
	 * @param rating 財務報導日時信用評等
	 */
	public void setRating(String rating) {
		this.rating = rating;
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
		return "CreditRating [creditRatingId=" + creditRatingId + ", creditRatingCode=" + creditRatingCode + ", oriModel=" + oriModel + ", oriRatingDate=" + oriRatingDate + ", oriRating=" + oriRating
				+ ", model=" + model + ", ratingDate=" + ratingDate + ", rating=" + rating + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
