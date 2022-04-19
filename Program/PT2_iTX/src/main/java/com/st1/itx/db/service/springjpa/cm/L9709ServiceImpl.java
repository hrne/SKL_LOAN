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

@Service
@Repository
public class L9709ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(String startDate, String endDate, TitaVo titaVo) {

		this.info("L9709ServiceImpl findAll startDate = " + startDate);
		this.info("L9709ServiceImpl findAll endDate = " + endDate);

		String sql = " ";
		sql += " SELECT A.\"AcNoCode\"          AS F0";
		sql += "      , SUM(NVL(A.\"DbAmt\",0)) AS F1 ";
		sql += "      , SUM(NVL(A.\"CrAmt\",0)) AS F2 ";
		sql += " FROM   \"AcMain\" A ";
		sql += " WHERE A.\"AcDate\" >= :startDate ";
		sql += "   AND A.\"AcDate\" <= :endDate ";
		sql += "   AND A.\"AcNoCode\" IN ('20222180000', '20222180100', '20222180200') ";
		sql += " GROUP BY A.\"AcNoCode\" ";
		sql += " ORDER BY A.\"AcNoCode\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return this.convertToMap(query);
	}

}