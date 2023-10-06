package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

/**
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L5735ServiceImpl")
@Repository
public class L5735ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getConstructionCompanyLoanData(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getConstructionCompanyLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

		String sql = "  ";

		sql += " WITH \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"ClCode1\" ";
		sql += "        , \"ClCode2\" ";
		sql += "        , \"ClNo\" ";
		sql += "        , SUM(NVL(\"OriEvaNotWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\"";
		sql += "   WHERE \"MainFlag\" = 'Y' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"ClCode1\" ";
		sql += "          , \"ClCode2\" ";
		sql += "          , \"ClNo\" ";
		sql += " )";
		sql += " SELECT CM.\"CustName\" ";
		sql += "      , MLB.\"CustNo\" ";
		sql += "      , MLB.\"FacmNo\" ";
		sql += "      , MLB.\"BormNo\" ";
		sql += "      , MLB.\"LoanBalance\" ";
		sql += "      , FM.\"LineAmt\" ";
		sql += "      , LBM.\"PrevPayIntDate\" ";
		sql += "      , MLB.\"ClCode1\" ";
		sql += "      , FM.\"UsageCode\" ";
		sql += "      , MLB.\"AcctCode\" ";
		sql += "      , MLB.\"ProdNo\" ";
		sql += "      , LBM.\"Status\" ";
		sql += "      , LBM.\"DrawdownDate\" ";
		sql += "      , NVL(CS.\"EvaNetWorth\", 0) AS \"EvaNetWorth\"";
		sql += "      , CASE ";
		sql += "          WHEN NVL(CS.\"EvaNetWorth\", 0) = ROUND(NVL(CI.\"LoanToValue\",0) / 100,2)";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 2) ";
		sql += "        END                        AS \"LoanRatio\" "; // 貸款成數
		sql += " FROM \"ConstructionCompany\" CC ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"CustNo\" = CC.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                       AND FM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += " LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                       AND CS.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = CS.\"ClCode1\" ";
		sql += "                       AND CI.\"ClCode2\" = CS.\"ClCode2\" ";
		sql += "                       AND CI.\"ClNo\" = CS.\"ClNo\" ";
		sql += " WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND NVL(CC.\"DeleteFlag\", ' ') != '*' ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += " ORDER BY CC.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getNormalCustomerLoanData(int inputDrawdownDate, String subTxCD, TitaVo titaVo) {

		this.info("getNormalCustomerLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

//		L5735A-建商餘額明細 X
//		L5735B-首購餘額明細O
//		L5735D-工業區土地抵押餘額明細O
//		L5735E-正常戶餘額明細O
//		L5735G-住宅貸款餘額明細O
//		L5735I-補助貸款餘額明細O
//		L5735J-政府優惠貸款餘額明細O
//		L5735K-保險業投資不動產及放款情形XS

		String cond = "";
		switch (subTxCD) {
		case "L5735B":
			cond += "   AND MLB.\"AcctCode\" IN ('340') ";
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		// 移到 getNormalCustomerLoanDataL5735D
//		case "L5735D":
//			cond += "   AND MLB.\"ClCode1\" IN ('2') ";
//			cond += "   AND MLB.\"ClCode2\" IN ('3') ";
//			cond += "   AND LBM.\"Status\" = 0 ";
//			break;
		case "L5735E":
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735G":
			cond += "   AND LBM.\"UsageCode\" IN ('02') ";
			cond += "   AND LBM.\"Status\" = 0 ";
			break;
		case "L5735I":
			cond += "   AND MLB.\"ProdNo\" BETWEEN '81' AND '88' ";
			break;
		case "L5735J":
			cond += "   AND MLB.\"ProdNo\" BETWEEN 'IA' AND 'II' ";
			break;
		}

		String sql = "  ";

		sql += " WITH \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"ClCode1\" ";
		sql += "        , \"ClCode2\" ";
		sql += "        , \"ClNo\" ";
		sql += "        , SUM(NVL(\"OriEvaNotWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\"";
		sql += "   WHERE \"MainFlag\" = 'Y' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"ClCode1\" ";
		sql += "          , \"ClCode2\" ";
		sql += "          , \"ClNo\" ";
		sql += " )";
		sql += " SELECT CM.\"CustName\" ";
		sql += "      , MLB.\"CustNo\" ";
		sql += "      , MLB.\"FacmNo\" ";
		sql += "      , MLB.\"BormNo\" ";
		sql += "      , MLB.\"LoanBalance\" ";
		sql += "      , FM.\"LineAmt\" ";
		sql += "      , LBM.\"PrevPayIntDate\" ";
		sql += "      , MLB.\"ClCode1\" ";
		sql += "      , FM.\"UsageCode\" ";
		sql += "      , MLB.\"AcctCode\" ";
		sql += "      , MLB.\"ProdNo\" ";
		sql += "      , LBM.\"Status\" ";
		sql += "      , LBM.\"DrawdownDate\" ";
		sql += "      , NVL(CS.\"EvaNetWorth\", 0) AS \"EvaNetWorth\"";
		sql += "      , CASE ";
		sql += "          WHEN NVL(CS.\"EvaNetWorth\", 0) = ROUND(NVL(CI.\"LoanToValue\",0) / 100,2)";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 2) ";
		sql += "        END                        AS \"LoanRatio\" "; // 貸款成數
		sql += " FROM  \"MonthlyLoanBal\" MLB ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                       AND FM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                    AND CS.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = CS.\"ClCode1\" ";
		sql += "                       AND CI.\"ClCode2\" = CS.\"ClCode2\" ";
		sql += "                       AND CI.\"ClNo\" = CS.\"ClNo\" ";
		sql += " WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += cond;
		sql += " ORDER BY MLB.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getNormalCustomerLoanDataL5735D(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getNormalCustomerLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

		String sql = "  ";

		sql += " WITH \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"OriEvaNotWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\"";
		sql += "   WHERE \"MainFlag\" = 'Y' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
		sql += " SELECT CM.\"CustName\" ";
		sql += "      , MLB.\"CustNo\" ";
		sql += "      , MLB.\"FacmNo\" ";
		sql += "      , MLB.\"BormNo\" ";
		sql += "      , MLB.\"LoanBalance\" ";
		sql += "      , FM.\"LineAmt\" ";
		sql += "      , LBM.\"PrevPayIntDate\" ";
		sql += "      , MLB.\"ClCode1\" ";
		sql += "      , MLB.\"ClCode2\" ";
		sql += "      , FM.\"UsageCode\" ";
		sql += "      , MLB.\"AcctCode\" ";
		sql += "      , MLB.\"ProdNo\" ";
		sql += "      , LBM.\"Status\" ";
		sql += "      , LBM.\"DrawdownDate\" ";
		sql += "      , NVL(CS.\"EvaNetWorth\", 0) AS \"EvaNetWorth\"";
		sql += "      , CASE ";
		sql += "          WHEN NVL(CS.\"EvaNetWorth\", 0) = 0";
		sql += "          THEN 0 ";
		sql += "        ELSE ROUND(FM.\"LineAmt\" / CS.\"EvaNetWorth\", 2) ";
		sql += "        END                        AS \"LoanRatio\" "; // 貸款成數
		sql += " FROM  \"MonthlyLoanBal\" MLB ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                       AND FM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                    AND CS.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += " WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += "   AND LBM.\"Status\" = 0 ";
		sql += "   AND MLB.\"ClCode1\" IN ('2') ";
		sql += "   AND MLB.\"ClCode2\" IN ('3') ";
		sql += "   AND LBM.\"Status\" = 0 ";
		sql += " ORDER BY MLB.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getL5735K(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getL5735K");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

		String sql = "  ";
		// --評估淨值(企金目前為0)
		sql += " WITH \"CFSum\" AS (";
		sql += "     SELECT \"CustNo\"";
		sql += "          , \"FacmNo\"";
		sql += "          , \"ClCode1\"";
		sql += "          , \"ClCode2\"";
		sql += "          , \"ClNo\"";
		sql += "          , SUM(Nvl(";
		sql += "             \"OriEvaNotWorth\", 0";
		sql += "             )) AS \"EvaNetWorth\"";
		sql += "     FROM \"ClFac\"";
		sql += "     WHERE \"MainFlag\" = 'Y'";
		sql += "     GROUP BY \"CustNo\"";
		sql += "            , \"FacmNo\"";
		sql += "            , \"ClCode1\"";
		sql += "            , \"ClCode2\"";
		sql += "            , \"ClNo\"";
		sql += " )";
		// --主要明細資料
		sql += " , \"Main\" AS (";
		sql += "     SELECT Cm.\"CustName\"";
		sql += "          , Mlb.\"CustNo\"";
		sql += "          , Mlb.\"FacmNo\"";
		sql += "          , Mlb.\"BormNo\"";
		sql += "          , Mlb.\"LoanBalance\"";
		sql += "          , Fm.\"LineAmt\"";
		sql += "          , Lbm.\"PrevPayIntDate\"";
		sql += "          , Mlb.\"ClCode1\"";
		sql += "          , Fm.\"UsageCode\"";
		sql += "          , Mlb.\"AcctCode\"";
		sql += "          , Mlb.\"ProdNo\"";
		sql += "          , Lbm.\"Status\"";
		sql += "          , Lbm.\"DrawdownDate\"";
		sql += "          , Nvl(";
		sql += "             Cs.\"EvaNetWorth\", 0";
		sql += "             ) AS \"EvaNetWorth\"";
		sql += "          , CASE";
		sql += "             WHEN Nvl(";
		sql += "                 Cs.\"EvaNetWorth\", 0";
		sql += "             ) = 0 THEN Round(";
		sql += "                 Nvl(";
		sql += "                     Ci.\"LoanToValue\", 0";
		sql += "                 ) / 100, 2";
		sql += "             )";
		sql += "         ELSE Round(";
		sql += "             Fm.\"LineAmt\" / Cs.\"EvaNetWorth\", 2";
		sql += "         )";
		sql += "     END AS \"LoanRatio\"";
		sql += "     FROM \"MonthlyLoanBal\"  Mlb";
		sql += "     LEFT JOIN \"CustMain\"        Cm ON Cm.\"CustNo\" = Mlb.\"CustNo\"";
		sql += "     LEFT JOIN \"FacMain\"         Fm ON Fm.\"CustNo\" = Mlb.\"CustNo\"";
		sql += "                               AND";
		sql += "                               Fm.\"FacmNo\" = Mlb.\"FacmNo\"";
		sql += "     LEFT JOIN \"LoanBorMain\"     Lbm ON Lbm.\"CustNo\" = Mlb.\"CustNo\"";
		sql += "                                    AND";
		sql += "                                    Lbm.\"FacmNo\" = Mlb.\"FacmNo\"";
		sql += "                                    AND";
		sql += "                                    Lbm.\"BormNo\" = Mlb.\"BormNo\"";
		sql += "     LEFT JOIN \"CFSum\"           Cs ON Cs.\"CustNo\" = Fm.\"CustNo\"";
		sql += "                             AND";
		sql += "                             Cs.\"FacmNo\" = Fm.\"FacmNo\"";
		// --若CFSum評估淨值0會找ClImm的貸放成數";
		sql += "     LEFT JOIN \"ClImm\"           Ci ON Ci.\"ClCode1\" = Cs.\"ClCode1\"";
		sql += "                             AND";
		sql += "                             Ci.\"ClCode2\" = Cs.\"ClCode2\"";
		sql += "                             AND";
		sql += "                             Ci.\"ClNo\" = Cs.\"ClNo\"";
		sql += "     WHERE Mlb.\"YearMonth\" = :inputYearMonth";
		sql += "           AND";
		sql += "           Mlb.\"LoanBalance\" > 0";
		sql += "           AND";
		sql += "           Lbm.\"DrawdownDate\" <= :inputDrawdownDate";
		sql += "     ORDER BY Mlb.\"CustNo\"";
		sql += "            , Mlb.\"FacmNo\"";
		sql += "            , Mlb.\"BormNo\"";
		sql += " )";
		sql += " , \"ConsCompany\" AS (";
		sql += "     SELECT 12 AS\"ClCode1\"";
		sql += "          , '12' AS \"UsageCode\"";
		sql += "          , 0    AS \"CustNoCount\"";
		sql += " 		 , NVL(SUM(MLB.\"LoanBalance\"),0) AS \"LoanBalance\" ";
		sql += "          , 0    AS \"LineAmt\"";
		sql += "          , 0    AS \"LoanRatio\"";
		sql += "          , 0    AS \"Count\"   ";
		sql += " 	FROM \"ConstructionCompany\" CC ";
		sql += " 	LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"CustNo\" = CC.\"CustNo\" ";
		sql += " 	LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "	WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   	AND MLB.\"LoanBalance\" > 0 ";
		sql += "   	AND NVL(CC.\"DeleteFlag\", ' ') != '*' ";
		sql += "   	AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += " )";
		// --押品1-用途別：用途別2的土地及建物(住宅抵押貸款)
		sql += " , \"tmpMainUsage2\" AS (";
		sql += "     SELECT \"ClCode1\"";
		sql += "          , \"UsageCode\"";
		sql += "          , COUNT(\"CustNo\")        AS \"CustNoCount\"";
		sql += "          , SUM(\"LoanBalance\")     AS \"LoanBalance\"";
		sql += "          , SUM(\"LineAmt\")         AS \"LineAmt\"";
		sql += "          , SUM(\"LoanRatio\")       AS \"LoanRatio\"";
		sql += "          , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "      , 0";
		sql += "      , 1";
		sql += "     ) AS \"Count\"";
		sql += "     FROM \"Main\"";
		sql += "     WHERE \"ClCode1\" IN (";
		sql += "         '01'";
		sql += "       , '02'";
		sql += "     )";
		sql += "           AND";
		sql += "           \"UsageCode\" = '02'";
		sql += "     GROUP BY \"CustNo\"";
		sql += "            , \"FacmNo\"";
		sql += "            , \"ClCode1\"";
		sql += "            , \"UsageCode\"";
		sql += "            , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "            , 0";
		sql += "            , 1";
		sql += "     )";
		sql += " )";
		// --押品1-用途別：用途別1的土地及土地建物
		sql += " , \"tmpMainUsage1\" AS (";
		sql += "     SELECT \"ClCode1\"";
		sql += "          , \"UsageCode\"";
		sql += "          , COUNT(\"CustNo\")        AS \"CustNoCount\"";
		sql += "          , SUM(\"LoanBalance\")     AS \"LoanBalance\"";
		sql += "          , SUM(\"LineAmt\")         AS \"LineAmt\"";
		sql += "          , SUM(\"LoanRatio\")       AS \"LoanRatio\"";
		sql += "          , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "      , 0";
		sql += "      , 1";
		sql += "     ) AS \"Count\"";
		sql += "     FROM \"Main\"";
		sql += "     WHERE \"ClCode1\" IN (";
		sql += "         '01'";
		sql += "       , '02'";
		sql += "     )";
		sql += "           AND";
		sql += "           \"UsageCode\" = '01'";
		sql += "     GROUP BY \"CustNo\"";
		sql += "            , \"FacmNo\"";
		sql += "            , \"ClCode1\"";
		sql += "            , \"UsageCode\"";
		sql += "            , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "            , 0";
		sql += "            , 1";
		sql += "     )";
		sql += " )";
		// --押品1-用途別：用途別1的股票質押
		sql += " , \"tmpMainUsage13\" AS (";
		sql += "     SELECT \"ClCode1\"";
		sql += "          , \"UsageCode\"";
		sql += "          , COUNT(\"CustNo\")        AS \"CustNoCount\"";
		sql += "          , SUM(\"LoanBalance\")     AS \"LoanBalance\"";
		sql += "          , SUM(\"LineAmt\")         AS \"LineAmt\"";
		sql += "          , SUM(\"LoanRatio\")       AS \"LoanRatio\"";
		sql += "          , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "      , 0";
		sql += "      , 1";
		sql += "     ) AS \"Count\"";
		sql += "     FROM \"Main\"";
		sql += "     WHERE \"ClCode1\" IN (";
		sql += "         '03'";
		sql += "     )";
		sql += "           AND";
		sql += "           \"UsageCode\" = '01'";
		sql += "     GROUP BY \"CustNo\"";
		sql += "            , \"FacmNo\"";
		sql += "            , \"ClCode1\"";
		sql += "            , \"UsageCode\"";
		sql += "            , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "            , 0";
		sql += "            , 1";
		sql += "     )";
		sql += " )";
		// --押品1-用途別：用途別大於3的";
		sql += " , \"tmpMainUsageOther\" AS (";
		sql += "     SELECT 99                     AS \"ClCode1\"";
		sql += "          , '99'                   AS \"UsageCode\"";
		sql += "          , COUNT(\"CustNo\")        AS \"CustNoCount\"";
		sql += "          , SUM(\"LoanBalance\")     AS \"LoanBalance\"";
		sql += "          , SUM(\"LineAmt\")         AS \"LineAmt\"";
		sql += "          , SUM(\"LoanRatio\")       AS \"LoanRatio\"";
		sql += "          , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "      , 0";
		sql += "      , 1";
		sql += "     ) AS \"Count\"";
		sql += "     FROM \"Main\"";
		sql += "     WHERE \"UsageCode\" BETWEEN '03' AND '99'";
		sql += "     GROUP BY \"CustNo\"";
		sql += "            , \"FacmNo\"";
		sql += "            , \"ClCode1\"";
		sql += "            , \"UsageCode\"";
		sql += "            , Decode(";
		sql += "         \"LoanRatio\", 0";
		sql += "            , 0";
		sql += "            , 1";
		sql += "     )";
		sql += " ), \"AvailableFund\" AS (";
		sql += "     SELECT 11 AS\"ClCode1\"";
		sql += "          , '11' AS \"UsageCode\"";
		// --為取得可運用資金的會計日期
		sql += "          , 0    AS \"CustNoCount\"";
		sql += "          , NVL(\"AvailableFunds\",0) AS \"LoanBalance\"";
		sql += "          , 0    AS \"LineAmt\"";
		sql += "          , 0    AS \"LoanRatio\"";
		sql += "          , NVL(\"AcDate\",0)    AS \"Count\"";
		sql += "     FROM \"InnFundApl\"";
		sql += "     WHERE \"AcDate\" = (";
		sql += "         SELECT MAX(\"AcDate\")";
		sql += "         FROM \"InnFundApl\"";
		sql += "         WHERE Trunc(\"AcDate\" / 100) <= :inputYearMonth";
		sql += "           AND \"AvailableFunds\" > 0 ";
		sql += "     )";
		sql += " ),  \"resultMain\" AS (";
		sql += "     SELECT \"ClCode1\"";
		sql += "          , \"UsageCode\"";
		sql += "          , \"LoanBalance\"  AS \"LoanBalance\"";
		// --戶號筆數
		sql += "          , \"CustNoCount\"  AS \"CustNoCount\" ";
		sql += "          , Decode(";
		sql += "         \"CustNoCount\", 0";
		sql += "      , 0";
		sql += "      , \"LineAmt\" / \"CustNoCount\"";
		sql += "     ) AS \"LineAmt\"";
		// --有效筆數(貸放成數大於0%)
		sql += "          , \"Count\"        AS \"effectiveCount\" ";
		sql += "          , Decode(";
		sql += "         \"CustNoCount\", 0";
		sql += "      , 0";
		sql += "      , \"LoanRatio\" / \"CustNoCount\"";
		sql += "     ) AS \"totalLTV\"";
		sql += "     FROM (";
		sql += "         SELECT *";
		sql += "         FROM \"tmpMainUsage2\"";
		sql += "         UNION ALL";
		sql += "         SELECT *";
		sql += "         FROM \"tmpMainUsage1\"";
		sql += "         UNION ALL";
		sql += "         SELECT *";
		sql += "         FROM \"tmpMainUsage13\"";
		sql += "         UNION ALL";
		sql += "         SELECT *";
		sql += "         FROM \"tmpMainUsageOther\"";
		sql += "         UNION ALL";
		sql += "         SELECT *";
		sql += "         FROM \"AvailableFund\"";
		sql += "         UNION ALL";
		sql += "         SELECT *";
		sql += "         FROM \"ConsCompany\"";
		sql += "     )";
		sql += " )";
		sql += " SELECT \"ClCode1\"";
		sql += "      , \"UsageCode\"";
		sql += "      , SUM(\"LoanBalance\")        AS \"LoanBalance\"";
		sql += "      , SUM(\"LineAmt\")            AS \"LineAmt\"";
		sql += "      , SUM(\"effectiveCount\")     AS \"effectiveCount\"";
		sql += "      , SUM(\"totalLTV\")           AS \"totalLTV\"";
		sql += "      , CASE";
		sql += "     		WHEN SUM(\"effectiveCount\") = 0     THEN 0";
		sql += "     		WHEN SUM(\"totalLTV\") = 0           THEN 0";
		sql += "     		ELSE Round(SUM(\"LineAmt\") /(SUM(\"totalLTV\") / SUM(\"effectiveCount\")))";
		sql += "		 END AS \"AllNetWorth\"";
		sql += " FROM \"resultMain\"";
		sql += " GROUP BY \"ClCode1\"";
		sql += "        , \"UsageCode\"";
		sql += " ORDER BY \"ClCode1\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankOnline(TitaVo titaVo) {

		try {
			this.info("1." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";
		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankMonth(TitaVo titaVo) {

		try {
			titaVo.setDataBaseOnMon();
			this.info("2." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";

		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}