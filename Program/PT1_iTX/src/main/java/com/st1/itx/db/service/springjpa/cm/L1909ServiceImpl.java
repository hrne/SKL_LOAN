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

@Service("l1909ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L1909ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L1909.findAll");

		String iRepCusName = "%"+titaVo.get("repCustName").trim()+"%";
		this.info("repCustName    ="   + iRepCusName);
		String sql = " select ";
		sql += "             \"RepCusName\", ";
		sql += "             \"CustId\",  ";
		sql += "             \"CustName\", ";
		sql += "             \"SubCom\",  ";
		sql += "             \"LastUpdate\" ";
		sql += "       from \"BankRelationSuspected\"  ";
		
		sql += "       where \"RepCusName\" Like '"+iRepCusName+"'  ";

		this.info("L1909 Simplsql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);


		return this.convertToMap(query);
	}

}