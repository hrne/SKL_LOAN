package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcCheque 應收票據資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcChequeId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2009480186837466387L;

// 資料年月
	@Column(name = "`DataDate`")
	private int dataDate = 0;

	// 入帳單位
	@Column(name = "`UnitCode`", length = 6)
	private String unitCode = " ";

	// 支票流水號
	@Column(name = "`ChequeSeq`")
	private int chequeSeq = 0;

	public AcChequeId() {
	}

	public AcChequeId(int dataDate, String unitCode, int chequeSeq) {
		this.dataDate = dataDate;
		this.unitCode = unitCode;
		this.chequeSeq = chequeSeq;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getDataDate() {
		return StaticTool.bcToRoc(this.dataDate);
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param dataDate 資料年月
	 * @throws LogicException when Date Is Warn
	 */
	public void setDataDate(int dataDate) throws LogicException {
		this.dataDate = StaticTool.rocToBc(dataDate);
	}

	/**
	 * 入帳單位<br>
	 * 
	 * @return String
	 */
	public String getUnitCode() {
		return this.unitCode == null ? "" : this.unitCode;
	}

	/**
	 * 入帳單位<br>
	 * 
	 *
	 * @param unitCode 入帳單位
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	/**
	 * 支票流水號<br>
	 * 
	 * @return Integer
	 */
	public int getChequeSeq() {
		return this.chequeSeq;
	}

	/**
	 * 支票流水號<br>
	 * 
	 *
	 * @param chequeSeq 支票流水號
	 */
	public void setChequeSeq(int chequeSeq) {
		this.chequeSeq = chequeSeq;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDate, unitCode, chequeSeq);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		AcChequeId acChequeId = (AcChequeId) obj;
		return dataDate == acChequeId.dataDate && unitCode.equals(acChequeId.unitCode) && chequeSeq == acChequeId.chequeSeq;
	}

	@Override
	public String toString() {
		return "AcChequeId [dataDate=" + dataDate + ", unitCode=" + unitCode + ", chequeSeq=" + chequeSeq + "]";
	}
}
