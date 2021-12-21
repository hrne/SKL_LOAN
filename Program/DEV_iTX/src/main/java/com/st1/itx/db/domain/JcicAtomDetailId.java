package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicAtomDetail 債務匯入資料功能明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicAtomDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3494840409325768705L;

// 功能代碼
	@Column(name = "`FunctionCode`", length = 6)
	private String functionCode = " ";

	// 順序
	@Column(name = "`DataOrder`")
	private int dataOrder = 0;

	public JcicAtomDetailId() {
	}

	public JcicAtomDetailId(String functionCode, int dataOrder) {
		this.functionCode = functionCode;
		this.dataOrder = dataOrder;
	}

	/**
	 * 功能代碼<br>
	 * 
	 * @return String
	 */
	public String getFunctionCode() {
		return this.functionCode == null ? "" : this.functionCode;
	}

	/**
	 * 功能代碼<br>
	 * 
	 *
	 * @param functionCode 功能代碼
	 */
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	/**
	 * 順序<br>
	 * 
	 * @return Integer
	 */
	public int getDataOrder() {
		return this.dataOrder;
	}

	/**
	 * 順序<br>
	 * 
	 *
	 * @param dataOrder 順序
	 */
	public void setDataOrder(int dataOrder) {
		this.dataOrder = dataOrder;
	}

	@Override
	public int hashCode() {
		return Objects.hash(functionCode, dataOrder);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JcicAtomDetailId jcicAtomDetailId = (JcicAtomDetailId) obj;
		return functionCode.equals(jcicAtomDetailId.functionCode) && dataOrder == jcicAtomDetailId.dataOrder;
	}

	@Override
	public String toString() {
		return "JcicAtomDetailId [functionCode=" + functionCode + ", dataOrder=" + dataOrder + "]";
	}
}
