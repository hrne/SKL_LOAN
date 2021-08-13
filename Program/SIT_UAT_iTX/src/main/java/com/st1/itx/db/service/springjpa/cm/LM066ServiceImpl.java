package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LM066ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM066ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		} 

		logger.info("lM066.findAll iYear=" + iYear + ",iMonth=" + iMonth);

		String sql = "SELECT R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,C.\"CustName\" AS F2";
		sql += "			,R.\"ReChkUnit\" AS F3";
		sql += "			,MOD(R.\"ReChkYearMonth\", 100) AS F4";
		sql += "			,R.\"DrawdownDate\" AS F5";
		sql += "			,R.\"LoanBal\" AS F6";
		sql += "			,R2.\"tLoanBal\" AS F7";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F8";
		sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS F9";
		sql += "			,DECODE(R.\"ReChkYearMonth\",0,' ','V') AS F10";
		sql += "			,R.\"ReChkYearMonth\" AS F11";
		sql += "			,R.\"Evaluation\" AS F12";
		sql += "	  FROM \"InnReCheck\" R";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "							 AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,\"NewFacmNo\"";
		sql += "					   ,MAX(\"RenewCode\") AS \"RenewCode\"";
		sql += "				 FROM \"AcLoanRenew\"";
		sql += "				 GROUP BY \"CustNo\",\"NewFacmNo\") RC";
		sql += "	    ON RC.\"CustNo\" = R.\"CustNo\" ";
		sql += "	   AND RC.\"NewFacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,SUM(\"LoanBal\") AS \"tLoanBal\"";
		sql += "				 FROM \"InnReCheck\"";
		sql += "	  			 WHERE \"YearMonth\" = :yyyymm";
		sql += "				   AND \"ConditionCode\" = 5";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 5";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYear+String.format("%02d",iMonth));
		return this.convertToMap(query.getResultList());
	}

}