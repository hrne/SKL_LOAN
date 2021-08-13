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
public class LM037ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM037ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM037.findAll ");
		String sql = "SELECT DECODE(M.\"EntCode\", '1', '1', '0')          F1";
		sql += "			,NVL (M.\"CityCode\", '0') F2";
		sql += "			,SUM(M.\"LoanBalance\") F3";
		sql += "	  FROM \"MonthlyLoanBal\" M";
		sql += "	  WHERE M.\"YearMonth\" = :entdy";
		sql += "		AND DECODE(M.\"DepartmentCode\", '1', '1', '0') = 0";
		sql += "		AND M.\"AcctCode\" = '990'";
		sql += "		AND M.\"LoanBalance\" > 0";
		sql += " 	  GROUP BY DECODE(M.\"EntCode\", '1', '1', '0')";
		sql += "			  ,M.\"CityCode\"";
		sql += "	  ORDER BY F1";
		sql += "			  ,M.\"CityCode\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}