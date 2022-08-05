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
	 * @param unitCode    金額單位
	 * 
	 */

	public List<Map<String, String>> findPart1(TitaVo titaVo, int yearMonth, String unitCode) throws Exception {
		this.info("LM085ServiceImpl findPart1 ");

		int unit = unitChange(unitCode);

		String sql = " ";
		sql += "	WITH \"roundData\" AS (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LnAmt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempTotal\" AS (";
		sql += "		SELECT DECODE(M.\"EntCode\",'1','G7','G6') AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','G7','G6')";
		sql += "		UNION";
		sql += "		SELECT 'E9' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		UNION";
		sql += "		SELECT 'G8' AS \"Column\"";
		sql += "			  ,NVL(R.\"LnAmt\",0) AS \"Value\"";
		sql += "		FROM \"roundData\" R";
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
		sql += "	)";
		// 表示單位正常顯示
		if (unit == 0) {
			sql += "	SELECT \"Column\", \"Value\" FROM \"tempTotal\"";
			sql += "	UNION";
			sql += "	SELECT \"Column\", \"Value\" FROM \"tempAmt\"";
			sql += "	UNION";
			sql += "	SELECT \"Column\", \"Value\" FROM \"tempCnt\"";
		} else {
			sql += "	SELECT \"Column\", ROUND(\"Value\" / " + unit + ",0) AS \"Value\" FROM \"tempTotal\"";
			sql += "	UNION";
			sql += "	SELECT \"Column\", ROUND(\"Value\" / " + unit + ",0) AS \"Value\" FROM \"tempAmt\"";
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
	 * @param lYearMonth 上個西元年月
	 * @param unitCode    金額單位
	 * 
	 */
	public List<Map<String, String>> findPart2_1(TitaVo titaVo, int lYearMonth) throws Exception {
		this.info("LM085ServiceImpl findPart2_1 ");

		String sql = " ";
		sql += "	SELECT \"LegalLoss\" AS \"LegalLoss\"";
		sql += "	FROM \"MonthlyLM052Loss\"";
		sql += "	WHERE \"YearMonth\" = :lyymm";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("lyymm", lYearMonth);

		return this.convertToMap(query);
	}

	
	public List<Map<String, String>> findPart2_2(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM085ServiceImpl findPart2_2 ");

		String sql = " ";
		sql += "	WITH \"tempPremiumDiscount\" AS (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"Value\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempCollection\" AS (";
		sql += "		SELECT \"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "	),\"tempEntCode\" AS (";
		sql += "		SELECT DECODE(\"EntCode\",1,1,0) AS \"EntCode\"";
		sql += "			  ,SUM(M.\"LoanBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyLoanBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"LoanBalance\" > 0 ";
		sql += "		GROUP BY DECODE(\"EntCode\",1,1,0)";
		sql += "		UNION";
		sql += "		SELECT 0 AS \"EntCode\"";
		sql += "			  ,NVL(\"Value\",0) AS \"Value\"";
		sql += "		FROM \"tempPremiumDiscount\"";
		sql += "		UNION";
		sql += "		SELECT 0 AS \"EntCode\"";
		sql += "			  ,NVL(\"Value\",0) AS \"Value\"";
		sql += "		FROM \"tempCollection\"";
		sql += "	),\"tempDepartmentCode\" AS (";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"DepartmentCode\" = '1' AND M.\"AcctCode\" <> '990' ";
		sql += "				 THEN 3 ";
		sql += "			   ELSE 2 END AS \"DepartmentCode\"";
		sql += "			  ,SUM(M.\"LoanBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyLoanBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"LoanBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"DepartmentCode\" = '1' AND M.\"AcctCode\" <> '990' ";
		sql += "				   THEN 3 ";
		sql += "			     ELSE 2 END ";
		sql += "		UNION";
		sql += "		SELECT 2 AS \"DepartmentCode\"";
		sql += "			  ,NVL(\"Value\",0) AS \"Value\"";
		sql += "		FROM \"tempPremiumDiscount\"";
		sql += "		UNION";
		sql += "		SELECT 2 AS \"DepartmentCode\"";
		sql += "			  ,NVL(\"Value\",0) AS \"Value\"";
		sql += "		FROM \"tempCollection\"";
		sql += "	),\"tempOvdu\" AS (";
		sql += "		SELECT DECODE(\"EntCode\",1,1,0) AS \"OvduCode1\"";
		sql += "			  ,DECODE(\"EntCode\",1,3,2) AS \"OvduCode2\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		  AND M.\"OvduTerm\" >= 3 ";
		sql += "		GROUP BY DECODE(\"EntCode\",1,1,0)";
		sql += "			    ,DECODE(\"EntCode\",1,3,2) ";
		sql += "		UNION";
		sql += "		SELECT 0 AS \"OvduCode1\"";
		sql += "			  ,2 AS \"OvduCode2\"";
		sql += "			  ,NVL(\"Value\",0) AS \"Value\"";
		sql += "		FROM \"tempCollection\"";
		sql += "	)";
		sql += "	SELECT A.\"Code\" AS \"Code\"";
		sql += "		  ,ROUND(NVL(B.\"Value\" , 0)/A.\"Value\",5) AS \"Value\"";
		sql += "	FROM(";
		sql += "		SELECT \"EntCode\" AS \"Code\"";
		sql += "			  ,SUM(\"Value\") AS \"Value\"";
		sql += "		FROM \"tempEntCode\"";
		sql += "		GROUP BY \"EntCode\"";
		sql += "		UNION";
		sql += "		SELECT \"DepartmentCode\" AS \"Code\"";
		sql += "			  ,SUM(\"Value\") AS \"Value\"";
		sql += "		FROM \"tempDepartmentCode\"";
		sql += "		GROUP BY \"DepartmentCode\"";
		sql += "	) A LEFT JOIN (";
		sql += "		SELECT \"OvduCode1\"   AS \"Code1\"";
		sql += "			  ,\"OvduCode2\"  AS \"Code2\"";
		sql += "			  ,SUM(\"Value\") AS \"Value\"";
		sql += "		FROM \"tempOvdu\"";
		sql += "		GROUP BY \"OvduCode1\"";
		sql += "				,\"OvduCode2\"";
		sql += "	) B ON B.\"Code1\" = A.\"Code\"";
		sql += "		OR B.\"Code2\" = A.\"Code\"";

		sql += "	";
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findPart2_3(TitaVo titaVo, int yearMonth) throws Exception {
		this.info("LM085ServiceImpl findPart2_3 ");
		//上個年月
		int lyeatMonth = ymd(yearMonth,-1) / 100;
		//下月1號
		int nDate = (ymd(yearMonth,1)  / 100 ) * 100 + 1;
		this.info("lyeatMonth="+lyeatMonth);
		this.info("nDate="+nDate);
		String sql = " ";
		sql += "	SELECT COUNT(*) AS \"F0\"";
		sql += "	      ,SUM(NVL(\"PrinBalance\",0) + NVL(LT.\"Interest\",0)) AS \"F1\"";
		sql += "	FROM \"MonthlyFacBal\" M"; 
		sql += "	LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = M.\"CustNo\"";
		sql += "							AND FM.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"MonthlyLoanBal\" MB ON MB.\"CustNo\" = FM.\"CustNo\"";
		sql += "								   AND MB.\"FacmNo\" = FM.\"FacmNo\"";
		sql += "								   AND MB.\"BormNo\" = FM.\"LastBormNo\"";
		sql += "								   AND MB.\"YearMonth\" = M.\"YearMonth\"";
		sql += "	LEFT JOIN \"LoanBorTx\" LT ON LT.\"CustNo\" = MB.\"CustNo\"";
		sql += "							  AND LT.\"FacmNo\" = MB.\"FacmNo\"";
		sql += "							  AND LT.\"BormNo\" = MB.\"BormNo\"";
		sql += "							  AND LT.\"TitaTxCd\" = 'L3420'";
		sql += "							  AND LT.\"DueDate\" > TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR( :acdate),'YYYYMMDD'),-1),'YYYYMMDD')) ";
		sql += "							  AND LT.\"DueDate\" <= :acdate ";
		sql += "	WHERE M.\"YearMonth\" = :lyymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		//--須出滿5期但未滿6期的轉催收戶號
		sql += "	  AND TRUNC(MONTHS_BETWEEN(TO_DATE(:acdate , 'YYYYMMDD'),TO_DATE(DECODE(M.\"NextIntDate\" , 0 ,19110101,M.\"NextIntDate\"),'YYYYMMDD'))) >= 5";
		sql += "	  AND TRUNC(MONTHS_BETWEEN(TO_DATE(:acdate , 'YYYYMMDD'),TO_DATE(DECODE(M.\"NextIntDate\" , 0 ,19110101,M.\"NextIntDate\"),'YYYYMMDD'))) <= 6";
		//--因為有月初1號 指定日
		sql += "	  AND TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR(M.\"NextIntDate\"),'YYYYMMDD'),6),'YYYYMMDD')) <= ";
		sql += "		  TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(TO_CHAR( :lyymm * 100 + 1 ),'YYYYMMDD'),2),'YYYYMMDD'))";
		sql += "	  AND LT.\"CustNo\" IS NOT NULL ";
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("lyymm", lyeatMonth);
		query.setParameter("acdate", nDate);

		return this.convertToMap(query);
	}
	
	
	/**
	 * 
	 * 產製資料
	 * 
	 * @param titaVo
	 * @param yearMonth 當前西元年月
	 * @param unitCode    金額單位
	 * 
	 */
	public List<Map<String, String>> findPart3(TitaVo titaVo, int yearMonth, String unitCode) throws Exception {
		this.info("LM085ServiceImpl findPart3 " + yearMonth);

		int unit = unitChange(unitCode);

		String sql = " ";
		sql += "	WITH \"roundData\" AS (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LnAmt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempTotal\" AS (";
		sql += "		SELECT DECODE(M.\"EntCode\",'1','B31','B30') AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','B31','B30')";
		sql += "		UNION";
		sql += "		SELECT 'B32' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		UNION";
		sql += "		SELECT 'B32' AS \"Column\"";
		sql += "			  ,NVL(R.\"LnAmt\",0) AS \"Value\"";
		sql += "		FROM \"roundData\" R";
		sql += "	),\"tempAmt\" AS (";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'B37'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'B37'";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'B34'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'B35'";
		sql += "			   ELSE 'N1' END AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'B37'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'B37'";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'B34'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'B35'";
		sql += "			     ELSE 'N1' END ";
		sql += "		UNION";
		sql += "		SELECT 'B36' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "	)";
		sql += "	SELECT \"Column\"";
		if (unit == 0) {
			sql += "		  ,SUM(\"Value\") AS \"Value\"";
		} else {
			sql += "		  ,ROUND(SUM(\"Value\") /" + unit + ",0) AS \"Value\"";
		}
		sql += "	FROM(";
		sql += "		SELECT * FROM \"tempTotal\"";
		sql += "		UNION";
		sql += "		SELECT * FROM \"tempAmt\"";
		sql += "	)";
		sql += "	GROUP BY \"Column\"";
		sql += "	ORDER BY \"Column\" ASC";

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
	 * @param data   西元年月資料群
	 * @param unitCode 金額單位
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
		sql += "	WITH \"roundData\" AS (";
		sql += "		SELECT I.\"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LnAmt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" IN (" + yearMonthGroup + ")";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "		GROUP BY I.\"YearMonth\"";
		sql += "	),\"tempTotal\" AS (";
		sql += "		SELECT M.\"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,DECODE(M.\"EntCode\",'1','B','B') AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" IN (" + yearMonthGroup + ")";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY M.\"YearMonth\"";
		sql += "			    ,DECODE(M.\"EntCode\",'1','B','B')";
		sql += "		UNION";
		sql += "		SELECT \"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,'B' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\"IN (" + yearMonthGroup + ")";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		UNION";
		sql += "		SELECT R.\"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,'B' AS \"Column\"";
		sql += "			  ,NVL(R.\"LnAmt\",0) AS \"Value\"";
		sql += "		FROM \"roundData\" R";
		sql += "	),\"tempAmt\" AS (";
		sql += "		SELECT M.\"YearMonth\" AS \"YearMonth\"";
		sql += "			  ,CASE";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'D'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'D'";
		sql += "				 WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D'";
		sql += "				 WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D'";
		sql += "			   ELSE 'N1' END AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" IN (" + yearMonthGroup + ")";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY M.\"YearMonth\"";
		sql += "			    ,CASE";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'D'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'D'";
		sql += "				   WHEN M.\"EntCode\" = '0' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D'";
		sql += "				   WHEN M.\"EntCode\" = '1' AND M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D'";
		sql += "			     ELSE 'N1' END ";
		sql += "	)";
		sql += "	SELECT A.\"YearMonth\" AS \"YearMonth\"";
		if (unit == 0) {
			sql += "		  ,NVL(A.\"Column\",'B') AS \"Col1\"";
			sql += "		  ,NVL(A.\"Value\" ,0)  AS \"Val1\"";
			sql += "		  ,NVL(B.\"Column\",'D')  AS \"Col2\"";
			sql += "		  ,NVL(B.\"Value\",0 )  AS \"Val2\"";
			sql += "		  ,NVL(C.\"Column\",'G') AS \"Col3\"";
			sql += "		  ,NVL(C.\"Value\",0)  AS \"Val3\"";
		} else {

			sql += "		  ,NVL(A.\"Column\",'B') AS \"Col1\"";
			sql += "		  ,ROUND(NVL(A.\"Value\",0) /" + unit + ",0)  AS \"Val1\"";
			sql += "		  ,NVL(B.\"Column\",'D')  AS \"Col2\"";
			sql += "		  ,ROUND(NVL(B.\"Value\",0) /" + unit + ",0)  AS \"Val2\"";
			sql += "		  ,NVL(C.\"Column\",'G') AS \"Col3\"";
			sql += "		  ,ROUND(NVL(C.\"Value\",0) /" + unit + ",0)  AS \"Val3\"";
		}
		sql += "	FROM (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,\"Column\"";
		sql += "			  ,SUM(\"Value\") AS \"Value\"";
		sql += "		FROM \"tempTotal\"";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "				,\"Column\"";
		sql += "	) A ";
		sql += "	LEFT JOIN (";
		sql += "		SELECT \"YearMonth\"";
		sql += "			  ,\"Column\"";
		sql += "			  ,SUM(\"Value\") AS \"Value\"";
		sql += "		FROM \"tempAmt\"";
		sql += "		WHERE \"Column\" NOT IN ('N1')";
		sql += "		GROUP BY \"YearMonth\"";
		sql += "				,\"Column\"";
		sql += "	) B ON B.\"YearMonth\" = A.\"YearMonth\"";
		sql += "	LEFT JOIN (";
		sql += "		SELECT TO_NUMBER (TRUNC(\"BadDebtDate\" / 10000 ) || '12') AS \"YearMonth\"";
		sql += "			  ,'G' AS \"Column\"";
		sql += "			  ,SUM(\"BadDebtAmt\") AS \"Value\"";
		sql += "		FROM \"LoanOverdue\"";
		sql += "		WHERE TRUNC(\"BadDebtDate\" / 10000 ) IN (" + yearGroupBadDebt + ")";
		sql += "		GROUP BY TO_NUMBER (TRUNC(\"BadDebtDate\" / 10000 ) || '12') ";
		sql += "			    ,'G' ";
		sql += "		UNION";
		sql += "		SELECT TRUNC(\"BadDebtDate\" / 100 ) AS \"YearMonth\"";
		sql += "			  ,'G' AS \"Column\"";
		sql += "			  ,SUM(\"BadDebtAmt\") AS \"Value\"";
		sql += "		FROM \"LoanOverdue\"";
		sql += "		WHERE TRUNC(\"BadDebtDate\" / 100 ) IN (" + yearMonthGroupBadDebt + ")";
		sql += "		GROUP BY TRUNC(\"BadDebtDate\" / 100 ) ";
		sql += "			    ,'G' ";
		sql += "	) C ON C.\"YearMonth\" = A.\"YearMonth\"";
		sql += "	ORDER BY A.\"YearMonth\" ASC";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
	/**
	 * 判斷單位
	 * @param unitCode 
	 * 		  0:元,
	 * 		  1:千元,
	 * 		  2:百萬元,
	 * 		  3:億元
	 * */
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
		this.info("unit="+unit);
		return unit;
	}
	
	/**
	 * 取得月底日
	 * @param yearMonth 西元年月(YYYYMM)
	 * @param num 單位(0=本月底日,1=下月底日,-1=上月底日)
	 * */
	private Integer ymd(int yearMonth,int num) {
		
		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		int iYear = yearMonth/100;
		int iMonth = yearMonth%100;
		
		int number = num - 1;
		// 設月底日
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth + number);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		
		return Integer.valueOf(dateFormat.format(calendar.getTime()));
	}

}