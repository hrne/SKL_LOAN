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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
/* 逾期放款明細 */
public class LM042ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	/**
	 * 查詢 LM042 統計數 工作表資料1
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * */
	public List<Map<String, String>> findStatistics1(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics1");

		this.info("yearMonth=" + yearMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" ";
		sql += " 	   ,SUBSTR(\"AssetClass\",0,1) AS \"AssetClass\" ";
		sql += " 	   ,\"KIND\" ";
		sql += "       ,\"RPTID\" ";
		sql += " 	   ,SUM(\"AMT\") AS \"AMT\" ";
		sql += " FROM ( ";
		sql += "   SELECT M.\"YearMonth\" ";
		sql += " 		 ,M.\"AssetClass\" ";
		sql += " 		 ,CASE";
		sql += " 			WHEN M.\"ClCode1\" IN (1,2)";
		sql += " 			 AND ( M.\"FacAcctCode\" = 340";
		sql += " 			  OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]'))";
		sql += " 			THEN 'Z'";
		sql += " 			WHEN M.\"ClCode1\" IN (1,2)";
		sql += " 			THEN 'C'";
		sql += " 			WHEN M.\"ClCode1\" IN (3,4)";
		sql += " 			THEN 'D'";
		sql += " 		  END AS \"KIND\"";
		sql += " 		 ,DECODE(REL.\"RptId\",NULL,'N','Y') AS \"RPTID\" ";
		sql += " 		 ,M.\"PrinBalance\" AS \"AMT\" ";
		sql += "   FROM \"MonthlyFacBal\" M ";
		sql += "   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "   						  AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "   LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\"";
		sql += "   LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
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
		sql += "             ) REL ON REL.\"RptId\" = CM.\"CustId\" ";
		sql += "   WHERE M.\"YearMonth\" = :yymm  ) ";
//		sql += "   	 AND SUBSTR(M.\"AssetClass\",0,1) <> '1' )";
		sql += "   GROUP BY \"YearMonth\" ";
		sql += "           ,SUBSTR(\"AssetClass\",0,1) ";
		sql += "           ,\"KIND\"";
		sql += "           ,\"RPTID\"";
		sql += "   ORDER BY \"KIND\" ASC";
		sql += "           ,\"RPTID\" DESC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}
	/**
	 * 查詢 LM042 統計數 工作表資料2
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * */
	public List<Map<String, String>> findStatistics2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics2");

		this.info("yearMonth=" + yearMonth);

		String sql = "";
		// 折溢價與催收費用
		sql += " SELECT 'DisPreRemFees' AS \"Item\" ";
		sql += " 	   ,SUM(\"DbAmt\" - \"CrAmt\")  AS \"AMT\" ";
		sql += " FROM \"AcMain\"";
		sql += " WHERE \"AcNoCode\" IN ( '10600304000' "; // 擔保放款-折溢價
		sql += "						,'10601301000' "; // 催收款項-法務費用
		sql += "						,'10601302000' ";// 催收款項-火險費用
		sql += "						,'10601304000') ";// 催收款項-折溢價
		sql += "   AND \"MonthEndYm\" = :yymm ";
		// 應收利息
		sql += " UNION ";
		sql += " SELECT 'IntRecv' AS \"Item\" ";
		sql += " 	   ,SUM(\"IntAmtAcc\") AS \"Amt\" ";// 應收利息
		sql += " FROM \"MonthlyLoanBal\" ";
		sql += " WHERE \"LoanBalance\" > 0 ";
		sql += "   AND \"YearMonth\" = :yymm ";
		// 專案貸款
		sql += " UNION ";
		sql += " SELECT 'ProLoan' AS \"Item\" ";
		sql += " 	   ,SUM(\"PrinBalance\") AS \"AMT\" ";
		sql += " FROM \"MonthlyFacBal\" ";
		sql += " WHERE \"PrinBalance\" > 0 ";
		sql += "   AND \"YearMonth\" = :yymm ";
		sql += "   AND \"ClCode1\" IN (1,2) ";
		sql += "   AND (\"FacAcctCode\" = 340";
		sql += "    OR REGEXP_LIKE(\"ProdNo\",'I[A-Z]'))";
		// 利關人_職員數
		sql += " UNION ";
		sql += " SELECT 'Stakeholder' AS \"Item\" ";
		sql += "       ,SUM(S0.\"LoanBal\") AS \"LoanBal\" ";
		sql += " FROM ( SELECT \"CustNo\" ";
		sql += "             , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyLoanBal\" ";
		sql += "        WHERE \"YearMonth\" = :yymm  ";
		sql += "          AND \"LoanBalance\" > 0 ";
		sql += "        GROUP BY \"CustNo\" ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = S0.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationSelf\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "             UNION ";
		sql += "             SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationFamily\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "             UNION ";
		sql += "             SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "             FROM \"RptRelationCompany\" ";
		sql += "             WHERE \"LAW005\" = '1' ";
		sql += "           ) S1 ON S1.\"RptId\" = CM.\"CustId\" ";
		sql += " WHERE CM.\"EmpNo\" IS NOT NULL ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}
	
	/**
	 * 查詢 LM042 統計數 工作表資料3
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * */
	public List<Map<String, String>> findStatistics3(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics3 ");

		this.info("yearMonth=" + yearMonth);

		String sql = "";
		sql += "SELECT * FROM (";
		sql += "	SELECT ( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			 END ) AS \"TYPE\"";
		sql += "		  ,( CASE";
		sql += "			   WHEN CDI.\"IndustryCode\" IS NOT NULL THEN '1'";
		sql += "			   WHEN F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			   ELSE '2'";
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
		sql += "	  AND SUBSTR(M.\"AssetClass\",0,1) = '1'";
		sql += "	GROUP BY( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			 END ) ";
		sql += "		  ,( CASE";
		sql += "			   WHEN CDI.\"IndustryCode\" IS NOT NULL THEN '1'";
		sql += "			   WHEN F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			   ELSE '2'";
		sql += "			 END ) ";
		sql += "    )RES ";
		sql += "	WHERE RES.\"KIND\" IN (1)";
		sql += "	ORDER BY RES.\"KIND\" ASC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 查詢 LM042 RBC工作表資料
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * @param lastYearMonth 上西元年月
	 * */
	public List<Map<String, String>> findRBC(TitaVo titaVo, int yearMonth, int lastYearMonth) throws Exception {
		this.info("lM042.findRBC ");

		// 去年 年月底
		int lEndYearMonth = (yearMonth / 100) * 100 + 12;

		this.info("lEndYearMonth=" + lEndYearMonth);
		this.info("lastYearMonth=" + lastYearMonth);
		this.info("yearMonth=" + yearMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" AS F0";
		sql += "	   ,\"LoanType\" AS F1";
		sql += "	   ,\"LoanItem\" AS F2";
		sql += "	   ,\"RelatedCode\" AS F3";
		sql += "	   ,\"LoanAmount\" AS F4";
		sql += "	   ,\"RiskFactor\" AS F5";
		sql += " FROM \"MonthlyLM042RBC\"";
		sql += " WHERE \"YearMonth\" IN ( :leyymm , :lyymm , :yymm )";
		sql += " ORDER BY \"YearMonth\" ASC ";
		sql += " 	 	 ,\"LoanType\" ASC ";
		sql += " 	 	 ,\"LoanItem\" ASC ";
		sql += " 	 	 ,\"RelatedCode\" ASC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("leyymm", lEndYearMonth);
		query.setParameter("lyymm", lastYearMonth);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

}