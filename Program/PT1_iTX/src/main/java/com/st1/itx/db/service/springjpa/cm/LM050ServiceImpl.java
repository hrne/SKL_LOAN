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
public class LM050ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 查詢前一季淨值
	 * 
	 * @param acdate 西元年月日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> fnEquity(int acdate, TitaVo titaVo) throws Exception {
		

		this.info("lM050.Totalequity acdate=" + acdate);
		String sql = " ";
		sql += "     SELECT \"AvailableFunds\" AS \"AvailableFunds\"";
		sql += "           ,\"AcDate\"  AS \"AcDate\"";
		sql += "     FROM \"InnFundApl\" ";
		sql += "     WHERE \"AcDate\" = (";
		sql += "     	SELECT MAX(\"AcDate\") ";
		sql += "     	FROM \"InnFundApl\" ";
		sql += "     	WHERE \"AcDate\" <= :acdate";
		sql += " 	      AND \"AvailableFunds\" > 0 ";
		sql += "     )";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acdate", acdate);
		return this.convertToMap(query);

	}

	/**
	 * 查詢所有資料
	 * 
	 * @param iEntdy 西元年月日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> findAll(int iEntdy, TitaVo titaVo) throws Exception {

		int inputYearMonth = iEntdy / 100;

		this.info("LM050ServiceImpl findAll inputYearMonth=" + inputYearMonth);

		String sql = " ";
		sql += " SELECT CASE ";
		sql += "          WHEN NVL(S1.\"Rel\",' ') = ' ' ";
		sql += "          THEN '3' "; // -- 一般客戶
		sql += "          WHEN S1.\"Rel\" = 'N' ";
		sql += "          THEN '2' "; // -- 職員
		sql += "          WHEN S1.\"Rel\" = 'A' ";
		sql += "          THEN '1' "; // -- A
		sql += "        ELSE '3' "; // -- 保險業利害關係人放款管理辦法第3條利害關係人
		sql += "        END               AS \"RptType\" "; // F0
		sql += "      , CASE ";
		sql += "          WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "          THEN S0.\"CustNo\" ";
		sql += "        ELSE 0 END        AS \"CustNo\"  "; // F1
		sql += "      , CASE ";
		sql += "          WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "          THEN CM.\"CustName\" ";
		sql += "        ELSE N' ' END      AS \"CustName\"  "; // F2
		sql += "      , SUM(S0.\"LoanBal\") AS \"LoanBal\" "; // F3
		sql += "      ,decode(s1.\"BusTitle\",NULL,decode(\"RelName\",NULL";
		sql += "      ,'為本公司負責人' || \"HeadTitle\" ";
		sql += "      ,'為本公司負責人' ||'('|| \"HeadTitle\" || decode(\"RelTitle\",'本人',' ',\"HeadName\") || ')' || decode(\"RelTitle\",'本人',' ','之'||\"RelTitle\"))  ";
		sql += "      ,'該公司' ||\"BusTitle\" ||'('|| \"RelName\" || ')'||'為本公司'||\"HeadTitle\" ||'之'||\"RelTitle\" )AS 	\"Remark\"  ";
		sql += "      ,decode(S1.\"Rel\",'A',CM.\"EntCode\",0) AS \"EntCode\"";
		sql += " FROM ( SELECT \"CustNo\" ";
		sql += "             , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyLoanBal\" ";
		sql += "        WHERE \"YearMonth\" = :inputYearMonth  ";
		sql += "          AND \"LoanBalance\" > 0 ";
		sql += "        GROUP BY \"CustNo\" ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = S0.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT ";
		sql += "             decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadName\",\"RelName\"),\"BusName\")as \"CustName\"  ";
		sql += "             ,to_char(decode(\"BusId\",'-',decode(\"RelId\",'-',\"HeadId\",\"RelId\"),\"BusId\"))as \"RptId\"  ";
		sql += "             ,\"RelWithCompany\" as \"Rel\"";
		sql += "             ,\"HeadName\" ";
		sql += "             ,\"HeadTitle\" ";
		sql += "             ,\"RelName\" ";
		sql += "             ,\"RelTitle\" ";
		sql += "             ,\"BusTitle\" ";
		sql += "             FROM \"LifeRelHead\" ";
		sql += "             WHERE \"RelWithCompany\"IN ('A','B') ";
		sql += "             AND \"AcDate\" = (SELECT MAX(\"AcDate\") FROM \"LifeRelHead\" WHERE TRUNC(\"AcDate\" / 100 ) = :inputYearMonth )";
		sql += "             AND \"LoanBalance\" > 0 ";
		sql += "             UNION ";
		sql += "             SELECT \"EmpName\" AS \"CustName\" ";
		sql += "              ,TO_CHAR(\"EmpId\") AS \"RptId\" ";
		sql += "                      ,'N' AS \"Rel\"  ";
		sql += "              ,NULL AS \"HeadName\"  ";
		sql += "              ,NULL AS \"HeadTitle\"  ";
		sql += "              ,NULL AS \"RelName\"  ";
		sql += "              ,NULL AS \"RelTitle\"  ";
		sql += "              ,NULL AS \"BusTitle\"  ";
		sql += "             FROM \"LifeRelEmp\" ";
		sql += "             WHERE \"AcDate\" = (SELECT MAX(\"AcDate\") FROM \"LifeRelEmp\" WHERE TRUNC(\"AcDate\" / 100 ) = :inputYearMonth )";
		sql += "           ) S1 ON S1.\"RptId\" = CM.\"CustId\" ";
		sql += " GROUP BY CASE ";
		sql += "            WHEN NVL(S1.\"Rel\",' ') = ' ' ";
		sql += "            THEN '3' "; // -- 一般客戶
		sql += "            WHEN S1.\"Rel\" ='N' ";
		sql += "            THEN '2' "; // -- 職員
		sql += "          WHEN S1.\"Rel\" = 'A' ";
		sql += "          THEN '1' "; // -- A
		sql += "          ELSE '3' "; // -- 保險業利害關係人放款管理辦法第3條利害關係人
		sql += "          END ";
		sql += "        , CASE ";
		sql += "            WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "            THEN S0.\"CustNo\" ";
		sql += "          ELSE 0 END ";
		sql += "        , CASE ";
		sql += "            WHEN NVL(S1.\"RptId\",' ') != ' ' ";
		sql += "            THEN CM.\"CustName\" ";
		sql += "          ELSE N' ' END ";
		sql += "      ,decode(s1.\"BusTitle\",NULL,decode(\"RelName\",NULL";
		sql += "      ,'為本公司負責人' || \"HeadTitle\" ";
		sql += "      ,'為本公司負責人' ||'('|| \"HeadTitle\" || decode(\"RelTitle\",'本人',' ',\"HeadName\")|| ')' ||decode(\"RelTitle\",'本人',' ','之'||\"RelTitle\"))  ";
		sql += "      ,'該公司' ||\"BusTitle\" ||'('|| \"RelName\" || ')'||'為本公司'||\"HeadTitle\" ||'之'||\"RelTitle\")  ";
		sql += "      ,decode(S1.\"Rel\",'A',CM.\"EntCode\",0) ";
		sql += " ORDER BY \"RptType\" ";
		sql += "        , \"LoanBal\" DESC  ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);
		return this.convertToMap(query);

	}

	/**
	 * 放款折溢價與催收費用的合計
	 * 
	 * @param iEntdy 西元年月日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> findAmtTotal(int iEntdy, TitaVo titaVo) throws Exception {

		int inputYearMonth = iEntdy / 100;

		this.info("LM050ServiceImpl findAll inputYearMonth=" + inputYearMonth);

		String sql = " ";
		sql += " select  sum(\"LoanBal\") as \"LoanBal\"";
		sql += " from \"MonthlyLM052AssetClass\"";
		sql += " where \"YearMonth\" = :inputYearMonth";
		sql += "   and \"AssetClassNo\" in ('61','62')";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);
		return this.convertToMap(query);

	}

}