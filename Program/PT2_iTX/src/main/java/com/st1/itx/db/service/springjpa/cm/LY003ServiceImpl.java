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
		if (formNum == 1) {
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
//		if (formNum == 4) {
//			sql += "		SUBSTR(MFB.\"AssetClass\",0,1) AS \"AssetClass\"";
//			sql += "	   ,(CASE";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) ";
//			sql += "    		   AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
//			sql += "   		    END) AS \"TYPE\"";
//			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
//			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";
//		}
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
//		if (formNum == 4) {
//			sql += "	GROUP BY SUBSTR(MFB.\"AssetClass\",0,1)";
//			sql += "	   		,(CASE";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) ";
//			sql += "    		     AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
//			sql += "   		      END)";
//		}

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
		sql += "       	       WHEN CL.\"LegalProg\" IN ('056','057','058','060') AND (M.\"OvduTerm\" > 3 OR M.\"PrinBalance\" = 1) AND M.\"Status\" IN (2,6,7) THEN 'C'";
		sql += "       	     ELSE 'B' END ) AS \"KIND\"";
		sql += "	      ,M.\"PrinBalance\" AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
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
		sql += "	  AND M.\"PrinBalance\" > 0 )";
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

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findAll3(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM055.findAll");

		int ilDate = 0;
		int iYear = yearMonth / 100;
		int iMonth = yearMonth % 100;
		if (iMonth == 1) {
			ilDate = (iYear - 1) * 10000 + 1201;
		} else {
			ilDate = (yearMonth - 1) * 100 + 1;
		}

		this.info("yearMonth=" + yearMonth);
		this.info("ilDate=" + ilDate);

		String sql = " ";
		// --取 逾期放款、未列入逾期應予評估放款(KIND=1、2)
		sql += "	WITH \"tempA\" AS (";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "		 	    AND ( M.\"FacAcctCode\" = 340 ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z' ";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) THEN 'C' ";
		sql += "       	     ELSE '99' END ) AS \"TYPE\"";
		sql += "	      ,M.\"AssetClass\" AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	GROUP BY(CASE";
		sql += "       	       WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "		 	    AND ( M.\"FacAcctCode\" = 340 ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z' ";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) THEN 'C' ";
		sql += "       	     ELSE '99' END )";
		sql += "	      ,M.\"AssetClass\"";
		// --取 放款折溢價及催收費用
		sql += "	),\"tempB\" AS (";
		sql += "		SELECT 'A' AS \"ClNo\"";
		sql += "          	  ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"AMT\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "	  	  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempC\" AS (";
		// --取 購置住宅+修繕貸款(正常)
		sql += "	SELECT RES.\"TYPE\" AS \"TYPE\" ";
		sql += "		  ,1 AS \"KIND\" ";
		sql += "		  ,SUM(RES.\"AMT\") AS \"AMT\"";
		sql += "	FROM (";
		sql += "	SELECT ( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"TYPE\"";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND M.\"ProdNo\" NOT IN ('60','61','62') AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\"";
		sql += "	LEFT JOIN \"CdIndustry\" CDI ON CDI.\"IndustryCode\" = CM.\"IndustryCode\"";
		sql += "							    AND (CDI.\"IndustryItem\" LIKE '不動產%' OR CDI.\"IndustryItem\" LIKE '建築%')";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND M.\"PrevIntDate\"  >= :lyymmdd";
		sql += "	GROUP BY ( CASE";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			     ELSE '99'";
		sql += "			   END ) ";
		sql += "		    ,( CASE";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND M.\"ProdNo\" NOT IN ('60','61','62') AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
		sql += "			     ELSE '99'";
		sql += "			   END ))RES ";
		sql += "	WHERE RES.\"KIND\" IN (1,2)";
		sql += "	GROUP BY RES.\"TYPE\"";
		// --取 應收利息
		sql += "	),\"tempD\" AS (";
		sql += "	SELECT SUM(M.\"IntAmtAcc\") AS \"AMT\" ";
		sql += "	FROM \"MonthlyLoanBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"LoanBalance\" > 0 ";
		// --取 資產五分類(正常、應予注意、可望收回、收回困難、收回無望)
		sql += "	),\"tempE\" AS (";
		sql += "	SELECT ( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"TYPE\"";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"OvduTerm\" IN (3,4,5) THEN '1' ";
		sql += "			   WHEN M.\"AcctCode\" = 990 THEN '1' ";
		sql += "			   WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"AcctCode\" <> 990 THEN '1' ";
		sql += "			   WHEN M.\"OvduTerm\" IN (1,2) THEN '2'";
		sql += "			   ELSE '3'";
		sql += "			 END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	GROUP BY(CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   ELSE '99'";
		sql += "			 END )";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"OvduTerm\" IN (3,4,5) THEN '1' ";
		sql += "			   WHEN M.\"AcctCode\" = 990 THEN '1' ";
		sql += "			   WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"AcctCode\" <> 990 THEN '1' ";
		sql += "			   WHEN M.\"OvduTerm\" IN (1,2) THEN '2'";
		sql += "			   ELSE '3'";
		sql += "			 END )";
		// --取 折溢價、催收費用
		sql += "	),\"tempF\" AS (";
		sql += "	SELECT 'ZZ' AS \"TYPE\"";
		sql += "		  ,4 AS \"KIND\"";
		sql += "		  ,\"LoanBal\" AS \"AMT\"";
		sql += "	FROM \"MonthlyLM052AssetClass\"";
		sql += "	WHERE \"YearMonth\" = :yymm";
		sql += "	  AND \"AssetClassNo\" = 62 ";
		sql += "	UNION";
		sql += "    SELECT 'ZZ' AS \"TYPE\"";
		sql += "          ,3 AS \"KIND\"";
		sql += "		  ,NVL(R.\"LnAmt\",0) AS \"AMT\"";
		sql += "    FROM (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\", 0)),0) AS \"LnAmt\"";
		sql += "    	FROM \"Ias39IntMethod\" I";
		sql += "        LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
		sql += "                                        AND I.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                                        AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                                        AND I.\"BormNo\" = MLB.\"BormNo\"";
		sql += "        WHERE I.\"YearMonth\" IN (:yymm) ";
		sql += "          AND MLB.\"AcctCode\" <> 990 ) R";
		sql += "	)";
		sql += "	SELECT R.\"TYPE\" AS F0";
		sql += "		  ,R.\"KIND\" AS F1";
		sql += "		  ,R.\"AMT\" AS F2";
		sql += "		  ,CASE";
		sql += "			 WHEN R.\"KIND\" = 1 THEN '逾期放款' ";
		sql += "			 WHEN R.\"KIND\" = 2 THEN '未列入逾期應予評估放款' ";
		sql += "			 WHEN R.\"KIND\" = 3 THEN '正常放款I' ";
		sql += "			 WHEN R.\"KIND\" = 4 THEN '應予注意II' ";
		sql += "			 WHEN R.\"KIND\" = 5 THEN '可望收回III'";
		sql += "			 WHEN R.\"KIND\" = 6 THEN '收回困難IV'";
		sql += "			 WHEN R.\"KIND\" = 7 THEN '收回無望V'";
		sql += "			 WHEN R.\"KIND\" = 8 THEN '備抵損失I' ";
		sql += "			 WHEN R.\"KIND\" = 9 THEN '備抵損失II' ";
		sql += "			 WHEN R.\"KIND\" = 10 THEN '備抵損失III' ";
		sql += "			 WHEN R.\"KIND\" = 11 THEN '備抵損失IV' ";
		sql += "			 WHEN R.\"KIND\" =12 THEN '備抵損失V'";
		sql += "		   END AS F3";
		sql += "	FROM ( ";
		sql += "		SELECT E.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(E.\"KIND\") AS \"KIND\"";
		sql += "			  ,E.\"AMT\" AS \"AMT\"";
		sql += "		FROM \"tempE\" E";
		sql += "		WHERE E.\"KIND\" IN (1,2)";
		sql += "		UNION";
		sql += "		SELECT A.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(A.\"KIND\") + 2 AS \"KIND\"";
		sql += "			  ,A.\"AMT\" AS \"AMT\"";
		sql += "		FROM \"tempA\" A";
		sql += "		UNION";
		sql += "		SELECT A.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(A.\"KIND\") + 7 AS \"KIND\"";
		sql += "			  ,ROUND(";
		sql += "			   CASE ";
		sql += "			  	 WHEN A.\"KIND\" = 1 AND A.\"TYPE\" = 'D' THEN A.\"AMT\" * 0.005 ";
		sql += "			  	 WHEN A.\"KIND\" = 1 THEN (A.\"AMT\" ";
		sql += "				      + (SELECT NVL(B.\"AMT\",0) FROM \"tempB\" B)) * 0.005 ";
		sql += "					  + C.\"AMT\" * 0.01 ";
		sql += "			  	 WHEN A.\"KIND\" = 2 AND A.\"TYPE\" = 'C' THEN A.\"AMT\" * 0.02 ";
		sql += "					  + (SELECT NVL(D.\"AMT\",0) FROM \"tempD\" D) * 0.02 ";
		sql += "			  	 WHEN A.\"KIND\" = 2 THEN A.\"AMT\" * 0.02 ";
		sql += "			  	 WHEN A.\"KIND\" = 3 THEN A.\"AMT\" * 0.1 ";
		sql += "			  	 WHEN A.\"KIND\" = 4 THEN A.\"AMT\" * 0.5 ";
		sql += "			  	 WHEN A.\"KIND\" = 5 THEN A.\"AMT\" * 1";
		sql += "			   END , 0 ) AS \"AMT\"";
		sql += "		FROM \"tempA\" A";
		sql += "		LEFT JOIN \"tempC\" C ON C.\"TYPE\" = A.\"TYPE\"";
		sql += "							 AND C.\"KIND\" = A.\"KIND\"";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,1 AS \"KIND\"";
		sql += "			  ,NVL(F.\"AMT\",0) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		WHERE F.\"KIND\" = 4 ";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(F.\"KIND\") AS \"KIND\"";
		sql += "			  ,NVL(F.\"AMT\",0) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(F.\"KIND\") + 5 AS \"KIND\"";
		sql += "			  ,ROUND(";
		sql += "			   CASE ";
		sql += "			  	 WHEN F.\"KIND\" = 3 THEN F.\"AMT\" * 0.005 ";
		sql += "			  	 WHEN F.\"KIND\" = 4 THEN F.\"AMT\" * 0.02 ";
		sql += "			   END , 0 ) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		WHERE F.\"KIND\" IN (3,4)";
		sql += "	)R";
		sql += "	ORDER BY R.\"KIND\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		query.setParameter("lyymmdd", ilDate);
		return this.convertToMap(query);
	}
}