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
 * TxBizDate 營業日檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxBizDate`")
public class TxBizDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7552484171953384403L;

	// 日期類別
	@Id
	@Column(name = "`DateCode`", length = 10)
	private String dateCode = " ";

	// 星期
	/* 1-5.週一-週五 6.週六 7.週日 */
	@Column(name = "`DayOfWeek`")
	private int dayOfWeek = 0;

	// 本營業日
	@Column(name = "`TbsDy`")
	private int tbsDy = 0;

	// 下營業日
	@Column(name = "`NbsDy`")
	private int nbsDy = 0;

	// 下下營業日
	@Column(name = "`NnbsDy`")
	private int nnbsDy = 0;

	// 上營業日
	@Column(name = "`LbsDy`")
	private int lbsDy = 0;

	// 上個月底日
	@Column(name = "`LmnDy`")
	private int lmnDy = 0;

	// 本月月底日
	@Column(name = "`TmnDy`")
	private int tmnDy = 0;

	// 本月月底營業日
	@Column(name = "`MfbsDy`")
	private int mfbsDy = 0;

	// 本營業日西元
	@Column(name = "`TbsDyf`")
	private int tbsDyf = 0;

	// 下營業日西元
	@Column(name = "`NbsDyf`")
	private int nbsDyf = 0;

	// 下下營業日西元
	@Column(name = "`NnbsDyf`")
	private int nnbsDyf = 0;

	// 上營業日西元
	@Column(name = "`LbsDyf`")
	private int lbsDyf = 0;

	// 上個月底日西元
	@Column(name = "`LmnDyf`")
	private int lmnDyf = 0;

	// 本月月底日西元
	@Column(name = "`TmnDyf`")
	private int tmnDyf = 0;

	// 本月月底營業日西元
	@Column(name = "`MfbsDyf`")
	private int mfbsDyf = 0;

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
	 * 日期類別<br>
	 * 
	 * @return String
	 */
	public String getDateCode() {
		return this.dateCode == null ? "" : this.dateCode;
	}

	/**
	 * 日期類別<br>
	 * 
	 *
	 * @param dateCode 日期類別
	 */
	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}

	/**
	 * 星期<br>
	 * 1-5.週一-週五 6.週六 7.週日
	 * 
	 * @return Integer
	 */
	public int getDayOfWeek() {
		return this.dayOfWeek;
	}

	/**
	 * 星期<br>
	 * 1-5.週一-週五 6.週六 7.週日
	 *
	 * @param dayOfWeek 星期
	 */
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * 本營業日<br>
	 * 
	 * @return Integer
	 */
	public int getTbsDy() {
		return this.tbsDy;
	}

	/**
	 * 本營業日<br>
	 * 
	 *
	 * @param tbsDy 本營業日
	 */
	public void setTbsDy(int tbsDy) {
		this.tbsDy = tbsDy;
	}

	/**
	 * 下營業日<br>
	 * 
	 * @return Integer
	 */
	public int getNbsDy() {
		return this.nbsDy;
	}

	/**
	 * 下營業日<br>
	 * 
	 *
	 * @param nbsDy 下營業日
	 */
	public void setNbsDy(int nbsDy) {
		this.nbsDy = nbsDy;
	}

	/**
	 * 下下營業日<br>
	 * 
	 * @return Integer
	 */
	public int getNnbsDy() {
		return this.nnbsDy;
	}

	/**
	 * 下下營業日<br>
	 * 
	 *
	 * @param nnbsDy 下下營業日
	 */
	public void setNnbsDy(int nnbsDy) {
		this.nnbsDy = nnbsDy;
	}

	/**
	 * 上營業日<br>
	 * 
	 * @return Integer
	 */
	public int getLbsDy() {
		return this.lbsDy;
	}

	/**
	 * 上營業日<br>
	 * 
	 *
	 * @param lbsDy 上營業日
	 */
	public void setLbsDy(int lbsDy) {
		this.lbsDy = lbsDy;
	}

	/**
	 * 上個月底日<br>
	 * 
	 * @return Integer
	 */
	public int getLmnDy() {
		return this.lmnDy;
	}

	/**
	 * 上個月底日<br>
	 * 
	 *
	 * @param lmnDy 上個月底日
	 */
	public void setLmnDy(int lmnDy) {
		this.lmnDy = lmnDy;
	}

	/**
	 * 本月月底日<br>
	 * 
	 * @return Integer
	 */
	public int getTmnDy() {
		return this.tmnDy;
	}

	/**
	 * 本月月底日<br>
	 * 
	 *
	 * @param tmnDy 本月月底日
	 */
	public void setTmnDy(int tmnDy) {
		this.tmnDy = tmnDy;
	}

	/**
	 * 本月月底營業日<br>
	 * 
	 * @return Integer
	 */
	public int getMfbsDy() {
		return this.mfbsDy;
	}

	/**
	 * 本月月底營業日<br>
	 * 
	 *
	 * @param mfbsDy 本月月底營業日
	 */
	public void setMfbsDy(int mfbsDy) {
		this.mfbsDy = mfbsDy;
	}

	/**
	 * 本營業日西元<br>
	 * 
	 * @return Integer
	 */
	public int getTbsDyf() {
		return this.tbsDyf;
	}

	/**
	 * 本營業日西元<br>
	 * 
	 *
	 * @param tbsDyf 本營業日西元
	 */
	public void setTbsDyf(int tbsDyf) {
		this.tbsDyf = tbsDyf;
	}

	/**
	 * 下營業日西元<br>
	 * 
	 * @return Integer
	 */
	public int getNbsDyf() {
		return this.nbsDyf;
	}

	/**
	 * 下營業日西元<br>
	 * 
	 *
	 * @param nbsDyf 下營業日西元
	 */
	public void setNbsDyf(int nbsDyf) {
		this.nbsDyf = nbsDyf;
	}

	/**
	 * 下下營業日西元<br>
	 * 
	 * @return Integer
	 */
	public int getNnbsDyf() {
		return this.nnbsDyf;
	}

	/**
	 * 下下營業日西元<br>
	 * 
	 *
	 * @param nnbsDyf 下下營業日西元
	 */
	public void setNnbsDyf(int nnbsDyf) {
		this.nnbsDyf = nnbsDyf;
	}

	/**
	 * 上營業日西元<br>
	 * 
	 * @return Integer
	 */
	public int getLbsDyf() {
		return this.lbsDyf;
	}

	/**
	 * 上營業日西元<br>
	 * 
	 *
	 * @param lbsDyf 上營業日西元
	 */
	public void setLbsDyf(int lbsDyf) {
		this.lbsDyf = lbsDyf;
	}

	/**
	 * 上個月底日西元<br>
	 * 
	 * @return Integer
	 */
	public int getLmnDyf() {
		return this.lmnDyf;
	}

	/**
	 * 上個月底日西元<br>
	 * 
	 *
	 * @param lmnDyf 上個月底日西元
	 */
	public void setLmnDyf(int lmnDyf) {
		this.lmnDyf = lmnDyf;
	}

	/**
	 * 本月月底日西元<br>
	 * 
	 * @return Integer
	 */
	public int getTmnDyf() {
		return this.tmnDyf;
	}

	/**
	 * 本月月底日西元<br>
	 * 
	 *
	 * @param tmnDyf 本月月底日西元
	 */
	public void setTmnDyf(int tmnDyf) {
		this.tmnDyf = tmnDyf;
	}

	/**
	 * 本月月底營業日西元<br>
	 * 
	 * @return Integer
	 */
	public int getMfbsDyf() {
		return this.mfbsDyf;
	}

	/**
	 * 本月月底營業日西元<br>
	 * 
	 *
	 * @param mfbsDyf 本月月底營業日西元
	 */
	public void setMfbsDyf(int mfbsDyf) {
		this.mfbsDyf = mfbsDyf;
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
		return "TxBizDate [dateCode=" + dateCode + ", dayOfWeek=" + dayOfWeek + ", tbsDy=" + tbsDy + ", nbsDy=" + nbsDy + ", nnbsDy=" + nnbsDy + ", lbsDy=" + lbsDy + ", lmnDy=" + lmnDy + ", tmnDy="
				+ tmnDy + ", mfbsDy=" + mfbsDy + ", tbsDyf=" + tbsDyf + ", nbsDyf=" + nbsDyf + ", nnbsDyf=" + nnbsDyf + ", lbsDyf=" + lbsDyf + ", lmnDyf=" + lmnDyf + ", tmnDyf=" + tmnDyf
				+ ", mfbsDyf=" + mfbsDyf + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
