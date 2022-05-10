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
public class L9731ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	*/
	public List<Map<String, String>> findSheet1(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("l9731.findLA$W30P ");

		String sql = "";
		sql += " SELECT M.\"CustNo\"                              AS F0 ";
		sql += "       ,M.\"FacmNo\"                              AS F1 ";
		sql += "       ,M.\"BormNo\"                              AS F2 ";
		sql += "       ,DECODE(M.\"AcSubBookCode\",'00A',' ','A') AS F3 ";
		sql += "       ,C.\"CustId\"                              AS F4 ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0)        AS F5 ";
		sql += "       ,M.\"AcctCode\"                            AS F6 ";
		sql += "       ,L.\"DrawdownDate\"                        AS F7 ";
		sql += "       ,L.\"MaturityDate\"                        AS F8 ";
		sql += "       ,M.\"StoreRate\"                           AS F9 ";
		sql += "       ,L.\"PayIntFreq\"                          AS F10 ";
		sql += "       ,L.\"PrevPayIntDate\"                      AS F11 ";
		sql += "       ,NVL(O.\"OvduDate\", 0)                    AS F12 ";
		sql += "       ,L.\"UsageCode\"                           AS F13 ";
		sql += "       ,NVL(F.\"LineAmt\",0)                      AS F14 ";
		sql += "       ,NVL(L.\"DrawdownAmt\",0)                  AS F15 ";
		sql += "       ,M.\"LoanBalance\"                         AS F16 ";
		sql += "       ,M.\"ProdNo\" 	                          AS F17 ";
		sql += "       ,F.\"FirstDrawdownDate\"                   AS F18 ";
		sql += "       ,CASE  ";
		sql += "       	  WHEN F.\"FirstDrawdownDate\" >= 20100101 ";
		sql += "           AND (MF.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]'))";
		sql += "       	  THEN '**'";
		sql += "       	  WHEN F.\"FirstDrawdownDate\" < 20100101 ";
		sql += "           AND (MF.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]'))";
		sql += "       	  THEN '*'";
		sql += "       	  ELSE ' ' END	AS F19";
		sql += "       ,CASE  ";
		sql += "       	  WHEN (MF.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]'))";
		sql += "       	  THEN 'Z'";
		sql += "       	  WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "       	  THEN 'C'";
		sql += "       	  WHEN M.\"ClCode1\" IN (3) ";
		sql += "       	  THEN 'D'";
		sql += "       	  ELSE 'C' END  AS F20";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND L.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND L.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = M.\"CustNo\" ";
		sql += "                            AND O.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "                            AND O.\"BormNo\" = M.\"BormNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"CustNo\" = F.\"CustNo\" ";
		sql += "                               AND MF.\"FacmNo\" = F.\"FacmNo\" ";
		sql += "                               AND MF.\"YearMonth\" = M.\"YearMonth\" ";
		sql += " WHERE M.\"YearMonth\" = :yymm ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += " ORDER BY F0,F1,F2 ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findSheet2(TitaVo titaVo, int yearMonth) throws Exception {
		// 年
		int iYear = yearMonth / 100;

		this.info("L9731.findSheet2 YYMM=" + yearMonth);

		String sql = " ";
		sql += "	 WITH \"allMonthMaxLoan\" AS ("; //
		sql += "	 	SELECT M.\"CustNo\" AS \"CustNo\"";
		sql += "	 		  ,MAX(\"MaxLoanBalance\") AS \"MaxLoanBal\"";
		sql += "	 	FROM(";
		sql += "	 		SELECT \"CustNo\"";
		sql += "	 			  ,\"YearMonth\"";
		sql += "	 			  ,SUM(\"LoanBalance\") AS \"MaxLoanBalance\"";
		sql += "	 		FROM \"MonthlyLoanBal\"";
		sql += "	 		WHERE TRUNC(\"YearMonth\" / 100 ) = :year";
		sql += "	 		  AND \"LoanBalance\" > 0 ";
		sql += "	 		GROUP BY \"CustNo\"";
		sql += "	 				,\"YearMonth\"";
		sql += "	 	)M";
		sql += "	 	GROUP BY M.\"CustNo\"";
		sql += "	 ),\"mainData\" AS (";
		sql += "	 		SELECT \"CustNo\" AS \"CustNo\"";
		sql += "	 	  		  ,SUM(\"LoanBalance\") AS \"TotalLoanBal\"";
		sql += "	 		FROM \"MonthlyLoanBal\"";
		sql += "	 		WHERE \"YearMonth\" = :yymm ";
		sql += "	 		  AND \"LoanBalance\" > 0 ";
		sql += "	 		GROUP BY \"CustNo\"";
		sql += "	 		ORDER BY SUM(\"LoanBalance\") DESC";
		sql += "	 )";
		sql += "	 SELECT M.\"CustNo\" AS F0";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F1";
		sql += "			,C.\"CustId\" AS F2";
		sql += "			,M.\"TotalLoanBal\" AS F3";
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

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("year", iYear);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findSheet3_1(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("l9731.findSheet3_1 ");

		String sql = " ";
		sql += " SELECT M.\"CustNo\"                              AS F0 ";
		sql += "       ,M.\"FacmNo\"                              AS F1 ";
		sql += "       ,DECODE(M.\"AcSubBookCode\",'00A',' ','A') AS F2 ";
		sql += "       ,\"Fn_ParseEOL\"(C.\"CustName\", 0)        AS F3 ";
		sql += "       ,C.\"CustId\"                              AS F4 ";
		sql += "       ,M.\"CityCode\"                            AS F5 ";
		sql += "       ,M.\"AcctCode\"                            AS F6 ";
		sql += "       ,TO_CHAR(M.\"StoreRate\",'0.00000')        AS F7 ";
		sql += "       ,F.\"FirstDrawdownDate\"                   AS F8 ";
		sql += "       ,M.\"PrevIntDate\"                         AS F9 ";
		sql += "       ,F.\"MaturityDate\"                        AS F10 ";
		sql += "       ,M.\"PrinBalance\"                         AS F11 ";
		sql += "       ,CASE ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"ProdNo\" IN ('60','61','62') THEN '催協' ";
		sql += "          WHEN M.\"ProdNo\" IN ('60','61','62') THEN '協' ";
		sql += "          WHEN M.\"AcctCode\" = 990  THEN '催' ";
		sql += "          WHEN M.\"OvduTerm\" IN (1,2,3,4,5) THEN TO_CHAR(M.\"OvduTerm\", '9') ";
		sql += "        ELSE '0' END AS F12";
		sql += "       ,CASE ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"ProdNo\" IN ('60','61','62') THEN '催協' ";
		sql += "          WHEN M.\"ProdNo\" IN ('60','61','62') THEN '協' ";
		sql += "          WHEN M.\"AcctCode\" = 990  THEN '催' ";
		sql += "        ELSE ' ' END AS F13";
		sql += "       ,CASE ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"PrinBalance\" = 1 THEN '5' ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"ProdNo\" IN ('60','61','62') THEN '2' ";
		sql += "          WHEN M.\"OvduTerm\" >= 7 AND M.\"OvduTerm\" <= 12 THEN '2' ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"ProdNo\" NOT IN ('60','61','62') THEN '3' ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"OvduTerm\" >= 12 THEN '2' ";
		sql += "          WHEN M.\"AcctCode\" <> 990 AND M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 THEN '2' ";
		sql += "          WHEN M.\"AcctCode\" <> 990 AND M.\"OvduTerm\" >= 1 AND M.\"OvduTerm\" <= 6 THEN '2' ";
		sql += "          WHEN M.\"AcctCode\" = 990 AND M.\"OvduTerm\" >= 12 THEN '3' ";
		sql += "        ELSE '1' END AS F14";
		sql += "       ,CASE  ";
		sql += "       	  WHEN (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]'))";
		sql += "       	  THEN 'Z'";
		sql += "       	  WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "       	  THEN 'C'";
		sql += "       	  WHEN M.\"ClCode1\" IN (3) ";
		sql += "       	  THEN 'D'";
		sql += "       	ELSE 'C' END	AS F15";
		sql += "       ,M.\"ProdNo\" 	                          AS F16 ";
		sql += "       ,M.\"LawAmount\"	                          AS F17 ";
		sql += " FROM \"MonthlyFacBal\" M ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " WHERE M.\"YearMonth\" = :yymm ";
		sql += "   AND M.\"PrinBalance\" > 0 ";
		sql += " ORDER BY M.\"CustNo\" ASC ";
		sql += " 		 ,M.\"FacmNo\" ASC ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findSheet3_2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("l9731.findSheet3_2 ");

		String sql = " ";
		sql += " SELECT M.\"CustNo\" || M.\"FacmNo\"              AS F0 ";
		sql += "       ,C.\"ClTypeJCIC\"                          AS F1 ";
		sql += "       ,M.\"ProdNo\" 	                          AS F2 ";
		sql += "       ,F.\"FirstDrawdownDate\"                   AS F3 ";
		sql += " FROM \"MonthlyLoanBal\" M ";
		sql += " LEFT JOIN \"CdCl\" C ON C.\"ClCode1\" = M.\"ClCode1\" ";
		sql += "                     AND C.\"ClCode2\" = M.\"ClCode2\" ";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " WHERE M.\"YearMonth\" = :yymm ";
		sql += "   AND M.\"LoanBalance\" > 0 ";
		sql += " ORDER BY M.\"CustNo\" ASC ";
		sql += " 		 ,M.\"FacmNo\" ASC ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

}