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
public class LM003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int tYearMonthMin = parse
				.stringToInteger(titaVo.getParam("inputYearStart") + "" + titaVo.getParam("inputMonthStart")) + 191100;
		int tYearMonthMax = parse
				.stringToInteger(titaVo.getParam("inputYearEnd") + "" + titaVo.getParam("inputMonthEnd")) + 191100;

		// 因起始年月一定為1月，要找前一月份固定前一年的12月份
		int lYearMonthMin = parse.stringToInteger(titaVo.getParam("inputYearStart")) * 100 + 191012;

		int lYearMonthMax = 0;
		// 如果起止年月相同(表示都是某年1月份)，直接等於lYearMonthMin
		if (tYearMonthMin == tYearMonthMax) {
			lYearMonthMax = lYearMonthMin;
		} else {
			// 排除1月份後，YearMonthMax只要減1為lYearMonthMax
			lYearMonthMax = tYearMonthMax - 1;
		}

		this.info("lM003.findAll ");
		String sql = "";
		sql += "    WITH \"TotalDrawdownAmt\" AS (";
		sql += "        SELECT";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 6) \"YearMonth\"";
		sql += "            , SUM(lbm.\"DrawdownAmt\") \"DrawdownAmt\"";
		sql += "        FROM";
		sql += "            \"LoanBorMain\"   lbm";
		sql += "            LEFT JOIN \"CustMain\"      cm ON cm.\"CustNo\" = lbm.\"CustNo\"";
		sql += "        WHERE";
		sql += "            lbm.\"RenewFlag\" = 0";
		sql += "            AND cm.\"EntCode\" != 1";
		sql += "              AND trunc(lbm.\"DrawdownDate\" / 100) BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "        GROUP BY";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 6)";
		sql += "    ), \"TurnOvduAmt\" AS (";
		sql += "        SELECT";
		sql += "            substr(lbtx.\"AcDate\", 1, 6";
		sql += "        ) \"YearMonth\"";
		sql += "            , SUM(\"Principal\") \"NoCloseAmt3\"";
		sql += "        FROM";
		sql += "            \"LoanBorTx\" lbtx";
		sql += "        WHERE";
		sql += "            lbtx.\"TitaTxCd\" = 'L3420'";
		sql += "            AND lbtx.\"TxDescCode\" = '3423'";
		sql += "            AND lbtx.\"TitaHCode\" = 0";
		sql += "            AND trunc(lbtx.\"AcDate\" / 100) BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "        GROUP BY";
		sql += "            substr(lbtx.\"AcDate\", 1";
		sql += "               , 6";
		sql += "        )";
		sql += "    ), \"NoCloseAmt\" AS (";
		sql += "        SELECT";
		sql += "            \"YearMonth\"";
		sql += "            , SUM(DECODE(\"Type\", 'PartlyRepay', \"TxAmt\", 0)) AS \"PartlyRepay\"";
		sql += "            , SUM(DECODE(\"Type\", 'Tenty', \"TxAmt\", 0)) AS \"Tenty\"";
		sql += "        FROM";
		sql += "            (";
		sql += "                SELECT";
		sql += "                    substr(lbtx.\"AcDate\", 1, 6) \"YearMonth\"";
		sql += "                    , CASE";
		sql += "                        WHEN lbtx.\"TitaTxCd\" IN (";
		sql += "                            'L3200'";
		sql += "                        )";
		sql += "                             AND lbtx.\"TxDescCode\" <> 3203 THEN";
		sql += "                            'Tenty'";
		sql += "                        WHEN  lbtx.\"TitaTxCd\" = 'L3200'";
		sql += "                        THEN 'PartlyRepay'";
		sql += "                        ELSE";
		sql += "                            '其他'";
		sql += "                    END AS \"Type\"";
		sql += "                    , lbtx.\"Principal\" \"TxAmt\"";
		sql += "                FROM";
		sql += "                    \"LoanBorTx\"        lbtx";
		sql += "                    LEFT JOIN \"LoanBorMain\"      lbm ON lbm.\"CustNo\" = lbtx.\"CustNo\"";
		sql += "                                                   AND lbm.\"FacmNo\" = lbtx.\"FacmNo\"";
		sql += "                                                   AND lbm.\"BormNo\" = lbtx.\"BormNo\"";
		sql += "                    LEFT JOIN \"MonthlyLoanBal\"   mlb ON mlb.\"CustNo\" = lbtx.\"CustNo\"";
		sql += "                                                      AND mlb.\"FacmNo\" = lbtx.\"FacmNo\"";
		sql += "                                                      AND mlb.\"BormNo\" = lbtx.\"BormNo\"";
		sql += "                                                      AND mlb.\"YearMonth\" = substr(lbtx.\"AcDate\", 1, 6)";
		sql += "                    LEFT JOIN \"CustMain\"         cm ON cm.\"CustNo\" = lbtx.\"CustNo\"";
		sql += "                WHERE";
		sql += "                    trunc(lbtx.\"AcDate\" / 100) BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "                    AND lbtx.\"TitaHCode\" = 0";
		sql += "                    AND mlb.\"LoanBalance\" > 0";
		sql += "                    AND lbm.\"Status\" IN (";
		sql += "                        0";
		sql += "                        , 3";
		sql += "                    )";
		sql += "                    AND cm.\"EntCode\" = 0";
		sql += "            )";
		sql += "        GROUP BY";
		sql += "            \"YearMonth\"";
		sql += "    ), \"CloseAmt\" AS (";
		sql += "        SELECT";
		sql += "            \"YearMonth\"";
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt2', \"TxAmt\", 0))";
		sql += "            + SUM(DECODE(\"Type\", 'CloseAmt11', \"TxAmt\", 0))";
		sql += "            + SUM(DECODE(\"Type\", 'CloseAmt12' , \"TxAmt\", 0)) AS \"CloseAmt2\""; // --自行還款(2+11+12)
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt1', \"TxAmt\", 0)) AS \"CloseAmt1\""; // --買賣(1)
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt3', \"TxAmt\", 0)) + SUM(DECODE(\"Type\", 'CloseAmt4', \"TxAmt\", 0)) AS \"CloseAmt4\""; // --利率高轉貸(3+4)
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt5', \"TxAmt\", 0)) + SUM(DECODE(\"Type\", 'CloseAmt6', \"TxAmt\", 0)) AS \"CloseAmt5\""; // --增貸不准(5+6)
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt7', \"TxAmt\", 0)) AS \"CloseAmt7\""; // --內部代償
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt8', \"TxAmt\", 0)) AS \"CloseAmt8\""; // --身亡
		sql += "            , SUM(DECODE(\"Type\", 'CloseAmt9', \"TxAmt\", 0)) + SUM(DECODE(\"Type\", 'CloseAmt14', \"TxAmt\", 0)) AS \"CloseAmt9\""; // --其他(9+14)
		sql += "        FROM";
		sql += "            (";
		// --LN6361個金還款金額 - 利率高轉貸 - 軍公教轉貸 - 買賣 - 增貸不准 - 動支不准 - 自行還款 - 綁約期還款 - 不滿意服務 -
		// 身亡- 其他 - 部份還款 - 本金攤提
		// --4-3-1-5-6-(2)-11-14-9-部分-本金
		sql += "                SELECT";
		sql += "                    mlb.\"YearMonth\"";
		sql += "                    , CASE";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 0 THEN";
		sql += "                            'CloseAmt0'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 1 THEN";
		sql += "                            'CloseAmt1'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 2 THEN";
		sql += "                            'CloseAmt2'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 3 THEN";
		sql += "                            'CloseAmt3'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 4 THEN";
		sql += "                            'CloseAmt4'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 5 THEN";
		sql += "                            'CloseAmt5'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 6 THEN";
		sql += "                            'CloseAmt6'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 7 THEN";
		sql += "                            'CloseAmt7'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 8 THEN";
		sql += "                            'CloseAmt8'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 9 THEN";
		sql += "                            'CloseAmt9'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 11 THEN";
		sql += "                            'CloseAmt11'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 12 THEN";
		sql += "                            'CloseAmt11'";
		sql += "                        WHEN nvl(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\") = 14 THEN";
		sql += "                            'CloseAmt14'";
		sql += "                        ELSE";
		sql += "                            'CloseElse'";
		sql += "                    END AS \"Type\"";
		sql += "                    , mlblast.\"LoanBalance\" AS \"TxAmt\"";
		sql += "                FROM";
		sql += "                    \"MonthlyLoanBal\"   mlb";
		sql += "                    LEFT JOIN \"FacMain\"          fm ON fm.\"CustNo\" = mlb.\"CustNo\"";
		sql += "                                              AND fm.\"FacmNo\" = mlb.\"FacmNo\"";
		sql += "                    LEFT JOIN \"LoanBorMain\"      lbm ON lbm.\"CustNo\" = mlb.\"CustNo\"";
		sql += "                                                   AND lbm.\"FacmNo\" = mlb.\"FacmNo\"";
		sql += "                                                   AND lbm.\"BormNo\" = mlb.\"BormNo\"";
		sql += "                    LEFT JOIN (";
		sql += "                        SELECT";
		sql += "                            \"CustNo\"";
		sql += "                            , \"FacmNo\"";
		sql += "                            , \"BormNo\"";
		sql += "                            , \"TxAmt\"";
		sql += "                            , \"TempAmt\"";
		sql += "                            , \"TitaTxCd\"";
		sql += "                            , \"TxDescCode\"";
		sql += "                            , \"AcDate\"";
		sql += "                            , ROW_NUMBER() OVER(";
		sql += "                                PARTITION BY \"CustNo\", \"FacmNo\", \"BormNo\", substr(\"AcDate\", 1, 6)";
		sql += "                                ORDER BY";
		sql += "                                    \"AcDate\" DESC";
		sql += "                            ) \"Seq\"";
		sql += "                        FROM";
		sql += "                            \"LoanBorTx\"";
		sql += "                        WHERE";
		sql += "                            nvl(JSON_VALUE(\"OtherFields\", '$.CaseCloseCode'), 999) = 0";
		sql += "                            AND substr(\"AcDate\", 1, 6) BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "                    ) lbtx ON lbtx.\"Seq\" = 1";
		sql += "                              AND lbtx.\"CustNo\" = mlb.\"CustNo\"";
		sql += "                              AND lbtx.\"FacmNo\" = mlb.\"FacmNo\"";
		sql += "                              AND lbtx.\"BormNo\" = mlb.\"BormNo\"";
		sql += "                              AND substr(lbtx.\"AcDate\", 1, 6) = mlb.\"YearMonth\"";
		sql += "                    LEFT JOIN \"FacClose\"         fc ON fc.\"CustNo\" = mlb.\"CustNo\"";
		sql += "                                               AND ( fc.\"FacmNo\" = 0";
		sql += "                                                     OR fc.\"FacmNo\" = mlb.\"FacmNo\" )";
		sql += "                                               AND fc.\"CloseDate\" = lbtx.\"EntryDate\"";
		sql += "                    LEFT JOIN \"MonthlyLoanBal\"   mlblast ON floor(months_between(TO_DATE(mlb.\"YearMonth\", 'YYYYMM'), TO_DATE(mlblast.\"YearMonth\"";
		sql += "                    , 'YYYYMM'))) = 1";
		sql += "                                                          AND mlblast.\"CustNo\" = mlb.\"CustNo\"";
		sql += "                                                          AND mlblast.\"FacmNo\" = mlb.\"FacmNo\"";
		sql += "                                                          AND mlblast.\"BormNo\" = mlb.\"BormNo\"";
		sql += "                WHERE";
		sql += "                    mlb.\"EntCode\" = 0 ";
		sql += "                    AND lbm.\"RenewFlag\" = 0";
		sql += "                    AND lbm.\"Status\" IN (";
		sql += "                        0";
		sql += "                        , 3";
		sql += "                    )";
		sql += "                    AND lbtx.\"CustNo\" IS NOT NULL";
		sql += "                    AND mlb.\"YearMonth\" BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "            )";
		sql += "        GROUP BY";
		sql += "            \"YearMonth\"";
		sql += "    ), \"EOMBalanceByEntCode\" AS (";
		sql += "        SELECT";
		sql += "            mlb.\"YearMonth\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, 0, mlb.\"LoanBalance\")) \"Ent0Amt\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, mlb.\"LoanBalance\", 0)) \"Ent1Amt\"";
		sql += "        FROM";
		sql += "            \"MonthlyLoanBal\" mlb";
		sql += "        WHERE";
		sql += "            mlb.\"YearMonth\" BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "            AND mlb.\"LoanBalance\" > 0";
		sql += "            AND mlb.\"AcctCode\" IN (";
		sql += "                '310'";
		sql += "                , '320'";
		sql += "                , '330'";
		sql += "                , '340'";
		sql += "            )";
		sql += "        GROUP BY";
		sql += "            mlb.\"YearMonth\"";
		sql += "    ), \"NaturalDrawdownAmtYearly\" AS (";
		sql += "        SELECT";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 4) \"DrawdownYear\"";
		sql += "            , SUM(lbm.\"DrawdownAmt\") \"TxAmt\"";
		sql += "        FROM";
		sql += "            \"LoanBorMain\"   lbm";
		sql += "            LEFT JOIN \"CustMain\"      cm ON cm.\"CustNo\" = lbm.\"CustNo\"";
		sql += "        WHERE";
		sql += "            cm.\"EntCode\" = 2";
		sql += "            AND lbm.\"RenewFlag\" = 0";
		sql += "        GROUP BY";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 4)";
		sql += "    ), \"LastEOMBalanceByEntCode\" AS (";
		sql += "        SELECT";
		sql += "            mlb.\"YearMonth\"";
		sql += "            , TO_CHAR(add_months(TO_DATE(mlb.\"YearMonth\" || '01', 'yyyymmdd'), 1), 'yyyymm') AS \"tYearMonth\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, 0, mlb.\"LoanBalance\")) \"Ent0Amt\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, mlb.\"LoanBalance\", 0)) \"Ent1Amt\"";
		sql += "        FROM";
		sql += "            \"MonthlyLoanBal\" mlb";
		sql += "        WHERE";
		sql += "            mlb.\"YearMonth\" BETWEEN :lYearMonthMin AND :lYearMonthMax";
		sql += "            AND mlb.\"LoanBalance\" > 0";
		sql += "            AND mlb.\"AcctCode\" IN (";
		sql += "                '310'";
		sql += "                , '320'";
		sql += "                , '330'";
		sql += "                , '340'";
		sql += "            )";
		sql += "        GROUP BY";
		sql += "            mlb.\"YearMonth\"";
		sql += "            , TO_CHAR(add_months(TO_DATE(mlb.\"YearMonth\" || '01', 'yyyymmdd'), 1), 'yyyymm')";
		sql += "    ), \"990BalanceByEntCode\" AS (";
		sql += "        SELECT";
		sql += "            mlb.\"YearMonth\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, 0, mlb.\"LoanBalance\")) \"Ent0Amt\"";
		sql += "            , SUM(DECODE(mlb.\"EntCode\", 1, mlb.\"LoanBalance\", 0)) \"Ent1Amt\"";
		sql += "        FROM";
		sql += "            \"MonthlyLoanBal\" mlb";
		sql += "        WHERE";
		sql += "            mlb.\"YearMonth\" BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "            AND mlb.\"LoanBalance\" > 0";
		sql += "            AND mlb.\"AcctCode\" IN (";
		sql += "                990";
		sql += "            )";
		sql += "        GROUP BY";
		sql += "            mlb.\"YearMonth\"";
		sql += "    ), \"EntPersonPartlyRepay\" AS (";
		sql += "        SELECT";
		sql += "            substr(lbtx.\"AcDate\", 1, 6) \"YearMonth\"";
		sql += "            , 'EntPersonPartlyRepay' AS \"Type\"";
		sql += "            , SUM(lbtx.\"Principal\") \"EntPersonPartlyRepay\"";
		sql += "        FROM";
		sql += "            \"LoanBorTx\"   lbtx";
		sql += "            LEFT JOIN \"CustMain\"    cm ON cm.\"CustNo\" = lbtx.\"CustNo\"";
		sql += "        WHERE";
		sql += "            trunc(lbtx.\"AcDate\" / 100) BETWEEN :YearMonthMin AND :YearMonthMax";
		sql += "            AND lbtx.\"TitaHCode\" = 0";
		sql += "            AND cm.\"EntCode\" IN (";
		sql += "                2";
		sql += "            )";
		sql += "            AND lbtx.\"TitaTxCd\" = 'L3200' ";
		sql += "        GROUP BY";
		sql += "            substr(lbtx.\"AcDate\", 1, 6)";
		sql += "    ), \"NaturalDrawdownAmtYearly\" AS (";
		sql += "        SELECT";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 4) \"DrawdownYear\"";
		sql += "            , SUM(lbm.\"DrawdownAmt\") \"TxAmt\"";
		sql += "        FROM";
		sql += "            \"LoanBorMain\"   lbm";
		sql += "            LEFT JOIN \"CustMain\"      cm ON cm.\"CustNo\" = lbm.\"CustNo\"";
		sql += "        WHERE";
		sql += "            cm.\"EntCode\" = 2";
		sql += "            AND lbm.\"RenewFlag\" = 0";
		sql += "        GROUP BY";
		sql += "            substr(lbm.\"DrawdownDate\", 1, 4)";
		sql += "    ), \"OutputYearMonth\" AS (";
		sql += "        SELECT";
		sql += "            TO_CHAR(add_months(TO_DATE(:YearMonthMin, 'YYYYMM'), level - 1), 'YYYYMM') \"YearMonth\"";
		sql += "        FROM";
		sql += "            dual";
		sql += "        CONNECT BY";
		sql += "            level <= ceil(months_between(TO_DATE(:YearMonthMax, 'YYYYMM'), TO_DATE(:YearMonthMin, 'YYYYMM'))) + 1";
		sql += "    )";
		sql += "    SELECT";
		sql += "        \"YearMonth\"";
		sql += "        , \"DrawdownAmt\"";
		sql += "        , \"CloseAmtSeq1\"";
		sql += "        , \"CloseAmtSeq2\"";
		sql += "        , \"RepayTotal\"";
		sql += "        - \"CloseAmtSeq1\" ";
		sql += "        - \"CloseAmtSeq2\" ";
		sql += "        - \"NoCloseAmtSeq1\" ";
		sql += "        - \"NoCloseAmtSeq2\" AS \"CloseAmtSeq3\"";
		// --CloseAmtSeq3(自行還款等) = (期初餘額-期末餘額-轉催收) - (利率高轉貸+軍公教) - (買賣) - (部分還款)-(本金攤提)
		sql += "        , \"NoCloseAmtSeq1\"";
		sql += "        , \"NoCloseAmtSeq2\"";
		sql += "        , \"NoCloseAmtSeq3\"";
		sql += "        , \"RepayTotal\" ";
		sql += "        + \"NoCloseAmtSeq3\" AS \"RepayTotal\"";
		sql += "        , \"Ent0Amt\"";
		sql += "        , \"Ent1Amt\"";
		sql += "        , \"Ent0Amt\" ";
		sql += "        + \"Ent1Amt\" AS \"EntTotalAmt\"";
		sql += "        , \"RepayTotal\" ";
		sql += "        - \"CloseAmtSeq1\" ";
		sql += "        - \"CloseAmtSeq2\" ";
		sql += "        - \"CloseAmt5\" ";
		sql += "        - \"CloseAmt2\" ";
		sql += "        - \"CloseAmt8\" ";
		sql += "        - \"CloseAmt9\" ";
		sql += "        - \"NoCloseAmtSeq1\" ";
		sql += "        - \"NoCloseAmtSeq2\" AS \"CloseAmt7\"";
		// CloseAmt7(內部代償?)=(期初餘額-期末餘額+撥款-轉催收) - (利率高轉貸+軍公教) - (買賣) - (增貸不准+動支不准) -
		// (自行還款+綁約期還款+不滿意服務) -(身亡)-(其他)-(部分還款)-(本金攤提)";
		sql += "        , \"EntPerAmt\"";
		sql += "    FROM";
		sql += "        (";
		sql += "            SELECT";
		sql += "                m.\"YearMonth\" AS \"YearMonth\"";
		sql += "                , round(nvl(drawdownamt.\"DrawdownAmt\", 0) / 100000000, 2) AS \"DrawdownAmt\""; // --撥款
		sql += "                , round(nvl(closeamt.\"CloseAmt4\", 0) / 100000000, 2) AS \"CloseAmtSeq1\""; // --利率高轉貸(3+4)
		sql += "                , round(nvl(closeamt.\"CloseAmt1\", 0) / 100000000, 2) AS \"CloseAmtSeq2\""; // --買賣
		sql += "                , round(nvl(closeamt.\"CloseAmt5\", 0) / 100000000, 2) AS \"CloseAmt5\""; // --增貸不准(5+6)
		sql += "                , round(nvl(closeamt.\"CloseAmt8\", 0) / 100000000, 2) AS \"CloseAmt8\""; // --身亡
		sql += "                , round(nvl(closeamt.\"CloseAmt9\", 0) / 100000000, 2) AS \"CloseAmt9\""; // --其他
		sql += "                , round(nvl(closeamt.\"CloseAmt2\", 0) / 100000000, 2) AS \"CloseAmt2\""; // --自行還款(2+11+12)
		sql += "                , round(nvl(nocloseamt.\"PartlyRepay\", 0) / 100000000, 2) AS \"NoCloseAmtSeq1\""; // --部分還款
		sql += "                , round(nvl(nocloseamt.\"Tenty\", 0) / 100000000, 2) AS \"NoCloseAmtSeq2\""; // --本金攤提
		sql += "                , round(nvl(turnovduamt.\"NoCloseAmt3\", 0) / 100000000, 2) AS \"NoCloseAmtSeq3\""; // --轉催收
		sql += "                , round(nvl(eombalancebyentcode.\"Ent0Amt\", 0) / 100000000, 2) AS \"Ent0Amt\""; // --期末餘額(個)
		sql += "                , round(nvl(eombalancebyentcode.\"Ent1Amt\", 0) / 100000000, 2) AS \"Ent1Amt\""; // --期末餘額(企)
		sql += "                , round(nvl(lasteombalancebyentcode.\"Ent0Amt\", 0) / 100000000, 2) AS \"LastEnt0Amt\""; // --期初餘額(個)
		sql += "                , round(nvl(lasteombalancebyentcode.\"Ent1Amt\", 0) / 100000000, 2) AS \"LastEnt1Amt\""; // --期初餘額(企)
		sql += "                , round(nvl(ovduamt.\"Ent0Amt\", 0) / 100000000, 2) AS \"Ent0OvduAmt\""; // --催收(個)
		sql += "                , round(nvl(ovduamt.\"Ent1Amt\", 0) / 100000000, 2) AS \"Ent1OvduAmt\""; // --催收(企)
		sql += "                , round(nvl(er.\"EntPersonPartlyRepay\", 0) / 100000000, 2) AS \"EntPerAmt\""; // --企金自然人
		sql += "                , round((nvl(lasteombalancebyentcode.\"Ent0Amt\", 0) ";
		sql += "			    - nvl(eombalancebyentcode.\"Ent0Amt\", 0) ";
		sql += "				+ nvl(drawdownamt.\"DrawdownAmt\", 0) ";
		sql += "                - nvl(turnovduamt.\"NoCloseAmt3\", 0)) / 100000000, 2) AS \"RepayTotal\""; // 合計(不含轉催收)
		sql += "            FROM";
		sql += "                \"OutputYearMonth\"           m";
		sql += "                LEFT JOIN \"TotalDrawdownAmt\"          drawdownamt ON drawdownamt.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"CloseAmt\"                  closeamt ON closeamt.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"NoCloseAmt\"                nocloseamt ON nocloseamt.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"TurnOvduAmt\"               turnovduamt ON turnovduamt.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"EOMBalanceByEntCode\"       eombalancebyentcode ON eombalancebyentcode.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"LastEOMBalanceByEntCode\"   lasteombalancebyentcode ON lasteombalancebyentcode.\"tYearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"990BalanceByEntCode\"       ovduamt ON ovduamt.\"YearMonth\" = m.\"YearMonth\"";
		sql += "                LEFT JOIN \"EntPersonPartlyRepay\"      er ON er.\"YearMonth\" = m.\"YearMonth\"";
		sql += "        )";
		sql += "    ORDER BY";
		sql += "        \"YearMonth\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("YearMonthMin", tYearMonthMin);
		query.setParameter("YearMonthMax", tYearMonthMax);
		query.setParameter("lYearMonthMin", lYearMonthMin);
		query.setParameter("lYearMonthMax", lYearMonthMax);

		return this.convertToMap(query);
	}

}