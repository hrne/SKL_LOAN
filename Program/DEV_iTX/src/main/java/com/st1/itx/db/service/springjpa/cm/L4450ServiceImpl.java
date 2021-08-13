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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l4450ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4450ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4450ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	String sql = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int entryDate = Integer.valueOf(titaVo.get("EntryDate")) + 19110000;

		logger.info("l4450.findAll EntryDate=" + entryDate);

		sql = " SELECT  \"EntryDate\"    AS F0            ";
		sql += "       , \"CustNo\"      AS F1            ";
		sql += "       , \"FacmNo\"      AS F2            ";
		sql += "       , \"BormNo\"      AS F3            ";
		sql += "       , \"PrevIntDate\" AS F4            ";
		sql += "       , \"PayIntDate\"  AS F5            ";
		sql += "       , \"RepayType\"   AS F6            ";
		sql += "       , \"UnpaidAmt\"   AS F7            ";
		sql += "       , \"TempAmt\"     AS F8            ";
		sql += "       , \"RepayAmt\"    AS F9            ";
		sql += "       , \"MediaCode\"   AS F10           ";
		sql += "       , \"AcDate\"      AS F11           ";
		sql += "       , \"RepayBank\"   AS F12           ";
		sql += "       , \"JsonFields\"  AS F13           ";
		sql += " FROM \"BankDeductDtl\" BD                ";
		sql += " WHERE \"EntryDate\" = " + entryDate;
		sql += " ORDER BY \"RepayBank\", \"RepayType\" DESC, \"CustNo\", \"FacmNo\", \"BormNo\", \"PayIntDate\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}

}