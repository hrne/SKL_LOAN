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

@Service("lM051ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LM051ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param formNum   表格次序
	 * @return
	 * @throws Exception
	 * 
	 */

	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth, int formNum) throws Exception {

		this.info("lM051.findAll2");

		this.info("yearMonth=" + yearMonth + "-" + formNum);

		int lYear = yearMonth / 100;
		int lMonth = yearMonth % 100 - 1;

		if (lMonth == 0) {
			lYear = lYear - 1;
			lMonth = 12;
		}

		int outStandingYMD = (lYear * 10000) + (lMonth * 100) + 1;
		this.info("outStandingYMD=" + outStandingYMD);
		String sql = " ";
		if (formNum == 1) {
			sql += "WITH rawData AS ( ";
			sql += "      SELECT SUM(CASE WHEN I.\"YearMonth\" = :yymm ";
			sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
			sql += "                 ELSE 0 END";
			sql += "                ) AS \"LnAmt\"";
			sql += "      FROM \"Ias39IntMethod\" I";
			sql += "      LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
			sql += "                                      AND I.\"CustNo\" = MLB.\"CustNo\" ";
			sql += "                                      AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
			sql += "                                      AND I.\"BormNo\" = MLB.\"BormNo\"";
			sql += "      WHERE NVL(I.\"YearMonth\", ' ') IN (:lyymm, :yymm) ";
			sql += "        AND NVL(MLB.\"CurrencyCode\",' ') = 'TWD'";
			sql += "        AND MLB.\"AcctCode\" <> '990' ";
			sql += "      GROUP BY DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') ";
			sql += "      ),";
			sql += "      roundData AS (";
			sql += "      SELECT CASE WHEN \"LnAmt\" < 0";
			sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"LnAmt\")+1 ";
			sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"LnAmt\")-1";
			sql += "                            ELSE 0 END ";
			sql += "                  WHEN \"LnAmt\" > 0";
			sql += "                  THEN ROUND(\"LnAmt\")";
			sql += "                  ELSE 0 END ";
			sql += "             AS \"LnAmt\"";
			sql += "      FROM rawData";
			sql += "      ) ";
			sql += " SELECT \"KIND\" ";
			sql += "	   ,SUM(\"AMT\")  AS \"AMT\" ";
			sql += " FROM (";
			sql += "	SELECT ( CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
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
			sql += "	GROUP BY ( CASE";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
			sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
			sql += "			     ELSE '99'";
			sql += "			   END )";
			sql += "	UNION";
			sql += "	SELECT 'TOTAL' AS \"KIND\"";
			sql += "          ,SUM(\"AMT\") AS \"AMT\"";
			sql += "	FROM ( SELECT SUM(M.\"PrinBalance\") AS \"AMT\"";
			sql += "		   FROM \"MonthlyFacBal\" M";
			sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
			sql += "						   		  AND F.\"FacmNo\" = M.\"FacmNo\"";
			sql += "		   WHERE M.\"YearMonth\" = :yymm";
			sql += "	  	     AND M.\"PrinBalance\" > 0";
			sql += "    	   UNION ";
			sql += "    	   SELECT CASE WHEN R.\"LnAmt\" >= 0 ";
			sql += "                	   THEN R.\"LnAmt\" ";
			sql += "                	   ELSE ABS(R.\"LnAmt\") END AS \"AMT\"";
			sql += "    	  FROM roundData R";
			sql += "		  UNION";
			sql += "	      SELECT SUM(\"TdBal\") AS \"AMT\"";
			sql += "		  FROM \"AcMain\"";
			sql += "		  WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000)";
			sql += "	  		AND \"MonthEndYm\" = :yymm ) ";
			sql += "    UNION ";
			sql += "    SELECT '99' AS \"KIND\" ";
			sql += "          ,CASE WHEN R.\"LnAmt\" >= 0 ";
			sql += "                THEN R.\"LnAmt\" ";
			sql += "                ELSE ABS(R.\"LnAmt\") END AS \"AMT\"";
			sql += "    FROM roundData R";
			sql += ")GROUP BY \"KIND\"";
			sql += " ORDER BY \"KIND\" ASC";
		} else if (formNum == 2) {
			sql += "SELECT * FROM (";
			sql += "	SELECT ( CASE";
			sql += "			   WHEN M.\"AcSubBookCode\" = '00A' THEN '1'";
			sql += "			   WHEN M.\"AcSubBookCode\" = '201' THEN 'A'";
			sql += "			   ELSE '99'";
			sql += "			 END ) AS \"ASBC\"";
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
			sql += "			     WHEN M.\"AcSubBookCode\" = '00A' THEN '1'";
			sql += "			     WHEN M.\"AcSubBookCode\" = '201' THEN 'A'";
			sql += "			     ELSE '99'";
			sql += "			   END )";
			sql += "		    ,( CASE";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND M.\"ProdNo\" NOT IN ('60','61','62') AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
			sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
			sql += "			     ELSE '99'";
			sql += "			   END ))RES ";
			sql += "	WHERE RES.\"KIND\" IN (1,2)";
			sql += "	ORDER BY RES.\"KIND\" ASC";
		} else if (formNum == 3) {
			sql += "SELECT * FROM (";
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
//			sql += "	  AND M.\"AssetClass\" IS NULL";
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
			sql += "	ORDER BY RES.\"KIND\" ASC";

		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", String.valueOf(yearMonth));
		if (formNum != 1) {
			query.setParameter("lyymmdd", String.valueOf(outStandingYMD));
		} else {
			query.setParameter("lyymm", String.valueOf(outStandingYMD / 100));
		}

		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出(備呆總帳)
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param lastYM    上西元年月
	 * @param formNum   表格次序
	 * @return
	 * @throws Exception
	 * 
	 */

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, int lastYM, int formNum) throws Exception {
		this.info("lM051.findAll");

		String iYearMonth = String.valueOf(yearMonth);

		this.info("yymm=" + iYearMonth + ",lyymm=" + lastYM);

		String sql = " ";

		if (formNum == 1) {

			sql += "WITH rawData AS ( ";
			sql += "      SELECT SUM(CASE WHEN I.\"YearMonth\" = :yymm ";
			sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
			sql += "                 ELSE 0 END";
			sql += "                ) AS \"LnAmt\"";
//			sql += "             -";
//			sql += "             SUM(CASE WHEN I.\"YearMonth\" = :lyymm";
//			sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
//			sql += "                 ELSE 0 END";
//			sql += "                )";
//			sql += "             AS \"LnAmt\"";
			sql += "      FROM \"Ias39IntMethod\" I";
			sql += "      LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
			sql += "                                      AND I.\"CustNo\" = MLB.\"CustNo\" ";
			sql += "                                      AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
			sql += "                                      AND I.\"BormNo\" = MLB.\"BormNo\"";
			sql += "      WHERE NVL(I.\"YearMonth\", ' ') IN (:lyymm, :yymm) ";
			sql += "        AND NVL(MLB.\"CurrencyCode\",' ') = 'TWD'";
			sql += "        AND MLB.\"AcctCode\" <> '990' ";
			sql += "      GROUP BY DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') ";
			sql += "      ),";
			sql += "      roundData AS (";
			sql += "      SELECT CASE WHEN \"LnAmt\" < 0";
			sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"LnAmt\")+1 ";
			sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"LnAmt\")-1";
			sql += "                            ELSE 0 END ";
			sql += "                  WHEN \"LnAmt\" > 0";
			sql += "                  THEN ROUND(\"LnAmt\")";
			sql += "                  ELSE 0 END ";
			sql += "             AS \"LnAmt\"";
			sql += "      FROM rawData";
			sql += "      )";
			sql += "	SELECT \"AssetClassNo\"";
			sql += "          ,\"AcSubBookCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    UNION ";
			sql += "    SELECT '61' AS \"AssetClassNo\" ";
			sql += "      	  ,'999' AS \"AcSubBookCode\" ";
			sql += "          ,CASE WHEN R.\"LnAmt\" >= 0 ";
			sql += "                THEN R.\"LnAmt\" ";
			sql += "                ELSE ABS(R.\"LnAmt\") END AS \"LoanBal\"";
			sql += "    FROM roundData R";

		} else if (formNum == 2) {

			// 此年月為上個月
			iYearMonth = String.valueOf(lastYM);

			sql += "	SELECT DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\") AS \"AssetClassNo\"";
			sql += "          ,SUM(\"LoanBal\") AS \"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    GROUP BY DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\")";
			sql += "    ORDER BY \"AssetClassNo\"";

		} else if (formNum == 3) {

			sql += "	SELECT \"OvduNo\"";
			sql += "          ,\"AcctCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052Ovdu\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "      AND \"OvduNo\" IN ('1','2','3')";
			sql += "    ORDER BY \"OvduNo\"";
			sql += "   			,\"AcctCode\"";

		} else if (formNum == 4) {

			sql += "	SELECT \"LoanAssetCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052LoanAsset\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    ORDER BY \"LoanAssetCode\"";

		} else if (formNum == 5) {

			sql += "	SELECT \"YearMonth\" ";
			sql += "          ,\"AssetEvaTotal\" "; // --五類資產評估合計
			sql += "          ,\"LegalLoss\""; // --法定備抵損失提撥
			sql += "          ,\"ApprovedLoss\""; // --會計部核定備抵損失
			sql += "    FROM \"MonthlyLM052Loss\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";

		} else if (formNum == 6) {

			sql += " SELECT ";
			sql += " 	   SUM(0-\"TdBal\")  AS \"LossTotal\" ";
			sql += " FROM \"CoreAcMain\"";
			sql += " WHERE \"AcNoCode\" IN ( '10623' "; // 備抵損失-擔保放款
			sql += "						,'10624') ";// 備抵損失-催收款項-擔保放款
			sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
			sql += "   AND \"CurrencyCode\" = 'TOL' ";

		}

		this.info("sql" + formNum + "=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (formNum == 1) {
			query.setParameter("lyymm", lastYM);
		}
		query.setParameter("yymm", iYearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出(明細表)
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM051.findAll2 ");
		this.info("yearMonth=" + yearMonth);
		String sql = " ";
		sql += "	  SELECT M.\"CustNo\""; // F0
		sql += "			,M.\"FacmNo\""; // F1
		sql += "			,DECODE(M.\"AcSubBookCode\",' ',' ','00A','A') AS \"AcSubBookCode\""; // F2
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\""; // F3
		sql += "			,M.\"PrinBalance\""; // F4
		sql += "			,M.\"FacAcctCode\""; // F5
		sql += "			,M.\"OvduTerm2\" AS \"OvduTerm\"";// F6
		sql += "			,M.\"CityCode\""; // F7
		sql += "			,M.\"PrevIntDate\""; // F8
		sql += "			,M.\"AssetClass\" "; // F9
		sql += "			,CD.\"Item\""; // F10
		sql += "			,NVL(L.\"Amount\",0) AS \"Amount\""; // F11
		sql += "			,(CASE ";
		sql += "				WHEN M.\"PrinBalance\" = 1 THEN '無擔保'";
		sql += "				WHEN M.\"PrinBalance\" > 1 AND M.\"AcctCode\" = '990' THEN '有擔保'";
		sql += "				WHEN M.\"PrinBalance\" > 1 THEN '有足額擔保'";
		sql += "			  ELSE '' END) ||";
		sql += "			 (CASE ";
		sql += "				WHEN M.\"AcctCode\" <> '990' THEN '--但債信不良(' || M.\"AssetClass\" || ')' ";
		sql += "			  ELSE '' END) ||";
		sql += "			 (CASE ";
		sql += "				WHEN M.\"AcctCode\" = '990' AND M.\"ProdNo\" IN ('60','61','62') THEN '--協議後正常還款'";
		sql += "				WHEN M.\"OvduTerm\" > 0 AND M.\"OvduTerm\" <= 5 AND M.\"OvduDays\" > 30 THEN '--逾期'";
		sql += "				WHEN M.\"OvduDays\" = 0 THEN '--正常繳息'";
		sql += "				WHEN M.\"OvduDays\" > 0 AND M.\"OvduDays\" <= 30 THEN '--逾期未滿30日'";
		sql += "				WHEN M.\"OvduTerm\" > 6 AND M.\"OvduTerm\" <= 12 AND M.\"OvduDays\" > 30 THEN '--逾期7-12(' || M.\"AssetClass\" ||')'";
		sql += "				WHEN M.\"OvduTerm\" > 12 THEN '--逾期12月(' || M.\"AssetClass\" || ')'";
		sql += "				WHEN M.\"OvduDays\" = 0 AND M.\"ProdNo\" IN ('60','61','62') THEN '--協議後正常繳款(' || M.\"AssetClass\" || ')'";
		sql += "			  ELSE '' END) AS \"Memo\""; // F12
		sql += "			,M.\"ProdNo\""; // F13
		sql += "			,M.\"RenewCode\""; // F14
		sql += "			,M.\"LawAmount\""; // F15
		sql += "			,M.\"AssetClass2\""; // F16
		sql += "			,M.\"PrinLawDiff\""; // F17
		sql += "			,M.\"LawAssetClass\""; // F18
		sql += "	  FROM(SELECT M.\"CustNo\"";
		sql += "				 ,M.\"FacmNo\"";
		sql += "				 ,DECODE(M.\"AcSubBookCode\",'201','00A',' ') AS \"AcSubBookCode\"";
		sql += "				 ,M.\"PrinBalance\" AS \"PrinBalance\"";
		sql += "				 ,M.\"PrinBalance\" - M.\"LawAmount\" AS \"PrinLawDiff\"";
		sql += "				 ,M.\"FacAcctCode\"";
		sql += "				 ,M.\"AcctCode\"";
		sql += "				 ,(CASE";
		sql += "				     WHEN M.\"OvduTerm\" > 5 OR M.\"OvduTerm\"= 0 THEN 99";
		sql += "					 ELSE M.\"OvduTerm\"";
		sql += "				   END) AS \"OvduTerm\"";
		sql += "				 ,M.\"OvduTerm\" AS \"OvduTerm2\"";
		sql += "				 ,M.\"OvduDays\"";
		sql += "				 ,M.\"CityCode\"";
		sql += "				 ,M.\"PrevIntDate\"";
		sql += "				 ,M.\"RenewCode\"";
		sql += "				 ,M.\"LawAmount\"";
		sql += "				 ,M.\"ProdNo\"";
		sql += "				 ,M.\"AssetClass\" ";
		sql += "				 ,M.\"AssetClass2\" ";
		sql += "				 ,M.\"LawAssetClass\" ";
		sql += "		   FROM \"MonthlyFacBal\" M";
		sql += "		   WHERE M.\"YearMonth\" = :yymm ";
		sql += "		     AND M.\"PrinBalance\" > 0 ";
		sql += "		     AND M.\"AssetClass\" IS NOT NULL ";
		sql += "		     AND M.\"AssetClass\" IN ('2','3','4','5') ";
		sql += "		   ) M";
		sql += "	  LEFT JOIN(SELECT * ";
		sql += "				FROM(SELECT L.\"CustNo\"";
		sql += "						   ,L.\"FacmNo\"";
		sql += "						   ,L.\"LegalProg\"";
		sql += "						   ,L.\"Amount\"";
		sql += "						   ,L.\"Memo\"";
		sql += "						   ,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\", L.\"FacmNo\" ORDER BY L.\"RecordDate\" DESC,L.\"CreateDate\" DESC ) AS SEQ";
		sql += "				     FROM \"CollLaw\" L";
		sql += "					 WHERE TRUNC(L.\"AcDate\" / 100) <= :yymm ) L";
		sql += "				WHERE L.SEQ = 1) L ";
		sql += "	    ON L.\"CustNo\" = M.\"CustNo\" AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "	  LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = M.\"AcSubBookCode\"";
		sql += "	  LEFT JOIN \"CdCode\" CD ON CD.\"Code\" = L.\"LegalProg\"";
		sql += "	   					     AND CD.\"DefCode\" = 'LegalProg' ";
//		sql += "	  ORDER BY DECODE(M.\"OvduText\",' ',1,'*協-' || SUBSTR(M.\"OvduTerm\",0,1),2,'協',3,'協*',4,'催',5,'催協',6) ASC";
		sql += "	  ORDER BY M.\"OvduTerm\" ASC";
		sql += "	  		  ,M.\"CustNo\" ASC";
		sql += "	  		  ,M.\"FacmNo\" ASC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", String.valueOf(yearMonth));
		return this.convertToMap(query);
	}

}