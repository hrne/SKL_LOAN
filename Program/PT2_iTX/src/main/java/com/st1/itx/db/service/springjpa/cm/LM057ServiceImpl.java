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
public class LM057ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	 * @param monthDate 西元年月底日
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int monthDate) throws Exception {
		this.info("lM057.findAll ");

		// 年
		int iYear = monthDate / 10000;
		// 月
		int iMonth = (monthDate / 100) % 100;

		int lastMonth = iYear * 100 + iMonth - 1;
		
		
		if(iMonth == 1) {
			
			lastMonth = (iYear - 1) * 100 + 12 ;
		}
		
//		int last4Month = 0;
//		if (iMonth <= 4) {
//			last4Month = ((iYear - 1) * 100) + (12 - (4 - iMonth));
//		} else {
//			last4Month = iYear * 100 + (iMonth - 4);
//		}

		this.info("lM057.findAll YYMM=" + ((iYear * 100) + iMonth) + ",lastMonth=" + lastMonth);

		String sql = " ";
		sql += " ";
		sql += "	SELECT DECODE(M.\"AcSubBookCode\",'201','A',' ') AS \"AcSubBookCode\"";
		sql += "       	  ,M.\"CustNo\"";
		sql += "       	  ,M.\"FacmNo\"";
		sql += "       	  ,M.\"BormNo\"";
		sql += "       	  ,C.\"CustName\"";
		sql += "       	  ,M.\"LoanBalance\"";
		sql += "       	  ,L.\"PrevPayIntDate\"";
		sql += "       	  ,M.\"StoreRate\"";
		sql += "       	  ,( CASE";
		sql += "       	       WHEN M.\"AcctCode\" = 990 AND M.\"LoanBalance\" = 1 THEN 'B3'";
		sql += "       	       WHEN M2.\"CustNo\" = M.\"CustNo\" AND M2.\"FacmNo\" = M.\"FacmNo\" AND M2.\"BormNo\" = M.\"BormNo\" THEN 'B3'";
		sql += "       	       WHEN CO.\"CustNo\" = M.\"CustNo\"  AND CO.\"LegalProg\" IN ('056','057','058','060') THEN 'C5'";
		sql += "       	     ELSE 'B1' END ) AS \"Type\"";
		sql += "		  ,NVL(CO.\"LegalProg\",' ') AS \"LegalProg\"";
		sql += " 		  ,' ' AS \"Memo\"";
		sql += "	      ,M.\"ProdNo\"";
		sql += "	FROM \"MonthlyLoanBal\" M";
		sql += "    LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" =  M.\"CustNo\"";
		sql += "                               AND L.\"FacmNo\" =  M.\"FacmNo\"";
		sql += "                               AND L.\"BormNo\" =  M.\"BormNo\"";
		sql += "                               AND L.\"LoanBal\" > 0";
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"CustNo\"";
		sql += "			  ,\"FacmNo\"";
		sql += "			  ,\"BormNo\"";
		sql += "		FROM \"MonthlyLoanBal\"";
		sql += "		WHERE \"YearMonth\" = :lyymm";
		sql += "		  AND \"AcctCode\" = 990";
		sql += "		  AND \"LoanBalance\" > 0";
		sql += "	) M2 ON M2.\"CustNo\" =  M.\"CustNo\" AND M2.\"CustNo\" =  M.\"CustNo\"";
		sql += "                               			  AND M2.\"FacmNo\" =  M.\"FacmNo\"";
		sql += "                               			  AND M2.\"BormNo\" =  M.\"BormNo\"";
		sql += "    LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                                  AND MF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                  AND MF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "				    ,\"FacmNo\"";
		sql += "					,\"LegalProg\"";
		sql += "					,ROW_NUMBER() OVER (PARTITION BY \"CustNo\", \"FacmNo\" ORDER BY \"RecordDate\" DESC) AS \"SEQ\"";
		sql += "			  FROM \"CollLaw\"";
		sql += "		      WHERE TRUNC(\"RecordDate\" / 100) <= :yymm ) CO";
		sql += "	  ON CO.\"CustNo\" = M.\"CustNo\" AND CO.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "	 								  AND CO.\"SEQ\" = 1";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"AcctCode\" = 990 ";
		sql += "	  AND M.\"LoanBalance\" > 0  ";
		info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", (iYear * 100) + iMonth);
		query.setParameter("lyymm", lastMonth);
		

		return this.convertToMap(query);

	}
	
	
	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param monthDate 西元年月底日
	 * 
	 */
	public List<Map<String, String>> findTotal(TitaVo titaVo, int monthDate) throws Exception {
		this.info("lM057.findAllTotal ");

		// 年
		int iYear = monthDate / 10000;
		// 月
		int iMonth = (monthDate / 100) % 100;

		int lastMonth = iYear * 100 + iMonth - 1;
		
		
		if(iMonth == 1) {
			
			lastMonth = (iYear - 1) * 100 + 12 ;
		}
		

		this.info("lM057.findAll YYMM=" + ((iYear * 100) + iMonth) + ",lastMonth=" + lastMonth);

		String sql = " ";
		
		sql += "	WITH \"rawData\" AS ( ";
		sql += "      SELECT SUM(CASE WHEN I.\"YearMonth\" = :yymm ";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             -";
		sql += "             SUM(CASE WHEN I.\"YearMonth\" = :lyymm";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             AS \"LnAmt\"";
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
		sql += "      \"roundData\" AS (";
		sql += "      SELECT CASE WHEN \"LnAmt\" < 0";
		sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"LnAmt\")+1 ";
		sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"LnAmt\")-1";
		sql += "                            ELSE 0 END ";
		sql += "                  WHEN \"LnAmt\" > 0";
		sql += "                  THEN ROUND(\"LnAmt\")";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"LnAmt\"";
		sql += "      FROM \"rawData\" ";
		sql += "      ),";
		sql += " 	\"tempTotal\" AS (";
		sql += " 		SELECT 'Total' AS \"Item\"";
		sql += " 			  ,SUM(\"LoanBalance\") AS \"AMT\" ";
		sql += " 		FROM \"MonthlyLoanBal\"";
		sql += " 		WHERE \"LoanBalance\" > 0 ";
		sql += " 		  AND \"YearMonth\" = :yymm ";
//		sql += " 		UNION";
//		sql += "		SELECT 'DPLoan' AS \"Item\" ";
//		sql += " 	   		  ,SUM(\"TdBal\")  AS \"AMT\" ";
//		sql += " 		FROM \"AcMain\"";
//		sql += " 		WHERE \"AcNoCode\" IN ( '10600304000' ) "; //-- 擔保放款-折溢價
//		sql += "   		  AND \"MonthEndYm\" = :yymm ";
		sql += " 		UNION";
		sql += " 		SELECT 'Collection' AS \"Item\" ";
		sql += " 	   		  ,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " 		FROM \"AcMain\"";
		sql += " 		WHERE \"AcNoCode\" IN (  '10601301000' "; //-- 催收款項-法務費用
		sql += "								,'10601302000' "; //-- 催收款項-火險費用
		sql += "								,'10601304000') ";//-- 催收款項-折溢價
		sql += "   		  AND \"MonthEndYm\" = :yymm ";
		sql += "    	UNION ";			
		sql += "    	SELECT 'Loss' AS \"Item\" ";
		sql += "          	  ,CASE WHEN R.\"LnAmt\" >= 0 ";
		sql += "                	THEN R.\"LnAmt\" ";
		sql += "                ELSE ABS(R.\"LnAmt\") END AS \"AMT\"";
		sql += "    	FROM \"roundData\" R";
		sql += " 	), \"ovduLoan\" AS (";
		sql += " 		SELECT 'Ovdu' AS \"Item\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"AMT\"";
		sql += " 		FROM \"MonthlyFacBal\"";
		sql += " 		WHERE \"YearMonth\" = :yymm";
		sql += " 		  AND \"AcctCode\" <> 990 ";
		sql += " 		  AND \"OvduTerm\" IN (3,4,5,6)";
		sql += " 	)";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"AcctCode\" = 990 AND M.\"LoanBalance\" = 1 THEN 'B3'";
		sql += "       	       WHEN M2.\"CustNo\" = M.\"CustNo\" AND M2.\"FacmNo\" = M.\"FacmNo\" AND M2.\"BormNo\" = M.\"BormNo\" THEN 'B3'";
		sql += "       	       WHEN CO.\"CustNo\" = M.\"CustNo\"  AND CO.\"LegalProg\" IN ('056','057','058','060') THEN 'C5'";
		sql += "       	     ELSE 'B1' END ) AS \"Item\"";
		sql += "       	  ,SUM(M.\"LoanBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyLoanBal\" M";
		sql += "    LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" =  M.\"CustNo\"";
		sql += "                               AND L.\"FacmNo\" =  M.\"FacmNo\"";
		sql += "                               AND L.\"BormNo\" =  M.\"BormNo\"";
		sql += "                               AND L.\"LoanBal\" > 0";
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"CustNo\"";
		sql += "			  ,\"FacmNo\"";
		sql += "			  ,\"BormNo\"";
		sql += "		FROM \"MonthlyLoanBal\"";
		sql += "		WHERE \"YearMonth\" = :lyymm";
		sql += "		  AND \"AcctCode\" = 990";
		sql += "		  AND \"LoanBalance\" > 0";
		sql += "	) M2 ON M2.\"CustNo\" =  M.\"CustNo\" AND M2.\"CustNo\" =  M.\"CustNo\"";
		sql += "                               			  AND M2.\"FacmNo\" =  M.\"FacmNo\"";
		sql += "                               			  AND M2.\"BormNo\" =  M.\"BormNo\"";
		sql += "    LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                                  AND MF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                  AND MF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "				    ,\"FacmNo\"";
		sql += "					,\"LegalProg\"";
		sql += "					,ROW_NUMBER() OVER (PARTITION BY \"CustNo\", \"FacmNo\" ORDER BY \"RecordDate\" DESC) AS \"SEQ\"";
		sql += "			  FROM \"CollLaw\"";
		sql += "		      WHERE TRUNC(\"RecordDate\" / 100) <= :yymm ) CO";
		sql += "	  ON CO.\"CustNo\" = M.\"CustNo\" AND CO.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "	 								  AND CO.\"SEQ\" = 1";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"AcctCode\" = 990 ";
		sql += "	  AND M.\"LoanBalance\" > 0  ";
		sql += "	GROUP BY CASE";
		sql += "       	       WHEN M.\"AcctCode\" = 990 AND M.\"LoanBalance\" = 1 THEN 'B3'";
		sql += "       	       WHEN M2.\"CustNo\" = M.\"CustNo\" AND M2.\"FacmNo\" = M.\"FacmNo\" AND M2.\"BormNo\" = M.\"BormNo\" THEN 'B3'";
		sql += "       	       WHEN CO.\"CustNo\" = M.\"CustNo\"  AND CO.\"LegalProg\" IN ('056','057','058','060') THEN 'C5'";
		sql += "       	     ELSE 'B1' END";
		sql += "    UNION";
		sql += "    SELECT \"Item\"";
		sql += "    	  ,NVL(\"AMT\",0) AS \"AMT\"";
		sql += "    FROM \"ovduLoan\"";
		sql += "    UNION";
		sql += "    SELECT \"Item\"";
		sql += "    	  ,NVL(\"AMT\",0) AS \"AMT\"";
		sql += "    FROM \"tempTotal\"";

		info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", (iYear * 100) + iMonth);
		query.setParameter("lyymm", lastMonth);
		return this.convertToMap(query);

	}

}