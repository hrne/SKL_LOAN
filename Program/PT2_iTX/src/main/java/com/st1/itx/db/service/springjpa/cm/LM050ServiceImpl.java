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
		sql += "     SELECT \"StockHoldersEqt\" AS \"StockHoldersEqt\"";
		sql += "           ,\"AcDate\"  AS \"AcDate\"";
		sql += "     FROM \"InnFundApl\" ";
		sql += "     WHERE \"AcDate\" = (";
		sql += "     	SELECT MAX(\"AcDate\") ";
		sql += "     	FROM \"InnFundApl\" ";
		sql += "     	WHERE \"AcDate\" <= :acdate";
		sql += " 	      AND \"StockHoldersEqt\" > 0 ";
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
	 * @param iEntdy  西元年月日
	 * @param rptType 關係人分群記號(1:負責人,2:職員,3:一般客戶)
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> findAll(int iEntdy, String rptType, TitaVo titaVo) throws Exception {

		int inputYearMonth = iEntdy / 100;

		this.info("LM050ServiceImpl findAll inputYearMonth=" + inputYearMonth);

		String sql = " ";
		sql += " with \"Main\" as (";
		sql += " SELECT ";
		sql += " row_number()over (";
		sql += "     partition by S0.\"CustNo\" ";
		sql += "     order by S1.\"Rel\") as \"Seq\",";
		sql += " CASE";
		sql += "     WHEN Nvl(";
		sql += "         S1.\"Rel\", ' '";
		sql += "     ) = ' '         THEN '3'";
		sql += "     WHEN S1.\"Rel\" = 'N'                  THEN '2'";
		sql += "     WHEN S1.\"Rel\" IN ('A','B')                  THEN '1'";
		sql += "     ELSE '3'";
		sql += " END                                                                                                                                                                   AS \"RptType\"";
		sql += "      , CASE";
		sql += "     WHEN Nvl(";
		sql += "         S1.\"RptId\", ' '";
		sql += "     ) != ' ' THEN S0.\"CustNo\"";
		sql += "     ELSE 0";
		sql += " END                                                                                                                                                                                                                                                                    AS \"CustNo\"";
		sql += "      , CASE";
		sql += "     WHEN Nvl(";
		sql += "         S1.\"RptId\", ' '";
		sql += "     ) != ' ' THEN Cm.\"CustName\"";
		sql += "     ELSE N' '";
		sql += " END                                                                                                                                                                                                                                                               AS \"CustName\"";
		sql += "      , S0.\"LoanBal\"                                                                                                                                                                                                                                                                                                                                               AS \"LoanBal\"";
		sql += "      , Decode(";
		sql += "     S1.\"BusTitle\", NULL";
		sql += "      , Decode(";
		sql += "         \"RelName\", NULL";
		sql += "      , '為本公司負責人' || \"HeadTitle\"";
		sql += "      , '為本公司負責人' || '(' || \"HeadTitle\" || Decode(";
		sql += "             \"RelTitle\", '本人'";
		sql += "      , ' '";
		sql += "      , \"HeadName\"";
		sql += "         ) || ')' || Decode(";
		sql += "             \"RelTitle\", '本人'";
		sql += "      , ' '";
		sql += "      , '之' || \"RelTitle\"";
		sql += "         )";
		sql += "     ), '該公司' || \"BusTitle\" || '(' || \"RelName\" || ')' || '為本公司' || \"HeadTitle\" || '之' || \"RelTitle\"";
		sql += " )                                               AS \"Remark\"";
		sql += "      , Decode(";
		sql += "     S1.\"Rel\", 'A'";
		sql += "      , Cm.\"EntCode\"";
		sql += "      , 0";
		sql += " )                                                                                                                                                                                                                                                                                                                             AS \"EntCode\"";
		sql += " FROM (";
		sql += "     SELECT \"CustNo\"";
		sql += "          , SUM(\"LoanBalance\") AS \"LoanBal\"";
		sql += "     FROM \"MonthlyLoanBal\"";
		sql += "     WHERE \"YearMonth\" = :inputYearMonth";
		sql += "           AND";
		sql += "           \"LoanBalance\" > 0";
		sql += "     GROUP BY \"CustNo\"";
		sql += " )                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 S0";
		sql += " LEFT JOIN \"CustMain\"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Cm ON Cm.\"CustNo\" = S0.\"CustNo\"";
		sql += " LEFT JOIN (";
		sql += "     SELECT Decode(";
		sql += "         \"BusId\", '-'";
		sql += "      , Decode(";
		sql += "             \"RelId\", '-'";
		sql += "      , \"HeadName\"";
		sql += "      , \"RelName\"";
		sql += "         ), \"BusName\"";
		sql += "     )                          AS \"CustName\"";
		sql += "          , To_Char(";
		sql += "         Decode(";
		sql += "             \"BusId\", '-'";
		sql += "              , Decode(";
		sql += "                 \"RelId\", '-'";
		sql += "      , \"HeadId\"";
		sql += "      , \"RelId\"";
		sql += "             ), \"BusId\"";
		sql += "         )";
		sql += "     )                       AS \"RptId\"";
		sql += "          , \"RelWithCompany\"                                                                                AS \"Rel\"";
		sql += "          , \"HeadName\"";
		sql += "          , \"HeadTitle\"";
		sql += "          , \"RelName\"";
		sql += "          , \"RelTitle\"";
		sql += "          , \"BusTitle\"";
		sql += "     FROM \"LifeRelHead\"";
		sql += "     WHERE \"RelWithCompany\" IN (";
		sql += "         'A'";
		sql += "       , 'B'";
		sql += "     )";
		sql += "           AND";
		sql += "           \"AcDate\" = (";
		sql += "               SELECT MAX(\"AcDate\")";
		sql += "               FROM \"LifeRelHead\"";
		sql += "               WHERE Trunc(\"AcDate\" / 100) = :inputYearMonth";
		sql += "           )";
		sql += "           AND";
		sql += "           \"LoanBalance\" > 0";
		sql += "     UNION";
		sql += "     SELECT \"EmpName\"            AS \"CustName\"";
		sql += "          , To_Char(\"EmpId\")     AS \"RptId\"";
		sql += "          , 'N'                  AS \"Rel\"";
		sql += "          , NULL                 AS \"HeadName\"";
		sql += "          , NULL                 AS \"HeadTitle\"";
		sql += "          , NULL                 AS \"RelName\"";
		sql += "          , NULL                 AS \"RelTitle\"";
		sql += "          , NULL                 AS \"BusTitle\"";
		sql += "     FROM \"LifeRelEmp\"";
		sql += "     WHERE \"AcDate\" = (";
		sql += "         SELECT MAX(\"AcDate\")";
		sql += "         FROM \"LifeRelEmp\"";
		sql += "         WHERE Trunc(\"AcDate\" / 100) = :inputYearMonth";
		sql += "     )";
		sql += " ) S1 ON S1.\"RptId\" = Cm.\"CustId\"";
		sql += " ORDER BY \"RptType\"";
		sql += "        , \"LoanBal\" DESC";
		sql += " )";

		// 負責人(名單)
		if ("1".equals(rptType)) {
			sql += " SELECT";
			sql += "     \"RptType\"";
			sql += "     , \"CustNo\"";
			sql += "     , \"CustName\"";
			sql += "     , \"LoanBal\"";
			sql += "     , \"Remark\"";
			sql += "     , \"EntCode\"";
			sql += " FROM";
			sql += "     \"Main\"";
			sql += " WHERE";
			sql += "     \"Seq\" = 1";
			sql += "     AND \"RptType\" = '1'";
		}
		// 職員(放款總額)
		if ("2".equals(rptType)) {
			sql += " SELECT";
			sql += "     SUM(\"LoanBal\") AS \"LoanBal\"";
			sql += " FROM";
			sql += "     \"Main\" m";
			sql += " WHERE";
			sql += "     \"Seq\" = 1";
			sql += "     AND \"RptType\" = '2'";
		}
		// 一般客戶(放款總額)
		if ("3".equals(rptType)) {
			sql += " SELECT";
			sql += "     SUM(\"LoanBal\") AS \"LoanBal\"";
			sql += " FROM";
			sql += "     \"Main\" m";
			sql += " WHERE";
			sql += "     \"Seq\" = 1";
			sql += "     AND \"RptType\" = '3'";
		}
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