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

	public List<Map<String, String>> queryDetail(int inputYearMonth, TitaVo titaVo) {

		this.info("LM048ServiceImpl queryDetail ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += "    WITH \"tmpData\" AS (";
		sql += "        SELECT Cm.\"CustNo\"";
		sql += "             , Cm.\"CustName\"";
		sql += "             , Fm.\"ApplNo\"";
		sql += "             , Fsa.\"MainApplNo\"";
		sql += "             , Round(Nvl(";
		sql += "                CASE";
		sql += "                    WHEN Fsa.\"CustNo\" IS NOT NULL THEN Fsl.\"LineAmt\"";
		sql += "                    ELSE Fm.\"LineAmt\"";
		sql += "                END";
		sql += "          , 0";
		sql += "        ) / 1000) AS \"LineAmt\"";
		sql += "             , Round(M.\"PrinBalance\" / 1000)                          AS \"PrinBalance\"";
		sql += "             , M2.\"StoreRate\"";
		sql += "             , Fm.\"MaturityDate\"";
		sql += "             , M2.\"PrevPayIntDate\"";
		sql += "             , Cg.\"CustNo\"                                            AS \"CustNoMain\"";
		sql += "             , Decode(";
		sql += "            Cg.\"CustName\", NULL";
		sql += "         , '        '";
		sql += "         , To_Char(Cg.\"CustName\" || '���Y���~')";
		sql += "        ) AS \"CustNameMain\"";
		sql += "             , Cm.\"IndustryCode\"";
		sql += "             , Substr(";
		sql += "            Cm.\"IndustryCode\", 3";
		sql += "         , 4";
		sql += "        ) AS \"IndustryCode2\"";
		sql += "             , Decode(";
		sql += "            Fm.\"RecycleCode\", '1'";
		sql += "         , 'V'";
		sql += "         , '0'";
		sql += "         , ' '";
		sql += "        )               AS \"RecycleCode\"";
		sql += "        FROM \"MonthlyFacBal\"  M";
		sql += "        LEFT JOIN \"FacMain\"        Fm ON Fm.\"CustNo\" = M.\"CustNo\"";
		sql += "                                  AND";
		sql += "                                  Fm.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        LEFT JOIN \"FacShareAppl\"   Fsa ON Fsa.\"CustNo\" = Fm.\"CustNo\"";
		sql += "                                        AND";
		sql += "                                        Fsa.\"FacmNo\" = Fm.\"FacmNo\"";
		sql += "                                        AND";
		sql += "                                        Fsa.\"ApplNo\" = Fm.\"ApplNo\"";
		sql += "        LEFT JOIN \"FacShareLimit\"  Fsl ON Fsa.\"KeyinSeq\" = 1";
		sql += "                                         AND";
		sql += "                                         Fsl.\"CustNo\" = Fsa.\"CustNo\"";
		sql += "                                         AND";
		sql += "                                         Fsl.\"FacmNo\" = Fsa.\"FacmNo\"";
		sql += "                                         AND";
		sql += "                                         Fsl.\"MainApplNo\" = Fsa.\"MainApplNo\"";
		sql += "        LEFT JOIN \"CustMain\"       Cm ON Cm.\"CustNo\" = M.\"CustNo\"";
		sql += "        LEFT JOIN \"ReltMain\"       Rm ON Rm.\"ReltUKey\" = Cm.\"CustUKey\"";
		sql += "        LEFT JOIN \"CustMain\"       Cg ON Cg.\"CustNo\" = Rm.\"CustNo\"";
		sql += "        LEFT JOIN (";
		sql += "            SELECT DISTINCT M.\"YearMonth\"";
		sql += "                          , M.\"CustNo\"";
		sql += "                          , M.\"FacmNo\"";
		sql += "                          , M.\"StoreRate\"";
		sql += "                          , M2.\"PrevPayIntDate\"";
		sql += "            FROM \"MonthlyLoanBal\" M";
		sql += "            LEFT JOIN (";
		sql += "                SELECT M.\"YearMonth\"";
		sql += "                     , M.\"CustNo\"";
		sql += "                     , M.\"FacmNo\"";
		sql += "                     , MIN(";
		sql += "                    CASE";
		sql += "                        WHEN Trunc(M.\"PrevPayIntDate\" / 100) >= :yymm THEN M.\"PrevPayIntDate\"";
		sql += "                        ELSE 0";
		sql += "                    END";
		sql += "                ) AS \"PrevPayIntDate\"";
		sql += "                FROM \"MonthlyLoanBal\" M";
		sql += "                WHERE M.\"YearMonth\" = :yymm";
		sql += "                GROUP BY M.\"YearMonth\"";
		sql += "                       , M.\"CustNo\"";
		sql += "                       , M.\"FacmNo\"";
		sql += "            ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                    AND";
		sql += "                    M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                    AND";
		sql += "                    M2.\"YearMonth\" = M.\"YearMonth\"";
		sql += "            WHERE M.\"YearMonth\" = :yymm";
		sql += "        ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                AND";
		sql += "                M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        WHERE M.\"YearMonth\" = :yymm";
		sql += "              AND";
		sql += "              ( ( M2.\"PrevPayIntDate\" = 0";
		sql += "                  AND";
		sql += "                  Trunc(Fm.\"RecycleDeadline\" / 100) >= :yymm )";
		sql += "                OR";
		sql += "                M2.\"PrevPayIntDate\" > 0 )";
		sql += "              AND";
		sql += "              M.\"EntCode\" IN (";
		sql += "                  1";
		sql += "              )";
		sql += "    ), \"tmpData2\" AS (";
		sql += "        SELECT SUM(\"LineAmt\")                   AS \"LineAmt\"";
		sql += "             , SUM(\"PrinBalance\")               AS \"PrinBalance\"";
		sql += "             , MAX(\"StoreRate\")                 AS \"StoreRate\"";
		sql += "             , MAX(\"MaturityDate\")              AS \"MaturityDate\"";
		sql += "             , MAX(\"PrevPayIntDate\")            AS \"PrevPayIntDate\"";
		sql += "             , Nvl(";
		sql += "            \"MainApplNo\", \"ApplNo\"";
		sql += "        )       AS \"ApplNo\"";
		sql += "        FROM \"tmpData\"";
		sql += "        GROUP BY Nvl(";
		sql += "            \"MainApplNo\", \"ApplNo\"";
		sql += "        )";
		sql += "    )";
		sql += "    SELECT Ci.\"IndustryCode\"";
		sql += "         , Ci.\"IndustryItem\"";
		sql += "         , Ci.\"IndustryRating\"";
		sql += "         , M.\"CustNo\"";
		sql += "         , M.\"CustName\"";
		sql += "         , M.\"RecycleCode\"";
		sql += "         , M2.\"LineAmt\"                                                                  AS \"LineAmt\"";
		sql += "         , M2.\"PrinBalance\"                                                              AS \"LoanBal\"";
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

	public List<Map<String, String>> groupLineAmt(int inputYearMonth, TitaVo titaVo) {

		this.info("LM048ServiceImpl groupLineAmt ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += "    SELECT M.\"CustNoMain\" AS \"CustNoMain\" ";
		sql += "         , M.\"CustNameMain\" AS \"CustNameMain\" ";
		sql += "     	 , SUM(M.\"LineAmt\") AS \"ToTalLineAmt\" ";
		sql += "    FROM ( ";
		sql += "    SELECT DISTINCT Cm.\"CustNo\" ";
		sql += "         , Cm.\"CustName\" ";
		sql += "         , Round( NVL(  ";
		sql += "         		CASE";
		sql += "         		  WHEN Fsa.\"CustNo\" IS NOT NULL ";
		sql += "         		  THEN Fsl.\"LineAmt\"";
		sql += "         		  ELSE Fm.\"LineAmt\"";
		sql += "         		END , 0 ) / 1000 ";
		sql += "         )      AS \"LineAmt\" ";
		sql += "         , Cg.\"CustNo\"                   AS \"CustNoMain\" ";
		sql += "         , Cg.\"CustName\"                   AS \"CustNameMain\" ";
		sql += "         , Cm.\"IndustryCode\" ";
		sql += "         , Substr( ";
		sql += "        Cm.\"IndustryCode\", 3 ";
		sql += "         , 4 ";
		sql += "    ) AS \"IndustryCode2\" ";
		sql += "    FROM \"MonthlyFacBal\"  M ";
		sql += "    LEFT JOIN \"FacMain\"        Fm ON Fm.\"CustNo\" = M.\"CustNo\" ";
		sql += "                                 AND Fm.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "    LEFT JOIN \"FacShareAppl\" Fsa ON Fsa.\"CustNo\" = Fm.\"CustNo\" ";
		sql += "                                  AND Fsa.\"FacmNo\" = Fm.\"FacmNo\" ";
		sql += "                                  AND Fsa.\"ApplNo\" = Fm.\"ApplNo\" ";
		sql += "    LEFT JOIN \"FacShareLimit\" Fsl ON Fsa.\"KeyinSeq\" = 1 ";
		sql += "                                  AND Fsa.\"CustNo\" = Fsa.\"CustNo\" ";
		sql += "                                  AND Fsa.\"FacmNo\" = Fsa.\"FacmNo\" ";
		sql += "                                  AND Fsa.\"MainApplNo\" = Fsa.\"MainApplNo\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cm ON Cm.\"CustNo\" = M.\"CustNo\" ";
		sql += "    LEFT JOIN \"ReltMain\"       Rm ON Rm.\"ReltUKey\" = Cm.\"CustUKey\" ";
		sql += "    LEFT JOIN \"CustMain\"       Cg ON Cg.\"CustNo\" = Rm.\"CustNo\" ";
		sql += "    LEFT join ( SELECT DISTINCT M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , M.\"StoreRate\" ";
		sql += "                              , M2.\"PrevPayIntDate\"";
		sql += "                FROM \"MonthlyLoanBal\" M ";
		sql += "                LEFT JOIN (";
		sql += "   						SELECT  M.\"YearMonth\" ";
		sql += "                              , M.\"CustNo\" ";
		sql += "                              , M.\"FacmNo\" ";
		sql += "                              , MIN(M.\"PrevPayIntDate\") AS \"PrevPayIntDate\" ";
		sql += "               		    FROM \"MonthlyLoanBal\" M ";
		sql += "                		WHERE M.\"YearMonth\" = :yymm  ";
		sql += "                		GROUP BY M.\"YearMonth\"";
		sql += "                				,M.\"CustNo\"";
		sql += "                				,M.\"FacmNo\"";
		sql += "                ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                	AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                	AND M2.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                WHERE M.\"YearMonth\" = :yymm  ";
		sql += "    ) m2 on m2.\"CustNo\"=m.\"CustNo\"  and  m2.\"FacmNo\" = m.\"FacmNo\" ";
		sql += "    where m.\"YearMonth\"= :yymm ";
		sql += "    and trunc(m2.\"PrevPayIntDate\"/100) >=  :yymm ";
		sql += "    and m.\"EntCode\" IN (1)  ";
		sql += "    and rm.\"CustNo\" is not null  ";
		sql += "    ) m ";
		sql += "    Group By M.\"CustNoMain\" ";
		sql += "           , M.\"CustNameMain\" ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("yymm", inputYearMonth);

		return this.convertToMap(query);

	}

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
	
	
	public List<Map<String, String>> riskLimit(int acdate, TitaVo titaVo) {
		this.info("LM048ServiceImpl riskLimit ");

		this.info("acdate = " + acdate);


		String sql = " ";
		sql+="  WITH \"tmp\" AS (";
		sql+="      SELECT";
		sql+="          *";
		sql+="      FROM";
		sql+="          \"CdComm\"";
		sql+="      WHERE";
		sql+="          \"CdType\" = '03'";
		sql+="          AND \"CdItem\" = '01'";
		sql+="          AND \"EffectDate\" <= :acdate ";
		sql+="  )";
		sql+="  SELECT";
		sql+="        JSON_VALUE(\"JsonFields\", '$.LimitRate11') * 100 AS \"LimitRateA1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan11') AS \"LimitLoanA1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate12') * 100 AS \"LimitRateA2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan12') AS \"LimitLoanA2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate13') * 100 AS \"LimitRateA3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan13') AS \"LimitLoanA3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate21') * 100 AS \"LimitRateB1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan21') AS  \"LimitLoanB1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate22') * 100 AS \"LimitRateB2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan22') AS \"LimitLoanB2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate23') * 100 AS \"LimitRateB3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan23') AS \"LimitLoanB3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate31') * 100 AS \"LimitRateC1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan31') AS \"LimitLoanC1\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate32') * 100 AS \"LimitRateC2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan32') AS \"LimitLoanC2\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate33') * 100 AS \"LimitRateC3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan33') AS \"LimitLoanC3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitRate43') * 100 AS \"LimitRateAB3\"";
		sql+="      , JSON_VALUE(\"JsonFields\", '$.LimitLoan43') AS \"LimitLoanAB3\"";
		sql+="  FROM";
		sql+="      \"tmp\"";
		sql+="  WHERE";
		sql+="      \"EffectDate\" = (";
		sql+="          SELECT";
		sql+="              MAX(\"EffectDate\")";
		sql+="          FROM";
		sql+="              \"tmp\"";
		sql+="      )";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("acdate", acdate);
		return this.convertToMap(query);
	}
	
	

}