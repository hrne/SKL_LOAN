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
 * CdCl 擔保品代號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdCl`")
public class CdCl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8232497504262132206L;

	@EmbeddedId
	private CdClId cdClId;

	// 擔保品代號1
	@Column(name = "`ClCode1`", insertable = false, updatable = false)
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`", insertable = false, updatable = false)
	private int clCode2 = 0;

	// 擔保品名稱
	/*
	 * 擔保品代號1 &amp; 擔保品代號2: 擔保品名稱21-01: 住宅1-02: 辦公1-03: 商場1-04: 廠房1-05: 停車位1-99:
	 * 其他2-01: 住宅區2-02: 商業區2-03: 工業區2-09: 其他分區2-10: 甲種建地2-11: 乙種建地2-12: 丙種建地2-13:
	 * 丁種建地2-19: 其他用地3-01: 股票4-01: 其他有價證券5-01: 銀行保證9-01: 車輛9-02: 機器設備
	 */
	@Column(name = "`ClItem`", length = 20)
	private String clItem;

	// JCIC類別
	@Column(name = "`ClTypeJCIC`", length = 2)
	private String clTypeJCIC;

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

	public CdClId getCdClId() {
		return this.cdClId;
	}

	public void setCdClId(CdClId cdClId) {
		this.cdClId = cdClId;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 * @return Integer
	 */
	public int getClCode1() {
		return this.clCode1;
	}

	/**
	 * 擔保品代號1<br>
	 * 
	 *
	 * @param clCode1 擔保品代號1
	 */
	public void setClCode1(int clCode1) {
		this.clCode1 = clCode1;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 * @return Integer
	 */
	public int getClCode2() {
		return this.clCode2;
	}

	/**
	 * 擔保品代號2<br>
	 * 
	 *
	 * @param clCode2 擔保品代號2
	 */
	public void setClCode2(int clCode2) {
		this.clCode2 = clCode2;
	}

	/**
	 * 擔保品名稱<br>
	 * 擔保品代號1 &amp; 擔保品代號2: 擔保品名稱2 1-01: 住宅 1-02: 辦公 1-03: 商場 1-04: 廠房 1-05: 停車位
	 * 1-99: 其他 2-01: 住宅區 2-02: 商業區 2-03: 工業區 2-09: 其他分區 2-10: 甲種建地 2-11: 乙種建地 2-12:
	 * 丙種建地 2-13: 丁種建地 2-19: 其他用地 3-01: 股票 4-01: 其他有價證券 5-01: 銀行保證 9-01: 車輛 9-02:
	 * 機器設備
	 * 
	 * @return String
	 */
	public String getClItem() {
		return this.clItem == null ? "" : this.clItem;
	}

	/**
	 * 擔保品名稱<br>
	 * 擔保品代號1 &amp; 擔保品代號2: 擔保品名稱2 1-01: 住宅 1-02: 辦公 1-03: 商場 1-04: 廠房 1-05: 停車位
	 * 1-99: 其他 2-01: 住宅區 2-02: 商業區 2-03: 工業區 2-09: 其他分區 2-10: 甲種建地 2-11: 乙種建地 2-12:
	 * 丙種建地 2-13: 丁種建地 2-19: 其他用地 3-01: 股票 4-01: 其他有價證券 5-01: 銀行保證 9-01: 車輛 9-02:
	 * 機器設備
	 *
	 * @param clItem 擔保品名稱
	 */
	public void setClItem(String clItem) {
		this.clItem = clItem;
	}

	/**
	 * JCIC類別<br>
	 * 
	 * @return String
	 */
	public String getClTypeJCIC() {
		return this.clTypeJCIC == null ? "" : this.clTypeJCIC;
	}

	/**
	 * JCIC類別<br>
	 * 
	 *
	 * @param clTypeJCIC JCIC類別
	 */
	public void setClTypeJCIC(String clTypeJCIC) {
		this.clTypeJCIC = clTypeJCIC;
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
		return "CdCl [cdClId=" + cdClId + ", clItem=" + clItem + ", clTypeJCIC=" + clTypeJCIC + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
