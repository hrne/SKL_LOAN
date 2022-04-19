package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * TxAttachType 附件類別檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAttachType`")
public class TxAttachType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4635020474470267176L;

// 檔案序號
	@Id
	@Column(name = "`TypeNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxAttachType_SEQ`")
	@SequenceGenerator(name = "`TxAttachType_SEQ`", sequenceName = "`TxAttachType_SEQ`", allocationSize = 1)
	private Long typeNo = 0L;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 附件類別
	@Column(name = "`TypeItem`", length = 50)
	private String typeItem;

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

	/**
	 * 檔案序號<br>
	 * 
	 * @return Long
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getTypeNo() {
		return this.typeNo;
	}

	/**
	 * 檔案序號<br>
	 * 
	 *
	 * @param typeNo 檔案序號
	 */
	public void setTypeNo(Long typeNo) {
		this.typeNo = typeNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 * @return String
	 */
	public String getTranNo() {
		return this.tranNo == null ? "" : this.tranNo;
	}

	/**
	 * 交易代號<br>
	 * 
	 *
	 * @param tranNo 交易代號
	 */
	public void setTranNo(String tranNo) {
		this.tranNo = tranNo;
	}

	/**
	 * 附件類別<br>
	 * 
	 * @return String
	 */
	public String getTypeItem() {
		return this.typeItem == null ? "" : this.typeItem;
	}

	/**
	 * 附件類別<br>
	 * 
	 *
	 * @param typeItem 附件類別
	 */
	public void setTypeItem(String typeItem) {
		this.typeItem = typeItem;
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
		return "TxAttachType [typeNo=" + typeNo + ", tranNo=" + tranNo + ", typeItem=" + typeItem + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
