package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB091 聯徵有價證券(股票除外)擔保品明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB091Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1105633505585288829L;

// 資料日期
	@Column(name = "`DataYM`")
	private int dataYM = 0;

	// 擔保品控制編碼
	/* Key,左靠右補空白 */
	@Column(name = "`ClActNo`", length = 50)
	private String clActNo = " ";

	// 擔保品所有權人或代表人IDN/BAN
	/* Key,左靠，股票持有人身份證或統一證號 */
	@Column(name = "`OwnerId`", length = 10)
	private String ownerId = " ";

	// 發行機構 BAN
	/* Key, */
	@Column(name = "`CompanyId`", length = 8)
	private String companyId = " ";

	public JcicB091Id() {
	}

	public JcicB091Id(int dataYM, String clActNo, String ownerId, String companyId) {
		this.dataYM = dataYM;
		this.clActNo = clActNo;
		this.ownerId = ownerId;
		this.companyId = companyId;
	}

	/**
	 * 資料日期<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 資料日期<br>
	 * 
	 *
	 * @param dataYM 資料日期
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 * 
	 * @return String
	 */
	public String getClActNo() {
		return this.clActNo == null ? "" : this.clActNo;
	}

	/**
	 * 擔保品控制編碼<br>
	 * Key,左靠右補空白
	 *
	 * @param clActNo 擔保品控制編碼
	 */
	public void setClActNo(String clActNo) {
		this.clActNo = clActNo;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * Key,左靠，股票持有人身份證或統一證號
	 * 
	 * @return String
	 */
	public String getOwnerId() {
		return this.ownerId == null ? "" : this.ownerId;
	}

	/**
	 * 擔保品所有權人或代表人IDN/BAN<br>
	 * Key,左靠，股票持有人身份證或統一證號
	 *
	 * @param ownerId 擔保品所有權人或代表人IDN/BAN
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * 發行機構 BAN<br>
	 * Key,
	 * 
	 * @return String
	 */
	public String getCompanyId() {
		return this.companyId == null ? "" : this.companyId;
	}

	/**
	 * 發行機構 BAN<br>
	 * Key,
	 *
	 * @param companyId 發行機構 BAN
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataYM, clActNo, ownerId, companyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicB091Id jcicB091Id = (JcicB091Id) obj;
		return dataYM == jcicB091Id.dataYM && clActNo.equals(jcicB091Id.clActNo) && ownerId.equals(jcicB091Id.ownerId) && companyId.equals(jcicB091Id.companyId);
	}

	@Override
	public String toString() {
		return "JcicB091Id [dataYM=" + dataYM + ", clActNo=" + clActNo + ", ownerId=" + ownerId + ", companyId=" + companyId + "]";
	}
}
