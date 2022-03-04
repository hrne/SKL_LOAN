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

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param formNum        表格次序
	 * @param endOfYearMonth 西元年底年月
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int formNum, int endOfYearMonth) throws Exception {

		this.info("LY003.findAll " + formNum);

		String sql = " ";
		sql += "	SELECT ";

		if (formNum == 2 || formNum == 3) {
			sql += "		(CASE";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		    END) AS \"TYPE\"";
			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";

		}
		if (formNum == 1 ) {
			sql += "	   (CASE";
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
		if (formNum == 1 || formNum == 2) {
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

		if (formNum == 2 || formNum == 3) {
			sql += "	GROUP BY (CASE";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		      END)";
		}

		if (formNum == 1) {
			sql += "	GROUP BY (CASE";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) ";
			sql += "    		     AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
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

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lY003.findAll2");
		this.info("yymm=" + yearMonth);

		String sql = " ";
		sql += " SELECT \"KIND\"";
		sql += "       ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += " FROM(";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"OvduTerm\" > 3 AND M.\"OvduTerm\" <= 6 THEN 'C'";
		sql += "       	       WHEN CL.\"LegalProg\" IN ('056','057','058','060') AND (M.\"OvduTerm\" > 3 OR M.\"PrinBalance\" = 1) AND L.\"Status\" IN (2,6,7) THEN 'C'";
		sql += "       	     ELSE 'B' END ) AS \"KIND\"";
		sql += "	      ,M.\"PrinBalance\" AS \"AMT\"";
		sql += "	FROM \"MonthlyLoanBal\" ML";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
		sql += "    LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" =  ML.\"CustNo\"";
		sql += "                               AND L.\"FacmNo\" =  ML.\"FacmNo\"";
		sql += "                               AND L.\"BormNo\" =  ML.\"BormNo\"";
		sql += "    LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = ML.\"YearMonth\"";
		sql += "                                 AND M.\"CustNo\" = ML.\"CustNo\"";
		sql += "                                 AND M.\"FacmNo\" = ML.\"FacmNo\"";
		sql += "	LEFT JOIN(SELECT L.\"CustNo\"";
		sql += "				    ,L.\"FacmNo\"";
		sql += "					,L.\"LegalProg\"";
		sql += "					,L.\"Amount\"";
		sql += "					,L.\"Memo\"";
		sql += "					,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\", L.\"FacmNo\" ORDER BY L.\"TitaTxtNo\" DESC) AS \"SEQ\"";
		sql += "			  FROM \"CollLaw\" L";
		sql += "		      WHERE TRUNC(L.\"AcDate\" / 100) <= :yymm ) CL";
		sql += "	  ON CL.\"CustNo\" = M.\"CustNo\" AND CL.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "	 								  AND CL.\"SEQ\" = 1";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND M.\"AssetClass\" IS NOT NULL)";
		sql += "    GROUP BY \"KIND\"";
		sql += "	UNION";
		sql += "	SELECT 'COLLECTION' AS \"KIND\"";
		sql += "       	  ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'COLLECTION'";
		sql += "	UNION";
		sql += "	SELECT 'TOTAL' AS \"KIND\"";
		sql += "       	  ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	       UNION";
		sql += "	       SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'TOTAL'";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

}