package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ051 延期繳款（喘息期）資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ051Id implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7058695346645230488L;

// 報送單位代號
	/* 三位文數字 */
	@Column(name = "`SubmitKey`", length = 3)
	private String submitKey = " ";

	// 債務人IDN
	@Column(name = "`CustId`", length = 10)
	private String custId = " ";

	// 協商申請日
	@Column(name = "`RcDate`")
	private int rcDate = 0;

	// 延期繳款年月
	@Column(name = "`DelayYM`")
	private int delayYM = 0;

	public JcicZ051Id() {
	}

	public JcicZ051Id(String submitKey, String custId, int rcDate, int delayYM) {
		this.submitKey = submitKey;
		this.custId = custId;
		this.rcDate = rcDate;
		this.delayYM = delayYM;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 * 
	 * @return String
	 */
	public String getSubmitKey() {
		return this.submitKey == null ? "" : this.submitKey;
	}

	/**
	 * 報送單位代號<br>
	 * 三位文數字
	 *
	 * @param submitKey 報送單位代號
	 */
	public void setSubmitKey(String submitKey) {
		this.submitKey = submitKey;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 債務人IDN<br>
	 * 
	 *
	 * @param custId 債務人IDN
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 協商申請日<br>
	 * 
	 * @return Integer
	 */
	public int getRcDate() {
		return StaticTool.bcToRoc(this.rcDate);
	}

	/**
	 * 協商申請日<br>
	 * 
	 *
	 * @param rcDate 協商申請日
	 * @throws LogicException when Date Is Warn
	 */
	public void setRcDate(int rcDate) throws LogicException {
		this.rcDate = StaticTool.rocToBc(rcDate);
	}

	/**
	 * 延期繳款年月<br>
	 * 
	 * @return Integer
	 */
	public int getDelayYM() {
		return this.delayYM;
	}

	/**
	 * 延期繳款年月<br>
	 * 
	 *
	 * @param delayYM 延期繳款年月
	 */
	public void setDelayYM(int delayYM) {
		this.delayYM = delayYM;
	}

	@Override
	public int hashCode() {
		return Objects.hash(submitKey, custId, rcDate, delayYM);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicZ051Id jcicZ051Id = (JcicZ051Id) obj;
		return submitKey.equals(jcicZ051Id.submitKey) && custId.equals(jcicZ051Id.custId) && rcDate == jcicZ051Id.rcDate && delayYM == jcicZ051Id.delayYM;
	}

	@Override
	public String toString() {
		return "JcicZ051Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", delayYM=" + delayYM + "]";
	}
}
