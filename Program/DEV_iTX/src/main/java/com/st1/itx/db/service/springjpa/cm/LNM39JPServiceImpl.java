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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lNM39JPServiceImpl")
@Repository

/*
 * LNM39JP 清單7：(LNFJP 契約變更(無實際撥款)之累積案件)
 */

public class LNM39JPServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LNM39JPServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		logger.info("----------- LNM39JP.findAll ---------------");
		logger.info("-----LNM39JP TitaVo=" + titaVo);
		logger.info("-----LNM39JP Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		logger.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單7
		sql = "SELECT " + "  \"AcDateYM\", \"CustNo\", "
				+ " \"NewFacmNo\", \"NewBormNo\", \"OldFacmNo\", \"OldBormNo\" " 
		        + " FROM  \"LoanIfrsJp\" "
		        + " WHERE \"DataYM\" = " + dateMonth
		        + " ORDER BY \"AcDateYM\", \"CustNo\", \"NewFacmNo\", \"NewBormNo\", \"OldFacmNo\", \"OldBormNo\" ";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM39JP.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
