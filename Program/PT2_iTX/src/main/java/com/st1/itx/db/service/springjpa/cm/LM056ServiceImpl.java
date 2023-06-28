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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
/* 逾期放款明細 */
public class LM056ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param isAllData 是否為產出明細
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, String isAllData) throws Exception {

		this.info("lM056.findAll");
		this.info(" yymm=" + yearMonth);

		String sql = " ";
		sql += "	SELECT MLB.\"CustNo\" AS \"CustNo\"";
		sql += "		  ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "		  ,C.\"CustId\" AS\"CustId\"";
		sql += "		  ,SUM(MLB.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "		  ,(CASE";
		sql += "			  WHEN FCA.\"SyndNo\" <> 0 THEN '聯貸'";
		sql += "			  WHEN MLB2.\"CustNo\" IS NOT NULL THEN '**'";
		sql += "			  WHEN S.\"RptId\" IS NOT NULL THEN '*'";
		sql += "			ELSE ' ' END ) AS \"KIND\"";
		sql += "		  ,'#N/A' AS \"isRelt\"";
		sql += "	FROM \"MonthlyLoanBal\" MLB";
		sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MLB.\"CustNo\"";
		sql += "								   AND MFB.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "								   AND MFB.\"YearMonth\" = MLB.\"YearMonth\"";
		sql += "	LEFT JOIN ( SELECT * ";
		sql += "				FROM ( SELECT M.\"CustNo\"";
		sql += "					   		 ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
		sql += "					   FROM \"MonthlyFacBal\" M ";
		sql += "					   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "											  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "					   WHERE M.\"YearMonth\" = :yymm";
		sql += "					   GROUP BY M.\"CustNo\" ) M ";
		sql += "				WHERE M.\"LineAmt\" > 100000000 ) MLB2";
		sql += "	ON MLB2.\"CustNo\" = MFB.\"CustNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = MLB.\"CustNo\"";
		sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = MLB.\"CustNo\"";
		sql += "							   AND L.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "							   AND L.\"BormNo\" = MLB.\"BormNo\"";
		sql += "	LEFT JOIN \"FacMain\" F2 ON F2.\"CustNo\" = MLB.\"CustNo\"";
		sql += "						    AND F2.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "						    AND F2.\"LastBormNo\" = MLB.\"BormNo\"";
		sql += "	LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = F2.\"ApplNo\"";
		sql += " LEFT JOIN ( SELECT ";
		sql += "             decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadName\",\"RelName\"),\"BusName\")as \"CustName\"  ";
		sql += "             ,to_char(decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadId\",\"RelId\"),\"BusId\"))as \"RptId\"  ";
		sql += "             ,\"RelWithCompany\" as \"Rel\"";
		sql += "             ,\"HeadName\" ";
		sql += "             ,\"HeadTitle\" ";
		sql += "             ,\"RelName\" ";
		sql += "             ,\"RelTitle\" ";
		sql += "             ,\"BusTitle\" ";
		sql += "             FROM \"LifeRelHead\" ";
		sql += "             WHERE \"RelWithCompany\"='A' ";
		sql += "             AND TRUNC(\"AcDate\" / 100 ) = :yymm ";
		sql += "             AND \"LoanBalance\" > 0 ";
		sql += "             UNION ";
		sql += "             SELECT \"EmpName\" AS \"CustName\" ";
		sql += "              ,TO_CHAR(\"EmpId\") AS \"RptId\" ";
		sql += "                      ,'N' AS \"Rel\"  ";
		sql += "              ,NULL AS \"HeadName\"  ";
		sql += "              ,NULL AS \"HeadTitle\"  ";
		sql += "              ,NULL AS \"RelName\"  ";
		sql += "              ,NULL AS \"RelTitle\"  ";
		sql += "              ,NULL AS \"BusTitle\"  ";
		sql += "             FROM \"LifeRelEmp\" ";	
		sql += "             AND TRUNC(\"AcDate\" / 100 ) = :yymm ";
		sql += "           ) S ON S.\"RptId\" = C.\"CustId\" ";
		sql += "	WHERE MLB.\"YearMonth\" = :yymm";
		sql += "	  AND MLB.\"LoanBalance\" > 0 ";
		if (isAllData == "N") {
			sql += "	  AND MLB2.\"CustNo\" IS NOT NULL ";
		}
		sql += "	GROUP BY MLB.\"CustNo\"";
		sql += "		    ,\"Fn_ParseEOL\"(C.\"CustName\",0)";
		sql += "		    ,C.\"CustId\"";
		sql += "		    ,(CASE";
		sql += "			    WHEN FCA.\"SyndNo\" <> 0 THEN '聯貸'";
		sql += "			    WHEN MLB2.\"CustNo\" IS NOT NULL THEN '**'";
		sql += "			    WHEN S.\"RptId\" IS NOT NULL THEN '*'";
		sql += "			  ELSE ' ' END )";
		sql += "		    ,'#N/A'";
		sql += "	ORDER BY \"LoanBalance\" DESC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM056.findAll2");
		this.info("yymm=" + yearMonth);

		String sql = " ";
		sql += "	WITH \"roundData\" AS (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LnAmt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempTotal\" AS (";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','G6','G7')";
		sql += "		UNION";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		UNION";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,NVL(R.\"LnAmt\",0) AS \"Value\"";
		sql += "		FROM \"roundData\" R";
		sql += "		UNION";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'D41'";
		sql += "				 WHEN M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D40'";
		sql += "			   ELSE 'N' END AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'D41'";
		sql += "				   WHEN M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D40'";
		sql += "			     ELSE 'N' END";
		sql += "	) ";
		sql += "	SELECT \"Column\" AS \"Column\" ";
		sql += "	      ,SUM(\"Value\") AS \"Value\" ";
		sql += "	FROM \"tempTotal\"";
		sql += "	GROUP BY \"Column\" ";
	
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}
}

//String sql = "SELECT J.\"SubTranCode\" AS F0";
//sql += "			,J.\"CustNo\" AS F1";
//sql += "			,J.\"FacmNo\" AS F2";
//sql += "			,J.\"BormNo\" AS F3";
//sql += "			,\"Fn_ParseEOL\"(CM.\"CustName\",0) AS F4";
//sql += "			,M.\"LoanBal\" AS F5";
//sql += "			,M.\"MaturityDate\" AS F6";
//sql += "			,CASE";
//sql += "			   WHEN TRUNC(F.\"OvduDate\" / 100) = :yymm THEN 'B3'";
//sql += "			   WHEN M.\"MaturityDate\" < :l4mdy THEN 'B1'";
//sql += "			   WHEN L.\"LegalProg\" IN ('056','058','060') THEN 'C5'";
//sql += "			 ELSE 'B3' END AS F7";
//sql += "			,NVL(L.\"LegalProg\",'#N/A') AS F8";
//sql += "			,'C7' AS F9";
//sql += "			,DECODE(L.\"Amount\",0,'#N/A','無擔保') AS F10";
//sql += "			,DECODE(F.\"RenewCode\",'2','協議',' ') AS F11";
//sql += "	  FROM(SELECT CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL) \"CustNo\"";
//sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",11, 3) AS DECIMAL) \"BormNo\"";
//sql += "				 ,J.\"SubTranCode\"";
//sql += "		   FROM \"JcicB201\" J";
//sql += "		   WHERE J.\"DataYM\" = :yymm) J";
//sql += "	  LEFT JOIN(SELECT L.\"CustNo\"";
//sql += "					  ,L.\"FacmNo\"";
//sql += "					  ,MAX(L.\"LegalProg\") \"LegalProg\"";
//sql += "					  ,SUM(L.\"Amount\") \"Amount\"";
//sql += "				FROM(SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
//sql += "					  	   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "						   ,L.\"LegalProg\"";
//sql += "					       ,0 \"Amount\"";
//sql += "						   ,ROW_NUMBER() OVER (PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
//sql += "					 FROM \"JcicB201\" J";
//sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1'";
//sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
//sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
//sql += "											AND L.\"LegalProg\" < 900";
//sql += "					 WHERE J.\"DataYM\" = :yymm";
//sql += "					 UNION ALL";
//sql += "					 SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
//sql += "						   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "						   ,NULL \"LegalProg\"";
//sql += "						   ,L.\"Amount\"";
//sql += "						   ,ROW_NUMBER() OVER(PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
//sql += "					 FROM \"JcicB201\" J";
//sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" ='1'";
//sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
//sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
//sql += "											AND L.\"LegalProg\" = 901";
//sql += "					 WHERE J.\"DataYM\" = :yymm) L";
//sql += "				 WHERE L.\"SEQ\" = 1";
//sql += "				 GROUP BY L.\"CustNo\", L.\"FacmNo\") L";
//sql += "	  ON L.\"CustNo\" = J.\"CustNo\"";
//sql += "	  AND L.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  LEFT JOIN \"MonthlyFacBal\" F ON F.\"YearMonth\" = :yymm";
//sql += "								   AND F.\"CustNo\" = J.\"CustNo\"";
//sql += "								   AND F.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = J.\"CustNo\"";
//sql += "	  							 AND M.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  							 AND M.\"BormNo\" = J.\"BormNo\"";
//sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = J.\"CustNo\"";