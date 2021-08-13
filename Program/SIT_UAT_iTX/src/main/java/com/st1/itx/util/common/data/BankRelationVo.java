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
	 * IsRelated = true 利害關係人(1,5) <BR>
	 * IsLnrelNear = true 準利害關係人(8) <BR>
	 * IsLimit = true 授信限制對象(1,5,8,9) <BR>
	 */
	private String LAW001 = " ";
	private String LAW002 = " ";
	private String LAW003 = " ";
	private String LAW005 = " ";
	private String LAW008 = " ";
	private String IsSalary = " ";
	private String IsRelated = " ";
	private String IsLnrelNear = " ";
	private String IsLimit = " ";

	@Override
	public String toString() {
		return "BankRelationVo [LAW001=" + LAW001 + ", LAW002=" + LAW002 + ", LAW003=" + LAW003 + ", LAW005=" + LAW005 + ", LAW008=" + LAW008 + ", IsSalary=" + IsSalary + ", IsRelated=" + IsRelated
				+ ", IsLnrelNear=" + IsLnrelNear + ", IsLimit=" + IsLimit + "]";
	}

	public String getLAW001() {
		return LAW001;
	}

	public void setLAW001(String lAW001) {
		LAW001 = lAW001;
	}

	public String getLAW002() {
		return LAW002;
	}

	public void setLAW002(String lAW002) {
		LAW002 = lAW002;
	}

	public String getLAW003() {
		return LAW003;
	}

	public void setLAW003(String lAW003) {
		LAW003 = lAW003;
	}

	public String getLAW005() {
		return LAW005;
	}

	public void setLAW005(String lAW005) {
		LAW005 = lAW005;
	}

	public String getLAW008() {
		return LAW008;
	}

	public void setLAW008(String lAW008) {
		LAW008 = lAW008;
	}

	public String getIsSalary() {
		return IsSalary;
	}

	public void setIsSalary(String isSalary) {
		IsSalary = isSalary;
	}

	public String getIsRelated() {
		return IsRelated;
	}

	public void setIsRelated(String isRelated) {
		IsRelated = isRelated;
	}

	public String getIsLnrelNear() {
		return IsLnrelNear;
	}

	public void setIsLnrelNear(String isLnrelNear) {
		IsLnrelNear = isLnrelNear;
	}

	public String getIsLimit() {
		return IsLimit;
	}

	public void setIsLimit(String isLimit) {
		IsLimit = isLimit;
	}

}