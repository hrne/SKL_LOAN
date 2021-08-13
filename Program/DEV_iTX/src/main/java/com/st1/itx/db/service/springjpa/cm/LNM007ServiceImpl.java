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

@Service("lNM007ServiceImpl")
@Repository

/*
 * LNM007 清單7：放款與應收帳款-stage轉換用
 */

public class LNM007ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM007ServiceImpl.class);

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

		logger.info("----------- LNM007.findAll ---------------");
		logger.info("-----LNM007 TitaVo=" + titaVo);
		logger.info("-----LNM007 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202005;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單7：放款與應收帳款-stage轉換用
		sql = "SELECT \"Ias34Ap\".\"CustNo\"" + "     , \"Ias34Ap\".\"CustId\"" + "     , \"Ias34Ap\".\"FacmNo\"" + "     , \"Ias34Ap\".\"ApplNo\"" + "     , \"Ias34Ap\".\"BormNo\""
				+ "     , \"Ias34Ap\".\"CustKind\"" + "     , \"Ias34Ap\".\"Status\"" + "     , \"Ias34Ap\".\"OvduDate\"" + "     , ' '                   AS \"OrgRating\""
				+ "     , ' '                   AS \"OrgModel\"" + "     , ' '                   AS \"FinRating\"" + "     , ' '                   AS \"FinModel\"" + "     , \"Ias34Ap\".\"OvduDays\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 1 AND  CustData.\"CustOvduDays\" >= 90 THEN 1" + "         ELSE 2" + "       END                   AS \"IasLossCond1\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 2 AND  CustData.\"CustOvduDays\" >= 90 THEN 1" + "         ELSE 2" + "       END                   AS \"IasLossCond2\""
				+ "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 1 THEN" + "           CASE " + "             WHEN \"Ias34Ap\".\"Status\" = 2 THEN 1" + "             ELSE 2"
				+ "           END" + "         ELSE 2" + "       END                   AS \"IasLossCond3\"" + "     , CASE " + "         WHEN \"Ias34Ap\".\"CustKind\" = 2 THEN" + "           CASE "
				+ "             WHEN \"Ias34Ap\".\"Status\" = 2 THEN 1" + "             ELSE 2" + "           END" + "         ELSE 2" + "       END                   AS \"IasLossCond4\""
				+ "     , 0                     AS \"IasLossCond5\"" + "     , 0                     AS \"PDtoD\"" + " FROM \"Ias34Ap\""
				+ "      LEFT JOIN ( SELECT A.\"CustNo\"          AS \"CustNo\"" + "                       , MAX(A.\"OvduDays\")   AS \"CustOvduDays\"" + "                  FROM  \"Ias34Ap\" A"
				+ "                  GROUP BY A.\"CustNo\"" + "                ) CustData   ON  CustData.\"CustNo\" = \"Ias34Ap\".\"CustNo\"" + " WHERE \"Ias34Ap\".\"DataYM\" = " + dateMonth
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM007.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
