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
	 * @param yearMonth  當西元年月
	 * @param lYearMonth 上西元年月
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findStatistics1(TitaVo titaVo, int yearMonth, int lYearMonth) throws Exception {
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
		sql += " 		 ,CASE ";
		sql += " 			WHEN M.\"GovProjectFlag\" IN ('C','Y') ";
		sql += " 			THEN 'Z' ";
		sql += " 			WHEN M.\"ClCode1\" IN (1,2) ";
		sql += " 			THEN 'C' ";
		sql += " 			WHEN M.\"ClCode1\" IN (3,4) ";
		sql += " 			THEN 'D' ";
		sql += " 		  END AS \"KIND\" ";
		sql += " 		 ,DECODE(REL.\"RptId\",NULL,'N','Y') AS \"RPTID\" ";
		sql += " 		 ,M.\"PrinBalance\" AS \"AMT\" ";
		sql += "   FROM \"MonthlyFacBal\" M ";
		sql += "   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\" ";
		sql += "   						  AND F.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "   LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\" ";
		sql += "   LEFT JOIN ( SELECT DISTINCT(TO_CHAR(DECODE(l.\"BusId\" , '-' , DECODE(\"RelId\" ,'-' ,l.\"RelId\"),l.\"BusId\")))  AS \"RptId\" ";
		sql += "               FROM \"LifeRelHead\" l ";
		sql += "               LEFT JOIN \"CustMain\" cm ON cm.\"CustId\" = TO_CHAR(DECODE(l.\"BusId\" , '-' , DECODE(\"RelId\" ,'-' ,l.\"RelId\"),l.\"BusId\"))  ";
		sql += "               WHERE l.\"RelWithCompany\" IN ('A','B') ";
		sql += "				AND l.\"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "				AND l.\"LoanBalance\" > 0 ";
		sql += "             ) REL ON REL.\"RptId\" = CM.\"CustId\" ";
		sql += "   WHERE M.\"YearMonth\" = :yymm  ";
		sql += "   	 AND M.\"PrinBalance\" > 0 )";
		sql += "   GROUP BY \"YearMonth\" ";
		sql += "           ,SUBSTR(\"AssetClass\",0,1) ";
		sql += "           ,\"KIND\"";
		sql += "           ,\"RPTID\"";
		sql += "	UNION ";
		sql += "	SELECT \"YearMonth\" AS \"YearMonth\" ";
		sql += "		  ,'999' AS \"AssetClass\" ";
		sql += "		  ,'L' AS \"KIND\" ";
		sql += "		  ,'N' AS \"RPTID\" ";
		sql += "		  ,\"LegalLoss\" AS \"AMT\" ";
		sql += "	FROM \"MonthlyLM052Loss\" ";
		sql += "	WHERE \"YearMonth\" = :lyymm ";
		sql += "   ORDER BY \"KIND\" ASC ";
		sql += "           ,\"RPTID\" DESC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		query.setParameter("lyymm", lYearMonth);
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
		// 擔保放款-折溢價
		sql += " SELECT 'tDisPreRemFees' AS \"Item\" ";
		sql += " 	   ,\"LoanBal\"  AS \"AMT\" ";
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '61' ";
		sql += " UNION  ";
		// 催收款項
		sql += " SELECT 'oDisPreRemFees' AS \"Item\" ";
		sql += " 	   ,\"LoanBal\"  AS \"AMT\" ";
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '62' ";
		sql += " UNION  ";
//        擔保放款-折溢價 
		sql += " SELECT  ";
		sql += "       'tDisPreRemLoss'					AS \"Item\" ";
		sql += "       ,\"StorageAmt\"  			AS \"Amt\"   ";// (61)擔保品溢折價
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '61' ";
		sql += " UNION  ";
		// 催收款項-折溢價與催收費用
		sql += " SELECT  ";
		sql += "       'oDisPreRemLoss'					AS \"Item\" ";
		sql += "       ,\"StorageAmt\"			AS \"Amt\"          "; // (62)折溢價與催收費用
		sql += " FROM \"MonthlyLM052AssetClass\"  ";
		sql += " WHERE \"YearMonth\" =  :yymm ";
		sql += "   AND \"AssetClassNo\" = '62' ";
		sql += " UNION  ";

		sql += " SELECT 'IntRecv' AS \"Item\" ";
		sql += " 	   ,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\"";
		sql += " WHERE \"AcNoCode\" IN ( '10240000000' ) "; // 應收利息
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'NTD' ";
		// 專案貸款
		sql += " UNION ";
		sql += " SELECT 'ProLoan' AS \"Item\" ";
		sql += " 	   ,SUM(\"PrinBalance\") AS \"AMT\" ";
		sql += " FROM \"MonthlyFacBal\" ";
		sql += " WHERE \"PrinBalance\" > 0 ";
		sql += "   AND \"YearMonth\" = :yymm ";
		sql += "   AND \"GovProjectFlag\" IN ('Y','C') ";

		// 服務課專案數字
		sql += " UNION ";
		sql += " SELECT 'ProAmt' AS \"Item\" ";
		sql += " 	   ,SUM(JSON_VALUE(\"JsonFields\",'$.LoanBal'))  AS \"AMT\" ";
		sql += " FROM \"CdComm\" ";
		sql += " WHERE \"CdType\" ='02'";
		sql += "   AND \"CdItem\" = '02'";
		sql += "   AND \"EffectDate\" =  :yymm * 100 + 1 ";
		// 放款總額 I13
		sql += " UNION ";
		sql += " SELECT 'TotalLoanAmt' AS \"Item\" ";
		sql += " 	   ,SUM(\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\" ";
		sql += " WHERE \"AcNoCode\" IN ( '10603','10604' ) ";
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'TOL' ";
		// 備呆P13
		sql += " UNION ";
		sql += " SELECT 'ApprovedLoss' AS \"Item\" ";
		sql += " 	   ,SUM(0-\"TdBal\")  AS \"AMT\" ";
		sql += " FROM \"CoreAcMain\" ";
		sql += " WHERE \"AcNoCode\" IN ( '10623','10624' ) ";
		sql += "   AND \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   AND \"CurrencyCode\" = 'TOL' ";
		// 利關人_職員數C16
		sql += " UNION ";
		sql += " SELECT 'RelEmpAmt' AS \"Item\" ";
		sql += "       ,SUM(\"PrinBalance\") AS \"AMT\" ";
		sql += " FROM ( SELECT b.* FROM (SELECT DISTINCT (\"CustNo\") ";
		sql += "	FROM \"LifeRelEmp\" l ";
		sql += "	LEFT JOIN \"CustMain\" cm ON cm.\"CustId\" = TO_CHAR(DECODE(l.\"EmpId\" , '-' , l.\"EmpId\", l.\"EmpId\")) ";
		sql += "	WHERE";
		sql += "	l.\"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "	AND l.\"LoanBalance\" > 0 ) a";
		sql += "	LEFT JOIN \"MonthlyFacBal\" b ON b.\"CustNo\" = a.\"CustNo\" ";
		sql += "	WHERE b.\"YearMonth\" = :yymm ) ";

		// 利關人金額C17
		sql += " UNION ";
		sql += " SELECT 'RelAmt' AS \"Item\" ";
		sql += " 	   ,SUM(\"PrinBalance\")  AS \"AMT\" ";
		sql += " FROM ( SELECT b.* FROM (SELECT DISTINCT (\"CustNo\") ";
		sql += "	FROM \"LifeRelHead\" l ";
		sql += "	LEFT JOIN \"CustMain\" cm ON cm.\"CustId\" = TO_CHAR(DECODE(l.\"BusId\" , '-' , DECODE(\"RelId\" ,'-' ,DECODE(\"HeadId\" ,'-' ,l.\"HeadId\",l.\"HeadId\"),l.\"RelId\"),l.\"BusId\")) ";
		sql += "	WHERE";
		sql += "	l.\"RelWithCompany\" IN ('A','B')";
		sql += "	AND l.\"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( :yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "	AND l.\"LoanBalance\" > 0 ) a";
		sql += "	LEFT JOIN \"MonthlyFacBal\" b ON b.\"CustNo\" = a.\"CustNo\" ";
		sql += "	WHERE b.\"YearMonth\" = :yymm ) ";

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

		String sql = "";
		sql += "SELECT * FROM (";
		sql += "	SELECT ( CASE";
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
		sql += "	  AND SUBSTR(M.\"AssetClass\",0,1) = '1'";
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