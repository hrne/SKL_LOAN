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

@Service("l7905ServiceImpl")
@Repository
/* IFRS9 資料欄位清單6 */
public class L7905ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7905ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7905.findAll ");

		String sql = "SELECT \"Ias34Ap\".\"CustNo\"" + "     , \"Ias34Ap\".\"CustId\"" + "     , \"Ias34Ap\".\"FacmNo\"" + "     , \"Ias34Ap\".\"ApplNo\"" + "     , \"Ias34Ap\".\"BormNo\""
				+ "     , \"Ias34Ap\".\"CustKind\"" + "     , \"Ias34Ap\".\"Status\"" + "     , \"Ias34Ap\".\"OvduDate\"" + "     , ' '                   AS \"OrgRating\""
				+ "     , ' '                   AS \"OrgModel\"" + "     , ' '                   AS \"FinRating\"" + "     , ' '                   AS \"FinModel\"" + "     , \"Ias34Ap\".\"OvduDays\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 1 AND  CustData.\"CustOvduDays\" >= 90 THEN 1" + "         ELSE 2" + "       END                   AS \"IasLossCond1\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 2 AND  CustData.\"CustOvduDays\" >= 90 THEN 1" + "         ELSE 2" + "       END                   AS \"IasLossCond2\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 1 THEN" + "           CASE " + "             WHEN \"Ias34Ap\".\"Status\" = 2 THEN 1" + "             ELSE 2"
				+ "           END" + "         ELSE 2" + "       END                   AS \"IasLossCond3\"" + "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 2 THEN" + "           CASE "
				+ "             WHEN \"Ias34Ap\".\"Status\" = 2 THEN 1" + "             ELSE 2" + "           END" + "         ELSE 2" + "       END                   AS \"IasLossCond4\""
				+ "     , 0                     AS \"IasLossCond5\"" + "     , 0                     AS \"PDtoD\"" + " FROM \"Ias34Ap\""
				+ "      LEFT JOIN ( SELECT A.\"CustNo\"          AS \"CustNo\"" + "                       , MAX(A.\"OvduDays\")   AS \"CustOvduDays\"" + "                  FROM  \"Ias34Ap\" A"
				+ "                  GROUP BY A.\"CustNo\"" + "                ) CustData   ON  CustData.\"CustNo\" = \"Ias34Ap\".\"CustNo\"" + " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7905Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}