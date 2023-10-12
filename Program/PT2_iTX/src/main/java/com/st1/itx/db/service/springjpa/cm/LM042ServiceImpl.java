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
	 * 
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStatistics1(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics1");

		this.info("yearMonth=" + yearMonth);

		String sql = "";
		sql += " WITH FACBAL ( ";
		sql += "		SELECT ";
		sql += "				\"YearMonth\" ";
		sql += "				\"AssetClass\" ";
		sql += "				\"GovProjectFlag\" ";
		sql += "				\"ClCode1\" ";
		sql += "				\"BankRelationFlag\" ";
		sql += "				\"PrinBalance\"-\"LawAmount\"  AS \"PrinBalance\" ";
		sql += "   		FROM \"MonthlyFacBal\"  ";
		sql += "   		WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 	AND \"PrinBalance\" > 0 ";
		sql += " 		UNION ALL ";
		sql += "		SELECT ";
		sql += "				\"YearMonth\" ";
		sql += "				\"AssetClass\" ";
		sql += "				\"GovProjectFlag\" ";
		sql += "				\"ClCode1\" ";
		sql += "				\"BankRelationFlag\" ";
		sql += "				\"LawAmount\"  AS \"PrinBalance\" ";
		sql += "   		FROM \"MonthlyFacBal\"  ";
		sql += "   		WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 	AND \"PrinBalance\" > 0 ";
		sql += " ) ";
		sql += " SELECT \"YearMonth\" ";
		sql += " 	   ,\"KIND\" ";
		sql += "       ,\"RPTID\" ";
		sql += " 	   ,\"AssetClass\" AS \"AssetClass\" ";
		sql += " 	   ,SUM(\"AMT\") AS \"AMT\" ";
		sql += " FROM ( ";
		sql += "   SELECT \"YearMonth\" ";
		sql += " 		 ,\"AssetClass\" ";
		sql += " 		 ,CASE ";
		sql += " 			WHEN \"GovProjectFlag\" IN ('C','Y') ";
		sql += " 			THEN 'Z' ";
		sql += " 			WHEN \"ClCode1\" IN (1,2) ";
		sql += " 			THEN 'C' ";
		sql += " 			WHEN \"ClCode1\" IN (3,4) ";
		sql += " 			THEN 'D' ";
		sql += " 		  END AS \"KIND\" ";
		sql += " 		 ,CASE WHEN \"BankRelationFlag\" IN ('A','B') THEN  'Y' ELSE 'N' END  AS \"RPTID\" ";
		sql += " 		 ,\"PrinBalance\" AS \"AMT\" ";
		sql += "   FROM FACBAL  ";
		sql += "   WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 AND \"PrinBalance\" > 0 )";
		sql += "   GROUP BY \"YearMonth\" ";
		sql += "           ,\"AssetClass\" ";
		sql += "           ,\"KIND\" ";
		sql += "           ,\"RPTID\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 查詢 LM042 統計數 工作表資料2
	 * 
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStatistics2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics2");

		this.info("yearMonth=" + yearMonth);

		String sql = "";

		sql += " WITH FACBAL ( ";
		sql += "		SELECT ";
		sql += "				\"YearMonth\" ";
		sql += "				\"AssetClass\" ";
		sql += "				\"GovProjectFlag\" ";
		sql += "				\"ClCode1\" ";
		sql += "				\"BankRelationFlag\" ";
		sql += "				\"PrinBalance\"-\"LawAmount\"  AS \"PrinBalance\" ";
		sql += "   		FROM \"MonthlyFacBal\"  ";
		sql += "   		WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 	AND \"PrinBalance\" > 0 ";
		sql += " 		UNION ALL ";
		sql += "		SELECT ";
		sql += "				\"YearMonth\" ";
		sql += "				\"AssetClass\" ";
		sql += "				\"GovProjectFlag\" ";
		sql += "				\"ClCode1\" ";
		sql += "				\"BankRelationFlag\" ";
		sql += "				\"LawAmount\"  AS \"PrinBalance\" ";
		sql += "   		FROM \"MonthlyFacBal\"  ";
		sql += "   		WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 	AND \"PrinBalance\" > 0 ";
		sql += " ) ";
		sql += " SELECT \"Item\" ";
		sql += "       ,\"RPTID\" ";
		sql += " 	   ,\"AssetClass\" AS \"AssetClass\" ";
		sql += " 	   ,SUM(\"AMT\") AS \"AMT\" ";
		sql += " FROM ( ";
		sql += "   SELECT CASE ";
		sql += " 			WHEN \"GovProjectFlag\" IN ('C','Y') ";
		sql += " 			THEN 'Z' ";
		sql += " 			WHEN \"ClCode1\" IN (1,2) ";
		sql += " 			THEN 'C' ";
		sql += " 			WHEN \"ClCode1\" IN (3,4) ";
		sql += " 			THEN 'D' ";
		sql += " 		  END AS \"Item\" ";
		sql += " 		 ,CASE WHEN \"BankRelationFlag\" IN ('A','B','G') THEN  'Y' ELSE 'N' END  AS \"RPTID\" ";
		sql += " 		 ,\"AssetClass\"  AS \"AssetClass\" ";
		sql += " 		 ,\"PrinBalance\" AS \"AMT\" ";
		sql += "   FROM FACBAL  ";
		sql += "   WHERE \"YearMonth\" = :yymm  ";
		sql += "   	 AND \"PrinBalance\" > 0 )";
		// 擔保放款-折溢價C10
		sql += " SELECT '6' AS \"Item\" ";
		sql += " 		 ,'N'  AS \"RPTID\" ";
		sql += " 		 ,1  AS \"AssetClass\" ";
		sql += " 	   ,\"LoanBal\"  AS \"AMT\" ";
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '61' ";
		sql += " UNION  ";
		// 催收款項D10
		sql += " SELECT '6' AS \"Item\" ";
		sql += " 		 ,'N'  AS \"RPTID\" ";
		sql += " 		 ,2  AS \"AssetClass\" ";
		sql += " 	   ,\"LoanBal\"  AS \"AMT\" ";
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '62' ";
		sql += " UNION  ";
//        擔保放款-折溢價 
		sql += " SELECT  ";
		sql += "       '6'					AS \"Item\" ";
		sql += "		,'N'  AS \"RPTID\" ";
		sql += "		,1  AS \"AssetClass\" ";
		sql += "		,\"StorageAmt\"  			AS \"Amt\"   ";// (61)擔保品溢折價
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '61' ";
		sql += " UNION  ";
		// 催收款項-折溢價與催收費用
		sql += " SELECT  ";
		sql += "       '6'					AS \"Item\" ";
		sql += " 		,'N'  AS \"RPTID\" ";
		sql += " 		,2  AS \"AssetClass\" ";
		sql += "		,\"StorageAmt\"			AS \"Amt\"          "; // (62)折溢價與催收費用
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '62' ";
		sql += " UNION  ";
		// 應收利息 I13
		sql += " SELECT 'IntRecv' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,1  AS \"AssetClass\" ";
		sql += " 	   ,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\"";
		sql += " WHERE \"AcNoCode\" IN ( '10240000000' ) "; // 應收利息
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'NTD' ";
		// 專案貸款 
		sql += " UNION ";
		sql += " SELECT 'ProLoan' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,1  AS \"AssetClass\" ";
		sql += " 	   ,SUM(\"PrinBalance\") AS \"AMT\" ";
		sql += " FROM \"MonthlyFacBal\" ";
		sql += " WHERE \"PrinBalance\" > 0 ";
		sql += "   AND \"YearMonth\" = :yymm ";
		sql += "   AND \"GovProjectFlag\" IN ('Y','C') ";

		// 服務課專案數字
		sql += " UNION ";
		sql += " SELECT 'ProAmt' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,1  AS \"AssetClass\" ";
		sql += " 	   ,SUM(JSON_VALUE(\"JsonFields\",'$.LoanBal'))  AS \"AMT\" ";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" ='02'";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND \"EffectDate\" =  :yymm * 100 + 1 ";
		// 放款總額 I13
		sql += " UNION ";
		sql += " SELECT 'TotalLoanAmt' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,0  AS \"AssetClass\" ";
		sql += " 	   ,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\" ";
		sql += " WHERE \"AcNoCode\" IN ( '10603','10604' ) ";
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'TOL' ";
		// 備呆 P13
		sql += " UNION ";
		sql += " SELECT 'ApprovedLoss' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,0  AS \"AssetClass\" ";
		sql += " 	   ,SUM(0-\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\" ";
		sql += " WHERE \"AcNoCode\" IN ( '10623','10624' ) ";
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'TOL' ";

		// 利關人_職員數C16~G16
		sql += " UNION ";
		sql += " SELECT 'RelEmp' AS \"Item\" ";
		sql += " 		,'Y'  AS \"RPTID\" ";
		sql += " 		,\"AssetClass\"  AS \"AssetClass\" ";
		sql += " 	   	,SUM(\"PrinBalance\")  AS \"AMT\" ";
		sql += " FROM  \"MonthlyFacBal\"  ";
		sql += " WHERE \"YearMonth\" = :yymm  ";
		sql += "   AND \"BankRelationFlag\" IN ('G')  ";

		// 利關人金額C17~G17
		sql += " UNION ";
		sql += " SELECT 'RelAmt' AS \"Item\" ";
		sql += " 		,'Y'  AS \"RPTID\" ";
		sql += " 		,\"AssetClass\"  AS \"AssetClass\" ";
		sql += " 	   	,SUM(\"PrinBalance\")  AS \"AMT\" ";
		sql += " FROM  \"MonthlyFacBal\"  ";
		sql += " WHERE \"YearMonth\" = :yymm  ";
		sql += "   AND \"BankRelationFlag\" IN ('A','B')  ";

		// 科子目淨額O14 
		sql += " UNION ";
		sql += " SELECT 'NetLoanAmt' AS \"Item\" ";
		sql += " 		,''  AS \"RPTID\" ";
		sql += " 		,0  AS \"AssetClass\" ";
		sql += " 	   	,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\" ";
		sql += " WHERE \"AcNoCode\" IN ('10603','10604', '10623','10624' ) ";
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'TOL' ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 查詢 LM042 統計數 工作表資料3
	 * 
	 * @param titaVo
	 * @param yearMonth 當西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStatistics3(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("lM042.findStatistics3 ");

		this.info("yearMonth=" + yearMonth);

		String sql = " ";
		sql += "SELECT * FROM ( ";
		sql += "	SELECT ( CASE ";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
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
		sql += "	  AND M.\"AssetClass\" = '1'";
		sql += "	GROUP BY( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
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
	 * 
	 * @param titaVo
	 * @param yearMonth     當西元年月
	 * @param lastYearMonth 上西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findRBC(TitaVo titaVo, int yearMonth, int lastYearMonth) throws Exception {
		this.info("lM042.findRBC ");

		// 去年 年月底
		int lEndYearMonth = (yearMonth / 100) * 100 + 12;

		// 當月底
		this.info("lEndYearMonth=" + lEndYearMonth);
		// 上月底
		this.info("lastYearMonth=" + lastYearMonth);
		// 去年 年底日
		this.info("yearMonth=" + yearMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\"  ";
		sql += "	   ,\"LoanType\"   ";
		sql += "	   ,\"LoanItem\"   ";
		sql += "	   ,\"LoanAmount\" ";
		sql += "	   ,\"RiskFactor\" ";
		sql += "	   ,\"RiskFactorAmount\" ";
		sql += " FROM \"MonthlyLM042RBC\"";
		sql += " WHERE \"YearMonth\" IN ( :leyymm , :lyymm , :yymm )";
		sql += " ORDER BY \"YearMonth\" ASC ";
		sql += " 	 	 ,\"LoanType\" ASC ";
		sql += " 	 	 ,\"LoanItem\" ASC ";

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