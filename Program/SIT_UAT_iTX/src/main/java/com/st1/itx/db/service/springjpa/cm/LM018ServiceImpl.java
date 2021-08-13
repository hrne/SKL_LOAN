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
public class LM018ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM018ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM018.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);
		String sql = "SELECT \"F1\"";
		sql += "              , \"F2\"";
		sql += "              , SUM(F3)";
		sql += "              , SUM(F4)";
		sql += "        FROM ( SELECT DECODE(M.\"ProdNo\", 'IE', 'ID', 'IG', 'IF', 'II', 'IH', '81'";
		sql += "                                         , 'ZZ', '82', 'ZZ', '83', 'ZZ', M.\"ProdNo\") AS F1";
		sql += "                    , M.\"YearMonth\" AS F2";
		sql += "                    , M.\"LoanBalance\" AS F3";
		sql += "                    , M.\"IntAmtRcv\" AS F4";
		sql += "               FROM \"MonthlyLoanBal\" M WHERE M.\"YearMonth\" = :entdy";
		sql += "                                           AND M.\"AcctCode\" IN ('310', '320', '330')";
		sql += "                                           AND M.\"ProdNo\"   IN ('IA', 'IB', 'IC', 'ID', 'IE', 'IF'";
		sql += "                                                                , 'IG', 'IH', 'II', '81', '82', '83')";
		sql += "               UNION ALL";
		sql += "               SELECT 'AA' AS F1";
		sql += "                     , M.\"YearMonth\" AS F2";
		sql += "                     , M.\"LoanBalance\" AS F3";
		sql += "                     , M.\"IntAmtRcv\" AS F4";
		sql += "               FROM \"MonthlyLoanBal\" M";
		sql += "               WHERE M.\"YearMonth\" = :entdy";
		sql += "                 AND  M.\"AcctCode\" = '340')";
		sql += "        GROUP BY \"F1\", \"F2\"";
		sql += "        ORDER BY \"F1\", \"F2\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}