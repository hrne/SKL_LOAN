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

@Service("l7903ServiceImpl")
@Repository
/* IFRS9 資料欄位清單5 */
public class L7903ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7903ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7903.findAll ");

		String sql = "SELECT \"DataYM\"" + "     , \"AcCode\"" + "     , SUM(\"LoanBal\") as \"LoanBal\"" + "     , \"AcBookCode\"" + " FROM \"Ias34Ap\"" + " WHERE \"DataYM\" = 202005"
				+ " GROUP BY \"DataYM\", \"AcCode\", \"AcBookCode\"" + " ORDER BY \"DataYM\", \"AcCode\", \"AcBookCode\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7903Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}