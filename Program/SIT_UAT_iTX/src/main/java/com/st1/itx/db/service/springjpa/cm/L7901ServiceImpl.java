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

@Service("l7901ServiceImpl")
@Repository
/* IFRS9 清單1~3 */
public class L7901ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7901ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(String strSql) throws Exception {
		logger.info("L7901.findAll ");

		String sql = strSql;

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7901Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());

	}
}