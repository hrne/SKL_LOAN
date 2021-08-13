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
public class LM012ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM012ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM012.findAll ");
		String rateMinimum = titaVo.getParam("RateMinimum");
		String rateMaximum = titaVo.getParam("RateMaximum");
		String rateRange = titaVo.getParam("RateRange");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("DataYear")) + 1911)) + titaVo.getParam("DataMonth");

		String sql = "SELECT \"F0\"";
		sql += "              ,SUM(F1) AS F1";
		sql += "              ,SUM(F2) AS F2";
		sql += "              ,SUM(F3) AS F3";
		sql += "              ,SUM(F4) AS F4";
		sql += "      FROM (SELECT CASE WHEN D.\"StoreRate\" < :rateMinimum THEN -1";
		sql += "                        WHEN D.\"StoreRate\" >=  :rateMaximum THEN 999";
		sql += "                   ELSE TRUNC(D.\"StoreRate\" / :rateRange) * :rateRange END AS F0";
		sql += "                  ,CASE WHEN D.\"AcctCode\" = '310' THEN D.\"LoanBalance\""; 
		sql += "                   ELSE 0 END F1";
		sql += "                  ,CASE WHEN D.\"AcctCode\" = '320' THEN D.\"LoanBalance\""; 
		sql += "                   ELSE 0 END F2";
		sql += "                  ,CASE WHEN D.\"AcctCode\" = '330' THEN D.\"LoanBalance\""; 
		sql += "                   ELSE 0 END F3";
		sql += "                  ,CASE WHEN D.\"AcctCode\" = '340' THEN D.\"LoanBalance\""; 
		sql += "                   ELSE 0 END F4";
		sql += "            FROM \"DailyLoanBal\" D"; 
		sql += "            WHERE D.\"MonthEndYm\" = :entdy";
		sql += "              AND D.\"AcctCode\" IN ('310', '320', '330', '340'))";
		sql += "      GROUP BY \"F0\"";
		sql += "      ORDER BY \"F0\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("rateMinimum", rateMinimum);
		query.setParameter("rateMaximum", rateMaximum);
		query.setParameter("rateRange", rateRange);
		return this.convertToMap(query.getResultList());
	}

}