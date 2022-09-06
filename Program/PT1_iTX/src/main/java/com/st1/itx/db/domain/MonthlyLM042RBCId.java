package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM042RBC LM042RBC會計報表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM042RBCId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8934106641639033698L;

// 資料年月
	@Column(name = "`YearMonth`")
	private int yearMonth = 0;

	// 放款種類
	/* 1：一般放款2：專案放款 */
	@Column(name = "`LoanType`", length = 1)
	private String loanType = " ";

	// 放款項目
	/*
	 * A：銀行保證放款B：動產擔保放款C：不動產擔保放款D：有價證券質押放款E：非控制與從屬關係F：具控制與從屬關係Z：政策性專案運用公共及社會福利事業投資N：
	 * 無
	 */
	@Column(name = "`LoanItem`", length = 1)
	private String loanItem = " ";

	// 對象關係人
	/* N：非關係人(非授信限制對象)Y：關係人(授信限制對象) */
	@Column(name = "`RelatedCode`", length = 1)
	private String relatedCode = " ";

	public MonthlyLM042RBCId() {
	}

	public MonthlyLM042RBCId(int yearMonth, String loanType, String loanItem, String relatedCode) {
		this.yearMonth = yearMonth;
		this.loanType = loanType;
		this.loanItem = loanItem;
		this.relatedCode = relatedCode;
	}

	/**
	 * 資料年月<br>
	 * 
	 * @return Integer
	 */
	public int getYearMonth() {
		return this.yearMonth;
	}

	/**
	 * 資料年月<br>
	 * 
	 *
	 * @param yearMonth 資料年月
	 */
	public void setYearMonth(int yearMonth) {
		this.yearMonth = yearMonth;
	}

	/**
	 * 放款種類<br>
	 * 1：一般放款 2：專案放款
	 * 
	 * @return String
	 */
	public String getLoanType() {
		return this.loanType == null ? "" : this.loanType;
	}

	/**
	 * 放款種類<br>
	 * 1：一般放款 2：專案放款
	 *
	 * @param loanType 放款種類
	 */
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	/**
	 * 放款項目<br>
	 * A：銀行保證放款 B：動產擔保放款 C：不動產擔保放款 D：有價證券質押放款 E：非控制與從屬關係 F：具控制與從屬關係 Z：政策性專案運用公共
	 * 及社會福利事業投資 N：無
	 * 
	 * @return String
	 */
	public String getLoanItem() {
		return this.loanItem == null ? "" : this.loanItem;
	}

	/**
	 * 放款項目<br>
	 * A：銀行保證放款 B：動產擔保放款 C：不動產擔保放款 D：有價證券質押放款 E：非控制與從屬關係 F：具控制與從屬關係 Z：政策性專案運用公共
	 * 及社會福利事業投資 N：無
	 *
	 * @param loanItem 放款項目
	 */
	public void setLoanItem(String loanItem) {
		this.loanItem = loanItem;
	}

	/**
	 * 對象關係人<br>
	 * N：非關係人(非授信限制對象) Y：關係人(授信限制對象)
	 * 
	 * @return String
	 */
	public String getRelatedCode() {
		return this.relatedCode == null ? "" : this.relatedCode;
	}

	/**
	 * 對象關係人<br>
	 * N：非關係人(非授信限制對象) Y：關係人(授信限制對象)
	 *
	 * @param relatedCode 對象關係人
	 */
	public void setRelatedCode(String relatedCode) {
		this.relatedCode = relatedCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(yearMonth, loanType, loanItem, relatedCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MonthlyLM042RBCId monthlyLM042RBCId = (MonthlyLM042RBCId) obj;
		return yearMonth == monthlyLM042RBCId.yearMonth && loanType.equals(monthlyLM042RBCId.loanType) && loanItem.equals(monthlyLM042RBCId.loanItem)
				&& relatedCode.equals(monthlyLM042RBCId.relatedCode);
	}

	@Override
	public String toString() {
		return "MonthlyLM042RBCId [yearMonth=" + yearMonth + ", loanType=" + loanType + ", loanItem=" + loanItem + ", relatedCode=" + relatedCode + "]";
	}
}
