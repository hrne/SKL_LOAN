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
public class LM052ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
			sql += "        AND MLB.\"AcctCode\" <> 990 ";
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
			sql += "	      SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
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

}