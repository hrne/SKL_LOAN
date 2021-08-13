package com.st1.itx.db.service.springjpa.cm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("checkAuthServiceImpl")
@Repository

public class CheckAuthServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CheckAuthServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(String tlrno, String tranno) throws Exception {
		logger.info("CheckAuthServiceImpl.findAll tlrno = " + tlrno + ", TranNo = " + tranno);

		String sql = "select a.\"TlrNo\",a.\"LevelFg\",a.\"AuthNo\",b.\"AuthFg\",0 \"BeginDate\",0 \"BeginTime\",99991231 \"EndDate\",2400 \"EndTime\" from \"TxTeller\" a "
				+ "left join \"TxAuthority\" b on b.\"AuthNo\" = a.\"AuthNo\" and b.\"TranNo\" = :tranno " + "where a.\"TlrNo\" = :tlrno " + "UNION ALL "
				+ "select c.\"TlrNo\",d.\"LevelFg\",d.\"AuthNo\",e.\"AuthFg\",c.\"BeginDate\",c.\"BeginTime\",c.\"EndDate\",c.\"EndTime\" from \"TxAgent\" c "
				+ "left join \"TxTeller\" d on d.\"TlrNo\" = c.\"TlrNo\" " + "left join \"TxAuthority\" e on e.\"AuthNo\" = d.\"AuthNo\" and e.\"TranNo\" = :tranno "
				+ "where c.\"AgentTlrNo\" = :tlrno and c.\"Status\" = 0 ";
//		;
		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
//		logger.info("CheckAuthServiceImpl.findAll 1");
		query = em.createNativeQuery(sql);
//		logger.info("CheckAuthServiceImpl.findAll 2");
		query.setParameter("tlrno", tlrno);
		query.setParameter("tranno", tranno);

		logger.info("CheckAuthServiceImpl.findAll 3");

		return this.convertToMap(query.getResultList());
	}

}
