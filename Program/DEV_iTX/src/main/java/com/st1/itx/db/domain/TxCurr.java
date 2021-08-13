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
 * TxCurr 幣別檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxCurr`")
public class TxCurr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3479648598104773015L;

	// 幣別代號
	@Id
	@Column(name = "`CurCd`")
	private int curCd = 0;

	// 幣別
	@Column(name = "`CurNm`", length = 3)
	private String curNm;

	// 英文名稱
	@Column(name = "`CurEm`", length = 50)
	private String curEm;

	// 中文名稱
	@Column(name = "`CurCm`", length = 20)
	private String curCm;

	// 放行記號
	@Column(name = "`ActFg`")
	private int actFg = 0;

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
	 * 幣別代號<br>
	 * 
	 * @return Integer
	 */
	public int getCurCd() {
		return this.curCd;
	}

	/**
	 * 幣別代號<br>
	 * 
	 *
	 * @param curCd 幣別代號
	 */
	public void setCurCd(int curCd) {
		this.curCd = curCd;
	}

	/**
	 * 幣別<br>
	 * 
	 * @return String
	 */
	public String getCurNm() {
		return this.curNm == null ? "" : this.curNm;
	}

	/**
	 * 幣別<br>
	 * 
	 *
	 * @param curNm 幣別
	 */
	public void setCurNm(String curNm) {
		this.curNm = curNm;
	}

	/**
	 * 英文名稱<br>
	 * 
	 * @return String
	 */
	public String getCurEm() {
		return this.curEm == null ? "" : this.curEm;
	}

	/**
	 * 英文名稱<br>
	 * 
	 *
	 * @param curEm 英文名稱
	 */
	public void setCurEm(String curEm) {
		this.curEm = curEm;
	}

	/**
	 * 中文名稱<br>
	 * 
	 * @return String
	 */
	public String getCurCm() {
		return this.curCm == null ? "" : this.curCm;
	}

	/**
	 * 中文名稱<br>
	 * 
	 *
	 * @param curCm 中文名稱
	 */
	public void setCurCm(String curCm) {
		this.curCm = curCm;
	}

	/**
	 * 放行記號<br>
	 * 
	 * @return Integer
	 */
	public int getActFg() {
		return this.actFg;
	}

	/**
	 * 放行記號<br>
	 * 
	 *
	 * @param actFg 放行記號
	 */
	public void setActFg(int actFg) {
		this.actFg = actFg;
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
		return "TxCurr [curCd=" + curCd + ", curNm=" + curNm + ", curEm=" + curEm + ", curCm=" + curCm + ", actFg=" + actFg + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
