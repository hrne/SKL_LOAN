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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * HlCusData 借款人資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlCusData`")
public class HlCusData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -665049516401057419L;

// 借款人戶號
	@Id
	@Column(name = "`HlCusNo`")
	private Long hlCusNo = 0L;

	// 客戶姓名
	@Column(name = "`HlCusName`", length = 50)
	private String hlCusName;

	// 更新日期
	@Column(name = "`ProcessDate`")
	private int processDate = 0;

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
	 * 借款人戶號<br>
	 * 
	 * @return Long
	 */
	public Long getHlCusNo() {
		return this.hlCusNo;
	}

	/**
	 * 借款人戶號<br>
	 * 
	 *
	 * @param hlCusNo 借款人戶號
	 */
	public void setHlCusNo(Long hlCusNo) {
		this.hlCusNo = hlCusNo;
	}

	/**
	 * 客戶姓名<br>
	 * 
	 * @return String
	 */
	public String getHlCusName() {
		return this.hlCusName == null ? "" : this.hlCusName;
	}

	/**
	 * 客戶姓名<br>
	 * 
	 *
	 * @param hlCusName 客戶姓名
	 */
	public void setHlCusName(String hlCusName) {
		this.hlCusName = hlCusName;
	}

	/**
	 * 更新日期<br>
	 * 
	 * @return Integer
	 */
	public int getProcessDate() {
		return StaticTool.bcToRoc(this.processDate);
	}

	/**
	 * 更新日期<br>
	 * 
	 *
	 * @param processDate 更新日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setProcessDate(int processDate) throws LogicException {
		this.processDate = StaticTool.rocToBc(processDate);
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
		return "HlCusData [hlCusNo=" + hlCusNo + ", hlCusName=" + hlCusName + ", processDate=" + processDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
