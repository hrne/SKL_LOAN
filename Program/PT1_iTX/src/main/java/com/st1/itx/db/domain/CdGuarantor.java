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
 * CdGuarantor 保證人關係代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdGuarantor`")
public class CdGuarantor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8561248985083379065L;

// 保證人關係代碼
	/*
	 * 01 負責人02 負責人之配偶03 負責人之父母04 負責人之子女05 負責人之兄弟姐妹06 董事07 董事之配偶08 董事之父母09 董事之子女10
	 * 董事之兄弟姐妹11 股東12 股東之配偶13 股東之父母14 股東之子女15 股東之兄弟姐妹16 總經理17 總經理之配偶18 總經理之父母19
	 * 總經理之子女20 總經理之兄弟姐妹21 其他經理人或員工22 其他經理人或員工之配偶23 其他經理人或員工之父母24 其他經理人或員工之子女25
	 * 其他經理人或員工之兄弟姐妹26 關係企業27 擔任負責人之企業28 配偶29 父母30 子女31 兄弟姐妹32 祖父母33 外祖父母34 孫子女35
	 * 外孫子女36 配偶之父母37 配偶之兄弟姊妹38 其他親屬39 其他非親屬自然人
	 */
	@Id
	@Column(name = "`GuaRelCode`", length = 2)
	private String guaRelCode = " ";

	// 保證人關係說明
	@Column(name = "`GuaRelItem`", length = 30)
	private String guaRelItem;

	// 保證人關係ＪＣＩＣ代碼
	@Column(name = "`GuaRelJcic`", length = 2)
	private String guaRelJcic;

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
	 * 保證人關係代碼<br>
	 * 01 負責人 02 負責人之配偶 03 負責人之父母 04 負責人之子女 05 負責人之兄弟姐妹 06 董事 07 董事之配偶 08 董事之父母 09
	 * 董事之子女 10 董事之兄弟姐妹 11 股東 12 股東之配偶 13 股東之父母 14 股東之子女 15 股東之兄弟姐妹 16 總經理 17 總經理之配偶
	 * 18 總經理之父母 19 總經理之子女 20 總經理之兄弟姐妹 21 其他經理人或員工 22 其他經理人或員工之配偶 23 其他經理人或員工之父母 24
	 * 其他經理人或員工之子女 25 其他經理人或員工之兄弟姐妹 26 關係企業 27 擔任負責人之企業 28 配偶 29 父母 30 子女 31 兄弟姐妹 32
	 * 祖父母 33 外祖父母 34 孫子女 35 外孫子女 36 配偶之父母 37 配偶之兄弟姊妹 38 其他親屬 39 其他非親屬自然人
	 * 
	 * @return String
	 */
	public String getGuaRelCode() {
		return this.guaRelCode == null ? "" : this.guaRelCode;
	}

	/**
	 * 保證人關係代碼<br>
	 * 01 負責人 02 負責人之配偶 03 負責人之父母 04 負責人之子女 05 負責人之兄弟姐妹 06 董事 07 董事之配偶 08 董事之父母 09
	 * 董事之子女 10 董事之兄弟姐妹 11 股東 12 股東之配偶 13 股東之父母 14 股東之子女 15 股東之兄弟姐妹 16 總經理 17 總經理之配偶
	 * 18 總經理之父母 19 總經理之子女 20 總經理之兄弟姐妹 21 其他經理人或員工 22 其他經理人或員工之配偶 23 其他經理人或員工之父母 24
	 * 其他經理人或員工之子女 25 其他經理人或員工之兄弟姐妹 26 關係企業 27 擔任負責人之企業 28 配偶 29 父母 30 子女 31 兄弟姐妹 32
	 * 祖父母 33 外祖父母 34 孫子女 35 外孫子女 36 配偶之父母 37 配偶之兄弟姊妹 38 其他親屬 39 其他非親屬自然人
	 *
	 * @param guaRelCode 保證人關係代碼
	 */
	public void setGuaRelCode(String guaRelCode) {
		this.guaRelCode = guaRelCode;
	}

	/**
	 * 保證人關係說明<br>
	 * 
	 * @return String
	 */
	public String getGuaRelItem() {
		return this.guaRelItem == null ? "" : this.guaRelItem;
	}

	/**
	 * 保證人關係說明<br>
	 * 
	 *
	 * @param guaRelItem 保證人關係說明
	 */
	public void setGuaRelItem(String guaRelItem) {
		this.guaRelItem = guaRelItem;
	}

	/**
	 * 保證人關係ＪＣＩＣ代碼<br>
	 * 
	 * @return String
	 */
	public String getGuaRelJcic() {
		return this.guaRelJcic == null ? "" : this.guaRelJcic;
	}

	/**
	 * 保證人關係ＪＣＩＣ代碼<br>
	 * 
	 *
	 * @param guaRelJcic 保證人關係ＪＣＩＣ代碼
	 */
	public void setGuaRelJcic(String guaRelJcic) {
		this.guaRelJcic = guaRelJcic;
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
		return "CdGuarantor [guaRelCode=" + guaRelCode + ", guaRelItem=" + guaRelItem + ", guaRelJcic=" + guaRelJcic + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate="
				+ lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
