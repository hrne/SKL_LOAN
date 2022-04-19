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
		this.info("lM003.findAll ");
		String sql = "";

		// 撥款金額
		sql += " WITH TotalDrawdownAmt AS ( ";
		sql += " SELECT SUBSTR(lbm.\"DrawdownDate\", 1, 6) \"YearMonth\" ";
		sql += "       ,SUM(lbm.\"DrawdownAmt\") \"Total\" ";
		sql += " FROM \"LoanBorMain\" lbm ";
		sql += " LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = lbm.\"CustNo\" ";
		sql += " WHERE lbm.\"RenewFlag\" = 'N' ";
		sql += "   AND cm.\"EntCode\" != 1 ";
		sql += " GROUP BY SUBSTR(lbm.\"DrawdownDate\", 1, 6) ";
		sql += " ) ";
		sql += "  ";

		// 結清三種
		// 算法是取上月MonthlyLoanBal餘額，因為根據LoanBorTx來查，存在這種情況：結案交易以外同一天同撥款有一般回收的交易
		// 但在樣張上的金額，單取結案交易金額不會對，與這些交易加起來才相符，因此以放款交易檔回溯出金額較困難
		// 舊有資料也不一定有清償金額，譬如有已提前清償但清償檔沒有資料的額度這種資料

		// 最後以折衷方式，只要有清償原因或提前清償原因，且本月餘額為0，就抓上月的餘額來帶入結清金額
		// 實際上原程式也是以類似的方式處理（額度月累積檔）
		// 此算法金額與在買賣、高轉貸項目與樣張相符
		// 但「自行還款等」類有差異

		// 此方法有問題是，現行系統沒有每個月的提前清償原因狀況
		// 所以一旦額度主檔有提前清償原因，而清償檔沒有資料
		// 歷史資料的金額會錯誤

		sql += " , CloseAmt AS ( ";
		sql += " SELECT mlb.\"YearMonth\" ";
		sql += "       ,SUM(DECODE(NVL(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\"), 4, mlbLast.\"LoanBalance\", 0)) \"CloseAmt4\" ";
		sql += "       ,SUM(DECODE(NVL(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\"), 1, mlbLast.\"LoanBalance\", 0)) \"CloseAmt1\" ";
		sql += "       ,SUM(DECODE(NVL(fc.\"CloseReasonCode\", fm.\"AdvanceCloseCode\"), 1, 0, 4, 0, 0, 0, mlbLast.\"LoanBalance\")) \"CloseAmtOthers\" ";
		sql += " FROM \"MonthlyLoanBal\" mlb ";
		sql += " LEFT JOIN \"FacMain\" fm ON fm.\"CustNo\" = mlb.\"CustNo\" ";
		sql += "                       AND fm.\"FacmNo\" = mlb.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" lbm ON lbm.\"CustNo\" = mlb.\"CustNo\" ";
		sql += "                            AND lbm.\"FacmNo\" = mlb.\"FacmNo\" ";
		sql += "                            AND lbm.\"BormNo\" = mlb.\"BormNo\" ";
		sql += " LEFT JOIN (SELECT \"CustNo\" ";
		sql += "                  ,\"FacmNo\" ";
		sql += "                  ,\"BormNo\" ";
		sql += "                  ,\"TxAmt\" ";
		sql += "                  ,\"TempAmt\" ";
		sql += "                  ,\"EntryDate\" ";
		sql += "                  ,ROW_NUMBER() OVER (PARTITION BY \"CustNo\" ";
		sql += "                                                  ,\"FacmNo\" ";
		sql += "                                                  ,\"BormNo\" ";
		sql += "                                                  ,SUBSTR(\"EntryDate\", 1, 6) ";
		sql += "                                      ORDER BY \"EntryDate\" DESC) \"Seq\" ";
		sql += "            FROM \"LoanBorTx\" ";
		sql += "            WHERE NVL(JSON_VALUE(\"OtherFields\", '$.CaseCloseCode'), 999) = 0 ";
		sql += "              AND SUBSTR(\"EntryDate\", 1, 6) BETWEEN :yearMonthMin AND :yearMonthMax ";
		sql += "           ) lbtx ON lbtx.\"Seq\" = 1 ";
		sql += "                 AND lbtx.\"CustNo\" = mlb.\"CustNo\" ";
		sql += "                 AND lbtx.\"FacmNo\" = mlb.\"FacmNo\" ";
		sql += "                 AND lbtx.\"BormNo\" = mlb.\"BormNo\" ";
		sql += "                 AND SUBSTR(lbtx.\"EntryDate\", 1, 6) = mlb.\"YearMonth\" ";
		sql += " LEFT JOIN \"FacClose\" fc ON fc.\"CustNo\" = mlb.\"CustNo\" ";
		sql += "                        AND (fc.\"FacmNo\" = 0 OR fc.\"FacmNo\" = mlb.\"FacmNo\") ";
		sql += "                        AND fc.\"CloseDate\" = lbtx.\"EntryDate\" ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" mlbLast ON FLOOR(MONTHS_BETWEEN(TO_DATE(mlb.\"YearMonth\", 'YYYYMM'), TO_DATE(mlbLast.\"YearMonth\", 'YYYYMM'))) = 1 ";
		sql += "                                   AND mlbLast.\"CustNo\" = mlb.\"CustNo\" ";
		sql += "                                   AND mlbLast.\"FacmNo\" = mlb.\"FacmNo\" ";
		sql += "                                   AND mlbLast.\"BormNo\" = mlb.\"BormNo\" ";
		sql += " WHERE mlb.\"EntCode\" != 1 ";
		sql += "   AND lbm.\"RenewFlag\" = 'N' ";
		sql += "   AND lbm.\"Status\" IN (0,3) ";
		sql += "   AND lbtx.\"CustNo\" is not null ";
		sql += "   AND mlb.\"YearMonth\" BETWEEN :yearMonthMin AND :yearMonthMax ";
		sql += " GROUP BY mlb.\"YearMonth\" ";
		sql += " ORDER BY mlb.\"YearMonth\" ";
		sql += " ) ";
		sql += "  ";

		// 轉催收

		sql += " , TurnOvdu AS ( ";
		sql += " SELECT SUBSTR(lbtx.\"AcDate\", 1, 6) \"YearMonth\" ";
		sql += "       ,SUM(\"Principal\") \"Total\" ";
		sql += " FROM \"LoanBorTx\" lbtx ";
		sql += " WHERE lbtx.\"TitaTxCd\" = 'L3420' ";
		sql += "   AND lbtx.\"TitaHCode\" = 0 ";
		sql += "   AND JSON_VALUE(lbtx.\"OtherFields\", '$.CaseCloseCode') = 3 ";
		sql += " GROUP BY SUBSTR(lbtx.\"AcDate\", 1, 6) ";
		sql += " ) ";
		sql += "  ";

		// 部分還本

		sql += " , PartlyRepay AS ( ";
		sql += " SELECT SUBSTR(lbtx.\"AcDate\", 1, 6) \"YearMonth\" ";
		sql += "       ,SUM(CASE lbtx.\"TitaTxCd\" ";
		sql += "                 WHEN 'L3200' ";
		sql += "                 THEN lbtx.\"ExtraRepay\" ";
		sql += "                 WHEN 'L3420' ";
		sql += "                 THEN lbtx.\"Principal\" ";
		sql += "            ELSE 0 END) \"Total\" ";
		sql += " FROM \"LoanBorTx\" lbtx ";
		sql += " LEFT JOIN \"LoanBorMain\" lbm ON lbm.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += "                            AND lbm.\"FacmNo\" = lbtx.\"FacmNo\" ";
		sql += "                            AND lbm.\"BormNo\" = lbtx.\"BormNo\" ";
		sql += " LEFT JOIN \"MonthlyFacBal\" mfb ON mfb.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += "                              AND mfb.\"FacmNo\" = lbtx.\"FacmNo\" ";
		sql += "                              AND mfb.\"YearMonth\" = SUBSTR(lbtx.\"AcDate\", 1, 6) ";
		sql += " LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += " WHERE lbtx.\"AcDate\" BETWEEN :yearMonthMin || '01' AND :yearMonthMax || '31' ";
		sql += "   AND lbtx.\"TitaHCode\" = 0 ";
		sql += "   AND ((lbtx.\"TitaTxCd\" = 'L3200') ";
		sql += "        OR ";
		sql += "        (    lbtx.\"TitaTxCd\" = 'L3420' ";
		sql += "         AND JSON_VALUE(lbtx.\"OtherFields\", '$.CaseCloseCode') = 0 ";
		sql += "        ) ";
		sql += "       ) ";
		sql += "   AND mfb.\"PrinBalance\" > 0 ";
		sql += "   AND lbm.\"Status\" = 0 ";
		sql += "   AND cm.\"EntCode\" != 1 ";
		sql += " GROUP BY SUBSTR(lbtx.\"AcDate\", 1, 6) ";
		sql += " ) ";
		sql += "  ";

		// 本金攤提

		sql += " , Tenty AS ( ";
		sql += " SELECT SUBSTR(lbtx.\"EntryDate\", 1, 6) \"YearMonth\" ";
		sql += "       ,SUM(lbtx.\"Principal\") \"Total\" ";
		sql += " FROM \"LoanBorTx\" lbtx ";
		sql += " LEFT JOIN \"LoanBorMain\" lbm ON lbm.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += "                            AND lbm.\"FacmNo\" = lbtx.\"FacmNo\" ";
		sql += "                            AND lbm.\"BormNo\" = lbtx.\"BormNo\" ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" mlb ON mlb.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += "                               AND mlb.\"FacmNo\" = lbtx.\"FacmNo\" ";
		sql += "                               AND mlb.\"BormNo\" = lbtx.\"BormNo\" ";
		sql += "                               AND mlb.\"YearMonth\" = SUBSTR(lbtx.\"EntryDate\", 1, 6) ";
		sql += " LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = lbtx.\"CustNo\" ";
		sql += " WHERE lbtx.\"EntryDate\" BETWEEN :yearMonthMin || '01' AND :yearMonthMax || '31' ";
		sql += "   AND lbtx.\"TitaTxCd\" = 'L3200' ";
		sql += "   AND lbtx.\"TitaHCode\" = 0 ";
		sql += "   AND mlb.\"LoanBalance\" > 0 ";
		sql += "   AND lbm.\"Status\" = 0 ";
		sql += "   AND cm.\"EntCode\" != 1 ";
		sql += "   AND JSON_VALUE(lbtx.\"OtherFields\", '$.CaseCloseCode') is null ";
		sql += " GROUP BY SUBSTR(lbtx.\"EntryDate\", 1, 6) ";
		sql += " ORDER BY \"YearMonth\" ";
		sql += " ) ";
		sql += "  ";

		// 企金別月底餘額

		sql += " , EOMBalanceByEntCode AS ( ";
		sql += " SELECT MLB.\"YearMonth\" ";
		sql += "       ,SUM(DECODE(MLB.\"EntCode\", 1, MLB.\"LoanBalance\", 0)) \"TotalEnterprise\" ";
		sql += "       ,SUM(DECODE(MLB.\"EntCode\", 1, 0, MLB.\"LoanBalance\")) \"TotalNatural\" ";
		sql += " FROM \"MonthlyLoanBal\" MLB ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += " WHERE MLB.\"YearMonth\" BETWEEN :yearMonthMin AND :yearMonthMax ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND LBM.\"Status\" = 0 ";
		sql += " GROUP BY MLB.\"YearMonth\" ";
		sql += " ) ";
		sql += "  ";

		// 自然人全年申貸撥款額

		sql += " , NaturalDrawdownAmtYearly AS ( ";
		sql += " SELECT SUBSTR(lbm.\"DrawdownDate\", 1, 4) \"DrawdownYear\" ";
		sql += "       ,SUM(lbm.\"DrawdownAmt\") \"Total\" ";
		sql += " FROM \"LoanBorMain\" lbm ";
		sql += " LEFT JOIN \"CustMain\" cm ON cm.\"CustNo\" = lbm.\"CustNo\" ";
		sql += " WHERE cm.\"EntCode\" = 2 ";
		sql += "   AND lbm.\"RenewFlag\" = 'N' ";
		sql += " GROUP BY SUBSTR(lbm.\"DrawdownDate\", 1, 4) ";
		sql += " ) ";
		sql += "  ";
		sql += " , OutputYearMonth AS ( ";
		sql += " SELECT TO_CHAR(ADD_MONTHS(TO_DATE(:yearMonthMin, 'YYYYMM'), LEVEL - 1), 'YYYYMM') \"YearMonth\" ";
		sql += " FROM DUAL ";
		sql += " CONNECT BY LEVEL <= CEIL(MONTHS_BETWEEN(TO_DATE(:yearMonthMax, 'YYYYMM'), TO_DATE(:yearMonthMin, 'YYYYMM'))) + 1 ";
		sql += " ) ";
		sql += "  ";
		sql += " SELECT S1.\"YearMonth\" \"YearMonth\" "; // F0 年月
		sql += "       ,NVL(S2.\"Total\", 0) \"TotalDrawdownAmt\" "; // F1 撥款金額
		sql += "       ,NVL(S3.\"CloseAmt4\", 0) \"CloseAmtA\" "; // F2 結清：利率高轉貸
		sql += "       ,NVL(S3.\"CloseAmt1\", 0) \"CloseAmtB\" "; // F3 結清：買賣
		sql += "       ,NVL(S3.\"CloseAmtOthers\", 0) \"CloseAmtC\" "; // F4 結清：自行還款等
		sql += "       ,  NVL(S3.\"CloseAmt4\", 0) ";
		sql += "        + NVL(S3.\"CloseAmt1\", 0) ";
		sql += "        + NVL(S3.\"CloseAmtOthers\", 0) \"CloseAmtSum\" "; // F5 結清小計
		sql += "       ,NVL(S4.\"Total\", 0) \"PartlyRepay\" "; // F6 非結清：部分還本
		sql += "       ,NVL(S5.\"Total\", 0) \"Tenty\" "; // F7 非結清：本金攤提
		sql += "       ,NVL(S6.\"Total\", 0) \"TurnOvdu\" "; // F8 非結清：轉催收
		sql += "       ,  NVL(S4.\"Total\", 0) ";
		sql += "        + NVL(S5.\"Total\", 0) ";
		sql += "        + NVL(S6.\"Total\", 0) \"NotClosedSum\" "; // F9 非結清小計
		sql += "       ,  NVL(S3.\"CloseAmt4\", 0) ";
		sql += "        + NVL(S3.\"CloseAmt1\" , 0) ";
		sql += "        + NVL(S3.\"CloseAmtOthers\", 0) ";
		sql += "        + NVL(S4.\"Total\", 0) ";
		sql += "        + NVL(S5.\"Total\", 0) ";
		sql += "        + NVL(S6.\"Total\", 0) \"RepayTotal\" "; // F10 結清與非結清合計
		sql += "       ,NVL(S8.\"TotalNatural\", 0) \"EOMBalance\" "; // F11 月底餘額
		sql += "       ,NVL(S8.\"TotalEnterprise\", 0) \"EOMBalanceEnt\" "; // F12 企金月底餘額
		sql += "       ,NVL(S8.\"TotalNatural\", 0) \"EOMBalanceNat\" "; // F13 自然人月底餘額
		sql += "       ,NVL(S9.\"Total\", 0) \"NatDrawdownYearly\" "; // F14 全年度自然人申貸撥款
		sql += "       ,0 \"Internal\" "; // F15 內部轉帳（預宣告；尚不清楚算法）
		sql += "       ,  NVL(S2.\"Total\", 0) ";
		sql += "        - NVL(S3.\"CloseAmt4\", 0) ";
		sql += "        - NVL(S3.\"CloseAmt1\" , 0) ";
		sql += "        - NVL(S3.\"CloseAmtOthers\", 0) ";
		sql += "        - NVL(S4.\"Total\", 0) ";
		sql += "        - NVL(S5.\"Total\", 0) ";
		sql += "        - NVL(S6.\"Total\", 0) \"Net\" "; // F16 淨增減
		sql += " FROM OutputYearMonth S1 ";
		sql += " LEFT JOIN TotalDrawdownAmt S2 ON S2.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN CloseAmt S3 ON S3.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN PartlyRepay S4 ON S4.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN Tenty S5 ON S5.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN TurnOvdu S6 ON S6.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN EOMBalanceByEntCode S8 ON S8.\"YearMonth\" = S1.\"YearMonth\" ";
		sql += " LEFT JOIN NaturalDrawdownAmtYearly S9 ON S9.\"DrawdownYear\" = SUBSTR(S1.\"YearMonth\", 1, 4) ";
		sql += " ORDER BY S1.\"YearMonth\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("yearMonthMin", parse.stringToInteger(titaVo.getParam("inputYearStart")) + 1911 + titaVo.getParam("inputMonthStart"));
		query.setParameter("yearMonthMax", parse.stringToInteger(titaVo.getParam("inputYearEnd")) + 1911 + titaVo.getParam("inputMonthEnd"));

		return this.convertToMap(query);
	}

}