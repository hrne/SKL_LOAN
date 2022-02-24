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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("LY003ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LY003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	
	public List<Map<String, String>> findAll(TitaVo titaVo, int formNum, int endOfYearMonth) throws Exception {

		this.info("LY003.findAll ");

		String sql = " ";
		sql += "	SELECT ";

		if (formNum == 1 || formNum == 2 || formNum == 3) {
			sql += "		(CASE";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		    END) AS \"TYPE\"";
			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";

		}
		if (formNum == 4) {
			sql += "		SUBSTR(MFB.\"AssetClass\",0,1) AS \"AssetClass\"";
			sql += "	   ,(CASE";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) ";
			sql += "    		   AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
			sql += "   		    END) AS \"TYPE\"";
			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";
		}
		sql += "	FROM ( SELECT S.\"CustNo\"";
		sql += "				 ,S.\"FacmNo\"";
		sql += "				 ,S.\"LineAmt\"";
		sql += "		   FROM ( SELECT \"CustNo\"";
		sql += "		   				,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "		   		  FROM \"FacMain\"";
		sql += "		   		  WHERE \"UtilAmt\" > 0 ";
		sql += "		   		  GROUP BY \"CustNo\" ) T";
		sql += "		   LEFT JOIN( SELECT \"CustNo\"";
		sql += "		   					,\"FacmNo\"";
		sql += "		   					,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "		   		 	  FROM \"FacMain\"";
		sql += "		   		  	  WHERE \"UtilAmt\" > 0 ";
		sql += "		   		  	  GROUP BY \"CustNo\"";
		sql += "		   		  	  		  ,\"FacmNo\" ) S";
		sql += "		   ON S.\"CustNo\" =  T.\"CustNo\"";
		if (formNum != 3) {
			sql += "		   WHERE T.\"LineAmt\" >= 100000000";
		}
		sql += "		  ) R";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	LEFT JOIN ( SELECT DISTINCT \"CustNo\"";
		sql += "					  ,\"FacmNo\"";
		sql += "					  ,\"ClCode1\"";
		sql += "					  ,\"ClCode2\"";
		sql += "					  ,\"ClNo\"";
		sql += "				FROM \"MonthlyLoanBal\"";
		sql += "				WHERE \"YearMonth\" = :yymm";
		sql += "				  AND \"LoanBalance\" > 0 ) MF";
		sql += "	 ON MF.\"CustNo\" = F.\"CustNo\"";
		sql += "	AND MF.\"FacmNo\" = F.\"FacmNo\"";
		sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MF.\"CustNo\"";
		sql += "	 							   AND MFB.\"FacmNo\" = MF.\"FacmNo\"";
		sql += "	 							   AND MFB.\"YearMonth\" = :yymm ";
		sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = MF.\"ClCode1\"";
		sql += "							AND CM.\"ClCode2\" = MF.\"ClCode2\"";
		sql += "							AND CM.\"ClNo\" = MF.\"ClNo\"";
		sql += "	LEFT JOIN ( SELECT \"CustNo\"";
		sql += "					  ,\"FacmNo\"";
		sql += "					  ,\"EntCode\"";
		sql += "					  ,SUM(\"LoanBalance\") AS \"LoanBalance\"";
		sql += "				FROM \"MonthlyLoanBal\"";
		sql += "				WHERE \"YearMonth\" = :yymm";
		sql += "				  AND \"LoanBalance\" > 0 ";
		sql += "				GROUP BY \"CustNo\"";
		sql += "						,\"FacmNo\"";
		sql += "						,\"EntCode\" ) MF2";
		sql += "	 ON MF2.\"CustNo\" = F.\"CustNo\"";
		sql += "	AND MF2.\"FacmNo\" = F.\"FacmNo\"";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = R.\"CustNo\"";
		sql += "    LEFT JOIN (SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationSelf\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "               UNION ";
		sql += "               SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationFamily\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "               UNION ";
		sql += "               SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationCompany\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "             ) R ON R.\"RptId\" = CM.\"CustId\" ";
		
		if (formNum == 1 || formNum == 2 || formNum == 3) {
			sql += "	GROUP BY (CASE";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		      END)";
		}
		
		if (formNum == 4) {
			sql += "	GROUP BY SUBSTR(MFB.\"AssetClass\",0,1)";
			sql += "	   		,(CASE";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) ";
			sql += "    		     AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
			sql += "   		      END)";
		}
		

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", endOfYearMonth);
		return this.convertToMap(query);

	}
	
	
//	public List<Map<String, String>> findAll1(TitaVo titaVo, int formNum, int endOfYearMonth) throws Exception {
//		this.info("LY003.findAll ");
//
//		String sql = " ";
//		if (formNum == 1) {
//			// row 6~13
//			sql += "	SELECT ( CASE";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   ELSE 'N'";
//			sql += "			 END ) AS \"KIND\"";
//			sql += "		  ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
//			sql += "		  ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "		  ,SUM(ML.\"LoanBalance\") AS \"LoanBalance\"";
//			sql += "	FROM \"MonthlyLoanBal\" ML";
//			sql += "	LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = ML.\"CustNo\"";
//			sql += "								   AND M.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "								   AND M.\"YearMonth\" = ML.\"YearMonth\"";
//			sql += "	LEFT JOIN ( SELECT * ";
//			sql += "				FROM ( SELECT M.\"CustNo\"";
//			sql += "					   		 ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "					   FROM \"MonthlyFacBal\" M ";
//			sql += "					   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
//			sql += "											  AND F.\"FacmNo\" = M.\"FacmNo\"";
//			sql += "					   WHERE M.\"YearMonth\" = :yymm";
//			sql += "					   GROUP BY M.\"CustNo\" ) M ";
//			sql += "				WHERE M.\"LineAmt\" > 100000000 ) FA";
//			sql += "	ON FA.\"CustNo\" = M.\"CustNo\"";
//			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
//			sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
//			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = ML.\"CustNo\"";
//			sql += "							   AND L.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "							   AND L.\"BormNo\" = ML.\"BormNo\"";
//			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = ML.\"ClCode1\"";
//			sql += "							AND CM.\"ClCode2\" = ML.\"ClCode2\"";
//			sql += "							AND CM.\"ClNo\" = ML.\"ClNo\"";
//			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
//			sql += "	WHERE ML.\"YearMonth\" = :yymm";
//			sql += "	  AND ML.\"LoanBalance\" > 0 ";
//			sql += "	  AND FA.\"CustNo\" IS NOT NULL ";
//			sql += "	GROUP BY (CASE";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   ELSE 'N' END )";
//
//		} else if (formNum == 2) {
//			// row 14~18
//			sql += "	SELECT (CASE";
//			sql += "			  WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" = 0 THEN 'D'";
//			sql += "			  WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" <> 0 THEN 'C'";
//			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" = 0 THEN 'B'";
//			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" <> 0 THEN 'A'";
//			sql += "			ELSE ' ' END ) AS \"isRelt\"";
//			sql += "		  ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
//			sql += "		  ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "		  ,SUM(ML.\"LoanBalance\") AS \"LoanBalance\"";
//			sql += "	FROM \"MonthlyLoanBal\" ML";
//			sql += "	LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = ML.\"CustNo\"";
//			sql += "								   AND M.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "								   AND M.\"YearMonth\" = ML.\"YearMonth\"";
//			sql += "	LEFT JOIN ( SELECT * ";
//			sql += "				FROM ( SELECT M.\"CustNo\"";
//			sql += "					   		 ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "					   FROM \"MonthlyFacBal\" M ";
//			sql += "					   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
//			sql += "											  AND F.\"FacmNo\" = M.\"FacmNo\"";
//			sql += "					   WHERE M.\"YearMonth\" = :yymm";
//			sql += "					   GROUP BY M.\"CustNo\" ) M ";
//			sql += "				WHERE M.\"LineAmt\" > 100000000 ) FA";
//			sql += "	ON FA.\"CustNo\" = M.\"CustNo\"";
//			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
//			sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
//			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = ML.\"CustNo\"";
//			sql += "							   AND L.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "							   AND L.\"BormNo\" = ML.\"BormNo\"";
//			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = ML.\"ClCode1\"";
//			sql += "							AND CM.\"ClCode2\" = ML.\"ClCode2\"";
//			sql += "							AND CM.\"ClNo\" = ML.\"ClNo\"";
//			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
//			sql += "	WHERE ML.\"YearMonth\" = :yymm";
//			sql += "	  AND ML.\"LoanBalance\" > 0 ";
//			sql += "	  AND FA.\"CustNo\" IS NOT NULL ";
//			sql += "	GROUP BY (CASE";
//			sql += "			    WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" = 0 THEN 'D'";
//			sql += "			    WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" <> 0 THEN 'C'";
//			sql += "			    WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" = 0 THEN 'B'";
//			sql += "			    WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" <> 0 THEN 'A'";
//			sql += "			  ELSE ' ' END )";
//
//		} else if (formNum == 3) {
//			// row 19~22
//			sql += "	SELECT (CASE";
//			sql += "			  WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" = 0 THEN 'D'";
//			sql += "			  WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" <> 0 THEN 'C'";
//			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" = 0 THEN 'B'";
//			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" <> 0 THEN 'A'";
//			sql += "			ELSE ' ' END ) AS \"isRelt\"";
//			sql += "		  ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
//			sql += "		  ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "		  ,SUM(ML.\"LoanBalance\") AS \"LoanBalance\"";
//			sql += "	FROM \"MonthlyLoanBal\" ML";
//			sql += "	LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = ML.\"CustNo\"";
//			sql += "								   AND M.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "								   AND M.\"YearMonth\" = ML.\"YearMonth\"";
//			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
//			sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
//			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = ML.\"CustNo\"";
//			sql += "							   AND L.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "							   AND L.\"BormNo\" = ML.\"BormNo\"";
//			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = ML.\"ClCode1\"";
//			sql += "							AND CM.\"ClCode2\" = ML.\"ClCode2\"";
//			sql += "							AND CM.\"ClNo\" = ML.\"ClNo\"";
//			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
//			sql += "	WHERE ML.\"YearMonth\" = :yymm";
//			sql += "	  AND ML.\"LoanBalance\" > 0 ";
//			sql += "	GROUP BY (CASE";
//			sql += "			    WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" = 0 THEN 'D'";
//			sql += "			    WHEN R.\"ReltCode\" IS NULL AND ML.\"EntCode\" <> 0 THEN 'C'";
//			sql += "			    WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" = 0 THEN 'B'";
//			sql += "			    WHEN R.\"ReltCode\" IS NOT NULL AND ML.\"EntCode\" <> 0 THEN 'A'";
//			sql += "			  ELSE ' ' END )";
//
//		} else if (formNum == 4) {
//			// row 23~24
//			sql += "	SELECT 'T' AS \"KIND\"";
//			sql += "	      ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
//			sql += "		  ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "		  ,SUM(ML.\"LoanBalance\") AS \"LoanBalance\"";
//			sql += "	FROM \"MonthlyLoanBal\" ML";
//			sql += "	LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = ML.\"CustNo\"";
//			sql += "								   AND M.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "								   AND M.\"YearMonth\" = ML.\"YearMonth\"";
//			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
//			sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
//			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = ML.\"CustNo\"";
//			sql += "							   AND L.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "							   AND L.\"BormNo\" = ML.\"BormNo\"";
//			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = ML.\"ClCode1\"";
//			sql += "							AND CM.\"ClCode2\" = ML.\"ClCode2\"";
//			sql += "							AND CM.\"ClNo\" = ML.\"ClNo\"";
//			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
//			sql += "	WHERE ML.\"YearMonth\" = :yymm";
//			sql += "	  AND ML.\"LoanBalance\" > 0 ";
//
//		} else if (formNum == 5) {
//			// row 25~66
//			sql += "	SELECT ( CASE";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   ELSE 'N'";
//			sql += "			 END ) AS \"KIND\"";
//			sql += "	      ,(CASE";
//			sql += "       	       WHEN M.\"PrinBalance\" = 1 THEN '5'";
//			sql += "       	       WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	       WHEN M.\"OvduTerm\" >= 12 THEN '3'";
//			sql += "       	       WHEN M.\"OvduTerm\" >= 7 THEN '2'";
//			sql += "       	       WHEN M.\"OvduTerm\" >= 1 THEN '2'";
//			sql += "       	       WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	       ELSE '1'";
//			sql += "       	     END ) AS \"AssetClass\"";
//			sql += "		  ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
//			sql += "		  ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
//			sql += "		  ,SUM(ML.\"LoanBalance\") AS \"LoanBalance\"";
//			sql += "	FROM \"MonthlyLoanBal\" ML";
//			sql += "	LEFT JOIN \"MonthlyFacBal\" M ON M.\"CustNo\" = ML.\"CustNo\"";
//			sql += "								   AND M.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "								   AND M.\"YearMonth\" = ML.\"YearMonth\"";
//			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
//			sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
//			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = ML.\"CustNo\"";
//			sql += "							   AND L.\"FacmNo\" = ML.\"FacmNo\"";
//			sql += "							   AND L.\"BormNo\" = ML.\"BormNo\"";
//			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = ML.\"ClCode1\"";
//			sql += "							AND CM.\"ClCode2\" = ML.\"ClCode2\"";
//			sql += "							AND CM.\"ClNo\" = ML.\"ClNo\"";
//			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
//			sql += "	WHERE ML.\"YearMonth\" = :yymm";
//			sql += "	  AND ML.\"LoanBalance\" > 0 ";
//			sql += "	GROUP BY (CASE";
//			sql += "			    WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			    WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			    WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			    ELSE 'N'";
//			sql += "			  END )";
//			sql += "	        ,(CASE";
//			sql += "       	        WHEN M.\"PrinBalance\" = 1 THEN '5'";
//			sql += "       	        WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	        WHEN M.\"OvduTerm\" >= 12 THEN '3'";
//			sql += "       	        WHEN M.\"OvduTerm\" >= 7 THEN '2'";
//			sql += "       	        WHEN M.\"OvduTerm\" >= 1 THEN '2'";
//			sql += "       	        WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	        ELSE '1'";
//			sql += "       	      END )";
//
//		} else if (formNum == 6) {
//			// row 83~87
//			sql += "	SELECT \"KIND\" AS \"KIND\"";
//			sql += "		  ,DECODE(\"COL\",'11','1','12','1',\"COL\") AS \"COL\"";
//			sql += "		  ,SUM(ROUND(\"AMT\",0)) AS \"AMT\"";
//			sql += "	FROM ( SELECT ( CASE";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   		  ELSE 'N'";
//			sql += "			 		END ) AS \"KIND\"";
//			sql += "				 ,DECODE(F.\"UsageCode\",'02','12','11') AS \"COL\"";
//			sql += "				 ,SUM(DECODE(F.\"UsageCode\",'02',M.\"PrinBalance\" * 0.015 ,M.\"PrinBalance\" * 0.005 )) AS \"AMT\"";
//			sql += "		   FROM \"MonthlyFacBal\" M";
//			sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
//			sql += "			  	 			   	  AND F.\"FacmNo\" = M.\"FacmNo\"";
//			sql += "		   WHERE M.\"YearMonth\" = :yymm";
//			sql += "	  		 AND M.\"PrinBalance\" > 0";
//			sql += "	  	   	 AND M.\"AssetClass\" IS NULL";
//			sql += "	       GROUP BY (CASE";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   		   ELSE 'N'";
//			sql += "			 		 END )";
//			sql += "				 ,DECODE(F.\"UsageCode\",'02','12','11')";
//			sql += "	       UNION";
//			sql += "		   SELECT ( CASE";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   		  WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   		  ELSE 'N'";
//			sql += "			 		END ) AS \"KIND\"";
//			sql += "		         ,( CASE";
//			sql += "       	       		  WHEN M.\"PrinBalance\" = 1 THEN '5'";
//			sql += "       	       		  WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	       		  WHEN M.\"OvduTerm\" >= 12 THEN '3'";
//			sql += "       	       		  WHEN M.\"OvduTerm\" >= 7 THEN '2'";
//			sql += "       	       		  WHEN M.\"OvduTerm\" >= 1 THEN '2'";
//			sql += "       	       		  WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	     	    END ) AS \"COL\"";
//			sql += "		         ,SUM((CASE";
//			sql += "       	       		  	 WHEN M.\"PrinBalance\" = 1 THEN M.\"PrinBalance\" * 1";
//			sql += "       	       		  	 WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN  M.\"PrinBalance\" * 0.02";
//			sql += "       	       		  	 WHEN M.\"OvduTerm\" >= 12 THEN  M.\"PrinBalance\" * 0.1";
//			sql += "       	       		  	 WHEN M.\"OvduTerm\" >= 7 THEN  M.\"PrinBalance\" * 0.02";
//			sql += "       	       		  	 WHEN M.\"OvduTerm\" >= 1 THEN M.\"PrinBalance\" * 0.02";
//			sql += "       	       		  	 WHEN M.\"ProdNo\" IN ('60','61','62') THEN M.\"PrinBalance\" * 0.02";
//			sql += "       	     	       END ))  AS \"AMT\"";
//			sql += "	       FROM \"MonthlyFacBal\" M";
//			sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
//			sql += "			  	 			   	  AND F.\"FacmNo\" = M.\"FacmNo\"";
//			sql += "		   WHERE M.\"YearMonth\" = :yymm";
//			sql += "	  		 AND M.\"PrinBalance\" > 0";
//			sql += "	  	   	 AND M.\"AssetClass\" IS NOT NULL";
//			sql += "	       GROUP BY (CASE";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "			   		   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "			   		   ELSE 'N'";
//			sql += "			 		 END )";
//			sql += "		           ,(CASE";
//			sql += "       	       		   WHEN M.\"PrinBalance\" = 1 THEN '5'";
//			sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 THEN '3'";
//			sql += "       	       		   WHEN M.\"OvduTerm\" >= 7 THEN '2'";
//			sql += "       	       		   WHEN M.\"OvduTerm\" >= 1 THEN '2'";
//			sql += "       	       		   WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
//			sql += "       	     	     END )";
//			sql += " ) GROUP BY \"KIND\"";
//			sql += " 		   ,DECODE(\"COL\",'11','1','12','1',\"COL\")";
//		} else if (formNum == 7) {
//			// row 67~72
//		} else {
//
//			// L6~L7
//			sql += "	SELECT \"AvailableFunds\" AS \"AvailableFunds\"";
//			sql += "		  ,\"Totalequity\" AS \"Totalequity\"";
//			sql += "	FROM \"CdVarValue\"";
//			sql += "	WHERE \"YearMonth\" = :yymm ";
//
//		}
//
//		this.info("sql=" + sql);
//
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("yymm", endOfYearMonth);
//		return this.convertToMap(query);
//
//	}

}