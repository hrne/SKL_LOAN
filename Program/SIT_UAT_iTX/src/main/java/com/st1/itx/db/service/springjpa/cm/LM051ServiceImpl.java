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
	 * @param groupNum  表格次序
	 * 
	 */

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, int groupNum) throws Exception {

		this.info("lM051.findAll ");
		this.info("yearMonth=" + yearMonth + "-" + groupNum);

//		// 0
//		String groupSelect1 = "	WHERE M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" = 0 ";
//		// 協
//		String groupSelect2 = "	WHERE M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" = 0 ";
//		// 催
//		String groupSelect3 = "	WHERE M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" IN (2,6,7) ";
//		// 催 + 協
//		String groupSelect4 = "	WHERE M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" IN (2,6,7) ";

		// 一般逾期
		String query1 = " ";
		query1 += "	 AND M.\"AcctCode\" <> 990";
		query1 += "  AND M.\"ProdNo\" NOT IN ('60','61','62')";
		query1 += "  AND M.\"OvduTerm\" > 0 ";
		// 協 * 1
		String query2 = " ";
		query2 += "	 AND M.\"AcctCode\" <> 990";
		query2 += "  AND M.\"ProdNo\" IN ('60','61','62')";
		query2 += "  AND M.\"OvduTerm\" > 0 ";
		// 協
		String query3 = " ";
		query3 += "	 AND M.\"AcctCode\" <> 990";
		query3 += "  AND M.\"ProdNo\" IN ('60','61','62')";
		query3 += "  AND M.\"OvduTerm\" = 0 ";
		// 催
		String query4 = " ";
		query4 += "  AND M.\"AcctCode\" = 990";
		query4 += "  AND M.\"ProdNo\" NOT IN ('60','61','62')";
		// 催 + 協
		String query5 = " ";
		query5 += "  AND M.\"AcctCode\" = 990";
		query5 += "  AND M.\"ProdNo\" IN ('60','61','62')";

		String sql = "SELECT M.\"CustNo\""; // F0
		sql += "			,M.\"FacmNo\""; // F1
		sql += "			,DECODE(M.\"AcSubBookCode\",' ',' ','00A','A') AS \"AcSubBookCode\""; // F2
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\""; // F3
		sql += "			,M.\"PrinBalance\""; // F4
		sql += "			,M.\"FacAcctCode\""; // F5
		sql += "			,M.\"OvduTerm\""; // F6
		sql += "			,M.\"CityCode\""; // F7
		sql += "			,M.\"PrevIntDate\""; // F8
		sql += "			,SUBSTR(M.\"AssetClass\",0,1) AS \"Class\""; // F9
		sql += "			,CD.\"Item\""; // F10
		sql += "			,NVL(M.\"Amount\",0) AS \"Amount\""; // F11
		sql += "			,(CASE ";
		sql += "				WHEN M.\"PrinBalance\" = 1 THEN '無擔保'";
		sql += "				WHEN M.\"PrinBalance\" > 1 THEN '有擔保'";
		sql += "			  ELSE '' END) ||";
		sql += "			 (CASE ";
		sql += "				WHEN M.\"Status\" = 0 THEN '--但債信不良(' || M.\"AssetNum\" || ')' ";
//		sql += "				WHEN M.\"PrinBalance\" > 1 THEN '有擔保'";
		sql += "			  ELSE '' END) ||";
		sql += "			 (CASE ";
		sql += "				WHEN M.\"OvduTerm\" > 0 AND M.\"OvduTerm\" <= 5 AND M.\"OvduDays\" > 30 THEN '--逾期'";
		sql += "				WHEN M.\"OvduDays\" = 0 THEN '--正常繳息'";
		sql += "				WHEN M.\"OvduDays\" > 0 AND M.\"OvduDays\" <= 30 THEN '--逾期未滿30日'";
		sql += "				WHEN M.\"OvduTerm\" > 6 AND M.\"OvduTerm\" <= 12 AND M.\"OvduDays\" > 30 THEN '--逾期7-12(' || M.\"AssetNum\" ||')'";
		sql += "				WHEN M.\"OvduTerm\" > 12 THEN '--逾期12月(' || M.\"AssetNum\" || ')'";
		sql += "				WHEN M.\"OvduDays\" = 0 AND M.\"ProdNo\" IN ('60','61','62') THEN '--協議後正常繳款(' || M.\"AssetNum\" || ')'";
		sql += "			  ELSE '' END) AS \"Memo\""; // F12
		sql += "			,M.\"ProdNo\""; // F13
		sql += "			,M.\"RenewCode\""; // F14
		sql += "			,M.\"LawAmount\""; // F15
		sql += "			,M.\"AssetClass\""; // F16
		sql += "			,M.\"Status\""; // F17
		sql += "		    ,M.\"OvduText\"";// F18
		sql += "	  FROM(SELECT M.\"CustNo\"";
		sql += "				 ,M.\"FacmNo\"";
		sql += "				 ,NVL(M.\"AcSubBookCode\",' ') AS \"AcSubBookCode\"";
		sql += "				 ,M.\"PrinBalance\"";
		sql += "				 ,M.\"FacAcctCode\"";
		sql += "				 ,M.\"OvduTerm\"";
		sql += "				 ,M.\"OvduDays\"";
		sql += "				 ,M.\"CityCode\"";
		sql += "				 ,M.\"PrevIntDate\"";
		sql += "				 ,NVL(L.\"LegalProg\",'000') AS \"LegalProg\"";
		sql += "				 ,L.\"Amount\"";
		sql += "				 ,L.\"Memo\"";
		sql += "				 ,M.\"RenewCode\"";
		sql += "				 ,M.\"LawAmount\"";
		sql += "				 ,M.\"AssetClass\"";
		sql += "				 ,M.\"Status\"";
		sql += "				 ,M.\"OvduText\"";
		sql += "				 ,M.\"ProdNo\"";
		sql += "				 ,M.\"AssetNum\"";
		sql += "		   FROM(SELECT M.\"CustNo\"";
		sql += "					  ,M.\"FacmNo\"";
		sql += "					  ,DECODE(M.\"AcSubBookCode\",'201','00A',' ') AS \"AcSubBookCode\"";
		sql += "					  ,M.\"PrinBalance\"";
		sql += "					  ,M.\"FacAcctCode\"";
		sql += "					  ,(CASE";
		sql += "						 WHEN M.\"OvduTerm\" > 5 OR M.\"OvduTerm\"= 0 THEN 99";
		sql += "						 ELSE M.\"OvduTerm\"";
		sql += "					   END) AS \"OvduTerm\"";
		sql += "					  ,(CASE";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" <= 5 AND M.\"OvduDays\" > 30 AND M.\"Status\" = 0 THEN '*協-' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"OvduDays\" =0 AND M.\"Status\" = 0 THEN '協' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduDays\" > 0 AND M.\"OvduDays\" < 30 AND M.\"Status\" = 0 THEN '協*' ";
		sql += "						 WHEN M.\"ProdNo\" NOT IN ('60','61','62') AND M.\"Status\" IN (2,6,7) THEN '催' ";
		sql += "						 WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"Status\" IN (2,6,7)  THEN '催協' ";
		sql += "						 ELSE ' '";
		sql += "					   END) AS \"OvduText\"";
		sql += "					  ,M.\"CityCode\"";
		sql += "					  ,M.\"PrevIntDate\"";
		sql += "					  ,M.\"RenewCode\"";
		sql += "					  ,M.\"LawAmount\"";
		sql += "					  ,M.\"AssetClass\"";
		sql += "					  ,SUBSTR(M.\"AssetClass\",0,1) AS \"AssetNum\"";
		sql += "				 	  ,M.\"Status\"";
		sql += "				 	  ,M.\"ProdNo\"";
		sql += "				      ,M.\"OvduDays\"";
		sql += "				FROM \"MonthlyFacBal\" M";
		sql += "				WHERE M.\"YearMonth\" =  :yymm ";
		sql += "				  AND M.\"PrinBalance\" > 0 ";
		// 有四種不同條件分四次
		switch (groupNum) {
		case 1:
			sql += query1;
			break;
		case 2:
			sql += query2;
			break;
		case 3:
			sql += query3;
			break;
		case 4:
			sql += query4;
			break;
		case 5:
			sql += query5;
			break;
		default:
			break;
		}
		sql += "				) M";
		sql += "			 	LEFT JOIN(SELECT * ";
		sql += "						  FROM(SELECT L.\"CustNo\"";
		sql += "									 ,L.\"FacmNo\"";
		sql += "									 ,L.\"LegalProg\"";
		sql += "									 ,L.\"Amount\"";
		sql += "									 ,L.\"Memo\"";
		sql += "									 ,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\", L.\"FacmNo\" ORDER BY L.\"TitaTxtNo\" DESC) AS SEQ";
		sql += "							   FROM \"CollLaw\" L";
		sql += "						  	   WHERE TRUNC(L.\"AcDate\" / 100) <= :yymm ) L";
		sql += "						  WHERE L.SEQ = 1) L ";
		sql += "				ON L.\"CustNo\" = M.\"CustNo\" AND L.\"FacmNo\" = M.\"FacmNo\" ) M";
		sql += "			LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "			LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = M.\"AcSubBookCode\"";
		sql += "			LEFT JOIN \"CdCode\" CD ON CD.\"Code\" = M.\"LegalProg\"";
		sql += "			AND CD.\"DefCode\" = 'LegalProg' ";
		sql += "			ORDER BY M.\"OvduTerm\",M.\"CustNo\",M.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", String.valueOf(yearMonth));
		return this.convertToMap(query);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param formNum   表格次序
	 * 
	 */

	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth, int formNum) throws Exception {

		this.info("lM051.findAll2");

		this.info("yearMonth=" + yearMonth + "-" + formNum);

		int lYear = yearMonth / 100;
		int lMonth = yearMonth % 100;

		if (lMonth == 1) {
			lYear = lYear - 1;
			lMonth = 12;
		}

		int outStandingYMD = (lYear * 10000) + (lMonth * 100) + 1;

		String sql = " ";
		if (formNum == 1) {
			sql += "SELECT * FROM (";
			sql += "	SELECT ( CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
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
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
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
			sql += "		   UNION";
			sql += "	      SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
			sql += "		  FROM \"AcMain\"";
			sql += "		  WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
			sql += "	  		AND \"MonthEndYm\" = :yymm ) ";
			sql += ")ORDER BY \"KIND\" ASC";
		} else if (formNum == 2) {
			sql += "SELECT * FROM (";
			sql += "	SELECT ( CASE";
			sql += "			   WHEN M.\"AcSubBookCode\" = '00A' THEN '1'";
			sql += "			   WHEN M.\"AcSubBookCode\" = '201' THEN 'A'";
			sql += "			   ELSE '99'";
			sql += "			 END ) AS \"ASBC\"";
			sql += "		  ,( CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
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
			sql += "	  AND M.\"PrevIntDate\"  >= :lyymmdd";
//			sql += "	  AND M.\"AssetClass\" IS NULL";
			sql += "	GROUP BY ( CASE";
			sql += "			     WHEN M.\"AcSubBookCode\" = '00A' THEN '1'";
			sql += "			     WHEN M.\"AcSubBookCode\" = '201' THEN 'A'";
			sql += "			     ELSE '99'";
			sql += "			   END )";
			sql += "		    ,( CASE";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
			sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
			sql += "			     ELSE '99'";
			sql += "			   END ))RES ";
			sql += "	WHERE RES.\"KIND\" IN (1,2)";
			sql += "	ORDER BY RES.\"KIND\" ASC";
		} else if (formNum == 3) {
			sql += "SELECT * FROM (";
			sql += "	SELECT ( CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "			   ELSE '99'";
			sql += "			 END ) AS \"TYPE\"";
			sql += "		  ,( CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
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
			sql += "	  AND M.\"PrevIntDate\"  >= :lyymmdd";
//			sql += "	  AND M.\"AssetClass\" IS NULL";
			sql += "	GROUP BY ( CASE";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "			     ELSE '99'";
			sql += "			   END ) ";
			sql += "		    ,( CASE";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN '3'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
			sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
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
		}

		return this.convertToMap(query);
	}

}