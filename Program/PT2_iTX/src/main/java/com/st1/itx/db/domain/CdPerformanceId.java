package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdPerformance 業績件數及金額核算標準設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdPerformanceId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8321907441298970831L;

// 工作年月
	/* 2021/4/12 新增 */
	@Column(name = "`WorkMonth`")
	private int workMonth = 0;

	// 計件代碼
	/*
	 * ~~~(綁約2年以下或未綁約)~~~A: 新貸件B: 新貸件(同押品,數額度之額度一以外)C: 原額度內—動支件D:
	 * 新增額度—新貸件(指有增加設定抵押權者)E: 展期件~~~(有綁約2年(含)以上)~~~1: 新貸件2: 新貸件(同押品,數額度之額度一以外)3:
	 * 原額度內—動支件4: 新增額度—新貸件(指有增加設定抵押權者)5: 展期件~~~(無關綁約)~~~6:
	 * 原額度內—6個月內動支件(還款後6個月內再動支者)7: 服務件8: 特殊件9: 固特利契轉
	 */
	@Column(name = "`PieceCode`", length = 1)
	private String pieceCode = " ";

	public CdPerformanceId() {
	}

	public CdPerformanceId(int workMonth, String pieceCode) {
		this.workMonth = workMonth;
		this.pieceCode = pieceCode;
	}

	/**
	 * 工作年月<br>
	 * 2021/4/12 新增
	 * 
	 * @return Integer
	 */
	public int getWorkMonth() {
		return this.workMonth;
	}

	/**
	 * 工作年月<br>
	 * 2021/4/12 新增
	 *
	 * @param workMonth 工作年月
	 */
	public void setWorkMonth(int workMonth) {
		this.workMonth = workMonth;
	}

	/**
	 * 計件代碼<br>
	 * ~~~(綁約2年以下或未綁約)~~~ A: 新貸件 B: 新貸件(同押品,數額度之額度一以外) C: 原額度內—動支件 D:
	 * 新增額度—新貸件(指有增加設定抵押權者) E: 展期件 ~~~(有綁約2年(含)以上)~~~ 1: 新貸件 2: 新貸件(同押品,數額度之額度一以外)
	 * 3: 原額度內—動支件 4: 新增額度—新貸件(指有增加設定抵押權者) 5: 展期件 ~~~(無關綁約)~~~ 6:
	 * 原額度內—6個月內動支件(還款後6個月內再動支者) 7: 服務件 8: 特殊件 9: 固特利契轉
	 * 
	 * @return String
	 */
	public String getPieceCode() {
		return this.pieceCode == null ? "" : this.pieceCode;
	}

	/**
	 * 計件代碼<br>
	 * ~~~(綁約2年以下或未綁約)~~~ A: 新貸件 B: 新貸件(同押品,數額度之額度一以外) C: 原額度內—動支件 D:
	 * 新增額度—新貸件(指有增加設定抵押權者) E: 展期件 ~~~(有綁約2年(含)以上)~~~ 1: 新貸件 2: 新貸件(同押品,數額度之額度一以外)
	 * 3: 原額度內—動支件 4: 新增額度—新貸件(指有增加設定抵押權者) 5: 展期件 ~~~(無關綁約)~~~ 6:
	 * 原額度內—6個月內動支件(還款後6個月內再動支者) 7: 服務件 8: 特殊件 9: 固特利契轉
	 *
	 * @param pieceCode 計件代碼
	 */
	public void setPieceCode(String pieceCode) {
		this.pieceCode = pieceCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(workMonth, pieceCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdPerformanceId cdPerformanceId = (CdPerformanceId) obj;
		return workMonth == cdPerformanceId.workMonth && pieceCode.equals(cdPerformanceId.pieceCode);
	}

	@Override
	public String toString() {
		return "CdPerformanceId [workMonth=" + workMonth + ", pieceCode=" + pieceCode + "]";
	}
}
