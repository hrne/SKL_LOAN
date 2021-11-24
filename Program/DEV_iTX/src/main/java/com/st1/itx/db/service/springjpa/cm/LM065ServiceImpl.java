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

@Service("lM065ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LM065ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
		int wDay = calendar.get(Calendar.DAY_OF_WEEK);
		this.info("day = " + dayItem[wDay - 1]);
		int diff = 0;
		if (wDay == 1) {
			diff = -2;
		} else if (wDay == 6) {
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

		this.info("lM065.findAll iYearMonth=" + iYearMonth);

		String sql = "SELECT DISTINCT(S1.\"CustNo\") AS F0";
		sql += "			,S1.\"FacmNo\" AS F1";
		sql += "			,S1.\"CustName\" AS F2";
		sql += "			,S1.\"ReChkUnit\" AS F3";
		sql += "			,S1.\"CenterShortName\" AS F4";
		sql += "			,S1.\"ReChkYearMonth\" AS F5";
		sql += "			,S1.\"DrawdownDate\" AS F6";
		sql += "			,S1.\"LoanBal\" AS F7";
		sql += "			,R2.\"tLoanBal\" AS F8";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F9";
		sql += "			,DECODE(S1.\"ReCheckCode\",'2','*',' ') AS F10";
		sql += "			,S1.\"Evaluation\" AS F11";
		sql += "	  FROM(SELECT DISTINCT(R.\"CustNo\")";
		sql += "	  	   FROM \"InnReCheck\" R";
		sql += "	  	   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  	   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "							 	  AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  	   LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
		sql += "	  	   LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = R.\"CustNo\"";
		sql += "							 	  	    AND LBM.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  	   WHERE R.\"YearMonth\" = :yyyymm";
		sql += "			 AND R.\"ConditionCode\" = 4";
		sql += "			 AND R.\"LoanBal\" > 0 ";
		sql += "			 AND CAST(SUBSTR(TRUNC(LBM.\"DrawdownDate\"/100),0,6) AS INT) =(";
		sql += "		   CASE";
		sql += "		  	 WHEN CAST(SUBSTR( :yyyymm ,5,2) AS INT) < 5";
		sql += "		  	 THEN SUBSTR(TRUNC( :yyyymm /100),0,4) || SUBSTR( :yyyymm ,5,2) ";
		sql += "			 ELSE SUBSTR(TRUNC( :yyyymm /100) - 1 ,0,4) || SUBSTR( :yyyymm + 7 ,5,2 ) END)) S0 ";
		sql += "	  LEFT JOIN(SELECT R.\"CustNo\" \"CustNo\"";
		sql += "					  ,R.\"FacmNo\" \"FacmNo\"";
		sql += "				  	  ,C.\"CustName\" \"CustName\"";
		sql += "				  	  ,E.\"CenterShortName\" \"CenterShortName\"";
		sql += "					  ,R.\"ReChkUnit\" \"ReChkUnit\"";
		sql += "					  ,R.\"ReChkYearMonth\" \"ReChkYearMonth\"";
		sql += "					  ,R.\"DrawdownDate\" \"DrawdownDate\"";
		sql += "					  ,R.\"LoanBal\" \"LoanBal\"";
		sql += "					  ,DECODE(R.\"ReCheckCode\",'2','*',' ') \"ReCheckCode\"";
		sql += "					  ,R.\"Evaluation\" \"Evaluation\"";
		sql += "	  		    FROM \"InnReCheck\" R";
		sql += "	  	  	  	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  		  	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "							 	       AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  		  	LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
		sql += "	  	   		LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = R.\"CustNo\"";
		sql += "							 	  	    	 AND LBM.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  		  	WHERE R.\"YearMonth\" = :yyyymm";
		sql += "				  AND R.\"ConditionCode\" = 4";
		sql += "				  AND R.\"LoanBal\" > 0 ) S1";
		sql += "	  ON S0.\"CustNo\"=S1.\"CustNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,\"NewFacmNo\"";
		sql += "					   ,MAX(\"RenewCode\") AS \"RenewCode\"";
		sql += "				 FROM \"AcLoanRenew\"";
		sql += "				 GROUP BY \"CustNo\",\"NewFacmNo\") RC";
		sql += "	    ON RC.\"CustNo\" = S1.\"CustNo\" ";
		sql += "	   AND RC.\"NewFacmNo\" = S1.\"FacmNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,SUM(\"LoanBal\") AS \"tLoanBal\"";
		sql += "				 FROM \"InnReCheck\"";
		sql += "	  			 WHERE \"YearMonth\" = :yyyymm";
		sql += "				   AND \"ConditionCode\" = 4";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = S1.\"CustNo\" ";
		sql += "	  ORDER BY S1.\"CustNo\", S1.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYearMonth);
		return this.convertToMap(query);
	}

}