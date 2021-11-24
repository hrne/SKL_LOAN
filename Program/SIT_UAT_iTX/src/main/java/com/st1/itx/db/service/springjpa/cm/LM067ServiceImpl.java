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

public class LM067ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

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

		this.info("lM067.findAll iYear=" + iYear + ",iMonth=" + iMonth);

		String sql = "WITH \"TEMP\" AS (SELECT \"CustNo\"";
		sql += "							   ,\"FacmNo\"";
		sql += "					  	   	   ,MAX(\"DrawdownDate\") AS \"DrawdownDate\"";
		sql += "						FROM \"InnReCheck\"";
		sql += "						WHERE \"YearMonth\" = :yyyymm ";
		sql += "						GROUP BY \"CustNo\"";
		sql += "								,\"FacmNo\")";
		sql += "	 SELECT  R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,C.\"CustName\" AS F2";
		sql += "			,R.\"CustTypeItem\" AS F3";
		sql += "			,R.\"UsageItem\" AS F4";
		sql += "			,CI.\"CityItem\" AS F5";
		sql += "			,MOD(R.\"ReChkYearMonth\",100) AS F6";
		sql += "			,TEMP.\"DrawdownDate\" AS F7";
		sql += "			,R.\"LoanBal\" AS F8";
		sql += "			,R2.\"tLoanBal\" AS F9";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F10";
		sql += "			,DECODE(R.\"FollowMark\",'1','免','2','V','3','X',' ') AS F11";
		sql += "			,R.\"ReChkUnit\" AS F12";
		sql += "			,R.\"Evaluation\" AS F13";
		sql += "			,R.\"Remark\" AS F14";
		sql += "	  FROM \"InnReCheck\" R";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						AND FM.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" ='UsageCode'";
		sql += "	  						 AND CC.\"Code\" = CONCAT('0',fm.\"UsageCode\")";
		sql += "	  LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						AND CF.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  						AND CF.\"MainFlag\" ='Y'";
		sql += "	  LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "	  						AND CM.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "	  						AND CM.\"ClNo\" = CF.\"ClNo\"";
		sql += "	  LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "	  						AND CL.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "	  						AND CL.\"ClNo\" = CM.\"ClNo\"";
		sql += "	  LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\"";
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
		sql += "				   AND \"ConditionCode\" = 6";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  LEFT JOIN \"TEMP\" TEMP ON TEMP.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						 AND TEMP.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 6";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYear + String.format("%02d", iMonth));
		return this.convertToMap(query);

	}

}