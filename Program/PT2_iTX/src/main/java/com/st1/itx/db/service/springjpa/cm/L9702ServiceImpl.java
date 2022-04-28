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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
/* 逾期放款明細 */
public class L9702ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private DateUtil dDateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getType1(int startDate, int endDate, TitaVo titaVo) throws Exception {

		// 2021-08-09 智偉 新增 : 清值
		dDateUtil.init();

		// 取期初日期

		this.info("L9702ServiceImpl getType1 startDate = " + startDate);

		dDateUtil.setDate_1(startDate);

		dDateUtil.setMons(-1);

		int lastMonth = dDateUtil.getCalenderDay() / 100;

		this.info("L9702ServiceImpl getType1 lastMonth = " + lastMonth);

		int endMonth = endDate / 100;

		String sql = "";
		sql += "  SELECT S1.\"EntCode\""; // 企金別
		sql += "        ,S1.\"SumLoanBal\"         AS \"BeginBal\""; // 期初餘額
		sql += "        ,NVL(S3.\"DrawdownAmt\",0) AS \"DrawdownAmt\""; // 撥款金額
		sql += "        ,NVL(S2.\"SumLoanBal\",0)  AS \"EndBal\""; // 期末餘額
		sql += "        ,NVL(S4.\"OvduPrinAmt\",0) AS \"OvduPrinAmt\""; // 轉催收金額
		sql += "        ,NVL(S5.\"LoanBalance\",0) AS \"OvduBal\""; // 期末催收餘額
		sql += "        ,NVL(S6.\"IntRcv\",0)      AS \"IntRcv\""; // 當期利息收入
		// 期初餘額
		sql += "  FROM (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"SumLoanBal\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :lastMonth";
		sql += "                AND \"AcctCode\" <> '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "       ) S1 ";
		// 期末餘額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"SumLoanBal\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :endMonth";
		sql += "                AND \"AcctCode\" <> '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S2 ON S2.\"EntCode\" = S1.\"EntCode\" ";
		// 撥款金額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(LBM.\"DrawdownAmt\")    AS \"DrawdownAmt\""; // 撥款金額加總
		sql += "              FROM \"LoanBorMain\" LBM";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "              WHERE LBM.\"DrawdownDate\" >= :startDate";
		sql += "                AND LBM.\"DrawdownDate\" <= :endDate";
		sql += "                AND LBM.\"RenewFlag\" = '0'"; // 排除展期撥款
		sql += "              GROUP BY CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S3 ON S3.\"EntCode\" = S1.\"EntCode\" ";
		// 轉催收
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(LO.\"OvduPrinAmt\")     AS \"OvduPrinAmt\""; // 撥款金額加總
		sql += "              FROM \"LoanOverdue\" LO";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = LO.\"CustNo\" ";
		sql += "              WHERE LO.\"OvduDate\" >= :startDate";
		sql += "                AND LO.\"OvduDate\" <= :endDate";
		sql += "              GROUP BY CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S4 ON S4.\"EntCode\" = S1.\"EntCode\" ";
		// 期末催收餘額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"LoanBalance\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :endMonth";
		sql += "                AND \"AcctCode\" = '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S5 ON S5.\"EntCode\" = S1.\"EntCode\" ";
		// 當期利息收入
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"Interest\"";
		sql += "                        +\"DelayInt\"";
		sql += "                        +\"BreachAmt\"";
		sql += "                        +\"CloseBreachAmt\")     AS \"IntRcv\""; // 當期利息收入
		sql += "              FROM \"LoanBorTx\" LBT ";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = LBT.\"CustNo\" ";
		sql += "              WHERE LBT.\"AcDate\" >= :startDate";
		sql += "                AND LBT.\"AcDate\" <= :endDate";
		sql += "              GROUP BY CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S6 ON S6.\"EntCode\" = S1.\"EntCode\" ";
		sql += "  ORDER BY \"EntCode\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("endMonth", endMonth);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> getType2(int startDate, int endDate, TitaVo titaVo) throws Exception {

		// 2021-08-09 智偉 新增 : 清值
		dDateUtil.init();

		// 取期初日期

		this.info("L9702ServiceImpl getType1 startDate = " + startDate);

		dDateUtil.setDate_1(startDate);

		dDateUtil.setMons(-1);

		int lastMonth = dDateUtil.getCalenderDay() / 100;

		this.info("L9702ServiceImpl getType1 lastMonth = " + lastMonth);

		int endMonth = endDate / 100;

		String sql = "";
		sql += "  SELECT S1.\"DepartmentCode\"";// 案件隸屬單位
		sql += "        ,S1.\"EntCode\""; // 企金別
		sql += "        ,S1.\"SumLoanBal\"         AS \"BeginBal\""; // 期初餘額
		sql += "        ,NVL(S3.\"DrawdownAmt\",0) AS \"DrawdownAmt\""; // 撥款金額
		sql += "        ,NVL(S2.\"SumLoanBal\",0)  AS \"EndBal\""; // 期末餘額
		sql += "        ,NVL(S4.\"OvduPrinAmt\",0) AS \"OvduPrinAmt\""; // 轉催收金額
		sql += "        ,NVL(S5.\"LoanBalance\",0) AS \"OvduBal\""; // 期末催收餘額
		sql += "        ,NVL(S6.\"IntRcv\",0)      AS \"IntRcv\""; // 當期利息收入
		// 期初餘額
		sql += "  FROM (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"SumLoanBal\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :lastMonth";
		sql += "                AND \"AcctCode\" <> '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "       ) S1 ";
		// 期末餘額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"SumLoanBal\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :endMonth";
		sql += "                AND \"AcctCode\" <> '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S2 ON S2.\"EntCode\" = S1.\"EntCode\" ";
		sql += "                AND S2.\"DepartmentCode\" = S1.\"DepartmentCode\"";
		// 撥款金額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN F.\"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(LBM.\"DrawdownAmt\")    AS \"DrawdownAmt\""; // 撥款金額加總
		sql += "              FROM \"LoanBorMain\" LBM";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = LBM.\"CustNo\" ";
		sql += "                                     AND F.\"FacmNo\" = LBM.\"FacmNo\" ";
		sql += "              WHERE LBM.\"DrawdownDate\" >= :startDate";
		sql += "                AND LBM.\"DrawdownDate\" <= :endDate";
		sql += "                AND LBM.\"RenewFlag\" = '0'"; // 排除展期撥款
		sql += "              GROUP BY CASE WHEN F.\"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S3 ON S3.\"EntCode\" = S1.\"EntCode\" ";
		sql += "                AND S3.\"DepartmentCode\" = S1.\"DepartmentCode\"";
		// 轉催收
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN F.\"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(CASE ";
		sql += "                           WHEN TX.\"TitaTxCd\" = 'L3420' ";
		sql += "                                AND TX.\"TitaHCode\" = '0' ";
		sql += "                                AND NVL(JSON_VALUE(TX.\"OtherFields\", '$.CaseCloseCode'), ' ') = '3' ";
		sql += "                           THEN TX.\"Principal\" ";
		sql += "                           WHEN TX.\"TitaTxCd\" = 'L3100' ";
		sql += "                                AND TX.\"TitaHCode\" = '3' ";
		sql += "                                AND TX.\"EntryDate\" <= :startDate ";
		sql += "                                AND NVL(JSON_VALUE(TX.\"OtherFields\", '$.CaseCloseCode'), ' ') = '3' ";
		sql += "                           THEN 0 - TX.\"Principal\" ";
		sql += "                         ELSE 0 END )            AS \"OvduPrinAmt\""; // 轉催收金額加總
		sql += "              FROM \"LoanBorTx\" TX ";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = TX.\"CustNo\" ";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = TX.\"CustNo\" ";
		sql += "                                     AND F.\"FacmNo\" = TX.\"FacmNo\" ";
		sql += "              WHERE TX.\"AcDate\" >= :startDate";
		sql += "                AND TX.\"AcDate\" <= :endDate";
		sql += "                AND CASE ";
		sql += "                      WHEN TX.\"TitaTxCd\" = 'L3420' ";
		sql += "                           AND TX.\"TitaHCode\" = '0' ";
		sql += "                           AND NVL(JSON_VALUE(TX.\"OtherFields\", '$.CaseCloseCode'), ' ') = '3' ";
		sql += "                      THEN 1 ";
		sql += "                      WHEN TX.\"TitaTxCd\" = 'L3100' ";
		sql += "                           AND TX.\"TitaHCode\" = '3' ";
		sql += "                           AND TX.\"EntryDate\" <= :startDate ";
		sql += "                           AND NVL(JSON_VALUE(TX.\"OtherFields\", '$.CaseCloseCode'), ' ') = '3' ";
		sql += "                      THEN 1 ";
		sql += "                    ELSE 0 END = 1 ";
		sql += "              GROUP BY CASE WHEN F.\"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S4 ON S4.\"EntCode\" = S1.\"EntCode\" ";
		sql += "                AND S4.\"DepartmentCode\" = S1.\"DepartmentCode\"";
		// 期末催收餘額
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN \"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN \"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"LoanBalance\")        AS \"LoanBalance\""; // 放款餘額加總
		sql += "              FROM \"MonthlyLoanBal\"";
		sql += "              WHERE \"YearMonth\" = :endMonth";
		sql += "                AND \"AcctCode\" = '990'";
		sql += "                AND \"LoanBalance\" > 0";
		sql += "              GROUP BY CASE WHEN \"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN \"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S5 ON S5.\"EntCode\" = S1.\"EntCode\" ";
		sql += "                AND S5.\"DepartmentCode\" = S1.\"DepartmentCode\"";
		// 當期利息收入
		sql += "  LEFT JOIN (";
		sql += "              SELECT CASE";
		sql += "                       WHEN F.\"DepartmentCode\" = '1' THEN '1'"; // 案件隸屬單位 (0:非企金單位;1:企金推展課)
		sql += "                     ELSE '0' END                AS \"DepartmentCode\"";
		sql += "                    ,CASE";
		sql += "                       WHEN C.\"EntCode\" = '1' THEN '1'"; // 企金別 (自然人;法人;企金自然人)
		sql += "                     ELSE '0' END                AS \"EntCode\"";
		sql += "                    ,SUM(\"Interest\"";
		sql += "                        +\"DelayInt\"";
		sql += "                        +\"BreachAmt\"";
		sql += "                        +\"CloseBreachAmt\")     AS \"IntRcv\""; // 當期利息收入
		sql += "              FROM \"LoanBorTx\" LBT ";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = LBT.\"CustNo\" ";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = LBT.\"CustNo\" ";
		sql += "                                     AND F.\"FacmNo\" = LBT.\"FacmNo\" ";
		sql += "              WHERE LBT.\"AcDate\" >= :startDate";
		sql += "                AND LBT.\"AcDate\" <= :endDate";
		sql += "                AND LBT.\"TitaHCode\" = '0' ";
		sql += "                AND NVL(JSON_VALUE(\"OtherFields\", '$.CaseCloseCode'), ' ') NOT IN ('3', '4', '5', '6', '7', '8')";
		sql += "              GROUP BY CASE WHEN F.\"DepartmentCode\" = '1' THEN '1' ELSE '0' END";
		sql += "                      ,CASE WHEN C.\"EntCode\" = '1' THEN '1' ELSE '0' END";
		sql += "            ) S6 ON S6.\"EntCode\" = S1.\"EntCode\" ";
		sql += "                AND S6.\"DepartmentCode\" = S1.\"DepartmentCode\"";
		sql += "  ORDER BY \"DepartmentCode\",\"EntCode\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lastMonth", lastMonth);
		query.setParameter("endMonth", endMonth);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("l9702.findAll");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);

		String sql = "SELECT \"F0\"";
		sql += "            ,\"F1\"";
		sql += "            ,\"F2\"";
		sql += "              ,SUM(F3) \"F3\"";
		sql += "      FROM (SELECT \"F0\"";
		sql += "                  ,\"F1\"";
		sql += "                  ,\"F2\"";
		sql += "                  ,CASE WHEN (  \"F2\"   = 1";
		sql += "                            OR  \"F2\"   = 2)";
		sql += "                            AND \"DbCr\" = 'C' THEN - \"TxAmt\"";
		sql += "                        WHEN    \"F2\"   = 4";
		sql += "                            AND \"DbCr\" = 'D' THEN - \"TxAmt\"";
		sql += "                   ELSE \"TxAmt\" END \"F3\"";
		sql += "            FROM (SELECT NVL(F.\"DepartmentCode\",0) F0";
		sql += "                        ,NVL(DECODE(C.\"EntCode\", '0', 0, 1), 0) F1";
		sql += "                        ,CASE WHEN A.\"TitaTxCd\" = 'L3100' THEN 1";
		sql += "                              WHEN A.\"TitaTxCd\" = 'L3440' THEN 2";
		sql += "                              WHEN    JSON_VALUE(A.\"JsonFields\",'$.CaseCloseCode') = '3'";
		sql += "                                  AND A.\"DbCr\"                                     = 'C'  THEN 3";
		sql += "                         ELSE 4 END  F2";
		sql += "                        ,A.\"DbCr\"";
		sql += "                        ,A.\"TxAmt\"";
		sql += "                  FROM \"AcDetail\" A";
		sql += "                  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\"";
		sql += "                  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                         AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                  WHERE A.\"AcDate\" >= :isday";
		sql += "                    AND A.\"AcDate\" <= :ieday";
		sql += "                    AND A.\"AcctCode\" LIKE '3%'";
		sql += "                    AND A.\"EntAc\" = 1))";
		sql += "      GROUP BY \"F0\"";
		sql += "              ,\"F1\"";
		sql += "              ,\"F2\"";
		sql += "      ORDER BY \"F0\"";
		sql += "              ,\"F1\"";
		sql += "              ,\"F2\"";

		/*
		 * SELECT "F1", "F2", "F3", SUM(F6) F6 FROM( SELECT "F1", "F2", "F3" , CASE WHEN
		 * ("F3" = 1 OR "F3" = 2) AND "F4" = 'C' THEN - "F5" WHEN "F3" = 4 AND "F4" =
		 * 'D' THEN - "F5" ELSE "F5" END F6 FROM ( SELECT NVL(F."DepartmentCode",0) F1 ,
		 * NVL(DECODE(C."EntCode", '0', 0, 1), 0) F2 , CASE WHEN A."TitaTxCd" = 'L3100'
		 * THEN 1 WHEN A."TitaTxCd" = 'L3440' THEN 2 WHEN
		 * JSON_VALUE(A."JsonFields",'$.CaseCloseCode') = '3' AND A."DbCr" = 'C' THEN 3
		 * ELSE 4 END F3 , A."DbCr" F4 , A."TxAmt" F5 FROM "AcDetail" A LEFT JOIN
		 * "CustMain" C ON C."CustNo" = A."CustNo" LEFT JOIN "FacMain" F ON F."CustNo" =
		 * A."CustNo" AND F."FacmNo" = A."FacmNo" WHERE A."AcDate" >= 查詢起日 AND
		 * A."AcDate" <= 查詢訖日 AND A."AcctCode" LIKE '3%' AND A."EntAc" = 1)) GROUP BY
		 * "F1", "F2", "F3" ORDER BY "F1", "F2", "F3"
		 * 
		 */

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		this.info(" isday=" + iSDAY + ",ieday=" + iEDAY);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findNow(TitaVo titaVo, String ltbsdy) throws Exception {

		this.info("l9702.findSUM");

		String sql = "SELECT \"F0\"";
		sql += "              ,SUM(F1) \"F1\"";
		sql += "              ,SUM(F2) \"F2\"";
		sql += "      FROM (SELECT F.\"DepartmentCode\" F0";
		sql += "                  ,DECODE(D.\"AcctCode\", '990', D.\"LoanBalance\", 0) F1";
		sql += "                  ,D.\"IntAmtRcv\" F2";
		sql += "            FROM \"DailyLoanBal\" D";
		sql += "            LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "                                   AND F.\"FacmNo\" = D.\"FacmNo\"";
		sql += "            WHERE D.\"DataDate\" = :iday)";
		sql += "      GROUP BY \"F0\"";
		sql += "      ORDER BY \"F0\"";

		/*
		 * SELECT "F1", SUM(F2), SUM(F3) FROM( SELECT F."DepartmentCode" F1 ,
		 * DECODE(D."AcctCode", '990', D."LoanBalance", 0) F2 , D."IntAmtRcv" F3 FROM
		 * "DailyLoanBal" D LEFT JOIN "FacMain" F ON F."CustNo" = D."CustNo" AND
		 * F."FacmNo" = D."FacmNo" WHERE D."DataDate" = 20200201 ) 日期如果是當日(營業日)
		 * 要減一天因為是晚上做批次產生 GROUP BY "F1" ORDER BY "F1"
		 * 
		 */

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", ltbsdy);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> finddbf(TitaVo titaVo) throws Exception {

		String ACCTDATE_ST = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String ACCTDATE_ED = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);

		this.info("l9702.finddbf");

		String sql = "SELECT T.\"CustNo\"";
		sql += "            ,T.\"FacmNo\"";
		sql += "            ,F.\"FirstDrawdownDate\"";
		sql += "            ,F.\"MaturityDate\"";
		sql += "            ,SUM(M.\"DrawdownAmt\") \"DrawdownAmt\"";
		sql += "            ,F.\"ApproveRate\"";
		sql += "            ,SUM(M.\"LoanBal\") \"LoanBal\"";
		sql += "            ,T.\"Interest\"";
		sql += "            ,F.\"DepartmentCode\"";
		sql += "      FROM (SELECT T.\"CustNo\"";
		sql += "                  ,T.\"FacmNo\"";
		sql += "                  ,SUM(T.\"Interest\" + T.\"DelayInt\") \"Interest\"";
		sql += "            FROM \"LoanBorTx\" T";
		sql += "            WHERE T.\"AcDate\" >= :st";
		sql += "              AND T.\"AcDate\" <= :ed";
		sql += "              AND T.\"TitaHCode\" = 0";
		sql += "              AND T.\"Interest\" + T.\"DelayInt\" > 0";
		sql += "            GROUP BY T.\"CustNo\", T.\"FacmNo\") T";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = T.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = T.\"FacmNo\"";
		sql += "      LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = T.\"CustNo\"";
		sql += "                                 AND M.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                                 AND M.\"Status\" IN (0, 2, 7)";
		sql += "      GROUP BY T.\"CustNo\"";
		sql += "              ,T.\"FacmNo\"";
		sql += "              ,F.\"FirstDrawdownDate\"";
		sql += "              ,F.\"MaturityDate\"";
		sql += "              ,F.\"ApproveRate\"";
		sql += "              ,T.\"Interest\"";
		sql += "              ,F.\"DepartmentCode\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("st", ACCTDATE_ST);
		query.setParameter("ed", ACCTDATE_ED);
		return this.convertToMap(query);
	}

}