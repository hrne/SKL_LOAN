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

@Service("L5R32ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L5R32ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		int acDate = Integer.valueOf(titaVo.get("RimAcDate")) + 19110000;

		this.info("L5R32.findAll AcDate=" + acDate);

//		sql = " SELECT SUM(\"TdBal\")    AS F0                         ";
		sql = " SELECT  \"TdBal\"        AS F0                         ";
//		陣列取法需多筆
		sql += "        ,''               AS F1                        ";
		sql += " FROM  \"AcMain\" BD                                   ";
		sql += " WHERE \"AcDate\" = " + acDate;
		sql += "   AND \"AcctCode\" in ('310','320','330','340','990') ";
		sql += "   AND \"AcBookCode\" = '000'                          ";
//		sql += "   GROUP BY \"AcDate\"                                 ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}

}