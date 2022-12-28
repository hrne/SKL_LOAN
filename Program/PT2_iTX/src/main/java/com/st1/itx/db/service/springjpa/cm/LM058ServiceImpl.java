package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM058ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param iYearMonth 西元年月
	 * @throws Exception
	 * @return 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int iYearMonth) throws Exception {

		// 年
		int iYear = iYearMonth / 100;

		// 月
		int iMonth = iYearMonth % 100;
		int ilYearMonth = 0;
		if (iMonth == 1) {
			ilYearMonth = (iYear - 1) * 100 + 12;
		} else {
			ilYearMonth = iYearMonth - 1;
		}

		this.info("lM058.findAll YYMM=" + iYearMonth);
		String sql = " ";
		sql += "	 WITH \"allMonthMaxLoan\" AS ("; // -- 整年度最高月份金額
		sql += "	 		SELECT \"CustNo\"";
		sql += "	 			  ,SUM(\"LoanBalance\") AS \"TotalLoanBal\"";
		sql += "	 		FROM \"DailyLoanBal\"";
		sql += "	 		WHERE TRUNC(\"DataDate\" / 100 ) = :lyymm";
		sql += "	 		  AND \"LoanBalance\" > 0 ";
		sql += "	 		GROUP BY \"CustNo\"";
		sql += "	 ),\"mainData\" AS (";
		sql += "	 	SELECT * FROM (";
		sql += "	 		SELECT \"CustNo\"";
		sql += "	 			  ,SUM(\"LoanBalance\") AS \"TotalLoanBal\"";
		sql += "	 		FROM \"DailyLoanBal\"";
		sql += "	 		WHERE TRUNC(\"DataDate\" / 100 ) = :yymm";
		sql += "	 		  AND \"LoanBalance\" > 0 ";
		sql += "	 		GROUP BY \"CustNo\"";
		sql += "	 		ORDER BY SUM(\"LoanBalance\") DESC";
		sql += "	 	)M";
		sql += "	 	WHERE ROWNUM <= 20 ";
		sql += "	 )";
		sql += "	 SELECT M.\"CustNo\" AS F0";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F1";
		sql += "			,C.\"CustId\" AS F2";
		sql += "			,CASE";
		sql += "			   WHEN R.\"RptId\" = '3' THEN 'F'"; // --一般客戶
		sql += "			   WHEN R.\"RptId\" IS null THEN 'F'";
		sql += "			ELSE '*' END AS F3";
		sql += "			,'2' AS F4";
		sql += "			,'新光人壽' AS F5";
		sql += "			,'2' AS F6";
		sql += "			,CASE ";
		sql += "			   WHEN M2.\"TotalLoanBal\" IS NULL THEN M.\"TotalLoanBal\" ";
		sql += "			   WHEN M.\"TotalLoanBal\" >= M2.\"TotalLoanBal\" THEN M.\"TotalLoanBal\" ";
		sql += "			 ELSE M2.\"TotalLoanBal\" END AS F7";
		sql += "			,M.\"TotalLoanBal\" AS F8";
		sql += "			,M.\"TotalLoanBal\" - NVL(M2.\"TotalLoanBal\",M.\"TotalLoanBal\") AS F9";
		sql += "			,M.\"TotalLoanBal\" AS F10";
		sql += "			,ROW_NUMBER () OVER (ORDER BY M.\"TotalLoanBal\" DESC) AS F11";
		sql += "	  FROM \"mainData\" M ";
		sql += "	  LEFT JOIN \"allMonthMaxLoan\" M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += " 	  LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "             	  FROM \"RptRelationSelf\" ";
		sql += "             	  WHERE \"LAW005\" = '1' ";
		sql += "             	  UNION ";
		sql += "             	  SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "             	  FROM \"RptRelationFamily\" ";
		sql += "             	  WHERE \"LAW005\" = '1' ";
		sql += "             	  UNION ";
		sql += "             	  SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "             	  FROM \"RptRelationCompany\" ";
		sql += "             	  WHERE \"LAW005\" = '1' ";
		sql += "           	    ) R ON R.\"RptId\" = C.\"CustId\" ";
		sql += "	  ORDER BY M.\"TotalLoanBal\" DESC";
//		String sql = " ";
//		sql += "	 SELECT D.\"CustNo\" AS F0";
//		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F1";
//		sql += "			,C.\"CustId\" AS F2";
//		sql += "			,CASE";
//		sql += "			   WHEN R.\"RptId\" = '3' THEN 'F'"; //一般客戶
//		sql += "			   WHEN R.\"RptId\" IS null THEN 'F'";
//		sql += "			ELSE '*' END AS F3";
//		sql += "			,'2' AS F4";
//		sql += "			,'新光人壽' AS F5";
//		sql += "			,'2' AS F6";
//		sql += "			,D.\"MaxLoanBal\" AS F7";
//		sql += "			,D.\"LoanBal\" AS F8";
//		sql += "			,D.\"LoanBal\" - D.\"MaxLoanBal\" AS F9";
//		sql += "			,D.\"LoanBal\" AS F10";
//		sql += "			,D.\"SEQ\" AS F11";
//		sql += "	  FROM(SELECT D.\"CustNo\" \"CustNo\"";
//		sql += "				 ,D.\"DataDate\" \"DataDate\"";
//		sql += "				 ,D.\"MonthEndYm\" \"MonthEndYm\"";
//		sql += "				 ,D.\"MaxLoanBal\" \"MaxLoanBal\"";
//		sql += "				 ,D.\"LoanBal\" \"LoanBal\"";
//		sql += "				 ,ROW_NUMBER() OVER(ORDER BY D.\"LoanBal\" DESC) AS SEQ";
//		sql += "		   FROM(SELECT D.\"CustNo\"";
//		sql += "					  ,D.\"DataDate\"";
//		sql += "					  ,D.\"MonthEndYm\"";
//		sql += "					  ,NVL(ML.\"LoanBalance\",0) AS \"MaxLoanBal\"";
//		sql += "					  ,SUM(D.\"LoanBalance\") \"LoanBal\"";
//		sql += "				FROM \"DailyLoanBal\" D";
//		sql += "	  			LEFT JOIN(SELECT \"CustNo\"";
//		sql += "							    ,MAX(\"LoanBalance\") \"LoanBalance\"";
//		sql += "						  FROM(SELECT \"CustNo\"";
//		sql += "									 ,TRUNC(\"DataDate\" / 100) \"YearMonth\"";
//		sql += "									 ,SUM(\"LoanBalance\") \"LoanBalance\"";
//		sql += "							   FROM \"DailyLoanBal\"";
//		sql += "							   WHERE TRUNC(\"DataDate\" / 10000) = :year";
//		sql += "						  	   GROUP BY \"CustNo\",TRUNC(\"DataDate\" / 100) )";
//		sql += "						  GROUP BY \"CustNo\") ML";
//		sql += "	 			ON ML.\"CustNo\" = D.\"CustNo\"";
//		sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :yymm";
//		sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\",ML.\"LoanBalance\")D )D";
//		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
//		sql += " 	  LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
//		sql += "             	  FROM \"RptRelationSelf\" ";
//		sql += "             	  WHERE \"LAW005\" = '1' ";
//		sql += "             	  UNION ";
//		sql += "             	  SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
//		sql += "             	  FROM \"RptRelationFamily\" ";
//		sql += "             	  WHERE \"LAW005\" = '1' ";
//		sql += "             	  UNION ";
//		sql += "             	  SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
//		sql += "             	  FROM \"RptRelationCompany\" ";
//		sql += "             	  WHERE \"LAW005\" = '1' ";
//		sql += "           	    ) R ON R.\"RptId\" = C.\"CustId\" ";
//		sql += " 	  WHERE D.\"SEQ\" <= 20";
//		sql += "	  ORDER BY D.\"SEQ\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lyymm", ilYearMonth);
		query.setParameter("yymm", iYearMonth);

		return this.convertToMap(query);
	}

}