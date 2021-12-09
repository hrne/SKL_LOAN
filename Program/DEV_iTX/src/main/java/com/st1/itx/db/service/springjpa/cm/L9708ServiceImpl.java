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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l9708ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9708ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L9708.findAll");

		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);

		String sql = "";
		sql += "   	   SELECT (CASE";
		sql += "					WHEN B.\"RepayBank\" = '812' THEN '1' ";
		sql += "					WHEN B.\"RepayBank\" = '006' THEN '2' ";
		sql += "					WHEN B.\"RepayBank\" = '700' THEN '3' ";
		sql += "					WHEN B.\"RepayBank\" = '103' THEN '4' ";
		sql += "				   ELSE '0' END ) F0";
		sql += "               , M.\"DrawdownDate\" F1";
		sql += "               , M.\"CustNo\" F2";
		sql += "               , M.\"FacmNo\" F3";
		sql += "               , M.\"FirstDueDate\" F4";
		sql += "               , B.\"RepayAcct\" F5";
		sql += "               , \"Fn_ParseEOL\"(C.\"CustName\",0) F6 ";
		sql += "        FROM \"LoanBorMain\" M";
		sql += "        LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                               AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "        LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = M.\"CustNo\"";
		sql += "        LEFT JOIN \"BankAuthAct\" B ON B.\"CustNo\" = M.\"CustNo\"";
		sql += "                                   AND B.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                   AND B.\"AuthType\" <> '02' ";
		sql += "        WHERE M.\"DrawdownDate\" = :iday ";
		sql += "          AND M.\"BormNo\" = 1 ";
		sql += "          AND F.\"RepayCode\" IN (1, 2, 3)";
		sql += "        ORDER BY \"F0\",\"F2\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iDAY);
		return this.convertToMap(query.getResultList());
	}

}