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

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lNM009ServiceImpl")
@Repository

/*
 * LNM009 清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用
 */

public class LNM009ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM009ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM009.findAll ---------------");
		logger.info("-----LNM009 TitaVo=" + titaVo);
		logger.info("-----LNM009 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單9：表外放款與應收帳款-資產基本資料與計算原始有效利率用
		sql = "SELECT A.\"CustNo\"" + "     , A.\"CustId\"" + "     , A.\"FacmNo\"" + "     , A.\"ApplNo\"" + "     , F.\"ApproveDate\"" + "     , A.\"FirstDrawdownDate\"" + "     , A.\"LineAmt\""
				+ "     , A.\"AcctFee\"" + "     , A.\"Fee\"" + "     , A.\"ApproveRate\"" + "     , A.\"GracePeriod\"" + "     , A.\"AmortizedCode\"" + "     , A.\"RateCode\""
				+ "     , A.\"RepayFreq\"" + "     , A.\"PayIntFreq\"" + "     , A.\"IndustryCode\"" + "     , A.\"ClTypeJCIC\"" + "     , CASE " + "         WHEN A.\"CityCode\" = '05' THEN 'A'"
				+ "         WHEN A.\"CityCode\" = '10' THEN 'B'" + "         WHEN A.\"CityCode\" = '15' THEN 'C'" + "         WHEN A.\"CityCode\" = '35' THEN 'D'"
				+ "         WHEN A.\"CityCode\" = '65' THEN 'E'" + "         WHEN A.\"CityCode\" = '70' THEN 'F'" + "         ELSE 'G'  " + "       END                   AS  \"CityCode\""
				+ "     , A.\"BaseRateCode\"" + "     , A.\"CustKind\"" + "     , A.\"ProdNo\"" + "     , A.\"EvaAmt\"" + "     , CASE"
				+ "         WHEN NVL(A.\"LineAmt\",0) > NVL(A.\"UtilBal\",0) THEN NVL(A.\"LineAmt\",0) - NVL(A.\"UtilBal\",0) " + "         ELSE 0 " + "       END                   AS  \"txAvblBal\""
				+ "     , A.\"RecycleCode\"" + "     , A.\"IrrevocableFlag\"" + "     , A.\"FacLineDate\"" + "     , A.\"AcCode\"" + "     , A.\"AcCurcd\"" + "     , A.\"AcBookCode\""
				+ "     , A.\"CurrencyCode\"" + "     , A.\"ExchangeRate\"" + "     , A.\"LineAmt\"         AS  \"txLineAmt\"" + "     , A.\"AcctFee\"         AS  \"txAcctFee\""
				+ "     , A.\"Fee\"             AS  \"txFee\"" + " FROM ( SELECT DISTINCT \"DataYM\", \"CustNo\", \"FacmNo\""
				+ "                      , FIRST_VALUE(\"BormNo\") OVER (PARTITION BY \"CustNo\", \"FacmNo\"  ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\") AS \"BormNo\"" + "        FROM   \"Ias34Ap\""
				+ "        WHERE  \"DataYM\" = " + dateMonth + "        ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"" + "      ) Acct"
				+ "   LEFT JOIN \"Ias34Ap\" A  ON  A.\"DataYM\" = Acct.\"DataYM\" " + "                           AND  A.\"CustNo\" = Acct.\"CustNo\" "
				+ "                           AND  A.\"FacmNo\" = Acct.\"FacmNo\" " + "                           AND  A.\"BormNo\" = Acct.\"BormNo\" "
				+ "   LEFT JOIN \"Ias39Loan34Data\" F  ON  F.\"DataYM\" = A.\"DataYM\" " + "                                   AND  F.\"CustNo\" = A.\"CustNo\" "
				+ "                                   AND  F.\"FacmNo\" = A.\"FacmNo\" " + "                                   AND  F.\"ApplNo\" = A.\"ApplNo\" "
				+ "                                   AND  F.\"BormNo\" = A.\"BormNo\" " + " ORDER BY A.\"CustNo\", A.\"FacmNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM009.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
