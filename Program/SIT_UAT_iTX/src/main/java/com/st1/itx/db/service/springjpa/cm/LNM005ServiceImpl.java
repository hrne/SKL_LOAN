package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lNM005ServiceImpl")
@Repository

/*
 * LNM005 清單5：會計帳
 */

public class LNM005ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("----------- LNM005.findAll ---------------");
		this.info("-----LNM005 TitaVo=" + titaVo);
		this.info("-----LNM005 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單5：會計帳
		sql = "SELECT \"DataYM\"" + "     , \"AcCode\"" + "     , SUM(\"LoanBal\") as \"LoanBal\"" + "     , \"AcBookCode\"" + " FROM \"Ias34Ap\"" + " WHERE \"DataYM\" = " + dateMonth
				+ " GROUP BY \"DataYM\", \"AcCode\", \"AcBookCode\"" + " ORDER BY \"DataYM\", \"AcCode\", \"AcBookCode\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM005.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
