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

@Service("LM087ServiceImpl")
@Repository
public class LM087ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 保險業利害關係人放款管理辦法第3條
	 * 
	 * @param yearMonth 西元年月
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findData1(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl findData1");

		String sql = "";
		// --＊保險業利害關係人放款管理辦法第3條
		// --對同一自然人擔保放款總額
		// --對同一法人擔保放款總額
		// --對同一利害關係人擔保放款總額
		// --對全體授信限制對象放款總額
		sql += "    WITH \"dataFromLM050\" AS (";
		sql += "        SELECT ROW_NUMBER() OVER(PARTITION BY S0.\"CustNo\"";
		sql += "            ORDER BY S1.\"Rel\", \"EntCode\"";
		sql += "                           , \"LoanBal\"";
		sql += "        ) AS \"Seq\"";
		sql += "             , ROW_NUMBER() OVER(PARTITION BY S1.\"Rel\", \"EntCode\"";
		sql += "            ORDER BY \"LoanBal\" DESC";
		sql += "        ) AS \"Seq2\"";
		sql += "             , CASE";
		sql += "            WHEN Nvl(";
		sql += "                S1.\"Rel\", ' '";
		sql += "            ) = ' ' THEN '3'";
		sql += "            WHEN S1.\"Rel\" = 'N' THEN '2'";
		sql += "            WHEN S1.\"Rel\" IN (";
		sql += "                'A'";
		sql += "              , 'B'";
		sql += "            ) THEN '1'";
		sql += "            ELSE '3'";
		sql += "        END AS \"RptType\"";
		sql += "             , CASE";
		sql += "            WHEN Nvl(";
		sql += "                S1.\"RptId\", ' '";
		sql += "            ) != ' ' THEN S0.\"CustNo\"";
		sql += "            ELSE 0";
		sql += "        END AS \"CustNo\"";
		sql += "             , CASE";
		sql += "            WHEN Nvl(";
		sql += "                S1.\"RptId\", ' '";
		sql += "            ) != ' ' THEN Cm.\"CustName\"";
		sql += "            ELSE N' '";
		sql += "        END AS \"CustName\"";
		sql += "             , S0.\"LoanBal\" AS \"LoanBal\"";
		sql += "             , Decode(";
		sql += "            S1.\"Rel\", 'A'";
		sql += "         , Cm.\"EntCode\"";
		sql += "         , 0";
		sql += "        ) AS \"EntCode\"";
		sql += "        FROM (";
		sql += "            SELECT \"CustNo\"";
		sql += "                 , SUM(\"LoanBalance\") AS \"LoanBal\"";
		sql += "            FROM \"MonthlyLoanBal\"";
		sql += "            WHERE \"YearMonth\" = :inputyearmonth";
		sql += "                  AND";
		sql += "                  \"LoanBalance\" > 0";
		sql += "            GROUP BY \"CustNo\"";
		sql += "        ) S0";
		sql += "        LEFT JOIN \"CustMain\" Cm ON Cm.\"CustNo\" = S0.\"CustNo\"";
		sql += "        LEFT JOIN (";
		sql += "            SELECT Decode(";
		sql += "                \"BusId\", '-'";
		sql += "         , Decode(";
		sql += "                    \"RelId\", '-'";
		sql += "         , \"HeadName\"";
		sql += "         , \"RelName\"";
		sql += "                ), \"BusName\"";
		sql += "            ) AS \"CustName\"";
		sql += "                 , To_Char(";
		sql += "                Decode(";
		sql += "                    \"BusId\", '-'";
		sql += "                     , Decode(";
		sql += "                        \"RelId\", '-'";
		sql += "         , \"HeadId\"";
		sql += "         , \"RelId\"";
		sql += "                    ), \"BusId\"";
		sql += "                )";
		sql += "            ) AS \"RptId\"";
		sql += "                 , \"RelWithCompany\" AS \"Rel\"";
		sql += "            FROM \"LifeRelHead\"";
		sql += "            WHERE \"RelWithCompany\" IN (";
		sql += "                'A'";
		sql += "              , 'B'";
		sql += "            )";
		sql += "                  AND";
		sql += "                  \"AcDate\" = (";
		sql += "                      SELECT MAX(\"AcDate\")";
		sql += "                      FROM \"LifeRelHead\"";
		sql += "                      WHERE Trunc(\"AcDate\" / 100) = :inputyearmonth";
		sql += "                  )";
		sql += "                  AND";
		sql += "                  \"LoanBalance\" > 0";
		sql += "            UNION";
		sql += "            SELECT \"EmpName\"            AS \"CustName\"";
		sql += "                 , To_Char(\"EmpId\")     AS \"RptId\"";
		sql += "                 , 'N'                  AS \"Rel\"";
		sql += "            FROM \"LifeRelEmp\"";
		sql += "            WHERE \"AcDate\" = (";
		sql += "                SELECT MAX(\"AcDate\")";
		sql += "                FROM \"LifeRelEmp\"";
		sql += "                WHERE Trunc(\"AcDate\" / 100) = :inputyearmonth";
		sql += "            )";
		sql += "        ) S1 ON S1.\"RptId\" = Cm.\"CustId\"";
		sql += "    )";
		// --對同一自然人擔保放款總額
		// --對同一法人擔保放款總額
		sql += "    SELECT \"CustName\"";
		sql += "         , ROUND(\"LoanBal\" / 1000000 , 2 ) AS \"LoanBal\"";
		sql += "         , \"EntCode\" AS \"Group\"";
		sql += "    FROM \"dataFromLM050\"";
		sql += "    WHERE \"Seq2\" = 1";
		sql += "          AND";
		sql += "          \"RptType\" = '1'";
		sql += "    UNION ALL";
		// --對同一利害關係人擔保放款總額
		sql += "    SELECT N'新光人壽 組'          AS \"CustName\"";
		sql += "         , ROUND(SUM(\"LoanBal\") / 1000000 , 2 )     AS \"LoanBal\"";
		sql += "         , '2'                AS \"Group\"";
		sql += "    FROM \"dataFromLM050\"";
		sql += "    WHERE \"Seq\" = 1";
		sql += "          AND";
		sql += "          \"RptType\" = '1'";
		sql += "    UNION ALL";
		// --對全體授信限制對象放款總額
		sql += "    SELECT N' '               AS \"CustName\"";
		sql += "         , ROUND(SUM(\"LoanBal\") / 1000000 , 2 )      AS \"LoanBal\"";
		sql += "         , '9'                AS \"Group\"";
		sql += "    FROM \"dataFromLM050\"";
		sql += "    WHERE \"Seq\" = 1";
		sql += "          AND";
		sql += "          \"RptType\" IN (";
		sql += "              '1'";
		sql += "            , '2'";
		sql += "          )";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 金控法第44條
	 * 
	 * @param yearMonth 西元年月
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findData2(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl findData2");

		String sql = "";
		// --＊金控法第44條
		// --對同一自然人擔保放款總額
		// --對同一法人擔保放款總額
		// --對全體授信限制對象放款總額
		sql += " WITH \"dataFromLM049\" AS (";
		sql += "     SELECT CASE";
		sql += "         WHEN F.\"CompanyName\" LIKE '新%金%'";
		sql += "              AND";
		sql += "              Length(";
		sql += "                  F.\"CompanyName\"";
		sql += "              ) = 4 THEN 1";
		sql += "         WHEN F.\"CompanyName\" LIKE '新%投%' THEN 4";
		sql += "         WHEN F.\"CompanyName\" LIKE '新%銀%' THEN 4";
		sql += "         WHEN F.\"CompanyName\" LIKE '新%人%' THEN 4";
		sql += "         ELSE 9";
		sql += "     END AS \"comSeq\"";
		sql += "          , ROW_NUMBER() OVER(PARTITION BY C.\"EntCode\"";
		sql += "         ORDER BY F.\"LoanBalance\" DESC";
		sql += "     ) AS \"Seq\"";
		sql += "          , F.\"Id\"";
		sql += "          , F.\"Name\"";
		sql += "          , F.\"LoanBalance\"";
		sql += "          , C.\"EntCode\" AS \"EntCode\"";
		sql += "     FROM \"FinHoldRel\"  F";
		sql += "     LEFT JOIN \"CustMain\"    C ON C.\"CustId\" = F.\"Id\"";
		sql += "     WHERE Trunc(F.\"AcDate\" / 100) = :inputyearmonth";
		sql += " )";
		// --對同一自然人擔保放款總額
		sql += " SELECT M.\"Name\"           AS \"CustName\"";
		sql += "      , ROUND(M.\"LoanBalance\"/ 1000000 , 2 )    AS \"LoanBal\"";
		sql += "      , '0'                AS \"Group\"";
		sql += " FROM \"dataFromLM049\" M";
		sql += " WHERE M.\"Seq\" = 1";
		sql += "       AND";
		sql += "       M.\"comSeq\" IN (";
		sql += "           1";
		sql += "         , 2";
		sql += "         , 3";
		sql += "         , 4";
		sql += "       )";
		sql += "       AND";
		sql += "       M.\"EntCode\" <> 1";
		sql += " UNION ALL";
		// --對同一法人擔保放款總額
		sql += " SELECT M.\"Name\"           AS \"CustName\"";
		sql += "      , ROUND(M.\"LoanBalance\"/ 1000000 , 2 )    AS \"LoanBal\"";
		sql += "      , '1'                AS \"Group\"";
		sql += " FROM \"dataFromLM049\" M";
		sql += " WHERE M.\"Seq\" = 1";
		sql += "       AND";
		sql += "       M.\"comSeq\" IN (";
		sql += "           1";
		sql += "         , 2";
		sql += "         , 3";
		sql += "         , 4";
		sql += "       )";
		sql += "       AND";
		sql += "       M.\"EntCode\" = 1";
		sql += " UNION ALL";
		// --對全體授信限制對象放款總額
		sql += " SELECT ' '                       AS \"Custname\"";
		sql += "      , ROUND(SUM(M.\"LoanBalance\")/ 1000000 , 2 )     AS \"LoanBal\"";
		sql += "      , '9'                        AS \"Group\"";
		sql += " FROM (";
		sql += "     SELECT DISTINCT \"Id\"";
		sql += "                   , \"LoanBalance\"";
		sql += "     FROM \"dataFromLM049\"";
		sql += " ) M";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 保險業對同一人同一關係人或同一關係企業之放款及其他交易管理辦法第2條
	 * 
	 * @param yearMonth 西元年月
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findData3(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl findData3");

		String sql = "";
		// --來源MonthlyFAcBal
		// --＊保險業對同一人同一關係人或同一關係企業之放款及其他交易管理辦法第2條
		// --對同一自然人擔保放款總額
		// --對同一法人擔保放款總額
		sql += "  WITH \"tMonthlyFacBal\" AS (";
		sql += "      SELECT C.\"CustName\"";
		sql += "           , Decode(";
		sql += "          M.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      ) AS \"EntCode\"";
		sql += "           , M.\"PrinBalance\" AS \"LoanBal\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY Decode(";
		sql += "          M.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      )";
		sql += "          ORDER BY M.\"PrinBalance\" DESC";
		sql += "      ) AS \"Seq\"";
		sql += "      FROM \"MonthlyFacBal\"  M";
		sql += "      LEFT JOIN \"CustMain\"       C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      WHERE M.\"YearMonth\" = 202203";
		sql += "            AND";
		sql += "            M.\"PrinBalance\" > 0";
		sql += "  ),\"dataFromLM088\" AS (";
		sql += "      SELECT Decode(";
		sql += "          Cm.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      ) AS \"EntCodeGroup\"";
		sql += "           , Decode(";
		sql += "          Cm2.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      ) AS \"EntCode\"";
		sql += "           , Cm.\"CustNo\"       AS \"CustNoGroup\"";
		sql += "           , Cm2.\"CustNo\"      AS \"CustNo\"";
		sql += "           , Cm.\"CustName\"     AS \"CustNameGroup\"";
		sql += "           , Cm2.\"CustName\"    AS \"CustName\"";
		sql += "           , Nvl(";
		sql += "          M.\"LoanBal\", 0";
		sql += "      ) AS \"LoanBal\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY Decode(";
		sql += "          Cm2.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      )";
		sql += "          ORDER BY Nvl(";
		sql += "              M.\"LoanBal\", 0";
		sql += "          ) DESC";
		sql += "      ) AS \"Seq\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY Decode(";
		sql += "          Cm.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      ), Decode(";
		sql += "          Cm2.\"EntCode\", 1";
		sql += "       , 1";
		sql += "       , 0";
		sql += "      )";
		sql += "          ORDER BY Nvl(";
		sql += "              M.\"LoanBal\", 0";
		sql += "          ) DESC";
		sql += "      ) AS \"Seq2\"";
		sql += "      FROM \"ReltMain\"  Rm";
		sql += "      LEFT JOIN \"CustMain\"  Cm ON Cm.\"CustNo\" = Rm.\"CustNo\"";
		sql += "      LEFT JOIN \"CustMain\"  Cm2 ON Cm2.\"CustUKey\" = Rm.\"ReltUKey\"";
		sql += "      LEFT JOIN (";
		sql += "          SELECT \"CustNo\"";
		sql += "               , SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += "          FROM \"MonthlyFacBal\"";
		sql += "          WHERE \"YearMonth\" = :inputyearmonth";
		sql += "                AND";
		sql += "                \"PrinBalance\" > 0";
		sql += "          GROUP BY \"CustNo\"";
		sql += "      ) M ON M.\"CustNo\" = Cm2.\"CustNo\"";
		sql += "      WHERE Rm.\"CaseNo\" = 0";
		sql += "  )";
		// --對同一關係人擔保放款總額
		sql += "  , \"tmpMain1\" AS (";
		sql += "      SELECT \"EntCodeGroup\"     AS \"EntCode\"";
		sql += "           , \"CustNoGroup\"      AS \"CustNo\"";
		sql += "           , \"CustNameGroup\"    AS \"CustName\"";
		sql += "           , \"EntCode\"          AS \"tEntCode\"";
		sql += "           , SUM(\"LoanBal\")     AS \"LoanBal\"";
		sql += "      FROM \"dataFromLM088\"";
		sql += "      WHERE \"EntCodeGroup\" IN (";
		sql += "          1";
		sql += "      )";
		sql += "            AND";
		sql += "            \"EntCode\" IN (";
		sql += "                1";
		sql += "            )";
		sql += "      GROUP BY \"EntCodeGroup\"";
		sql += "             , \"CustNoGroup\"";
		sql += "             , \"CustNameGroup\"";
		sql += "             , \"EntCode\"";
		sql += "  )";
		// --對同一關係人擔保放款，其中對自然人之放款總額
		sql += "  , \"tmpMain2\" AS (";
		sql += "      SELECT \"EntCodeGroup\"     AS \"EntCode\"";
		sql += "           , \"CustNoGroup\"      AS \"CustNo\"";
		sql += "           , \"CustNameGroup\"    AS \"CustName\"";
		sql += "           , \"EntCode\"          AS \"tEntCode\"";
		sql += "           , SUM(\"LoanBal\")     AS \"LoanBal\"";
		sql += "      FROM \"dataFromLM088\"";
		sql += "      WHERE \"EntCodeGroup\" IN (";
		sql += "          1";
		sql += "      )";
		sql += "      GROUP BY \"EntCodeGroup\"";
		sql += "             , \"CustNoGroup\"";
		sql += "             , \"CustNameGroup\"";
		sql += "             , \"EntCode\"";
		sql += "  )";
		// --對同一關係企業擔保放款總額
		sql += "  , \"tmpMain3\" AS (";
		sql += "      SELECT \"EntCodeGroup\"     AS \"EntCode\"";
		sql += "           , \"CustNoGroup\"      AS \"CustNo\"";
		sql += "           , \"CustNameGroup\"    AS \"CustName\"";
		sql += "           , \"EntCode\"          AS \"tEntCode\"";
		sql += "           , SUM(\"LoanBal\")     AS \"LoanBal\"";
		sql += "      FROM \"dataFromLM088\"";
		sql += "      WHERE \"EntCodeGroup\" IN (";
		sql += "          0";
		sql += "      )";
		sql += "      GROUP BY \"EntCodeGroup\"";
		sql += "             , \"CustNoGroup\"";
		sql += "             , \"CustNameGroup\"";
		sql += "             , \"EntCode\"";
		sql += "  ), \"tmp1\" AS (";
		sql += "      SELECT CASE";
		sql += "          WHEN T.\"EntCode\" = 1";
		sql += "               AND";
		sql += "               M.\"EntCode\" = 1 THEN M.\"CustName\"";
		sql += "          ELSE T.\"CustName\"";
		sql += "      END AS \"CustName\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY T.\"tEntCode\"";
		sql += "          ORDER BY T.\"LoanBal\" DESC";
		sql += "      ) AS \"Seq\"";
		sql += "           , T.\"LoanBal\"";
		sql += "      FROM \"tmpMain1\"  T";
		sql += "      LEFT JOIN \"dataFromLM088\"      M ON M.\"CustNoGroup\" = T.\"CustNo\"";
		sql += "                            AND";
		sql += "                            M.\"Seq2\" = 1";
		sql += "  ), \"tmp2\" AS (";
		sql += "      SELECT CASE";
		sql += "          WHEN T.\"EntCode\" = 1 THEN M.\"CustName\"";
		sql += "          ELSE T.\"CustName\"";
		sql += "      END AS \"CustName\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY T.\"tEntCode\"";
		sql += "          ORDER BY T.\"LoanBal\" DESC";
		sql += "      ) AS \"Seq\"";
		sql += "           , T.\"LoanBal\"";
		sql += "      FROM \"tmpMain2\"  T";
		sql += "      LEFT JOIN \"dataFromLM088\"      M ON M.\"CustNoGroup\" = T.\"CustNo\"";
		sql += "                            AND";
		sql += "                            M.\"Seq2\" = 1";
		sql += "  ), \"tmp3\" AS (";
		sql += "      SELECT CASE";
		sql += "          WHEN T.\"EntCode\" = 0";
		sql += "               AND";
		sql += "               M.\"EntCode\" = 0 THEN M.\"CustName\"";
		sql += "          ELSE T.\"CustName\"";
		sql += "      END AS \"CustName\"";
		sql += "           , ROW_NUMBER() OVER(PARTITION BY T.\"tEntCode\"";
		sql += "          ORDER BY T.\"LoanBal\" DESC";
		sql += "      ) AS \"Seq\"";
		sql += "           , T.\"LoanBal\"";
		sql += "      FROM \"tmpMain3\"  T";
		sql += "      LEFT JOIN \"dataFromLM088\"      M ON M.\"CustNoGroup\" = T.\"CustNo\"";
		sql += "                            AND";
		sql += "                            M.\"Seq2\" = 1";
		sql += "  )";
		sql += "  SELECT \"CustName\"";
		sql += "       , ROUND(\"LoanBal\"/ 1000000 , 2 ) AS \"LoanBal\"";
		sql += "       , '4' AS \"Group\"";
		sql += "  FROM \"tmp1\"";
		sql += "  WHERE \"Seq\" = 1";
		sql += "  UNION ALL";
		sql += "  SELECT \"CustName\"";
		sql += "       , ROUND(SUM(\"LoanBal\")/ 1000000 , 2 )     AS \"LoanBal\"";
		sql += "       , '2'                AS \"Group\"";
		sql += "  FROM \"tmp2\"";
		sql += "  WHERE \"Seq\" = 1";
		sql += "  GROUP BY \"CustName\"";
		sql += "  UNION ALL";
		sql += "  SELECT \"CustName\"";
		sql += "       , ROUND(\"LoanBal\"/ 1000000 , 2 ) AS \"LoanBal\"";
		sql += "       , '3' AS \"Group\"";
		sql += "  FROM \"tmp3\"";
		sql += "  WHERE \"Seq\" = 1";
		sql += "  UNION ALL";
		sql += "  SELECT \"CustName\"";
		sql += "       , ROUND(\"LoanBal\"/ 1000000 , 2 ) AS \"LoanBal\"";
		sql += "       ,to_char(\"EntCode\") AS \"Group\"";
		sql += "  FROM \"tMonthlyFacBal\"";
		sql += "  WHERE \"Seq\" = 1";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 來源逾期月報表
	 * 
	 * @param yearMonth 西元年月
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findData4(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl findData4");

		String sql = "";
		// --數值來源自逾期月報表
		sql += "  WITH \"dataFromLM085\" AS (";
		sql += "      SELECT 'G'                        AS \"Column\"";
		sql += "           , SUM(M.\"PrinBalance\")       AS \"Value\"";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      WHERE M.\"YearMonth\" = :inputyearmonth";
		sql += "            AND";
		sql += "            M.\"PrinBalance\" > 0";
		sql += "      UNION ALL";
		sql += "      SELECT 'E9'             AS \"Column\"";
		sql += "           , SUM(\"TdBal\")     AS \"Value\"";
		sql += "      FROM \"CoreAcMain\"";
		sql += "      WHERE \"AcDate\" = To_Number(";
		sql += "          To_Char(";
		sql += "              Last_Day(";
		sql += "                  To_Date(";
		sql += "                      To_Char(:inputyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                  )";
		sql += "              ), 'YYYYMMDD'";
		sql += "          )";
		sql += "      )";
		sql += "            AND";
		sql += "            \"AcNoCode\" IN (";
		sql += "                '10601301000'";
		sql += "              , '10601302000'";
		sql += "            )";
		sql += "            AND";
		sql += "            \"CurrencyCode\" = 'NTD'";
		sql += "      UNION ALL";
		sql += "      SELECT 'G'              AS \"Column\"";
		sql += "           , SUM(\"TdBal\")     AS \"Value\"";
		sql += "      FROM \"CoreAcMain\"";
		sql += "      WHERE \"AcDate\" = To_Number(";
		sql += "          To_Char(";
		sql += "              Last_Day(";
		sql += "                  To_Date(";
		sql += "                      To_Char(:inputyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                  )";
		sql += "              ), 'YYYYMMDD'";
		sql += "          )";
		sql += "      )";
		sql += "            AND";
		sql += "            \"AcNoCode\" IN (";
		sql += "                '10600304000'";
		sql += "              , '10601304000'";
		sql += "            )";
		sql += "            AND";
		sql += "            \"CurrencyCode\" = 'NTD'";
		sql += "      UNION ALL";
		sql += "      SELECT 'E8'             AS \"Column\"";
		sql += "           , SUM(\"TdBal\")     AS \"Value\"";
		sql += "      FROM \"CoreAcMain\"";
		sql += "      WHERE \"AcDate\" = To_Number(";
		sql += "          To_Char(";
		sql += "              Last_Day(";
		sql += "                  To_Date(";
		sql += "                      To_Char(:inputyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                  )";
		sql += "              ), 'YYYYMMDD'";
		sql += "          )";
		sql += "      )";
		sql += "            AND";
		sql += "            \"AcNoCode\" IN (";
		sql += "                '10601304000'";
		sql += "            )";
		sql += "            AND";
		sql += "            \"CurrencyCode\" = 'NTD'";
		sql += "      UNION ALL";
		sql += "      SELECT 'H26'            AS \"Column\"";
		sql += "           , SUM(\"TdBal\")     AS \"Value\"";
		sql += "      FROM \"CoreAcMain\"";
		sql += "      WHERE \"AcDate\" = To_Number(";
		sql += "          To_Char(";
		sql += "              Last_Day(";
		sql += "                  To_Date(";
		sql += "                      To_Char(:inputyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                  )";
		sql += "              ), 'YYYYMMDD'";
		sql += "          )";
		sql += "      )";
		sql += "            AND";
		sql += "            \"AcNoCode\" IN (";
		sql += "                '10601'";
		sql += "              , '10602'";
		sql += "            )";
		sql += "            AND";
		sql += "            \"CurrencyCode\" = 'TOL'";
		sql += "      UNION ALL";
		sql += "      SELECT CASE";
		sql += "          WHEN M.\"AcctCode\" <> '990'";
		sql += "               AND";
		sql += "               M.\"OvduTerm\" >= 3 THEN 'C'";
		sql += "          WHEN M.\"AcctCode\" <> '990'";
		sql += "               AND";
		sql += "               M.\"OvduTerm\" >= 3 THEN 'C'";
		sql += "          WHEN M.\"AcctCode\" = '990' THEN 'E'";
		sql += "          ELSE 'N1'";
		sql += "      END AS \"Column\"";
		sql += "           , SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      WHERE M.\"YearMonth\" = :inputyearmonth";
		sql += "            AND";
		sql += "            M.\"PrinBalance\" > 0";
		sql += "      GROUP BY";
		sql += "          CASE";
		sql += "              WHEN M.\"AcctCode\" <> '990'";
		sql += "                   AND";
		sql += "                   M.\"OvduTerm\" >= 3 THEN 'C'";
		sql += "              WHEN M.\"AcctCode\" <> '990'";
		sql += "                   AND";
		sql += "                   M.\"OvduTerm\" >= 3 THEN 'C'";
		sql += "              WHEN M.\"AcctCode\" = '990' THEN 'E'";
		sql += "              ELSE 'N1'";
		sql += "          END";
		sql += "  ), \"tempMain\" AS (";
		sql += "      SELECT M.\"OvduLoanBal\"";
		sql += "           , M2.\"LoanBal\"";
		sql += "           , M3.\"TotalLoanBal\"";
		sql += "      FROM (";
		sql += "          SELECT 'Loan'           AS \"Column\"";
		sql += "               , SUM(\"Value\")     AS \"OvduLoanBal\"";
		sql += "          FROM \"dataFromLM085\"";
		sql += "          WHERE \"Column\" IN (";
		sql += "              'C'";
		sql += "            , 'E'";
		sql += "            , 'E8'";
		sql += "            , 'E9'";
		sql += "          )";
		sql += "      ) M";
		sql += "      LEFT JOIN (";
		sql += "          SELECT 'Loan'           AS \"Column\"";
		sql += "               , SUM(\"Value\")     AS \"LoanBal\"";
		sql += "          FROM \"dataFromLM085\"";
		sql += "          WHERE \"Column\" IN (";
		sql += "              'G'";
		sql += "            , 'E9'";
		sql += "          )";
		sql += "      ) M2 ON M2.\"Column\" = M.\"Column\"";
		sql += "      LEFT JOIN (";
		sql += "          SELECT 'Loan'           AS \"Column\"";
		sql += "               , SUM(\"Value\")     AS \"TotalLoanBal\"";
		sql += "          FROM \"dataFromLM085\"";
		sql += "          WHERE \"Column\" IN (";
		sql += "              'G'";
		sql += "            , 'E9'";
		sql += "            , 'H26'";
		sql += "          )";
		sql += "      ) M3 ON M3.\"Column\" = M.\"Column\"";
		sql += "  )";
		sql += "  SELECT ROUND(\"LoanBal\"/1000000,2) AS \"LoanBal\"";
		sql += "       , ROUND(\"OvduLoanBal\"/1000000,2) AS \"OvduLoanBal\"";
		sql += "       , ROUND(\"TotalLoanBal\"/1000000,2) AS \"TotalLoanBal\"";
		sql += "       , Round(";
		sql += "      		\"OvduLoanBal\" / \"TotalLoanBal\", 4";
		sql += "  		) * 100 AS \"OvduTotalRate\"";
		sql += "       , Round(";
		sql += "      		\"OvduLoanBal\" / \"LoanBal\", 4";
		sql += "  		) * 100 AS \"OvduRate\"";
		sql += "  FROM \"tempMain\"";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 查詢淨值及可運用資金
	 * 
	 * @param yearMonth 西元年月日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> fnEquity(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl fnEquity=" + yearMonth);

		String sql = " ";
		sql += "     SELECT \"StockHoldersEqt\" AS \"StockHoldersEqt\"";
		sql += "           ,\"AvailableFunds\"  AS \"AvailableFunds\"";
		sql += "           ,\"AcDate\"  AS \"AcDate\"";
		sql += "     FROM \"InnFundApl\" ";
		sql += "     WHERE \"AcDate\" = (";
		sql += "     	SELECT MAX(\"AcDate\") ";
		sql += "     	FROM \"InnFundApl\" ";
		sql += "     	WHERE TRUNC(\"AcDate\"/100) < :inputyearmonth";
		sql += " 	      AND \"StockHoldersEqt\" > 0 ";
		sql += "     )";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", yearMonth);
		return this.convertToMap(query);

	}

	/**
	 * RBC
	 * 
	 * @param yearMonth 西元年月日
	 * @param titaVo
	 * @return
	 * @throws Exception
	 **/
	public List<Map<String, String>> findRBC(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM087ServiceImpl findRBC=" + yearMonth);

		// 本月
		String iYearMonth = yearMonth + "";

		int year = yearMonth / 100;
		int month = yearMonth % 100;

		int lyear = month == 1 ? year - 1 : year;
		int lmonth = month == 1 ? 12 : month - 1;
		// 上月
		String ilYearMonth = (lyear * 100 + lmonth) + "";

		// 去年12月(以上月的基準年份減1)
		int lyear12 = lyear - 1;
		String ilYear12 = lyear12 + "12";
		this.info("iYearMonth =" + iYearMonth);
		this.info("ilYearMonth =" + ilYearMonth);
		this.info("ilYear12 =" + ilYear12);
		String sql = " ";
		sql += "  WITH \"tempTotal\" AS (";
		sql += "      SELECT M.\"YearMonth\"";
		sql += "           , SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "      FROM \"MonthlyFacBal\" M";
		sql += "      WHERE M.\"YearMonth\" IN (";
		sql += "          :inputyearmonth";
		sql += "        , :inputlyearmonth";
		sql += "        , :inputl12yearmonth";
		sql += "      )";
		sql += "            AND";
		sql += "            M.\"PrinBalance\" > 0";
		sql += "      GROUP BY M.\"YearMonth\"";
		sql += "      UNION ALL";
		sql += "      SELECT Trunc(\"AcDate\" / 100)     AS \"YearMonth\"";
		sql += "           , SUM(\"TdBal\")              AS \"Value\"";
		sql += "      FROM \"CoreAcMain\"";
		sql += "      WHERE \"AcDate\" IN (";
		sql += "          To_Number(";
		sql += "              To_Char(";
		sql += "                  Last_Day(";
		sql += "                      To_Date(";
		sql += "                          To_Char(:inputlyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                      )";
		sql += "                  ), 'YYYYMMDD'";
		sql += "              )";
		sql += "          )";
		sql += "        , To_Number(";
		sql += "              To_Char(";
		sql += "                  Last_Day(";
		sql += "                      To_Date(";
		sql += "                          To_Char(:inputyearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                      )";
		sql += "                  ), 'YYYYMMDD'";
		sql += "              )";
		sql += "          )";
		sql += "        , To_Number(";
		sql += "              To_Char(";
		sql += "                  Last_Day(";
		sql += "                      To_Date(";
		sql += "                          To_Char(:inputl12yearmonth * 100 + 1), 'YYYYMMDD'";
		sql += "                      )";
		sql += "                  ), 'YYYYMMDD'";
		sql += "              )";
		sql += "          )";
		sql += "      )";
		sql += "            AND";
		sql += "            \"AcNoCode\" IN (";
		sql += "                '10600304000'";
		sql += "              , '10601304000'";
		sql += "              , '10601301000'";
		sql += "              , '10601302000'";
		sql += "            )";
		sql += "            AND";
		sql += "            \"CurrencyCode\" = 'NTD'";
		sql += "      GROUP BY Trunc(\"AcDate\" / 100)";
		sql += "  ), \"tempMain\" AS (";
		sql += "      SELECT \"YearMonth\"      AS \"YearMonth\"";
		sql += "           , SUM(\"Value\")     AS \"LoanBal\"";
		sql += "      FROM \"tempTotal\"";
		sql += "      GROUP BY \"YearMonth\"";
		sql += "  )";
		sql += "  SELECT To_Number(";
		sql += "              To_Char(";
		sql += "                  Last_Day(";
		sql += "                      To_Date(";
		sql += "                          To_Char(M.\"YearMonth\" * 100 + 1), 'YYYYMMDD'";
		sql += "                      )";
		sql += "                  ), 'YYYYMMDD'";
		sql += "              )";
		sql += "          ) AS \"YearMonth\"";
		sql += "       , Round(";
		sql += "      		M.\"LoanBal\" / 1000000, 2";
		sql += "  		) AS \"LoanBal\"";
		sql += "       , NVL(Round(";
		sql += "      		M2.\"RiskFactorAmount\" / 1000000, 2";
		sql += "  		),0) AS \"RiskFactorAmount\"";
		sql += "  FROM \"tempMain\" M";
		sql += "  LEFT JOIN (";
		sql += "      SELECT \"YearMonth\"";
		sql += "           , SUM(\"RiskFactorAmount\") AS \"RiskFactorAmount\"";
		sql += "      FROM \"MonthlyLM042RBC\"";
		sql += "      WHERE \"YearMonth\" IN (";
		sql += "          :inputyearmonth";
		sql += "        , :inputlyearmonth";
		sql += "        , :inputl12yearmonth";
		sql += "      )";
		sql += "      GROUP BY \"YearMonth\"";
		sql += "  ) M2 ON M2.\"YearMonth\" = M.\"YearMonth\"";
		sql += "  ORDER BY M.\"YearMonth\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputyearmonth", iYearMonth);
		query.setParameter("inputlyearmonth", ilYearMonth);
		query.setParameter("inputl12yearmonth", ilYear12);
		return this.convertToMap(query);

	}

}