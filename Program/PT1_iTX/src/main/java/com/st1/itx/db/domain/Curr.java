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
@Table(name = "`Curr`")
public class Curr implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9166895123264367410L;

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
	@Column(name = "`CurCm`", length = 50)
	private String curCm;

	// 放行記號
	@Column(name = "`ActFg`")
	private int actFg = 0;

	// 建立日期
	@Column(name = "`CreateDate`")
	private int createDate = 0;

	// 建立時間
	@Column(name = "`CreateTime`")
	private int createTime = 0;

	// 建立人員
	@Column(name = "`CreateTlrNo`", length = 6)
	private String createTlrNo;

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
	 * 建立日期<br>
	 * 
	 * @return Integer
	 */
	public int getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建立日期<br>
	 * 
	 *
	 * @param createDate 建立日期
	 */
	public void setCreateDate(int createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建立時間<br>
	 * 
	 * @return Integer
	 */
	public int getCreateTime() {
		return this.createTime;
	}

	/**
	 * 建立時間<br>
	 * 
	 *
	 * @param createTime 建立時間
	 */
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	/**
	 * 建立人員<br>
	 * 
	 * @return String
	 */
	public String getCreateTlrNo() {
		return this.createTlrNo == null ? "" : this.createTlrNo;
	}

	/**
	 * 建立人員<br>
	 * 
	 *
	 * @param createTlrNo 建立人員
	 */
	public void setCreateTlrNo(String createTlrNo) {
		this.createTlrNo = createTlrNo;
	}

	@Override
	public String toString() {
		return "Curr [curCd=" + curCd + ", curNm=" + curNm + ", curEm=" + curEm + ", curCm=" + curCm + ", actFg=" + actFg + ", createDate=" + createDate + ", createTime=" + createTime
				+ ", createTlrNo=" + createTlrNo + "]";
	}
}
