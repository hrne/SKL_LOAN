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

@Service("l7201ServiceImpl")
@Repository
/* L7201: IFRS9 表外放款承諾資料產出 */
public class L7201ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7201ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(int fg) throws Exception {
		logger.info("L7201.findAll ");
		String sql = "";

		if (fg == 1) {
			sql = "SELECT M.\"CustNo\"" + "     , M.\"FacmNo\"" + "     , M.\"ApplNo\"" + "     , M.\"ApproveDate\"" + "     , M.\"FirstDrawdownDate\"" + "     , M.\"MaturityDate\""
					+ "     , M.\"LoanTermYy\"" + "     , M.\"LoanTermMm\"" + "     , M.\"LoanTermDd\"" + "     , M.\"UtilDeadline\"" + "     , M.\"RecycleDeadline\"" + "     , M.\"LineAmt\""
					+ "     , M.\"UtilBal\"" + "     , M.\"AvblBal\"" + "     , M.\"RecycleCode\"" + "     , M.\"IrrevocableFlag\"" + "     , CASE "
					+ "         WHEN M.\"AcBookCode\" IN ('201') THEN 3" + "         ELSE 1" + "       END                     AS \"AcBookCode\"  " + "     , M.\"Ccf\"" + "     , M.\"ExpLimitAmt\""
					+ "     , M.\"DbAcNoCode\"" + "     , M.\"CrAcNoCode\"" + "     , 1                       AS \"FixData1\"" + " FROM  \"Ias39LoanCommit\" M" + " WHERE M.\"DataYm\" = 202005"
					+ "   AND M.\"DrawdownFg\" = 0" + " ORDER BY M.\"CustNo\", M.\"FacmNo\", M.\"ApplNo\"";
		}

		if (fg == 2) {
			sql = "SELECT M.\"CustNo\"" + "     , M.\"FacmNo\"" + "     , M.\"ApplNo\"" + "     , M.\"ApproveDate\"" + "     , M.\"FirstDrawdownDate\"" + "     , M.\"MaturityDate\""
					+ "     , M.\"LoanTermYy\"" + "     , M.\"LoanTermMm\"" + "     , M.\"LoanTermDd\"" + "     , M.\"UtilDeadline\"" + "     , M.\"RecycleDeadline\"" + "     , M.\"LineAmt\""
					+ "     , M.\"UtilBal\"" + "     , M.\"AvblBal\"" + "     , M.\"RecycleCode\"" + "     , M.\"IrrevocableFlag\"" + "     , CASE "
					+ "         WHEN M.\"AcBookCode\" IN ('201') THEN 3" + "         ELSE 1" + "       END                     AS \"AcBookCode\"  " + "     , M.\"Ccf\"" + "     , M.\"ExpLimitAmt\""
					+ "     , 1                       AS \"FixData1\"" + " FROM  \"Ias39LoanCommit\" M" + " WHERE M.\"DataYm\" = 202005" + "   AND M.\"DrawdownFg\" = 1"
					+ " ORDER BY M.\"CustNo\", M.\"FacmNo\", M.\"ApplNo\"";
		}

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7201Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}