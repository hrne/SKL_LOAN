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

@Service("lNM006ServiceImpl")
@Repository

/*
 * LNM006 清單6：放款與應收帳款-協商戶用
 */

public class LNM006ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM006ServiceImpl.class);

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

		logger.info("----------- LNM006.findAll ---------------");
		logger.info("-----LNM006 TitaVo=" + titaVo);
		logger.info("-----LNM006 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202005;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單6：放款與應收帳款-協商戶用
		sql = "SELECT \"CustNo\"" + "     , \"CustId\"" + "     , \"AgreeNo\"" + "     , \"AgreeFg\"" + "     , \"FacmNo\"" + "     , \"BormNo\"" + " FROM  \"Ias34Gp\"" + " WHERE \"DataYM\" = "
				+ dateMonth + " ORDER BY \"CustNo\", \"AgreeNo\", \"AgreeFg\", \"FacmNo\", \"BormNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM006.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
