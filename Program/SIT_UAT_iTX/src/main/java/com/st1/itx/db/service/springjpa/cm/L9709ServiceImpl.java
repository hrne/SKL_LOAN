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

	public List<Map<String, String>> findAll(TitaVo titaVo) {

		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);

		this.info("L9709ServiceImpl findAll iDAY = " + iDAY);

		String sql = "  SELECT   A.\"AcNoCode\" F0";
		sql += "              , SUM(A.\"DbAmt\") F1";
		sql += "              , SUM(A.\"CrAmt\") F2";
		sql += "              , SUM(A.\"TdBal\") F3";
		sql += "         FROM   \"AcMain\" A";
		sql += "         WHERE A.\"AcDate\"      = :iday ";
		sql += "           AND A.\"AcNoCode\" IN ('20222180000', '20222180100' , '20222180200')";
		sql += "         GROUP BY A.\"AcNoCode\"";
		sql += "         ORDER BY A.\"AcNoCode\"";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iDAY);
		return this.convertToMap(query);
	}

}