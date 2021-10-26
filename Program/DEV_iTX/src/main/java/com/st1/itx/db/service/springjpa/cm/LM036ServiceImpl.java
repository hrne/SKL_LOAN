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
public class LM036ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Query for LM036 表二 Bad Rate - 房貸(件數)
	 * 
	 * @param startMonth 資料範圍年月-起(西元)
	 * @param endMonth   資料範圍年月-迄(西元)
	 * @param titaVo     titaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> queryBadRateCounts(int startMonth, int endMonth, TitaVo titaVo) {
		this.info("LM036ServiceImpl queryBadRateCount ");

		this.info("LM036ServiceImpl startMonth = " + startMonth);
		this.info("LM036ServiceImpl endMonth = " + endMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" ";
		sql += "      , SUM(\"Counts\") AS \"Counts\" ";
		sql += "      , \"BadDebtMonth\" AS \"BadDebtMonth\" ";
		sql += "      , SUM(\"BadDebtCount\") AS \"BadDebtCount\" ";
		sql += " FROM ( ";
		sql += "     SELECT TRUNC(FAC.\"FirstDrawdownDate\" / 100 ) ";
		sql += "                                     AS \"YearMonth\" "; // F0 初貸年月
		sql += "          , 1                        AS \"Counts\" "; // F1 初貸件數
		sql += "          , 0                        AS \"BadDebtMonth\" "; // F2 逾90天時年月
		sql += "          , 0                        AS \"BadDebtCount\" "; // F3 逾90天件數
		sql += "     FROM \"FacCaseAppl\" FCA ";
		sql += "     LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = FCA.\"ApplNo\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "     WHERE FCA.\"BranchNo\" = '0000' ";
		sql += "       AND FCA.\"ApproveDate\" > 0 ";
		sql += "       AND FAC.\"FirstDrawdownDate\" > 0 ";
		sql += "       AND FCA.\"ProcessCode\" = '1' ";
		sql += "       AND NVL(FAC.\"CustNo\",0) != 0 ";
		sql += "       AND CM.\"EntCode\" = '0' ";
		sql += "       AND NVL(FCA.\"PieceCode\",' ') NOT IN ('3','5','7','C','E') ";
		sql += "       AND TRUNC(FAC.\"FirstDrawdownDate\" / 100 ) >= :startMonth ";
		sql += "       AND TRUNC(FAC.\"FirstDrawdownDate\" / 100 ) <= :endMonth ";
		sql += "     UNION ALL ";
		sql += "     SELECT \"YearMonth\" "; // F0 初貸年月
		sql += "          , 0             AS \"Counts\" "; // F1 初貸件數
		sql += "          , \"YearMonth\" AS \"BadDebtMonth\" "; // F2 逾90天時年月
		sql += "          , 0             AS \"BadDebtCount\" "; // F3 逾90天件數
		sql += "     FROM \"MonthlyFacBal\" ";
		sql += "     WHERE \"YearMonth\" >= :startMonth  ";
		sql += "       AND \"YearMonth\" <= :endMonth  ";
		sql += "     UNION ALL ";
		sql += "     SELECT TRUNC(FAC.\"FirstDrawdownDate\" / 100) ";
		sql += "                            AS \"YearMonth\" "; // F0 初貸年月
		sql += "          , 0               AS \"Counts\" "; // F1 初貸件數
		sql += "          , M.\"YearMonth\" AS \"BadDebtMonth\" "; // F2 逾90天時年月
		sql += "          , 1               AS \"BadDebtCount\" "; // F3 逾90天件數
		sql += "     FROM \"MonthlyFacBal\" M ";
		sql += "     LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\" ";
		sql += "                              AND FAC.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "     LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = FAC.\"ApplNo\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "     WHERE FCA.\"BranchNo\" = '0000' ";
		sql += "       AND FCA.\"ApproveDate\" > 0 ";
		sql += "       AND FAC.\"FirstDrawdownDate\" > 0 ";
		sql += "       AND FCA.\"ProcessCode\" = '1' ";
		sql += "       AND NVL(FAC.\"CustNo\",0) != 0 ";
		sql += "       AND CM.\"EntCode\" = '0' ";
		sql += "       AND NVL(FCA.\"PieceCode\",' ') NOT IN ('3','5','7','C','E') ";
		sql += "       AND M.\"YearMonth\" >= :startMonth  ";
		sql += "       AND M.\"YearMonth\" <= :endMonth  ";
		sql += "       AND M.\"OvduDays\" >= 90 ";
		sql += " ) ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += "        , \"BadDebtMonth\" ";
		sql += " ORDER BY \"YearMonth\" ";
		sql += "        , \"BadDebtMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);

		return this.convertToMap(query);
	}

	/**
	 * Query for LM036 表三 Bad Rate - 房貸(金額)
	 * 
	 * @param startMonth 資料範圍年月-起(西元)
	 * @param endMonth   資料範圍年月-迄(西元)
	 * @param titaVo     titaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> queryBadRateAmt(int startMonth, int endMonth, TitaVo titaVo) {
		this.info("LM036ServiceImpl queryBadRateAmt ");

		this.info("LM036ServiceImpl startMonth = " + startMonth);
		this.info("LM036ServiceImpl endMonth = " + endMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" "; // -- F0 初貸年月 
		sql += "      , SUM(\"DrawdownAmt\") AS \"DrawdownAmt\" "; // -- F1 初貸金額
		sql += "      , \"BadDebtMonth\" "; // -- F2 逾90天時年月
		sql += "      , SUM(\"BadDebtBal\") AS \"BadDebtBal\" "; // -- F3 逾90天時餘額
		sql += " FROM ( ";
		sql += "     SELECT TRUNC(FAC.\"FirstDrawdownDate\" / 100 )  ";
		sql += "                                     AS \"YearMonth\" "; // -- F0 初貸年月
		sql += "          , NVL(LBM.\"DrawdownAmt\",0) AS \"DrawdownAmt\" "; // -- F1 初貸金額
		sql += "          , 0                        AS \"BadDebtMonth\" "; // -- F2 逾90天時年月
		sql += "          , 0                        AS \"BadDebtBal\" "; // -- F3 逾90天時餘額
		sql += "     FROM \"FacCaseAppl\" FCA ";
		sql += "     LEFT JOIN \"FacMain\" FAC ON FAC.\"ApplNo\" = FCA.\"ApplNo\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "     LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                                  AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "                                  AND TRUNC(LBM.\"DrawdownDate\" / 100 ) = TRUNC(FAC.\"FirstDrawdownDate\" / 100 )  ";
		sql += "     WHERE FCA.\"BranchNo\" = '0000' ";
		sql += "       AND FCA.\"ApproveDate\" > 0 ";
		sql += "       AND FAC.\"FirstDrawdownDate\" > 0 ";
		sql += "       AND FCA.\"ProcessCode\" = '1' ";
		sql += "       AND NVL(FAC.\"CustNo\",0) != 0 ";
		sql += "       AND CM.\"EntCode\" = '0' ";
		sql += "       AND NVL(FCA.\"PieceCode\",' ') NOT IN ('3','5','7','C','E') ";
		sql += "       AND TRUNC(FAC.\"FirstDrawdownDate\" / 100 ) >= :startMonth ";
		sql += "       AND TRUNC(FAC.\"FirstDrawdownDate\" / 100 ) <= :endMonth ";
		sql += "     UNION ALL ";
		sql += "     SELECT \"YearMonth\" "; // -- F0 初貸年月
		sql += "          , 0             AS \"Counts\" "; // -- F1 初貸件數
		sql += "          , \"YearMonth\"   AS \"BadDebtMonth\" "; // -- F2 逾90天時年月
		sql += "          , 0             AS \"BadDebtBal\" "; // -- F3 逾90天時餘額
		sql += "     FROM \"MonthlyFacBal\" ";
		sql += "     WHERE \"YearMonth\" >= :startMonth  ";
		sql += "       AND \"YearMonth\" <= :endMonth  ";
		sql += "     UNION ALL ";
		sql += "     SELECT TRUNC(FAC.\"FirstDrawdownDate\" / 100) ";
		sql += "                            AS \"YearMonth\" "; // -- F0 初貸年月
		sql += "          , 0               AS \"Counts\" "; // -- F1 初貸件數
		sql += "          , M.\"YearMonth\"   AS \"BadDebtMonth\" "; // -- F2 逾90天時年月
		sql += "          , M.\"PrinBalance\" AS \"BadDebtBal\" "; // -- F3 逾90天時餘額
		sql += "     FROM \"MonthlyFacBal\" M ";
		sql += "     LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = M.\"CustNo\" ";
		sql += "                              AND FAC.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "     LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = FAC.\"ApplNo\" ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "     WHERE FCA.\"BranchNo\" = '0000' ";
		sql += "       AND FCA.\"ApproveDate\" > 0 ";
		sql += "       AND FAC.\"FirstDrawdownDate\" > 0 ";
		sql += "       AND FCA.\"ProcessCode\" = '1' ";
		sql += "       AND NVL(FAC.\"CustNo\",0) != 0 ";
		sql += "       AND CM.\"EntCode\" = '0' ";
		sql += "       AND NVL(FCA.\"PieceCode\",' ') NOT IN ('3','5','7','C','E') ";
		sql += "       AND M.\"YearMonth\" >= :startMonth  ";
		sql += "       AND M.\"YearMonth\" <= :endMonth  ";
		sql += "       AND M.\"OvduDays\" >= 90 ";
		sql += " ) ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += "        , \"BadDebtMonth\" ";
		sql += " ORDER BY \"YearMonth\" ";
		sql += "        , \"BadDebtMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);

		return this.convertToMap(query);
	}

	/**
	 * Query for LM036 表四 Deliquency
	 * 
	 * @param startMonth 資料範圍年月-起(西元)
	 * @param endMonth   資料範圍年月-迄(西元)
	 * @param titaVo     titaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> queryDelinquency(int startMonth, int endMonth, TitaVo titaVo) {
		this.info("LM036ServiceImpl queryDelinquency ");

		this.info("LM036ServiceImpl startMonth = " + startMonth);
		this.info("LM036ServiceImpl endMonth = " + endMonth);

		String sql = "";
		sql += " SELECT \"YearMonth\" "; // F0 資料年月
		sql += "      , SUM(\"EntNormal\")        AS \"EntNormal\" "; // F1 法人正常
		sql += "      , SUM(\"EntOvdue1To2\")     AS \"EntOvdue1To2\" "; // F2 法人逾1~2期
		sql += "      , SUM(\"EntOvdue3To6\")     AS \"EntOvdue3To6\" "; // F3 法人逾3~6期
		sql += "      , SUM(\"EntColl\")          AS \"EntColl\" "; // F4 法人催收
		sql += "      , SUM(\"NatNormal\")        AS \"NatNormal\" "; // F5 自然人正常
		sql += "      , SUM(\"NatOvdue1To2\")     AS \"NatOvdue1To2\" "; // F6 自然人逾1~2期
		sql += "      , SUM(\"NatOvdue3To6\")     AS \"NatOvdue3To6\" "; // F7 自然人逾3~6期
		sql += "      , SUM(\"NatColl\")          AS \"NatColl\" "; // F8 自然人催收
		sql += "      , SUM(\"Normal\")           AS \"Normal\" "; // F9 總額正常
		sql += "      , SUM(\"Ovdue1To2\")        AS \"Ovdue1To2\" "; // F10 總額逾1~2期
		sql += "      , SUM(\"Ovdue3To6\")        AS \"Ovdue3To6\" "; // F11 總額逾3~6期
		sql += "      , SUM(\"Coll\")             AS \"Coll\" "; // F12 總額催收
		sql += "      , SUM(\"Total\")            AS \"Total\" "; // F13 放款總餘額
		sql += " FROM ( ";
		sql += "     SELECT \"YearMonth\" ";
		sql += "          , \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"OvduDays\" ";
		sql += "          , \"OvduTerm\" ";
		sql += "          , \"EntCode\" ";
		sql += "          , \"AcctCode\" ";
		sql += "          , \"PrinBalance\" ";
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END             AS \"EntNormal\" "; // --法人正常
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntOvdue1To2\" "; // --法人逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntOvdue3To6\" "; // --法人逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') = '1' ";
		sql += "                   AND \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"EntColl\" "; // --法人轉催收
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatNormal\" "; // --自然人正常
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatOvdue1To2\" "; // --自然人逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatOvdue3To6\" "; // --自然人逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN NVL(\"EntCode\", '0') != '1' ";
		sql += "                   AND \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"NatColl\" "; // --自然人轉催收
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" = 0 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Normal\" "; // --總額正常
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" >= 1 ";
		sql += "                   AND \"OvduTerm\" <= 2 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Ovdue1To2\" "; // --總額逾1~2期
		sql += "          , CASE ";
		sql += "              WHEN \"OvduTerm\" >= 3 ";
//		sql += "                   AND \"OvduTerm\" <= 6 ";
		sql += "                   AND \"AcctCode\" != '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Ovdue3To6\" "; // --總額逾3~6期
		sql += "          , CASE ";
		sql += "              WHEN \"AcctCode\" = '990' ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "            ELSE 0 ";
		sql += "            END              AS \"Coll\" "; // --總額轉催收
		sql += "          , \"PrinBalance\"  AS \"Total\" "; // --放款總餘額
		sql += "     FROM \"MonthlyFacBal\" ";
		sql += "     WHERE \"PrinBalance\" > 0 ";
		sql += "       AND \"YearMonth\" >= :startMonth ";
		sql += "       AND \"YearMonth\" <= :endMonth ";
		sql += " ) ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += " ORDER BY \"YearMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);

		return this.convertToMap(query);
	}

	/**
	 * Query for LM036 表五 Collection
	 * 
	 * @param startMonth 資料範圍年月-起(西元)
	 * @param endMonth   資料範圍年月-迄(西元)
	 * @param titaVo     titaVo
	 * @return 查詢結果
	 */
	public List<Map<String, String>> queryCollection(int startMonth, int endMonth, TitaVo titaVo) {
		this.info("LM036ServiceImpl queryCollection ");

		this.info("LM036ServiceImpl startMonth = " + startMonth);
		this.info("LM036ServiceImpl endMonth = " + endMonth);

		String sql = "";
		sql += " WITH \"MonthlyTurnedData\" AS ( ";
		sql += " SELECT \"YearMonth\" ";
		sql += "      , \"Type\" ";
		sql += "      , SUM(CASE ";
		sql += "              WHEN \"Type\" >= 1 ";
		sql += "                   AND \"Type\" <= 6 ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "              WHEN \"Type\" = 7 ";
		sql += "              THEN \"OvduBal\" ";
		sql += "              WHEN \"Type\" = 8 ";
		sql += "              THEN \"BadDebtBal\" ";
		sql += "            ELSE 0 END ";
		sql += "           ) AS \"Total\" ";
		sql += " FROM ( ";
		sql += "     SELECT TM.\"YearMonth\" ";
		sql += "          , CASE ";
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 0 ";
		sql += "                   AND TM.\"OvduTerm\" = 1 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 1 "; // -- 1 = M0 TO M1
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 1 ";
		sql += "                   AND TM.\"OvduTerm\" = 2 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 2 "; // -- 2 = M1 TO M2
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 2 ";
		sql += "                   AND TM.\"OvduTerm\" = 3 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 3 "; // -- 3 = M2 TO M3
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 3 ";
		sql += "                   AND TM.\"OvduTerm\" = 4 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 4 "; // -- 4 = M3 TO M4
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 4 ";
		sql += "                   AND TM.\"OvduTerm\" = 5 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 5 "; // -- 5 = M4 TO M5
		sql += "              WHEN NVL(LM.\"OvduTerm\",0) = 5 ";
		sql += "                   AND TM.\"OvduTerm\" >= 6 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 6 "; // -- 6 = M5 TO M6
		sql += "              WHEN NVL(LM.\"AcctCode\",'000') != '990' ";
		sql += "                   AND TM.\"AcctCode\" = '990' ";
		sql += "                   AND TM.\"OvduBal\" > 0 ";
		sql += "                   AND TM.\"BadDebtBal\" = 0 ";
		sql += "              THEN 7 "; // -- = M6 TO 轉催
		sql += "              WHEN NVL(LM.\"AcctCode\",'000') = '990' ";
		sql += "                   AND NVL(LM.\"BadDebtBal\",0) = 0 ";
		sql += "                   AND TM.\"AcctCode\" = '990' ";
		sql += "                   AND TM.\"BadDebtBal\" > 0 ";
		sql += "              THEN 8 "; // -- = 轉催 TO 轉呆
		sql += "            ELSE 0 END AS \"Type\" ";
		sql += "          , TM.\"PrinBalance\" ";
		sql += "          , TM.\"OvduBal\"  ";
		sql += "          , TM.\"BadDebtBal\" ";
		sql += "     FROM \"MonthlyFacBal\" TM ";// -- This Month
		sql += "     LEFT JOIN \"MonthlyFacBal\" LM ON LM.\"YearMonth\" = \"Fn_GetLastMonth\"(TM.\"YearMonth\") ";
		sql += "                                 AND LM.\"CustNo\" = TM.\"CustNo\" ";
		sql += "                                 AND LM.\"FacmNo\" = TM.\"FacmNo\" ";
		sql += "     WHERE TM.\"PrinBalance\" + TM.\"OvduBal\" + TM.\"BadDebtBal\" > 0 ";
		sql += "       AND TM.\"YearMonth\" >= :startMonth ";
		sql += "       AND TM.\"YearMonth\" <= :endMonth ";
		sql += "     UNION ALL"; // 補零
		sql += "     SELECT S0.\"YearMonth\" ";
		sql += "          , S1.\"Type\" ";
		sql += "          , 0 AS \"PrinBalance\" ";
		sql += "          , 0 AS \"OvduBal\"  ";
		sql += "          , 0 AS \"BadDebtBal\" ";
		sql += "     FROM ( SELECT DISTINCT ";
		sql += "                   \"YearMonth\" ";
		sql += "            FROM \"MonthlyFacBal\" ";
		sql += "            WHERE \"YearMonth\" >= :startMonth ";
		sql += "              AND \"YearMonth\" <= :endMonth ";
		sql += "          ) S0 ";
		sql += "        , ( SELECT 0 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 1 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 2 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 3 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 4 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 5 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 6 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 7 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "            UNION ALL ";
		sql += "            SELECT 8 AS \"Type\" ";
		sql += "            FROM DUAL ";
		sql += "          ) S1 ";
		sql += " ) ";
		sql += " WHERE \"Type\" > 0 ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += "        , \"Type\" ";
		sql += " ) ";
		sql += " , \"MonthlyData\" AS ( ";
		sql += " SELECT \"YearMonth\" ";
		sql += "      , \"Type\" ";
		sql += "      , SUM(CASE ";
		sql += "              WHEN \"Type\" >= 1 ";
		sql += "                   AND \"Type\" <= 6 ";
		sql += "              THEN \"PrinBalance\" ";
		sql += "              WHEN \"Type\" = 7 ";
		sql += "              THEN \"OvduBal\" ";
		sql += "              WHEN \"Type\" = 8 ";
		sql += "              THEN \"BadDebtBal\" ";
		sql += "            ELSE 0 END ";
		sql += "           ) AS \"Total\" ";
		sql += " FROM ( ";
		sql += "     SELECT TM.\"YearMonth\" ";
		sql += "          , CASE ";
		sql += "              WHEN TM.\"OvduTerm\" = 1 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 1 "; // -- 1 = M1
		sql += "              WHEN TM.\"OvduTerm\" = 2 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 2 "; // -- 2 = M2
		sql += "              WHEN TM.\"OvduTerm\" = 3 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 3 "; // -- 3 = M3
		sql += "              WHEN TM.\"OvduTerm\" = 4 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 4 "; // -- 4 = M4
		sql += "              WHEN TM.\"OvduTerm\" = 5 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 5 "; // -- 5 = M5
		sql += "              WHEN TM.\"OvduTerm\" >= 6 ";
		sql += "                   AND TM.\"AcctCode\" != '990' ";
		sql += "              THEN 6 "; // -- 6 = M6
		sql += "              WHEN TM.\"AcctCode\" = '990' ";
		sql += "                   AND TM.\"OvduBal\" > 0 ";
		sql += "                   AND TM.\"BadDebtBal\" = 0 ";
		sql += "              THEN 7 "; // -- 7 = 轉催
		sql += "            ELSE 0 END AS \"Type\" ";
		sql += "          , TM.\"PrinBalance\" ";
		sql += "          , TM.\"OvduBal\"  ";
		sql += "          , TM.\"BadDebtBal\" ";
		sql += "     FROM \"MonthlyFacBal\" TM "; // -- This Month
		sql += "     WHERE TM.\"PrinBalance\" + TM.\"OvduBal\" + TM.\"BadDebtBal\" > 0 ";
		sql += "       AND TM.\"YearMonth\" >= \"Fn_GetLastMonth\"( :startMonth ) ";
		sql += "       AND TM.\"YearMonth\" <= :endMonth ";
		sql += " ) ";
		sql += " WHERE \"Type\" > 0 ";
		sql += " GROUP BY \"YearMonth\" ";
		sql += "        , \"Type\" ";
		sql += " ) ";
		sql += " SELECT TM.\"YearMonth\" ";
		sql += "      , TM.\"Type\" ";
		sql += "      , TM.\"Total\" ";
		sql += "      , LM.\"Type\"  AS \"LastMonthType\" ";
		sql += "      , LM.\"Total\" AS \"LastMonthTotal\" ";
		sql += "      , CASE ";
		sql += "          WHEN NVL(LM.\"Total\",0) > 0 ";
		sql += "          THEN ROUND(TM.\"Total\" / LM.\"Total\",4) * 100 ";
		sql += "        ELSE 100 END AS \"RollingRate\" ";
		sql += " FROM \"MonthlyTurnedData\" TM ";
		sql += " LEFT JOIN \"MonthlyData\" LM ON LM.\"YearMonth\" = \"Fn_GetLastMonth\"(TM.\"YearMonth\") ";
		sql += "                           AND LM.\"Type\" = TM.\"Type\" - 1 ";
		sql += " WHERE TM.\"YearMonth\" >= :startMonth ";
		sql += "   AND TM.\"YearMonth\" <= :endMonth ";
		sql += "   AND TM.\"Type\" >= 2 ";
		sql += " ORDER BY TM.\"YearMonth\" ";
		sql += "        , TM.\"Type\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startMonth", startMonth);
		query.setParameter("endMonth", endMonth);

		return this.convertToMap(query);
	}

}