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
 * Ias39LGD 違約損失率檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`Ias39LGD`")
public class Ias39LGD implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1454531071281535058L;

	@EmbeddedId
	private Ias39LGDId ias39LGDId;

	// 生效日期
	@Column(name = "`Date`", insertable = false, updatable = false)
	private int date = 0;

	// 類別
	@Column(name = "`Type`", length = 2, insertable = false, updatable = false)
	private String type;

	// 類別說明
	@Column(name = "`TypeDesc`", length = 10)
	private String typeDesc;

	// 違約損失率％
	@Column(name = "`LGDPercent`")
	private BigDecimal lGDPercent = new BigDecimal("0");

	// 啟用記號
	/* Y:啟用 , N:未啟用 */
	@Column(name = "`Enable`", length = 1)
	private String enable;

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

	public Ias39LGDId getIas39LGDId() {
		return this.ias39LGDId;
	}

	public void setIas39LGDId(Ias39LGDId ias39LGDId) {
		this.ias39LGDId = ias39LGDId;
	}

	/**
	 * 生效日期<br>
	 * 
	 * @return Integer
	 */
	public int getDate() {
		return StaticTool.bcToRoc(this.date);
	}

	/**
	 * 生效日期<br>
	 * 
	 *
	 * @param date 生效日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setDate(int date) throws LogicException {
		this.date = StaticTool.rocToBc(date);
	}

	/**
	 * 類別<br>
	 * 
	 * @return String
	 */
	public String getType() {
		return this.type == null ? "" : this.type;
	}

	/**
	 * 類別<br>
	 * 
	 *
	 * @param type 類別
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 類別說明<br>
	 * 
	 * @return String
	 */
	public String getTypeDesc() {
		return this.typeDesc == null ? "" : this.typeDesc;
	}

	/**
	 * 類別說明<br>
	 * 
	 *
	 * @param typeDesc 類別說明
	 */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	/**
	 * 違約損失率％<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLGDPercent() {
		return this.lGDPercent;
	}

	/**
	 * 違約損失率％<br>
	 * 
	 *
	 * @param lGDPercent 違約損失率％
	 */
	public void setLGDPercent(BigDecimal lGDPercent) {
		this.lGDPercent = lGDPercent;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
	 * 
	 * @return String
	 */
	public String getEnable() {
		return this.enable == null ? "" : this.enable;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
	 *
	 * @param enable 啟用記號
	 */
	public void setEnable(String enable) {
		this.enable = enable;
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
		return "Ias39LGD [ias39LGDId=" + ias39LGDId + ", typeDesc=" + typeDesc + ", lGDPercent=" + lGDPercent + ", enable=" + enable + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
