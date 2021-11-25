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
import com.st1.itx.eum.ContentName;

@Service
@Repository
/* 逾期放款明細 */
public class L5801ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	// 補貼息申貸名冊工作檔

	public List<Map<String, String>> findAll(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl.findAll ");

		this.info("thisMonth =" + thisMonth);
		this.info("lastMonth =" + lastMonth);
		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";

		sql += " SELECT N.\"CustNo\"                                                     "; // -- F0 戶號
		sql += "      , N.\"FacmNo\"                                                     "; // -- F1 額度
		sql += "      , N.\"ProdNo\"                                                     "; // -- F2 商品代碼
		sql += "      , N.\"ProjectKind\"                                                "; // -- F3 專案融資種類
		sql += "      , CU.\"CustName\"                                                  "; // -- F4 借款人戶名
		sql += "      , CU.\"CustId\"                                                    "; // -- F5 借款人身份證字號
		sql += "      , CU2.\"CustName\"  AS \"CustName1\"                               "; // -- F6 配偶人戶名
		sql += "      , CU2.\"CustId\"    AS \"CustId1\"                                 "; // -- F7 配偶身份證字號
		sql += "      , N.\"LoanTermYy\"                                                 "; // -- F8 貸款期限
		sql += "      , N.\"ApproveRate\"                                                "; // -- F9 貸放利率
		sql += "      , N.\"DrawdownAmt\"                                                "; // -- F10 優惠貸款金額
		sql += "      , N.\"FirstDrawdownDate\"                                          "; // -- F11 撥款日期
		sql += "      , CI.\"CityItem\"                                                  "; // -- F12 屋址區域
		sql += "      , N.\"Remark\"                                                     "; // -- F13 註記
		sql += " FROM ( ";
		sql += " SELECT T.\"CustNo\"                           AS \"CustNo\" ";
		sql += "      , T.\"FacmNo\"                           AS \"FacmNo\" ";
		sql += "      , MAX(T.\"ProdNo\")                      AS \"ProdNo\" ";
		sql += "      , MAX(CASE T.\"ProdNo\"";
		sql += "      WHEN 'IA' THEN 1";
		sql += "      WHEN 'IB' THEN 2";
		sql += "      WHEN 'IC' THEN 3";
		sql += "      WHEN 'ID' THEN 4";
		sql += "      WHEN 'IE' THEN 4";
		sql += "      WHEN 'IF' THEN 5";
		sql += "      WHEN 'IG' THEN 5";
		sql += "      WHEN 'IF' THEN 6";
		sql += "      ELSE           6";
		sql += "      END)                                     AS \"ProjectKind\"";
		sql += "      , MAX(FA.\"LoanTermYy\")                 AS \"LoanTermYy\"";
		sql += "      , MAX(FA.\"ApproveRate\")                AS \"ApproveRate\"";
		sql += "      , SUM(LN.\"DrawdownAmt\")                AS \"DrawdownAmt\"";
		sql += "      , MAX(FA.\"FirstDrawdownDate\")          AS \"FirstDrawdownDate\"";
		sql += "      , MAX(T.\"CityCode\")                    AS \"CityCode\"";
		sql += "      , MAX(CASE WHEN B.\"AcctCode\" = '990'";
		sql += "                      THEN '催收轉正戶'";
		sql += "                 ELSE      '新貸戶'";
		sql += "            END)                             AS \"Remark\"";

		sql += " FROM \"MonthlyLoanBal\" T";
		sql += "   LEFT JOIN \"MonthlyLoanBal\" B";
		sql += "          ON B.\"YearMonth\" = :lastMonth ";
		sql += "         AND B.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND B.\"FacmNo\" = T.\"FacmNo\"";
		sql += "         AND B.\"BormNo\" = T.\"BormNo\"";
		sql += "   LEFT JOIN \"LoanBorMain\" LN";
		sql += "          ON LN.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND LN.\"FacmNo\" = T.\"FacmNo\"";
		sql += "         AND LN.\"BormNo\" = T.\"BormNo\"";
		sql += "   LEFT JOIN \"FacMain\" FA";
		sql += "          ON FA.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND FA.\"FacmNo\" = T.\"FacmNo\"";
		sql += "   WHERE T.\"YearMonth\" = :thisMonth ";

		sql += "     AND T.\"ProdNo\" >= 'IA'";
		sql += "     AND T.\"ProdNo\" <= 'II'";
		sql += "     AND (   (TRUNC(FA.\"FirstDrawdownDate\" / 100) = :thisMonth) ";
		sql += "          OR (T.\"AcctCode\" <> '990' AND B.\"AcctCode\" = '990'))";
		sql += "   GROUP BY T.\"CustNo\", T.\"FacmNo\"";
		sql += "   ) N";

		sql += "   LEFT JOIN \"CustMain\" CU";
		sql += "          ON CU.\"CustNo\" = N.\"CustNo\"";
		sql += "   LEFT JOIN \"ReltMain\" RM";
		sql += "          ON RM.\"ReltUKey\" = CU.\"CustUKey\"";
		sql += "          AND RM.\"ReltCode\" = '2'";
		sql += "   LEFT JOIN \"CustMain\" CU2";
		sql += "          ON CU2.\"CustNo\" = RM.\"CustNo\"";
		sql += "   LEFT JOIN \"CdCity\" CI";
		sql += "          ON CI.\"CityCode\" =  N.\"CityCode\"";
		sql += "   ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("thisMonth", thisMonth);

		return this.convertToMap(query);
	}

	// 補貼息結清名冊/終止名冊工作檔

	public List<Map<String, String>> findAll2(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl2.findAll ");

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " SELECT CASE WHEN N.\"AcctCode\" = '990'";
		sql += "                  THEN '結清名冊'";
		sql += "             WHEN N.\"LoanBalance\" = 0";
		sql += "                  THEN '結清名冊'";
		sql += "             ELSE      '終止名冊'";
		sql += "        END   AS \"F0\"                                                  "; // -- F0 種類
		sql += "      , N.\"CustNo\"                                                     "; // -- F1 戶號
		sql += "      , N.\"FacmNo\"                                                     "; // -- F2 額度
		sql += "      , N.\"ProdNo\"                                                     "; // -- F3 商品代碼
		sql += "      , N.\"ProjectKind\"                                                "; // -- F4 專案融資種類
		sql += "      , CU.\"CustName\"                                                  "; // -- F5 借款人戶名
		sql += "      , CU.\"CustId\"                                                    "; // -- F6 借款人身份證字號
		sql += "      , N.\"DrawdownAmt\"                                                "; // -- F7 原核撥優惠貸款金額"
		sql += "      , N.\"CloseDate\"                                                  "; // -- F8 銷戶/終止補貼日期

		sql += "      , CASE WHEN N.\"AcctCode\" = '990'";
		sql += "                  THEN '轉催收'";
		sql += "             WHEN N.\"LoanBalance\" = 0";
		sql += "                  THEN '正常'";
		sql += "             ELSE      ''";
		sql += "        END     AS \"F9\"                                                         "; // -- F9 註記

		sql += "      FROM (";
		sql += "      SELECT T.\"CustNo\"                           AS \"CustNo\"";
		sql += "           , T.\"FacmNo\"                           AS \"FacmNo\"";
		sql += "           , MAX(T.\"ProdNo\")                      AS \"ProdNo\"";
		sql += "           , MAX(CASE T.\"ProdNo\"";

		sql += "                      WHEN 'IA' THEN 1";
		sql += "                      WHEN 'IB' THEN 2";
		sql += "                      WHEN 'IC' THEN 3";
		sql += "                      WHEN 'ID' THEN 4";
		sql += "                      WHEN 'IE' THEN 4";
		sql += "                      WHEN 'IF' THEN 5";
		sql += "                      WHEN 'IG' THEN 5";
		sql += "                      WHEN 'IF' THEN 6";
		sql += "                      ELSE           6";
		sql += "            END)                               AS \"ProjectKind\"";
		sql += "      , SUM(LN.\"DrawdownAmt\")                AS \"DrawdownAmt\"";

		sql += "      , SUM(T.\"LoanBalance\")                 AS \"LoanBalance\"";
		sql += "      , MAX(T.\"AcctCode\")                    AS \"AcctCode\"   ";
		sql += "      , MAX(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) <= :thisMonth";
		sql += "                      THEN LN.\"MaturityDate\"";
		sql += "                 ELSE LN.\"AcDate\"";
		sql += "            END)                               AS \"CloseDate\"  ";

		sql += "      , SUM(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) <= :thisMonth ";
		sql += "                      THEN 0";
		sql += "                 WHEN T.\"AcctCode\" = '990'";
		sql += "                      THEN 0";
		sql += "                 ELSE T.\"LoanBalance\"";
		sql += "            END)                             AS \"ThisMonthBal\" ";

		sql += "   FROM \"MonthlyLoanBal\" T";
		sql += "   LEFT JOIN \"MonthlyLoanBal\" B";
		sql += "          ON B.\"YearMonth\" = :lastMonth ";
		sql += "         AND B.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND B.\"FacmNo\" = T.\"FacmNo\"";
		sql += "         AND B.\"BormNo\" = T.\"BormNo\"";
		sql += "   LEFT JOIN \"LoanBorMain\" LN";
		sql += "          ON LN.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND LN.\"FacmNo\" = T.\"FacmNo\"";
		sql += "         AND LN.\"BormNo\" = T.\"BormNo\"";

		sql += "   WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "         AND T.\"ProdNo\" >= 'IA'";
		sql += "         AND T.\"ProdNo\" <= 'II'";
		sql += "         AND NOT (T.\"AcctCode\" = '990' AND B.\"AcctCode\" = '990')";
		sql += "         AND TRUNC(LN.\"MaturityDate\" / 100) >= :thisMonth ";
		sql += "   GROUP BY T.\"CustNo\", T.\"FacmNo\"";
		sql += "   ) N";
		sql += "   LEFT JOIN \"CustMain\" CU";
		sql += "          ON CU.\"CustNo\" = N.\"CustNo\"";
		sql += "   WHERE N.\"ThisMonthBal\" = 0";
		sql += "   ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}

	// 補貼息核撥清單工作檔

	public List<Map<String, String>> findAll3(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl3.findAll ");

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " SELECT N.\"ProjectKind\"                                                   "; // -- F0 專案融資種類
		sql += "      , ROUND(N.\"ThisMonthBal\" * N.\"SubsidyRate\" / 1200, 0)  AS \"F1\"        "; // -- F1 補貼息"
		sql += "      , N.\"LastMonthBal\"                                                  "; // -- F2 A.上月貸款餘額
		sql += "      , N.\"OpenAmount\"                                                    "; // -- F3 B.本月貸出數
		sql += "      , N.\"CloseAmount\"                                                   "; // -- F4 C1.本月收回數" --還款+結清+轉催收
		sql += "      , N.\"MaturityAmount\"                                                "; // -- F5 C2.屆期不再申撥補貼息" --超過到期日
		sql += "      , N.\"ThisMonthBal\"                                                  "; // -- F6 D.本月貸款餘額

		sql += "      FROM (SELECT MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 1";
		sql += "                            WHEN 'IB' THEN 2";
		sql += "                            WHEN 'IC' THEN 3";
		sql += "                            WHEN 'ID' THEN 4";
		sql += "                            WHEN 'IE' THEN 4";
		sql += "                            WHEN 'IF' THEN 5";
		sql += "                            WHEN 'IG' THEN 5";
		sql += "                            WHEN 'IF' THEN 6";
		sql += "                            ELSE           6";
		sql += "                       END)                           AS \"ProjectKind\"";

		sql += "                 , MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 0.85";
		sql += "                            WHEN 'IB' THEN 0.85";
		sql += "                            WHEN 'IC' THEN 0.425";
		sql += "                            WHEN 'ID' THEN 0.25";
		sql += "                            WHEN 'IE' THEN 0.25";
		sql += "                            WHEN 'IF' THEN 0.125";
		sql += "                            WHEN 'IG' THEN 0.125";
		sql += "                            WHEN 'IF' THEN 0.7";
		sql += "                            ELSE           0.7";
		sql += "                       END)                           AS \"SubsidyRate\"";

		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE NVL(B.\"LoanBalance\",0)";
		sql += "                       END)                           AS \"LastMonthBal\"";

		sql += "                 , SUM(CASE WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN 0";
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 新貸
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 催收轉正
		sql += "                            ELSE 0";
		sql += "                       END)                            AS \"OpenAmount\"";

		sql += "                 , SUM(CASE WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN NVL(B.\"LoanBalance\",0)        "; // -- 轉催收
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            WHEN TRUNC(LN.\"MaturityDate\" / 100) = :thisMonth  ";
		sql += "                                 THEN 0";
		sql += "                            WHEN NVL(B.\"LoanBalance\",0) > 0         "; // -- 還款+結清
		sql += "                                 THEN B.\"LoanBalance\" - T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"CloseAmount\"   "; // --還款+結清+轉催收

		sql += "                 , SUM(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) = :thisMonth ";
		sql += "                             AND T.\"AcctCode\" <> '990'";
		sql += "                             AND T.\"LoanBalance\" > 0";
		sql += "                                 THEN T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"MaturityAmount\" "; // --超過到期日

		sql += "                 , SUM(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) <= :thisMonth ";
		sql += "                                 THEN 0";
		sql += "                            WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE T.\"LoanBalance\"";
		sql += "                       END)                             AS \"ThisMonthBal\"";

		sql += "             FROM \"MonthlyLoanBal\" T";
		sql += "             LEFT JOIN \"MonthlyLoanBal\" B";
		sql += "                    ON B.\"YearMonth\" = :lastMonth ";
		sql += "                   AND B.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND B.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                   AND B.\"BormNo\" = T.\"BormNo\"";
		sql += "             LEFT JOIN \"LoanBorMain\" LN";
		sql += "                    ON LN.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND LN.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                   AND LN.\"BormNo\" = T.\"BormNo\"";
		sql += "             LEFT JOIN \"FacMain\" FA";
		sql += "                    ON FA.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND FA.\"FacmNo\" = T.\"FacmNo\"";

		sql += "             WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "               AND T.\"ProdNo\" >= 'IA'";
		sql += "               AND T.\"ProdNo\" <= 'II'";
		sql += "               AND NOT (T.\"AcctCode\" = '990' AND B.\"AcctCode\" = '990')";
		sql += "               AND TRUNC(LN.\"MaturityDate\" / 100) >= :thisMonth ";

		sql += "             GROUP BY  CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 1";
		sql += "                            WHEN 'IB' THEN 2";
		sql += "                            WHEN 'IC' THEN 3";
		sql += "                            WHEN 'ID' THEN 4";
		sql += "                            WHEN 'IE' THEN 4";
		sql += "                            WHEN 'IF' THEN 5";
		sql += "                            WHEN 'IG' THEN 5";
		sql += "                            WHEN 'IF' THEN 6";
		sql += "                            ELSE           6";
		sql += "                        END";
		sql += "             ) N";
		sql += "  ORDER BY  N.\"ProjectKind\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}

	// 補貼息核撥清單明細檔

	public List<Map<String, String>> findAll4(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl4.findAll ");

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " SELECT N.\"CustNo\"                                                     "; // -- F0 戶號
		sql += "      , N.\"FacmNo\"                                                     "; // -- F1 額度
		sql += "      , N.\"ProdNo\"                                                     "; // -- F2 商品代碼
		sql += "      , N.\"ProjectKind\"                                                "; // -- F3 專案融資種類
		sql += "      , N.\"SubsidyRate\"                                                "; // -- F4 補貼利率
		sql += "      , ROUND(N.\"ThisMonthBal\" * N.\"SubsidyRate\" / 1200, 0)   AS \"F5\" "; // -- F5 補貼息
		sql += "      , N.\"AcBookCode\" + '/' + N.\"AcSubBookCode\"              AS \"F6\" "; // -- F6 帳冊別
		sql += "      , N.\"LastMonthBal\"                                               "; // -- F7 A.上月貸款餘額
		sql += "      , N.\"OpenAmount\"                                                 "; // -- F8 B.本月貸出數
		sql += "      , N.\"CloseAmount\"                                                "; // -- F9 C1.本月收回數 --還款+結清+轉催收
		sql += "      , N.\"MaturityAmount\"                                             "; // -- F10 C2.屆期不再申撥補貼息 --超過到期日
		sql += "      , N.\"ThisMonthBal\"                                               "; // -- F11 D.本月貸款餘額

		sql += "      FROM (";
		sql += "            SELECT T.\"CustNo\"                           AS \"CustNo\"";
		sql += "                 , T.\"FacmNo\"                           AS \"FacmNo\"";
		sql += "                 , MAX(T.\"ProdNo\")                      AS \"ProdNo\"";
		sql += "                 , MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 1";
		sql += "                            WHEN 'IB' THEN 2";
		sql += "                            WHEN 'IC' THEN 3";
		sql += "                            WHEN 'ID' THEN 4";
		sql += "                            WHEN 'IE' THEN 4";
		sql += "                            WHEN 'IF' THEN 5";
		sql += "                            WHEN 'IG' THEN 5";
		sql += "                            WHEN 'IF' THEN 6";
		sql += "                            ELSE           6";
		sql += "                       END)                           AS \"ProjectKind\"";

		sql += "                 , MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 0.85";
		sql += "                            WHEN 'IB' THEN 0.85";
		sql += "                            WHEN 'IC' THEN 0.425";
		sql += "                            WHEN 'ID' THEN 0.25";
		sql += "                            WHEN 'IE' THEN 0.25";
		sql += "                            WHEN 'IF' THEN 0.125";
		sql += "                            WHEN 'IG' THEN 0.125";
		sql += "                            WHEN 'IF' THEN 0.7";
		sql += "                            ELSE           0.7";
		sql += "                       END)                           AS \"SubsidyRate\"";

		sql += "                 , MAX(T.\"AcBookCode\")              AS \"AcBookCode\"";
		sql += "                 , MAX(T.\"AcSubBookCode\")           AS \"AcSubBookCode\"";

		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE NVL(B.\"LoanBalance\",0)";
		sql += "                       END)                             AS \"LastMonthBal\"";

		sql += "                 , SUM(CASE WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN 0";
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 新貸
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 催收轉正
		sql += "                            ELSE 0";
		sql += "                       END)                            AS \"OpenAmount\"";

		sql += "                 , SUM(CASE WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN NVL(B.\"LoanBalance\",0)        "; // -- 轉催收
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            WHEN TRUNC(LN.\"MaturityDate\" / 100) = :thisMonth  ";
		sql += "                                 THEN 0";
		sql += "                            WHEN NVL(B.\"LoanBalance\",0) > 0         "; // -- 還款+結清
		sql += "                                 THEN B.\"LoanBalance\" - T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"CloseAmount\"   "; // --還款+結清+轉催收

		sql += "                 , SUM(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) = :thisMonth ";
		sql += "                             AND T.\"AcctCode\" <> '990'";
		sql += "                             AND T.\"LoanBalance\" > 0";
		sql += "                                 THEN T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"MaturityAmount\" "; // --超過到期日

		sql += "                 , SUM(CASE WHEN TRUNC(LN.\"MaturityDate\" / 100) <= :thisMonth ";
		sql += "                                 THEN 0";
		sql += "                            WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE T.\"LoanBalance\"";
		sql += "                       END)                             AS \"ThisMonthBal\"";

		sql += "             FROM \"MonthlyLoanBal\" T";
		sql += "             LEFT JOIN \"MonthlyLoanBal\" B";
		sql += "                    ON B.\"YearMonth\" = :lastMonth ";
		sql += "                   AND B.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND B.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                   AND B.\"BormNo\" = T.\"BormNo\"";
		sql += "             LEFT JOIN \"LoanBorMain\" LN";
		sql += "                    ON LN.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND LN.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                   AND LN.\"BormNo\" = T.\"BormNo\"";
		sql += "             LEFT JOIN \"FacMain\" FA";
		sql += "                    ON FA.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND FA.\"FacmNo\" = T.\"FacmNo\"";

		sql += "             WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "               AND T.\"ProdNo\" >= 'IA'";
		sql += "               AND T.\"ProdNo\" <= 'II'";
		sql += "               AND NOT (T.\"AcctCode\" = '990' AND B.\"AcctCode\" = '990')";
		sql += "               AND TRUNC(LN.\"MaturityDate\" / 100) >= :thisMonth ";

		sql += "             GROUP BY T.\"CustNo\", T.\"FacmNo\"";
		sql += "             ) N";
		sql += " ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}
}