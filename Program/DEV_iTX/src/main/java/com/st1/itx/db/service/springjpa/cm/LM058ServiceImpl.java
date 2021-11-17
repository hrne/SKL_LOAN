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
/* 逾期放款明細 */
public class LM058ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("1.thisMonthEndDate=" + thisMonthEndDate);

		String[] dayItem = { "日", "一", "二", "三", "四", "五", "六" };
		// 星期 X (排除六日用) 代號 0~6對應 日到六
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		this.info("day = " + dayItem[day - 1]);
		int diff = 0;
		if (day == 1) {
			diff = -2;
		} else if (day == 6) {
			diff = 1;
		}
		this.info("diff=" + diff);
		calendar.add(Calendar.DATE, diff);
		// 矯正月底日
		thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));
		this.info("2.thisMonthEndDate=" + thisMonthEndDate);
		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		// 上個月底
		calendar.set(iYear, iMonth - 1, 0);

		int lastMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		String iLYearMonth = String.valueOf(lastMonthEndDate / 100);

		this.info("lM058.findAll YYMM=" + iYearMonth + ",LYYMM=" + iLYearMonth);

		String sql = "SELECT D.\"CustNo\" AS F0";
		sql += "			,C.\"CustName\" AS F1";
		sql += "			,C.\"CustId\" AS F2";
		sql += "			,CASE";
		sql += "			   WHEN R.\"RelsCode\" = '99' THEN 'F'";
		sql += "			   WHEN R.\"RelsCode\" IS null THEN 'F'";
		sql += "			ELSE '*' END AS F3";
		sql += "			,'2' AS F4";
		sql += "			,'新光人壽' AS F5";
		sql += "			,'2' AS F6";
		sql += "			,D.\"maxLoanBal\" AS F7";
		sql += "			,D.\"LoanBal\" AS F8";
		sql += "			,D.\"LoanBal\" - D.\"maxLoanBal\" AS F9";
		sql += "			,D.\"LoanBal\" AS F10";
		sql += "			,D.\"SEQ\" AS F11";
		sql += "	  FROM(SELECT D.\"CustNo\" \"CustNo\"";
		sql += "				 ,D.\"DataDate\" \"DataDate\"";
		sql += "				 ,D.\"MonthEndYm\" \"MonthEndYm\"";
		sql += "				 ,D.\"maxLoanBal\" \"maxLoanBal\"";
		sql += "				 ,D.\"LoanBal\" \"LoanBal\"";
		sql += "				 ,ROW_NUMBER() OVER(ORDER BY D.\"LoanBal\" DESC) AS SEQ";
		sql += "		   FROM(SELECT D.\"CustNo\"";
		sql += "					  ,D.\"DataDate\"";
		sql += "					  ,D.\"MonthEndYm\"";
		sql += "					  ,DDD.\"LoanBalance\" \"maxLoanBal\"";
		sql += "					  ,SUM(D.\"LoanBalance\") \"LoanBal\"";
		sql += "				FROM \"DailyLoanBal\" D";
		sql += "	  			LEFT JOIN(SELECT \"CustNo\"";
		sql += "							    ,MAX(\"LoanBalance\") \"LoanBalance\"";
		sql += "						  FROM(SELECT \"CustNo\"";
		sql += "									 ,TRUNC(\"DataDate\" / 100)";
		sql += "									 ,SUM(\"LoanBalance\") \"LoanBalance\"";
		sql += "							   FROM \"DailyLoanBal\"";
		sql += "							   WHERE TRUNC(\"DataDate\" / 100) = :yymm";
		sql += "						  	   GROUP BY \"CustNo\",TRUNC(\"DataDate\" / 100) )";
		sql += "						  GROUP BY \"CustNo\") DDD";
		sql += "	 			ON DDD.\"CustNo\" = D.\"CustNo\"";
		sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :yymm";
		sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\",DDD.\"LoanBalance\")D )D";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "	  LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
		sql += " 	  WHERE D.\"SEQ\" <= 20";
		sql += "	  ORDER BY D.\"SEQ\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);
//		query.setParameter("lyymm", iLYYMM);

		return this.convertToMap(query);
	}

//	String sql = "SELECT D.\"CustNo\" AS F0";
//	sql += "			,C.\"CustName\" AS F1";
//	sql += "			,C.\"CustId\" AS F2";
//	sql += "			,CASE";
//	sql += "			   WHEN R.\"RelsCode\" = '99' THEN 'F'";
//	sql += "			   WHEN R.\"RelsCode\" IS null THEN 'F'";
//	sql += "			ELSE '*' END AS F3";
//	sql += "			,'2' AS F4";
//	sql += "			,'新光人壽' AS F5";
//	sql += "			,'2' AS F6";
//	sql += "			,D.\"LoanBal\" AS F7";
//	sql += "			,D.\"MLoanBal\" AS F8";
//	sql += "			,D.\"LoanBal\" - D.\"LLoanBal\" AS F9";
//	sql += "			,D.\"LLoanBal\" AS F10";
//	sql += "			,D.\"SEQ\" AS F11";
//	sql += "	  FROM(SELECT D.\"CustNo\"";
//	sql += "				 ,MAX(D.\"LoanBal\") \"LoanBal\"";
//	sql += "				 ,MAX(D.\"LLoanBal\") \"LLoanBal\"";
//	sql += "				 ,SUM(DECODE(D.\"MonthEndYm\",:yymm, D.\"LoanBal\", 0)) \"MLoanBal\"";
//	sql += "				 ,ROW_NUMBER() OVER(ORDER BY MAX(D.\"LoanBal\") DESC) AS SEQ";
//	sql += "		   FROM(SELECT D.\"CustNo\"";
//	sql += "					  ,D.\"DataDate\"";
//	sql += "					  ,D.\"MonthEndYm\"";
//	sql += "					  ,SUM(D.\"LoanBalance\") \"LoanBal\"";
//	sql += "					  ,0 \"LLoanBal\"";
//	sql += "				FROM \"DailyLoanBal\" D";
//	sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :yymm";
//	sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\"";
//	sql += "				UNION ALL";
//	sql += "				SELECT D.\"CustNo\"";
//	sql += "					  ,D.\"DataDate\"";
//	sql += "					  ,D.\"MonthEndYm\"";
//	sql += "					  ,0 \"LoanBal\"";
//	sql += "					  ,SUM(D.\"LoanBalance\") \"LLoanBal\"";
//	sql += "				FROM \"DailyLoanBal\" D";
//	sql += "				WHERE TRUNC(D.\"DataDate\" / 100) = :lyymm";
//	sql += "				GROUP BY D.\"CustNo\", D.\"DataDate\", D.\"MonthEndYm\") D";
//	sql += "		   GROUP BY D.\"CustNo\") D";
//	sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
//	sql += "	  LEFT JOIN \"RelsMain\" R ON R.\"RelsId\" = C.\"CustId\"";
//	sql += " 	  WHERE D.\"SEQ\" <= 20";
//	sql += "	  ORDER BY D.\"SEQ\"";

}