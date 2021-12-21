package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Basic;

/**
 * TxAttachment 附件檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAttachment`")
public class TxAttachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6562071519433418859L;

// 檔案序號
	@Id
	@Column(name = "`FileNo`")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "`TxAttachment_SEQ`")
	@SequenceGenerator(name = "`TxAttachment_SEQ`", sequenceName = "`TxAttachment_SEQ`", allocationSize = 1)
	private Long fileNo = 0L;

	// 交易代號
	@Column(name = "`TranNo`", length = 5)
	private String tranNo;

	// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 撥款
	@Column(name = "`BormNo`")
	private int bormNo = 0;

	// 交易參考編號
	@Column(name = "`MrKey`", length = 100)
	private String mrKey;

	// 附件類別
	@Column(name = "`TypeItem`", length = 50)
	private String typeItem;

	// 檔名
	@Column(name = "`FileItem`", length = 100)
	private String fileItem;

	// 檔案內容
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "`FileData`")
	private byte[] fileData;

	// 備註
	@Column(name = "`Desc`", length = 100)
	private String desc;

	// 狀態
	/* 0:正常 1:刪除 */
	@Column(name = "`Status`")
	private int status = 0;

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
	public Long getFileNo() {
		return this.fileNo;
	}

	/**
	 * 檔案序號<br>
	 * 
	 *
	 * @param fileNo 檔案序號
	 */
	public void setFileNo(Long fileNo) {
		this.fileNo = fileNo;
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
	 * 額度<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款<br>
	 * 
	 *
	 * @param bormNo 撥款
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 交易參考編號<br>
	 * 
	 * @return String
	 */
	public String getMrKey() {
		return this.mrKey == null ? "" : this.mrKey;
	}

	/**
	 * 交易參考編號<br>
	 * 
	 *
	 * @param mrKey 交易參考編號
	 */
	public void setMrKey(String mrKey) {
		this.mrKey = mrKey;
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
	 * 檔名<br>
	 * 
	 * @return String
	 */
	public String getFileItem() {
		return this.fileItem == null ? "" : this.fileItem;
	}

	/**
	 * 檔名<br>
	 * 
	 *
	 * @param fileItem 檔名
	 */
	public void setFileItem(String fileItem) {
		this.fileItem = fileItem;
	}

	/**
	 * 檔案內容<br>
	 * 
	 * @return byte[]
	 */
	public byte[] getFileData() {
		return this.fileData;
	}

	/**
	 * 檔案內容<br>
	 * 
	 *
	 * @param fileData 檔案內容
	 */
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	/**
	 * 備註<br>
	 * 
	 * @return String
	 */
	public String getDesc() {
		return this.desc == null ? "" : this.desc;
	}

	/**
	 * 備註<br>
	 * 
	 *
	 * @param desc 備註
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 狀態<br>
	 * 0:正常 1:刪除
	 * 
	 * @return Integer
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 狀態<br>
	 * 0:正常 1:刪除
	 *
	 * @param status 狀態
	 */
	public void setStatus(int status) {
		this.status = status;
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
		return "TxAttachment [fileNo=" + fileNo + ", tranNo=" + tranNo + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", mrKey=" + mrKey + ", typeItem=" + typeItem
				+ ", fileItem=" + fileItem + ", fileData=" + fileData + ", desc=" + desc + ", status=" + status + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
