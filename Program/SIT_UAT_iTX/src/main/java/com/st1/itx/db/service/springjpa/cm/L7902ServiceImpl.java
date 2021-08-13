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

@Service("l7902ServiceImpl")
@Repository
/* IFRS9 資料欄位清單4 */
public class L7902ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L7902ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll() throws Exception {
		logger.info("L7902.findAll ");

		String sql = "SELECT A.\"CustNo\"" + "     , A.\"CustId\"" + "     , A.\"FacmNo\"" + "     , A.\"BormNo\"" + "     , A.\"AcCode\"" + "     , A.\"Status\"" + "     , A.\"FirstDrawdownDate\""
				+ "     , A.\"DrawdownDate\"" + "     , A.\"MaturityDate\"" + "     , A.\"LineAmt\"" + "     , A.\"DrawdownAmt\"" + "     , A.\"LoanBal\"" + "     , A.\"IntAmt\"" + "     , A.\"Fee\""
				+ "     , A.\"OvduDays\"" + "     , A.\"OvduDate\"" + "     , A.\"BadDebtDate\"" + "     , A.\"BadDebtAmt\"" + "     , A.\"DerDate\"" + "     , A.\"DerRate\""
				+ "     , A.\"DerLoanBal\"" + "     , A.\"DerIntAmt\"" + "     , A.\"DerFee\"" + "     , A.\"DerY1Amt\"" + "     , A.\"DerY2Amt\"" + "     , A.\"DerY3Amt\"" + "     , A.\"DerY4Amt\""
				+ "     , A.\"DerY5Amt\"" + "     , A.\"DerY1Int\"" + "     , A.\"DerY2Int\"" + "     , A.\"DerY3Int\"" + "     , A.\"DerY4Int\"" + "     , A.\"DerY5Int\"" + "     , A.\"DerY1Fee\""
				+ "     , A.\"DerY2Fee\"" + "     , A.\"DerY3Fee\"" + "     , A.\"DerY4Fee\"" + "     , A.\"DerY5Fee\"" + "     , A.\"IndustryCode\"" + "     , A.\"ClKindCode\"" + "     , CASE"
				+ "         WHEN \"CdArea\".\"CityCode\" = '05' THEN 'A'" + "         WHEN \"CdArea\".\"CityCode\" = '10' THEN 'B'" + "         WHEN \"CdArea\".\"CityCode\" = '15' THEN 'C'"
				+ "         WHEN \"CdArea\".\"CityCode\" = '35' THEN 'D'" + "         WHEN \"CdArea\".\"CityCode\" = '65' THEN 'E'" + "         WHEN \"CdArea\".\"CityCode\" = '70' THEN 'F'"
				+ "         ELSE 'G'" + "       END                   AS  \"AreaCode\"" + "     , A.\"ProdRateCode\"" + "     , A.\"CustKind\"" + "     , A.\"ProdNo\"" + " FROM \"Ias34Dp\" A"
				+ "        LEFT JOIN \"CdArea\"  ON \"CdArea\".\"Zip3\"  = A.\"AreaCode\"" + " WHERE A.\"DataYM\"  =  202005" + " ORDER BY A.\"CustNo\", A.\"FacmNo\", A.\"BormNo\"";

		logger.info("sql=" + sql);
		Query query;
//		query = em.createQuery(sql, L7902Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		// 設定參數
//		query.setParameter("defno", defno);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}