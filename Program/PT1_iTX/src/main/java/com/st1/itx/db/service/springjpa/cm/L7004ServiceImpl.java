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

@Service("l7004ServiceImpl")
@Repository
/* LNM34DP 清單4 */
public class L7004ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7004ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7004.findAll ");

		String sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"BormNo\", \"AcCode\", " + "\"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"MaturityDate\", "
				+ "\"LineAmt\", \"DrawdownAmt\", \"LoanBal\", \"IntAmt\", \"Fee\", " + "\"OvduDays\", \"OvduDate\", \"BadDebtDate\", \"BadDebtAmt\", \"DerDate\", "
				+ "\"DerRate\", \"DerLoanBal\", \"DerIntAmt\", \"DerFee\", " + "\"DerY1Amt\", \"DerY2Amt\", \"DerY3Amt\", \"DerY4Amt\", \"DerY5Amt\", "
				+ "\"DerY1Int\", \"DerY2Int\", \"DerY3Int\", \"DerY4Int\", \"DerY5Int\", " + "\"DerY1Fee\", \"DerY2Fee\", \"DerY3Fee\", \"DerY4Fee\", \"DerY5Fee\", "
				+ "\"IndustryCode\", \"ClKindCode\", \"AreaCode\", \"ProdRateCode\", \"CustKind\", \"ProdNo\" " + " FROM  \"Ias34Dp\" " + " WHERE \"DataYM\" = 202005"
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7004Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}