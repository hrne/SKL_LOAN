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

@Service("lM065ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LM065ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		String iYearMonthL5 = "";

		// 此表根據樣張明細備註有取五個月前的撥款日
		if (iMonth - 5 < 0) {
			iYearMonthL5 = String.valueOf(((iYear - 1) * 100) + (12 + (iMonth - 5)));
		} else {
			iYearMonthL5 = String.valueOf((iYear * 100) + (iMonth - 5));
		}

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("lM065.findAll");
		this.info("iYearMonth=" + iYearMonth);
		this.info("iYearMonthL5=" + iYearMonthL5);

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
		sql += "			 AND TRUNC(LBM.\"DrawdownDate\"/100) = :l5yymm ) S0";
		sql += "	  LEFT JOIN(SELECT R.\"CustNo\" \"CustNo\"";
		sql += "					  ,R.\"FacmNo\" \"FacmNo\"";
		sql += "				  	  ,\"Fn_ParseEOL\"(C.\"CustName\",0) \"CustName\"";
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
		query.setParameter("l5yymm", iYearMonthL5);
		query.setParameter("yyyymm", iYearMonth);
		return this.convertToMap(query);
	}

}