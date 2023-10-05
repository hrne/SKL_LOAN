package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class LM085ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 
	 * 產製資料
	 * 
	 * @param titaVo
	 * @param yearMonth 當前西元年月
	 * @param unitCode  金額單位
	 * @return
	 * @throws Exception
	 * 
	 */

	public List<Map<String, String>> findPart1(TitaVo titaVo, int yearMonth, String unitCode) throws Exception {
		this.info("LM085ServiceImpl findPart1 ");

		int unit = unitChange(unitCode);

		String sql = " ";
		sql += "	WITH \"tempTotal\" AS (";
		sql += "		SELECT DECODE(M.\"EntCode\",'1','G7','G6') AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','G7','G6')";	
		sql += "		UNION All ";
		sql += "		SELECT 'G6' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";				
		sql += "		UNION All ";
		sql += "		SELECT 'G7' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'H20' AS \"Column\"";
		sql += "			  ,SUM(\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M ";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND \"DepartmentCode\" ='1' ";
		sql += "		UNION All ";
		sql += "		SELECT 'H20' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'E9' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10601301000','10601302000') "; // 催收款項法務費用+催收款項火險費用
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT 'E9' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'G24' AS \"Column\"";// 本月新轉催收金額
		sql += "			  ,SUM(\"UnpaidPrincipal\" + \"UnpaidInterest\" ) AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "   		WHERE \"OvduDate\"  BETWEEN :yymm*100+1 AND :yymm*100+31 ";
		sql += "		  AND \"AcctCode\" = '990' "; // 催收放款
		sql += "		  AND \"YearMonth\" = :yymm ";
		sql += "		UNION All ";
		sql += "		SELECT 'G24' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'F24' AS \"Column\""; // 本月新轉催收筆數
		sql += "			  ,SUM(1) AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "   		WHERE \"OvduDate\"  BETWEEN :yymm*100+1 AND :yymm*100+31 ";
		sql += "		  AND \"AcctCode\" = '990' "; // 催收放款
		sql += "		  AND \"PrinBalance\" > 0  ";
		sql += "		  AND \"YearMonth\" = :yymm ";
		sql += "		UNION All ";
		sql += "		SELECT 'F24' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'G8' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10600304000','10601304000') "; // 放款折溢價+催收款項折溢價
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT 'G8' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'E8' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10601304000') "; // 催收款項折溢價
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT 'E8' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'H26' AS \"Column\"";// 保貸
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10601','10602') ";// 壽險貸款 墊繳保費
		sql += "		  AND \"CurrencyCode\" = 'TOL' ";// 幣別合計
		sql += "		UNION All ";
		sql += "		SELECT 'H26' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'B20' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10623','10624') "; // 備抵損失擔保放款+備抵損失催收款項擔保放款
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT 'B20' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "	),\"tempAmt\" AS (";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'C6'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'C7'";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'E6'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'E7'";
		sql += "			   ELSE 'N1' END AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'C6'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'C7'";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'E6'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'E7'";
		sql += "			     ELSE 'N1' END ";
		sql += "		UNION All ";
		sql += "		SELECT 'C6' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'C7' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'E6' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'E7' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'N1' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "	),\"tempCnt\" AS (";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'B6'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'B7'";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D6'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D7'";
		sql += "			   ELSE 'N2' END AS \"Column\"";
		sql += "			  ,SUM(1) AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'B6'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'B7'";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D6'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D7'";
		sql += "			     ELSE 'N2' END ";
		sql += "		UNION All ";
		sql += "		SELECT 'B6' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'B7' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'D6' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'D7' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT 'N2' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "	)";
		// 表示單位正常顯示
		if (unit == 0) {
			sql += "	SELECT \"Column\", SUM(\"Value\") AS \"Value\" FROM \"tempTotal\" GROUP BY \"Column\"";
			sql += "	UNION";
			sql += "	SELECT \"Column\", SUM(\"Value\") AS \"Value\" FROM \"tempAmt\" GROUP BY \"Column\"";
			sql += "	UNION";
			sql += "	SELECT \"Column\", SUM(\"Value\") AS \"Value\" FROM \"tempCnt\" GROUP BY \"Column\"";
		} else {
			sql += "	SELECT \"Column\", ROUND(\"Value\" / " + unit + ",0) AS \"Value\" ";
			sql += "	FROM (SELECT \"Column\", SUM(\"Value\") AS \"Value\" FROM \"tempTotal\" GROUP BY \"Column\")";
			sql += "	UNION";
			sql += "	SELECT \"Column\", ROUND(\"Value\" / " + unit + ",0) AS \"Value\" FROM \"tempAmt\"";
			sql += "	FROM (SELECT \"Column\", SUM(\"Value\") AS \"Value\" FROM \"tempAmt\" GROUP BY \"Column\")";
			sql += "	UNION";
			sql += "	SELECT \"Column\", \"Value\" FROM \"tempCnt\"";
		}

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 
	 * 產製資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, String>> findPart2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM085ServiceImpl findPart2 ");

		String sql = " ";
		sql += "	WITH \"tempTotal\" AS ( ";
		sql += "		SELECT DECODE(M.\"EntCode\",'1','31','30') AS \"Column\" "; // 1企金
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','31','30')";
		sql += "		UNION All ";
		sql += "		SELECT '31' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT '30' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT DECODE(M.\"EntCode\",'1','35','34') AS \"Column\" "; // 1企金
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		  AND M.\"AcctCode\" = '990' ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','35','34')";
		sql += "		UNION All ";
		sql += "		SELECT '35' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "		UNION All ";
		sql += "		SELECT '34' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT '32' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10600304000','10601301000','10601302000') "; // 放款折溢價+催收款項法務費用+催收款項火險費用
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT '32' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT '36' AS \"Column\"";
		sql += "			  ,SUM(\"TdBal\") AS \"Value\"";
		sql += "		FROM \"CoreAcMain\"";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "		  AND \"AcNoCode\" IN ('10601304000','10601301000','10601302000') "; // 催收款項折溢價+催收款項法務費用+催收款項火險費用
		sql += "		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "		UNION All ";
		sql += "		SELECT '36' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT '37' AS \"Column\""; // 逾期3期(不含催收)
		sql += "			  ,SUM(\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\"";
		sql += "   		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AcctCode\" <> '990' ";
		sql += "		  AND \"OvduTerm\" >= 3 ";
		sql += "		UNION All ";
		sql += "		SELECT '37' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT '39' AS \"Column\" "; // 逾放總額
		sql += "			  ,SUM(\"Amt\") AS \"Value\" ";
		sql += "		FROM  ";
		sql += "			 (SELECT SUM(\"PrinBalance\") AS \"Amt\"  "; // 逾期3期(不含催收)
		sql += "			FROM \"MonthlyFacBal\" ";
		sql += "   			WHERE \"YearMonth\" = :yymm ";
		sql += "			  AND \"AcctCode\" <> '990' ";
		sql += "			  AND \"OvduTerm\" >= 3 ";
		sql += "			UNION All ";
		sql += "			 SELECT SUM(\"TdBal\") AS \"Amt\"  ";
		sql += "			FROM \"CoreAcMain\" ";
		sql += "   			WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   			  AND \"CurrencyCode\" = 'NTD' ";
		sql += "   			  AND \"AcNoCode\" IN ('10604')) "; // 催收及催收費用含折溢價
		sql += "		UNION All ";
		sql += "		SELECT '39' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";

		sql += "		UNION All ";
		sql += "		SELECT '40' AS \"Column\" "; // 放款總額
		sql += "			  ,SUM( \"TdBal\") AS \"Value\" ";
		sql += "		FROM \"CoreAcMain\" ";
		sql += "   		WHERE \"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   		  AND \"CurrencyCode\" = 'NTD' ";
		sql += "   		  AND \"AcNoCode\" IN ('10603','10604') "; // 放款含折溢價 催收及催收費用含折溢價
		sql += "		UNION All ";
		sql += "		SELECT '40' AS \"Column\"";
		sql += "			   ,0   AS \"Value\"";
		sql += "		FROM dual ";
		sql += "   		  ) ";
	// 單位元
		sql += "	SELECT ";
		sql += "	\"Column\"	";
		sql += "	,SUM(\"Value\") AS \"Value\"	";
		sql += "	FROM \"tempTotal\" ";
		sql += "	GROUP BY \"Column\" ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 
	 * 產製資料
	 * 
	 * @param titaVo
	 * @param data     西元年月資料群
	 * @param unitCode 金額單位
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, String>> findPart4(TitaVo titaVo, List<String> data, String unitCode) throws Exception {
		this.info("LM085ServiceImpl findPart4 ");

		int unit = unitChange(unitCode);

		String yearMonthGroup = data.get(0);
		String yearGroupBadDebt = data.get(1);
		String yearMonthGroupBadDebt = data.get(2);

		this.info("yearMonthGroup=" + yearMonthGroup);
		this.info("yearGroupBadDebt=" + yearGroupBadDebt);
		this.info("yearMonthGroupBadDebt=" + yearMonthGroupBadDebt);

		// 第一筆當等於12月時 上一年就要變成整年度， 用年度的話 最少4個 最多6個 無論怎樣年度產出六個

		String sql = " ";

		sql += "	WITH OVDATA AS (";
		sql += "		SELECT \"YearMonth\" 		     "; // 逾期(不含催收)
		sql += "				,SUM(\"PrinBalance\") AS \"OvAmt\" ";
		sql += "		FROM \"MonthlyFacBal\" ";
		sql += "		WHERE \"YearMonth\" IN (" + yearMonthGroup + ")";
		sql += "		  AND \"AcctCode\" <> '990' ";
		sql += "		  AND \"OvduTerm\" >= 3  ";
		sql += "		GROUP BY \"YearMonth\"   ";
		sql += "	), COREDATA AS ( ";
		sql += "		SELECT O.\"YearMonth\" 			     ";
		sql += "			  ,SUM(CASE WHEN C.\"AcNoCode\" IN ('10604') THEN  C.\"TdBal\" 	ELSE 0 END )	AS 	\"OvduBal\"	 ";
		sql += "			  ,SUM(CASE WHEN C.\"AcNoCode\" IN ('10603','10604') THEN  C.\"TdBal\" 	ELSE 0 END )	AS 	\"LoanBal\"	 ";
		sql += "		FROM OVDATA O ";
		sql += "   		LEFT JOIN \"CoreAcMain\" C ON C.\"AcDate\" = TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR( O.\"YearMonth\"*100+1), 'YYYYMMDD')),'YYYYMMDD')) ";
		sql += "   								  AND C.\"CurrencyCode\" = 'NTD' ";
		sql += "   								  AND \"AcNoCode\" IN ('10603','10604') "; // 催收及催收費用含折溢價
		sql += "		WHERE C.\"AcDate\" IS NOT NULL ";
		sql += "		GROUP BY O.\"YearMonth\"  ";
		sql += "	), BADDATA AS ( "; // 當年度轉呆金額
		sql += "		SELECT TRUNC(\"AcDate\" / 10000 ) AS \"Year\" ";
		sql += "			 	,SUM(CASE WHEN \"TitaHCode\" IN ('0','2','4')  THEN \"TxAmt\" ELSE 0 - \"TxAmt\" END  ) AS \"BadAmt\"";
		sql += "		FROM 	\"LoanBorTx\"   ";
		sql += "		WHERE \"TxDescCode\" IN ('3427','3428') ";
		sql += "		GROUP BY TRUNC(\"AcDate\" / 10000 )  ";
		sql += "	)	 ";
		sql += "	SELECT 	 ";
		sql += "	 O.\"YearMonth\" 	 											AS \"YearMonth\"	 ";
		sql += "	,SUM(NVL(C.\"LoanBal\",0)   )						AS \"LoanBal\" 	 ";
		sql += "	,SUM(NVL(O.\"OvAmt\",0) + NVL(C.\"OvduBal\",0)   )	AS \"OvduBal\" 	 ";
		sql += "	,SUM(NVL(B.\"BadAmt\",0) 	  )				 		AS \"BadAmt\" 	 ";
		sql += "	FROM OVDATA	 O ";
		sql += "	LEFT JOIN  COREDATA	 C ON C.\"YearMonth\" = O.\"YearMonth\" ";
		sql += "	LEFT JOIN  BADDATA	 B ON B.\"Year\" = TRUNC(O.\"YearMonth\"/100 ) ";
		sql += "	ORDER BY O.\"YearMonth\" ASC";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	/**
	 * 判斷單位
	 * 
	 * @param unitCode 0:元, 1:千元, 2:百萬元, 3:億元
	 */
	private Integer unitChange(String unitCode) {
		int unit = 0;
		switch (unitCode) {
		case "1":
			unit = 1000;
			break;
		case "2":
			unit = 1000000;
			break;
		case "3":
			unit = 10000000;
			break;
		default:
			unit = 0;
			break;
		}
		this.info("unit=" + unit);
		return unit;
	}

	/**
	 * 取得月底日
	 * 
	 * @param yearMonth 西元年月(YYYYMM)
	 * @param num       單位(0=本月底日,1=下月底日,-1=上月底日)
	 */
	private Integer ymd(int yearMonth, int num) {

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		int iYear = yearMonth / 100;
		int iMonth = yearMonth % 100;

		int number = num - 1;
		// 設月底日
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth + number);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		return Integer.valueOf(dateFormat.format(calendar.getTime()));
	}

}