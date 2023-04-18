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
		boolean onLineMode = false;

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

		sql += " WITH RATEDATA AS (";
		sql += "		SELECT \"EffectDate\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate1')  AS \"Rate1\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate2')  AS \"Rate2\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate3')  AS \"Rate3\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate4')  AS \"Rate4\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate5')  AS \"Rate5\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate6')  AS \"Rate6\"";
		sql += "			 , ROW_NUMBER()OVER(ORDER BY \"EffectDate\" DESC) AS \"Seq\"";
		sql += "		FROM \"CdComm\"";
		sql += "		WHERE \"CdType\" = '01'";
		sql += "		  AND \"CdItem\" = '01'";
		sql += "		  AND TRUNC(\"EffectDate\" / 100 ) <= :thisMonth ";
		sql += "		  AND \"Enable\" = 'Y' ";
		sql += " )";
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
		sql += "      , N.\"SellerName\"                                                 "; // -- F13 售屋者戶名
		sql += "      , N.\"SellerId\"                                                   "; // -- F14 售屋者身份證字號及營利事業編號
		sql += "      , N.\"Remark\"                                                     "; // -- F15 註記
		sql += "      , CASE N.\"ProjectKind\" ";
		sql += "             WHEN 1 THEN NVL(RA.\"Rate1\",0) " ;// 0.85";
		sql += "             WHEN 2 THEN NVL(RA.\"Rate2\",0) " ;// 0.85";
		sql += "             WHEN 3 THEN NVL(RA.\"Rate3\",0) " ;//0.425";
		sql += "             WHEN 4 THEN NVL(RA.\"Rate4\",0) " ;//0.25";
		sql += "             WHEN 5 THEN NVL(RA.\"Rate5\",0) " ;//0.125";
		sql += "             ELSE        NVL(RA.\"Rate6\",0) " ;//0.7";
		sql += "        END                           AS \"SubsidyRate\""; // -- F16 補貼利率"
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
		sql += "      ELSE           6";
		sql += "      END)                                     AS \"ProjectKind\"";
		sql += "      , MAX(FA.\"LoanTermYy\")                 AS \"LoanTermYy\"";
		sql += "      , MAX(FA.\"ApproveRate\")                AS \"ApproveRate\"";
		sql += "      , SUM(LN.\"DrawdownAmt\")                AS \"DrawdownAmt\"";
		sql += "      , MAX(FA.\"FirstDrawdownDate\")          AS \"FirstDrawdownDate\"";
		sql += "      , MAX(T.\"CityCode\")                    AS \"CityCode\"";
		sql += "      , MAX(c.\"SellerId\")                    AS \"SellerId\"";
		sql += "      , MAX(c.\"SellerName\")                  AS \"SellerName\"";
		sql += "      , MAX(CASE WHEN B.\"AcctCode\" = '990'";
		sql += "                      THEN '催收轉正戶'";
		sql += "                 ELSE      '新貸戶'";
		sql += "            END)                             AS \"Remark\"";

		sql += " FROM \"MonthlyLoanBal\" T";
		sql += " LEFT JOIN (";
		sql += "                 SELECT";
		sql += "                    cf.\"CustNo\",";
		sql += "                    cf.\"FacmNo\",";
		sql += "                    clb.\"SellerId\",";
		sql += "                    clb.\"SellerName\",";
		sql += "                    ROW_NUMBER() OVER(";
		sql += "                        PARTITION BY cf.\"CustNo\", cf.\"FacmNo\"";
		sql += "                        ORDER BY";
		sql += "                            clb.\"SellerId\", clb.\"SellerName\"";
		sql += "                    ) AS \"Seq\"";
		sql += "                FROM";
		sql += "                    \"ClFac\"        cf";
		sql += "                    LEFT JOIN \"ClBuilding\"   clb ON clb.\"ClCode1\" = cf.\"ClCode1\"";
		sql += "                                                  AND clb.\"ClCode2\" = cf.\"ClCode2\"";
		sql += "                                                  AND clb.\"ClNo\" = cf.\"ClNo\"";
		sql += "                WHERE";
		sql += "                    cf.\"MainFlag\" = 'Y'";
		sql += "                    AND";
		sql += "                    CASE";
		sql += "                            WHEN nvl(clb.\"SellerId\", ' ') != ' '   THEN";
		sql += "                                1";
		sql += "                            WHEN nvl(clb.\"SellerName\", ' ') != ' ' THEN";
		sql += "                                1";
		sql += "        else 0";
		sql += "                        END";
		sql += "                    = 1";
		sql += "            ) c ON c.\"CustNo\" = t.\"CustNo\"";
		sql += "                   AND c.\"FacmNo\" = t.\"FacmNo\"";
		sql += "                   AND C.\"Seq\" = 1";
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
		sql += "          ON RM.\"CustNo\" = CU.\"CustNo\"";
		sql += "          AND RM.\"ReltCode\" = 2";
		sql += "   LEFT JOIN \"CustMain\" CU2";
		sql += "          ON CU2.\"CustUKey\" = RM.\"ReltUKey\"";
		sql += "   LEFT JOIN \"CdCity\" CI";
		sql += "          ON CI.\"CityCode\" =  N.\"CityCode\"";
		sql += "   LEFT JOIN RATEDATA RA";
		sql += "          ON RA.\"Seq\" = 1";

		sql += "   ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 L5801.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("thisMonth", thisMonth);

		return this.convertToMap(query);
	}

	// 補貼息結清名冊/終止名冊工作檔

	public List<Map<String, String>> findAll2(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl2.findAll ");
		boolean onLineMode = false;

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " WITH RATEDATA AS (";
		sql += "		SELECT \"EffectDate\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate1')  AS \"Rate1\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate2')  AS \"Rate2\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate3')  AS \"Rate3\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate4')  AS \"Rate4\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate5')  AS \"Rate5\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate6')  AS \"Rate6\"";
		sql += "			 , ROW_NUMBER()OVER(ORDER BY \"EffectDate\" DESC) AS \"Seq\"";
		sql += "		FROM \"CdComm\"";
		sql += "		WHERE \"CdType\" = '01'";
		sql += "		  AND \"CdItem\" = '01'";
		sql += "		  AND TRUNC(\"EffectDate\" / 100 ) <= :thisMonth ";
		sql += "		  AND \"Enable\" = 'Y' ";
		sql += " )";
		sql += " SELECT CASE WHEN N.\"Type\" = 2";
		sql += "                  THEN '終止名冊'";
		sql += "             ELSE      '結清名冊'";
		sql += "        END   AS \"F0\"                                                  "; // -- F0 種類
		sql += "      , N.\"CustNo\"                                                     "; // -- F1 戶號
		sql += "      , N.\"FacmNo\"                                                     "; // -- F2 額度
		sql += "      , N.\"ProdNo\"                                                     "; // -- F3 商品代碼
		sql += "      , N.\"ProjectKind\"                                                "; // -- F4 專案融資種類
		sql += "      , CU.\"CustName\"                                                  "; // -- F5 借款人戶名
		sql += "      , CU.\"CustId\"                                                    "; // -- F6 借款人身份證字號
		sql += "      , N.\"DrawdownAmt\"                                                "; // -- F7 原核撥優惠貸款金額
		sql += "      , N.\"CloseDate\"                                                  "; // -- F8 銷戶/終止補貼日期

		sql += "      , CASE WHEN N.\"Type\" = 1";
		sql += "                  THEN '正常'";
		sql += "             WHEN N.\"Type\" = 2";
		sql += "                  THEN ''";
		sql += "             ELSE      '轉催收' ";
		sql += "        END     AS \"F9\"                                                         "; // -- F9 註記
		sql += "      , N.\"LastMonthBal\"                                                "; // -- F10 上月貸款餘額"

		sql += "      , CASE N.\"ProjectKind\" ";
		sql += "             WHEN 1 THEN NVL(RA.\"Rate1\",0) " ;// 0.85";
		sql += "             WHEN 2 THEN NVL(RA.\"Rate2\",0) " ;// 0.85";
		sql += "             WHEN 3 THEN NVL(RA.\"Rate3\",0) " ;//0.425";
		sql += "             WHEN 4 THEN NVL(RA.\"Rate4\",0) " ;//0.25";
		sql += "             WHEN 5 THEN NVL(RA.\"Rate5\",0) " ;//0.125";
		sql += "             ELSE        NVL(RA.\"Rate6\",0) " ;//0.7";
		sql += "        END                           AS \"SubsidyRate\""; // -- F11 補貼利率"
		
		
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
		sql += "                      ELSE           6";
		sql += "            END)                               AS \"ProjectKind\" ";
		sql += "      , SUM(CASE WHEN LN.\"RenewFlag\" = '0' THEN LN.\"DrawdownAmt\" ";
		sql += "                 ELSE 0  END)                  AS \"DrawdownAmt\"";
		sql += "      , SUM(T.\"LoanBalance\")                 AS \"LoanBalance\" ";
		sql += "      , MAX(T.\"AcctCode\")                    AS \"AcctCode\"    ";
		sql += "      , MAX(CASE WHEN T.\"LoanBalance\" = 0   THEN LN.\"AcDate\"  ";
		sql += "                 WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12";
		sql += "		                + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "		              )  = 240                THEN LN.\"MaturityDate\"";// 屆滿20年
		sql += "                 ELSE LN.\"AcDate\"  ";
		sql += "            END)                               AS \"CloseDate\"   ";// (淑微:終止名冊時使用到期日)

		sql += "      , SUM(B.\"LoanBalance\")                 AS \"LastMonthBal\" ";
		sql += "      , MAX(CASE WHEN T.\"LoanBalance\" = 0   THEN 1 ";// 結清
		sql += "                 WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12";
		sql += "		                + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "		              )  = 240                THEN 2 ";// 屆滿20年
		sql += "                 ELSE 3";// 轉催
		sql += "            END)                               AS \"Type\" ";
		
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
		sql += "   LEFT JOIN \"FacMain\" FA";
		sql += "          ON FA.\"CustNo\" = T.\"CustNo\"";
		sql += "         AND FA.\"FacmNo\" = T.\"FacmNo\"";
		
		sql += "   WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "         AND T.\"ProdNo\" >= 'IA'";
		sql += "         AND T.\"ProdNo\" <= 'II'";

		sql += "         AND ( ( T.\"LoanBalance\" = 0)     ";// 本月結清
		sql += "               OR ( ( T.\"LoanBalance\" > 0)     ";
		sql += "                    AND ( (T.\"AcctCode\" = '990' AND B.\"AcctCode\" NOT IN '990')";// 本月轉催
		sql += "                          OR ( ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 ";
		sql += "                                 + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "                               )  = 240 ) ) ))";// 本月屆滿20年

		sql += "         AND ( ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12"; 
		sql +=	"		         + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) "; 
		sql +=	"		       )  <= 240 )" ;//排除已屆滿20年
		
//		sql += "         AND B.\"LoanBalance\" > 0";// 排除上個月餘額已是0
		sql += "         AND LN.\"Status\" NOT IN 1 ";// 排除展期
		
		sql += "   GROUP BY T.\"CustNo\", T.\"FacmNo\"";
		sql += "   ) N";
		sql += "   LEFT JOIN \"CustMain\" CU";
		sql += "          ON CU.\"CustNo\" = N.\"CustNo\"";
		sql += "   LEFT JOIN RATEDATA RA";
		sql += "          ON RA.\"Seq\" = 1";
		sql += "   WHERE N.\"LastMonthBal\"  > 0" ;// 排除上個月餘額已是0
		sql += "   ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 L5801.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}

	// 補貼息核撥清單工作檔

	public List<Map<String, String>> findAll3(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl3.findAll ");
		boolean onLineMode = false;

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";

		sql += " WITH TMPDATA AS ("; // 同戶號額度的筆數只計1筆
		sql += "      SELECT T.\"CustNo\" , T.\"FacmNo\" , T.\"BormNo\" , T.\"ProdNo\" ";
		sql += "           , ROW_NUMBER ()  ";
		sql += "             OVER ( PARTITION BY  T.\"CustNo\" , T.\"FacmNo\" ";
		sql += "                    ORDER BY T.\"CustNo\" , T.\"FacmNo\" ";
		sql += "                  ) AS \"DetailSeq\" ";
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
		sql += "               AND NOT (NVL(T.\"AcctCode\",'*') = '990' AND NVL(B.\"AcctCode\",'*') = '990')";
		sql += "               AND NOT (NVL(T.\"LoanBalance\",0) = 0 AND NVL(B.\"LoanBalance\",0) = 0 ) ";
		sql += "               AND (  ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 ";
		sql += "                          + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "                      )  <= 240 )    ";// 排除本月超過屆滿20年
		sql += " )";

		sql += " ,RATEDATA AS (";
		sql += "		SELECT \"EffectDate\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate1')  AS \"Rate1\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate2')  AS \"Rate2\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate3')  AS \"Rate3\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate4')  AS \"Rate4\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate5')  AS \"Rate5\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate6')  AS \"Rate6\"";
		sql += "			 , ROW_NUMBER()OVER(ORDER BY \"EffectDate\" DESC) AS \"Seq\"";
		sql += "		FROM \"CdComm\"";
		sql += "		WHERE \"CdType\" = '01'";
		sql += "		  AND \"CdItem\" = '01'";
		sql += "		  AND TRUNC(\"EffectDate\" / 100 ) <= :thisMonth ";
		sql += "		  AND \"Enable\" = 'Y' ";
		sql += " )";

		sql += " SELECT N.\"ProjectKind\"                                                         "; // -- F0 專案融資種類
		sql += "      , N.\"LastMonthBalCount\"                                                   "; // -- F1 A.上月貸款餘額(次數)
		sql += "      , N.\"LastMonthBal\"                                                        "; // -- F2 A.上月貸款餘額
		sql += "      , N.\"OpenAmountCount\"                                                     "; // -- F3 B.本月貸出數(次數)
		sql += "      , N.\"OpenAmount\"                                                          "; // -- F4 B.本月貸出數
		sql += "      , N.\"CloseAmountCount\"                                                    "; // -- F5 C1.本月收回數" --還款+結清+轉催收(次數)
		sql += "      , N.\"CloseAmount\"                                                         "; // -- F6 C1.本月收回數" --還款+結清+轉催收
		sql += "      , N.\"MaturityAmountCount\"                                                 "; // -- F7 C2.屆期不再申撥補貼息" --超過到期日(次數)
		sql += "      , N.\"MaturityAmount\"                                                      "; // -- F8 C2.屆期不再申撥補貼息" --超過到期日
		sql += "      , N.\"ThisMonthBalCount\"                                                   "; // -- F9 D.本月貸款餘額(次數)
		sql += "      , N.\"ThisMonthBal\"                                                        "; // -- F10 D.本月貸款餘額
		sql += "      , ROUND(N.\"ThisMonthBal\" * N.\"SubsidyRate\" / 1200, 0)  AS \"F11\"       "; // -- F11 補貼息"
		sql += "      , N.\"SubsidyRate\"                                                         "; // -- F12 補貼利率

		sql += "      FROM (SELECT MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 1";
		sql += "                            WHEN 'IB' THEN 2";
		sql += "                            WHEN 'IC' THEN 3";
		sql += "                            WHEN 'ID' THEN 4";
		sql += "                            WHEN 'IE' THEN 4";
		sql += "                            WHEN 'IF' THEN 5";
		sql += "                            WHEN 'IG' THEN 5";
		sql += "                            ELSE           6";
		sql += "                       END)                           AS \"ProjectKind\"";

		sql += "                 , MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN NVL(RA.\"Rate1\",0) "  ;// 0.85";
		sql += "                            WHEN 'IB' THEN NVL(RA.\"Rate2\",0) " ;// 0.85";
		sql += "                            WHEN 'IC' THEN NVL(RA.\"Rate3\",0) " ;//0.425";
		sql += "                            WHEN 'ID' THEN NVL(RA.\"Rate4\",0) " ;//0.25";
		sql += "                            WHEN 'IE' THEN NVL(RA.\"Rate4\",0) " ;//0.25";
		sql += "                            WHEN 'IF' THEN NVL(RA.\"Rate5\",0) " ;//0.125";
		sql += "                            WHEN 'IG' THEN NVL(RA.\"Rate5\",0) " ;//0.125";
		sql += "                            ELSE           NVL(RA.\"Rate6\",0) " ;//0.7";
		sql += "                       END)                           AS \"SubsidyRate\"";

		sql += "                 , SUM(CASE WHEN NVL(TP.\"DetailSeq\",0) > 1 THEN 0";
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') != '990' AND NVL(B.\"LoanBalance\",0) != 0";
		sql += "                                 THEN 1";
		sql += "                            ELSE  0";
		sql += "                       END)                           AS \"LastMonthBalCount\"";
		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE NVL(B.\"LoanBalance\",0)";
		sql += "                       END)                           AS \"LastMonthBal\"";

		sql += "                 , SUM(CASE WHEN NVL(TP.\"DetailSeq\",0) > 1 THEN 0";
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" = '0'   ";
		sql += "                                 THEN 1";// -- 新貸
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990' THEN 1";// -- 催收轉正
		sql += "                            ELSE 0 ";
		sql += "                       END)                            AS \"OpenAmountCount\"";

		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" = '0' ";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 新貸
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 催收轉正
		sql += "                            ELSE 0";
		sql += "                       END)                            AS \"OpenAmount\"";

		sql += "                 , SUM(CASE WHEN NVL(TP.\"DetailSeq\",0) > 1 THEN 0";
		sql += "                            WHEN LN.\"Status\" = 1   THEN 0 ";//展期不計
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" IN ('1','2') THEN 0 ";//展期不計
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' THEN  0 ";
		sql += "                            WHEN NVL(T.\"LoanBalance\",0) = 0         ";//結清
		sql += "                                 THEN 1";
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                 )  = 240    THEN 0";
		sql += "                            WHEN NVL(T.\"AcctCode\",'*') = '990'";// -- 轉催收
		sql += "                                 THEN 1";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"CloseAmountCount\"   "; // --結清+轉催收(不含還款)

		sql += "                 , SUM(CASE WHEN LN.\"Status\" = 1   THEN  NVL(B.\"LoanBalance\",0) ";//展期
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" IN ('1','2')";
		sql += "                                 THEN 0 - NVL(T.\"LoanBalance\",0)        "; // -- 展期+借新還舊:展期新舊需計算差額,但不計筆數 
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' THEN  0 ";
		sql += "                            WHEN NVL(T.\"LoanBalance\",0) = 0  THEN NVL(B.\"LoanBalance\",0) "; // -- 結清
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                 )  = 240    THEN  NVL(B.\"LoanBalance\",0) - NVL(T.\"LoanBalance\",0) ";//--屆期需計算差額
		sql += "                            WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN NVL(B.\"LoanBalance\",0)        "; // -- 轉催收
		sql += "                            WHEN NVL(B.\"LoanBalance\",0) > 0         "; // -- 還款+結清
		sql += "                                 THEN B.\"LoanBalance\" - T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"CloseAmount\"   "; // --還款+結清+轉催收

		sql += "                 , SUM(CASE WHEN NVL(TP.\"DetailSeq\",0) > 1 THEN 0";
		sql += "                            WHEN T.\"LoanBalance\" = 0   THEN 0 " ; 
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                 )  = 240    THEN 1";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"MaturityAmountCount\" "; // --借款期限至本月為止屆滿20年

		sql += "                 , SUM(CASE WHEN T.\"LoanBalance\" = 0   THEN 0 " ; 
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                  )  = 240   THEN T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"MaturityAmount\" "; // --借款期限至本月為止屆滿20年

		sql += "                 , SUM(CASE WHEN NVL(TP.\"DetailSeq\",0) > 1 THEN 0";
		sql += "                            WHEN LN.\"Status\" = 1   THEN 1 ";//展期
		sql += "                            WHEN T.\"AcctCode\" = '990'  THEN 0";
		sql += "                            WHEN T.\"LoanBalance\" = 0   THEN 0";
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                  )  = 240              THEN 0";
		sql += "                            ELSE 1";
		sql += "                       END)                             AS \"ThisMonthBalCount\"";

		sql += "                 , SUM(CASE WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                  )  = 240              THEN 0";
		sql += "                            WHEN T.\"AcctCode\" = '990'  THEN 0";
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
		sql += "             LEFT JOIN TMPDATA TP";
		sql += "                    ON TP.\"CustNo\" = T.\"CustNo\"";
		sql += "                   AND TP.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                   AND TP.\"BormNo\" = T.\"BormNo\"";
		sql += "             LEFT JOIN RATEDATA RA";
		sql += "                    ON RA.\"Seq\" = 1";
		
		sql += "             WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "               AND T.\"ProdNo\" >= 'IA'";
		sql += "               AND T.\"ProdNo\" <= 'II'";
		sql += "               AND NOT (NVL(T.\"AcctCode\",'*') = '990' AND NVL(B.\"AcctCode\",'*') = '990')";
		sql += "               AND NOT (NVL(T.\"LoanBalance\",0) = 0 AND NVL(B.\"LoanBalance\",0) = 0 ) ";
		sql += "               AND (  ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 ";
		sql += "                          + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "                      )  <= 240 )    ";// 排除本月超過屆滿20年

		sql += "             GROUP BY  CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN 1";
		sql += "                            WHEN 'IB' THEN 2";
		sql += "                            WHEN 'IC' THEN 3";
		sql += "                            WHEN 'ID' THEN 4";
		sql += "                            WHEN 'IE' THEN 4";
		sql += "                            WHEN 'IF' THEN 5";
		sql += "                            WHEN 'IG' THEN 5";
		sql += "                            ELSE           6";
		sql += "                        END";
		sql += "             ) N";
		sql += "  ORDER BY  N.\"ProjectKind\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 L5801.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}

	// 補貼息核撥清單明細檔

	public List<Map<String, String>> findAll4(int thisMonth, int lastMonth, TitaVo titaVo) throws Exception {
		this.info("L5801ServiceImpl4.findAll ");
		boolean onLineMode = false;

		if (thisMonth == 0) {
			this.error("L5801ServiceImpl.findAll thisMonth = 0");
			return null;
		}

		if (lastMonth == 0) {
			this.error("L5801ServiceImpl.findAll lastMonth = 0");
			return null;
		}

		String sql = " ";
		sql += " WITH RATEDATA AS (";
		sql += "		SELECT \"EffectDate\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate1')  AS \"Rate1\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate2')  AS \"Rate2\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate3')  AS \"Rate3\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate4')  AS \"Rate4\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate5')  AS \"Rate5\"";
		sql += "			 , Json_value(\"JsonFields\",'$.SubsidyRate6')  AS \"Rate6\"";
		sql += "			 , ROW_NUMBER()OVER(ORDER BY \"EffectDate\" DESC) AS \"Seq\"";
		sql += "		FROM \"CdComm\"";
		sql += "		WHERE \"CdType\" = '01'";
		sql += "		  AND \"CdItem\" = '01'";
		sql += "		  AND TRUNC(\"EffectDate\" / 100 ) <= :thisMonth ";
		sql += "		  AND \"Enable\" = 'Y' ";
		sql += " )";
		sql += " SELECT N.\"CustNo\"                                                     "; // -- F0 戶號
		sql += "      , N.\"FacmNo\"                                                     "; // -- F1 額度
		sql += "      , N.\"ProdNo\"                                                     "; // -- F2 商品代碼
		sql += "      , N.\"ProjectKind\"                                                "; // -- F3 專案融資種類
		sql += "      , N.\"SubsidyRate\"                                                "; // -- F4 補貼利率
		sql += "      , CASE WHEN NVL(N.\"AcBookCode\", '')  = N.\"AcBookCode\" AND NVL(N.\"AcSubBookCode\", '')  = N.\"AcSubBookCode\" "
				+ "THEN N.\"AcBookCode\" || '/' || N.\"AcSubBookCode\" ELSE '' END AS \"F5\" "    ;   // -- F5 帳冊別                           
		sql += "      , N.\"LastMonthBal\"                                               "; // -- F6 A.上月貸款餘額
		sql += "      , N.\"OpenAmount\"                                                 "; // -- F7 B.本月貸出數
		sql += "      , N.\"CloseAmount\"                                                "; // -- F8 C1.本月收回數 --還款+結清+轉催收
		sql += "      , N.\"MaturityAmount\"                                             "; // -- F9 C2.屆期不再申撥補貼息 --借款期限至本月為止屆滿20年
		sql += "      , N.\"ThisMonthBal\"                                               "; // -- F10 D.本月貸款餘額
		sql += "      , ROUND(N.\"ThisMonthBal\" * N.\"SubsidyRate\" / 1200, 0)   AS \"F11\" "; // -- F11 補貼息

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
		sql += "                            ELSE           6";
		sql += "                       END)                           AS \"ProjectKind\"";

		sql += "                 , MAX(CASE T.\"ProdNo\"";
		sql += "                            WHEN 'IA' THEN NVL(RA.\"Rate1\",0) "  ;// 0.85";
		sql += "                            WHEN 'IB' THEN NVL(RA.\"Rate2\",0) "  ;// 0.85";
		sql += "                            WHEN 'IC' THEN NVL(RA.\"Rate3\",0) "  ;// 0.425";
		sql += "                            WHEN 'ID' THEN NVL(RA.\"Rate4\",0) "  ;// 0.25";
		sql += "                            WHEN 'IE' THEN NVL(RA.\"Rate4\",0) "  ;// 0.25";
		sql += "                            WHEN 'IF' THEN NVL(RA.\"Rate5\",0) "  ;// 0.125";
		sql += "                            WHEN 'IG' THEN NVL(RA.\"Rate5\",0) "  ;// 0.125";
		sql += "                            ELSE           NVL(RA.\"Rate6\",0) "  ;// 0.7";
		sql += "                       END)                           AS \"SubsidyRate\"";

		sql += "                 , MAX(T.\"AcBookCode\")              AS \"AcBookCode\"";
		sql += "                 , MAX(T.\"AcSubBookCode\")           AS \"AcSubBookCode\"";

		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN 0";
		sql += "                            ELSE NVL(B.\"LoanBalance\",0)";
		sql += "                       END)                             AS \"LastMonthBal\"";

		sql += "                 , SUM(CASE WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" = '0' ";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 新貸
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '990'";
		sql += "                                 THEN T.\"LoanBalance\"               "; // -- 催收轉正
		sql += "                            ELSE 0";
		sql += "                       END)                            AS \"OpenAmount\"";

		sql += "                 , SUM(CASE WHEN LN.\"Status\" = 1   THEN  NVL(B.\"LoanBalance\",0) ";//展期
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' AND LN.\"RenewFlag\" IN ('1','2')";
		sql += "                                 THEN 0 - NVL(T.\"LoanBalance\",0)        "; // -- 展期+借新還舊:展期新舊需計算差額,但不計筆數 
		sql += "                            WHEN NVL(B.\"AcctCode\",'*') = '*' THEN  0 ";
		sql += "                            WHEN NVL(T.\"LoanBalance\",0) = 0  THEN NVL(B.\"LoanBalance\",0) "; // -- 結清
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                 )  = 240    THEN  NVL(B.\"LoanBalance\",0) - NVL(T.\"LoanBalance\",0) ";//--屆期需計算差額
		sql += "                            WHEN T.\"AcctCode\" = '990'";
		sql += "                                 THEN NVL(B.\"LoanBalance\",0)        "; // -- 轉催收
		sql += "                            WHEN NVL(B.\"LoanBalance\",0) > 0         "; // -- 還款+結清
		sql += "                                 THEN B.\"LoanBalance\" - T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"CloseAmount\"   "; // --還款+結清+轉催收

		sql += "                 , SUM(CASE WHEN T.\"LoanBalance\" = 0   THEN 0 " ; 
		sql += "                            WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ; 
		sql += "		                            + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                  )  = 240   THEN T.\"LoanBalance\"";
		sql += "                            ELSE 0";
		sql += "                       END)                             AS \"MaturityAmount\" "; // --借款期限至本月為止屆滿20年

		sql += "                 , SUM(CASE WHEN ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 " ;  
		sql +=	"	                               + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) " ; 
		sql += "                                 )  = 240   THEN 0";
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
		sql += "             LEFT JOIN RATEDATA RA";
		sql += "                    ON RA.\"Seq\" = 1";

		sql += "             WHERE T.\"YearMonth\" = :thisMonth ";
		sql += "               AND T.\"ProdNo\" >= 'IA'";
		sql += "               AND T.\"ProdNo\" <= 'II'";
		sql += "               AND NOT (NVL(T.\"AcctCode\",'*') = '990' AND NVL(B.\"AcctCode\",'*') = '990')";
		sql += "               AND NOT (NVL(T.\"LoanBalance\",0) = 0 AND NVL(B.\"LoanBalance\",0) = 0 ) ";
		sql += "               AND (  ( (TRUNC(:thisMonth / 100) - TRUNC(FA.\"FirstDrawdownDate\" / 10000) ) * 12 ";
		sql += "                          + MOD(:thisMonth,100) - MOD(TRUNC(FA.\"FirstDrawdownDate\" / 100),100) ";
		sql += "                      )  <= 240 )    ";// 排除本月超過屆滿20年

		sql += "             GROUP BY T.\"CustNo\", T.\"FacmNo\"";
		sql += "             ) N";
		sql += "  WHERE n.\"ThisMonthBal\" != 0 ";
		sql += " ORDER BY  N.\"ProjectKind\", N.\"ProdNo\", N.\"CustNo\", N.\"FacmNo\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 L5801.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("lastMonth", lastMonth);

		return this.convertToMap(query);
	}
}