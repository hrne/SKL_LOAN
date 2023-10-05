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

/**
 * 
 * 20230905 User(舜雯)已確認 企業放款餘額限額 不需要判斷，顯示所有資料即可
 * 
 * 
 */
@Service
@Repository
/* 逾期放款明細 */
public class LM048ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 主要查詢SQL語法
	 */
	private String sqlMain() {
		String sqlMain = "	";
		sqlMain += "    WITH \"tmpData\" AS (";
		sqlMain += "        SELECT Cm.\"CustNo\"";
		sqlMain += "             , Cm.\"CustName\"";
		sqlMain += "             , Fm.\"ApplNo\"";
		sqlMain += "             , Fsa.\"MainApplNo\"";
		sqlMain += "             , Round(Nvl(";
		sqlMain += "                CASE";
		sqlMain += "                    WHEN Fsa.\"CustNo\" IS NOT NULL THEN Fsl.\"LineAmt\"";
		sqlMain += "                    ELSE Fm.\"LineAmt\"";
		sqlMain += "                END";
		sqlMain += "          , 0";
		sqlMain += "        ) / 1000) AS \"LineAmt\"";
		sqlMain += "             , Round(M.\"PrinBalance\" / 1000)                          AS \"PrinBalance\"";
		sqlMain += "             , M2.\"StoreRate\"";
		sqlMain += "             , Fm.\"MaturityDate\"";
		sqlMain += "             , M2.\"PrevPayIntDate\"";
		sqlMain += "             , Cg.\"CustNo\"                                            AS \"CustNoMain\"";
		sqlMain += "             , Decode(";
		sqlMain += "            Cg.\"CustName\", NULL";
		sqlMain += "         , '        '";
		sqlMain += "         , To_Char(Cg.\"CustName\" || '關係企業')";
		sqlMain += "        ) AS \"CustNameMain\"";
		sqlMain += "             , Cm.\"IndustryCode\"";
		sqlMain += "             , Substr(";
		sqlMain += "            Cm.\"IndustryCode\", 3";
		sqlMain += "         , 4";
		sqlMain += "        ) AS \"IndustryCode2\"";
		sqlMain += "             , Decode(";
		sqlMain += "            Fm.\"RecycleCode\", '1'";
		sqlMain += "         , 'V'";
		sqlMain += "         , '0'";
		sqlMain += "         , ' '";
		sqlMain += "        )               AS \"RecycleCode\"";
		sqlMain += "        FROM \"MonthlyFacBal\"  M";
		sqlMain += "        LEFT JOIN \"FacMain\"        Fm ON Fm.\"CustNo\" = M.\"CustNo\"";
		sqlMain += "                                  AND";
		sqlMain += "                                  Fm.\"FacmNo\" = M.\"FacmNo\"";
		sqlMain += "        LEFT JOIN \"FacShareAppl\"   Fsa ON Fsa.\"CustNo\" = Fm.\"CustNo\"";
		sqlMain += "                                        AND";
		sqlMain += "                                        Fsa.\"FacmNo\" = Fm.\"FacmNo\"";
		sqlMain += "                                        AND";
		sqlMain += "                                        Fsa.\"ApplNo\" = Fm.\"ApplNo\"";
		sqlMain += "        LEFT JOIN \"FacShareLimit\"  Fsl ON Fsa.\"KeyinSeq\" = 1";
		sqlMain += "                                         AND";
		sqlMain += "                                         Fsl.\"CustNo\" = Fsa.\"CustNo\"";
		sqlMain += "                                         AND";
		sqlMain += "                                         Fsl.\"FacmNo\" = Fsa.\"FacmNo\"";
		sqlMain += "                                         AND";
		sqlMain += "                                         Fsl.\"MainApplNo\" = Fsa.\"MainApplNo\"";
		sqlMain += "        LEFT JOIN \"CustMain\"       Cm ON Cm.\"CustNo\" = M.\"CustNo\"";
		sqlMain += "        LEFT JOIN \"ReltMain\"       Rm ON Rm.\"ReltUKey\" = Cm.\"CustUKey\"";
		sqlMain += "        LEFT JOIN \"CustMain\"       Cg ON Cg.\"CustNo\" = Rm.\"CustNo\"";
		sqlMain += "        LEFT JOIN (";
		sqlMain += "            SELECT DISTINCT M.\"YearMonth\"";
		sqlMain += "                          , M.\"CustNo\"";
		sqlMain += "                          , M.\"FacmNo\"";
		sqlMain += "                          , M.\"StoreRate\"";
		sqlMain += "                          , M2.\"PrevPayIntDate\"";
		sqlMain += "            FROM \"MonthlyLoanBal\" M";
		sqlMain += "            LEFT JOIN (";
		sqlMain += "                SELECT M.\"YearMonth\"";
		sqlMain += "                     , M.\"CustNo\"";
		sqlMain += "                     , M.\"FacmNo\"";
		sqlMain += "                     , MIN( M.\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
		sqlMain += "                FROM \"MonthlyLoanBal\" M";
		sqlMain += "                WHERE M.\"YearMonth\" = :yymm";
		sqlMain += "                  AND Trunc(M.\"PrevPayIntDate\" / 100) >= :yymm ";
		sqlMain += "                GROUP BY M.\"YearMonth\"";
		sqlMain += "                       , M.\"CustNo\"";
		sqlMain += "                       , M.\"FacmNo\"";
		sqlMain += "            ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sqlMain += "                    AND";
		sqlMain += "                    M2.\"FacmNo\" = M.\"FacmNo\"";
		sqlMain += "                    AND";
		sqlMain += "                    M2.\"YearMonth\" = M.\"YearMonth\"";
		sqlMain += "            WHERE M.\"YearMonth\" = :yymm";
		sqlMain += "              AND M.\"LoanBalance\" > 0 ";
		sqlMain += "        ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sqlMain += "                AND";
		sqlMain += "                M2.\"FacmNo\" = M.\"FacmNo\"";
		sqlMain += "        WHERE M.\"YearMonth\" = :yymm";
		sqlMain += "              AND";
		sqlMain += "              ( Trunc(Fm.\"RecycleDeadline\" / 100) >= :yymm ";
		sqlMain += "                OR";
		sqlMain += "                M2.\"PrevPayIntDate\" > 0 )";
		sqlMain += "              AND";
		sqlMain += "              M.\"EntCode\" IN (";
		sqlMain += "                  1";
		sqlMain += "              )";
		sqlMain += "    ), \"tmpData2\" AS (";
		sqlMain += "        SELECT SUM(\"LineAmt\")                   AS \"LineAmt\"";
		sqlMain += "             , SUM(\"PrinBalance\")               AS \"PrinBalance\"";
		sqlMain += "             , MAX(\"StoreRate\")                 AS \"StoreRate\"";
		sqlMain += "             , MAX(\"MaturityDate\")              AS \"MaturityDate\"";
		sqlMain += "             , MAX(\"PrevPayIntDate\")            AS \"PrevPayIntDate\"";
		sqlMain += "             , Nvl(";
		sqlMain += "            	\"MainApplNo\", \"ApplNo\"";
		sqlMain += "        		)       AS \"ApplNo\"";
		sqlMain += "             , \"CustNoMain\"";
		sqlMain += "             , \"CustNameMain\"";
		sqlMain += "        FROM \"tmpData\"";
		sqlMain += "        GROUP BY Nvl(";
		sqlMain += "            \"MainApplNo\", \"ApplNo\"";
		sqlMain += "        ) , \"CustNoMain\"";
		sqlMain += "          , \"CustNameMain\"";
		sqlMain += "    ), \"tmpData3\" AS (";
		sqlMain += "        SELECT Distinct \"CustNoMain\"";
		sqlMain += "             , \"CustNameMain\" ";
		sqlMain += "             , SUM(\"LineAmt\")               AS \"LineAmt\"";
		sqlMain += "        FROM \"tmpData2\"";
		sqlMain += "        GROUP BY \"CustNoMain\"";
		sqlMain += "    )";

		return sqlMain;
	}

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) {

		this.info("LM048ServiceImpl queryDetail ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += sqlMain();
		sql += "    SELECT Ci.\"IndustryCode\"";
		sql += "         , Ci.\"IndustryItem\"";
		sql += "         , Ci.\"IndustryRating\"";
		sql += "         , M.\"CustNo\"";
		sql += "         , M.\"CustName\"";
		sql += "         , M.\"RecycleCode\"";
		sql += "         , M2.\"LineAmt\"           AS \"LineAmt\"";
		sql += "         , M2.\"PrinBalance\"       AS \"LoanBal\"";
		sql += "         , Decode(";
		sql += "        M.\"RecycleCode\", 'V'";
		sql += "         , M2.\"LineAmt\" - M2.\"PrinBalance\"";
		sql += "         , 0";
		sql += "    )               AS \"AvailableBal\"";
		sql += "         , M.\"StoreRate\"";
		sql += "         , M.\"MaturityDate\"";
		sql += "         , M.\"PrevPayIntDate\"";
		sql += "         , Nvl(";
		sql += "        M.\"CustNoMain\", M.\"CustNo\"";
		sql += "    ) AS \"CustNoMain\"";
		sql += "         , Nvl(";
		sql += "        M.\"CustNameMain\", M.\"CustName\"";
		sql += "    ) AS \"CustNameMain\"";
		sql += "    FROM (";
		sql += "        SELECT DISTINCT Substr(";
		sql += "            \"IndustryCode\", 3";
		sql += "         , 4";
		sql += "        ) AS \"IndustryCode\"";
		sql += "                      , \"IndustryItem\"";
		sql += "                      , \"IndustryRating\"";
		sql += "        FROM \"CdIndustry\"";
		sql += "        WHERE \"IndustryRating\" IS NOT NULL";
		sql += "              AND";
		sql += "              \"MainType\" IS NOT NULL";
		sql += "    ) Ci";
		sql += "    LEFT JOIN \"tmpData\"   M ON M.\"IndustryCode2\" = Ci.\"IndustryCode\"";
		sql += "    LEFT JOIN \"tmpData2\"  M2 ON M2.\"ApplNo\" = M.\"ApplNo\"";
		sql += "    ORDER BY Ci.\"IndustryRating\"";
		sql += "           , Ci.\"IndustryCode\"";
		sql += "           , Nvl(";
		sql += "        M.\"CustNoMain\", 9999999";
		sql += "    )";
		sql += "           , Nvl(";
		sql += "        M.\"CustNo\", 9999999";
		sql += "    )";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);

		return this.convertToMap(query);
	}

	// 同一關係企業核貸總金額
	public List<Map<String, String>> groupLineAmt(int inputYearMonth, TitaVo titaVo) {

		this.info("LM048ServiceImpl groupLineAmt ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += sqlMain();
		sql += "    SELECT \"CustNoMain\" AS \"CustNoMain\" ";
		sql += "     	 , \"CustNameMain\" AS \"CustNameMain\" ";
		sql += "     	 , \"LineAmt\" AS \"ToTalLineAmt\" ";
		sql += "    FROM \"tmpData3\"";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);

		return this.convertToMap(query);

	}

	// 查詢放款總餘額和淨值
	public List<Map<String, String>> queryLoanBal(int inputYearMonth, TitaVo titaVo) {
		this.info("LM048ServiceImpl queryLoanBal ");

		this.info("yearMonth = " + inputYearMonth);

		int lyy12 = (inputYearMonth / 100 - 1) * 100 + 12;

		this.info("lyy12 = " + lyy12);

		String sql = " ";
		sql += "    SELECT \"Name\" ";
		sql += "         , SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "    FROM ( ";
		sql += "        SELECT 'LoanBal'              AS \"Name\" ";
		sql += "             , ROUND(SUM(\"PrinBalance\")/1000)     AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyFacBal\" ";
		sql += "        WHERE \"YearMonth\" = :yymm ";
		sql += "              AND ";
		sql += "              \"PrinBalance\" > 0 ";
		sql += "        UNION ";
		sql += "        SELECT 'LoanBal'          AS \"Name\" ";
		sql += "             , ROUND(SUM(\"LoanBal\") / 1000)     AS \"LoanBal\" ";
		sql += "        FROM \"MonthlyLM052AssetClass\" ";
		sql += "        WHERE \"YearMonth\" = :yymm ";
		sql += "              AND ";
		sql += "              \"AssetClassNo\" IN ( ";
		sql += "                  '61' ";
		sql += "                , '62' ";
		sql += "              ) ";
		sql += "        UNION ";
		sql += "        SELECT 'NetWorth'                 AS \"Name\" ";
		sql += "             , ROUND(SUM(\"StockHoldersEqt\")/1000)     AS \"LoanBal\" ";
		sql += "        FROM \"InnFundApl\" ";
		sql += "        WHERE \"AcDate\" = ( ";
		sql += "            SELECT MAX(\"AcDate\") ";
		sql += "            FROM \"InnFundApl\" ";
		sql += "            WHERE Trunc(\"AcDate\" / 100) = :lyy12 ";
		sql += "                  AND ";
		sql += "                  \"StockHoldersEqt\" > 0 ";
		sql += "        ) ";
		sql += "    ) ";
		sql += "    GROUP BY \"Name\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);
		query.setParameter("lyy12", lyy12);
		return this.convertToMap(query);
	}

	// 風險管理限額標準
	public List<Map<String, String>> riskLimit(int acdate, TitaVo titaVo) {
		this.info("LM048ServiceImpl riskLimit ");

		this.info("acdate = " + acdate);

		String sql = " ";
		sql += "  WITH \"tmp\" AS (";
		sql += "      SELECT";
		sql += "          *";
		sql += "      FROM";
		sql += "          \"CdComm\"";
		sql += "      WHERE";
		sql += "          \"CdType\" = '03'";
		sql += "          AND \"CdItem\" = '01'";
		sql += "          AND \"EffectDate\" <= :acdate ";
		sql += "  )";
		sql += "  SELECT";
		sql += "        JSON_VALUE(\"JsonFields\", '$.LimitRate11') * 100 AS \"LimitRateA1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan11') AS \"LimitLoanA1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate12') * 100 AS \"LimitRateA2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan12') AS \"LimitLoanA2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate13') * 100 AS \"LimitRateA3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan13') AS \"LimitLoanA3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate21') * 100 AS \"LimitRateB1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan21') AS  \"LimitLoanB1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate22') * 100 AS \"LimitRateB2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan22') AS \"LimitLoanB2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate23') * 100 AS \"LimitRateB3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan23') AS \"LimitLoanB3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate31') * 100 AS \"LimitRateC1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan31') AS \"LimitLoanC1\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate32') * 100 AS \"LimitRateC2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan32') AS \"LimitLoanC2\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate33') * 100 AS \"LimitRateC3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan33') AS \"LimitLoanC3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitRate43') * 100 AS \"LimitRateAB3\"";
		sql += "      , JSON_VALUE(\"JsonFields\", '$.LimitLoan43') AS \"LimitLoanAB3\"";
		sql += "  FROM";
		sql += "      \"tmp\"";
		sql += "  WHERE";
		sql += "      \"EffectDate\" = (";
		sql += "          SELECT";
		sql += "              MAX(\"EffectDate\")";
		sql += "          FROM";
		sql += "              \"tmp\"";
		sql += "      )";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("acdate", acdate);
		return this.convertToMap(query);
	}

}