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

@Service("l7907ServiceImpl")
@Repository
/* IFRS9 資料欄位清單9 */
public class L7907ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7907ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7907.findAll ");

		String sql = "SELECT A.\"CustNo\"" + "     , A.\"CustId\"" + "     , A.\"FacmNo\"" + "     , A.\"ApplNo\"" + "     , A.\"ApproveDate\"" + "     , A.\"FirstDrawdownDate\""
				+ "     , A.\"LineAmt\"" + "     , A.\"AcctFee\"" + "     , A.\"Fee\"" + "     , A.\"ApproveRate\"" + "     , A.\"GracePeriod\"" + "     , A.\"AmortizedCode\""
				+ "     , A.\"RateCode\"" + "     , A.\"RepayFreq\"" + "     , A.\"PayIntFreq\"" + "     , A.\"IndustryCode\"" + "     , A.\"ClTypeJCIC\"" + "     , CASE "
				+ "         WHEN A.\"CityCode\" = '05' THEN 'A'" + "         WHEN A.\"CityCode\" = '10' THEN 'B'" + "         WHEN A.\"CityCode\" = '15' THEN 'C'"
				+ "         WHEN A.\"CityCode\" = '35' THEN 'D'" + "         WHEN A.\"CityCode\" = '65' THEN 'E'" + "         WHEN A.\"CityCode\" = '70' THEN 'F'" + "         ELSE 'G'  "
				+ "       END                   AS  \"CityCode\"" + "     , A.\"BaseRateCode\"" + "     , A.\"CustKind\"" + "     , A.\"ProdNo\"" + "     , A.\"EvaAmt\"" + "     , CASE"
				+ "         WHEN NVL(A.\"LineAmt\",0) > NVL(A.\"UtilBal\",0) THEN NVL(A.\"LineAmt\",0) - NVL(A.\"UtilBal\",0) " + "         ELSE 0 " + "       END                   AS  \"txAvblBal\""
				+ "     , A.\"RecycleCode\"" + "     , A.\"IrrevocableFlag\"" + "     , A.\"FacLineDate\"" + "     , A.\"AcCode\"" + "     , A.\"AcCurcd\"" + "     , A.\"AcBookCode\""
				+ "     , A.\"CurrencyCode\"" + "     , A.\"ExchangeRate\"" + "     , A.\"LineAmt\"         AS  \"txLineAmt\"" + "     , A.\"AcctFee\"         AS  \"txAcctFee\""
				+ "     , A.\"Fee\"             AS  \"txFee\"" + " FROM ( SELECT DISTINCT \"DataYM\", \"CustNo\", \"FacmNo\""
				+ "                      , FIRST_VALUE(\"BormNo\") OVER (PARTITION BY \"CustNo\", \"FacmNo\"  ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\") AS \"BormNo\"" + "        FROM   \"Ias34Ap\""
				+ "        WHERE  \"DataYM\" = 202005" + "        ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"" + "      ) Acct" + "   LEFT JOIN \"Ias34Ap\" A  ON  A.\"DataYM\" = Acct.\"DataYM\""
				+ "                           AND  A.\"CustNo\" = Acct.\"CustNo\" " + "                           AND  A.\"FacmNo\" = Acct.\"FacmNo\" "
				+ "                           AND  A.\"BormNo\" = Acct.\"BormNo\"" + " ORDER BY A.\"CustNo\", A.\"FacmNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7907Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}