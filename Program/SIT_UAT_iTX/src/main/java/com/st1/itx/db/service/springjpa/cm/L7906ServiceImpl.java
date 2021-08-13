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

@Service("l7906ServiceImpl")
@Repository
/* IFRS9 資料欄位清單6 */
public class L7906ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7906ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7906.findAll ");

		String sql = "SELECT F.\"CustNo\"" + "     , F.\"CustId\"" + "     , F.\"FacmNo\"" + "     , F.\"ApplNo\"" + "     , CASE" + "         WHEN F.\"EntCode\" = '0' THEN '2'"
				+ "         WHEN F.\"EntCode\" = '1' THEN '1'" + "         WHEN F.\"EntCode\" = '2' THEN '1'" + "         ELSE '2'" + "       END                   AS \"EntCode\"" + "     , CASE"
				+ "         WHEN F.\"ApproveDate\" = 0 THEN ''" + "         ELSE to_char(F.\"ApproveDate\")" + "       END                   AS \"ApproveDate\"" + "     , CASE"
				+ "         WHEN F.\"FirstDrawdownDate\" = 0 THEN ''" + "         ELSE to_char(F.\"FirstDrawdownDate\")" + "       END                   AS \"FirstDrawdownDate\"" + "     , CASE"
				+ "         WHEN F.\"CurrencyCode\" = 'TWD' THEN F.\"LineAmt\"" + "         ELSE 0" + "       END                   AS \"LineAmt\"" + "     , F.\"ProdNo\"" + "     , CASE"
				+ "         WHEN F.\"CurrencyCode\" = 'TWD' THEN" + "           CASE WHEN F.\"LineAmt\" > F.\"UtilBal\" THEN ( F.\"LineAmt\" - F.\"UtilBal\" )" + "                ELSE 0"
				+ "           END" + "         ELSE 0" + "       END                   AS \"UsableBal\"" + "     , F.\"RecycleCode\"" + "     , F.\"IrrevocableFlag\"" + "     , F.\"IndustryCode\""
				+ "     , ''                    AS \"OrgRating\"" + "     , ''                    AS \"OrgModel\"" + "     , ''                    AS \"FinRating\""
				+ "     , ''                    AS \"FinModel\"" + "     , ''                    AS \"PDModel\"" + "     , ''                    AS \"PD\""
				+ "     , F.\"LineAmt\"           AS \"CurrLineAmt\"" + "     , CASE" + "         WHEN F.\"LineAmt\" > F.\"UtilBal\" THEN ( F.\"LineAmt\" - F.\"UtilBal\" )" + "         ELSE 0"
				+ "       END                   AS \"CurrUsableBal\"" + " FROM  \"Ifrs9FacData\" F" + " WHERE F.\"DataYM\" = 202005" + "   AND F.\"Status\" IN (0, 2, 4, 6)"
				+ " ORDER BY \"CustNo\", \"FacmNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7906Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}