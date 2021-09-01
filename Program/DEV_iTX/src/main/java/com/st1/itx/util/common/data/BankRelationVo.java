package com.st1.itx.util.common.data;

import javax.persistence.Entity;

@Entity
public class BankRelationVo {
	/**
	 * LAW001 = Y 金控法第44條(1) <BR>
	 * LAW002 = Y 金控法第44條(列項)(2) <BR>
	 * LAW003 = Y 金控法第45條(3) <BR>
	 * LAW005 = Y 保險法(放款)(5) <BR>
	 * LAW008 = Y 準利害關係人(8) <BR>
	 * IsSalary = true 15日薪的員工(9) <BR>
	 * IsLimit     = true 授信限制對象(1,5,8,9) <BR> 
	 * IsRelated   = true 利害關係人(1,5) <BR>       
	 * IsLnrelNear = true 準利害關係人(8) <BR>       
	 * IsFinancial = true 是否為金控疑似準利害關係人   
	 * DataDate   資料日期
	 */
	private String LAW001 = " ";
	private String LAW002 = " ";
	private String LAW003 = " ";
	private String LAW005 = " ";
	private String LAW008 = " ";
	private String IsSalary = " ";
	private String IsLimit = " ";
	private String IsRelated = " ";
	private String IsLnrelNear = " ";
	private String IsFinancial = " ";
	private String DataDate = "";

	@Override
	public String toString() {
		return "BankRelationVo [LAW001=" + LAW001 + ", LAW002=" + LAW002 + ", LAW003=" + LAW003 + ", LAW005=" + LAW005
				+ ", LAW008=" + LAW008 + ", IsSalary=" + IsSalary + ", IsRelated=" + IsRelated + ", IsLnrelNear="
				+ IsLnrelNear + ", IsLimit=" + IsLimit + ", IsFinancial=" + IsFinancial + "]";
	}

	/**
	 * @return the lAW001
	 */
	public String getLAW001() {
		return LAW001;
	}

	/**
	 * @param lAW001 the lAW001 to set
	 */
	public void setLAW001(String lAW001) {
		LAW001 = lAW001;
	}

	/**
	 * @return the lAW002
	 */
	public String getLAW002() {
		return LAW002;
	}

	/**
	 * @param lAW002 the lAW002 to set
	 */
	public void setLAW002(String lAW002) {
		LAW002 = lAW002;
	}

	/**
	 * @return the lAW003
	 */
	public String getLAW003() {
		return LAW003;
	}

	/**
	 * @param lAW003 the lAW003 to set
	 */
	public void setLAW003(String lAW003) {
		LAW003 = lAW003;
	}

	/**
	 * @return the lAW005
	 */
	public String getLAW005() {
		return LAW005;
	}

	/**
	 * @param lAW005 the lAW005 to set
	 */
	public void setLAW005(String lAW005) {
		LAW005 = lAW005;
	}

	/**
	 * @return the lAW008
	 */
	public String getLAW008() {
		return LAW008;
	}

	/**
	 * @param lAW008 the lAW008 to set
	 */
	public void setLAW008(String lAW008) {
		LAW008 = lAW008;
	}

	/**
	 * @return the isSalary
	 */
	public String getIsSalary() {
		return IsSalary;
	}

	/**
	 * @param isSalary the isSalary to set
	 */
	public void setIsSalary(String isSalary) {
		IsSalary = isSalary;
	}

	/**
	 * @return the isLimit
	 */
	public String getIsLimit() {
		return IsLimit;
	}

	/**
	 * @param isLimit the isLimit to set
	 */
	public void setIsLimit(String isLimit) {
		IsLimit = isLimit;
	}

	/**
	 * @return the isRelated
	 */
	public String getIsRelated() {
		return IsRelated;
	}

	/**
	 * @param isRelated the isRelated to set
	 */
	public void setIsRelated(String isRelated) {
		IsRelated = isRelated;
	}

	/**
	 * @return the isLnrelNear
	 */
	public String getIsLnrelNear() {
		return IsLnrelNear;
	}

	/**
	 * @param isLnrelNear the isLnrelNear to set
	 */
	public void setIsLnrelNear(String isLnrelNear) {
		IsLnrelNear = isLnrelNear;
	}

	/**
	 * @return the isFinancial
	 */
	public String getIsFinancial() {
		return IsFinancial;
	}

	/**
	 * @param isFinancial the isFinancial to set
	 */
	public void setIsFinancial(String isFinancial) {
		IsFinancial = isFinancial;
	}

	/**
	 * @return the dataDate
	 */
	public String getDataDate() {
		return DataDate;
	}

	/**
	 * @param dataDate the dataDate to set
	 */
	public void setDataDate(String dataDate) {
		DataDate = dataDate;
	}


}