package com.st1.itx.db.service.springjpa.cm;

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
public class LM062ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM062ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		// 2021XX
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = Integer.valueOf(String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000).substring(4, 6));
		if (iMonth == 1) {
			iYear = iYear - 1;
			iMonth = 12;
		} else {
			iMonth = iMonth - 1;
		}

		logger.info("lM062.findAll iYear=" + iYear + ",iMonth=" + iMonth);

		String sql = "SELECT R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,C.\"CustName\" AS F2";
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
		sql += "				   AND \"ConditionCode\" = 1";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 1";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\",R.\"FacmNo\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYear+String.format("%02d",iMonth));
		return this.convertToMap(query.getResultList());
	}

}