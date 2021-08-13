package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@Table(name = "`BizDate`")
public class BizDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8863837612860512385L;

	// 國別
	@Id
	@Column(name = "`Country`", length = 2)
	private String country = " ";

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

	// 日期
	@Column(name = "`CreateDate`")
	private int createDate = 0;

	// 時間
	@Column(name = "`CreateTime`")
	private int createTime = 0;

	// 人員
	@Column(name = "`CreateTlrno`", length = 6)
	private String createTlrno;

	/**
	 * 國別<br>
	 * 
	 * @return String
	 */
	public String getCountry() {
		return this.country == null ? "" : this.country;
	}

	/**
	 * 國別<br>
	 * 
	 *
	 * @param country 國別
	 */
	public void setCountry(String country) {
		this.country = country;
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
	 * 日期<br>
	 * 
	 * @return Integer
	 */
	public int getCreateDate() {
		return this.createDate;
	}

	/**
	 * 日期<br>
	 * 
	 *
	 * @param createDate 日期
	 */
	public void setCreateDate(int createDate) {
		this.createDate = createDate;
	}

	/**
	 * 時間<br>
	 * 
	 * @return Integer
	 */
	public int getCreateTime() {
		return this.createTime;
	}

	/**
	 * 時間<br>
	 * 
	 *
	 * @param createTime 時間
	 */
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	/**
	 * 人員<br>
	 * 
	 * @return String
	 */
	public String getCreateTlrno() {
		return this.createTlrno == null ? "" : this.createTlrno;
	}

	/**
	 * 人員<br>
	 * 
	 *
	 * @param createTlrno 人員
	 */
	public void setCreateTlrno(String createTlrno) {
		this.createTlrno = createTlrno;
	}

	@Override
	public String toString() {
		return "BizDate [country=" + country + ", dayOfWeek=" + dayOfWeek + ", tbsDy=" + tbsDy + ", nbsDy=" + nbsDy + ", nnbsDy=" + nnbsDy + ", lbsDy=" + lbsDy + ", lmnDy=" + lmnDy + ", tmnDy="
				+ tmnDy + ", mfbsDy=" + mfbsDy + ", tbsDyf=" + tbsDyf + ", nbsDyf=" + nbsDyf + ", nnbsDyf=" + nnbsDyf + ", lbsDyf=" + lbsDyf + ", lmnDyf=" + lmnDyf + ", tmnDyf=" + tmnDyf
				+ ", mfbsDyf=" + mfbsDyf + ", createDate=" + createDate + ", createTime=" + createTime + ", createTlrno=" + createTlrno + "]";
	}
}
