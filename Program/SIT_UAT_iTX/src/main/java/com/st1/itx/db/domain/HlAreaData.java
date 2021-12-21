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

/**
 * HlAreaData 區域資料主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`HlAreaData`")
public class HlAreaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8935254562367232656L;

// 區域代碼
	@Id
	@Column(name = "`AreaUnitNo`", length = 6)
	private String areaUnitNo = " ";

	// 區域名稱
	@Column(name = "`AreaName`", length = 20)
	private String areaName;

	// 區域主管員編
	@Column(name = "`AreaChiefEmpNo`", length = 6)
	private String areaChiefEmpNo;

	// 區域主管名稱
	@Column(name = "`AreaChiefName`", length = 15)
	private String areaChiefName;

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
	 * 區域代碼<br>
	 * 
	 * @return String
	 */
	public String getAreaUnitNo() {
		return this.areaUnitNo == null ? "" : this.areaUnitNo;
	}

	/**
	 * 區域代碼<br>
	 * 
	 *
	 * @param areaUnitNo 區域代碼
	 */
	public void setAreaUnitNo(String areaUnitNo) {
		this.areaUnitNo = areaUnitNo;
	}

	/**
	 * 區域名稱<br>
	 * 
	 * @return String
	 */
	public String getAreaName() {
		return this.areaName == null ? "" : this.areaName;
	}

	/**
	 * 區域名稱<br>
	 * 
	 *
	 * @param areaName 區域名稱
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 區域主管員編<br>
	 * 
	 * @return String
	 */
	public String getAreaChiefEmpNo() {
		return this.areaChiefEmpNo == null ? "" : this.areaChiefEmpNo;
	}

	/**
	 * 區域主管員編<br>
	 * 
	 *
	 * @param areaChiefEmpNo 區域主管員編
	 */
	public void setAreaChiefEmpNo(String areaChiefEmpNo) {
		this.areaChiefEmpNo = areaChiefEmpNo;
	}

	/**
	 * 區域主管名稱<br>
	 * 
	 * @return String
	 */
	public String getAreaChiefName() {
		return this.areaChiefName == null ? "" : this.areaChiefName;
	}

	/**
	 * 區域主管名稱<br>
	 * 
	 *
	 * @param areaChiefName 區域主管名稱
	 */
	public void setAreaChiefName(String areaChiefName) {
		this.areaChiefName = areaChiefName;
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
		return "HlAreaData [areaUnitNo=" + areaUnitNo + ", areaName=" + areaName + ", areaChiefEmpNo=" + areaChiefEmpNo + ", areaChiefName=" + areaChiefName + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
