package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClImmRankDetail 擔保品不動產檔設定順位明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClImmRankDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1663770484585984905L;

// 擔保品代號1
	@Column(name = "`ClCode1`")
	private int clCode1 = 0;

	// 擔保品代號2
	@Column(name = "`ClCode2`")
	private int clCode2 = 0;

	// 擔保品編號
	@Column(name = "`ClNo`")
	private int clNo = 0;

	// 設定順位(1~9)
	@Column(name = "`SettingSeq`", length = 1)
	private String settingSeq = " ";

	public ClImmRankDetailId() {
	}

	public ClImmRankDetailId(int clCode1, int clCode2, int clNo, String settingSeq) {
		this.clCode1 = clCode1;
		this.clCode2 = clCode2;
		this.clNo = clNo;
		this.settingSeq = settingSeq;
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
	 * 擔保品編號<br>
	 * 
	 * @return Integer
	 */
	public int getClNo() {
		return this.clNo;
	}

	/**
	 * 擔保品編號<br>
	 * 
	 *
	 * @param clNo 擔保品編號
	 */
	public void setClNo(int clNo) {
		this.clNo = clNo;
	}

	/**
	 * 設定順位(1~9)<br>
	 * 
	 * @return String
	 */
	public String getSettingSeq() {
		return this.settingSeq == null ? "" : this.settingSeq;
	}

	/**
	 * 設定順位(1~9)<br>
	 * 
	 *
	 * @param settingSeq 設定順位(1~9)
	 */
	public void setSettingSeq(String settingSeq) {
		this.settingSeq = settingSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clCode1, clCode2, clNo, settingSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ClImmRankDetailId clImmRankDetailId = (ClImmRankDetailId) obj;
		return clCode1 == clImmRankDetailId.clCode1 && clCode2 == clImmRankDetailId.clCode2 && clNo == clImmRankDetailId.clNo && settingSeq.equals(clImmRankDetailId.settingSeq);
	}

	@Override
	public String toString() {
		return "ClImmRankDetailId [clCode1=" + clCode1 + ", clCode2=" + clCode2 + ", clNo=" + clNo + ", settingSeq=" + settingSeq + "]";
	}
}
