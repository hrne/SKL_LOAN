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

@Service
@Repository
/* 逾期放款明細 */
public class LM063ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("lM063.findAll iYearMonth=" + iYearMonth);

		String sql = "SELECT R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F2";
		sql += "			,R.\"ReChkYearMonth\" AS F3";
		sql += "			,R.\"DrawdownDate\" AS F4";
		sql += "			,R.\"LoanBal\" AS F5";
		sql += "			,R2.\"tLoanBal\" AS F6";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F7";
		sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS F8";
		sql += "			,R.\"Evaluation\" AS F9";
		sql += "	  FROM \"InnReCheck\" R";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
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
		sql += "				   AND \"ConditionCode\" = 2";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 2";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\",R.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYearMonth);
		return this.convertToMap(query);
	}
}