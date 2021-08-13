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
public class LM022ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM022ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY").toString()) + 19110000) / 100);
		String today = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY").toString()) + 19110000));
		logger.info("lM022.findAll ");

		String sql = "SELECT M.\"CustNo\"         AS F0";
		sql += "            ,M.\"FacmNo\"         AS F1";
		sql += "            ,M.\"BormNo\"         AS F2";
		sql += "            ,C.\"CustName\"       AS F3";
		sql += "            ,L.\"LoanBal\"      AS F4";
		sql += "            ,L.\"PrevPayIntDate\"   AS F5";
		sql += "            ,M.\"StoreRate\"      AS F6";
//		sql += "            ,L.\"DueAmt\"    AS F7";
		sql += "            ,T.\"Principal\"    AS F7";
		sql += "            ,C.\"CustId\"         AS F8";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = M.\"BormNo\"";
		sql += "      LEFT JOIN \"LoanBorTx\" T ON T.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND T.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND T.\"BormNo\" = M.\"BormNo\"";
		sql += "                                 AND TRUNC(T.\"IntEndDate\"/100) = :entdy ";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      WHERE M.\"YearMonth\" = :entdy";
		sql += "        AND L.\"MaturityDate\" > :today ";
		sql += "        AND M.\"ProdNo\" IN ('81', '82', '83')";
		sql += "        ORDER BY M.\"CustNo\",L.\"LoanBal\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("today", today);
		return this.convertToMap(query.getResultList());
	}

}