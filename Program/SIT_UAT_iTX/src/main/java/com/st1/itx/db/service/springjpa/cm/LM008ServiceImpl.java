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

/**
 * LM008-應收利息明細表
 * 
 * @author ST1-Wei
 * @version 1.0.0
 */
@Service
@Repository
public class LM008ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM008ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAcctCodeGroup(TitaVo titaVo) throws Exception {
		logger.info("lM008.findAcctCodeGroup ");

		String bcYearMonth = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

		String sql = "SELECT \"AcctCode\"";
		sql += "              ,COUNT(*) AS \"Cnts\"";
		sql += "        FROM ( SELECT A.\"AcctCode\"";
		sql += "               FROM \"AcLoanInt\" A";
		sql += "               LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = A.\"CustNo\"";
		sql += "               WHERE A.\"YearMonth\" = :bcYearMonth )";
		sql += "        GROUP BY \"AcctCode\"";
		sql += "        ORDER BY \"AcctCode\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("bcYearMonth", bcYearMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findDetail(String acctCode, TitaVo titaVo) throws Exception {
		logger.info("lM008.findDetail acctCode = " + acctCode);

		String bcDate = String.valueOf(Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000);
		String bcYearMonth = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

		String sql = "SELECT \"AcctCode\" AS F0";
		sql += "              , \"Aging\" AS F1";
		sql += "              , \"CustNo\" AS F2";
		sql += "              , \"FacmNo\" AS F3";
		sql += "              , \"CustName\" AS F4";
		sql += "              , SUM(\"LoanBal\") AS F5";
		sql += "              , MAX(\"IntRate\") AS F6";
		sql += "              , MIN(\"IntStartDate\") AS F7";
		sql += "              , SUM(\"INT\") AS F8";
		sql += "              , SUM(\"OINT\") AS F9";
		sql += "        FROM ( SELECT A.\"AcctCode\"";
		sql += "                    , A.\"Aging\"";
		sql += "                    , A.\"CustNo\"";
		sql += "                    , C.\"CustName\"";
		sql += "                    , A.\"FacmNo\"";
		sql += "                    , A.\"LoanBal\"";
		sql += "                    , A.\"IntRate\"";
		sql += "                    , A.\"IntStartDate\"";
		sql += "                    , CASE WHEN A.\"PayIntDate\" <= :bcDate THEN A.\"Interest\"";
		sql += "                      ELSE 0 END INT";
		sql += "                    , CASE WHEN A.\"PayIntDate\" > :bcDate THEN A.\"Interest\"";
		sql += "                      ELSE 0 END OINT";
		sql += "               FROM \"AcLoanInt\" A";
		sql += "               LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = A.\"CustNo\"";
		sql += "               WHERE A.\"YearMonth\" = :bcYearMonth ";
		sql += "                 AND A.\"AcctCode\" = :acctCode";
//		sql += "                 AND A.\"IntStartDate\" <> 0";
		sql += " ) S1";
		sql += "        GROUP BY \"AcctCode\", \"Aging\", \"CustNo\", \"CustName\", \"FacmNo\"";
		sql += "        ORDER BY F0, F1, F2, F3";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acctCode", acctCode);
		query.setParameter("bcDate", bcDate);
		query.setParameter("bcYearMonth", bcYearMonth);
		return this.convertToMap(query.getResultList());
	}

}