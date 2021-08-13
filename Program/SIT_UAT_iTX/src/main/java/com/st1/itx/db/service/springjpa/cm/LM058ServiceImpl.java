package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(LM058ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYEAR = iENTDY.substring(0, 4);
		String iMM = iENTDY.substring(4, 6);
		String iYYMM = iENTDY.substring(0, 6);
		String iLYYMM = "";
		if (iMM.equals("1")) {
			iLYYMM = String.valueOf(Integer.valueOf(iYEAR) - 1) + "12";
		} else {
			iLYYMM = iYEAR + String.format("%02d", Integer.valueOf(iMM) - 1);
		}

		logger.info("lM058.findAll YYMM=" + iYYMM + ",LYYMM=" + iLYYMM);

		String sql = "SELECT D.\"CustNo\" AS F0";
		sql += "			,C.\"CustName\" AS F1";
		sql += "			,C.\"CustId\" AS F2";
		sql += "			,CASE";
		sql += "			   WHEN R.\"RelsCode\" = '99' THEN 'F'";
		sql += "			   WHEN R.\"RelsCode\" IS null THEN 'F'";
		sql += "			ELSE '*' END AS F3";
		sql += "			,'2' AS F4";
		sql += "			,'新光人壽' AS F5";
		sql += "			,'2' AS F6";
		sql += "			,D.\"maxLoanBal\" AS F7";
		sql += "			,D.\"LoanBal\" AS F8";
		sql += "			,D.\"LoanBal\" - D.\"maxLoanBal\" AS F9";
		sql += "			,D.\"LoanBal\" AS F10";
		sql += "			,D.\"SEQ\" AS F11";
		sql += "	  FROM(SELECT D.\"CustNo\" \"CustNo\"";
		sql += "				 ,D.\"DataDate\" \"DataDate\"";
		sql += "				 ,D.\"MonthEndYm\" \"MonthEndYm\"";
		sql += "				 ,D.\"maxLoanBal\" \"maxLoanBal\"";
		sql += "				 ,D.\"LoanBal\" \"LoanBal\"";
		sql += "				 ,ROW_NUMBER() OVER(ORDER BY D.\"LoanBal\" DESC) AS SEQ";
		sql += "		   FROM(SELECT D.\"CustNo\"";
		sql += "					  ,D.\"DataDate\"";
		sql += "					  ,D.\"MonthEndYm\"";
		sql += "					  ,DDD.\"LoanBalance\" \"maxLoanBal\"";
		sql += "					  ,SUM(D.\"LoanBalance\") \"LoanBal\"";
		sql += "				FROM \"DailyLoanBal\" D";
		sql += "	  			LEFT JOIN(SELECT \"CustNo\"";
		sql += "							    ,MAX(\"DataDate\") \"DataDate\"";
		sql += "						  FROM \"DailyLoanBal\"";
		sql += "						  GROUP BY \"CustNo\") DD";
		sql += "	 			ON DD.\"CustNo\" = D.\"CustNo\" AND DD.\"DataDate\" = D.\"DataDate\"";
		sql += "	  			LEFT JOIN(SELECT \"CustNo\"";
		sql += "							    ,MAX(\"LoanBalance\") \"LoanBalance\"";
		sql += "						  FROM(SELECT \"CustNo\"";
		sql += "									 ,TRUNC(\"DataDate\" / 100)";
		sql += "									 ,SUM(\"LoanBalance\") \"LoanBalance\"";
		sql += "							   FROM \"DailyLoanBal\"";
		sql += "							   WHERE TRUNC(\"DataDate\" / 100) = :yymm";
		sql += "						  	   GROUP BY \"CustNo\",TRUNC(\"DataDate\" / 100) )";
		sql += "						  GROUP BY \"CustNo\") DDD";
		sql += "	 			ON DDD.\"CustNo\" = D.\"CustNo\"";
		sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :yymm";
		sql += "				AND DD.\"DataDate\" IS NOT NULL";
		sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\",DDD.\"LoanBalance\")D )D";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "	  LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
		sql += " 	  WHERE D.\"SEQ\" <= 20";
		sql += "	  ORDER BY D.\"SEQ\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);
//		query.setParameter("lyymm", iLYYMM);

		return this.convertToMap(query.getResultList());
	}

//	String sql = "SELECT D.\"CustNo\" AS F0";
//	sql += "			,C.\"CustName\" AS F1";
//	sql += "			,C.\"CustId\" AS F2";
//	sql += "			,CASE";
//	sql += "			   WHEN R.\"RelsCode\" = '99' THEN 'F'";
//	sql += "			   WHEN R.\"RelsCode\" IS null THEN 'F'";
//	sql += "			ELSE '*' END AS F3";
//	sql += "			,'2' AS F4";
//	sql += "			,'新光人壽' AS F5";
//	sql += "			,'2' AS F6";
//	sql += "			,D.\"LoanBal\" AS F7";
//	sql += "			,D.\"MLoanBal\" AS F8";
//	sql += "			,D.\"LoanBal\" - D.\"LLoanBal\" AS F9";
//	sql += "			,D.\"LLoanBal\" AS F10";
//	sql += "			,D.\"SEQ\" AS F11";
//	sql += "	  FROM(SELECT D.\"CustNo\"";
//	sql += "				 ,MAX(D.\"LoanBal\") \"LoanBal\"";
//	sql += "				 ,MAX(D.\"LLoanBal\") \"LLoanBal\"";
//	sql += "				 ,SUM(DECODE(D.\"MonthEndYm\",:yymm, D.\"LoanBal\", 0)) \"MLoanBal\"";
//	sql += "				 ,ROW_NUMBER() OVER(ORDER BY MAX(D.\"LoanBal\") DESC) AS SEQ";
//	sql += "		   FROM(SELECT D.\"CustNo\"";
//	sql += "					  ,D.\"DataDate\"";
//	sql += "					  ,D.\"MonthEndYm\"";
//	sql += "					  ,SUM(D.\"LoanBalance\") \"LoanBal\"";
//	sql += "					  ,0 \"LLoanBal\"";
//	sql += "				FROM \"DailyLoanBal\" D";
//	sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :yymm";
//	sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\"";
//	sql += "				UNION ALL";
//	sql += "				SELECT D.\"CustNo\"";
//	sql += "					  ,D.\"DataDate\"";
//	sql += "					  ,D.\"MonthEndYm\"";
//	sql += "					  ,0 \"LoanBal\"";
//	sql += "					  ,SUM(D.\"LoanBalance\") \"LLoanBal\"";
//	sql += "				FROM \"DailyLoanBal\" D";
//	sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :lyymm";
//	sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\") D";
//	sql += "		   GROUP BY D.\"CustNo\") D";
//	sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
//	sql += "	  LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
//	sql += " 	  WHERE D.\"SEQ\" <= 20";
//	sql += "	  ORDER BY D.\"SEQ\"";

}